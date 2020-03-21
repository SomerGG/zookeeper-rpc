package cn.xysomer.zookeeperrpc.provider;

import cn.xysomer.zookeeperrpc.provider.registry.IRegistryCenter;
import cn.xysomer.zookeeperrpc.provider.registry.ZKRegistryCenter;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.StringUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description
 * @Author Somer
 * @Date 2020-03-12 23:43
 */
public class RPCNioServer implements ApplicationContextAware, InitializingBean {

    private int port;

    public RPCNioServer(int port) {
        this.port = port;
    }

    private Map<String, Object> registryServiceMap = new HashMap<>();

    EventLoopGroup boosGroup = null;

    EventLoopGroup workerGroup = null;

    ServerBootstrap server = null;

    IRegistryCenter registryCenter = new ZKRegistryCenter();

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            //初始化NIO服务端
            server = new ServerBootstrap();
            boosGroup = new NioEventLoopGroup();
            workerGroup = new NioEventLoopGroup();
            server.group(boosGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            ChannelPipeline pipeline = channel.pipeline();
                            //设置协议编码器
                            pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
                            //设置协议解码器
                            pipeline.addLast(new LengthFieldPrepender(4));
                            //设置对象编码器
                            pipeline.addLast("encoder", new ObjectEncoder());
                            //设置对象解码器
                            pipeline.addLast("decoder", new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));
                            //自定义处理逻辑
                            pipeline.addLast(new RPCServerHandler(registryServiceMap));
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture future = server.bind(port).sync();
            System.out.println("NIO Server Start Listen Port：" + port);
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
            boosGroup.shutdownGracefully();
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, Object> serviceBeanMap = applicationContext.getBeansWithAnnotation(RPCService.class);
        if (!serviceBeanMap.isEmpty()) {
            for (Object serviceBean : serviceBeanMap.values()) {
                RPCService rpcService = serviceBean.getClass().getAnnotation(RPCService.class);
                String serviceName = rpcService.value().getName();
                String version = rpcService.version();
                if (!StringUtils.isEmpty(version)) {
                    serviceName += "-" + version;
                }
                registryServiceMap.put(serviceName, serviceBean);
                registryCenter.registry(serviceName, getAddress() + ":" + port);
            }
        }
    }

    private static String getAddress() {
        InetAddress inetAddress = null;
        try {
            inetAddress = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return inetAddress.getHostAddress();// 获得本机的ip地址
    }
}
