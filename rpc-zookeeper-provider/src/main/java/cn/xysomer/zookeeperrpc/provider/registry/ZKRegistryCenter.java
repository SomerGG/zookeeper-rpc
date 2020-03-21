package cn.xysomer.zookeeperrpc.provider.registry;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

/**
 * @Description
 * @Author Somer
 * @Date 2020-03-21 16:15
 */
public class ZKRegistryCenter implements IRegistryCenter {

    CuratorFramework curatorFramework = null;

    {
        curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(ZKConfig.CONNECTION_STR)//连接地址
                .sessionTimeoutMs(5000)//会话超时时间5秒，
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))//重试策略：衰减重试，最大不超过3次
                .namespace("registry")//命名空间
                .build();
        curatorFramework.start();
    }


    @Override
    public void registry(String serviceName, String serviceAddress) {
        String servicePath = "/" + serviceName;
        try {
            //判断节点是否存在
            if (curatorFramework.checkExists().forPath(servicePath) == null) {
                curatorFramework.create()
                        .creatingParentsIfNeeded()
                        .withMode(CreateMode.PERSISTENT)//持久节点
                        .forPath(servicePath);
            }
            String addressPath = servicePath + "/" + serviceAddress;
            curatorFramework.create().withMode(CreateMode.EPHEMERAL).forPath(addressPath);
            System.out.println("服务注册成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
