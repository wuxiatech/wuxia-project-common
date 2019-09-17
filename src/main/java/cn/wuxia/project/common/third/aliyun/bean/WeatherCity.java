package cn.wuxia.project.common.third.aliyun.bean;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author songlin
 */
@Getter
@Setter
public class WeatherCity implements Serializable {
    private static final long serialVersionUID = 7183714922893617757L;
    private String cityid;
    private String parentid;
    private String citycode;
    private String city;
}
