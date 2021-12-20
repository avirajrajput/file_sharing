package com.manacher.filesharing.models;

import android.os.Parcel;
import android.os.Parcelable;

public class FileLength implements Parcelable {
    private long fileSize;

    public FileLength() {
    }

    protected FileLength(Parcel in) {
        fileSize = in.readLong();
    }

    public static final Creator<FileLength> CREATOR = new Creator<FileLength>() {
        @Override
        public FileLength createFromParcel(Parcel in) {
            return new FileLength(in);
        }

        @Override
        public FileLength[] newArray(int size) {
            return new FileLength[size];
        }
    };

    public long getFileSize() {
        return fileSize;
    }

    public FileLength(long fileSize) {
        this.fileSize = fileSize;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(fileSize);
    }
}
