package hxd.rpc.registry;

import java.net.InetSocketAddress;

/**
 * @author huxiaodong
 */
public interface ServiceRegistry {
    /**
     * 把服务注册到nacos
     */
    void registry(String serviceName, InetSocketAddress inetSocketAddress);

    /**
     * 从注册中心获取一个服务提供者地址
     */
    InetSocketAddress lookUpService(String serviceName);
}
