package com.example.demo.service;

import com.example.demo.dto.URLMappingRequest;
import java.util.Optional;

/**
 * Created by mj104603 on 1/23/2018.
 */
public interface URLMappingService {

  Optional<String> getShortURL(URLMappingRequest urlMappingRequest);

  Optional<String> getLongURL(String shortURL);

}
