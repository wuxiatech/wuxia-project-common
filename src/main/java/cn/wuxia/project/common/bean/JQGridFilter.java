package cn.wuxia.project.common.bean;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JQGridFilter {


    String groupOp; //"AND",
    List<JQGridFilterRules> rules; //[{"field":"mobile","op":"eq","data":"13611464542"}]}


}
