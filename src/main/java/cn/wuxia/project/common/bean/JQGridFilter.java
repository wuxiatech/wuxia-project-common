package cn.wuxia.project.common.bean;

import java.util.List;

public class JQGridFilter {


    String groupOp; //"AND",
    List<JQGridFilterRules> rules; //[{"field":"mobile","op":"eq","data":"13611464542"}]}


    public String getGroupOp() {
        return groupOp;
    }

    public void setGroupOp(String groupOp) {
        this.groupOp = groupOp;
    }

    public List<JQGridFilterRules> getRules() {
        return rules;
    }

    public void setRules(List<JQGridFilterRules> rules) {
        this.rules = rules;
    }
}
