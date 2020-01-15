package cn.wuxia.project.common.model;

import cn.wuxia.project.common.security.UserContext;
import cn.wuxia.project.common.security.UserContextUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

/**
 * @author songlin
 */
@Configuration
public class ModifyInfoJpaAuditor implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        UserContext userContext = UserContextUtil.getUserContext();
        if(userContext != null){
            return Optional.of(userContext.getName());
        }
        return Optional.empty();
    }
}
