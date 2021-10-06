package hxd.rpc.transport;

import hxd.rpc.serializer.CommonSerializer;

/**
 * @author huxiaodong
 */
public interface RpcServer {
    int DEFAULT_SERIALIZER = CommonSerializer.KRYO_SERIALIZER;

    void start();

    <T> void publishService(Object service, String serviceName);
}
