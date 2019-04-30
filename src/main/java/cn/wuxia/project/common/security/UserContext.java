package cn.wuxia.project.common.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.Collection;

@Setter
@Getter
@AllArgsConstructor
public class UserContext implements Serializable {

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 1L;

    String id;

    String name;

    String mobile;

    String headImg;


    Collection<String> authorities;

    public UserContext(String id, String name, String mobile) {
        this.id = id;
        this.name = name;
        this.mobile = mobile;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
