package cn.wuxia.project.common.bean;

import cn.wuxia.common.util.DateUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.Date;

public class SimpleFieldProperty implements Serializable {

    private static final long serialVersionUID = 3615587405927351190L;

    String name;

    Object value;

    public SimpleFieldProperty() {
    }

    public SimpleFieldProperty(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public static SimpleFieldProperty value(String value) {
        return new SimpleFieldProperty(null, value);
    }

    public SimpleFieldProperty name(String name) {
        setName(name);
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @JsonIgnore
    public String getStringValue() {
        return getValue() == null ? "" : getValue().toString();
    }

    @JsonIgnore
    public Date getDateValue() {
        if (getValue() == null)
            return null;
        if (getValue() instanceof Date) {
            return (Date) getValue();
        } else if (getValue() instanceof String) {
            try {
                return DateUtil.stringToDate(getStringValue());
            } catch (NumberFormatException e) {
                return null;
            }
        } else {
            throw new ClassCastException("" + getValue());
        }
    }

    @JsonIgnore
    public boolean getBooleanValue() {
        if (getValue() == null)
            return false;
        if (getValue() instanceof Boolean) {
            return BooleanUtils.toBoolean((Boolean) getValue());
        } else if (getValue() instanceof String)
            return BooleanUtils.toBoolean(getStringValue());
        else
            throw new ClassCastException("" + getValue());
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
