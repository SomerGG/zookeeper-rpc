package cn.xysomer.zookeeperrpc.provider;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @Description
 * @Author Somer
 * @Date 2020-03-12 23:58
 */
@Configuration
@ComponentScan(basePackages = "cn.xysomer.zookeeperrpc.provider")
public class SpringConfig {

    @Bean(name = "rpcNioServer")
    public RPCNioServer rpcNioServer() {
        return new RPCNioServer(8080);
    }
}
