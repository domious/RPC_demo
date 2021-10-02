package hxd.rpc.test;

import hxd.rpc.api.HelloService;
import hxd.rpc.netty.server.NettyServer;
import hxd.rpc.registry.DefaultServiceRegistry;
import hxd.rpc.registry.ServiceRegistry;

/**
 * @author huxiaodong
 */
public class NettyTestServer {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        ServiceRegistry registry = new DefaultServiceRegistry();
        registry.registry(helloService);
        NettyServer server = new NettyServer();
        server.start(9999);
    }
}
