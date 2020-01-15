package cn.wuxia.project.common.model;

import cn.wuxia.common.util.reflection.ReflectionUtil;
import cn.wuxia.common.validator.ValidationEntity;
import org.springframework.data.annotation.Transient;

/**
 * @author songlin
 */
public abstract class AbstractPrimaryKeyEntity<K> extends ValidationEntity {

    public abstract K getId();

    @Transient
    @javax.persistence.Transient
    protected Class<K> getKeyType() {
        return ReflectionUtil.getSuperClassGenricType(getClass());
    }
}
