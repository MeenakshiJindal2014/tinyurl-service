package com.example.demo.service;

import com.example.demo.component.URLCache;
import com.example.demo.dto.URLMappingRequest;
import com.example.demo.model.URLMapping;
import com.example.demo.repository.URLMappingEntity;
import com.example.demo.repository.URLMappingRepository;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Service;

/**
 * Created by mj104603 on 1/23/2018.
 */
@Service
public class URLMappingServiceImpl implements URLMappingService {

  final List<Character> charset = new ArrayList<>(Arrays.asList(
      'a', 'b', 'c', 'd', 'e', 'f', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
      'w', 'x', 'y', 'z',
      'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S',
      'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
      '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-', '.'));

  final String prefix = "http://mj-url/";
  static AtomicLong sequence = new AtomicLong();

  private URLCache urlCache;

  private URLMappingRepository urlMappingRepository;

  URLMappingServiceImpl(URLCache urlCache, URLMappingRepository urlMappingRepository) {
    this.urlCache = urlCache;
    this.urlMappingRepository = urlMappingRepository;
    // Define login to externalize sequence number for scaling
    sequence.set(Instant.now().toEpochMilli());
  }


  public Optional<String> getShortURL(URLMappingRequest urlMappingRequest) {
    if(!validateURL(urlMappingRequest.getUrl()))
      return Optional.empty();
    long nextValue = sequence.getAndIncrement();
    String shortValue = convertBase10ToBase64(nextValue);
    StringBuilder shortvalue_buffer = new StringBuilder("http://");
    shortvalue_buffer
        .append(urlMappingRequest.getPrefix() != null ? urlMappingRequest.getPrefix() : prefix)
        .append("/").append(shortValue);
    persistURLMappingEntity(nextValue, shortvalue_buffer.toString(), urlMappingRequest.getUrl());
    return Optional.of(shortvalue_buffer.toString());
  }


  public Optional<String> getLongURL(String shortURLval) {
    Optional<Long> urlmappingid = convertBase64ToBase10(shortURLval);
    if (urlmappingid.isPresent()) {
      URLMapping urlMapping = urlCache.getURLMapping(urlmappingid.get());
      if (urlMapping != null) {
        return Optional.ofNullable(urlMapping.getLongurl());
      }
    }
    return Optional.empty();
  }


  public Optional<Long> convertBase64ToBase10(String base64val) {
    long result = 0;
    for (char c : base64val.toCharArray()) {
      int val = charset.indexOf(c);
      if (val == -1) {
        return Optional.empty();
      }
      result = result * 64 + charset.indexOf(c);
    }
    return Optional.of(result);
  }


  public String convertBase10ToBase64(long base10val) {
    StringBuilder base64val = new StringBuilder();
    long num = base10val;
    while (num != 0) {
      base64val.append(charset.get((int) (num % 64)));
      num = num / 64;
    }
    return base64val.reverse().toString();
  }

  boolean validateURL(String url) {
    try {
      new URL(url);
    } catch (MalformedURLException e) {
      return false;
    }
    return true;
  }

  private void persistURLMappingEntity(Long id, String shorturl, String longurl) {
    URLMappingEntity urlMappingEntity = new URLMappingEntity();
    urlMappingEntity.setId(id);
    urlMappingEntity.setLongurl(longurl);
    urlMappingEntity.setShorturl(shorturl);
    urlMappingEntity.setCreationDate(Instant.now().toString());
    urlMappingRepository.saveAndFlush(urlMappingEntity);
  }


}
