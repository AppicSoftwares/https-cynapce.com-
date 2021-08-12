package com.alcanzar.cynapse.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.api.SaveResumeApi;
import com.alcanzar.cynapse.utils.AppConstantClass;
import com.alcanzar.cynapse.utils.AppCustomPreferenceClass;
import com.alcanzar.cynapse.utils.FilePath;
import com.alcanzar.cynapse.utils.MyToast;
import com.alcanzar.cynapse.utils.PostImage;
import com.alcanzar.cynapse.utils.Util;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import static com.alcanzar.cynapse.utils.FilePath.isDownloadsDocument;
import static com.alcanzar.cynapse.utils.Util.getDriveFilePath;

public class ResumeUpLoad extends AppCompatActivity implements View.OnClickListener {

    Button btnuploadresume;
    private Uri fileUri;
    private String pdfPath;
    TextView resumeName, title;
    ImageView titleIcon;
    private String url = "", pdf_name = "";
    private static final int PICK_PDF_REQUEST = 11;
    ImageView backbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume_up_load);

        btnuploadresume = findViewById(R.id.uploadresume);
        btnuploadresume.setOnClickListener(this);
        resumeName = findViewById(R.id.resumename);
        backbtn = findViewById(R.id.btnBack);
        title = findViewById(R.id.title);
        titleIcon = findViewById(R.id.titleIcon);
        backbtn.setOnClickListener(this);
        titleIcon.setVisibility(View.INVISIBLE);
        title.setText("My Resume");
        title.setTextColor(Color.WHITE);

        String dataresume = AppCustomPreferenceClass.readString(ResumeUpLoad.this, "pdf_name_code", "");

        if (!dataresume.equalsIgnoreCase("")) {
            resumeName.setText(dataresume);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.uploadresume:

                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    //TODO: enters here in case Permission is not granted
                    Log.d("entered", "here0");
                    //TODO: showing an explanation to user
                    if (ActivityCompat.shouldShowRequestPermissionRationale(ResumeUpLoad.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        MyToast.toastLong(ResumeUpLoad.this, "Application needs storage permission to upload resume");
                        Log.d("entered", "here1");
                    } else {
                        // No explanation needed; request the permission
                        ActivityCompat.requestPermissions(ResumeUpLoad.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                PICK_PDF_REQUEST);
                        Log.d("entered", "here2");
                        // PICK_PDF_REQUEST is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
                } else {
                    Log.d("entered", "here3");
                    openPdfIntent();
//                    //TODO : executed if the permission have already been granted
//                    //TODO : calling the upload resume method here
//                    if(uploadResume.getText().toString().equals("Upload Resume")){
//                        openPdfIntent();
//                    }
//                    else {
//                        //viewPdf();
//                    }
                }
                break;

            case R.id.btnBack:
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                break;
        }
    }

