package hxd.rpc.test;

import hxd.rpc.annotation.Service;
import hxd.rpc.api.ByeService;

/**
 * @author huxiaodong
 */
@Service
public class ByeServiceImpl implements ByeService {
    @Override
    public String bye(String name) {
        return "bye, " + name;
    }
}
