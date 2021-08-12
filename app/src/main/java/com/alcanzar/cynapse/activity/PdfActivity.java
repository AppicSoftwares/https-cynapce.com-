package com.alcanzar.cynapse.activity;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.utils.AppConstantClass;
import com.github.barteksc.pdfviewer.PDFView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PdfActivity extends AppCompatActivity {
    PDFView pdfView;
    ProgressBar tkt_progressBar;
    String pdfurl="";
    ImageView btnBack;
    TextView title;
    String getPdfurl;
    WebView webview;
    String url= AppConstantClass.HOST4+"webroot/files/brochures/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);
        pdfView = findViewById(R.id.pdfView);
        btnBack=findViewById(R.id.btnBack);
        tkt_progressBar=findViewById(R.id.tkt_progressBar);
        title=findViewById(R.id.title);
        webview=findViewById(R.id.webview);
        title.setText("Pdf View");

        webview.getSettings().setJavaScriptEnabled(true);


        if(getIntent()!=null)
        {
            pdfurl = getIntent().getStringExtra("pdfurl");
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1001);
            return;

        } else {
            tkt_progressBar.setVisibility(View.VISIBLE);
            pdfView.setVisibility(View.VISIBLE);
           // getPdfurl=url+pdfurl;
            Log.d("UPEKDLDF","<><"+getPdfurl);
            new RetrievePDFStream().execute(pdfurl.trim());
//            downloadFileName = arrayList.get(getAdapterPosition()).replace("http://162.243.205.148/cynapce/app/webroot/files/brochures/", "");
//            Log.d("DOWNLOADIMAGNAME", downloadFileName);

//                    downloadFileName = brochuers_file.replace("http://162.243.205.148/cynapce/app/webroot/files/brochures/", "");
            // new DownloadFileFromURL().execute(arrayList.get(getAdapterPosition()));

        }
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    class RetrievePDFStream extends AsyncTask<String, Void, InputStream> {

        @Override
        protected InputStream doInBackground(String... params) {
            InputStream inputStream = null;
            try {
                URL url = new URL(params[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }
            } catch (IOException e) {
                return null;
            }
            return inputStream;

        }

        @Override
        protected void onPostExecute(InputStream inputStream) {

            pdfView.fromStream(inputStream)
                    //.pages(0, 2, 1, 3, 3, 3) // all pages are displayed by default
                    .enableSwipe(true) // allows to block changing pages using swipe
                    .swipeHorizontal(false)
                    .enableDoubletap(true)
                    .defaultPage(0)

                    // allows to draw something on the current page, usually visible in the middle of the screen
                    //.onDraw(onDrawListener)
                    // allows to draw something on all pages, separately for every page. Called only for visible pages
                    //.onDrawAll(onDrawListener)
                    // .onLoad(onLoadCompleteListener) // called after document is loaded and starts to be rendered
                    //.onPageChange(onPageChangeListener)
                    //.onPageScroll(onPageScrollListener)
                    //.onError(onErrorListener)
                    //.onRender(onRenderListener) // called after document is rendered for the first time
                    // called on single tap, return true if handled, false to toggle scroll handle visibility
                    // .onTap(onTapListener)
                    .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
                    .password(null)
                    .scrollHandle(null)
                    .enableAntialiasing(true) // improve rendering a little bit on low-res screens
                    // spacing between pages in dp. To define spacing color, set view background
                    .spacing(0)
                    .load();

            new Handler().postDelayed(new Runnable() {


                @Override
                public void run() {
                    //  pDialog.setVisibility(View.GONE);
                    tkt_progressBar.setVisibility(View.GONE);

                }
            }, 10000);
        }
    }
}
