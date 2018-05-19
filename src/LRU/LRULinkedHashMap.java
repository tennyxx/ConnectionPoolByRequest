package LRU;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * LinkedHashMap扩展类（线程安全），基于访问顺序排列 实现LRU算法
 *
 * @author liucx
 * @createTime 20180507 16:29:39
 * @update 2018年5月11日 添加将要关闭的数据库链接是否为空or null的判断
 * @param <K>
 * @param <V>
 */
public class LRULinkedHashMap<K, V> extends LinkedHashMap<K, V> {
    private final Lock lock = new ReentrantLock();
    private static final long serialVersionUID = 1L;
    // 定义缓存的容量
    private int maxsize;

    // 带参数的构造器
    public LRULinkedHashMap(int maxsize) {
        // 调用LinkedHashMap的构造器，传入以下参数 false 基于插入顺序 true 基于访问顺序
        super(16, 0.75f, true);
        // 传入指定的缓存最大容量
        this.maxsize = maxsize;
    }

    // 实现LRU，如果map里面的元素个数大于了缓存最大容量，则删除链表的顶端元素
    @Override
    public synchronized boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        if (size() > maxsize) {
            System.out.println("将要被删除的key&&value为：" + eldest.getKey() + "="
                    + eldest.getValue());
        }
        return size() > maxsize;
    }
    @Override
    public V get(Object key) {
        return super.get(key);
    }
    @Override
    public V put(K key, V value) {
        synchronized (LRULinkedHashMap.class) {
            return super.put(key, value);
        }
    }

}