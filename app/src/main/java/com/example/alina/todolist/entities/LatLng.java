package com.example.alina.todolist.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class LatLng implements Parcelable{
    private double latitude;
    private double longitude;

    public LatLng(){}

    public LatLng(double latitude, double longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public int describeContents() {return 0;}

    @Override
    public void writeToParcel (Parcel dest, int flags) {
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
    }

    protected LatLng (Parcel in) {
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
    }

    public static final Creator<LatLng> CREATOR = new Creator<LatLng>() {
        @Override
        public LatLng createFromParcel(Parcel source) {
            return new LatLng(source);
        }

        @Override
        public LatLng[] newArray(int size) {
            return new LatLng[0];
        }
    };

    public double getLatitude() {
        return latitude;
    }

    public LatLng setLatitude(double latitude) {
        this.latitude = latitude;
        return this;
    }

    public double getLongitude() {
        return longitude;
    }

    public LatLng setLongitude(double longitude) {
        this.longitude = longitude;
        return this;
    }
}
