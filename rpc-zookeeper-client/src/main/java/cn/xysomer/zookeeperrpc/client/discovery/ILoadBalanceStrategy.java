package cn.xysomer.zookeeperrpc.client.discovery;

import java.util.List;

/**
 * @Description 负载均衡策略
 * @Author Somer
 * @Date 2020-03-21 16:49
 */
public interface ILoadBalanceStrategy {

    String selectHost(List<String> serviceRepos);
}
