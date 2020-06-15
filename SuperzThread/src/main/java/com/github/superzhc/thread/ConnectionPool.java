package com.github.superzhc.thread;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

/**
 * 2020年06月15日 superz add
 */
public class ConnectionPool
{
    private LinkedList<Connection> pool = new LinkedList<>();

    public ConnectionPool(int initialSize) {
        if (initialSize > 0) {
            for (int i = 0; i < initialSize; i++) {
                pool.addLast(ConnectionDriver.createConnection());
            }
        }
    }

    public void releaseConnection(Connection connection) {
        if (null != connection) {
            synchronized (pool) {
                // 连接释放后需要进行通知，这样其他消费者能够感知到连接池中已经归还了一个连接
                pool.addLast(connection);
                pool.notifyAll();
            }
        }
    }

    /**
     * 在mills内无法获取到连接，将会返回null
     * @param mills
     * @return
     */
    public Connection fetchConnection(long mills) throws InterruptedException {
        synchronized (pool){
            if(mills<=0){
                while (pool.isEmpty()){
                    pool.wait();
                }
                return pool.removeFirst();
            }else{
                long future=System.currentTimeMillis()+mills;
                long remaining=mills;
                while (pool.isEmpty()&&remaining>0){
                    pool.wait(remaining);
                    remaining=future-System.currentTimeMillis();
                }
                Connection result=null;
                if(!pool.isEmpty())
                    result=pool.removeFirst();
                return result;
            }
        }
    }

    public static class ConnectionDriver
    {
        static class ConnectionHandler implements InvocationHandler
        {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if ("commit".equals(method.getName())) {
                    TimeUnit.MILLISECONDS.sleep(100);
                }
                return null;
            }
        }

        // 创建一个Connection的代理，在commit时休眠100毫秒
        public static final Connection createConnection() {
            return (Connection) Proxy.newProxyInstance(ConnectionDriver.class.getClassLoader(),
                    new Class[] {Connection.class }, new ConnectionHandler());
        }
    }
}
