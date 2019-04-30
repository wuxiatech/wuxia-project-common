package cn.wuxia.project.common.dao;

import java.io.Serializable;

import cn.wuxia.common.entity.ValidationEntity;
import cn.wuxia.common.spring.orm.core.jpa.repository.BasicJpaRepository;

public interface CommonJpaDao<T extends ValidationEntity, K extends Serializable> extends BasicJpaRepository<T, K> {

}
