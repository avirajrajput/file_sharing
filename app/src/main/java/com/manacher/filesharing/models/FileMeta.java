package com.manacher.filesharing.models;

import android.os.Parcel;
import android.os.Parcelable;

public class FileMeta implements Parcelable {

    private String fileName;
    private Long mFileSize;
    private String filePath;

    private String senderId;

    public FileMeta() {
    }

    public FileMeta(String fileName, Long mFileSize, String filePath, String senderId) {
        this.fileName = fileName;
        this.mFileSize = mFileSize;
        this.filePath = filePath;
        this.senderId = senderId;
    }

    protected FileMeta(Parcel in) {
        fileName = in.readString();
        if (in.readByte() == 0) {
            mFileSize = null;
        } else {
            mFileSize = in.readLong();
        }
        filePath = in.readString();
        senderId = in.readString();
    }

    public static final Creator<FileMeta> CREATOR = new Creator<FileMeta>() {
        @Override
        public FileMeta createFromParcel(Parcel in) {
            return new FileMeta(in);
        }

        @Override
        public FileMeta[] newArray(int size) {
            return new FileMeta[size];
        }
    };

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getmFileSize() {
        return mFileSize;
    }

    public void setmFileSize(Long mFileSize) {
        this.mFileSize = mFileSize;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(fileName);
        if (mFileSize == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(mFileSize);
        }
        parcel.writeString(filePath);
        parcel.writeString(senderId);
    }
}
