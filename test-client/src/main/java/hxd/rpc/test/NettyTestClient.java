package hxd.rpc.test;

import hxd.rpc.api.ByeService;
import hxd.rpc.serializer.CommonSerializer;
import hxd.rpc.transport.RpcClient;
import hxd.rpc.api.HelloObject;
import hxd.rpc.api.HelloService;
import hxd.rpc.transport.RpcClientProxy;
import hxd.rpc.transport.netty.client.NettyClient;

/**
 * @author huxiaodong
 */
public class NettyTestClient {
    public static void main(String[] args) {
        RpcClient client = new NettyClient(CommonSerializer.KRYO_SERIALIZER);
        RpcClientProxy rpcClientProxy = new RpcClientProxy(client);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(12, "This is a message");
        String res = helloService.hello(object);
        System.out.println(res);
        ByeService byeService = rpcClientProxy.getProxy(ByeService.class);
        System.out.println(byeService.bye("Netty"));

    }
}
