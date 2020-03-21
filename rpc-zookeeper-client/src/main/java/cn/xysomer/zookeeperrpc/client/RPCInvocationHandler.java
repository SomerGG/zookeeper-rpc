package cn.xysomer.zookeeperrpc.client;


import cn.xysomer.zookeeperrpc.api.RPCRequest;
import cn.xysomer.zookeeperrpc.client.discovery.IServiceDiscovery;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @Description 代理生成拦截器
 * @Author Somer
 * @Date 2020-03-13 09:36
 */
public class RPCInvocationHandler implements InvocationHandler {

    private IServiceDiscovery serviceDiscovery;

    private String version;

    public RPCInvocationHandler(IServiceDiscovery serviceDiscovery, String version) {
        this.serviceDiscovery = serviceDiscovery;
        this.version = version;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RPCRequest request = new RPCRequest();
        request.setClassName(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setParameters(args);
        request.setVersion(this.version);
        String serviceName = request.getClassName();
        if (!StringUtils.isEmpty(version)) {
            serviceName += "-" + version;
        }
        //获得服务注册的地址
        String serviceAddress = serviceDiscovery.discovery(serviceName);
        System.out.println("============" + serviceAddress);
        //远程通信
        RPCNetTransport rpcNetTransport = new RPCNetTransport(serviceAddress);
        Object result = rpcNetTransport.send(request);
        return result;
    }
}
