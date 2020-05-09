package com.mailerdaemon.app.events;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

import lombok.Data;

@Data
public class PostModel {
  private String heading;
  private String details;
  private String photo;
  @ServerTimestamp
  private Date date;

}
