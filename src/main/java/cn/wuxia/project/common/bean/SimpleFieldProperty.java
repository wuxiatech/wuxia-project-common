package cn.wuxia.project.common.bean;

import cn.wuxia.common.util.DateUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
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
