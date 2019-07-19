package cn.wuxia.project.common.bean;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @author songlin
 */
@Getter
@Setter
@NoArgsConstructor
public class LayuiResult implements Serializable {
    private static final long serialVersionUID = -4923773355694982750L;
    int code;
    String msg;
    long count;
    Object data;


    public LayuiResult(long count, List data) {
        this.count = count;
        this.data = data;
    }

    LayuiResult(Object data) {
        this.data = data;
    }

    public static LayuiResult data(Object data) {
        return new LayuiResult(data);
    }

}
