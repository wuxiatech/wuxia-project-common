package cn.wuxia.project.common.model;

import java.io.Serializable;
import java.util.Date;

/**
 * common of data create and modify info
 * 
 * @author songlin.li
 */
public class ModifyInfoMongoEntity extends CommonMongoEntity implements Serializable {
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 1L;

    protected String createdBy;

    protected Date createdOn;

    protected String modifiedBy;

    protected Date modifiedOn;

    protected Date isObsoleteDate;

    public final static String LOGICAL_DELETE_STATUS_PROPERTY = "isObsoleteDate";
    public ModifyInfoMongoEntity() {

    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public Date getIsObsoleteDate() {
        return isObsoleteDate;
    }

    public void setIsObsoleteDate(Date isObsoleteDate) {
        this.isObsoleteDate = isObsoleteDate;
    }
}
