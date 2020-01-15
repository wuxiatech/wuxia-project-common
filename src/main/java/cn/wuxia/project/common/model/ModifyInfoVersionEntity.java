package cn.wuxia.project.common.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.io.Serializable;

/**
 * common of data create and modify info
 * 
 * @author songlin.li
 */
@Embeddable
@MappedSuperclass
public class ModifyInfoVersionEntity extends ModifyInfoEntity implements Serializable {
	private static final long serialVersionUID = 5510870682551652759L;
	private int version;

	public ModifyInfoVersionEntity() {
		super();
	}

	public ModifyInfoVersionEntity(String id) {
		super(id);
	}
	@Version
	@Column(name = "_version")
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}
}
