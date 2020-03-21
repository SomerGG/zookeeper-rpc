package cn.xysomer.zookeeperrpc.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description
 * @Author Somer
 * @Date 2020-03-12 22:48
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {

    private String name;

    private int age;
}
