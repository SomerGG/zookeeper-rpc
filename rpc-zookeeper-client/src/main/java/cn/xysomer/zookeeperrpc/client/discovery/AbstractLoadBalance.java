package cn.xysomer.zookeeperrpc.client.discovery;

import java.util.List;

/**
 * @Description 抽象负载均衡策略
 * @Author Somer
 * @Date 2020-03-21 16:50
 */
public abstract class AbstractLoadBalance implements ILoadBalanceStrategy {
    @Override
    public String selectHost(List<String> serviceRepos) {
        if (null == serviceRepos || serviceRepos.size() == 0) {
            return null;
        }
        if (null != serviceRepos && serviceRepos.size() == 1) {
            return serviceRepos.get(0);
        }
        return null;
    }

    protected abstract String doSelect(List<String> serviceRepos);
}
