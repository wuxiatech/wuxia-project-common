package cn.wuxia.project.common.model;

import org.springframework.data.annotation.Version;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * common of data create and modify info
 *
 * @author songlin.li
 */
@Embeddable
@MappedSuperclass
public class ModifyInfoVersionJpaEntity extends ModifyInfoJpaEntity implements Serializable {


    private int version;

    public ModifyInfoVersionJpaEntity() {
        super();
    }

    public ModifyInfoVersionJpaEntity(String id) {
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
