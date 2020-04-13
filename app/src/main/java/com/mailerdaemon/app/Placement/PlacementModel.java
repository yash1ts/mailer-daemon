package com.mailerdaemon.app.Placement;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

import lombok.Data;

@Data
public class PlacementModel {
    String data;
    @ServerTimestamp
    Date date;
}
