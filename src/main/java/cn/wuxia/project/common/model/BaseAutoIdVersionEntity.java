package cn.wuxia.project.common.model;

import lombok.NoArgsConstructor;

import javax.persistence.*;

@MappedSuperclass
@NoArgsConstructor
public abstract class BaseAutoIdVersionEntity extends AbstractPrimaryKeyEntity<Long> {
    private static final long serialVersionUID = -521288223498886805L;
    private Long id;

    private int version;

    public BaseAutoIdVersionEntity(Long id) {
        this.id = id;
    }

    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
