package com.mailerdaemon.app.utils;

import com.mailerdaemon.app.events.PostModel;

public interface DialogOptions {
    void showOptions(PostModel model, String path);
    void showDialog(String path);
}
