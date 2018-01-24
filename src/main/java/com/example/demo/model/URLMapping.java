package com.example.demo.model;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by mj104603 on 1/23/2018.
 */
@Getter
@Setter
@ToString
public class URLMapping  implements Serializable{

  private long id;
  private String longurl;
  private String shorturl;
}
