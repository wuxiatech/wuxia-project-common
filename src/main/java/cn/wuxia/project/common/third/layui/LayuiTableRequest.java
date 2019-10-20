package cn.wuxia.project.common.third.layui;

import cn.wuxia.common.orm.query.Conditions;
import cn.wuxia.common.orm.query.Pages;
import cn.wuxia.common.util.StringUtil;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.collections4.MapUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author songlin
 */
@Getter
@Setter
@NoArgsConstructor
public class LayuiTableRequest implements Serializable {
    int page;
    int limit;
    List<Conditions> conditions = Lists.newArrayList();
    Map<String, Object> condition;

    public Pages toPages() {
        Pages pages = new Pages(page, limit);
        pages.setConditions(conditions);
        if (MapUtils.isNotEmpty(condition)) {
            for (Map.Entry<String, Object> entry : condition.entrySet()) {
                if (StringUtil.isNotBlank(entry.getValue())) {
                    pages.addCondition(Conditions.eq(entry.getKey(), "" + entry.getValue()));
                }
            }
        }
        return pages;
    }
}
