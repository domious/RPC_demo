package hxd.rpc.transport;

import hxd.rpc.entry.RpcRequest;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 客户端动态代理
 * @author huxiaodong
 */
public class RpcClientProxy implements InvocationHandler {

    private String host;
    private int port;

    //private final RpcClient rpcClient;

    public RpcClientProxy(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * 由于在客户端层面没有具体实现类，通过动态代理生成需要的RpcRquest对象发给服务端，通过传递host和port来指定服务端地址
     *
     */
    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clazz){
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequest request = RpcRequest.builder()
                .interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameters(args)
                .paramType(method.getParameterTypes())
                .build();
        RpcClient rpcClient = new RpcClient();
        return null;
    }
}
