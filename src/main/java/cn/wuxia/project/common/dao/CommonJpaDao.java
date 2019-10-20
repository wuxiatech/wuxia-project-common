package cn.wuxia.project.common.dao;

import cn.wuxia.common.spring.orm.core.jpa.repository.BasicJpaRepository;

import java.io.Serializable;

/**
 * @author songlin
 */
public interface CommonJpaDao<T extends Serializable, K extends Serializable> extends BasicJpaRepository<T, K> {

}
