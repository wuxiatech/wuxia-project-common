/*
 * Created on :4 Aug, 2014
 * Author     :songlin
 * Change History
 * Version       Date         Author           Reason
 * <Ver.No>     <date>        <who modify>       <reason>
 * Copyright 2014-2020 武侠科技 All right reserved.
 */
package cn.wuxia.project.common.model;

import cn.wuxia.common.entity.Base64UuidGenerator;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

/**
 * [ticket id]
 * Base64压缩UUID长度替换Hibernate原有UUID生成器
 * {@link http://my.oschina.net/noahxiao/blog/132277?p=1}
 *
 * @author songlin
 * @ Version : V<Ver.No> <4 Aug, 2014>
 */
public class BasePrimaryKeyGenerator implements IdentifierGenerator {


    @Override
    public BasePrimaryKey generate(SharedSessionContractImplementor arg0, Object arg1) throws HibernateException {
        System.out.println(arg1);
        return new BasePrimaryKeyImpl(Base64UuidGenerator.UuidUtils.compressedUuid());
    }
}
