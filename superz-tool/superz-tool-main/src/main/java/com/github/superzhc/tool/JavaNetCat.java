package com.github.superzhc.tool;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Java 版本的 netcat 简易工具
 *
 * @author superz
 * @create 2021/10/9 10:33
 */
public class JavaNetCat {

    public static void main(String[] args) throws Exception {
        JavaNetCat.listen(9090);
    }

    /**
     * Socket 连接
     *
     * @param host
     * @param port
     * @throws Exception
     */
    public static void connect(String host, int port) throws Exception {
        System.err.println("Connecting to " + host + " port " + port);
        final Socket socket = new Socket(host, port);
        transferStreams(socket);
    }

    /**
     * 监听端口
     *
     * @param port
     * @throws Exception
     */
    public static void listen(int port) throws Exception {
        System.err.println("Listening at port " + port);
        ServerSocket serverSocket = new ServerSocket(port);
        Socket socket = serverSocket.accept();
        System.err.println("Accepted");
        transferStreams(socket);
    }

    private static void transferStreams(Socket socket) throws IOException,
            InterruptedException {
        InputStream input1 = System.in;
        OutputStream output1 = socket.getOutputStream();
        InputStream input2 = socket.getInputStream();
        PrintStream output2 = System.out;
        Thread thread1 = new Thread(new StreamTransferer(input1, output1));
        Thread thread2 = new Thread(new StreamTransferer(input2, output2));
        thread1.start();
        thread2.start();
        thread1.join();
        socket.shutdownOutput();
        thread2.join();
    }

    public static class StreamTransferer implements Runnable {
        private InputStream input;
        private OutputStream output;

        public StreamTransferer(InputStream input, OutputStream output) {
            this.input = input;
            this.output = output;
        }

        @Override
        public void run() {
            try {
                PrintWriter writer = new PrintWriter(output);
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                String line;
                while ((line = reader.readLine()) != null) {
                    writer.println(line);
                    writer.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
