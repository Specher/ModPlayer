package com.specher.modplayer;



import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;

/**
 * Created by su on 2016/6/4.
 */
public class FileUtil {

    private static final String TAG = "FileUtil";

    public static final String LOCAL = "ModPlayer";

    public static final String LOCAL_PATH = Environment.getExternalStorageDirectory().getPath() + File.separator;

    /**
     * 褰曢煶鏂囦欢鐩綍
     */
    public static final String REC_PATH = LOCAL_PATH + LOCAL + File.separator;


    /**
     * 鑷姩鍦⊿D鍗″垱寤虹浉鍏崇殑鐩綍
     */
    static {
        File dirRootFile = new File(LOCAL_PATH);
        if (!dirRootFile.exists()) {
            dirRootFile.mkdirs();
        }
        File recFile = new File(REC_PATH);
        if (!recFile.exists()) {
            recFile.mkdirs();
        }
    }

    private FileUtil() {
    }

    /**
     * 鍒ゆ柇鏄惁瀛樺湪瀛樺偍绌洪棿	 *
     *
     * @return
     */
    public static boolean isExitSDCard() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    private static boolean hasFile(String fileName) {
        File f = createFile(fileName);
        return null != f && f.exists();
    }

    public static void deleteFile(String name) {
        File myCaptureFile = getFile(name);
        if (myCaptureFile.exists()) {
            myCaptureFile.delete();
        }
    }



    public static File getFile(String name){
        File myCaptureFile = new File(REC_PATH + name);
        if (myCaptureFile.exists()) {
           return  myCaptureFile;
        }else{
            return  null;
        }
    }
    public static String getPath(Context context, Uri uri) {

        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { "_data" };
            Cursor cursor = null;

            try {
                cursor = context.getContentResolver().query(uri, projection,null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
            }
        }

        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }
    public static File createFile(String fileName) {

        File myCaptureFile = new File(REC_PATH + fileName);
        if (myCaptureFile.exists()) {
            myCaptureFile.delete();
        }
        try {
            myCaptureFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return myCaptureFile;
    }

    // 灏嗗瓧绗︿覆鍐欏叆鍒版枃鏈枃浠跺熬閮�
    public static void writeTxtToFile(File file,String strcontent) {
        try {
            RandomAccessFile raf = new RandomAccessFile(file, "rwd");
            raf.seek(file.length());
            raf.write(strcontent.getBytes());
            raf.close();
        } catch (Exception e) {
            Log.e(TAG, "write File Error :" + e);
        }
    }

    // 灏嗗瓧绗︿覆鍐欏叆鍒版枃浠�
    public static void writeTxtFile(File file,String strcontent) {
        try {
            RandomAccessFile raf = new RandomAccessFile(file, "rwd");

            raf.seek(0);
            raf.write(strcontent.getBytes());
            raf.close();
        } catch (Exception e) {
            Log.e(TAG, "write File Error :" + e);
        }
    }

    //璇诲彇鏂囨湰鏂囦欢涓殑鍐呭
    public static String ReadTxtFile(String strFilePath)
    {
        String path = strFilePath;
        StringBuilder content = new StringBuilder(); //鏂囦欢鍐呭瀛楃涓�
        //鎵撳紑鏂囦欢

        try {
            File file = new File(path);
            //濡傛灉path鏄紶閫掕繃鏉ョ殑鍙傛暟锛屽彲浠ュ仛涓�涓潪鐩綍鐨勫垽鏂�
            if (file.isDirectory()) {
                Log.d(TAG, "The File doesn't not exist.");
            } else {

                InputStream instream = new FileInputStream(file);
                if (instream != null) {
                    InputStreamReader inputreader = new InputStreamReader(instream);
                    BufferedReader buffreader = new BufferedReader(inputreader);
                    String line;
                    //鍒嗚璇诲彇
                    while ((line = buffreader.readLine()) != null) {
                        content.append(line).append("\n");
                    }
                    instream.close();
                }
            }
        }
            catch (java.io.FileNotFoundException e)
            {
                Log.d("TestFile", "The File doesn't not exist.");
            }
            catch (IOException e)
            {
                Log.d(TAG, e.getMessage());
            }

        return content.toString();
    }

}
