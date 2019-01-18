package org.shao;

/**
 * Created by hmh on 2019/1/15.
 */
public class CommonTest {

    public static void main(String[] args) {
//        test1();
        Integer f1 = 100, f2 = 100, f3 = 150, f4 = 150;
        System.out.println(f1 == f2);
        System.out.println(f3 == f4);
//        f1 = new Integer(100);
        f2 = new Integer(100);
//        f3 = new Integer(150);
        f4 = new Integer(150);
        System.out.println(f1 == f2);
        System.out.println(f3 == f4);
    }

    private static void test1() {
        double totalMoney = 10L;
        int totalCount = test2(totalMoney);
        System.out.println(Double.valueOf(totalMoney).intValue() + " 块钱一共能得到 " + totalCount + " 瓶啤酒");
    }

    private static int test2(double totalMoney) {
        int totalCount = 0;
        double unitPrice = 2L;
        int initCount = (int) (totalMoney / unitPrice);
        totalCount += initCount;
        System.out.println("initCount = " + initCount);

        int inputCount = initCount;
        int outputCount;
        while (inputCount > 0) {
            outputCount = test3(inputCount);
            totalCount += outputCount;
            System.out.println("outputCount = " + outputCount);
            if (outputCount <= 0) {
                break;
            }
            inputCount = outputCount + test4(inputCount);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return totalCount;
    }

    private static int test3(int count) {
        return count / 4 + count / 2;
    }

    private static int test4(int count) {
        int surplusCount1 = count % 4;
        int surplusCount2 = count % 2;
        if (surplusCount1 > 0 && surplusCount2 > 0) {
            if (surplusCount1 > surplusCount2) return surplusCount2;
            else return surplusCount1;
        }
        return 0;
    }

}
