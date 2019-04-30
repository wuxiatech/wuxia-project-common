package cn.wuxia.project.common.dao;

import cn.wuxia.common.entity.ValidationEntity;
import cn.wuxia.common.exception.AppDaoException;
import cn.wuxia.common.exception.AppServiceException;
import cn.wuxia.common.exception.ValidateException;
import cn.wuxia.common.hibernate.dao.BasicHibernateDao;
import cn.wuxia.common.sensitive.ValidtionSensitiveUtil;
import cn.wuxia.common.util.*;
import cn.wuxia.common.util.reflection.BeanUtil;
import cn.wuxia.common.util.reflection.ReflectionUtil;
import cn.wuxia.project.common.model.ModifyInfoEntity;
import cn.wuxia.project.common.security.UserContextUtil;
import org.hibernate.SessionFactory;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.persister.entity.AbstractEntityPersister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate5.SessionFactoryUtils;
import org.springframework.util.Assert;

import javax.persistence.Table;
import java.beans.IntrospectionException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 基础Dao。
 *
 * @author songlin.li
 * @since 2013-6-19
 */
public abstract class CommonDao<T extends ValidationEntity, PK extends Serializable> extends BasicHibernateDao<T, PK> implements DaoInterface {
    private JdbcTemplate jdbcTemplate;

    /**
     * 不同的数据源使用不同的sessionFactory(名字不同)
     */
    @Autowired
    public void setSessionFactory(final SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        this.jdbcTemplate = new JdbcTemplate(SessionFactoryUtils.getDataSource(sessionFactory));
    }

    protected JdbcTemplate getJdbcTemplate() {
        if (jdbcTemplate == null)
            this.jdbcTemplate = new JdbcTemplate(SessionFactoryUtils.getDataSource(sessionFactory));
        return jdbcTemplate;
    }

    /**
     * @param k
     * @return
     * @author songlin
     */
    public T findById(PK k) {
        Assert.notNull(k, "id不能为空");
        return get(k);
    }

    @Override
    public void save(T e) {
        try {
            saveEntity(e);
        } catch (AppDaoException e1) {
            throw new AppServiceException("保存有误", e1);
        }
    }

    @Override
    public void saveEntity(T e) throws AppDaoException {
        Assert.notNull(e, "实体对象不能为空");
        try {
            String idname = HibernateUtils.getIdName(entityClass);
            Object idvalue = ReflectionUtil.invokeGetterMethod(e, idname);

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
            super.saveEntity(e);
        } catch (ValidateException e1) {
            throw new AppDaoException(e1);
        }
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
    public void batchSave(Collection<T> entitys) {
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
        for (T e : entitys) {
            try {

                Object idvalue = ReflectionUtil.invokeGetterMethod(e, idname);

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
        super.batchSave(entitys);
    }

    /**
     * get an entity for logical delete
     *
     * @return
     * @author songlin.li
     */
    @Override
    public T getEntityById(final Serializable id) {
        Table table = entityClass.getAnnotation(Table.class);
        String schema = "";
        if (table != null && StringUtil.isNotBlank(table.schema()))
            schema = table.schema() + ".";
        String sql = "select * from " + schema + table.name() + " where " + getIdName() + "= ?";
        return queryUnique(sql, entityClass, id);
    }

    /**
     * logical delete, update set isObsoleteDate = system date rewrite delete
     * entity for service partner business
     *
     * @param id
     * @author songlin.li
     */
    @Override
    public void deleteEntityById(final Serializable id) {
        Assert.notNull(id, "id Can not be null");
        String hql = "update " + entityClass.getSimpleName() + " set isObsoleteDate = ? where " + getIdName() + "= ?";
        createQuery(hql, new Date(), id).executeUpdate();
        logger.debug("delete entity {},id is {}", entityClass.getSimpleName(), id);
    }

    /**
     * 获取所有实体
     *
     * @param tableName
     * @return
     * @author songlin
     */
    public ClassMetadata getEntityByTableName(String tableName) {
        Map<String, ClassMetadata> map = getSessionFactory().getAllClassMetadata();
        int i = 0;
        for (Map.Entry<String, ClassMetadata> m : map.entrySet()) {
            ClassMetadata cmd = m.getValue();
            AbstractEntityPersister persister = (AbstractEntityPersister) getSessionFactory().getClassMetadata(cmd.getMappedClass());
            // if (persister != null) {
            // if(StringUtil.startsWith(persister.getTableName().toLowerCase(),
            // "vw_")){
            // continue;
            // }
            // System.out.println("CREATE UNIQUE INDEX IDX_" +
            // persister.getTableName().toUpperCase() + "_"
            // + persister.getIdentifierPropertyName().toUpperCase() + " ON " +
            // persister.getTableName().toUpperCase() + " ("
            // + persister.getIdentifierPropertyName().toUpperCase() + ");");
            // }
            if (persister != null && StringUtil.equalsIgnoreCase(tableName, persister.getTableName())) {
                return cmd;
            }
        }
        return null;
    }

    /**
     * 万一需要呢，扩展来用一下吧
     *
     * @return
     */
    public Class<T> getEntityClass() {
        return entityClass;
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
