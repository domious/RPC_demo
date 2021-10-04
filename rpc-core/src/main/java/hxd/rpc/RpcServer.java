package hxd.rpc;

/**
 * @author huxiaodong
 */
public interface RpcServer {
    void start();
    <T> void publishService(Object service, Class<T> serviceClass);
}
