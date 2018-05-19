package PoolsAchieve;

import LRU.LRULinkedHashMap;

import java.io.Console;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
        //定时清理集合中不活跃连接的时间，单位：秒
        final Integer intervalsSeconds = 5;
        //第一次清理执行的等待时间，单位：秒
        final Integer firstWait = 0;
        System.out.println("静态构造方法");
        connMap.put("liucxtest1", new ConnCollection(10, "liucxtest1", null));
        connMap.put("liucxtest2", new ConnCollection(10, "liucxtest2", null));
        connMap.put("liucxtest3", new ConnCollection(10, "liucxtest3", null));
        connMap.put("liucxtest4", new ConnCollection(10, "liucxtest4", null));
        connMap.put("liucxtest4", new ConnCollection(10, "liucxtest5", null));
        connMap.put("liucxtest4", new ConnCollection(10, "liucxtest6", null));

        connMap.put("liucxtest5", new ConnCollection(10, "liucxtest7", null));
        ClearMap(firstWait, intervalsSeconds);
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
                    Conn = new ConnCollection(20);
                    Conn.setRequestKey(connectKey);
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
    public static void ClearMap(Integer firstWait, Integer intervalsSeconds) {
        System.out.println("执行循环");
        Runnable runnable = new Runnable() {
            public void run() {
                Integer count =0;
                while (true) {
                    count++;
                    System.out.println("持续循环第"+count+"次--- size为" + connMap.size());
                    Iterator<Map.Entry<String, ConnCollection>> it = connMap.entrySet().iterator();
                    while (it.hasNext()) {

                        Map.Entry<String, ConnCollection> item = it.next();
                        String key = item.getKey();
                        ConnCollection obj = item.getValue();
                        Integer seconds = obj.getSeconds();
                        System.out.println("当前循环item输出为:" + obj.toString());
                        if (seconds == 0) {
                            System.out.println("执行删除" + obj.getRequestKey());
                            //关闭数据库连接

                            //从集合中移除
                            it.remove();
                            System.out.println("删除后的size为" + connMap.size());
                        }
                    }
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        Thread t1 = new Thread(runnable);
        t1.start();
        //ScheduledExecutorService service = Executors
        //      .newSingleThreadScheduledExecutor();
        // 第二个参数为首次执行的延时时间，第三个参数为定时执行的间隔时间
        //System.out.println("ClearMap Begin !!");
        //service.scheduleAtFixedRate(runnable, firstWait, intervalsSeconds, TimeUnit.SECONDS);
    }
}
