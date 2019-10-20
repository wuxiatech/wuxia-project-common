package cn.wuxia.project.common.model;

import cn.wuxia.common.util.reflection.ReflectionUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author songlin
 */
@AllArgsConstructor
@Getter
public class BasePrimaryKeyImpl<T> implements BasePrimaryKey<T> {
    Class<T> keyType;

    public BasePrimaryKeyImpl(T value) {
        this.keyType = ReflectionUtil.getSuperClassGenricType(getClass());
        this.value = value;
    }

    private T value;

    public T get() {
        return value;
    }

}
