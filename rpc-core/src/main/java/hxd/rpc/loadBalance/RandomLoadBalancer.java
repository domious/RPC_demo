package hxd.rpc.loadBalance;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;

/**
 * @author huxiaodong
 */
public class RandomLoadBalancer implements LoadBalancer{
    private int index = 0;


    @Override
    public Instance select(List<Instance> instances) {
        if (index >= instances.size()) {
            index %= instances.size();
        }
        return instances.get(index++);
    }
}
