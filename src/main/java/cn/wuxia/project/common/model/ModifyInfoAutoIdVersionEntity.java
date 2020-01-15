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
public class ModifyInfoAutoIdVersionEntity extends ModifyInfoAutoIdEntity implements Serializable {
	private static final long serialVersionUID = -1520444981354340825L;
	private int version;

	public ModifyInfoAutoIdVersionEntity() {
		super();
	}

	public ModifyInfoAutoIdVersionEntity(Long id) {
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
