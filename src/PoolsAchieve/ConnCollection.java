package PoolsAchieve;

import java.sql.Connection;

/**
 *  数据库链接操作实体
 *  @author liucx
 */
public class ConnCollection {
    /**
     * @param InitSeconds 初始化倒计时时间（秒）
     */
    public ConnCollection(Integer  InitSeconds) {
        this.InitSeconds = InitSeconds;
        this.Seconds = InitSeconds;
        CountDown ();
    }

    public ConnCollection(Integer initSeconds, String requestKey, Connection conn) {
        InitSeconds = initSeconds;
        Seconds = InitSeconds;
        RequestKey = requestKey;
        Conn = conn;
        CountDown ();
    }

    //初始时间秒数
    private Integer InitSeconds;
    //剩余的秒数
    private Integer Seconds;
    //重置剩余时间
    private void  ResetSeconds(){
        this.Seconds = this.InitSeconds;
    }
    //秒单位倒计时
    private void CountDown ()  {
         while (Seconds>0){
             Seconds--;
             try {
                 Thread.sleep(1000);
             } catch (InterruptedException e) {
                 e.printStackTrace();
             }
         }
    }
    private String RequestKey;
    private Connection Conn;

    public String getRequestKey() {
        return RequestKey;
    }

    public void setRequestKey(String requestKey) {
        RequestKey = requestKey;
    }

    public Connection getConn() {
        this.ResetSeconds();
        return Conn;
    }

    public void setConn(Connection conn) {
        this.ResetSeconds();
        Conn = conn;
    }

    public Integer getSeconds() {
        return Seconds;
    }

    public void setSeconds(Integer seconds) {
        Seconds = seconds;
    }

    @Override
    public String toString() {
        return "ConnCollection{" +
                "InitSeconds=" + InitSeconds +
                ", Seconds=" + Seconds +
                ", RequestKey='" + RequestKey + '\'' +
                ", Conn=" + Conn +
                '}';
    }
}
