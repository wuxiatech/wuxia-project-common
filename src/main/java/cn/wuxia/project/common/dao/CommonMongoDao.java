package cn.wuxia.project.common.dao;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.wuxia.common.util.ClassLoaderUtil;
import cn.wuxia.project.common.model.CommonMongoEntity;
import cn.wuxia.project.common.model.ModifyInfoEntity;
import cn.wuxia.project.common.model.ModifyInfoMongoEntity;
import cn.wuxia.project.common.security.UserContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.Assert;

import cn.wuxia.common.entity.Base64UuidGenerator;
import cn.wuxia.common.exception.AppServiceException;
import cn.wuxia.common.exception.ValidateException;
import cn.wuxia.common.sensitive.ValidtionSensitiveUtil;
import cn.wuxia.common.spring.orm.mongo.SpringDataMongoDao;
import cn.wuxia.common.util.DateUtil;
import cn.wuxia.common.util.StringUtil;
import cn.wuxia.common.util.reflection.BeanUtil;
import cn.wuxia.common.util.reflection.ReflectionUtil;

/**
 * 基础Dao。
 *
 * @author songlin.li
 * @since 2013-6-19
 */
public abstract class CommonMongoDao<T extends CommonMongoEntity, K extends Serializable>
        extends SpringDataMongoDao<T, K> implements DaoInterface {

    @Autowired
    @Override
    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        super.setMongoTemplate(mongoTemplate);
    }

    /**
     * save , update 不能混用 update请使用 {@link #update(CommonMongoEntity)}
     */
    @Override
    public void save(T entity) {
        Assert.notNull(entity, "实体对象不能为空");
        String idvalue = entity.getId();
        if (StringUtil.isNotBlank(idvalue)) {
            update(entity);
            return;
        } else {
            idvalue = Base64UuidGenerator.UuidUtils.compressedUuid();
            entity.setId(idvalue);
        }
        if (entity instanceof ModifyInfoMongoEntity) {
            try {
                /**
                 * 有与框架的及部署的原因，并非在当前线程中可以拿得到用户信息，比如dubbox及webservice
                 * 可以在consumer层set值
                 */
                String username = null;
                if (StringUtil.isBlank(username) && UserContextUtil.getUserContext() != null) {
                    username = UserContextUtil.getName();
                }

                ModifyInfoEntity infoEntity = new ModifyInfoEntity();
                BeanUtil.copyProperties(infoEntity, entity);
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
                BeanUtil.copyProperties(entity, infoEntity);
            } catch (Exception ex) {
                logger.warn(ex.getMessage(), ex);
            }
        }
        try {
            ValidtionSensitiveUtil.validate(entity);
            super.save(entity);
        } catch (ValidateException e1) {
            throw new AppServiceException("", e1);
            // throw new AppDaoException(e1);
        }
    }

    /**
     * save , update 不能混用
     *
     * @param entity
     * @author songlin
     */
    public void update(T entity) {
        Assert.notNull(entity, "实体对象不能为空");
        K idvalue = (K) entity.getId();
        if (StringUtil.isBlank(idvalue)) {
            save(entity);
            return;
        }
        Timestamp time = DateUtil.newInstanceDate();
        if (entity instanceof ModifyInfoMongoEntity) {
            try {
                /**
                 * 有与框架的及部署的原因，并非在当前线程中可以拿得到用户信息，比如dubbox及webservice
                 * 可以在consumer层set值
                 */
                String username = null;
                if (StringUtil.isBlank(username) && UserContextUtil.getUserContext() != null) {
                    username = UserContextUtil.getName();
                }

                ModifyInfoEntity infoEntity = new ModifyInfoEntity();
                BeanUtil.copyProperties(infoEntity, entity);

                if (StringUtil.isNotBlank(username)) {
                    infoEntity.setModifiedBy(filterEmoji(username));
                }
                infoEntity.setModifiedOn(time);
                if (infoEntity.getCreatedOn() == null) {
                    infoEntity.setCreatedOn(time);
                }
                BeanUtil.copyProperties(entity, infoEntity);
            } catch (Exception ex) {
                logger.warn(ex.getMessage(), ex);
            }
        }

        Query query = new Query(Criteria.where("id").is(idvalue));
        Update update = new Update();
        List<Field> fields = ReflectionUtil.getAccessibleFields(getEntityClass());
        for (Field field : fields) {
            try {
                if (Modifier.isFinal(field.getModifiers()) || Modifier.isStatic(field.getModifiers())) {
                    continue;
                }
                String propertyName = field.getName();
                Object propertyValue = field.get(entity);
                /**
                 * 暂时定义为空的放弃 FIXME
                 */
                if (propertyValue == null) {
                    logger.debug("{}为空，忽略", propertyName);
                    continue;
                }
                /**
                 * 指定特殊字段，忽略
                 */
                if (StringUtil.equals("createdOn", propertyName) || StringUtil.equals("createdBy", propertyName)) {
                    continue;
                }
                Transient ingore = ReflectionUtil.getAnnotation(field, Transient.class);
                if (ingore != null) {
                    logger.debug("{}为@Transient，忽略", propertyName);
                    continue;
                }

                Method method = ReflectionUtil.getGetterMethodByPropertyName(entity, propertyName);
                if (method == null) {
                    logger.debug("{}没有getter方法，忽略", propertyName);
                }
                ingore = ReflectionUtil.getAnnotation(method, Transient.class);
                if (ingore != null) {
                    logger.debug("{}为@Transient，忽略", propertyName);
                    continue;
                }
                propertyValue = method.invoke(entity);

                update.set(propertyName, propertyValue);
                logger.debug("set {}={}", propertyName, propertyValue);
            } catch (Exception e) {
                logger.warn("error set {}, {}", field.getName(), e.getMessage());
                continue;
            }
        }
        super.update(query, update);
        logger.info("完成保存, class={}，id={}, modifiedBy={}", getEntityClass(), idvalue, time);
        entity = findById((K) entity.getId());
    }

    /**
     * 由于部分公用信息需要赋值，则不调用mongoTemplate.batchSave
     *
     * @param entitys
     */
    @Override
    public void batchSave(Collection<T> entitys) {
        Assert.notEmpty(entitys, "entitys不能为空");
        for (T t : entitys) {
            save(t);
        }
    }

    /**
     * 临时解决方法，后续数据库createdBy,modifiedBy字段可以升级为utf8mb4 当前是为了兼容旧数据库
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

    public Class getEntityClass() {
        return super.getEntityClass();
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
