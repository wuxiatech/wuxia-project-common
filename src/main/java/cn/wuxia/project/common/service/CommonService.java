/*
 * Copyright 2011-2020 wuxia.gd.cn All right reserved.
 */
package cn.wuxia.project.common.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import cn.wuxia.common.entity.ValidationEntity;
import cn.wuxia.common.orm.query.Conditions;
import cn.wuxia.common.orm.query.Pages;
import cn.wuxia.common.orm.query.Sort;

/**
 * 服务基础接口类。
 *
 * @author songlin.li
 * @since 2013-12-3
 */
public interface CommonService<E extends ValidationEntity, K extends Serializable> {

    /**
     * 保存对象
     * @param t
     * @return
     */
    public E save(E t);

    /**
     * 批量保存
     * @param collection
     */
    public void batchSave(Collection<E> collection);

    /**
     * 和save一致
     * @param t
     * @return
     */
    public E update(E t);

    public void delete(E t);

    public void delete(K k);

    public E findById(K k);

    public List<E> findBy(String property, Object value);

    public E findUniqueBy(String property, Object value);

    public List<E> findIn(String property, Object... value);

    public List<E> findAll();

    public List<E> findAll(Sort sort);

    public Pages<E> findAll(Pages<E> pages);

    public List<E> find(Conditions... condition);

    public List<E> find(Sort sort, Conditions... condition);

    public void evict(E t);
}
