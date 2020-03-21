package cn.xysomer.zookeeperrpc.client;

import cn.xysomer.zookeeperrpc.api.RPCRequest;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

/**
 * @Description
 * @Author Somer
 * @Date 2020-03-13 09:38
 */
public class RPCNetTransport {

    private String serviceAddress;

    public RPCNetTransport(String serviceAddress) {
        this.serviceAddress = serviceAddress;
    }

    EventLoopGroup loopGroup = null;

    public Object send(RPCRequest request) {
        Bootstrap client = new Bootstrap();
        final RPCClientHandler rpcClientHandler = new RPCClientHandler();
        try {
            loopGroup = new NioEventLoopGroup();//NIO 客户端线程组
            client.group(loopGroup)
                    .channel(NioSocketChannel.class)//设置 Channel 为 NioSocketChannel
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            ChannelPipeline pipeline = channel.pipeline();
                            pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
                            pipeline.addLast("frameEncoder", new LengthFieldPrepender(4));
                            pipeline.addLast("encoder", new ObjectEncoder());
                            pipeline.addLast("decoder", new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));
                            pipeline.addLast("handler", rpcClientHandler);//网络传输事件处理
                        }
                    });
            System.out.println("--------" +serviceAddress);
            String[] url = serviceAddress.split(":");
            ChannelFuture future = client.connect(url[0], Integer.parseInt(url[1])).sync();//发起异步连接操作
            future.channel().writeAndFlush(request).sync();//将请求发送给服务端
            future.channel().closeFuture().sync();//等待客户端链路关闭
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            loopGroup.shutdownGracefully().syncUninterruptibly();
        }
        return rpcClientHandler.getResult();
    }
}
