package com.mailerdaemon.app.Notices;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

import lombok.Data;

@Data
public class NoticeModel {
  private String heading;
  private String details;
  private String photo;
  @ServerTimestamp
  private Date date;

}
