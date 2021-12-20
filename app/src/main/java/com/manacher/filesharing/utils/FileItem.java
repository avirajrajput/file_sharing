package com.manacher.filesharing.utils;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ProgressBar;

public class FileItem implements Parcelable {
    private String fileName;
    private Drawable icon;
    private Long mFileSize;
    private Drawable done;
    private String filePath;

    private String senderId;
    private String absolutePath;

    private volatile Integer mProgress;

    private boolean isComplete = false;



    private volatile ProgressBar mProgressBar;
    public FileItem(String fileName, Drawable icon, String filePath, Long size) {
        this.fileName = fileName;
        this.icon = icon;
        this.filePath = filePath;
        mProgress = 0;
        mFileSize = size;
        mProgressBar = null;
    }

    public FileItem(String fileName, Drawable icon, String filePath, Long size, String senderId) {
        this.fileName = fileName;
        this.icon = icon;
        this.filePath = filePath;
        this.senderId = senderId;

        mProgress = 0;
        mFileSize = size;
        mProgressBar = null;

    }

    public FileItem(String fileName, Drawable icon, String filePath, Long size, String absolutePath, String senderId) {
        this.fileName = fileName;
        this.icon = icon;
        this.filePath = filePath;
        this.senderId = senderId;
        this.absolutePath = absolutePath;
        mProgress = 0;
        mFileSize = size;
        mProgressBar = null;

    }

    public FileItem(FileItem fileItem){
        this.fileName = fileItem.getFileName();
        this.icon = fileItem.getIcon();
        this.mFileSize = fileItem.getFileSize();
        this.filePath = fileItem.getFilePath();
        this.absolutePath = fileItem.getAbsolutePath();
        this.senderId = fileItem.getSenderId();
        this.mProgress = 0;
    }

    protected FileItem(Parcel in) {
        fileName = in.readString();
        if (in.readByte() == 0) {
            mFileSize = null;
        } else {
            mFileSize = in.readLong();
        }
        filePath = in.readString();
        absolutePath = in.readString();
        senderId = in.readString();
        if (in.readByte() == 0) {
            mProgress = null;
        } else {
            mProgress = in.readInt();
        }
    }

    public static final Creator<FileItem> CREATOR = new Creator<FileItem>() {
        @Override
        public FileItem createFromParcel(Parcel in) {
            return new FileItem(in);
        }

        @Override
        public FileItem[] newArray(int size) {
            return new FileItem[size];
        }
    };

    public Drawable getDone() {
        return done;
    }

    public void setDone(Drawable done) {
        this.done = done;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public ProgressBar getProgressBar() {
        return mProgressBar;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }

    public void setProgressBar(ProgressBar progressBar) {
        mProgressBar = progressBar;
    }

    public Integer getProgress() {
        return mProgress;
    }

    public void setProgress(Integer progress) {
        this.mProgress = progress;
    }

    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public Long getFileSize() {
        return mFileSize;
    }

    public void setFileSize(Long mFileSize) {
        this.mFileSize = mFileSize;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
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
        parcel.writeString(absolutePath);
        parcel.writeString(senderId);
        if (mProgress == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(mProgress);
        }
    }
}
