package com.mailerdaemon.app.Events;

import com.mailerdaemon.app.Notices.NoticeModel;

import java.util.List;

import lombok.Data;

@Data
public class EventModel {
  private String name;
  private String date;
  private String day;
  List<NoticeModel> posts;

}
