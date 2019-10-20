package cn.wuxia.project.common.model;

import cn.wuxia.common.util.StringUtil;
import org.springframework.data.annotation.Id;

/**
 * @author songlin
 */
public class CommonMongoEntity extends AbstractPrimaryKeyEntity<String> {


    private static final long serialVersionUID = -2510725762307951081L;

    protected String id;


    @Override
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
