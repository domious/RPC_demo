package hxd.rpc.test;

import hxd.rpc.RpcClient;
import hxd.rpc.api.HelloObject;
import hxd.rpc.api.HelloService;
import hxd.rpc.netty.client.NettyClient;
import hxd.rpc.RpcClientProxy;

/**
 * @author huxiaodong
 */
public class NettyTestClient {
    public static void main(String[] args) {
        RpcClient client = new NettyClient("127.0.0.1", 9999);
        RpcClientProxy rpcClientProxy = new RpcClientProxy(client);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(12, "This is a message");
        String res = helloService.hello(object);
        System.out.println(res);

    }
}
