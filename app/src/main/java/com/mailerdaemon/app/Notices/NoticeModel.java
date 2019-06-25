package com.mailerdaemon.app.Notices;

import lombok.Data;

@Data
public class NoticeModel{
  private String heading;
  private String details;
  private String photo;
  private String date;

  public NoticeModel(String heading,String date,String details,String photo){
    this.photo=photo;
    this.heading=heading;
    this.date=date;
    this.details=details;
  }
  public NoticeModel(){

  }

}
