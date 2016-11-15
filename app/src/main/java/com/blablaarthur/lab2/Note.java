package com.blablaarthur.lab2;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by Артур on 24.10.2016.
 */

class Note implements Parcelable{
    public Integer Id;
    public String Title;
    public String Description;
    public int Importance; //1 2 3
    public String DateTime;
    public String Image;

    Note(){
        Id = -1;
        Title = "";
        Description = "";
        Importance = 0;
        DateTime = "";
        Image = "";
    }

    Note(Integer id, String title, String desc, int imp, String datetime, String image){
        Id = id;
        Title = title;
        Description = desc;
        Importance = imp;
        DateTime = datetime;
        Image = image;
    }

    Note(Parcel in){
        Id = in.readInt();
        Title = in.readString();
        Description = in.readString();
        Importance = in.readInt();
        DateTime = in.readString();
        Image = in.readString();
    }

    public static final Parcelable.Creator<Note> CREATOR = new Parcelable.Creator<Note>() {
        // распаковываем объект из Parcel
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        public Note[] newArray(int size) {
            return new Note[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Id);
        dest.writeString(Title);
        dest.writeString(Description);
        dest.writeInt(Importance);
        dest.writeString(DateTime);
        dest.writeString(Image);
    }
}
