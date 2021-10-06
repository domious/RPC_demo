package hxd.rpc.test;

import hxd.rpc.api.ByeService;
import hxd.rpc.api.HelloObject;
import hxd.rpc.api.HelloService;
import hxd.rpc.serializer.CommonSerializer;
import hxd.rpc.transport.RpcClientProxy;
import hxd.rpc.transport.socket.client.SocketClient;

/**
 * @author huxiaodong01
 */
public class SocketClientTest {
    public static void main(String[] args) {
        SocketClient client = new SocketClient(CommonSerializer.KRYO_SERIALIZER);
        RpcClientProxy proxy = new RpcClientProxy(client);
        HelloService helloService = proxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(12, "This is a message");
        String res = helloService.hello(object);
        System.out.println(res);
        ByeService byeService = proxy.getProxy(ByeService.class);
        System.out.println(byeService.bye("Netty"));
    }

}
