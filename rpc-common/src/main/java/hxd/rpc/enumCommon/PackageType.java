package hxd.rpc.enumCommon;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author huxiaodong01
 */
@AllArgsConstructor
@Getter
public enum PackageType {

    /**
     *
     */
    REQUEST_PACK(0),
    RESPONSE_PACK(1);

    private final int code;

}
