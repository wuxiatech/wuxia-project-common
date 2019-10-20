package cn.wuxia.project.common.model;

import cn.wuxia.common.util.StringUtil;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * common of data create and modify info
 *
 * @author songlin.li
 */
@MappedSuperclass
public abstract class BaseUuidEntity extends AbstractPrimaryKeyEntity<String> {
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 1L;

    private String id;


    public BaseUuidEntity() {
        super();
    }

    public BaseUuidEntity(String id) {
        if (StringUtil.isNotBlank(id)) {
            this.id = id;
        }
    }

    @Override
    @GenericGenerator(name = "hibernate-uuid", strategy = "cn.wuxia.common.entity.Base64UuidGenerator")
    @GeneratedValue(generator = "hibernate-uuid")
    @Column(name = "id", unique = true, nullable = false)
    @Id
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = StringUtil.isNotBlank(id) ? id : null;
    }

}
