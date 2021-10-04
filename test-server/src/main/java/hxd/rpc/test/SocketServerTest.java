package hxd.rpc.test;

import hxd.rpc.api.HelloService;
import hxd.rpc.registry.DefaultServiceRegistry;
import hxd.rpc.registry.ServiceRegistry;
import hxd.rpc.socket.server.SocketServer;

/**
 * @author huxiaodong01
 */
public class SocketServerTest {
    public static void main(String[] args) {
        /*
          创建service，并注册到RpcServer中，指定服务端端口为9000
         */
        HelloService helloService = new HelloServiceImpl();
        ServiceRegistry serviceRegistry = new DefaultServiceRegistry();
        serviceRegistry.registry(helloService);
        SocketServer rpcServer = new SocketServer(serviceRegistry);
        rpcServer.start(9000);

    }
}
