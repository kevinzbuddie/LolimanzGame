package com.gamezone.loliman.lolimanzgame.fileOperations;

import android.os.Environment;
import android.support.annotation.NonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Loliman on 2018/1/14.
 */

public class FileOperations extends File {

    private String mPathName;
    private String mFileName;

    public FileOperations(@NonNull String pathname, String filename) {
        super(pathname, filename);
        mPathName = pathname;
        mFileName = filename;
    }

    public boolean FileIsExist( ){
        try
        {
            File f=new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + mPathName + File.separator + mFileName);
            if(!f.exists())
            {
                return false;
            }
        }
        catch (Exception e)
        {
            return false;
        }
        return true;
    }

    public boolean CreateNewFile() throws IOException
    {
        try
        {
            //external file ops
            File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator + mPathName);
            if (!dir.exists()){
                dir.mkdirs();
            }
            File f = new File(dir, mFileName);
            if (!f.exists()){
                if (dir.exists()) {
                    return f.createNewFile();
                }
            }else if(f.exists()){
                f.delete();
                return f.createNewFile();
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return true;
    }

    public boolean DeleteFile()
    {
        File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + mPathName + File.separator + mFileName);
        if(f.exists()){
            return f.delete();
        }
        return false;
    }

    public void WriteFile(String Content) throws IOException
    {
        try
        {
            //external file ops
            File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + mPathName + File.separator + mFileName);

            FileOutputStream fos = new FileOutputStream(f);
            fos.write(Content.getBytes());
            fos.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public String ReadFile() throws IOException
    {
        try
        {
            //external file ops
            File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + mPathName + File.separator + mFileName);

            FileInputStream fis = new FileInputStream(f);

            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            return new String(buffer);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
