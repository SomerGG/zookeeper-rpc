package cn.xysomer.zookeeperrpc.api;

/**
 * @Description
 * @Author Somer
 * @Date 2020-03-12 20:23
 */
public interface IHelloService {

    String sayHello(String name);

    String saveUser(User user);
}
