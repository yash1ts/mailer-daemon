package com.mailerdaemon.app.Events;

import com.google.firebase.firestore.CollectionReference;

import lombok.Data;

@Data
class EventModel {
  private String name;
  private String date;
  private String day;
  CollectionReference posts;

}
