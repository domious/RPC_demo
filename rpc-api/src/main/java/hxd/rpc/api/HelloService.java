package hxd.rpc.api;

import lombok.Data;

/**
 * @author huxiaodong
 */
public interface HelloService {
    String hello(HelloObject object);
}
