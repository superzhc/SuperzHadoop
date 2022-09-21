package com.github.superzhc.common.ssh;

import com.jcraft.jsch.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * @author superz
 * @create 2022/3/15 9:26
 **/
public class JschTool implements Closeable {
    private static final Logger log = LoggerFactory.getLogger(JschTool.class);

    private String host;
    private Integer port;
    private String username;
    /*1、用户名和密码登录 2、如果私钥路径不为空，则作为私钥文件的密码*/
    private String password;
    /*私钥的路径*/
    private String privateKeyPath = null;

    public JschTool(String host, Integer port, String username, String password) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    public JschTool(String host, Integer port, String username, String privateKeyPath, String passphrase) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.privateKeyPath = privateKeyPath;
        this.password = passphrase;
    }

    private Session session = null;

    /**
     * 可重用 Session
     *
     * @return
     */
    public Session getSession() {
        if (null == session || !session.isConnected()) {
            session = connect();
        }
        return session;
    }

    /**
     * 打开一个新的 Session
     *
     * @return
     */
    public Session openSession() {
        return connect();
    }

    /**
     * 一个 Session 表示一个与 SSH 服务器的连接。
     * 一个 Session 可以包含多种多样的 Channel ，并且创建 openChannel(java.lang.String)
     * 关于 Session 的使用，创建连接后这个 session 是一直可用的，所以不需要关闭。
     * 由 Session 中 open 的 Channel 在使用后应该关闭。
     *
     * @return
     */
    private Session connect() {
        Session session = null;
        boolean pkLogin = false;
        try {
            JSch jSch = new JSch();

            if (null != privateKeyPath && privateKeyPath.trim().length() > 0) {
                jSch.addIdentity(privateKeyPath, password);
                pkLogin = true;
            }

            if (null == port || port < 0 || port > 65535) {
                session = jSch.getSession(username, host);
            } else {
                session = jSch.getSession(username, host, port);
            }

            if (!pkLogin && (null != password && password.trim().length() > 0)) {
                session.setPassword(password);
            }

            //如果服务器连接不上，则抛出异常
            if (session == null) {
                throw new Exception("session is null");
            }

            //设置首次登录跳过主机检查，可选值：(ask | yes | no)
            session.setConfig("StrictHostKeyChecking", "no");
            //设置登录超时时间
            session.connect(3000);
            log.info("登录成功，服务器信息：{}", session.getServerVersion());
            return session;
        } catch (Exception e) {
            log.error("登录失败【Host={}:{}，Username={}】", host, port, username, e);
            throw new RuntimeException("登录失败");
        }
    }

    public String executes(String... commands) {
        return executes(getSession(), commands);
    }

    public String executes(Session session, String... commands) {
        return execute(session, commands);
    }

    public String execute(String[] commands) {
        return execute(getSession(), commands);
    }

    public String execute(Session session, String[] commands) {
        Channel channel = null;
        try {
            // log.debug("执行命令：{}", String.join("\n", commands));
            channel = session.openChannel("shell");
            ChannelShell shell = (ChannelShell) channel;
            shell.connect();

            StringBuilder result = new StringBuilder();
            try (InputStream in = shell.getInputStream()) {
                // 输入多条命令
                OutputStream outputStream = shell.getOutputStream();
                // 使用 PrintWriter 可以不用在每条命令后面输入换行符
                PrintWriter writer = new PrintWriter(outputStream);
                for (String command : commands) {
                    writer.println(command);
                }
                // 结束本次shell交互命令
                writer.println("exit");
                writer.flush();

                try (InputStreamReader iReader = new InputStreamReader(in)) {
                    try (BufferedReader reader = new BufferedReader(iReader)) {
                        String line = null;
                        while ((line = reader.readLine()) != null) {
                            result.append(line).append("\n");
                        }
                    }
                }
            }
            return result.toString();
        } catch (Exception e) {
            log.error("执行命令异常！", e);
            throw new RuntimeException(e);
        } finally {
            if (null != channel && channel.isConnected()) {
                channel.disconnect();
            }
        }
    }

    public String execute(String command) {
        return execute(getSession(), command);
    }

    public String execute(Session session, String command) {
        Channel channel = null;
        try {
            log.debug("执行命令：{}", command);
            channel = session.openChannel("exec");
            ChannelExec channelExec = (ChannelExec) channel;
            channelExec.setCommand(command);
            channelExec.connect(3 * 1000);

            StringBuilder result = new StringBuilder();
            // 注意：响应结果可能并不会第一时间就返回，做个兼容，尝试三次
            try (InputStream in = channelExec.getInputStream()) {
                int counter = 0;
                while ((in.available() == 0) && counter < 3) {
                    Thread.sleep(3 * 1000);
                    counter++;
                }

                try (InputStreamReader iReader = new InputStreamReader(in)) {
                    try (BufferedReader reader = new BufferedReader(iReader)) {
                        String line = null;
                        while ((line = reader.readLine()) != null) {
                            result.append(line).append("\n");
                        }
                    }
                }
            }
            return result.toString();
        } catch (Exception e) {
            log.error("执行命令异常！", e);
            throw new RuntimeException(e);
        } finally {
            if (null != channel && channel.isConnected()) {
                channel.disconnect();
            }
        }
    }

    /**
     * 将远程端口绑定到本地
     *
     * @param session
     * @param localPort
     * @param remoteHost
     * @param remotePort
     * @return
     */
    public boolean bindPort(Session session, int localPort, String remoteHost, int remotePort) {
        if (null != session && session.isConnected()) {
            try {
                session.setPortForwardingL(localPort, remoteHost, remotePort);
                return true;
            } catch (JSchException e) {
                return false;
            }
        }
        return false;
    }

    public boolean unBindPort(Session session, int localPort) {
        if (null != session && session.isConnected()) {
            try {
                session.delPortForwardingL(localPort);
                return true;
            } catch (JSchException e) {
                return false;
            }
        }
        return false;
    }

    public ChannelSftp sftp(Session session) {
        return (ChannelSftp) openChannel(session, "sftp");
    }

    public ChannelShell shell(Session session) {
        return (ChannelShell) openChannel(session, "shell");
    }

    public ChannelExec exec(Session session) {
        return (ChannelExec) openChannel(session, "exec");
    }

    public Channel openChannel(Session session, String type) {
        Channel channel = null;
        try {
            channel = session.openChannel(type);
            channel.connect(3000);
            return channel;
        } catch (JSchException e) {
            return null;
        }
    }

    @Override
    public void close() throws IOException {
        if (null != session && session.isConnected()) {
            session.disconnect();
        }
    }
}
