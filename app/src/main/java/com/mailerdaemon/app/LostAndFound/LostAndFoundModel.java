package com.mailerdaemon.app.LostAndFound;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

import lombok.Data;

@Data
public class LostAndFoundModel {
    private String heading;
    private String details;
    private String photo;
    @ServerTimestamp
    private Date date;
    private Boolean verified;
    private String uid;
}
