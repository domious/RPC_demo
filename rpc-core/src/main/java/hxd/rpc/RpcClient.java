package hxd.rpc;

import hxd.rpc.entry.RpcRequest;

/**
 * @author huxiaodong
 */
public interface RpcClient {
    Object sendRequest(RpcRequest rpcRequest);
}
