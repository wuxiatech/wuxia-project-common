package cn.wuxia.project.common.bean;

import cn.wuxia.common.orm.query.Conditions;
import cn.wuxia.common.orm.query.MatchType;
import cn.wuxia.common.orm.query.Pages;
import cn.wuxia.common.orm.query.Sort;
import cn.wuxia.common.util.JsonUtil;
import cn.wuxia.common.util.StringUtil;
import lombok.Getter;
import lombok.Setter;

/**
 * @author songlin
 */
@Getter
@Setter
public class JQGridRequest {

    Boolean _search;// true
    Long nd;// 1536122190516
    Integer rows;// 15
    Integer page; // 1
    String sidx;// id
    String sord;// asc
    String filters; // {"groupOp":"AND","rules":[{"field":"mobile","op":"eq","data":"13611464542"}]}
    //searchField:
    //searchString:
    //searchOper:


    public Pages toPages() {
        Pages pages = new Pages(page, rows);
        if (pages.getPageSize() < 0) {
            pages.setPageSize(20);
        }
        if (StringUtil.isNotBlank(filters)) {
            JQGridFilter filter = JsonUtil.fromJson(filters, JQGridFilter.class);
            for (JQGridFilterRules filterRules : filter.rules) {
                pages.addCondition(new Conditions(filterRules.getField(), getMatchType(filterRules.getOp()), filterRules.getData()));
            }
        }
        if (StringUtil.isNotBlank(sidx)) {
            pages.setSort(new Sort(new Sort.Order(Sort.Direction.valueOf(sord.toUpperCase()), sidx)));
        }
        return pages;
    }

    private MatchType getMatchType(String filterRuleOp) {
        try {
            switch (JQGridFilterRulesOpration.valueOf(filterRuleOp)) {

                case eq:
                    return MatchType.EQ;
                case ne:
                    return MatchType.NE;
                case lt:
                    return MatchType.LT;
                case le:
                    return MatchType.LTE;
                case gt:
                    return MatchType.GT;
                case ge:
                    return MatchType.GTE;
                case bw:
                    return MatchType.LL;
                case bn:

                    break;
                case in:
                    return MatchType.IN;
                case ni:
                    return MatchType.NIN;
                case ew:
                    return MatchType.RL;
                case en:
                    break;
                case cn:
                    return MatchType.FL;
                case nc:
                    return MatchType.NL;
            }
        } catch (Exception e) {
            return MatchType.FL;
        }
        return MatchType.EQ;
    }
}
