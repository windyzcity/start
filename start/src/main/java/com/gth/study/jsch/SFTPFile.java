package com.gth.study.jsch;

import com.jcraft.jsch.*;

import java.util.Properties;

/**
 * sftp通过上传文件
 * 1.直接上传到目标机
 * 2.通过跳板机上传到目标机
 *
 * 参考：
 * 1.https://www.cnblogs.com/zpch/p/7999328.html
 * 2.http://www.jcraft.com/jsch/examples/JumpHosts.java.html
 *
 * @date 2020/10/6
 */
public class SFTPFile {

    public static void main(String[] args) throws SftpException, JSchException {
        String ip = "192.168.199.188";
        int port = 22;
        String user = "root";
        String password = "123123";
        //String hostIp = null;
        String hostIp = "192.168.199.189";
        sftpFile(ip, port, user, password, hostIp);
    }

    /**
     * sftp传输文件
     *
     * @param ip
     * @param port
     * @param userName
     * @param pwd
     * @param hostIp
     */
    public static void sftpFile(String ip, int port, String userName, String pwd, String hostIp) {
        Session sshSession = null;
        Session session = null;
        ChannelSftp sftp = null;
        try {
            JSch jsch = new JSch();
            sshSession = jsch.getSession(userName, ip, port);
            sshSession.setPassword(pwd);
            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");
            sshConfig.put("PreferredAuthentications", "password,keyboard-interactive");
            sshSession.setConfig(sshConfig);
            sshSession.connect(100 * 1000);//可设置超时时间
            //完成上诉映射之后，即可通过本地端口连接了
            if (hostIp != null && !"".equals(hostIp)) {
                // 有跳板机
                System.out.println("Hop session is ok");
                session = getSessionByHop(jsch, sshSession, hostIp);
            } else {
                // 无跳板机
                System.out.println("Dest session is ok");
                session = sshSession;
            }
            //建立sftp通道
            Channel channel = (Channel) session.openChannel("sftp");//创建sftp通信通道
            channel.connect();
            sftp = (ChannelSftp) channel;

            // 文件上传
            put(sftp);
        } catch (JSchException e) {
            if (hostIp != null && !"".equals(hostIp)) {
                System.out.println("Hop " + ip + " JSchException error:");
            } else {
                System.out.println("Dest " + ip + " JSchException error:");
            }
            e.printStackTrace();
        } catch (SftpException e) {
            System.out.println("SftpException error:");
            e.printStackTrace();
        } finally {
            // 关闭连接：此通道是阻塞式的，一定要关闭连接，该线程就会结束
            if (sftp != null) {
                sftp.disconnect();
            }
            if (session != null) {
                session.disconnect();
            }
            if (sshSession != null) {
                sshSession.disconnect();
            }
        }
    }

    /**
     * 通过跳板机与目标机建立的session
     *
     * @param jsch
     * @param sshSession
     * @param hostIp
     * @return
     * @throws JSchException
     */
    private static Session getSessionByHop(JSch jsch, Session sshSession, String hostIp) {
        //此处开始为端口映射到本地的部分
        int assingedPort = 0;
        Session session = null;
        try {
            assingedPort = sshSession.setPortForwardingL(0, hostIp, 22);
            System.out.println("assingedPort=" + assingedPort + ",hostIp=" + hostIp);
            session = jsch.getSession("gth", "127.0.0.1", assingedPort);
            Properties remoteCfg = new Properties();
            remoteCfg.put("StrictHostKeyChecking", "no");
            remoteCfg.put("PreferredAuthentications", "password,keyboard-interactive");
            session.setConfig(remoteCfg);
            session.setPassword("gth123");
            session.connect(100 * 1000);
            System.out.println("Dest by Hop session is ok");
        } catch (JSchException e) {
            System.out.println("getSessionByHop " + hostIp + " JSchException error: ");
            e.printStackTrace();
        }
        return session;
    }

    /**
     * 文件传输
     *
     * @param sftp
     * @throws SftpException
     */
    private static void put(ChannelSftp sftp) throws SftpException {
        String localFile = "C:\\Users\\G\\Desktop\\jdkapi18chm.zip";
        String remoteDirectory = "/tmp";
        String remoteFileName = "jdkapi18chm.zip";
        sftp.cd(remoteDirectory);
        long start = System.currentTimeMillis();
        sftp.put(localFile, remoteFileName);
        System.out.println("put " + localFile + " success! it takes:" + (System.currentTimeMillis() - start) / 1000.0 + "s");
    }
}
