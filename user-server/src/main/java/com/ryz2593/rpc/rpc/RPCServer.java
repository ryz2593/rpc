package com.ryz2593.rpc.rpc;

import com.ryz2593.rpc.data.RequestData;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author ryz2593
 * @date 2019/4/22
 * @desc
 */
public class RPCServer {
    //定义保持发布服务的列表
    private Map<String, Object> serviceMap = new ConcurrentHashMap<>(32);
    //定义线程池优化线程对象
    private ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(4,
            30, 200, TimeUnit.MICROSECONDS,
            new ArrayBlockingQueue<Runnable>(10));


    /**
     * 发布服务的方法
     *
     * @param interfaceClass
     * @param instance
     */
    public void publishService(Class<?> interfaceClass, Object instance) {
        serviceMap.put(interfaceClass.getName(), instance);
    }

    public void start(int port) {
        try {
            //创建服务端对象
            ServerSocket socket = new ServerSocket();
            //绑定端口
            socket.bind(new InetSocketAddress(port));
            System.out.println("服务启动成功");
            //接口客户端请求
            while (true) {
                poolExecutor.execute(new ServerTask(socket.accept()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class ServerTask implements Runnable {

        private final Socket socket;

        public ServerTask(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {

            //真正处理服务端对于客户端请求的逻辑
            try (
                    ObjectInputStream deSerializer = new ObjectInputStream(socket.getInputStream());
                    ObjectOutputStream serializer = new ObjectOutputStream(socket.getOutputStream())
            ) {
                //接收客户端发送协议对象
                RequestData data = (RequestData) deSerializer.readObject();
                //获取接口全名称
                String interfaceName = data.getInterfaceName();
                //通过接口名称在发布服务器列表中获取服务实例
                Object instance = serviceMap.get(interfaceName);
                //通过反射构建方法对象
                Method method = instance.getClass().getDeclaredMethod(data.getMethodName(), data.getParametersTypes());
                //反射调用实例中对应方法
                Object result = method.invoke(instance, data.getParameters());
                //把返回结果序列化返回给客户端
                serializer.writeObject(result);

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try{
                    if(socket != null) {
                        socket.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        }
    }

}
