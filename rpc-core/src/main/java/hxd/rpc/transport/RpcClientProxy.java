package hxd.rpc.transport;

import hxd.rpc.entry.RpcRequest;
import hxd.rpc.entry.RpcResponse;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

/**
 * 客户端动态代理
 * @author huxiaodong
 */
public class RpcClientProxy implements InvocationHandler {


    private final hxd.rpc.RpcClient rpcClient;

    public RpcClientProxy(hxd.rpc.RpcClient rpcClient) {
        this.rpcClient = rpcClient;
    }


    /**
     * 由于在客户端层面没有具体实现类，通过动态代理生成需要的RpcRquest对象发给服务端，通过传递host和port来指定服务端地址
     */
    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clazz){
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequest rpcRequest = RpcRequest.builder()
                .requestId(UUID.randomUUID().toString())
                .interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameters(args)
                .paramType(method.getParameterTypes())
                .build();

        return rpcClient.sendRequest(rpcRequest);
    }
}
