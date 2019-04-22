package com.ryz2593.rpc.client;

import com.ryz2593.rpc.api.UserService;
import com.ryz2593.rpc.RPCClient;

import java.net.InetSocketAddress;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        //创建地址对象
        InetSocketAddress address = new InetSocketAddress("localhost", 12345);
        //调用user-server暴露的服务
        UserService userService = RPCClient.getRemoteProxy(UserService.class, address);

        String result = userService.addUser("ryz2593");

        System.out.println("调用的结果：" + result);
    }
}
