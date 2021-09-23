package hxd.rpc.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author huxiaodong
 */
@Data
@AllArgsConstructor
public class HelloObject implements Serializable {

    private Integer id;

    private String message;
}
