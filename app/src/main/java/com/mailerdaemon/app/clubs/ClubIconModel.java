package com.mailerdaemon.app.clubs;

import java.io.Serializable;

import lombok.Data;

@Data
class ClubIconModel implements Serializable {
  private String url;
  private Integer tag;
}
