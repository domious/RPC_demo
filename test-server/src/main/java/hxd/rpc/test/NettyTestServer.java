package hxd.rpc.test;

import hxd.rpc.annotation.ServiceScan;
import hxd.rpc.api.HelloService;
import hxd.rpc.serializer.CommonSerializer;
import hxd.rpc.transport.RpcServer;
import hxd.rpc.transport.netty.server.NettyServer;
import hxd.rpc.serializer.KryoSerializer;

/**
 * @author huxiaodong
 */
@ServiceScan
public class NettyTestServer {
    public static void main(String[] args) {
        RpcServer server = new NettyServer("127.0.0.1", 9999, CommonSerializer.KRYO_SERIALIZER);
        server.start();
    }
}
