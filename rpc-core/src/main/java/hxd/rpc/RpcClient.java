package hxd.rpc;

import hxd.rpc.entry.RpcRequest;
import hxd.rpc.serializer.CommonSerializer;

/**
 * @author huxiaodong
 */
public interface RpcClient {
    Object sendRequest(RpcRequest rpcRequest);
    void setSerializer(CommonSerializer serializer);
}
