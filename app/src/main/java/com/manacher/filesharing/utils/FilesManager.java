package com.manacher.filesharing.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.MimeTypeMap;
import androidx.core.content.FileProvider;

import com.manacher.filesharing.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class FilesManager {

    private Activity context;
    public static String TAG_OPEN_FILE = "open_file";
    public static String TAG_SELECT_FILE = "select_file";

    public FilesManager(){

    }

    public FilesManager(Activity context) {
        this.context = context;
    }

    public File getFile(String path){
        return new File(path);
    }

    public boolean createFile(File file){

        try {
            Log.d("KL89", "createDirectory-1: " +"File is Created");
            return file.createNewFile();
        } catch (IOException e) {
            Log.d("KL89", "createDirectory-2: " +"File not is Created == " +e);
            e.printStackTrace();
            return false;
        }
    }

    public long gtFileSize(File file){

        if(file.exists()){
            return file.length();
        }else{
            return -1;
        }
    }

    public double getMbs(long size){
        return size * (0.00_00_00_95);
    }

    public File[] getFileList(File directory){
        return directory.listFiles();
    }

    public boolean isFolder(File file){
        return file.isDirectory();
    }

    public boolean isFolder(Uri filePath){
        File file = new File(String.valueOf(filePath));
        return file.isDirectory();
    }


    public boolean isFile(File file){
        return file.isFile();
    }

    public boolean isFile(Uri filePath){
        File file = new File(String.valueOf(filePath));
        return file.isFile();
    }

    public boolean isFileExists(File file){
        return file.exists();
    }

    public boolean isTrueType(File file, String types[]){

        boolean valid = false;

        String fileName = file.getName();

        for(String type:types) {
            String fileType = fileName.substring(fileName.lastIndexOf("."));
            if(TextUtils.equals(fileType.toLowerCase(), "."+type)){
                valid = true;
            }
        }
        return valid;

    }

    public boolean isApk(File file){

        String fileName = file.getName();

        if (fileName.toLowerCase().contains(".")){
            String fileType = fileName.substring(fileName.lastIndexOf("."));
            return TextUtils.equals(fileType.toLowerCase(), ".apk");
        }
    return false;
    }

    public boolean isPdf(File file){

        String fileName = file.getName();

        if (fileName.toLowerCase().contains(".")) {
            String fileType = fileName.substring(fileName.lastIndexOf("."));
            return TextUtils.equals(fileType.toLowerCase(), ".pdf");
        }
    return false;
    }

    public boolean isText(File file){

        String fileName = file.getName();

        if (fileName.toLowerCase().contains(".")) {
            String fileType = fileName.substring(fileName.lastIndexOf("."));
            if (TextUtils.equals(fileType.toLowerCase(), ".text") ||
                    TextUtils.equals(fileType.toLowerCase(), ".txt")) {
                return true;
            }
            return false;
        }
        return false;
    }

    public boolean isJpeg(File file){

        String fileName = file.getName();

        if (fileName.toLowerCase().contains(".")) {

            String fileType = fileName.substring(fileName.lastIndexOf("."));
            if (TextUtils.equals(fileType.toLowerCase(), ".jpeg") ||
                    TextUtils.equals(fileType.toLowerCase(), ".jpg")) {
                return true;
            }
            return  false;
        }
        return false;

    }

    public boolean isPng(File file){

        String fileName = file.getName();

        if (fileName.toLowerCase().contains(".")) {
            String fileType = fileName.substring(fileName.lastIndexOf("."));
            return TextUtils.equals(fileType.toLowerCase(), ".png");
        }
        return false;
    }


    public boolean isMp4(File file){

        String fileName = file.getName();


        if (fileName.toLowerCase().contains(".")) {
            String fileType = fileName.substring(fileName.lastIndexOf("."));
            if (TextUtils.equals(fileType.toLowerCase(), ".mp4") ||
                    TextUtils.equals(fileType.toLowerCase(), ".3gp")) {
                return true;
            }
            return false;
        }
        return false;
    }

    public boolean isMp3(File file){

        String fileName = file.getName();

        if (fileName.toLowerCase().contains(".")) {
            String fileType = fileName.substring(fileName.lastIndexOf("."));
            return (fileType.toLowerCase().equals(".mp3"));
        }
        return false;
    }

    public void createDirectory(String parentPath, String childPath){
        File file = new File(parentPath, childPath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public void createDirectory(File parentFile, String childPath){
        File file = new File(parentFile.getPath(), childPath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public void deleteDirectory(String parentPath, String childPath){
        File file = new File(parentPath, childPath);
        if (!file.exists()) {
            if (file.exists())
                file.delete();
        }
    }

    public void deleteFile(File file){
            if (file.exists())
                file.delete();
    }

    public String getMimeType(String url) {
        String type = null;
        String extension = getExtension(url);

        Log.d("GH67", "getMimeType: " +extension);

        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
            Log.d("GH67", "Type: " +type);
        }
        return type;
    }

    public String getMimeType(File file) {
        Uri url = Uri.fromFile(file);
        String type = null;
        String extension = getExtension(file);
        Log.d("GH67", "getMimeType: " +extension);

        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
            Log.d("GH67", "Type: " +type);
        }
        return type;
    }

    public String getExtension(File file){

        Log.d("DR23", "getExtension: "+file.getPath());
        if (isFolder(file)){
            return "folder";
        }else if (isApk(file)){
            return "apk";

        }else if (isJpeg(file)){
            return "jpg";

        }else if (isPng(file)){
            return "png";

        }else if (isPdf(file)){
            return "pdf";

        }else if (isText(file)){
            return "text";

        }else if (isMp3(file)){
            return "mp3";

        }else if (isMp4(file)){
            return "mp4";
        }
        else {
            return MimeTypeMap.getFileExtensionFromUrl(String.valueOf(file.getPath()));
        }
    }

    public String getExtension(String filePath){
        File file = new File(filePath);

        if (isFolder(file)){
            return "folder";
        }else if (isApk(file)){
            return "apk";

        }else if (isJpeg(file)){
            return "jpg";

        }else if (isPng(file)){
            return "png";

        }else if (isPdf(file)){
            return "pdf";

        }else if (isText(file)){
            return "text";

        }else if (isMp3(file)){
            return "mp3";

        }else if (isMp4(file)){
            return "mp4";

        }

        else {
            return MimeTypeMap.getFileExtensionFromUrl(String.valueOf(file.getPath()));
        }
    }

    public Uri getUri(String authority, File file){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return FileProvider.getUriForFile(context, authority, file);

        } else {
            return Uri.fromFile(file);

        }
    }

    public long getFileSize(File file) {

        if (getFileList(file) == null){
            return 0;
        }
        Log.d("PL09", "fileSize: " +file.length());

        long size = 0;
        for (File item : Objects.requireNonNull(file.listFiles())) {
            if (item.isFile()) {
                System.out.println(item.getName() + " " + item.length());
                size += item.length();
            }
            else
                size += getFileSize(item);
        }
        return size;
    }

    public long getFileSize(String filePath) {
        File file = new File(filePath);

        if (getFileList(file) == null){
            return 0;
        }
        Log.d("PL09", "fileSize: " +file.length());

        long size = 0;
        for (File item : Objects.requireNonNull(file.listFiles())) {
            if (item.isFile()) {
                System.out.println(item.getName() + " " + item.length());
                size += item.length();
            }
            else
                size += getFileSize(item);
        }
        return size;
    }

    public boolean isBagHasItem(List<FileItem> list, String fileName){

        for (FileItem fileItem:list
        ) {
            if (fileItem.getFileName().toLowerCase().equals(fileName.toLowerCase())) {
                return  true;
            }
        }
        return false;
    }

    public void openFile(File file){
        try {

        Intent intent;
        Uri apkUri = getUri(context.getString(R.string.authority), file);


            intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(apkUri, getMimeType(file));

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            context.startActivity(intent);

        } catch (Exception e) {
            // no Activity to handle this kind of files
        }
    }

    public ArrayList<File> getAllFileList(String rootPath, String fileType) {
        ArrayList<File> finalFileList = new ArrayList<>();

        try {
            File rootFolder = new File(rootPath);
            File[] files = rootFolder.listFiles(); //here you will get NPE if directory doesn't contains  any file,handle it like this.
            assert files != null;
            for (File file : files) {
                if (file.isDirectory()) {
                    if (getAllFileList(file.getAbsolutePath(), fileType) != null) {
                        finalFileList.addAll(getAllFileList(file.getAbsolutePath(), fileType));

                    } else {
                        break;
                    }
                } else if (file.getName().endsWith(fileType)) {
                    finalFileList.add(new File(file.getAbsolutePath()));

                }
            }
            return finalFileList;
        } catch (Exception e) {
            return null;
        }
    }


    @SuppressLint("UseCompatLoadingForDrawables")

    public Drawable getIcon(String fileName){

                if (fileName.toLowerCase().contains("pdf")) {

                    return (context.getResources().getDrawable(R.drawable.pdf));

                } else if (fileName.toLowerCase().contains("text") || fileName.toLowerCase().contains("txt")) {

                    return (context.getResources().getDrawable(R.drawable.txt));

                } else if (fileName.toLowerCase().contains("mp3")) {

                    return (context.getResources().getDrawable(R.drawable.mp3));

                } else if (fileName.toLowerCase().contains("jpeg") || fileName.toLowerCase().contains("jpg")) {

                    return (context.getResources().getDrawable(R.drawable.jpg));

                } else if (fileName.toLowerCase().contains("png")) {

                    return (context.getResources().getDrawable(R.drawable.png));

                } else if (fileName.toLowerCase().contains("mp4")) {

                    return (context.getResources().getDrawable(R.drawable.video_file));

                } else if (fileName.toLowerCase().contains("apk")) {

                    return (context.getResources().getDrawable(R.drawable.apk));

                } else {
                    return (context.getResources().getDrawable(R.drawable.file_icon2));
                }
    }

    public static long getRealSizeFromUri(Context context, Uri uri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Audio.Media.SIZE };
            cursor = context.getContentResolver().query(uri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE);
            cursor.moveToFirst();
            return Long.parseLong(cursor.getString(column_index));
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
