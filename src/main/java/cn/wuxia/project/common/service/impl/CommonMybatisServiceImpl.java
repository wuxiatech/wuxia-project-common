/*
 * Copyright 2011-2020 wuxia.gd.cn All right reserved.
 */
package cn.wuxia.project.common.service.impl;

import cn.wuxia.common.exception.AppDaoException;
import cn.wuxia.common.exception.ValidateException;
import cn.wuxia.common.orm.query.Conditions;
import cn.wuxia.common.orm.query.Pages;
import cn.wuxia.common.util.DateUtil;
import cn.wuxia.common.util.ListUtil;
import cn.wuxia.common.util.StringUtil;
import cn.wuxia.common.util.reflection.ReflectionUtil;
import cn.wuxia.project.common.dao.CommonMybatisDao;
import cn.wuxia.project.common.model.AbstractPrimaryKeyEntity;
import cn.wuxia.project.common.model.ModifyInfoEntity;
import cn.wuxia.project.common.service.CommonService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.util.Assert;

import java.io.Serializable;
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
public abstract class CommonMybatisServiceImpl<E extends AbstractPrimaryKeyEntity<K>, K extends Serializable> extends AbstractServiceImpl<E, K> implements CommonService<E, K> {


    protected abstract CommonMybatisDao<E, K> getCommonDao();

    private Class<E> entityClass = ReflectionUtil.getSuperClassGenricType(getClass());

    protected MyBatisServiceImpl myBatisService;

    CommonMybatisServiceImpl() {
        myBatisService = new MyBatisServiceImpl();
    }

    class MyBatisServiceImpl extends ServiceImpl<CommonMybatisDao<E, K>, E> {

    }


    @Override
    public E save(E e) throws AppDaoException {
        Assert.notNull(e, "实体对象不能为空");
        try {
            e.validate();
        } catch (ValidateException ex) {
            throw new AppDaoException(ex.getMessage());
        }
        invokeModifyInfo(e);
        this.getCommonDao().insert(e);
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
//        this.getCommonDao(). (entitys);
    }

    @Override
    public E update(E e) throws AppDaoException {
        Assert.notNull(e, "实体对象不能为空");
        try {
            e.validate();
        } catch (ValidateException ex) {
            throw new AppDaoException(ex.getMessage());
        }
        invokeModifyInfo(e);
        this.getCommonDao().updateById(e);
        return e;
    }

    @Override
    public void delete(E e) {
        logger.debug("delete entity {},id is {}", entityClass.getSimpleName());
        if (e instanceof ModifyInfoEntity) {
            ReflectionUtil.invokeSetterMethod(e, ModifyInfoEntity.LOGICAL_DELETE_STATUS_PROPERTY, DateUtil.newInstanceDate(), Timestamp.class);
            this.getCommonDao().updateById(e);
        } else {
            this.getCommonDao().deleteById(e.getId());
        }
    }

    @Override
    public void delete(K k) {
        Assert.notNull(k, "id can't be null");
        E e = findById(k);
        delete(e);
    }


    @Override
    public E findById(K k) {
        Assert.notNull(k, "id can not be null");
        return this.getCommonDao().selectById(k);
    }

    @Override
    public List<E> findBy(String property, Object value) {
        Assert.notNull(property, "property name can not be null");
        QueryWrapper<E> queryWrapper = new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper();
        queryWrapper.eq(property, value);
        return this.getCommonDao().selectList(queryWrapper);
    }

    @Override
    public E findUniqueBy(String property, Object value) {
        Assert.notNull(property, "property name can not be null");
        QueryWrapper<E> queryWrapper = new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper();
        queryWrapper.eq(property, value);
        return this.getCommonDao().selectOne(queryWrapper);
    }

    @Override
    public List<E> findIn(String property, Object... value) {
        Assert.notNull(property, "property name can not be null");
        QueryWrapper<E> queryWrapper = new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper();
        queryWrapper.in(property, value);
        return this.getCommonDao().selectList(queryWrapper);
    }

    @Override
    public List<E> findAll() {
        return this.getCommonDao().selectList(new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper());
    }

    @Override
    public List<E> findAll(cn.wuxia.common.orm.query.Sort sort) {
        Pages page = new Pages();
        page.setSort(sort);
        page.setAutoCount(false);
        return findAll(page).getResult();
    }

    @Override
    public Pages<E> findAll(Pages<E> pages) {
        Page page = new Page(pages.getPageNo(), pages.getPageSize());
        page = this.getCommonDao().selectPage(page, condition2QueryWrapper(pages.getConditions()));
        pages.setTotalCount(page.getTotal());
        pages.setResult(page.getRecords());
        return pages;
    }

    QueryWrapper<E> condition2QueryWrapper(List<Conditions> conditions) {
        QueryWrapper<E> queryWrapper = new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper();
        if (ListUtil.isNotEmpty(conditions)) {
            conditions.stream().forEachOrdered(conditions1 -> {
                switch (conditions1.getMatchType()) {

                    case EQ:
                        queryWrapper.eq(conditions1.getProperty(), conditions1.getPropertyValue());
                        break;
                    case NE:
                        queryWrapper.ne(conditions1.getProperty(), conditions1.getPropertyValue());
                        break;
                    case ISN:
                        queryWrapper.isNull(conditions1.getProperty());
                        break;
                    case INN:
                        queryWrapper.isNotNull(conditions1.getProperty());
                        break;
                    case LL:
                        queryWrapper.likeLeft(conditions1.getProperty(), conditions1.getPropertyValue());
                        break;
                    case RL:
                        queryWrapper.likeRight(conditions1.getProperty(), conditions1.getPropertyValue());
                        break;
                    case FL:
                        queryWrapper.like(conditions1.getProperty(), conditions1.getPropertyValue());
                        break;
                    case NL:
                        queryWrapper.notLike(conditions1.getProperty(), conditions1.getPropertyValue());
                        break;
                    case LT:
                        queryWrapper.lt(conditions1.getProperty(), conditions1.getPropertyValue());
                        break;
                    case GT:
                        queryWrapper.gt(conditions1.getProperty(), conditions1.getPropertyValue());
                        break;
                    case GTE:
                        queryWrapper.ge(conditions1.getProperty(), conditions1.getPropertyValue());
                        break;
                    case LTE:
                        queryWrapper.le(conditions1.getProperty(), conditions1.getPropertyValue());
                        break;
                    case IN:
                        queryWrapper.in(conditions1.getProperty(), conditions1.getPropertyValue());
                        break;
                    case NIN:
                        queryWrapper.notIn(conditions1.getProperty(), conditions1.getPropertyValue());
                        break;
                    case BW:
                        queryWrapper.between(conditions1.getProperty(), conditions1.getPropertyValue(), conditions1.getAnotherValue());
                        break;
                }
            });
        }
        return queryWrapper;
    }


    @Override
    public List<E> find(Conditions... conditions) {
        return this.getCommonDao().selectList(condition2QueryWrapper(ListUtil.arrayToList(conditions)));
    }

    @Override
    public List<E> find(cn.wuxia.common.orm.query.Sort sort, Conditions... conditions) {
        return this.getCommonDao().selectList(condition2QueryWrapper(ListUtil.arrayToList(conditions)));
    }
}
