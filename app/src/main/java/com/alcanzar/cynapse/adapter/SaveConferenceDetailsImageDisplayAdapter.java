package com.alcanzar.cynapse.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.Toast;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.activity.ImagePDFDisplay;
import com.alcanzar.cynapse.activity.ShowImageTicket;
import com.alcanzar.cynapse.utils.MyToast;
import com.alcanzar.cynapse.utils.ServiceUtility;
import com.alcanzar.cynapse.utils.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
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
import java.util.ArrayList;
import java.util.Random;

import uk.co.senab.photoview.PhotoViewAttacher;


public class SaveConferenceDetailsImageDisplayAdapter extends PagerAdapter {

    private ArrayList<String> images;
    private LayoutInflater inflater;
    private Context context;
    private PDFView pdfView;
    private PhotoViewAttacher photoViewAttacher;

    public SaveConferenceDetailsImageDisplayAdapter(Context context, ArrayList<String> images) {

        this.context = context;
        this.images = images;
        inflater = LayoutInflater.from(context);
        ImageView imageView;

        notifyDataSetChanged();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView((View) object);
    }

    @Override
    public int getCount() {

        return images.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {

        View myImageLayout = inflater.inflate(R.layout.slide, view, false);
        final ImageView myImage =  myImageLayout.findViewById(R.id.image);

        if (checkFileExe(images.get(position)).equalsIgnoreCase(".pdf"))
        {
//            Glide.with(context).load(R.drawable.pdf_viewer)
//                    .thumbnail(0.5f)
//                    .crossFade()
//                    .placeholder(R.drawable.animation_loading_circle)
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .error(R.drawable.no_img_placeholder)
//                    .into(myImage);
            Picasso.with(context).load(R.drawable.pdf_viewer).placeholder(R.drawable.animation_loading_circle).error(R.drawable.no_img_placeholder).into(myImage);

        } else
            {
//            Glide.with(context).load(images.get(position))
//                    .placeholder(R.drawable.animation_loading_circle)
//                    .thumbnail(0.5f)
//                    .crossFade()
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .error(R.drawable.no_img_placeholder)
//                    .into(myImage);

            //Picasso.with(context).load(images.get(position)).error(R.drawable.whey_protien).into(myImage);
                Picasso.with(context).load(images.get(position)).placeholder(R.drawable.animation_loading_circle).error(R.drawable.no_img_placeholder).into(myImage);
            }

//
        myImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                if (myImage.getDrawable().getConstantState() == ContextCompat.getDrawable(context, R.drawable.no_img_placeholder).getConstantState()) {

                    MyToast.toastLong(((Activity)context),"No Image Available");
                }
                else {
                    Intent intent = new Intent(context, ImagePDFDisplay.class);
                    intent.putExtra("res", images.get(position));
                    context.startActivity(intent);
                }
//                final Dialog dialogSociallog = new Dialog(context);
//                dialogSociallog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                dialogSociallog.setContentView(R.layout.image_load);
//                Window window = dialogSociallog.getWindow();
//                window.setLayout(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT);
//                dialogSociallog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//
//                ImageView imageView = dialogSociallog.findViewById(R.id.loadImg);
//                pdfView = dialogSociallog.findViewById(R.id.pdfView);
//                //ImageView closeImg = dialogSociallog.findViewById(R.id.closeImg);
//
//                if (checkFileExe(images.get(position)).equalsIgnoreCase(".pdf")) {
//                    pdfView.setVisibility(View.VISIBLE);
//                    imageView.setVisibility(View.GONE);
//                    openPdf(images.get(position), context);
//                    //downloadPDF(images.get(position));
//                } else {
//                    pdfView.setVisibility(View.GONE);
//                    photoViewAttacher = new PhotoViewAttacher(imageView);
//                    photoViewAttacher.update();
//                    imageView.setVisibility(View.VISIBLE);
//                    Picasso.with(context)
//                            .load(images.get(position))
//                            .placeholder(R.drawable.animation_loading_circle)
//                            .error(R.drawable.no_img_placeholder)
//                            .into(imageView);
//                }
//                closeImg.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        dialogSociallog.dismiss();
//                    }
//                });
                //dialogSociallog.show();
            }
        });

//        Picasso.with(context)
//                .load(images.get(position))
//                .placeholder(R.drawable.placeholder)
//                .error(R.drawable.placeholder)
//                .into(myImage);


        //myImage.setImageResource(Integer.parseInt(images.get(position)));
        view.addView(myImageLayout, 0);
        return myImageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
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

            pDialog = new ProgressDialog(context, R.style.DialogTheme);
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

    private String fileName;
    private ProgressDialog pDialog;

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