/*
 * Copyright 2011-2020 wuxia.gd.cn All right reserved.
 */
package cn.wuxia.project.common.service.impl;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

import cn.wuxia.project.common.service.CommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.Assert;

import cn.wuxia.project.common.dao.CommonMongoDao;
import cn.wuxia.project.common.model.CommonMongoEntity;
import cn.wuxia.common.exception.AppServiceException;
import cn.wuxia.common.orm.query.Conditions;
import cn.wuxia.common.orm.query.Pages;
import cn.wuxia.common.orm.query.Sort;
import cn.wuxia.common.util.ListUtil;
import cn.wuxia.common.util.reflection.ReflectionUtil;
import org.springframework.util.CollectionUtils;

/**
 * 服务基础实现类。
 *
 * @author songlin.li
 * @since 2013-12-3
 */
public abstract class CommonMongoServiceImpl<E extends CommonMongoEntity, K extends Serializable> implements CommonService<E, K> {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected abstract CommonMongoDao<E, K> getCommonDao();

    @Override
    public E save(E e) {
        try {
            e.validate();
            this.getCommonDao().save(e);
        } catch (Exception e1) {
            throw new AppServiceException("", e1);
        }
        return e;
    }

    @Override
    public void batchSave(Collection<E> collection) {
        if (CollectionUtils.isEmpty(collection)) {
            logger.warn("保存对象为空！");
            return;

        }
        this.getCommonDao().batchSave(collection);
    }

    @Override
    public E update(E e) {
        try {
            e.validate();
            this.getCommonDao().update(e);
        } catch (Exception e1) {
            throw new AppServiceException("", e1);
        }

        return e;
    }

    @Override
    public void delete(E t) {
        Object o = null;
        try {
            o = ReflectionUtil.invokeGetterMethod(t, getIdName(t.getClass()));
        } catch (IntrospectionException e) {
            logger.error("获取id值出错", e);
        }
        delete((K) o);
    }

    @Override
    public void delete(K k) {
        Assert.notNull(k, "id can't be null");
        try {
            this.getCommonDao().deleteById(k);
        } catch (Exception e) {
            throw new AppServiceException("", e);
        }
    }

    @Override
    public void evict(E e) {
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
        return this.getCommonDao().find(new Query());
    }

    @Override
    public List<E> findAll(Sort sort) {
        Pages pages = new Pages();
        pages.setSort(sort);
        pages.setAutoCount(false);
        return findAll(pages).getResult();
    }

    @Override
    public List<E> find(Conditions... condition) {
        Pages pages = new Pages();
        pages.setConditions(ListUtil.arrayToList(condition));
        pages.setAutoCount(false);
        return findAll(pages).getResult();
    }

    @Override
    public List<E> find(Sort sort, Conditions... condition) {
        Pages pages = new Pages();
        pages.setSort(sort);
        pages.setConditions(ListUtil.arrayToList(condition));
        pages.setAutoCount(false);
        return findAll(pages).getResult();
    }
    @Override
    public Pages<E> findAll(Pages<E> pages) {
        return this.getCommonDao().findPage(pages);
    }

    private String getIdName(Class<?> clazz) throws IntrospectionException {
        BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
        PropertyDescriptor[] properties = beanInfo.getPropertyDescriptors();

        String idName = null;
        for (int i = 0; i < properties.length; i++) {
            Method get = properties[i].getReadMethod();
            if (get.getAnnotation(Id.class) == null) {
                continue;
            }
            idName = properties[i].getName();
            break;
        }
        return idName;
    }
}
