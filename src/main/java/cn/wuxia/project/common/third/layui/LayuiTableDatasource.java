package cn.wuxia.project.common.third.layui;

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
public class LayuiTableDatasource implements Serializable {
    private static final long serialVersionUID = -4923773355694982750L;
    int code;
    String msg;
    long count;
    Object data;


    public LayuiTableDatasource(long count, List data) {
        this.count = count;
        this.data = data;
    }

    LayuiTableDatasource(Object data) {
        this.data = data;
    }

    public static LayuiTableDatasource data(Object data) {
        return new LayuiTableDatasource(data);
    }

}
