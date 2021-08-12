package com.alcanzar.cynapse.adapter;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.TextView;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.model.ImageModel;
import com.alcanzar.cynapse.utils.AppConstantClass;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class ImageShowAdapter extends RecyclerView.Adapter<ImageShowAdapter.TicketsViewHolder> {
    private Context context;
    private int rowLayout;
    private ArrayList<ImageModel> arrayList;
    String root = "", downloadFileName = "";
    private ProgressDialog pDialog;
    private File imgFile;
    public ImageShowAdapter(Context context, int rowLayout, ArrayList<ImageModel> arrayList) {
        this.context = context;
        this.rowLayout = rowLayout;
        this.arrayList = arrayList;


    }


    public class TicketsViewHolder extends RecyclerView.ViewHolder {

        TextView tv_imagname;


        public TicketsViewHolder(View itemView) {
            super(itemView);
            tv_imagname = itemView.findViewById(R.id.tv_imagname);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) context,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1001);
                    return;

                } else {
                        downloadFileName=arrayList.get(getAdapterPosition()).getImage_name().replace(AppConstantClass.HOST4+"webroot/files/brochures/", "");
                        Log.d("DOWNLOADIMAGNAME",downloadFileName);

//                    downloadFileName = brochuers_file.replace("http://162.243.205.148/cynapce/app/webroot/files/brochures/", "");
                    new DownloadFileFromURL().execute(arrayList.get(getAdapterPosition()).getImage_name());

                }
                }
            });

        }
    }

    @Override
    public ImageShowAdapter.TicketsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(rowLayout, parent, false);
        return new TicketsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageShowAdapter.TicketsViewHolder holder, int position) {


        holder.tv_imagname.setText(arrayList.get(position).getImage_name().replace(AppConstantClass.HOST4+"webroot/files/brochures/", ""));


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public void removeAt(int position) {
        arrayList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, arrayList.size());
    }

    class DownloadFileFromURL extends AsyncTask<String, String, String> {


        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(context);
            pDialog.setMessage("Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setMax(100);
            pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pDialog.setCancelable(true);
            pDialog.show();


            root = Environment.getExternalStorageDirectory().toString();
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();
                // getting file length
                int lenghtOfFile = conection.getContentLength();

                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

//                File file = new File(Environment.DIRECTORY_DOCUMENTS /);
                root = Environment.getExternalStorageDirectory().toString();
                File rootFile = new File(root, "Cynapse");
                if (!rootFile.exists()) {
                    rootFile.mkdirs();
                }

                String imagePath = rootFile + "/" + downloadFileName;
                System.out.println("imagePath==" + imagePath);

                imgFile = new File(imagePath);


                try {
                    imgFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                // Output stream to write file
                OutputStream output = new FileOutputStream(imgFile.getAbsolutePath());


                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();


            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        /**
         * After completing background task
         * Dismiss the progress dialog
         **/
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded
//            dismissDialog(progress_bar_type);
            pDialog.dismiss();

            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);

            Log.d("IMGFILEOLIEIIE", imgFile + "");

            MimeTypeMap mime = MimeTypeMap.getSingleton();
            String ext1 = imgFile.getName().substring(imgFile.getName().indexOf(".") + 1).toLowerCase();
            String type = mime.getMimeTypeFromExtension(ext1);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Uri contentUri = FileProvider.getUriForFile(context, "com.alcanzar.cynapse.provider", imgFile);
                intent.setDataAndType(contentUri, type);
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            } else {
                intent.setDataAndType(Uri.fromFile(imgFile), type);

                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            }

           context.startActivity(intent);

        }
    }
}
