package cn.xysomer.zookeeperrpc.provider;

import cn.xysomer.zookeeperrpc.api.IHelloService;
import cn.xysomer.zookeeperrpc.api.User;

/**
 * @Description
 * @Author Somer
 * @Date 2020-03-12 23:40
 */
@RPCService(value = IHelloService.class, version = "v2")
public class HelloServiceImplV2 implements IHelloService {

    @Override
    public String sayHello(String name) {
        System.out.println("[v2.0] sayHello()：" + name);
        return "[v2.0] sayHello()：" + name;
    }

    @Override
    public String saveUser(User user) {
        System.out.println("[v2.0] saveUser()：" + user.toString());
        return "[v2.0] saveUser()：" + user.toString();
    }
}
