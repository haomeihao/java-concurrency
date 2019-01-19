[TOC]
## 分布式锁实现
我的博客文章：分布式锁实现[点击查看](https://my.oschina.net/shaochuan/blog/2353168)

#### 一、分布式锁简介
```
1).什么是锁？
答: 狭义来讲，锁是用于线程互斥达到同步效果的一种手段。 比如 JDK的 synchronized 和 Reentrantlock。

2).什么是分布式锁？ 
答: 跨（JVM）进程对共享资源加锁，以保证分布式环境下的数据一致性安全。

3).分布式锁流行的背景？
答: JDK 提供的 synchronized 和 Reentrantlock 只能在单（JVM）进程内对共享资源加锁，无法适应现在流行的分布式架构。
  分布式锁就是为了解决 分布式应用进行逻辑处理时经常会遇到的并发问题，经典场景就是电商领域的秒杀。

4).分布式锁常见的实现方案？
答: 根据互联网上的资料总结，目前最流行的是基于 Redis 实现，还有 基于 Zookeeper 实现。

5).为何需要分布式锁？
  Martin Kleppmann 是英国剑桥大学的分布式系统的研究员，之前和 Redis 之父 Antirez 进行过关于 RedLock(红锁，后续有讲到)是否安全的激烈讨论。
  Martin 认为一般我们使用分布式锁有两个场景：
  效率： 使用分布式锁可以避免不同节点重复相同的工作，这些工作会浪费资源。 
  正确性： 加分布式锁同样可以避免破坏正确性的发生，如果两个节点在同一条数据上面操作，比如多个节点机器对同一个订单操作不同的流程有可能会导致该笔订单最后状态出现错误，造成损失。 ([高]并发下的商品超卖现象)
```

#### 二、Redis实现分布式锁

##### 2.1.Redis简介
```
Redis 是互联网技术领域使用最为广泛的存储中间件，它是「Remote Dictionary Service」的首字母缩写，也就是「远程字典服务」。
Redis 以其超高的性能、完美的文档、简洁易懂的源码和丰富的客户端库支持在开源中间件领域广受好评。
国内外很多大型互联网公司都在使用 Redis，比如 Twitter、YouPorn、暴雪娱乐、Github、StackOverflow、腾讯、阿里、京东、华为、新浪微博等等，很多中小型公司也都有应用。
也可以说，对 Redis 的了解和应用实践已成为当下中高级后端开发者绕不开的必备技能。


Redis 关键词: C语言编写、单进程单线程模型、多路复用I/O模型（非阻塞IO）、所有操作都是原子性的
  超高性能、可基于内存也可持久化、key-value数据库、支持主从同步。

  进程线程模型: 
    单进程多线程模型：MySQL、Memcached、Oracle（Windows版本）;
    多进程模型：Oracle（Linux版本）;
    Nginx有两类进程，一类称为Master进程(相当于管理进程)，另一类称为Worker进程（实际工作进程）。
      启动方式有两种：
      1).单进程启动：此时系统中仅有一个进程，该进程既充当Master进程的角色，也充当Worker进程的角色。
　　  2).多进程启动：此时系统有且仅有一个Master进程，至少有一个Worker进程工作。
　　  Master进程主要进行一些全局性的初始化工作和管理Worker的工作；事件处理是在Worker中进行的。
  超高性能: Redis能读的速度是110000次/s,写的速度是81000次/s 。
  原子性: 单个操作是原子性的。多个操作也支持事务，即原子性，通过MULTI和EXEC指令包起来。


Redis官方推荐的 Client 有两个:
  1).Redisson [distributed and scalable Java data structures on top of Redis server]
  2).Jedis
```

##### 2.2.Jedis Client 操作 Redis 指令 实现分布式锁
```
单Redis实例实现分布式锁的正确方法:
1).加锁
  第一步: SET resource_name my_random_value NX
  第二步: PX 30000
  无法保证原子性操作，所以 Redis 2.6.12 版本之前 是通过 Lua脚本保证原子性
  Redis 2.6.12 版本之后提供了原子性指令:
    SET resource_name my_random_value NX PX 30000
    > set redis_distlock xxx NX PX 30000
2).解锁
  为了保证锁超时情况下解锁 不解了别的线程的锁 造成安全问题 故 value 设置成一个随机值保证唯一性 
  那么在解锁时要分成两步:
  第一步: 匹配 要解锁的 value 值 是否是当前线程的 value值
  第二步: 然后继续 del 操作
  为了保证原子性，通用要通过 Lua脚本实现
  > if redis.call("get",KEYS[1]) == ARGV[1] then
	    return redis.call("del",KEYS[1])
	else
	    return 0
	end
3).等待时间内阻塞重试（保证一个正常分布式锁必需的）	
  加锁的时候 要进行 等待时间内阻塞重试，等待时间超时则放弃获取锁 提高获取锁成功率；不然只会有一个线程获取锁，其他全部失败。


总结: 一个可用的分布式锁就写好了。但是还是存在诸多问题，比如：
  不支持可重入锁（递归锁）: 目前是非重入锁（自旋锁）
  不支持公平锁（按照请求顺序加锁）： 目前是非公平锁（无序）
  高可用不行（最重要）: 多节点同步导致的分布式锁失效问题 


超时问题: 
  Redis 的分布式锁不能解决超时问题，如果在加锁和释放锁之间的逻辑执行的太长，以至于超出了锁的超时限制，就会出现问题。因为这时候第一个线程持有的锁过期了，临界区的逻辑还没有执行完，这个时候第二个线程就提前重新持有了这把锁，导致临界区代码不能得到严格的串行执行。
  为了避免这个问题，Redis 分布式锁不要用于较长时间的任务。如果真的偶尔出现了，数据出现的小波错乱可能需要人工介入解决。


核心依赖&代码: 
  <dependency>
    <groupId>redis.clients</groupId>
    <artifactId>jedis</artifactId>
    <version>2.9.0</version>
  </dependency>

  // 定义的一些常量
  String COMMAND_NX = "NX";
  String COMMAND_PX = "PX";
  String LOCK_SUCCESS = "OK";
  Integer UNLOCK_SUCCESS = 1;
  int LOCK_RETRY_TIMES = 3;
  long LOCK_WAIT_TIME = 1000L;
  long LOCK_TIMEOUT = 3000L;

  // 非阻塞加锁 获取不到直接返回
  public boolean lock(String key, String value) {
    Holder<Boolean> flagHolder = new Holder<>();
    flagHolder.setValue(false);
    jedisPool.execute(jedis -> {
        String result = jedis.set(key, value, RedisConstant.COMMAND_NX, RedisConstant.COMMAND_PX, RedisConstant.LOCK_TIMEOUT);
        if (RedisConstant.LOCK_SUCCESS.equalsIgnoreCase(result)) {
            flagHolder.setValue(true);
        }
    });
    return flagHolder.getValue();
  }
  // 阻塞加锁 等待时间内重试3次
  public boolean tryLock(String key, String value, Long waitTime) {
    boolean flag = false;
    long lockWaitTime = System.currentTimeMillis() + waitTime;
    while (!flag && System.currentTimeMillis() < lockWaitTime) {
        flag = this.lock(key, value);
        if (flag) {
            break;
        }
        try {
            Thread.sleep(waitTime / RedisConstant.LOCK_RETRY_TIMES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    return flag;
  }
  // 解锁
  public boolean unlock(String key, String value) {
    Holder<Boolean> flagHolder = new Holder<>();
    flagHolder.setValue(false);
    String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
    jedisPool.execute(jedis -> {
        Object result = jedis.eval(script, Collections.singletonList(key), Collections.singletonList(value));
        if (RedisConstant.UNLOCK_SUCCESS.equals(result)) {
            flagHolder.setValue(true);
        }
    });
    return flagHolder.getValue();
  }

  // 保证唯一性即可
  String value = String.valueOf(System.currentTimeMillis() + RedisConstant.LOCK_TIMEOUT);
  // 加锁 阻塞等待时间为1秒
  boolean flag = jedisLock.tryLock(productId, value, RedisConstant.LOCK_WAIT_TIME);
  if (!flag) {
      throw new KillException(101, "哎呦喂，人也太多了，换个地方再试试！");
  }
  try {
      // 注意可能抛出异常 不要补获 直接往上抛
      this.orderProductMockDiffUser(productId);
  } finally {
      // 解锁
      jedisLock.unlock(productId, value);
  }

```

##### 2.3.Redisson Client 封装好的API 实现分布式锁
```
以上方法在 分布式多节点同步（异步同步方式） 高可用不行，不安全问题发生


Redisson 介绍:
  Jedis 是 Redis 的 Java 实现的客户端，其 API 提供了比较全面的 Redis 命令的支持。
  Redission 也是 Redis 的客户端，相比于 Jedis 功能简单。
  Jedis 简单使用阻塞的 I/O 和 Redis 交互，Redission 通过 Netty 支持非阻塞 I/O。

  Redission 封装了锁的实现，其继承了 java.util.concurrent.locks.Lock 的接口，让我们像操作我们的本地 Lock 一样去操作 Redission 的 Lock。
  Redission 不仅提供了 Java 自带的一些方法(lock，tryLock)，还提供了异步加锁，对于异步编程更加方便。


Redlock 算法: 
  加锁时，它会向过半节点发送 set(key, value, nx=True, ex=xxx) 指令，只要过半节点 set 成功，那就认为加锁成功。
  释放锁时，需要向所有节点发送 del 指令。
  不过 Redlock 算法还需要考虑出错重试、时钟漂移等很多细节问题，同时因为 Redlock 需要向多个节点进行读写，意味着相比单实例 Redis 性能会下降一些。

  官方 Redlock 算法介绍: http://www.redis.cn/topics/distlock.html


核心依赖&代码: 
  <dependency>
    <groupId>org.redisson</groupId>
    <artifactId>redisson</artifactId>
    <version>3.6.5</version>
  </dependency>

  // 定义的一些常量
  String REDIS_ADDR = "redis://127.0.0.1:6379";
  String REDIS_LOCK_KEY = "redis_lock:0001";
  long LOCK_WAIT_TIME = 1000L;
  long LOCK_TIMEOUT = 3000L;

  @Bean
  public RedissonClient redissonClient() {
    // 支持单机，主从，哨兵，集群等模式
    // 此为单机模式
    Config config = new Config();
    config.useSingleServer()
            .setAddress(RedisConstant.REDIS_ADDR)
            .setTimeout(3000);
    return Redisson.create(config);
  }

  // 获取红锁
  RLock redLock = redissonClient.getLock(productId);
  try {
      // 加锁 等待时间 超时时间
      boolean flag = redLock.tryLock(RedisConstant.LOCK_WAIT_TIME, RedisConstant.LOCK_TIMEOUT, TimeUnit.MILLISECONDS);
      if (!flag) {
          throw new KillException(101, "哎呦喂，人也太多了，换个地方再试试！");
      }
      // 注意可能抛出异常 不要补获 直接往上抛
      this.orderProductMockDiffUser(productId);
  } catch (InterruptedException e) {
      log.error("【Redisson加锁异常】", e);
      throw new KillException(102, "请求发生异常，请重试！");
  } finally {
      // 解锁
      redLock.unlock();
  }


总结: 
  如果你很在乎高可用性，希望挂了一台 redis 完全不受影响，那就应该考虑 redlock。
  不过代价也是有的，需要更多的 redis 实例，性能也下降了。  
  
```

#### 三、Zookeeper实现分布式锁

##### 3.1.Zookeeper简介
```
ZooKeeper: A Distributed Coordination Service for Distributed Applications
ZooKeeper 是一个为分布式应用提供一致性服务的软件。

ZooKeeper is a centralized service for maintaining configuration information, naming, providing distributed synchronization, and providing group services.
ZooKeeper 是一个集中的服务，为了维护配置信息、命名服务、提供分布式同步、提供组服务。

ZooKeeper 是 Google 的 Chubby 一个开源的实现。

ZooKeeper 的目标就是封装好复杂易出错的关键服务，将简单易用的接口和性能高效、功能稳定的系统提供给用户。


一句话: zookeeper = 文件系统 + 通知机制
  文件系统: 每个子目录项如 NameService 都被称作为znode
    有四种类型的znode：
      PERSISTENT 持久节点（客户端与zookeeper连接断开后，节点依旧存在）
      PERSISTENT_SEQUENTIAL 持久顺序节点（根据创建时间顺序给节点名称进行编号）
      EPHEMERAL 临时节点（客户端与zookeeper连接断开后，节点会被删除）
      EPHEMERAL_SEQUENTIAL 临时顺序节点（创建节点时编号，断开连接时被删除）
  通知机制:
    客户端注册监听它关心的目录节点，当目录节点发生变化（数据改变、被删除、子目录节点增加删除）时，zookeeper会通知客户端。  


原理: 
  ZooKeeper是以Fast Paxos算法为基础的，Paxos 算法存在活锁的问题，即当有多个proposer交错提交时，有可能互相排斥导致没有一个proposer能提交成功，
  而Fast Paxos作了一些优化，通过选举产生一个leader (领导者)，只有leader才能提交proposer，
  具体算法可见Fast Paxos。因此，要想弄懂ZooKeeper首先得对Fast Paxos有所了解。

```

##### 3.2.Curator Client 封装好的API 实现分布式锁
```
Curator 是 Netflix 公司开源的一个 Zookeeper 客户端，与 Zookeeper 提供的原生客户端相比，
Curator 的抽象层次更高，简化了 Zookeeper 客户端的开发量。


核心依赖&代码: recipes是食谱的意思
  <dependency>
    <groupId>org.apache.curator</groupId>
    <artifactId>curator-recipes</artifactId>
    <version>2.12.0</version>
  </dependency>

  // 定义的一些常量
  String ZK_ADDR = "127.0.0.1:2181";
  String ZK_LOCK_PATH = "/zk_lock";
  long LOCK_WAIT_TIME = 1000L;

  // 通过工厂类创建客户端 RetryNTimes(重试次数，重试间的阻塞时间单位ms) 是客户端连接重试策略
  CuratorFramework client = CuratorFrameworkFactory.newClient(ZKConstant.ZK_ADDR, new RetryNTimes(10, 5000));
  client.start();

  // 创建分布式锁, 锁空间的根节点路径为 /productId
  InterProcessMutex zkLock = new InterProcessMutex(curatorClient, "/" + productId);
  try {
      // 加锁 等待时间1秒
      boolean flag = zkLock.acquire(ZKConstant.LOCK_WAIT_TIME, TimeUnit.MILLISECONDS);
      if (!flag) {
          throw new KillException(101, "哎呦喂，人也太多了，换个地方再试试！");
      }
      // 注意可能抛出异常 不要补获 直接往上抛
      this.orderProductMockDiffUser(productId);
  } catch (KillException ke) {
      throw ke;
  } catch (Exception e) {
      log.error("【ZK加锁异常】", e);
      throw new KillException(102, "请求发生异常，请重试！");
  } finally {
      try {
          // 解锁
          zkLock.release();
      } catch (Exception e) {
          log.error("【ZK解锁异常】", e);
      }
  }


原理分析:   
  String ourPath = null;
  boolean hasTheLock = false;

  while(!isDone) {
    isDone = true;
    try {
    	// 1. 创建 临时顺序节点
        ourPath = this.driver.createsTheLock(this.client, this.path, localLockNodeBytes);
        // 2. 获取锁
        // 2.1 查找父节点下所有的临时顺序节点并排序，判断自己创建的节点是不是最靠前的一个，是则成功获取锁；
        // 2.2 如果不是，则向排序前一个节点注册 Watcher，用于监听 前一个节点 是否存在，此时加锁失败，进入等待状态；
        // 2.3 再来一个线程加锁，则client1得到了锁，client2监听了node1, client3监听了node2，形成了一个等待队列，就像JUC里的AQS(抽象队列同步器)
        hasTheLock = this.internalLockLoop(startMillis, millisToWait, ourPath);
    } catch (NoNodeException var14) {
        if (!this.client.getZookeeperClient().getRetryPolicy().allowRetry(retryCount++, System.currentTimeMillis() - startMillis, RetryLoop.getDefaultRetrySleeper())) {
            throw var14;
        }
        isDone = false;
    }
  }

  public String createsTheLock(CuratorFramework client, String path, byte[] lockNodeBytes) throws Exception {
    String ourPath;
    if (lockNodeBytes != null) {
        ourPath = (String)((ACLBackgroundPathAndBytesable)client.create().creatingParentContainersIfNeeded().withProtection().withMode(CreateMode.EPHEMERAL_SEQUENTIAL)).forPath(path, lockNodeBytes);
    } else {
        ourPath = (String)((ACLBackgroundPathAndBytesable)client.create().creatingParentContainersIfNeeded().withProtection().withMode(CreateMode.EPHEMERAL_SEQUENTIAL)).forPath(path);
    }

    return ourPath;
  }

  public enum CreateMode {
    PERSISTENT(0, false, false), // 持久节点（客户端与zookeeper连接断开后，节点依旧存在）
    PERSISTENT_SEQUENTIAL(2, false, true), // 持久顺序节点（根据创建时间顺序给节点名称进行编号）
    EPHEMERAL(1, true, false), // 临时节点（客户端与zookeeper连接断开后，节点会被删除）
    EPHEMERAL_SEQUENTIAL(3, true, true); // 临时顺序节点（创建节点时编号，断开连接时被删除）

    private int flag;
    private boolean ephemeral; // 临时的 短暂的
    private boolean sequential; // 按次序的
    
    private CreateMode(int flag, boolean ephemeral, boolean sequential) {
        this.flag = flag;
        this.ephemeral = ephemeral;
        this.sequential = sequential;
    }
  }

  // 释放锁
  1). 任务完成，显示释放
    调用删除node1节点的指令
  2). 任务执行过程中，客户端崩溃
    连接断开，根据临时节点的特性，相关联的node1节点会自动被删除
  node1被删除，由于client2一直监视着node1的存在状态，client2会立刻收到通知，这时候
  client2会再次查询父节点下的所有子节点再排序，判断自己的节点node2是否是最靠前的节点，是则获取锁成功。


总结：
  没有超时时间的设置，所以不会存在 Redis 超时时间的问题。  

```

#### 四、分布式锁总结
```
分布式锁的一些特点？
1).互斥性： 和我们本地锁一样互斥性是最基本，但是分布式锁需要保证在不同节点的不同线程的互斥。
2).锁超时： 和本地锁一样支持锁超时，防止死锁。

3).支持阻塞和非阻塞： 和 ReentrantLock 一样支持 lock 和 trylock 以及 tryLock(long timeOut)。
4).可重入性： 同一个节点上的同一个线程如果获取了锁之后那么也可以再次获取这个锁。
5).支持公平锁和非公平锁(可选)： 公平锁的意思是按照请求加锁的顺序获得锁，非公平锁就相反是无序的。这个一般来说实现的比较少。

6).高效，高可用： 加锁和解锁需要高效，同时也需要保证高可用防止分布式锁失效，可以增加降级。


我们需要的分布式锁应该是怎么样的？

1).可以保证在分布式部署的应用集群中，同一个方法在同一时间只能被一台机器上的一个线程执行。（基础要求）

2).这把锁最好是一把阻塞锁（根据业务需求考虑要不要这条，一般都是需要的）
3).这把锁要是一把可重入锁（避免死锁），还有公平锁（功能更丰富）

4).有高可用的获取锁和释放锁功能 （高可用）
5).获取锁和释放锁的性能要好（在安全性、可靠性的前提下追求性能更好，以适应于高并发的场景）


对比:
1).Redis实现:
  优点: 性能好，借助于 Redis 的高性能(内存)，实现起来较为方便，借助于 Reids 简单的指令（单线程、原子性指令、lua脚本）。
    多节点的情况下 Redisson 对 RedLock 封装的比较好，高可用推荐 Redisson 封装的 RedLock算法 实现。
  缺点: 通过超时时间来控制锁的失效时间并不是十分的靠谱。
    失效时间我设置多长时间为好？如何设置的失效时间太短，方法没等执行完，锁就自动释放了，那么就会产生并发问题。
    如果设置的时间太长，其他获取锁的线程就可能要平白的多等一段时间。
    或者由于一些别的因素导致的偶发性超时（对时间敏感），比如
      时钟发生跳跃: ZK 不会受影响
    Martin 觉得 RedLock 不安全很大的原因也是因为 时钟的跳跃，因为锁过期强依赖于时间，但是 ZK 不需要依赖时间，依赖每个节点的 Session。 
    Redis 作者也给出了解答，对于时间跳跃分为人为调整和 NTP 自动调整：
      人为调整： 人为调整影响的完全可以人为不调整，这个是处于可控的。
      NTP 自动调整： 这个可以通过一定的优化，把跳跃时间控制在可控范围内，虽然会跳跃，但是是完全可以接受的。

2).Zookeeper实现:
  优点: 比 Reids 基于超时时间的思路 更可靠，实现起来比较简单，高可用利用ZK集群进行保证。

  缺点: 性能没有 Redis 这种缓存服务器好，每次创建锁、释放锁的过程中，都要动态创建、销毁临时节点来实现。
    ZK集群中创建和删除节点只能通过Leader服务器来执行，然后将数据同不到所有的Follower机器上。

  其实，使用Zookeeper也有可能带来并发问题，只是并不常见而已。考虑这样的情况，由于网络抖动，客户端可ZK集群的session连接断了，那么zk以为客户端挂了，就会删除临时节点，这时候其他客户端就可以获取到分布式锁了。就可能产生并发问题。这个问题不常见是因为zk有重试机制，一旦zk集群检测不到客户端的心跳，就会重试，Curator客户端支持多种重试策略。多次重试之后还不行的话才会删除临时节点。（所以，选择一个合适的重试策略也比较重要，要在锁的粒度和并发之间找一个平衡。）


总结:
  从实现的复杂性角度（从低到高） Zookeeper >= Redis 两者差不多
  
  从可靠性角度（从高到低）Zookeeper > Redis  ZK可靠性比Redis好

  从性能角度（从高到低）Redis > Zookeeper  Redis性能比ZK好
```

#### 五、相关资料链接
```
redis官方分布式锁 Redlock算法 文档: http://www.redis.cn/topics/distlock.html

你以为 Redlock算法 真的很完美？: http://martin.kleppmann.com/2016/02/08/how-to-do-distributed-locking.html

Redlock 分析
  Martin Kleppmann 在这儿分析了Redlock: http://martin.kleppmann.com/2016/02/08/how-to-do-distributed-locking.html
  我不赞同他的说法，并且对他做出了回复 我的回复在这儿: http://antirez.com/news/101

```