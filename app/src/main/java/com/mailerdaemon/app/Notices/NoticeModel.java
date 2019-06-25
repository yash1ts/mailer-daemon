package com.mailerdaemon.app.Notices;

import lombok.Data;

@Data
public class NoticeModel{
  private String heading;
  private String details;
  private String photo;
  private String date;

  public NoticeModel(){

  }

}
