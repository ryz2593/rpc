package com.ryz2593.rpc;

import com.ryz2593.rpc.data.RequestData;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author ryz2593
 * @date 2019/4/19
 * @desc
 */
public class RPCClient {

    /**
     * 生成接口对应代理实现类实例的具体方法
     *
     * @param interfaceClass
     * @param address
     * @param <T>
     * @return
     */
    public static <T> T getRemoteProxy(final Class<T> interfaceClass, final InetSocketAddress address) {
        //JDK动态代理
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass}, new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        try (Socket socket = new Socket()) {
                            //连接到服务端（从注册中心获取注册地址列表）[负载均衡算法]
                            socket.connect(address);
                            try (
                                    ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                                    ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                            ) {
                                //封装网络请求对象
                                RequestData data = new RequestData();
                                data.setInterfaceName(interfaceClass.getName());
                                data.setMethodName(method.getName());
                                data.setParametersTypes(method.getParameterTypes());
                                data.setParameters(args);
                                //序列化网络请求数据
                                out.writeObject(data);
                                //获取服务端序列化的结果
                                return in.readObject();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        return null;
                    }
                });
    }
}
