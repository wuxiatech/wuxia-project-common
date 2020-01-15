package cn.wuxia.project.common.dao;

import cn.wuxia.common.entity.Base64UuidGenerator;
import cn.wuxia.common.spring.orm.mongo.SpringDataMongoDao;
import cn.wuxia.common.util.StringUtil;
import cn.wuxia.common.util.reflection.ReflectionUtil;
import cn.wuxia.project.common.model.AbstractPrimaryKeyEntity;
import cn.wuxia.project.common.model.CommonMongoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

/**
 * 基础Dao。
 *
 * @author songlin.li
 * @since 2013-6-19
 */
public abstract class CommonMongoDao<T extends AbstractPrimaryKeyEntity, K extends Serializable>
        extends SpringDataMongoDao<T, K> implements CommonDaoInterface {

    @Autowired
    @Override
    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        super.setMongoTemplate(mongoTemplate);
    }

    /**
     * save , update 不能混用 update请使用 {@link #update(AbstractPrimaryKeyEntity)}
     */
    @Override
    public void save(T entity) {
        Assert.notNull(entity, "实体对象不能为空");
        if (StringUtil.isNotBlank(entity.getId())) {
            update(entity);
            return;
        } else if (entity instanceof CommonMongoEntity) {
            String idvalue = Base64UuidGenerator.UuidUtils.compressedUuid();
            CommonMongoEntity mongoEntity = (CommonMongoEntity) entity;
            mongoEntity.setId(idvalue);
        }
        super.save(entity);

    }

    /**
     * save , update 不能混用
     *
     * @param entity
     * @author songlin
     */
    public void update(T entity) {
        Assert.notNull(entity, "实体对象不能为空");
        Object idvalue = entity.getId();
        if (StringUtil.isBlank(idvalue)) {
            save(entity);
            return;
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
        logger.info("完成保存, class={}，id={}", getEntityClass(), idvalue);
        entity = findById((K) entity.getId());
    }


    @Override
    public Class getEntityClass() {
        return super.getEntityClass();
    }

}
