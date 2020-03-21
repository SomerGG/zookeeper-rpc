package cn.xysomer.zookeeperrpc.provider.registry;

/**
 * @Description 注册中心
 * @Author Somer
 * @Date 2020-03-21 16:14
 */
public interface IRegistryCenter {

    /**
     * 服务注册
     *
     * @param serviceName 服务名称
     * @param serviceAddress 服务地址
     */
    void registry(String serviceName, String serviceAddress);
}
