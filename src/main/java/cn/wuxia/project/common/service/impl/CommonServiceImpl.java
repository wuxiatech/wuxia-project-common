/*
 * Copyright 2011-2020 wuxia.gd.cn All right reserved.
 */
package cn.wuxia.project.common.service.impl;

import java.beans.IntrospectionException;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import cn.wuxia.project.common.dao.CommonDao;
import cn.wuxia.project.common.service.CommonService;
import cn.wuxia.common.entity.ValidationEntity;
import cn.wuxia.common.exception.AppDaoException;
import cn.wuxia.common.exception.AppServiceException;
import cn.wuxia.common.orm.query.Conditions;
import cn.wuxia.common.orm.query.Pages;
import cn.wuxia.common.orm.query.Sort;
import cn.wuxia.common.util.HibernateUtils;
import cn.wuxia.common.util.ListUtil;
import cn.wuxia.common.util.reflection.ReflectionUtil;

/**
 * 服务基础实现类。
 *
 * @author songlin.li
 * @since 2013-12-3
 */
@Transactional
public abstract class CommonServiceImpl<E extends ValidationEntity, K extends Serializable> implements CommonService<E, K> {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected abstract CommonDao<E, K> getCommonDao();

    @Override
    public E save(E e) {
        try {
            this.getCommonDao().saveEntity(e);
        } catch (AppDaoException e1) {
            throw new AppServiceException("", e1);
        }
        return e;
    }

    @Override
    public void batchSave(Collection<E> collection) {
        this.getCommonDao().batchSave(collection);
    }

    @Override
    public E update(E e) {
        return save(e);
    }

    @Override
    public void delete(E t) {
        Object o = null;
        try {
            o = ReflectionUtil.invokeGetterMethod(t, HibernateUtils.getIdName(t.getClass()));
        } catch (IntrospectionException e) {
            logger.error("获取id值出错", e);
        }
        delete((K) o);
    }

    @Override
    public void delete(K k) {// throws Exception {
        Assert.notNull(k, "id can't be null");
        this.getCommonDao().deleteEntityById(k);
    }

    @Override
    public void evict(E e) {
        getCommonDao().evict(e);
    }

    @Override
    public E findById(K k) {
        Assert.notNull(k, "id can not be null");
        return this.getCommonDao().findById(k);
    }

    @Override
    public List<E> findBy(String property, Object value) {
        Assert.notNull(property, "property name can not be null");
        return this.getCommonDao().findBy(property, value);
    }

    @Override
    public E findUniqueBy(String property, Object value) {
        Assert.notNull(property, "property name can not be null");
        return this.getCommonDao().findUniqueBy(property, value);
    }

    @Override
    public List<E> findIn(String property, Object... value) {
        Assert.notNull(property, "property name can not be null");
        return this.getCommonDao().findIn(property, value);
    }

    @Override
    public List<E> findAll() {
        return this.getCommonDao().getAll();
    }

    @Override
    public List<E> findAll(Sort sort) {
        return this.getCommonDao().findAll(sort);
    }

    @Override
    public Pages<E> findAll(Pages<E> pages) {
        return this.getCommonDao().findAll(pages);
    }

    @Override
    public List<E> find(Conditions... conditions) {
        return this.getCommonDao().find(this.getCommonDao().buildCriterion(conditions));
    }

    @Override
    public List<E> find(Sort sort, Conditions... conditions) {
        Pages<E> pages = new Pages();
        pages.setSort(sort);
        pages.setConditions(ListUtil.arrayToList(conditions));
        pages.setAutoCount(false);
        return this.getCommonDao().findAll(pages).getResult();
    }
}
