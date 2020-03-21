package cn.xysomer.zookeeperrpc.client.discovery;

/**
 * @Description 服务发现
 * @Author Somer
 * @Date 2020-03-21 16:36
 */
public interface IServiceDiscovery {

    /**
     * 根据服务名称返回服务地址
     *
     * @param serviceName
     * @return
     */
    String discovery(String serviceName);
}
