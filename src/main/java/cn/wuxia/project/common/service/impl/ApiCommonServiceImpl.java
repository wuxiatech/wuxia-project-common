/*
 * Copyright 2011-2020 武侠科技 All right reserved.
 */
package cn.wuxia.project.common.service.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.fasterxml.jackson.databind.util.ClassUtil;

import cn.wuxia.project.common.bean.CommonDto;
import cn.wuxia.project.common.model.CommonEntity;
import cn.wuxia.project.common.model.CommonMongoEntity;
import cn.wuxia.project.common.service.CommonService;
import cn.wuxia.common.exception.AppServiceException;
import cn.wuxia.common.exception.ValidateException;
import cn.wuxia.common.orm.query.Conditions;
import cn.wuxia.common.orm.query.Pages;
import cn.wuxia.common.orm.query.Sort;
import cn.wuxia.common.util.StringUtil;
import cn.wuxia.common.util.reflection.BeanUtil;
import cn.wuxia.common.util.reflection.ReflectionUtil;

/**
 * 当返回的对象（泛型对象不是数据库对应的实体或者泛型对象不是CommonEntity/CommonMongoEntity的子类时调用该方法）
 *
 * @author songlin.li
 * @since 2013-12-3
 */
public abstract class ApiCommonServiceImpl<E extends CommonDto, T extends CommonEntity, K extends Serializable>
        implements CommonService<E, K>, InitializingBean {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected Class<E> dtoClass = ReflectionUtil.getSuperClassGenricType(getClass(), 0);

    protected Class<T> entityClass = ReflectionUtil.getSuperClassGenricType(getClass(), 1);
    /**
     * 2选1
     *
     * @author songlin
     * @return
     */
    private CommonServiceImpl<T, K> commonService;

    /**
     * 2选1
     *
     * @author songlin
     * @return
     */
    private CommonMongoServiceImpl<? extends CommonMongoEntity, K> commonMongoService;


    public ApiCommonServiceImpl() {
        if (dtoClass.getSimpleName().equals("Object")) {
            logger.info("实现类：{}", getClass());
            dtoClass = ReflectionUtil.getSuperClassGenricType(getClass().getSuperclass());
            logger.info("初始化父类：{} 并且泛型对象为：{}", getClass().getSuperclass(), dtoClass);
        } else {
            logger.info("初始化父类：{} 并且泛型对象为：{}", getClass(), dtoClass);
        }
    }

    public ApiCommonServiceImpl(CommonServiceImpl<T, K> commonService) {
        this();
        this.commonService = commonService;
        entityClass = ReflectionUtil.getSuperClassGenricType(commonService.getClass());
        logger.info("dtoClass={};entityClass={}", dtoClass, entityClass);
    }

    public ApiCommonServiceImpl(CommonMongoServiceImpl<? extends CommonMongoEntity, K> commonMongoService) {
        this();
        this.commonMongoService = commonMongoService;
        entityClass = ReflectionUtil.getSuperClassGenricType(commonMongoService.getClass());
        logger.info("dtoClass={};entityClass={}", dtoClass, entityClass);
    }

    public void setCommonService(CommonServiceImpl<T, K> commonService) {
        this.commonService = commonService;
        entityClass = ReflectionUtil.getSuperClassGenricType(commonService.getClass());
        logger.info("dtoClass={};entityClass={}", dtoClass, entityClass);
    }

    /**
     * 2选1
     *
     * @return
     * @author songlin
     */
    public CommonServiceImpl<T, K> getCommonService() {
        return commonService;
    }

    public void setCommonMongoService(CommonMongoServiceImpl<? extends CommonMongoEntity, K> commonMongoService) {
        this.commonMongoService = commonMongoService;
        entityClass = ReflectionUtil.getSuperClassGenricType(commonMongoService.getClass());
        logger.info("dtoClass={};entityClass={}", dtoClass, entityClass);
    }

    /**
     * 2选1
     *
     * @return
     * @author songlin
     */
    public CommonMongoServiceImpl<? extends CommonMongoEntity, K> getCommonMongoService() {
        return commonMongoService;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (dtoClass.getSimpleName().equals("Object")) {
            logger.info("实现类：{}", getClass());
            dtoClass = ReflectionUtil.getSuperClassGenricType(getClass().getSuperclass());
            logger.info("初始化父类：{} 并且泛型对象为：{}", getClass().getSuperclass(), dtoClass);
        } else {
            logger.info("初始化父类：{} 并且泛型对象为：{}", getClass(), dtoClass);
        }
    }

    private Object byId(K id) {
        if (getCommonService() != null) {
            return this.getCommonService().findById(id);
        } else if (getCommonMongoService() != null) {
            return this.getCommonMongoService().findById(id);
        }
        return null;
    }

    public E findById(K id) {
        Assert.notNull(id, "id不能为空");

        Object source = byId(id);

        if (source == null)
            return null;

        E target = null;
        try {
            target = dtoClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            logger.error("转换出错", e);

        }

        BeanUtil.copyProperties(target, source);
        return target;
    }

    public void delete(K id) {
        Assert.notNull(id, "id不能为空");
        if (getCommonService() != null) {
            this.getCommonService().delete(id);
        } else if (getCommonMongoService() != null) {
            try {
                this.getCommonMongoService().delete(id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public E save(E e) {
        Assert.notNull(e, "实体对象不能为空");
        try {
            e.validate();
        } catch (ValidateException e1) {
            throw new AppServiceException("", e1);
        }
        Object target = null;
        if (StringUtil.isNotBlank(e.getId())) {
            target = byId((K) e.getId());
        }
        if (target == null) {
            target = ClassUtil.createInstance(entityClass, false);
        }
        BeanUtil.copyPropertiesWithoutNullValues(target, e);

        /**
         * 检查敏感字
         */
        try {
            if (getCommonService() != null) {
                ReflectionUtil.invokeMethod(getCommonService(), "save", new Class[]{entityClass}, new Object[]{target});
                //                this.getCommonDao().saveEntity(target);
            } else if (getCommonMongoService() != null) {
                ReflectionUtil.invokeMethod(getCommonMongoService(), "save", new Class[]{entityClass}, new Object[]{target});
                //                getCommonMongoDao().save( target);
            }
        } catch (Exception e1) {
            logger.error("保存出错", e1);
            throw new AppServiceException("保存出错", e1);
        }

        BeanUtil.copyProperties(e, target);

        return e;
    }

    public void batchSave(Collection<E> entitys) {
        for (E e : entitys) {
            save(e);
        }
    }

    @Override
    public E update(E t) {
        return save(t);
    }

    @Override
    public void delete(E t) {
        delete((K) t.getId());
    }

    @Override
    public List<E> findBy(String property, Object value) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public E findUniqueBy(String property, Object value) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<E> findIn(String property, Object... value) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<E> findAll() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<E> findAll(Sort sort) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Pages<E> findAll(Pages<E> pages) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<E> find(Conditions... condition) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<E> find(Sort sort, Conditions... condition) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void evict(E t) {
        // TODO Auto-generated method stub

    }

}
