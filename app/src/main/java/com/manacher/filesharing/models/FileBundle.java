package com.manacher.filesharing.models;

import android.os.Parcel;
import android.os.Parcelable;


public class FileBundle implements Parcelable {
    private String filePath;
    private String fileName;
    private Long fileSize;

    public FileBundle() {
    }

    public FileBundle(String filePath, String fileName, Long fileSize) {
        this.filePath = filePath;
        this.fileName = fileName;
        this.fileSize = fileSize;
    }

    protected FileBundle(Parcel in) {
        filePath = in.readString();
        fileName = in.readString();
        if (in.readByte() == 0) {
            fileSize = null;
        } else {
            fileSize = in.readLong();
        }
    }

    public static final Creator<FileBundle> CREATOR = new Creator<FileBundle>() {
        @Override
        public FileBundle createFromParcel(Parcel in) {
            return new FileBundle(in);
        }

        @Override
        public FileBundle[] newArray(int size) {
            return new FileBundle[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(filePath);
        parcel.writeString(fileName);
        if (fileSize == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(fileSize);
        }
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }
}
