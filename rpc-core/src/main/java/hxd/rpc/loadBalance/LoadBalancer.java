package hxd.rpc.loadBalance;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;

/**
 * @author huxiaodong
 */
public interface LoadBalancer {
    Instance select(List<Instance> instances);
}
