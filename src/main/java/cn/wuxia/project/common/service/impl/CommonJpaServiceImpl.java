/*
 * Copyright 2011-2020 wuxia.gd.cn All right reserved.
 */
package cn.wuxia.project.common.service.impl;

import cn.wuxia.common.entity.ValidationEntity;
import cn.wuxia.common.exception.AppServiceException;
import cn.wuxia.common.exception.ValidateException;
import cn.wuxia.common.orm.query.Conditions;
import cn.wuxia.common.orm.query.Pages;
import cn.wuxia.common.orm.query.PropertyType;
import cn.wuxia.common.sensitive.ValidtionSensitiveUtil;
import cn.wuxia.common.spring.orm.core.PropertyFilter;
import cn.wuxia.common.spring.orm.core.RestrictionNames;
import cn.wuxia.common.util.*;
import cn.wuxia.common.util.reflection.BeanUtil;
import cn.wuxia.common.util.reflection.ReflectionUtil;
import cn.wuxia.project.common.dao.CommonJpaDao;
import cn.wuxia.project.common.model.ModifyInfoEntity;
import cn.wuxia.project.common.security.UserContextUtil;
import cn.wuxia.project.common.service.CommonService;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.beans.IntrospectionException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 服务基础实现类。
 *
 * @author songlin.li
 * @since 2013-12-3
 */
@Transactional
public abstract class CommonJpaServiceImpl<E extends ValidationEntity, K extends Serializable> implements CommonService<E, K> {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected abstract CommonJpaDao<E, K> getCommonJpaDao();

    private Class<E> entityClass = ReflectionUtil.getSuperClassGenricType(getClass());

    @Override
    public E save(E e) {
        Assert.notNull(e, "实体对象不能为空");
        try {
            String idname = HibernateUtils.getIdName(entityClass);
            K idvalue = (K)ReflectionUtil.invokeGetterMethod(e, idname);
            if(idvalue != null){
                E orignal = findById(idvalue);
                BeanUtil.copyPropertiesWithoutNullValues(orignal, e);
                e = orignal;
            }
            /**
             * 有与框架的及部署的原因，并非在当前线程中可以拿得到用户信息，比如dubbox及webservice
             * 可以在consumer层set值
             */
            if (e instanceof ModifyInfoEntity) {

                String username = null;
                if (StringUtil.isBlank(username) && UserContextUtil.getUserContext() != null) {
                    username = UserContextUtil.getName();
                }
                ModifyInfoEntity infoEntity = new ModifyInfoEntity();
                BeanUtil.copyProperties(infoEntity, e);
                Timestamp time = DateUtil.newInstanceDate();

                if (StringUtil.isNotBlank(idvalue)) {
                    if (StringUtil.isNotBlank(username)) {
                        infoEntity.setModifiedBy(filterEmoji(username));
                    }
                    infoEntity.setModifiedOn(time);
                    if (infoEntity.getCreatedOn() == null) {
                        infoEntity.setCreatedOn(time);
                    }
                } else {
                    String modifiedBy = infoEntity.getModifiedBy();
                    if (StringUtil.isNotBlank(username)) {
                        infoEntity.setCreatedBy(filterEmoji(username));
                    } else if (StringUtil.isNotBlank(modifiedBy)) {
                        infoEntity.setCreatedBy(filterEmoji((String) modifiedBy));
                        infoEntity.setModifiedBy(null);
                    }
                    infoEntity.setCreatedOn(time);
                }
                BeanUtil.copyProperties(e, infoEntity);
            }
        } catch (Exception ex) {
            logger.warn(ex.getMessage(), ex);
        }
        try {
            ValidtionSensitiveUtil.validate(e);
            this.getCommonJpaDao().save(e);
        } catch (Exception e1) {
            throw new AppServiceException("", e1);
        }
        return e;
    }

    /**
     * 临时解决方法，后续数据库createdBy,modifiedBy字段可以升级为utf8mb4
     * 当前是为了兼容旧数据库
     *
     * @param source
     * @return
     * @author songlin
     */
    private String filterEmoji(String source) {
        if (source != null) {
            Pattern emoji = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
                    Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
            Matcher emojiMatcher = emoji.matcher(source);
            if (emojiMatcher.find()) {
                source = emojiMatcher.replaceAll("*");
                return source;
            }
            return source;
        }
        return source;
    }

    @Override
    public void batchSave(Collection<E> entitys) {
        if (ListUtil.isEmpty(entitys)) {
            return;
        }
        Collection<ValidateException> exs = new ArrayList<ValidateException>();
        String idname = null;
        try {
            idname = HibernateUtils.getIdName(entityClass);
        } catch (IntrospectionException e2) {
            logger.warn(e2.getMessage());
        }
        for (E e : entitys) {
            try {

                Object idvalue = ReflectionUtil.invokeGetterMethod(e, idname);
                if (e instanceof ModifyInfoEntity) {
                    /**
                     * 有与框架的及部署的原因，并非在当前线程中可以拿得到用户信息，比如dubbox及webservice
                     * 可以在consumer层set值
                     */
                    String username = null;
                    if (StringUtil.isBlank(username) && UserContextUtil.getUserContext() != null) {
                        username = UserContextUtil.getName();
                    }
                    ModifyInfoEntity infoEntity = new ModifyInfoEntity();
                    BeanUtil.copyProperties(infoEntity, e);
                    Timestamp time = DateUtil.newInstanceDate();

                    if (StringUtil.isNotBlank(idvalue)) {
                        if (StringUtil.isNotBlank(username)) {
                            infoEntity.setModifiedBy(filterEmoji(username));
                        }
                        infoEntity.setModifiedOn(time);
                    } else {
                        String modifiedBy = infoEntity.getModifiedBy();
                        if (StringUtil.isNotBlank(username)) {
                            infoEntity.setCreatedBy(filterEmoji(username));
                        } else if (StringUtil.isNotBlank(modifiedBy)) {
                            infoEntity.setCreatedBy(filterEmoji((String) modifiedBy));
                            infoEntity.setModifiedBy(null);
                        }
                        infoEntity.setCreatedOn(time);
                    }
                    BeanUtil.copyProperties(e, infoEntity);
                }
            } catch (Exception ex) {
                logger.warn(ex.getMessage(), ex);
            }
            try {
                e.validate();
                ValidtionSensitiveUtil.validate(e);
            } catch (ValidateException e1) {
                logger.error("保存有误", e1);
                exs.add(e1);
                continue;
            }
        }
        if (!exs.isEmpty()) {
            throw new AppServiceException("参数有误");
        }
        this.getCommonJpaDao().saveAll(entitys);
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
        E e = findById(k);
        if (e instanceof ModifyInfoEntity) {
            ReflectionUtil.invokeSetterMethod(e, "isObsoleteDate", DateUtil.newInstanceDate(), Timestamp.class);
        }
        logger.debug("delete entity {},id is {}", entityClass.getSimpleName(), k);
        this.getCommonJpaDao().save(e);
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

    /**
     * 简单判断是否存在spring-security
     *
     * @return
     */
    public boolean hasSpringSecurity() {
        try {
            Class clazz = ClassLoaderUtil.loadClass("org.springframework.security.core.Authentication");
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
