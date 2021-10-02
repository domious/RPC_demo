package hxd.rpc.serializer;

import com.esotericsoftware.kryo.Kryo;
import hxd.rpc.entry.RpcRequest;
import hxd.rpc.entry.RpcResponse;

/**
 * @author huxiaodong
 */
public class KryoSerializer implements CommonSerializer{

    private static final ThreadLocal<Kryo> kryoThreadLocal = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        kryo.register(RpcResponse.class);
        kryo.register(RpcRequest.class);

        return kryo;
    });

    @Override
    public byte[] serialize(Object obj) {
        return new byte[0];
    }

    @Override
    public Object deserialize(byte[] bytes, Class<?> clazz) {
        return null;
    }

    @Override
    public int getCode() {
        return 0;
    }
}
