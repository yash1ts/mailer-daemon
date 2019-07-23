package Utils;

import com.mailerdaemon.app.Notices.NoticeModel;

public interface DialogOptions {
    void showOptions(NoticeModel model, String path);
    void showDialog(String path);
}
