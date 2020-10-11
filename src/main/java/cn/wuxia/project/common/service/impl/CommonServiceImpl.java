/*
 * Copyright 2011-2020 wuxia.gd.cn All right reserved.
 */
package cn.wuxia.project.common.service.impl;

import cn.wuxia.common.exception.AppDaoException;
import cn.wuxia.common.exception.ValidateException;
import cn.wuxia.common.orm.query.Conditions;
import cn.wuxia.common.orm.query.Pages;
import cn.wuxia.common.orm.query.Sort;
import cn.wuxia.common.util.ListUtil;
import cn.wuxia.common.util.StringUtil;
import cn.wuxia.project.common.dao.CommonDao;
import cn.wuxia.project.common.model.AbstractPrimaryKeyEntity;
import cn.wuxia.project.common.model.ModifyInfoEntity;
import cn.wuxia.project.common.service.CommonService;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 服务基础实现类。
 *
 * @author songlin.li
 * @since 2013-12-3
 */
public abstract class CommonServiceImpl<E extends AbstractPrimaryKeyEntity, K extends Serializable> extends AbstractServiceImpl<E, K> implements CommonService<E, K> {


    protected abstract CommonDao<E, K> getCommonDao();

    @Override
    public E save(E entity) throws AppDaoException {
        try {
            invokeModifyInfo(entity);
            entity.validate();
            this.getCommonDao().save(entity);
        } catch (Exception e) {
            throw new AppDaoException(e);
        }
        return entity;
    }

    @Override
    public void batchSave(Collection<E> entitys) throws AppDaoException {
        Assert.notEmpty(entitys, "保存对象不能为空");


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
        this.getCommonDao().batchSave(entitys);
    }

    @Override
    public E update(E e) throws AppDaoException {
        return save(e);
    }

    @Override
    public void delete(E e) {
        if (e instanceof ModifyInfoEntity) {
            getCommonDao().deleteByLogical((K) e.getId());
        } else {
            getCommonDao().delete(e);
        }
    }

    @Override
    public void delete(K k) {// throws Exception {
        Assert.notNull(k, "id can't be null");
        if (ModifyInfoEntity.class.isAssignableFrom(getCommonDao().getEntityClass())) {
            this.getCommonDao().deleteByLogical(k);
        } else {
            this.getCommonDao().delete(k);
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
