package cn.xysomer.zookeeperrpc.provider;


import cn.xysomer.zookeeperrpc.api.IHelloService;
import cn.xysomer.zookeeperrpc.api.User;

/**
 * @Description
 * @Author Somer
 * @Date 2020-03-12 23:40
 */
@RPCService(value = IHelloService.class, version = "v1")
public class HelloServiceImplV1 implements IHelloService {

    @Override
    public String sayHello(String name) {
        System.out.println("[v1.0] sayHello()：" + name);
        return "[v1.0] sayHello()：" + name;
    }

    @Override
    public String saveUser(User user) {
        System.out.println("[v1.0] saveUser()：" + user.toString());
        return "[v1.0] saveUser()：" + user.toString();
    }
}
