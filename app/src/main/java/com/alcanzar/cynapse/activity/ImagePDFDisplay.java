package com.alcanzar.cynapse.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.alcanzar.cynapse.R;

import com.alcanzar.cynapse.utils.MyToast;
import com.github.barteksc.pdfviewer.PDFView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import uk.co.senab.photoview.PhotoViewAttacher;

public class ImagePDFDisplay extends AppCompatActivity
{

    ImageView imageView;
    PDFView pdfView;
    private PhotoViewAttacher photoViewAttacher;

    private String fileName;
    private ProgressDialog pDialog;
    String res = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_load);

        imageView = findViewById(R.id.loadImg);
        pdfView = findViewById(R.id.pdfView);

        res = getIntent().getStringExtra("res");

        if (checkFileExe(res).equalsIgnoreCase(".pdf")) {
            pdfView.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.GONE);
            openPdf(res, this);
            //downloadPDF(res);
        } else {
            pdfView.setVisibility(View.GONE);
            photoViewAttacher = new PhotoViewAttacher(imageView);
            photoViewAttacher.update();
            imageView.setVisibility(View.VISIBLE);
            Picasso.with(this)
                    .load(res)
                    .placeholder(R.drawable.animation_loading_circle)
                    .error(R.drawable.no_img_placeholder)
                    .into(imageView);
        }

    }

    private String checkFileExe(String str) {
        String string = str.substring(str.length() - 4, str.length());
        return string;
    }

    private void openPdf(String filePath, Context context) {
        try {
//            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/PDFDOWNLOAD/"+filePath);
//            if (file.exists()) {
//                pdfView.fromFile(file).load();
//            } else {
//                downloadPDF(filePath);
//            }

            downloadPDF(filePath);

        } catch (Exception e) {
            MyToast.toastLong((Activity) context, e.getMessage());
        }
    }


     public void downloadPDF(String path) {
        new DownloadFile().execute(path);
    }

    private class DownloadFile extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(ImagePDFDisplay.this, R.style.DialogTheme);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {
            String fileUrl = strings[0];
// -> https://letuscsolutions.files.wordpress.com/2015/07/five-point-someone-chetan-bhagat_ebook.pdf
            fileName = "pdfFile.pdf";
            //->five-point-someone-chetan-bhagat_ebook.pdf
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File folder = new File(extStorageDirectory, "PDFDOWNLOAD");

            if (!folder.isDirectory()) {
                folder.mkdir();
            }
            File pdfFile = new File(folder, fileName);

            try {
                pdfFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            FileDownloader.downloadFile(fileUrl, pdfFile);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/PDFDOWNLOAD/" + fileName);
            if (file.exists()) {
                pdfView.fromFile(file).load();
            } else {
                //do stuff
            }
            pDialog.dismiss();
            //Toast.makeText(context, "Download PDf successfully", Toast.LENGTH_SHORT).show();
            Log.d("Download complete", "----------");
        }
    }

}


class FileDownloader {
    private static final int MEGABYTE = 1024 * 1024;

    public static void downloadFile(String fileUrl, File directory) {
        try {

            URL url = new URL(fileUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            //urlConnection.setRequestMethod("GET");
            //urlConnection.setDoOutput(true);
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            FileOutputStream fileOutputStream = new FileOutputStream(directory);
            int totalSize = urlConnection.getContentLength();

            byte[] buffer = new byte[MEGABYTE];
            int bufferLength = 0;

            while ((bufferLength = inputStream.read(buffer)) > 0) {
                fileOutputStream.write(buffer, 0, bufferLength);
            }

            fileOutputStream.close();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
