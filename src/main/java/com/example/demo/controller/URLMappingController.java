package com.example.demo.controller;

import com.example.demo.dto.URLMappingRequest;
import com.example.demo.service.URLMappingService;
import java.util.Optional;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by mj104603 on 1/23/2018.
 */
@RestController
public class URLMappingController {

  URLMappingService urlMappingService;

  URLMappingController(URLMappingService urlMappingService){
    this.urlMappingService = urlMappingService;
  }

  @PostMapping(path = "/urls/shorten", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> getShortenURL(@RequestBody @NotNull URLMappingRequest urlMappingRequest){
     Optional<String> shortURL = urlMappingService.getShortURL(urlMappingRequest);
     if(shortURL.isPresent())
       return new ResponseEntity<>(shortURL.get(), HttpStatus.CREATED);
     return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
  }


  @GetMapping(path = "/urls/{tinyurl}")
  public ResponseEntity<Void> getLongURL(@PathVariable @NotBlank String tinyurl){
    Optional<String> foundLongURL = urlMappingService.getLongURL(tinyurl);
    if(foundLongURL.isPresent())
    {
      HttpHeaders httpHeaders = new HttpHeaders();
      httpHeaders.set(HttpHeaders.LOCATION, foundLongURL.get());
      return new ResponseEntity<>(httpHeaders, HttpStatus.TEMPORARY_REDIRECT);
    }
    return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
  }

}
