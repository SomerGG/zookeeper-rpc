package cn.xysomer.zookeeperrpc.api;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description
 * @Author Somer
 * @Date 2020-03-12 20:23
 */
@Data
public class RPCRequest implements Serializable {

    private String className;//请求服务对应的类名

    private String methodName;//请求方法名称

    private Object[] parameters;//请求列表

    private String version;//版本号
}
