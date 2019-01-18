### Netty重点笔记

#### NIO标准
```aidl
import java.nio.channels.Selector;
import java.nio.channels.SelectionKey;

import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import java.nio.ByteBuffer;

import java.nio.charset.Charset;
```

#### Netty Server
```aidl
import io.netty.bootstrap.ServerBootstrap;

import io.netty.channel.nio.NioEventLoopGroup;

import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;

import io.netty.buffer.ByteBuf;
```

#### Netty Client
```aidl
import io.netty.bootstrap.Bootstrap;

import io.netty.channel.nio.NioEventLoopGroup;

import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;

import io.netty.buffer.ByteBuf;
```

#### 数据读写容器 ByteBuf
```
import io.netty.buffer.ByteBuf;

capacity
maxCapacity
readerIndex
writerIndex

由于 Netty 使用了堆外内存，而堆外内存是不被 jvm 直接管理的，
也就是说申请到的内存无法被垃圾回收器直接回收，所以需要我们手动回收。
有点类似于c语言里面，申请到的内存必须手工释放，否则会造成内存泄漏。

release()、retain()
默认情况下，当创建完一个 ByteBuf，它的引用为1
然后每次调用 retain() 方法， 它的引用就加一
release() 方法原理是将引用计数减一，减完之后如果发现引用计数为0，
则直接回收 ByteBuf 底层的内存。

slice()、duplicate()、copy()

retainedSlice() 与 retainedDuplicate()
// retainedSlice 等价于
slice().retain()
// retainedDuplicate() 等价于
duplicate().retain()

```

#### 客户端与服务端通信协议编解码(序列化协议)
``` 
我们把 Java 对象根据协议封装成二进制数据包的过程成为编码，
而把从二进制数据包中解析出 Java 对象的过程成为解码

序列化+编码
反序列化+解码

```

#### Pipeline 与 ChannelHandler
``` 

```