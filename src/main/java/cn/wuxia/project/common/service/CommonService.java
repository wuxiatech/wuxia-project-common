/*
 * Copyright 2011-2020 wuxia.gd.cn All right reserved.
 */
package cn.wuxia.project.common.service;

import cn.wuxia.common.exception.AppDaoException;
import cn.wuxia.common.orm.query.Conditions;
import cn.wuxia.common.orm.query.Pages;
import cn.wuxia.common.orm.query.Sort;
import cn.wuxia.common.validator.ValidationEntity;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 服务基础接口类。
 *
 * @author songlin.li
 * @since 2013-12-3
 */
public interface CommonService<E extends ValidationEntity, K extends Serializable> {

    /**
     * 保存对象
     *
     * @param t
     * @return
     */
    E save(E t) throws AppDaoException;

    /**
     * 批量保存
     *
     * @param collection
     */
    void batchSave(Collection<E> collection) throws AppDaoException;

    /**
     * 和save一致
     *
     * @param t
     * @return
     */
    E update(E t) throws AppDaoException;

    void delete(E t);

    void delete(K k);

    E findById(K k);

    List<E> findBy(String property, Object value);

    E findUniqueBy(String property, Object value);

    List<E> findIn(String property, Object... value);

    List<E> findAll();

    List<E> findAll(Sort sort);

    Pages<E> findAll(Pages<E> pages);

    List<E> find(Conditions... condition);

    List<E> find(Sort sort, Conditions... condition);

    E findUnique(Conditions... condition);

}
