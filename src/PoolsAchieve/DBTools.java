package PoolsAchieve;

import LRU.LRULinkedHashMap;

import java.io.Console;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 数据库工具类
 *
 * @author liucx
 * @create 20180517 15:34
 */
public class DBTools {
    // 线程可见的LRU集合，存放数据库链接
    private static volatile Map<String, ConnCollection> connMap = new LRULinkedHashMap<String, ConnCollection>(
            500);

    static {
        //开启循环清理之前等待的时间（第一次循环时间），单位：毫秒
        final Integer _FIRSTWAIT = 5000;
        //定时清理集合中不活跃连接的时间，单位：毫秒
        final Integer _INTERVALSSECONDS = 5000;
        //不活动链接保留时间  单位：秒
        final Long _KEEPSECONDS = 20l;

        System.out.println("静态构造方法");
        connMap.put("liucxtest1", new ConnCollection(new Date(), "liucxtest1", null));
        connMap.put("liucxtest2", new ConnCollection(new Date(), "liucxtest2", null));
        connMap.put("liucxtest3", new ConnCollection(new Date(), "liucxtest3", null));
        connMap.put("liucxtest4", new ConnCollection(new Date(), "liucxtest4", null));
        connMap.put("liucxtest5", new ConnCollection(new Date(), "liucxtest5", null));
        connMap.put("liucxtest6", new ConnCollection(new Date(), "liucxtest6", null));

        connMap.put("liucxtest7", new ConnCollection(new Date(), "liucxtest7", null));
        ClearMap(_FIRSTWAIT, _INTERVALSSECONDS, _KEEPSECONDS);
    }

    /**
     * 获取数据list
     *
     * @return
     * @throws SQLException
     * @author liucx
     */
    public static void GetMapList(String connectKey) {
        // 防止线程1创建过程中线程2取到的还是null
        ConnCollection Conn = connMap.get(connectKey);
        if (Conn == null) {
            synchronized (DBTools.class) {
                if (Conn == null) {
                    //省略获取连接 Conn = GetConnection(env, dbEnum);
                    Conn = new ConnCollection(new Date(), connectKey, null);
                    connMap.put(connectKey, Conn);
                }
            }
        }
    }

    /**
     * 定时执行清理集合中的链接对象
     *
     * @param firstWait
     * @param intervalsSeconds
     */
    public static void ClearMap(final Integer firstWait, final Integer intervalsSeconds, final Long keepSeconds) {
        System.out.println("执行ClearMap");
        Runnable runnable = new Runnable() {
            public void run() {
                Integer count = 0;
                while (true) {
                    count++;
                    System.out.println("持续循环第" + count + "次--- size为" + connMap.size());
                    Iterator<Map.Entry<String, ConnCollection>> it = connMap.entrySet().iterator();
                    while (it.hasNext()) {

                        Map.Entry<String, ConnCollection> item = it.next();
                        String key = item.getKey();
                        ConnCollection obj = item.getValue();
                        Date d = obj.getNewDate();
                        Date now = new Date();
                        Long l = (now.getTime() - d.getTime()) / 1000;//计算相差秒数
                        System.out.println("当前时间差值为" + l + "秒，当前循环item输出为:" + obj.toString());
                        if (l > keepSeconds) {
                            System.out.println("执行删除" + obj.getRequestKey());
                            //关闭数据库连接

                            //从集合中移除
                            it.remove();
                            System.out.println("删除后的size为" + connMap.size());
                        }
                    }
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        Thread t1 = new Thread(runnable);
        //第一次循环前的等待时间。
        try {
            Thread.sleep(firstWait);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        t1.start();

    }
}
