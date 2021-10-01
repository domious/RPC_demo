package hxd.rpc.registry;

/**
 * @author huxiaodong
 */
public interface ServiceRegistry {
    /**
     * 服务注册
     */
    <T> void registry(T service);

    /**
     * 获取服务
     */
    Object getService(String serviceName);
}
