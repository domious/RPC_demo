package hxd.rpc.test;

import hxd.rpc.api.HelloService;
import hxd.rpc.netty.server.NettyServer;
import hxd.rpc.provider.ServiceProviderImpl;
import hxd.rpc.provider.ServiceProvider;
import hxd.rpc.serializer.KryoSerializer;

/**
 * @author huxiaodong
 */
public class NettyTestServer {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        NettyServer server = new NettyServer("127.0.0.1", 9999);
        server.setSerializer(new KryoSerializer());
        server.publishService(helloService, HelloService.class);
    }
}
