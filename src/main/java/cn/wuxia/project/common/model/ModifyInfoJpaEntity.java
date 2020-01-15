package cn.wuxia.project.common.model;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * common of data create and modify info
 * 
 * @author songlin.li
 */
@Embeddable
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class ModifyInfoJpaEntity extends BaseUuidEntity implements Serializable {
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 1L;

	protected String createdBy;

	protected Timestamp createdOn;

	protected String modifiedBy;

	protected Timestamp modifiedOn;

	protected Timestamp isObsoleteDate;

	public final static String ISOBSOLETE_DATE_IS_NULL = " is_Obsolete_Date is null ";
	public final static String LOGICAL_DELETE_STATUS_PROPERTY = "isObsoleteDate";
	public final static String LOGICAL_DELETE_STATUS_COLUMN = "is_Obsolete_Date";

	public ModifyInfoJpaEntity() {
		super();
	}

	public ModifyInfoJpaEntity(String id) {
		super(id);
	}

	@CreatedBy
	@Column(name = "CREATED_BY", updatable = false)
	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	@CreatedDate
	@Column(name = "CREATED_ON", updatable = false)
	public Timestamp getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Timestamp createdOn) {
		this.createdOn = createdOn;
	}

	@LastModifiedBy
	@Column(name = "MODIFIED_BY")
	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	@LastModifiedDate
	@Column(name = "MODIFIED_ON")
	public Timestamp getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(Timestamp modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

//	@StateDelete(type = PropertyType.D, propertyName = "isObsoleteDate", value = "")
	@Column(name = "IS_OBSOLETE_DATE")
	public Timestamp getIsObsoleteDate() {
		return isObsoleteDate;
	}

	public void setIsObsoleteDate(Timestamp isObsoleteDate) {
		this.isObsoleteDate = isObsoleteDate;
	}
}
