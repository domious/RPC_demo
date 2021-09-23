package hxd.rpc.test;

import hxd.rpc.api.HelloService;
import hxd.rpc.transport.RpcServer;

/**
 * @author huxiaodong01
 */
public class SocketServerTest {
    public static void main(String[] args) {
        /*
          创建一个service，并注册到RpcServer中，指定服务端端口为9000
         */
        HelloService helloService = new HelloServiceImpl();
        RpcServer rpcServer = new RpcServer();
        rpcServer.register(helloService, 9000);

    }
}
