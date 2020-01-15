package cn.wuxia.project.common.dao;

import cn.wuxia.common.hibernate.dao.BasicHibernateDao;
import cn.wuxia.common.util.StringUtil;
import cn.wuxia.project.common.model.AbstractPrimaryKeyEntity;
import org.hibernate.SessionFactory;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.persister.entity.AbstractEntityPersister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate5.SessionFactoryUtils;
import org.springframework.util.Assert;

import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * 基础Dao。
 *
 * @author songlin.li
 * @since 2013-6-19
 */
public abstract class CommonDao<T extends AbstractPrimaryKeyEntity, PK extends Serializable> extends BasicHibernateDao<T, PK> implements CommonDaoInterface {
    private JdbcTemplate jdbcTemplate;

    /**
     * 不同的数据源使用不同的sessionFactory(名字不同)
     */
    @Override
    @Autowired
    public void setSessionFactory(final SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        this.jdbcTemplate = new JdbcTemplate(SessionFactoryUtils.getDataSource(sessionFactory));
    }

    protected JdbcTemplate getJdbcTemplate() {
        if (jdbcTemplate == null) {
            this.jdbcTemplate = new JdbcTemplate(SessionFactoryUtils.getDataSource(sessionFactory));
        }
        return jdbcTemplate;
    }

    /**
     * get an entity for logical delete
     *
     * @return
     * @author songlin.li
     */
    public T findByIdIncludeLogicalDelete(final PK id) {
        Table table = entityClass.getAnnotation(Table.class);
        String schema = "";
        if (table != null && StringUtil.isNotBlank(table.schema())) {
            schema = table.schema() + ".";
        }
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
    public void deleteByLogical(final PK id) {
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


}
