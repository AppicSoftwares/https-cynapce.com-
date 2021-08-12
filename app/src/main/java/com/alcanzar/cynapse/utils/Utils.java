package com.alcanzar.cynapse.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Base64;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class Utils {

    //for image portrait orientation and in circle image

    public static File imageFile;
    public static String imagePath;
    public static LinearLayout gets_file_info;
    private static String token = "";


    public static int Orientation(String filePath) {
        int angle = 0;
        try {
            ExifInterface exif = new ExifInterface(filePath);
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

    public static void getKeyHash(Activity activity) {
        try {
            PackageInfo info = activity.getPackageManager().getPackageInfo(
                    "com.alcanzar.cynapse",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

            Log.d("resss",e.getMessage());

        } catch (NoSuchAlgorithmException e) {
            Log.d("resss",e.getMessage());
        }
    }

    public Bitmap changeOrientationBitmap(String picPath, int x, int y) {
//        File f = new File(picPath);
        Matrix mat = new Matrix();
        mat.postRotate(Utils.Orientation(picPath));
        Bitmap bmp = null;
//        try {
//            bmp = BitmapFactory.decodeStream(new FileInputStream(f), null, null);
        bmp = getScaledBitmap(picPath, x, y);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
        Bitmap correctBmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), mat, true);

        Bitmap circleBitmap = Bitmap.createBitmap(correctBmp.getWidth(), correctBmp.getHeight(), Bitmap.Config.ARGB_8888);
        BitmapShader shader = new BitmapShader(correctBmp, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setShader(shader);
        Canvas c1 = new Canvas(circleBitmap);
        c1.drawCircle(correctBmp.getWidth() / 2, correctBmp.getHeight() / 2, correctBmp.getWidth() / 2, paint);


        return circleBitmap;
    }

    public static void sop(String s)
    {
        System.out.println(s);
    }

    public static Boolean isActivityRunning(Class activityClass,Activity activity)
    {
        ActivityManager activityManager = (ActivityManager) activity.getBaseContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(Integer.MAX_VALUE);

        for (ActivityManager.RunningTaskInfo task : tasks) {
            if (activityClass.getCanonicalName().equalsIgnoreCase(task.baseActivity.getClassName()))
                return true;
        }

        return false;
    }

    public static String firebaseToken(Activity activity)
    {
        FirebaseApp.initializeApp(activity);
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.e("Zsvadsv", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        token = task.getResult().getToken();

                        // Log and toast
                        Log.e("TokenCheck",token);
//                        Toast.makeText(MainActivity.this, "asdggadegads", Toast.LENGTH_SHORT).show();
                    }
                });

        return token;
    }


    public Bitmap getScaledBitmap(String picturePath, int width, int height) {
        BitmapFactory.Options sizeOptions = new BitmapFactory.Options();
        sizeOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(picturePath, sizeOptions);

        int inSampleSize = calculateInSampleSize(sizeOptions, width, height);

        sizeOptions.inJustDecodeBounds = false;
        sizeOptions.inSampleSize = inSampleSize;

        return BitmapFactory.decodeFile(picturePath, sizeOptions);
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    public static Bitmap decodeUri(Context context, Uri uri,
                                   final int requiredSize) throws FileNotFoundException {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(context.getContentResolver()
                .openInputStream(uri), null, o);

        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;

        while (true) {
            if (width_tmp / 2 < requiredSize || height_tmp / 2 < requiredSize)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(context.getContentResolver()
                .openInputStream(uri), null, o2);
    }
    public static boolean saveImage(Bitmap save_bitmap, Context act,
                                    String rootDir, String body, boolean bool) {
        if (isStorageAvailable(act)) {

            String root = Environment.getExternalStorageDirectory().toString()+"/Panzura Media";
            File rootFile = new File(root, rootDir);
            if (!rootFile.exists()) {
                rootFile.mkdirs();
            }
            String fname = generateUniqueName("pic") + ".jpg";
            imageFile = new File(rootFile, fname);

            FileOutputStream f = null;
            try {
                f = new FileOutputStream(imageFile);
                save_bitmap.compress(Bitmap.CompressFormat.PNG, 90, f);
                f.flush();
                f.close();
                imagePath = imageFile.getAbsolutePath();
                galleryAddPic(imageFile, act, body, bool);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    public static void galleryAddPic(File file, final Context context, final String body, final boolean bool) {
        MediaScannerConnection.scanFile(context, new String[]{file.toString()}, (String[]) null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, final Uri uri) {
                        imagePath = path;

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

    public static boolean isStorageAvailable(Context con) {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            Toast.makeText(con, "sd card is not writable", Toast.LENGTH_SHORT)
                    .show();
        } else {
            Toast.makeText(con, "SD is not available..!!", Toast.LENGTH_SHORT)
                    .show();
        }
        return false;
    }
}
