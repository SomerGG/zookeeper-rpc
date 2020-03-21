package cn.xysomer.zookeeperrpc.client.discovery;

import java.util.List;
import java.util.Random;

/**
 * @Description 随机负载策略
 * @Author Somer
 * @Date 2020-03-21 16:52
 */
public class RandomBalance extends AbstractLoadBalance {

    @Override
    protected String doSelect(List<String> serviceRepos) {
        int length = serviceRepos.size();
        Random random = new Random();
        return serviceRepos.get(random.nextInt(length));
    }
}
