package hxd.rpc.transport;

import hxd.rpc.entry.RpcRequest;
import hxd.rpc.serializer.CommonSerializer;

/**
 * @author huxiaodong
 */
public interface RpcClient {
    Object sendRequest(RpcRequest rpcRequest);
    int DEFAULT_SERIALIZER = CommonSerializer.KRYO_SERIALIZER;
}
