package com.mailerdaemon.app.events;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
class EventModel {
  List<PostModel> posts;
  private String name;
  @ServerTimestamp
  private Date date;
}