//    private void openPdfIntent() {
////        //TODO: calling the intent to open pdf
////
////        Intent intent = new Intent();
////        intent.addCategory(Intent.CATEGORY_OPENABLE);
////        intent.setType("application/pdf");
////        intent.setAction(Intent.ACTION_GET_CONTENT);
////        startActivityForResult(Intent.createChooser(intent, "Select Pdf"), PICK_PDF_REQUEST);
//////        Intent intent = new Intent().setType("application/pdf").setAction(Intent.ACTION_GET_CONTENT);
//////        startActivityForResult(Intent.createChooser(intent,"Select Pdf"),PICK_PDF_REQUEST);
////    }

    private void openPdfIntent() {
        //TODO: calling the intent to open pdf

        String[] mimeTypes =
                {"application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
                        "application/vnd.ms-powerpoint","application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx
//                        "application/vnd.ms-excel","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xls & .xlsx
//                        "text/plain",
                        "application/pdf",
                        "application/rtf"
                };


        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        //intent.addCategory(Intent.CATEGORY_OPENABLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //intent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
            intent.setType("*/*");
            if (mimeTypes.length > 0) {
                //intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            }
        } else {
            String mimeTypesStr = "";
            for (String mimeType : mimeTypes) {
                mimeTypesStr += mimeType + "|";
            }
            intent.setType(mimeTypesStr.substring(0, mimeTypesStr.length() - 1));
        }
        startActivityForResult(Intent.createChooser(intent, "Select Pdf"), PICK_PDF_REQUEST);
//        Intent intent = new Intent().setType("application/pdf").setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent,"Select Pdf"),PICK_PDF_REQUEST);
    }

    private void openFile(Uri uri,String name) {

        //Uri uri = Uri.fromFile(file);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, MimeTypeMap.getSingleton().getExtensionFromMimeType(name));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(intent, "Open " + name + " with ..."));
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("data12", String.valueOf(data));
        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            fileUri = data.getData();

            Log.d("PickedPdfPath: ", fileUri.toString());
            //TODO: Calling the upload here
            if (fileUri != null) {
                try {
                    getPdfPath();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("asdvasdvb", fileUri.toString());
            }
//            Log.e("asdvasdvb",FilePath.getPath(getActivity(), fileUri);)
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void getPdfPath() {
        //TODO: getting the pdf path and pdfFile name

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O_MR1) {
            if (Util.isGoogleDriveUri(fileUri)) {
                //pdfPath = fileUri.getLastPathSegment();
                pdfPath = getDriveFilePath(fileUri,ResumeUpLoad.this);
                Log.d("pdfStringPath__", "<><<" + pdfPath);
            } else {
                if (isDownloadsDocument(fileUri))
                {
                    pdfPath = Util.getRealPathFromURI(fileUri, ResumeUpLoad.this);
                    Log.d("pdfStringPath___+", "<><<" + pdfPath);
                }
                else {
                    pdfPath = fileUri.getPath();
                    Log.d("pdfStringPath___", "<><<" + pdfPath);
                }

            }
        } else {
            pdfPath = FilePath.getPath(ResumeUpLoad.this, fileUri);
        }

        Log.d("pdfStringPath :", "<><<" + pdfPath);

        try {
            pdfName = pdfPath.substring(pdfPath.lastIndexOf("/") + 1);
            Log.d("pdfName :", pdfName);

            //openFile(fileUri,pdfName);
//            resumeName.setText(pdfName);
            //AppCustomPreferenceClass.writeString(ResumeUpLoad.this,"pdf_name",pdfName);
            //AppCustomPreferenceClass.writeString(ResumeUpLoad.this,AppCustomPreferenceClass.resumePath,pdfPath);
            //TODO: calling the pdf upload here
            url = AppConstantClass.HOST + "fileUpload/resume";
            uploadFile(new File(pdfPath), url, pdfName, "file");
        } catch (NullPointerException ne) {
            //MyToast.toastLong(getActivity(), "You need to upload files which are stored locally!");
            ne.printStackTrace();
        }

    }


    //TODO: used to upload the pdf to the server
    void uploadFile(File file, final String url, String name, String type) {
        Log.e("file_name", ":" + file);
        PostImage post = new PostImage(file, url, name, ResumeUpLoad.this, type) {
            //            {
//                "Cynapse": {
//                "res_code": "1",
//                        "res_msg": "File Uploaded Successfuly.",
//                        "sync_time": 1521888468,
//                        "file_name": "0706444001521888468.pdf"
//            }
//            }
            @Override
            public void receiveData(String response) {
                Log.d("response1", response.toString());
                try {
                    JSONObject response1 = new JSONObject(response);
                    JSONObject data = response1.getJSONObject("Cynapse");
                    MyToast.logMsg("jsonImage", data.toString());
                    String res = data.getString("res_code");
                    String res1 = data.getString("res_msg");
                    //pdf_name = data.getString("file_name");

                    Log.e("pdfName", pdf_name);
                    Log.d("resmsg", data.toString());

                    if (res.equals("1")) {
                        //MyToast.toastLong(ResumeUpLoad.this, "Your file have been uploaded successfully");
                        AppCustomPreferenceClass.writeString(ResumeUpLoad.this, "pdf_name_code", data.getString("file_name"));
                        AppCustomPreferenceClass.writeString(ResumeUpLoad.this, "pdf_name", pdfName);
                        //AppCustomPreferenceClass.writeString(ResumeUpLoad.this,AppCustomPreferenceClass.resumePath,pdfPath);
                        postSaveResumeApi();
                    } else {
                        MyToast.toastLong(ResumeUpLoad.this, res1);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void receiveError() {
                Log.e("PROFILE", "ERROR");
            }
        };

        post.execute(url, null, null);
    }

    private void postSaveResumeApi() throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(ResumeUpLoad.this, AppCustomPreferenceClass.UserId, ""));
        params.put("upload_resume", AppCustomPreferenceClass.readString(ResumeUpLoad.this, "pdf_name_code", ""));
        params.put("resume_name", AppCustomPreferenceClass.readString(ResumeUpLoad.this, "pdf_name", ""));

        header.put("Cynapse", params);
        new SaveResumeApi(ResumeUpLoad.this, header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    String sync_time = header.getString("sync_time");


                    if (res_code.equals("1")) {

                        JSONObject userdata = header.getJSONObject("User");

                        String resume_code = userdata.getString("resume");
                        String resume_name = userdata.getString("resume_name");
                        resumeName.setText(resume_code);
                        AppCustomPreferenceClass.writeString(ResumeUpLoad.this, "pdf_name_code", resume_code);
                        AppCustomPreferenceClass.writeString(ResumeUpLoad.this, "pdf_name", resume_name);

                        Log.d("applied", response.toString());
                        Log.d("resumecode", resume_code + "" + "" + resume_name);
//                        JsonObject jsonObject = new JsonObject();
                        MyToast.toastLong(ResumeUpLoad.this, res_msg);

                    } else {

                        MyToast.toastLong(ResumeUpLoad.this, res_msg);

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

    private String pdfName = "";
}
