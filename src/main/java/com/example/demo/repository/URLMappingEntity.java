package com.example.demo.repository;

import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by mj104603 on 1/23/2018.
 */
@Entity
@Getter@Setter@ToString
public class URLMappingEntity {

  @Id
  private long id;

  private String longurl;

  private String shorturl;

  private String creationDate;

}
