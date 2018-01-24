package com.example.demo.component;

import com.example.demo.model.URLMapping;
import com.example.demo.repository.URLMappingEntity;
import com.example.demo.repository.URLMappingRepository;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * Created by mj104603 on 1/23/2018.
 */
@Service
@CacheConfig(cacheNames = "urlmappings")
public class URLCache {

  private URLMappingRepository urlMappingRepository;

  URLCache(URLMappingRepository urlMappingRepository)
  {
     this.urlMappingRepository = urlMappingRepository;
  }

  @Cacheable(value = "urlmappings", key = "#id")
  public URLMapping getURLMapping(Long id){
    URLMappingEntity urlMappingEntity = urlMappingRepository.findOne(id);
    if(urlMappingEntity!= null)
      return mapURLMappingEntityToURLMapping(urlMappingEntity);
    return null;
  }

  private URLMapping mapURLMappingEntityToURLMapping(URLMappingEntity urlMappingEntity){
    URLMapping urlMapping = new URLMapping();
    urlMapping.setId(urlMappingEntity.getId());
    urlMapping.setLongurl(urlMappingEntity.getLongurl());
    urlMapping.setShorturl(urlMappingEntity.getShorturl());
    return urlMapping;
  }

}
