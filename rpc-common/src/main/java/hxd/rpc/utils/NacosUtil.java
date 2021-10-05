package hxd.rpc.utils;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import hxd.rpc.enumCommon.RpcError;
import hxd.rpc.exception.RpcException;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 在我们关闭服务端后，nacos中依然有已关闭的服务，此时客户端调用是错误的
 * 此util是在服务端关闭之前自动向nacos注销服务
 * @author huxiaodong
 */
@Slf4j
public class NacosUtil {
    private static final NamingService namingService;
    private static InetSocketAddress inetSocketAddress;
    private static final Set<String> serviceNames = new HashSet<>();

    private static final String SERVER_ADDR = "8.141.60.147:8848";

    static {
        namingService = getNacosNamingService();
    }

    private static NamingService getNacosNamingService() {
        try {
            return NamingFactory.createNamingService(SERVER_ADDR);
        } catch (NacosException e) {
            log.error("连接到Nacos时有错误发生: ", e);
            throw new RpcException(RpcError.FAILED_TO_CONNECT_TO_SERVICE_REGISTRY);
        }
    }

    public static void registerService(String serviceName, InetSocketAddress address) throws NacosException {
        namingService.registerInstance(serviceName, address.getHostName(), address.getPort());
        NacosUtil.inetSocketAddress = address;
        serviceNames.add(serviceName);
    }

    public static List<Instance> getAllInstance(String serviceName) throws NacosException {
        return namingService.getAllInstances(serviceName);
    }

    public static void clearRegistry(){
        if (!serviceNames.isEmpty() && inetSocketAddress != null) {
            String host = inetSocketAddress.getHostName();
            int port = inetSocketAddress.getPort();
            serviceNames.forEach(service -> {
                try {
                    namingService.deregisterInstance(service, host, port);
                } catch (NacosException e) {
                    log.error("注销服务 {} 失败", service, e);
                }
            });
        }
    }
}
