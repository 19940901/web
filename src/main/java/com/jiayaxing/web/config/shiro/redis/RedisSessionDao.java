package com.jiayaxing.web.config.shiro.redis;

import org.apache.juli.logging.LogFactory;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.apache.shiro.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class RedisSessionDao extends AbstractSessionDAO {
    private static Logger log = LoggerFactory.getLogger(RedisSessionDao.class);
    private static final  String KEY_PREFIX = "redis.shiro.session_";
    private RedisTemplate redisTemplate;

    private ValueOperations valueOperations;

    public RedisSessionDao(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.valueOperations = redisTemplate.opsForValue();
    }

    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = this.generateSessionId(session);
        log.debug("shiro sessionId is {}", sessionId);
        this.assignSessionId(session, sessionId);
        valueOperations.set(generateKey(sessionId), session, session.getTimeout());
        return sessionId;
    }

    @Override
    protected Session doReadSession(Serializable serializable) {
        log.debug("shiro redis session read sessionId {}", serializable);
        Session session = (Session) valueOperations.get(generateKey(serializable));
        return session;

    }

    @Override
    public void update(Session session) throws UnknownSessionException {
        log.debug("shiro redis session update {}", session.getId());
        valueOperations.set(generateKey(session.getId()), session, session.getTimeout(), TimeUnit.MILLISECONDS);
    }

    @Override
    public void delete(Session session) {
        log.debug("shiro redis session delete {}", session.getId());
        redisTemplate.delete(generateKey(session.getId()));
    }

    @Override
    public Collection<Session> getActiveSessions() {
        Set<Object> keyset = redisTemplate.keys(generateKey("*"));
        Set<Session> sessionSet = new HashSet<>();
        if (CollectionUtils.isEmpty(keyset)) {
            return Collections.emptySet();
        }
        for (Object key : keyset) {
            sessionSet.add((Session) valueOperations.get(key));
        }
        log.debug("shiro redis session all.size={}", sessionSet.size());
        return sessionSet;
    }

    /**
     * 重组key
     * 区别其他使用环境的key
     *
     * @param key
     * @return
     */
    private String generateKey(Object key) {
        return KEY_PREFIX + this.getClass().getName() + "_" + key;
    }
}
