package hxd.rpc.provider;

/**
 * @author huxiaodong
 */
public interface ServiceProvider {
    /**
     * 服务注册
     */
    <T> void registry(T service);

    /**
     * 获取服务
     */
    Object getService(String serviceName);
}
