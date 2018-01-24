package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by mj104603 on 1/23/2018.
 */
@Getter@Setter@ToString
public class URLMappingRequest {

  @NotBlank
  private String url;

  private String prefix;

}
