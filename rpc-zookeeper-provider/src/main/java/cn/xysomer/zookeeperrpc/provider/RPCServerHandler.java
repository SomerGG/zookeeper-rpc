package cn.xysomer.zookeeperrpc.provider;

import cn.xysomer.zookeeperrpc.api.RPCRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @Description
 * @Author Somer
 * @Date 2020-03-12 23:52
 */
public class RPCServerHandler extends ChannelInboundHandlerAdapter {

    private Map<String, Object> registryMap;

    public RPCServerHandler(Map<String, Object> registryMap) {
        this.registryMap = registryMap;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //请求信息读取到之后，
        RPCRequest request = (RPCRequest) msg;
        String serviceName = request.getClassName();
        String version = request.getVersion();
        if (!StringUtils.isEmpty(version)) {
            serviceName += "-" + version;
        }
        Object service = registryMap.get(serviceName);
        if (null == service) {
            throw new RuntimeException("service not found：" + serviceName);
        }
        Class clazz = Class.forName(request.getClassName());
        Object[] args = request.getParameters();
        Method method;
        if (null != args) {
            Class<?>[] types = new Class[args.length];
            for (int i = 0; i < args.length; i++) {
                types[i] = args[i].getClass();
            }
            method = clazz.getMethod(request.getMethodName(), types);
        } else {
            method = clazz.getMethod(request.getMethodName());
        }
        Object result = method.invoke(service, args);
        ctx.writeAndFlush(result);
        ctx.flush();
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
