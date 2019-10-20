package cn.wuxia.project.common.third.layui;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author songlin
 * layui 穿梭框的数据源
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LayuiTransferDatasource {
    String value;
    String title;
    Boolean disabled;
    Boolean checked;

    public LayuiTransferDatasource(String value, String title) {
        this.value = value;
        this.title = title;
    }



}
