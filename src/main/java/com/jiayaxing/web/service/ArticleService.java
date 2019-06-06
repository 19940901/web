package com.jiayaxing.web.service;

import com.jiayaxing.web.dao.ArticleMapper;
import com.jiayaxing.web.model.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleService {

    @Autowired
    ArticleMapper articleMapper;

    @Cacheable(cacheManager = "redisCacheManager",key = "#user",cacheNames = "Users")
    public List<Article> findByuser(String user) {
        return articleMapper.selectByUser(user);
    }

    public void add(Article article) {
        articleMapper.insert(article);
    }
}
