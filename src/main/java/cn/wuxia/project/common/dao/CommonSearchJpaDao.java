package cn.wuxia.project.common.dao;

import cn.wuxia.common.entity.ValidationEntity;
import cn.wuxia.common.spring.orm.core.jpa.repository.BasicJpaRepository;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.io.Serializable;

public interface CommonSearchJpaDao<T extends ValidationEntity, K extends Serializable> extends ElasticsearchRepository<T, K>, CommonJpaDao<T, K> {

}
