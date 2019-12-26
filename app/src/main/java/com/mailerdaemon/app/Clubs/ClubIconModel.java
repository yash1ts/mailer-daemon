package com.mailerdaemon.app.Clubs;

import java.io.Serializable;

import lombok.Data;

@Data
class ClubIconModel implements Serializable {
  private String url;
  private String tag;
}
