package hxd.rpc.test;

import hxd.rpc.api.HelloObject;
import hxd.rpc.api.HelloService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author huxiaodong
 */
@Slf4j
public class HelloServiceImpl implements HelloService {
    @Override
    public String hello(HelloObject object) {
        log.info("接收到：{}", object.getMessage());
        return "调用的返回值：" + object.getId();
    }
}
