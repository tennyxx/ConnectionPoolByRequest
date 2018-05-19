package PoolsAchieve;

import java.sql.Connection;
import java.util.Date;


/**
 *  数据库链接操作实体
 *  @author liucx
 */
public class ConnCollection {
    /**
     * @param InitDate 初始化倒计时时间（秒）
     */
    public ConnCollection(Date  InitDate) {

    }
    public ConnCollection(Date initDate, String requestKey, Connection conn) {
        InitDate = initDate;
        NewDate = initDate;
        RequestKey = requestKey;
        Conn = conn;
    }

    //创建实例的时间，备用
    private Date InitDate;
    //用于判断的时间，每次操作更新会更新该时间
    private Date NewDate;

    /**
     * 有任何操作时自动调用该方法，刷新对比时间
     */
    private void  ResetDate(){
        this.NewDate = new Date();
    }
    private String RequestKey;
    private Connection Conn;

    public Date getInitDate() {
        return InitDate;
    }

    public void setInitDate(Date initDate) {
        InitDate = initDate;
    }

    public Date getNewDate() {
        return NewDate;
    }

    public void setNewDate(Date newDate) {
        NewDate = newDate;
    }

    public String getRequestKey() {
        return RequestKey;
    }

    public void setRequestKey(String requestKey) {
        RequestKey = requestKey;
    }

    public Connection getConn() {
        this.ResetDate();
        return Conn;
    }

    public void setConn(Connection conn) {
        this.ResetDate();
        Conn = conn;
    }

    @Override
    public String toString() {
        return "ConnCollection{" +
                "InitDate=" + InitDate +
                ", NewDate=" + NewDate +
                ", RequestKey='" + RequestKey + '\'' +
                ", Conn=" + Conn +
                '}';
    }
}
