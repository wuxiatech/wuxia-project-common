package cn.wuxia.project.common.model;

import cn.wuxia.common.entity.ValidationEntity;
import cn.wuxia.common.util.reflection.ReflectionUtil;
import org.springframework.data.annotation.Transient;

/**
 * @author songlin
 */
public abstract class AbstractPrimaryKeyEntity<K> extends ValidationEntity {

    private Class<K> keyType;

    public AbstractPrimaryKeyEntity() {
        this.keyType = ReflectionUtil.getSuperClassGenricType(getClass());
    }

    public abstract K getId();

    @Transient
    @javax.persistence.Transient
    protected Class<K> getKeyType() {
        return keyType;
    }
}
