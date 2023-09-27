package com.itsyx.im.tcp;


import com.itsyx.im.codec.config.BootstrapConfig;
import com.itsyx.im.tcp.reciver.MessageReciver;
import com.itsyx.im.tcp.redis.RedisManager;
import com.itsyx.im.tcp.register.RegistryZK;
import com.itsyx.im.tcp.register.ZKit;
import com.itsyx.im.tcp.server.LimServer;
import com.itsyx.im.tcp.server.LimWebSocketServer;
import com.itsyx.im.tcp.util.MqFactory;
import org.I0Itec.zkclient.ZkClient;
import org.yaml.snakeyaml.Yaml;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Starter {

    public static void main(String[] args)  {
        if(args.length > 0){
            start(args[0]);
        }
    }

    private static void start(String path){
        try {
            Yaml yaml = new Yaml();
            InputStream inputStream = new FileInputStream(path);
            BootstrapConfig bootstrapConfig = yaml.loadAs(inputStream, BootstrapConfig.class);
            new LimServer(bootstrapConfig.getLim()).start();
            new LimWebSocketServer(bootstrapConfig.getLim()).start();

            RedisManager.init(bootstrapConfig);
            MqFactory.init(bootstrapConfig.getLim().getRabbitmq());
            MessageReciver.init(bootstrapConfig.getLim().getBrokerId().toString());
            registerZK(bootstrapConfig); // 向zk注册
        }catch (Exception e){
            e.printStackTrace();
            System.exit(500);
        }
    }

    public static void registerZK(BootstrapConfig config) throws UnknownHostException {
        String hostAddress = InetAddress.getLocalHost().getHostAddress();
        ZkClient zkClient = new ZkClient(config.getLim().getZkConfig().getZkAddr(),config.getLim().getZkConfig().getZkConnectTimeOut());
        ZKit zKit = new ZKit(zkClient);
        RegistryZK registryZK = new RegistryZK(zKit, hostAddress, config.getLim());
        Thread thread = new Thread(registryZK);
        thread.start();
    }

}
