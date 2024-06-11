package com.vicky.hackstraz;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
//import android.widget.Button;
import android.widget.Button;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadTask {

    private static final String TAG = "Download Task";
    private Context context;
    private Button btn;
    private String downloadUrl = "", downloadFileName = "";

    public DownloadTask(Context context, Button btn, String downloadUrl,String filename) {
        this.context = context;
        this.btn=btn;
        this.downloadUrl = URLs.URL_DOWNLOAD+downloadUrl;

        downloadFileName = filename+".pdf";//Create file name by picking download file name from URL
        Log.e(TAG, downloadFileName);

        //Start Downloading Task
        new DownloadingTask().execute();
    }

    private class DownloadingTask extends AsyncTask<Void, Void, Void> {

        File apkStorage = null;
        File outputFile = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            btn.setText("Downloading..!");
            Toast.makeText(context,"Starting Download..!!",Toast.LENGTH_SHORT).show();//Set Button Text when download started
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                if (outputFile != null) {
                    Toast.makeText(context,"Download completed..!!",Toast.LENGTH_SHORT).show();//Set Button Text when download started//If Download completed then change button text
                    btn.setText("Open");
                    NotificationManager notif=(NotificationManager)
                              context.getSystemService(Context.NOTIFICATION_SERVICE);
                    Notification notify;
                    if (Build.VERSION.SDK_INT < 16) {
                        notify=new Notification.Builder
                                (context).setContentTitle("HACKSTARZ").setContentText("Download").
                                setContentTitle("Certificates Downloaded Successfully..!").setSmallIcon(R.drawable.ic_file_download_black_24dp).getNotification();

                    } else {
                        notify=new Notification.Builder
                                (context).setContentTitle("HACKSTARZ").setContentText("Download").
                                setContentTitle("Certificates Downloaded Successfully..!").setSmallIcon(R.drawable.ic_file_download_black_24dp).build();

                    }

                    notify.flags |= Notification.FLAG_AUTO_CANCEL;
                    notif.notify(0, notify);
                } else {
                    btn.setText("Download failed!!");
                    Toast.makeText(context,"Download Failed..!!",Toast.LENGTH_SHORT).show();//Set Button Text when download started
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                        }
                    }, 3000);

                    Log.e(TAG, "Download Failed");

                }
            } catch (Exception e) {
                e.printStackTrace();

                //Change button text if exception occurs
                btn.setText("Download Failed!!");
                Toast.makeText(context,"Download Failed..!!",Toast.LENGTH_SHORT).show();//Set Button Text when download started
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                    }
                }, 3000);
                Log.e(TAG, "Download Failed with Exception - " + e.getLocalizedMessage());

            }


            super.onPostExecute(result);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                URL url = new URL(downloadUrl);//Create Download URl
                HttpURLConnection c = (HttpURLConnection) url.openConnection();//Open Url Connection
                c.setRequestMethod("GET");//Set Request Method to "GET" since we are grtting data
                c.connect();//connect the URL Connection

                //If Connection response is not OK then show Logs
                if (c.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    Log.e(TAG, "Server returned HTTP " + c.getResponseCode()
                            + " " + c.getResponseMessage());

                }


                //Get File if SD card is present
                if (new CheckForSDCard().isSDCardPresent()) {

                    apkStorage = new File(
                            Environment.getExternalStorageDirectory() + "/"
                                    + URLs.DIR_DOWNLOAD);
                } else
                    Toast.makeText(context, "Oops!! There is no SD Card.", Toast.LENGTH_SHORT).show();

                //If File is not present create directory
                if (!apkStorage.exists()) {
                    apkStorage.mkdir();
                    Log.e(TAG, "Directory Created.");
                }

                outputFile = new File(apkStorage, downloadFileName);//Create Output file in Main File

                //Create New File if not present
                if (!outputFile.exists()) {
                    outputFile.createNewFile();
                    Log.e(TAG, "File Created");
                }

                FileOutputStream fos = new FileOutputStream(outputFile);//Get OutputStream for NewFile Location

                InputStream is = c.getInputStream();//Get InputStream for connection

                byte[] buffer = new byte[1024];//Set buffer type
                int len1 = 0;//init length
                while ((len1 = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len1);//Write new file
                }

                //Close all connection after doing task
                fos.close();
                is.close();

            } catch (Exception e) {

                //Read exception if something went wrong
                e.printStackTrace();
                outputFile = null;
                Log.e(TAG, "Download Error Exception " + e.getMessage());
            }

            return null;
        }
    }
}