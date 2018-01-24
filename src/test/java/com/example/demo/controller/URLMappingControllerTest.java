package com.example.demo.controller;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import com.example.demo.dto.URLMappingRequest;
import com.example.demo.service.URLMappingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Created by mj104603 on 1/24/2018.
 */
public class URLMappingControllerTest {

  @InjectMocks
  private URLMappingController classUnderTest;

  private MockMvc mockmvc;

  @Mock
  private URLMappingService urlMappingService;

  @Before
  public void setup(){
   initMocks(this);
    classUnderTest = new URLMappingController(urlMappingService);
   mockmvc = standaloneSetup(classUnderTest).build();
  }


  @Test
  public void getShortenURL_sucess() throws Exception {
    URLMappingRequest request = new URLMappingRequest();
    request.setUrl("http://google.com");
    request.setPrefix("mj-url");
    when(urlMappingService.getShortURL(any(URLMappingRequest.class))).thenReturn(Optional.of("http://mj-url/rwe"));
    mockmvc.perform(post("/urls/shorten").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(convertObjectToJsonBytes(request)).accept(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isCreated());
    verify(urlMappingService, times(1)).getShortURL(any(URLMappingRequest.class));
  }

  @Test
  public void getShortenURL_error() throws Exception {
    URLMappingRequest request = new URLMappingRequest();
    request.setUrl("http://google.com");
    request.setPrefix("mj-url");
    when(urlMappingService.getShortURL(any(URLMappingRequest.class))).thenReturn(Optional.empty());
    mockmvc.perform(post("/urls/shorten").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(convertObjectToJsonBytes(request)).accept(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isIAmATeapot());
    verify(urlMappingService, times(1)).getShortURL(any(URLMappingRequest.class));
  }

  @Test
  public void getLongURL() throws Exception {
    when(urlMappingService.getLongURL("r4tsdf")).thenReturn(Optional.of("http://google.com"));
    mockmvc.perform(get("/urls/r4tsdf")).andExpect(status().isTemporaryRedirect());
   verify(urlMappingService, times(1)).getLongURL("r4tsdf");
  }

  private byte[] convertObjectToJsonBytes(final Object object) throws IOException {
    final ObjectMapper mapper = new ObjectMapper();
    return mapper.writeValueAsBytes(object);
  }

}
