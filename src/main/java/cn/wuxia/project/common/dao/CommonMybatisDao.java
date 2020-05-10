package cn.wuxia.project.common.dao;

import cn.wuxia.project.common.model.AbstractPrimaryKeyEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.io.Serializable;

/**
 * @author songlin
 */
public interface CommonMybatisDao<T extends AbstractPrimaryKeyEntity<K>, K extends Serializable> extends BaseMapper<T> {

}
