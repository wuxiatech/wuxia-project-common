package cn.wuxia.project.common.support;

import cn.wuxia.common.util.DateUtil;
import cn.wuxia.common.util.DateUtil.DateFormatter;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.Date;

public class CustomDateSerializer extends JsonSerializer<Date> {
    @Override
    public void serialize(Date value, JsonGenerator jgen, SerializerProvider provider) throws IOException,
            JsonProcessingException {
        jgen.writeString(DateUtil.dateToString(value, DateFormatter.FORMAT_YYYY_MM_DD_HH_MM_SS));
    }
}