/*
 * Copyright 2011-2020 wuxia.gd.cn All right reserved.
 */
package cn.wuxia.project.common.service.impl;

import cn.wuxia.common.exception.AppDaoException;
import cn.wuxia.common.exception.ValidateException;
import cn.wuxia.common.orm.query.Conditions;
import cn.wuxia.common.orm.query.Pages;
import cn.wuxia.common.orm.query.PropertyType;
import cn.wuxia.common.sensitive.ValidtionSensitiveUtil;
import cn.wuxia.common.spring.orm.core.PropertyFilter;
import cn.wuxia.common.spring.orm.core.RestrictionNames;
import cn.wuxia.common.util.DateUtil;
import cn.wuxia.common.util.ListUtil;
import cn.wuxia.common.util.StringUtil;
import cn.wuxia.common.util.reflection.ReflectionUtil;
import cn.wuxia.project.common.dao.CommonJpaDao;
import cn.wuxia.project.common.model.AbstractPrimaryKeyEntity;
import cn.wuxia.project.common.model.ModifyInfoEntity;
import cn.wuxia.project.common.service.CommonService;
import com.google.common.collect.Lists;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * 服务基础实现类。
 *
 * @author songlin.li
 * @since 2013-12-3
 */
public abstract class CommonJpaServiceImpl<E extends AbstractPrimaryKeyEntity, K extends Serializable> extends AbstractServiceImpl<E, K> implements CommonService<E, K> {


    protected abstract CommonJpaDao<E, K> getCommonJpaDao();

    private Class<E> entityClass = ReflectionUtil.getSuperClassGenricType(getClass());

    @Override
    public E save(E e) throws AppDaoException {
        Assert.notNull(e, "实体对象不能为空");
        try {
            ValidtionSensitiveUtil.validate(e);
            e.validate();
        } catch (ValidateException ex) {
            throw new AppDaoException(ex.getMessage());
        }
        invokeModifyInfo(e);
        this.getCommonJpaDao().save(e);
        return e;
    }


    @Override
    public void batchSave(Collection<E> entitys) throws AppDaoException {
        if (ListUtil.isEmpty(entitys)) {
            return;
        }

        Collection<String> exs = new ArrayList<String>(entitys.size());
        for (E e : entitys) {
            try {
                e.validate();
                ValidtionSensitiveUtil.validate(e);
            } catch (ValidateException e1) {
                logger.error("参数有误", e1);
                exs.add(e1.getMessage());
                continue;
            }
            invokeModifyInfo(e);
        }
        if (!exs.isEmpty()) {
            throw new AppDaoException(StringUtil.join(exs, "/n"));
        }
        this.getCommonJpaDao().saveAll(entitys);
    }

    @Override
    public E update(E e) throws AppDaoException {
        return save(e);
    }

    @Override
    public void delete(E e) {
        logger.debug("delete entity {},id is {}", entityClass.getSimpleName());
        if (e instanceof ModifyInfoEntity) {
            ReflectionUtil.invokeSetterMethod(e, ModifyInfoEntity.LOGICAL_DELETE_STATUS_PROPERTY, DateUtil.newInstanceDate(), Timestamp.class);
            this.getCommonJpaDao().save(e);
        } else {
            this.getCommonJpaDao().delete(e);
        }
    }

    @Override
    public void delete(K k) {
        Assert.notNull(k, "id can't be null");
        E e = findById(k);
        delete(e);
    }

    @Override
    public void evict(E e) {

    }

    @Override
    public E findById(K k) {
        Assert.notNull(k, "id can not be null");
        Optional<E> optional = this.getCommonJpaDao().findById(k);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    @Override
    public List<E> findBy(String property, Object value) {
        Assert.notNull(property, "property name can not be null");
        return this.getCommonJpaDao().findBy(property, value);
    }

    @Override
    public E findUniqueBy(String property, Object value) {
        Assert.notNull(property, "property name can not be null");
        return this.getCommonJpaDao().findOneBy(property, value);
    }

    @Override
    public List<E> findIn(String property, Object... value) {
        Assert.notNull(property, "property name can not be null");
        List<PropertyFilter> filters = Lists.newArrayList();
        PropertyFilter filter = new PropertyFilter();
        filter.setRestrictionName(RestrictionNames.IN);
        filter.setPropertyType(PropertyType.S.getValue());
        filter.setPropertyNames(new String[]{property});
        filter.setMatchValue(StringUtil.join(value, ","));
        filters.add(filter);
        return this.getCommonJpaDao().findBy(filters);
    }

    @Override
    public List<E> findAll() {
        return this.getCommonJpaDao().findAll();
    }

    @Override
    public List<E> findAll(cn.wuxia.common.orm.query.Sort sort) {
        Pages page = new Pages();
        page.setSort(sort);
        return findAll(page).getResult();
    }

    @Override
    public Pages<E> findAll(Pages<E> pages) {
        return this.getCommonJpaDao().findPage(pages);
    }

    @Override
    public List<E> find(Conditions... conditions) {
        Pages page = new Pages();
        page.setConditions(ListUtil.arrayToList(conditions));
        return findAll(page).getResult();

    }

    @Override
    public List<E> find(cn.wuxia.common.orm.query.Sort sort, Conditions... conditions) {
        Pages page = new Pages();
        page.setSort(sort);
        page.setConditions(ListUtil.arrayToList(conditions));
        return findAll(page).getResult();
    }
}
