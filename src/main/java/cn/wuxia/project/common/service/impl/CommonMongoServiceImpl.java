/*
 * Copyright 2011-2020 wuxia.gd.cn All right reserved.
 */
package cn.wuxia.project.common.service.impl;

import cn.wuxia.common.exception.AppDaoException;
import cn.wuxia.common.exception.AppServiceException;
import cn.wuxia.common.exception.ValidateException;
import cn.wuxia.common.orm.query.Conditions;
import cn.wuxia.common.orm.query.Pages;
import cn.wuxia.common.orm.query.Sort;
import cn.wuxia.common.util.DateUtil;
import cn.wuxia.common.util.ListUtil;
import cn.wuxia.common.util.StringUtil;
import cn.wuxia.common.util.reflection.ReflectionUtil;
import cn.wuxia.project.common.dao.CommonMongoDao;
import cn.wuxia.project.common.model.AbstractPrimaryKeyEntity;
import cn.wuxia.project.common.model.ModifyInfoMongoEntity;
import cn.wuxia.project.common.service.CommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotNull;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 服务基础实现类。
 *
 * @author songlin.li
 * @since 2013-12-3
 */
public abstract class CommonMongoServiceImpl<E extends AbstractPrimaryKeyEntity, K extends Serializable> extends AbstractServiceImpl<E, K> implements CommonService<E, K> {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected abstract CommonMongoDao<E, K> getCommonDao();

    @Override
    public E save(E entity) throws AppDaoException {
        try {
            entity.validate();
            invokeModifyInfo(entity);
            this.getCommonDao().save(entity);
        } catch (ValidateException e1) {
            throw new AppDaoException(e1.getMessage());
        }
        return entity;
    }

    @Override
    public void batchSave(Collection<E> entitys) throws AppDaoException {
        if (CollectionUtils.isEmpty(entitys)) {
            logger.warn("保存对象为空！");
            return;
        }
        Collection<String> exs = new ArrayList<String>(entitys.size());
        for (E entity : entitys) {
            try {
                invokeModifyInfo(entity);
                entity.validate();
            } catch (ValidateException e) {
                exs.add(entity.getId() + "：" + e.getMessage());
            }
        }
        if (ListUtil.isNotEmpty(exs)) {
            throw new AppDaoException(StringUtil.join(exs, "/n"));
        }
        getCommonDao().batchSave(entitys);
    }

    @Override
    public E update(E e) throws AppDaoException {
        return save(e);
    }

    @Override
    public void delete(@NotNull E e) {
        Assert.notNull(e.getId(), "id不能为空");
        if (e instanceof ModifyInfoMongoEntity) {
            ReflectionUtil.invokeSetterMethod(e, ModifyInfoMongoEntity.LOGICAL_DELETE_STATUS_PROPERTY, DateUtil.newInstanceDate(), Timestamp.class);
            this.getCommonDao().update(e);
        } else {
            this.getCommonDao().delete(e);
        }
    }

    @Override
    public void delete(K k) {
        Assert.notNull(k, "id can't be null");
        /**
         * 逻辑删
         */
        if (ModifyInfoMongoEntity.class.isAssignableFrom(getCommonDao().getEntityClass())) {
            Query query = new Query(Criteria.where("id").is(k));
            Update update = Update.update(ModifyInfoMongoEntity.LOGICAL_DELETE_STATUS_PROPERTY, DateUtil.newInstanceDate());
            this.getCommonDao().update(query, update);
        } else {
            try {
                this.getCommonDao().deleteById(k);
            } catch (Exception e) {
                throw new AppServiceException("", e);
            }
        }
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
        return this.getCommonDao().findPage(pages).getResult();
    }

    @Override
    public E findUnique(Conditions... condition) {
        return this.getCommonDao().findUniqueBy(condition);
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
