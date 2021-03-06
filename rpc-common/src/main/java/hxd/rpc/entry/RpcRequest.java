package hxd.rpc.entry;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author huxiaodong
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RpcRequest implements Serializable {

    private String requestId;

    /**
     * 待调用接口名称
     */
    private String interfaceName;

    /**
     * 调用方法名
     */
    private String methodName;

    /**
     * 调用参数值
     */
    private Object[] parameters;

    /**
     *调用方法参数类型
     */
    private Class<?>[] paramType;
}
