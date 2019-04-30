package cn.wuxia.project.common.third.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 收到消息响应处理器。基类封装了校验操作，以及消息事件派发，具体事件处理由具体子类完成。
 */
public abstract class ResponseParser<X extends ResponseMessage, Y extends ResponseSession> {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected abstract <T> T onMessage(final X message);

    protected abstract void onSessionStart(Y session);

    protected abstract void onSessionEnd(Y session);

}
