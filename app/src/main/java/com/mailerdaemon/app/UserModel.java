package com.mailerdaemon.app;

import lombok.Data;

@Data
public class UserModel {
    String userId;
    String name;
    String email;
    Boolean rejectedPost;
}
