package com.alcanzar.cynapse.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.activity.CheckOutActivity;
import com.alcanzar.cynapse.activity.MainActivity;
import com.alcanzar.cynapse.activity.MyProfileActivity;
import com.alcanzar.cynapse.activity.OtpActivity;
import com.alcanzar.cynapse.activity.Sell_Buy_Practice_Activity;
import com.alcanzar.cynapse.activity.TieUpsActivity;
import com.alcanzar.cynapse.adapter.Adapter_Filter;
import com.alcanzar.cynapse.api.ChkProfileApi;
import com.alcanzar.cynapse.api.GetCityApi;
import com.alcanzar.cynapse.model.CityModel;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static com.alcanzar.cynapse.utils.FilePath.isMediaDocument;


public class Util {

    public static String imagePath = "";
    public static File imageFile = null;
    public static File videoFile = null;
    Context context;

    public static DecimalFormat form = new DecimalFormat("0.0");

    public static String decimalNumberRound(float decimalNumber) {

        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        System.out.println(df.format(decimalNumber));
        return df.format(decimalNumber);
    }

//    public static void SaveImage(Context context, Bitmap finalBitmap, String folderName) {
//
//        String root = Environment.getExternalStorageDirectory().toString();
//        File myDir = new File(root + "/" + folderName);
//        if (!myDir.isDirectory())
//            myDir.mkdirs();
//
//        String fname = generateUniqueFileName("image") + ".jpg";
//        File file = new File(myDir, fname);
//        if (file.exists()) file.delete();
//        try {
//            FileOutputStream out = new FileOutputStream(file);
//            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
//            out.flush();
//            out.close();
//            imageFile = file;
//            scanMedia(file.getAbsolutePath(), context);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    // }

    public static String generateUniqueFileName(String fileName) {
        String filename = fileName;
        long millis = System.currentTimeMillis();
        filename = filename + millis;
        return filename;
    }

    static void scanMedia(String path, Context context) {
        MediaScannerConnection.scanFile(context,
                new String[]{path}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        Log.i("ExternalStorage", "Scanned " + path + ":");
                        Log.i("ExternalStorage", "-> uri=" + uri);
                        imagePath = path;

                    }
                });
    }

    public static boolean isEmpty(EditText etText) {

        return etText.getText().toString().trim().equals("");
    }


    public static Bitmap mutableBitmap(Bitmap bmp) {
        bmp = bmp.copy(bmp.getConfig(), true);
        return bmp;
    }

    public static Bitmap takeShot(View bitView) {
        Bitmap bm;
        bitView.setDrawingCacheEnabled(true);
        bitView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        bm = Bitmap.createBitmap(bitView.getDrawingCache());
        bitView.setDrawingCacheEnabled(false);
        return bm;
    }


    public static String getDateNew(long timeStamp) {

//        try{
//            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
//            Date netDate = (new Date(timeStamp));
//            return sdf.format(netDate);
//        }
//        catch(Exception ex){
//            return "xx";
//        }
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(timeStamp);
        Date d = c.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm a");
        return sdf.format(d);
    }

    public static String getDateNew_(long timeStamp) {

//        try{
//            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
//            Date netDate = (new Date(timeStamp));
//            return sdf.format(netDate);
//        }
//        catch(Exception ex){
//            return "xx";
//        }
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(timeStamp);
        Date d = c.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        return sdf.format(d);
    }

    public static String getTimeNew(long timeStamp) {

//        try{
//            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
//            Date netDate = (new Date(timeStamp));
//            return sdf.format(netDate);
//        }
//        catch(Exception ex){
//            return "xx";
//        }
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(timeStamp);
        Date d = c.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
        return sdf.format(d);
    }

    public static void openCamera(Context mContext, int requestCode, String type) {

        try {

            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions((Activity) mContext,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},
                        requestCode);
                return;
            }

        imageFile = createImageFile(type);
        Intent cameraIntent;

        cameraIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            cameraIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(mContext, "com.alcanzar.cynapse.provider", imageFile);
            Log.e("ImageFile", "ImageFile== " + imageFile + "== " + contentUri);
