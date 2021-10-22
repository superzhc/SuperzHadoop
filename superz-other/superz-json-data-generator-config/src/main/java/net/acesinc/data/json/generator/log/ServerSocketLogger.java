package net.acesinc.data.json.generator.log;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author superz
 * @create 2021/10/22 16:20
 */
public class ServerSocketLogger implements EventLogger {
    private static final Logger log = LogManager.getLogger(ServerSocketLogger.class);
    private static final String PORT_NAME = "port";

    private final ServerSocket server;
    private Integer port = 8080;

    private boolean isRunning = true;
    private Thread acceptThread;
    /*private Thread heartbeatThread;*/

    private final List<Socket> sockets = new ArrayList<>();

    public ServerSocketLogger(Map<String, Object> props) throws IOException {
        if (props.containsKey(PORT_NAME)) {
            port = (Integer) props.get(PORT_NAME);
        }

        server = new ServerSocket(port);

        /**
         * 使用一个监听线程，持续获取
         */
        acceptThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (isRunning) {
                    try {
                        final Socket socket = server.accept();
                        sockets.add(socket);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        acceptThread.start();

        // 去除失效的客户端连接
        /* 心跳服务是通过发送一个数据，这会造成数据污染，去掉
        heartbeatThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (isRunning) {
                        for (Socket socket : sockets) {
                            try {
                                socket.sendUrgentData(0xFF);
                            } catch (IOException e) {
                                try {
                                    socket.close();
                                } catch (IOException ex) {
                                }
                            }
                        }

                        Thread.sleep(1000 * 5);
                    }
                } catch (InterruptedException e) {
                    log.error(e);
                }
            }
        });
        heartbeatThread.start();
        */
    }

    @Override
    public void logEvent(String event, Map<String, Object> producerConfig) {
        int counter = 0;
        for (Socket socket : sockets) {
            if (null != socket && !socket.isClosed()) {
                try {
                    PrintWriter out = new PrintWriter(socket.getOutputStream());
                    out.println(event);
                    out.flush();
                } catch (IOException e) {
                    log.info("socket error", e);
                }
                counter++;
            }
        }
        log.debug("Sending event to [" + counter + "] sockets: [ " + event + " ]");
    }

    @Override
    public void shutdown() {
        isRunning = false;

        try {
            for (Socket socket : sockets) {
                if (null != socket && socket.isClosed()) {
                    socket.close();
                }
            }
            server.close();
        } catch (IOException ex) {
            log.error("shutdown error: ",ex);
        }

        /* 关闭监听线程 */
        acceptThread.interrupt();
        /*heartbeatThread.interrupt();*/
    }
}
