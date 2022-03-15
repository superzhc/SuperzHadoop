package com.github.superzhc.common.ssh;

import com.jcraft.jsch.*;

/**
 * @author superz
 * @create 2022/3/15 9:26
 **/
public class JschTool {
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
        if (null == session) {
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
            return session;
        } catch (Exception e) {
            return null;
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
}
