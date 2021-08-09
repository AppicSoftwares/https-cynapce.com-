package com.alcanzar.cynapse.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.activity.PdfActivity;
import com.alcanzar.cynapse.api.DeletePdfApi;
import com.alcanzar.cynapse.appDatabase.DatabaseHelper;
import com.alcanzar.cynapse.model.PdfDelModel;
import com.alcanzar.cynapse.utils.AppConstantClass;
import com.alcanzar.cynapse.utils.AppCustomPreferenceClass;
import com.android.volley.VolleyError;
import com.github.barteksc.pdfviewer.PDFView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class PdfDelConf extends RecyclerView.Adapter<PdfDelConf.TicketsViewHolder> {
    private Context context;
    private int rowLayout;
    private ArrayList<String> arrayList;
    private ArrayList<PdfDelModel> arrayListPdf;
    String root = "", downloadFileName = "", conference_id = "";
    private ProgressDialog pDialog;
    ProgressBar tkt_progressBar;
    PDFView pdfView;
    private File imgFile;
    DatabaseHelper handler;

    public PdfDelConf(Context context, int rowLayout, ArrayList<String> arrayList, PDFView pdfView, ProgressBar tkt_progressBar, String conference_id,ArrayList<PdfDelModel> arrayListPdf) {
        this.context = context;
        this.rowLayout = rowLayout;
        this.arrayList = arrayList;
        this.pdfView = pdfView;
        this.tkt_progressBar = tkt_progressBar;
        this.conference_id = conference_id;
        this.arrayListPdf=arrayListPdf;
        handler= new DatabaseHelper(context);
    }


    public class TicketsViewHolder extends RecyclerView.ViewHolder {

        TextView tv_imagname;
        ImageView img_delete;
        RelativeLayout rel_pdf;
        LinearLayout lnr_pdf;

        public TicketsViewHolder(View itemView) {
            super(itemView);
            tv_imagname = itemView.findViewById(R.id.tv_imagname);
            img_delete = itemView.findViewById(R.id.img_delete);
            rel_pdf = itemView.findViewById(R.id.rel_pdf);
            lnr_pdf = itemView.findViewById(R.id.lnr_pdf);




            rel_pdf.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("kdkdkdkdkdTTTTTT", arrayListPdf.get(getAdapterPosition()).getPdf_url());
                    Intent intent = new Intent(context, PdfActivity.class);
                    intent.putExtra("pdfurl",arrayListPdf.get(getAdapterPosition()).getPdf_url());
                    context.startActivity(intent);

                }
            });
            img_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String str = arrayListPdf.get(getAdapterPosition()).getPdf_url();
                    int index = str.lastIndexOf('/') + 1;
                    String str1 = str.substring(str.lastIndexOf("/") + 1).replaceAll("\\s+", "");
                    ;
                    Log.d("STRIIII111", str1);

                    try {
                        DeletePdfAPI(conference_id, str1, getAdapterPosition());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            });
        }
    }

    @Override
    public PdfDelConf.TicketsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(rowLayout, parent, false);
        return new TicketsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PdfDelConf.TicketsViewHolder holder, int position) {

        if (arrayList.get(position).equals("")) {
            Log.d("kdkdkd3333","111111");
            holder.lnr_pdf.setVisibility(View.GONE);
        } else {
            Log.d("kdkdk4444","22222");

        }

        holder.tv_imagname.setText(arrayListPdf.get(position).getPdf_url().replace(AppConstantClass.HOST4+"webroot/files/brochures/", ""));



    }


    @Override
    public int getItemCount() {
        return arrayListPdf.size();
    }


    public void removeAt(int position) {
        arrayList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, arrayListPdf.size());
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
            }, 3000);
        }
    }

    private void DeletePdfAPI(String s, String brochurename, final int pos) throws JSONException {


        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(context, AppCustomPreferenceClass.UserId, ""));
        params.put("conference_id", s);
        params.put("broushers_name", brochurename);


        header.put("Cynapse", params);
        new DeletePdfApi(context, header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);

                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    Log.d("RESPONSEPDFAPI", response.toString());
                    Log.d("POSTIONN", pos + "");
                    if (res_code.equals("1")) {



                        try
                        {
                          handler.deleteData(arrayListPdf.get(pos).getPdf_id(), DatabaseHelper.TABLE_DELETE_PDF, DatabaseHelper.pdf_id);
                          arrayListPdf.remove(pos);
                          handler.deleteTableName(DatabaseHelper.TABLE_DELETE_PDF);
                          PdfDelModel pdfDelModel=new PdfDelModel();
                          for(int i=0;i<arrayListPdf.size();i++)
                          {
                              pdfDelModel.setPdf_id(arrayListPdf.get(i).getPdf_id());
                              pdfDelModel.setPdf_url(arrayListPdf.get(i).getPdf_url());
                              pdfDelModel.setConference_d(conference_id);
                              handler.AddPdfDelModel(pdfDelModel,true);

                          }


//                notifyDataSetChanged();
                            notifyItemRemoved(pos);
                        }
                        catch (IndexOutOfBoundsException iob)
                        {
                            iob.printStackTrace();
                        }


                    } else {
                        // MyToast.toastLong(context, res_msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void errorApi(VolleyError error) {
                super.errorApi(error);
            }
        };
    }
}
