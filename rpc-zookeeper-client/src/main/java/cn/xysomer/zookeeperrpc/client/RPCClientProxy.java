package cn.xysomer.zookeeperrpc.client;

import cn.xysomer.zookeeperrpc.client.discovery.IServiceDiscovery;
import cn.xysomer.zookeeperrpc.client.discovery.ZKServiceDiscovery;

import java.lang.reflect.Proxy;

/**
 * @Description 客户端代理生成
 * @Author Somer
 * @Date 2020-03-13 09:33
 */
public class RPCClientProxy {

    private IServiceDiscovery serviceDiscovery = new ZKServiceDiscovery();

    public <T> T createClientProxy(final Class<?> interfaceClass, final String version) {
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[]{interfaceClass}, new RPCInvocationHandler(serviceDiscovery, version));
    }
}
