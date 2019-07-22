package com.mailerdaemon.app.Notices;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Data;

@Data
public class NoticeModel implements Parcelable {
  private String heading;
  private String details;
  private String photo;
  private String date;

  public NoticeModel(){

  }

  protected NoticeModel(Parcel in) {
    heading = in.readString();
    details = in.readString();
    photo = in.readString();
    date = in.readString();
  }

  public static final Creator<NoticeModel> CREATOR = new Creator<NoticeModel>() {
    @Override
    public NoticeModel createFromParcel(Parcel in) {
      return new NoticeModel(in);
    }

    @Override
    public NoticeModel[] newArray(int size) {
      return new NoticeModel[size];
    }
  };

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(heading);
    dest.writeString(details);
    dest.writeString(photo);
    dest.writeString(date);
  }
}
