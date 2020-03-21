package cn.xysomer.zookeeperrpc.client.discovery;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description 基于 Zookeeper 的服务发现
 * @Author Somer
 * @Date 2020-03-21 16:38
 */
public class ZKServiceDiscovery implements IServiceDiscovery {

    CuratorFramework curatorFramework;

    private List<String> serviceRepos = new ArrayList<>();//服务本地缓存

    {
        curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(ZKConfig.CONNECTION_STR)
                .sessionTimeoutMs(5000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .namespace("registry")
                .build();
        curatorFramework.start();
    }


    @Override
    public String discovery(String serviceName) {
        String servicePath = "/" + serviceName;
        if (serviceRepos.isEmpty()) {
            try {
                serviceRepos = curatorFramework.getChildren().forPath(servicePath);
                //注册监听
                registryWatch(servicePath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //针对已有的地址做负载均衡
        ILoadBalanceStrategy loadBalanceStrategy = new RandomBalance();
        return loadBalanceStrategy.selectHost(serviceRepos);
    }

    private void registryWatch(final String path) {
        PathChildrenCache cache = new PathChildrenCache(curatorFramework, path, true);
        PathChildrenCacheListener childrenCacheListener = (curatorFramework1, pathChildrenCacheEvent) -> {
            System.out.println("客户端收到节点变更事件");
            //再次更新本地缓存
            serviceRepos = curatorFramework1.getChildren().forPath(path);
        };
        //添加监听
        cache.getListenable().addListener(childrenCacheListener);
        try {
            cache.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
