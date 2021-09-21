package hxd.rpc.serializer;

/**
 * @author huxiaodong
 */
public interface CommonSerializer {
    Integer KRYO_SERIALIZER = 0;
    Integer JSON_SERIALIZER = 1;
    Integer HESSIAN_SERIALIZER = 2;
    Integer PROTOBUF_SERIALIZER = 3;

    Integer DEFAULT_SERIALIZER = KRYO_SERIALIZER;

    /*static CommonSerializer getByCode(int code) {
        switch (code) {
            case 0:

        }
    }*/
}
