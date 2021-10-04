package hxd.rpc.provider;

import hxd.rpc.enumCommon.RpcError;
import hxd.rpc.exception.RpcException;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author huxiaodong
 */
@Slf4j
public class ServiceProviderImpl implements ServiceProvider {

    private static final Map<String, Object> serviceMap = new ConcurrentHashMap<>(8);

    private static final Set<String> registerService = ConcurrentHashMap.newKeySet();

    @Override
    public synchronized <T> void registry(T service) {
        String serviceName = service.getClass().getCanonicalName();
        if (registerService.contains(serviceName)) {
            return;
        }
        registerService.add(serviceName);
        //得到service实现的接口名
        Class<?>[] interfaces = service.getClass().getInterfaces();
        if (interfaces.length == 0) {
            throw new RpcException(RpcError.SERVICE_NOT_IMPLEMENT_ANY_INTERFACE);
        }
        for (Class<?> c : interfaces) {
            serviceMap.put(c.getCanonicalName(), service);
        }
        log.info("给接口：{} 注册服务：{}", interfaces, serviceName);
    }

    @Override
    public Object getService(String serviceName) {
        Object service = serviceMap.get(serviceName);
        if (service == null) {
            throw new RpcException(RpcError.SERVICE_NOT_FOUND);
        }
        return service;
    }
}
