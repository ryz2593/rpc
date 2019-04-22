package com.ryz2593.rpc;

import com.ryz2593.rpc.api.UserService;
import com.ryz2593.rpc.rpc.RPCServer;
import com.ryz2593.rpc.service.UserServiceImpl;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        //创建RPC Server 端
        RPCServer rpcServer = new RPCServer();
        rpcServer.publishService(UserService.class, new UserServiceImpl());

        //发布服务
        rpcServer.start(12345);
        System.out.println( "Hello World!" );
    }
}