//                cameraIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
        } else {
            // cameraIntent.setDataAndType(Uri.fromFile(imageFile), type);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
        }


        ((Activity) mContext).startActivityForResult(cameraIntent, requestCode);

        } catch (IOException e) {
            Log.e("Exception--", "" + e);
            e.printStackTrace();
        }
        catch (SecurityException e) {
            Log.e("Exception-", "" + e);
            e.printStackTrace();
        }
    }

    @SuppressLint("SimpleDateFormat")
    public static File createImageFile(String type) throws IOException {
        String getRoot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
        File myDir = new File(getRoot + "/temp/");
        myDir.mkdirs();
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmm")
                .format(new Date());
        String imageFileName = "FX" + timeStamp;

        File image = null;
        if (type.equalsIgnoreCase("image")) {
            image = File.createTempFile(imageFileName, ".jpg", myDir);
            MyToast.logMsg("Check FOr Video", "On Create Img" + image);
        } else {
            image = File.createTempFile(imageFileName, ".mp4", myDir);
//            image = new File(imageFileName + File.separator
//                    + "VID_" + timeStamp + ".mp4");
            MyToast.logMsg("Check FOr Video", "On Create Video" + image);
        }

        return image;
    }


    public static void openGallery(Context mContext, int requestCode, String type) {

        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions((Activity) mContext,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},
                    requestCode);
            return;
        }

        Intent intent = new Intent();
        if (type.equalsIgnoreCase("image")) {
            intent.setType("image/*");
        } else {
            intent.setType("video/*");
        }

        intent.setAction(Intent.ACTION_PICK);
        ((Activity) mContext).startActivityForResult(intent, requestCode);
    }

    public static boolean isStorageAvailable(Context con) {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            Toast.makeText(con, "sd card is not writeable", Toast.LENGTH_SHORT)
                    .show();
        } else {
            Toast.makeText(con, "SD is not available..!!", Toast.LENGTH_SHORT).show();
        }
        return false;
    }


    public static Bitmap customDecodeFile(String path, int reqW, int reqH) {
        final BitmapFactory.Options ops = new BitmapFactory.Options();
        ops.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, ops);
        ops.inSampleSize = calculateInSampleSize(ops, reqW, reqH);
        ops.inJustDecodeBounds = false;
        Bitmap bmp = BitmapFactory.decodeFile(path, ops);

        return bmp;
    }

    public static String getRealPathFromURI(Uri contentUri, Activity activity) {
        String path = null;
        String[] proj = {MediaStore.MediaColumns.DATA};
        Cursor cursor = activity.getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            path = cursor.getString(column_index);
        }
        cursor.close();
        return path;
    }


    public static boolean isGoogleDriveUri(Uri uri) {
        return "com.google.android.apps.docs.storage".equals(uri.getAuthority()) || "com.google.android.apps.docs.storage.legacy".equals(uri.getAuthority());
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            inSampleSize = heightRatio <= widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }


    public static void shareImage(File file, Context act) {

        Intent share = new Intent(Intent.ACTION_SEND);
        share.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        share.setType("image/jpeg");
        share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        act.startActivity(Intent.createChooser(share, "Share Image"));
    }


    public static void initShareIntent(String type, Context con, File imaFile) {
        if (isNetConnected(con)) {
            boolean found = false;
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("image/jpeg");

            // gets the list of intents that can be loaded.
            List<ResolveInfo> resInfo = con.getPackageManager()
                    .queryIntentActivities(share, 0);
            if (!resInfo.isEmpty()) {
                for (ResolveInfo info : resInfo) {
                    if (info.activityInfo.packageName.toLowerCase().contains(
                            type)
                            || info.activityInfo.name.toLowerCase().contains(
                            type)) {
                        share.putExtra(Intent.EXTRA_SUBJECT, "Awesome App..");
                        share.putExtra(
                                Intent.EXTRA_TEXT,
                                "Get It on GooglePlay..\n"
                                        + " https://play.google.com/store/apps/details?id="
                                        + con.getPackageName());
                        share.putExtra(Intent.EXTRA_STREAM,
                                Uri.fromFile(imaFile));
                        share.setPackage(info.activityInfo.packageName);
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    Toast.makeText(con, "App was not installed.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                con.startActivity(Intent.createChooser(share, "Select"));
            }
        }
    }


    public static boolean isNetConnected(Context ctx) {
        ConnectivityManager cm = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            Toast.makeText(ctx, "Enable Internet Connection..",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public static void moreapps(Context ctx, String publishername) {
        if (isNetConnected(ctx)) {
            Intent pubPage = null;
            pubPage = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://search?q=pub:" + publishername));
            pubPage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ctx.startActivity(pubPage);
        }
    }

//    public static void rate(Context ctx) {
//        if (isNetConnected(ctx)) {
//            ctx.startActivity(new Intent(Intent.ACTION_VIEW, Uri
//                    .parse("market://details?id=" + ctx.getPackageName()))
//                    .addFlags(268435456));
//        }
//    }


    public static Bitmap getImageThumbnail(Bitmap bmp, int width, int height) {
        return ThumbnailUtils.extractThumbnail(bmp, width, height);
    }

    public static boolean saveImage(Bitmap save_bitmap, Context act,
                                    String rootDir) {
        if (isStorageAvailable(act)) {

            String root = Environment.getExternalStorageDirectory().toString();
            File rootFile = new File(root, rootDir);
            if (!rootFile.exists()) {
                rootFile.mkdirs();
            }
            String fname = generateUniqueName("pic") + ".jpg";
            imageFile = new File(rootFile, fname);
            if (imageFile.exists()) {
                imageFile.delete();
            }
            FileOutputStream f = null;
            try {
                f = new FileOutputStream(imageFile);
                save_bitmap.compress(Bitmap.CompressFormat.PNG, 90, f);
                f.flush();
                f.close();
                galleryAddPic(imageFile, act);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    public static void galleryAddPic(File file, final Context context) {
        MediaScannerConnection.scanFile(context, new String[]{file.toString()}, (String[]) null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, final Uri uri) {

                    }
                });
    }

    @SuppressLint("SimpleDateFormat")
    private static String generateUniqueName(String filename) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        filename = filename + timeStamp;
        return filename;
    }

    public static String getRealFilePath(final Context context, final Uri uri) {

        if (null == uri)
            return null;

        final String scheme = uri.getScheme();
        String data = null;

        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri,
                    new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }


    public static Matrix ratio(Bitmap bitmap, float width, float height) {
        float oldWidth = bitmap.getWidth();
        float oldHeight = bitmap.getHeight();
        float ratio;
        float bitratio;
        Matrix m = new Matrix();
        if (oldWidth >= oldHeight)
            bitratio = oldHeight;
        else
            bitratio = oldWidth;
        if (width >= height) {
            ratio = (float) width / bitratio;

        } else {
            ratio = (float) height / bitratio;
        }
        m.postScale(ratio, ratio);
        return m;
    }

//    public static void hidekeyboard(Context context) {
//        ((Activity) context).getWindow().setSoftInputMode(
//                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
//        );
//    }


    public static void hidekeyboard(Context ctx) {
        InputMethodManager inputManager = (InputMethodManager) ctx
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View v = ((Activity) ctx).getCurrentFocus();
        if (v == null)
            return;

        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    //    public void showSoftKeyboard(View view) {
//        if (view.requestFocus()) {
//            InputMethodManager imm = (InputMethodManager)
//                    getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
//        }
//    }
    public boolean getIsInternetConnectionContext(Context context) {
//    Log.d("==getIsInternetConnectionContext", "getIsInternetConnectionContext");
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
        }
        return false;
    }


    public static int Orientation(File f) {
        int angle = 0;
        try {
            ExifInterface exif = new ExifInterface(f.getPath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                angle = 90;
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                angle = 180;
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                angle = 270;
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        return angle;
    }


    public static String getDateCurrentTimeZone(long timestamp) {
        try {
            Calendar calendar = Calendar.getInstance();
            TimeZone tz = TimeZone.getDefault();
            calendar.setTimeInMillis(timestamp * 1000);
            calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
//        HH:mm:ss
            Date currenTimeZone = (Date) calendar.getTime();
            return sdf.format(currenTimeZone);
        } catch (Exception e) {
        }
        return "";
    }

//    public static void share(Context ctx) {
//
//        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
//        sharingIntent.setType("text/plain");
//        sharingIntent.putExtra(Intent.EXTRA_SUBJECT,
//                R.string.app_name);
//        sharingIntent.putExtra(
//                Intent.EXTRA_TEXT,
//                "https://play.google.com/store/apps/details?id="
//                        + ctx.getPackageName()+"\nReferal code:- "+ CustomPreference.readString(ctx,CustomPreference.ReferalNo,""));
//
//        Intent i = Intent.createChooser(sharingIntent, "Share using");
//        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        ctx.startActivity(i);
//
//    }

    public static boolean isVerifiedProfile(Activity activity) {
        boolean b = false;
        if (AppCustomPreferenceClass.readString(activity, AppCustomPreferenceClass.name, "").equalsIgnoreCase("")) {
            b = false;
            showDialog(activity);
        } else if (AppCustomPreferenceClass.readString(activity, AppCustomPreferenceClass.medical_profile_id, "").equalsIgnoreCase("")) {
            b = false;
            showDialog(activity);
        } else if (!validate2(activity)) {
            b = false;
            showDialog(activity);
        } else if (AppCustomPreferenceClass.readString(activity, AppCustomPreferenceClass.title_id, "").equalsIgnoreCase("")) {
            b = false;
            showDialog(activity);
        } else if (AppCustomPreferenceClass.readString(activity, AppCustomPreferenceClass.Country_id, "").equalsIgnoreCase("")) {
            b = false;
            showDialog(activity);
        } else if (AppCustomPreferenceClass.readString(activity, AppCustomPreferenceClass.state_id, "").equalsIgnoreCase("")) {
            b = false;
            showDialog(activity);
        } else if (AppCustomPreferenceClass.readString(activity, AppCustomPreferenceClass.city_id, "").equalsIgnoreCase("")) {
            b = false;
            showDialog(activity);
        } else if (!validate3(activity)) {
            b = false;
            showDialog(activity);
        } else {
            b = true;
        }
        return b;
    }

    private static boolean validate2(Activity activity) {
        boolean b = false;

        if (AppCustomPreferenceClass.readString(activity, AppCustomPreferenceClass.department_id, "").equalsIgnoreCase("")) {
            if (AppCustomPreferenceClass.readString(activity, AppCustomPreferenceClass.sub_specialization_id, "").equalsIgnoreCase("") &&
                    AppCustomPreferenceClass.readString(activity, AppCustomPreferenceClass.specilization_id, "").equalsIgnoreCase("")) {
                b = false;
            } else {
                b = true;
            }
        } else {
            b = true;
        }

        return b;
    }


    private static boolean validate3(Activity activity) {
        boolean b = false;

        if (AppCustomPreferenceClass.readString(activity, AppCustomPreferenceClass.highest_degree_id, "").equalsIgnoreCase("")) {
            if (AppCustomPreferenceClass.readString(activity, AppCustomPreferenceClass.year_of_study, "").equalsIgnoreCase("")) {
                b = false;
            } else {
                b = true;
            }
        } else {
            b = true;
        }

        return b;
    }

    private static void callMyProfileActivity(Activity activity) {
        Intent intent = new Intent(activity, MyProfileActivity.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        //activity.finish();
        //MyToast.toastLong(activity,"Please Fill");
    }

    public static void showDialog(final Activity activity) {

        Button yes;
        ImageView close;

        final Dialog dialoglog = new Dialog(activity);
        dialoglog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialoglog.setContentView(R.layout.alert_dialog_profile);
        Window window = dialoglog.getWindow();
        window.setLayout(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
        dialoglog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        yes = dialoglog.findViewById(R.id.btnGotIt);
        close = dialoglog.findViewById(R.id.close);

        dialoglog.show();

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callMyProfileActivity(activity);
                dialoglog.dismiss();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialoglog.dismiss();
            }
        });
    }

    public static boolean isVerifiyEMailPHoneNO(final Activity activity) throws JSONException {
        JSONObject header = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("uuid", AppCustomPreferenceClass.readString(activity, AppCustomPreferenceClass.UserId, ""));
        header.put("Cynapse", params);
        Log.d("CITYHEADER", params.toString());
        new ChkProfileApi(activity, header) {
            @Override
            public void responseApi(JSONObject response) {
                super.responseApi(response);
                try {
                    JSONObject header = response.getJSONObject("Cynapse");
                    String res_msg = header.getString("res_msg");
                    String res_code = header.getString("res_code");
                    Log.d("CITYRESPONSE", response.toString());

                    if (res_code.equals("1")) {
                        JSONObject child = header.getJSONObject("AccountMaster");
                        {

                            if (child.optString("email_verified").equalsIgnoreCase("") ||
                                    child.optString("email_verified").equalsIgnoreCase("0")) {
                                bol = false;
                                Toast.makeText(activity, "To continue please verify your Email", Toast.LENGTH_LONG).show();
                            } else {
                                AppCustomPreferenceClass.writeString(activity, AppCustomPreferenceClass.email_verified, child.optString("email_verified"));
                                bol = true;
                            }

                            if (bol) {
                                if (child.optString("phone_number").equalsIgnoreCase("")) {
                                    bol = false;
                                    Toast.makeText(activity, "To continue please verify your Mobile number", Toast.LENGTH_LONG).show();
                                } else {
                                    AppCustomPreferenceClass.writeString(activity, AppCustomPreferenceClass.phoneNumber, child.optString("phone_number"));
                                    bol = true;
                                }
                            }
                        }
                    } else {

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

        return bol;
    }

    public static String getDriveFilePath(Uri uri, Context context) {
        Uri returnUri = uri;
        Cursor returnCursor = context.getContentResolver().query(returnUri, null, null, null, null);
        /*
         * Get the column indexes of the data in the Cursor,
         *     * move to the first row in the Cursor, get the data,
         *     * and display it.
         * */
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
        returnCursor.moveToFirst();

        String name = (returnCursor.getString(nameIndex));
        String size = (Long.toString(returnCursor.getLong(sizeIndex)));
        File file = new File(context.getCacheDir(), name);
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            FileOutputStream outputStream = new FileOutputStream(file);
            int read = 0;
            int maxBufferSize = 1 * 1024 * 1024;
            int bytesAvailable = inputStream.available();

            //int bufferSize = 1024;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);

            final byte[] buffers = new byte[bufferSize];
            while ((read = inputStream.read(buffers)) != -1) {
                outputStream.write(buffers, 0, read);
            }
            Log.e("File Size", "Size " + file.length());
            inputStream.close();
            outputStream.close();
            Log.e("File Path", "Path " + file.getPath());
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }
        return file.getPath();
    }


    private static boolean bol = false;

}