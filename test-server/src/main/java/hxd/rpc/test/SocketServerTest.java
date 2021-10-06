package hxd.rpc.test;

import hxd.rpc.annotation.ServiceScan;
import hxd.rpc.api.HelloService;
import hxd.rpc.provider.ServiceProviderImpl;
import hxd.rpc.provider.ServiceProvider;
import hxd.rpc.serializer.CommonSerializer;
import hxd.rpc.transport.socket.server.SocketServer;

/**
 * @author huxiaodong01
 */
@ServiceScan
public class SocketServerTest {
    public static void main(String[] args) {
        /*
          创建service，并注册到RpcServer中，指定服务端端口为9000
         */
        SocketServer rpcServer = new SocketServer("127.0.0.1", 9000, CommonSerializer.KRYO_SERIALIZER);
        rpcServer.start();

    }
}
