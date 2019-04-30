package cn.wuxia.project.common.bean;

import cn.wuxia.project.common.model.CommonEntityApi;
import cn.wuxia.project.common.support.CustomDateSerializer;
import cn.wuxia.common.entity.ValidationEntity;
import cn.wuxia.common.util.StringUtil;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.util.Date;

/**
 * common of data create and modify info
 * 
 * @author songlin.li
 */
public abstract class CommonDto extends ValidationEntity implements Serializable, CommonEntityApi {
    private static final long serialVersionUID = -8107269594757837046L;
    /**
     * Comment for <code>serialVersionUID</code>
     */

    protected String id;

    protected String modifiedBy;

    protected Date modifiedOn;

    protected Date isObsoleteDate;

    public CommonDto() {
        super();
    }

    public CommonDto(String id) {
        if (StringUtil.isNotBlank(id)) {
            this.id = id;
        }
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = StringUtil.isNotBlank(id) ? id : null;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    @JsonSerialize(using = CustomDateSerializer.class)
    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    @JsonSerialize(using = CustomDateSerializer.class)
    public Date getIsObsoleteDate() {
        return isObsoleteDate;
    }

    public void setIsObsoleteDate(Date isObsoleteDate) {
        this.isObsoleteDate = isObsoleteDate;
    }

}
