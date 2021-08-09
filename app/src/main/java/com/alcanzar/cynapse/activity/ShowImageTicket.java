package com.alcanzar.cynapse.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.utils.AppConstantClass;
import com.alcanzar.cynapse.utils.CheckForSDCard;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class ShowImageTicket extends AppCompatActivity {
    ImageView btnBack, titleIcon, img_showTickt;
    RelativeLayout head_rel_lay;
    TextView title, txt_download;
    String URL,brochuers_file;
    String root="";
    private File imgFile;
    //String URL="http://www.freeimageslive.com/galleries/objects/general/pics/woodenbox0482.jpg";
    ProgressDialog mProgressDialog;
    private ProgressDialog pDialog;
    private String downloadUrl = "", downloadFileName = "";
    public static final int progress_bar_type = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_show_image_ticket);
        btnBack = findViewById(R.id.btnBack);
        head_rel_lay = findViewById(R.id.head_rel_lay);
        img_showTickt = findViewById(R.id.img_showTickt);
        titleIcon = findViewById(R.id.titleIcon);
        txt_download = findViewById(R.id.txt_download);
        titleIcon.setVisibility(View.GONE);
        title = findViewById(R.id.title);
        title.setVisibility(View.GONE);
        if (getIntent() != null) {
            URL = getIntent().getStringExtra("image");
            brochuers_file=getIntent().getStringExtra("brochuers_file");
        }
        Log.d("RULLRLR", brochuers_file);

//        Glide.with(getApplicationContext()).load(URL)
//                .thumbnail(0.5f)
//                .crossFade()
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .error(R.drawable.whey_protien)
//                .into(img_showTickt);


        Picasso.with(getApplicationContext()).load(URL).error(R.drawable.avatar).into(img_showTickt);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        txt_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ActivityCompat.checkSelfPermission(ShowImageTicket.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(ShowImageTicket.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions((Activity) ShowImageTicket.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},1001);
                    return;

                }
                else
                {

                    downloadFileName = brochuers_file.replace(AppConstantClass.HOST4+"webroot/files/brochures/", "");
                    new DownloadFileFromURL().execute(brochuers_file);

                }

                // new DownloadingTask(ShowImageTicket.this, txt_download, URL).execute();
            }
        });
    }

    class DownloadingTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog pDialog;
        File apkStorage = null;
        File outputFile = null;
        String filePath="";
        private static final String TAG = "Download Task";
        private Context context;
        private TextView buttonText;
        private String downloadUrl = "", downloadFileName = "";

        public DownloadingTask(Context context, TextView buttonText, String downloadUrl) {
            this.context = context;
            this.buttonText = buttonText;
            this.downloadUrl = downloadUrl;
            // pDialog.setMessage("Downloading file...");
            pDialog = new ProgressDialog(context, R.style.DialogTheme);
            pDialog.setCancelable(false);
            pDialog.show();
            View v = LayoutInflater.from(context).inflate(R.layout.custom_progress_view, null, false);
            pDialog.setContentView(v);
            //downloadFileName = downloadUrl.replace(Utils.mainUrl, "");//Create file name by picking download file name from URL
            downloadFileName = downloadUrl.replace(AppConstantClass.HOST4+"webroot/files/brochures/", "");//Create file name by picking download file name from URL
            Log.e(TAG, downloadFileName);
            filePath= Environment.getExternalStorageDirectory() + "/"
                    + "Cynapse/"+downloadFileName;

            //Start Downloading Task
            // new DownloadingTask().execute();
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            buttonText.setEnabled(false);
            buttonText.setText(R.string.downloadStarted);//Set Button Text when download started
            pDialog.setMessage("Downloading file. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setMax(100);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                pDialog.dismiss();
                if (outputFile != null) {
                    buttonText.setEnabled(true);
                    buttonText.setText(R.string.downloadCompleted);//If Download completed then change button text
                } else {
                    // buttonText.setText(R.string.downloadFailed);//If download failed change button text
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            buttonText.setEnabled(true);
                            // buttonText.setText(R.string.downloadAgain);//Change button text again after 3sec
                        }
                    }, 3000);

                    Log.e(TAG, "Download Failed");

                }
            } catch (Exception e) {
                e.printStackTrace();

                //Change button text if exception occurs
                buttonText.setText(R.string.downloadFailed);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        buttonText.setEnabled(true);
                        // buttonText.setText(R.string.downloadAgain);
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
                    Log.d("SDCARDDD", "SDVARIDDIIGG");
                    apkStorage = new File(
                            Environment.getExternalStorageDirectory() + "/"
                                    + "Cynapse");
                } else
                    Toast.makeText(context, "Oops!! There is no SD Card.", Toast.LENGTH_SHORT).show();

                //If File is not present create directory
                if (!apkStorage.exists()) {
                    apkStorage.mkdir();
                    Log.e(TAG, "Directory Created.");
                }

                outputFile = new File(apkStorage, downloadFileName);//Create Output file in Main File
                scanMedia(filePath, context);

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
    void scanMedia(String path, Context context) {
        MediaScannerConnection.scanFile(context, new String[]{path}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        Log.i("ExternalStorage", "Scanned " + path + ":");
                        Log.i("ExternalStorage", "-> uri=" + uri);

                    }
                });
    }
    class DownloadFileFromURL extends AsyncTask<String, String, String> {


        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ShowImageTicket.this);
            pDialog.setMessage("Downloading file. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setMax(100);
            pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pDialog.setCancelable(true);
            pDialog.show();

            (ShowImageTicket.this).showDialog(progress_bar_type);
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
                File rootFile = new File(root, "Cynapse" );
                if (!rootFile.exists()) {
                    rootFile.mkdirs();
                }

                String imagePath = rootFile+ "/" + downloadFileName;
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



            galleryAddPic(imgFile, ShowImageTicket.this);
//            c.finish();


        }

        void openFile(Context context, File url) throws IOException {

            Uri uri = Uri.fromFile(url);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            if (url.getAbsolutePath().contains(".pdf")) {
                intent.setDataAndType(uri, "application/pdf");
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            //CustomPreference.pdf_boolean=true;
//            c.finish();
        }

        public void galleryAddPic(final File file, final Context context) {
            MediaScannerConnection.scanFile(context, new String[]{file.toString()}, (String[]) null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, final Uri uri) {
                            try {
                                Log.e("separated", "fileNameee" + file.getAbsolutePath());

                                openFile(ShowImageTicket.this, file);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }
    }
}
