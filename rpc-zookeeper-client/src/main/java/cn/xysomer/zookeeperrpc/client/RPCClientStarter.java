package cn.xysomer.zookeeperrpc.client;

import cn.xysomer.zookeeperrpc.api.IHelloService;
import cn.xysomer.zookeeperrpc.api.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @Description 客户端测试
 * @Author Somer
 * @Date 2020-03-13 09:52
 */
public class RPCClientStarter {

    public static void main(String[] args) throws InterruptedException {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(RPCClientConfig.class);
        RPCClientProxy rpcClientProxy = applicationContext.getBean(RPCClientProxy.class);
        IHelloService helloService = rpcClientProxy.createClientProxy(IHelloService.class,"v2");
        for (int i = 0; i < 100; i++) {
            Thread.sleep(1000);
            String sayHello = helloService.sayHello("Somer");
            System.out.println(sayHello);
            String saveUser = helloService.saveUser(new User("Somer", 18));
            System.out.println(saveUser);
        }
    }
}
