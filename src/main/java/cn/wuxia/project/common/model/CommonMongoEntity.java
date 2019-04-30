package cn.wuxia.project.common.model;

import java.io.Serializable;

import org.springframework.data.annotation.Id;

import cn.wuxia.common.entity.ValidationEntity;
import cn.wuxia.common.util.StringUtil;

public class CommonMongoEntity extends ValidationEntity implements Serializable, CommonEntityApi {


    private static final long serialVersionUID = -2510725762307951081L;

    protected String id;


    @Id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        if (StringUtil.isNotBlank(id)) {
            this.id = id;
        }
    }
}
