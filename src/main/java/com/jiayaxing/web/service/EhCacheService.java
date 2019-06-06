package com.jiayaxing.web.service;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service("ehCacheService")
public class EhCacheService {
	
	@Cacheable(value = "passwordRetryCache", key = "#username")
	public AtomicInteger getPasswordRetryCache(String username) {
		return null;
	}
	
	@CacheEvict(value="passwordRetryCache",key="#username")
	public boolean deleteCacheByUsername(String username) {
		return true;
	}
	
	@CachePut(value="registerInfo",key="#checkCode")
	public String saveRegisterInfo(String checkCode,String registerInfo) {
		return registerInfo;
	}
	
	@Cacheable(value = "registerInfo", key = "#checkCode")
	public String getRegisterInfo(String checkCode) {
		return null;
	}
	
	@CacheEvict(value="registerInfo",key="#checkCode")
	public boolean deleteRegisterInfo(String checkCode) {
		return true;
	}
}
