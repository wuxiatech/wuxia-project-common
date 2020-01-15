package cn.wuxia.project.common.service.impl;

import cn.wuxia.common.util.ClassLoaderUtil;
import cn.wuxia.common.util.DateUtil;
import cn.wuxia.common.util.StringUtil;
import cn.wuxia.project.common.model.AbstractPrimaryKeyEntity;
import cn.wuxia.project.common.model.ModifyInfoAutoIdEntity;
import cn.wuxia.project.common.model.ModifyInfoEntity;
import cn.wuxia.project.common.model.ModifyInfoMongoEntity;
import cn.wuxia.project.common.security.UserContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author songlin
 */
public abstract class AbstractServiceImpl<E extends AbstractPrimaryKeyEntity, K> {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected void invokeModifyInfo(AbstractPrimaryKeyEntity e) {
        /**
         * 有与框架的及部署的原因，并非在当前线程中可以拿得到用户信息，比如dubbox及webservice
         * 可以在consumer层set值
         */
        String username = UserContextUtil.getName();
        Timestamp time = DateUtil.newInstanceDate();
        if (e instanceof ModifyInfoEntity) {
            ModifyInfoEntity infoEntity = (ModifyInfoEntity) e;
            if (StringUtil.isNotBlank(e.getId())) {
                infoEntity.setModifiedBy(filterEmoji(username));
                infoEntity.setModifiedOn(time);
            } else {
                infoEntity.setCreatedBy(filterEmoji(username));
                infoEntity.setCreatedOn(time);
            }
        } else if (e instanceof ModifyInfoAutoIdEntity) {
            ModifyInfoAutoIdEntity infoEntity = (ModifyInfoAutoIdEntity) e;
            if (StringUtil.isNotBlank(e.getId())) {
                infoEntity.setModifiedBy(filterEmoji(username));
                infoEntity.setModifiedOn(time);
            } else {
                infoEntity.setCreatedBy(filterEmoji(username));
                infoEntity.setCreatedOn(time);
            }
        } else if (e instanceof ModifyInfoMongoEntity) {
            ModifyInfoMongoEntity infoEntity = (ModifyInfoMongoEntity) e;
            if (StringUtil.isNotBlank(e.getId())) {
                infoEntity.setModifiedBy(filterEmoji(username));
                infoEntity.setModifiedOn(time);
            } else {
                infoEntity.setCreatedBy(filterEmoji(username));
                infoEntity.setCreatedOn(time);
            }
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
    private Pattern emoji = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
            Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

    protected String filterEmoji(String source) {
        if (StringUtil.isNotBlank(source)) {
            Matcher emojiMatcher = emoji.matcher(source);
            if (emojiMatcher.find()) {
                source = emojiMatcher.replaceAll("*");
                return source;
            }
            return source;
        }
        return source;
    }

    /**
     * 简单判断是否存在spring-security
     *
     * @return
     */
    protected boolean hasSpringSecurity() {
        try {
            Class clazz = ClassLoaderUtil.loadClass("org.springframework.security.core.Authentication");
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
