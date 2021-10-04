package hxd.rpc.test;

import hxd.rpc.api.HelloObject;
import hxd.rpc.api.HelloService;
import hxd.rpc.socket.client.SocketClient;
import hxd.rpc.RpcClientProxy;

/**
 * @author huxiaodong01
 */
public class SocketClientTest {
    public static void main(String[] args) {
        SocketClient client = new SocketClient("127.0.0.1", 9000);
        RpcClientProxy proxy = new RpcClientProxy(client);
        HelloService helloService = proxy.getProxy(HelloService.class);
        HelloObject helloObject = new HelloObject(12, "hello hxd, 123");
        String msg = helloService.hello(helloObject);
        System.out.println(msg);
    }

}
