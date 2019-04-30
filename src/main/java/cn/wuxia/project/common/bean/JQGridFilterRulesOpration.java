package cn.wuxia.project.common.bean;

public enum JQGridFilterRulesOpration {
    eq("等于"),
    ne("不等"),
    lt("小于"),
    le("小于等于"),
    gt("大于"),
    ge("大于等于"),
    bw("开始于"),
    bn("不开始于"),
    in("在内"),
    ni("不在内"),
    ew("结束于"),
    en("不结束于"),
    cn("包含"),
    nc("不包含");

    private String displayName;

    JQGridFilterRulesOpration(String displayName) {
        displayName = displayName;
    }


    public String getDisplayName() {
        return displayName;
    }
}
