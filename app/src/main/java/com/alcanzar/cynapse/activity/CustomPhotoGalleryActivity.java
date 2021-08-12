package com.alcanzar.cynapse.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alcanzar.cynapse.R;

import java.util.prefs.Preferences;


public class CustomPhotoGalleryActivity extends Activity {

    private GridView grdImages;
    private Button btnSelect;

    private ImageAdapter imageAdapter;
    private String[] arrPath;
    private boolean[] thumbnailsselection;
    private int ids[];
    private int count;
    String action;
    Preferences pref;
    RelativeLayout rl_top_header;
    ImageView imgNoMedia;
    private static final int PICK_IMAGE_REQUEST = 12;

    /**
     * Overrides methods
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_gallery);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        grdImages = (GridView) findViewById(R.id.grdImages);
        btnSelect = (Button) findViewById(R.id.btnSelect);
        if (ActivityCompat.checkSelfPermission(CustomPhotoGalleryActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&

                ActivityCompat.checkSelfPermission(CustomPhotoGalleryActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(CustomPhotoGalleryActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                    PICK_IMAGE_REQUEST);
        }

//        pref = new Preferences(this);
//
//        ImageView image = (ImageView) findViewById(R.id.image);
//
//        int width = getResources().getDisplayMetrics().widthPixels;
//        // int height = getResources().getDisplayMetrics().heightPixels;
//        RelativeLayout.LayoutParams param = (RelativeLayout.LayoutParams) image.getLayoutParams();
//        param.width = Math.round(width * 0.60f);
//
//        if (!pref.get(Constants.logo_name).equals("null"))
//            Glide.with(this).load(utils.getAPPURL(this) + "/images/enterprises/" + pref.get(Constants.logo_name))
//                    .placeholder(R.drawable.logo_loginscreen).error(R.drawable.logo_loginscreen).fitCenter()
//                    .into(image);
//        else
//            Glide.with(this).load(utils.getAPPURL(this) + "/images/logo_loginScreen.png")
//                    .placeholder(R.drawable.logo_loginscreen).error(R.drawable.logo_loginscreen).fitCenter()
//                    .into(image);



//        rl_top_header = (RelativeLayout) findViewById(R.id.rl_top_header);
//
//        rl_top_header.setBackgroundColor(Color.rgb(Integer.parseInt(pref.get(Constants.top_red)),
//                Integer.parseInt(pref.get(Constants.top_green)), Integer.parseInt(pref.get(Constants.top_blue))));

       // action = getIntent().getAction();
//        if (action == null) {
//            finish();
//        }
        init();

        btnSelect.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                try {


                final int len = thumbnailsselection.length;
                int cnt = 0;
                String selectImages = "";
                for (int i = 0; i < len; i++) {
                    if (thumbnailsselection[i]) {
                        cnt++;
                        selectImages = selectImages + arrPath[i] + "|";
                        Log.d("SLECTIMAGES",selectImages+"");
                    }
                }

                    Intent i = new Intent();
                    i.putExtra("data", selectImages);
                    setResult(Activity.RESULT_OK, i);
                    finish();


//                if (cnt == 0) {
//                    Toast.makeText(getApplicationContext(), "Please select at least one image", Toast.LENGTH_SHORT).show();
//                } else if (cnt > 4) {
//                    Toast.makeText(getApplicationContext(), "Please select only 4 images.", Toast.LENGTH_SHORT).show();
//                } else {
//                    Log.d("SelectedImages", selectImages);
//                    Intent i = new Intent();
//                    i.putExtra("data", selectImages);
//                    setResult(Activity.RESULT_OK, i);
//                    finish();
//                }
            }catch (Exception e){Toast.makeText(getApplicationContext(), "No images found", Toast.LENGTH_SHORT).show();}
            }
        });
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_CANCELED);
        super.onBackPressed();

    }

    void init() {

        int MyVersion = Build.VERSION.SDK_INT;
        Log.d(">>>>",MyVersion+"=="+ Build.VERSION_CODES.LOLLIPOP_MR1);
        if (MyVersion >= Build.VERSION_CODES.N) {

            requestForSpecificPermission();
            if (!checkIfAlreadyhavePermission()) {
                requestForSpecificPermission();
            }

        }
        else
        {
            Log.d("===granted", "granted");
            final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};
            final String orderBy = MediaStore.Images.Media._ID;
            @SuppressWarnings("deprecation")
            Cursor imagecursor = managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy);
            int image_column_index = imagecursor.getColumnIndex(MediaStore.Images.Media._ID);
            this.count = imagecursor.getCount();
            this.arrPath = new String[this.count];
            ids = new int[count];
            this.thumbnailsselection = new boolean[this.count];
            for (int i = 0; i < this.count; i++) {
                imagecursor.moveToPosition(i);
                ids[i] = imagecursor.getInt(image_column_index);
                int dataColumnIndex = imagecursor.getColumnIndex(MediaStore.Images.Media.DATA);
                arrPath[i] = imagecursor.getString(dataColumnIndex);
            }

            imageAdapter = new ImageAdapter();
            grdImages.setAdapter(imageAdapter);
            imagecursor.close();

        }
    }

//    private void checkImageStatus() {
//        if (imageAdapter.isEmpty()) {
//            imgNoMedia.setVisibility(View.VISIBLE);
//        } else {
//            imgNoMedia.setVisibility(View.GONE);
//        }
//    }


    private boolean checkIfAlreadyhavePermission() {

        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {

            return false;
        }
    }

    private void requestForSpecificPermission() {

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 101:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //granted
                    Log.d("===granted", "granted");
                    final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};
                    final String orderBy = MediaStore.Images.Media._ID;
                    @SuppressWarnings("deprecation")
                    Cursor imagecursor = managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy);
                    int image_column_index = imagecursor.getColumnIndex(MediaStore.Images.Media._ID);
                    this.count = imagecursor.getCount();
                    this.arrPath = new String[this.count];
                    ids = new int[count];
                    this.thumbnailsselection = new boolean[this.count];
                    for (int i = 0; i < this.count; i++) {
                        imagecursor.moveToPosition(i);
                        ids[i] = imagecursor.getInt(image_column_index);
                        int dataColumnIndex = imagecursor.getColumnIndex(MediaStore.Images.Media.DATA);
                        arrPath[i] = imagecursor.getString(dataColumnIndex);
                    }

                    imageAdapter = new ImageAdapter();
                    grdImages.setAdapter(imageAdapter);
                    imagecursor.close();


                } else {
                    //not granted
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    /**
     * Class method
     */

    /**
     * This method used to set bitmap.
     *
     * @param iv represented ImageView
     * @param id represented id
     */

    private void setBitmap(final ImageView iv, final int id) {

        new AsyncTask<Void, Void, Bitmap>() {

            @Override
            protected Bitmap doInBackground(Void... params) {
                return MediaStore.Images.Thumbnails.getThumbnail(getApplicationContext().getContentResolver(), id, MediaStore.Images.Thumbnails.MINI_KIND, null);
            }

            @Override
            protected void onPostExecute(Bitmap result) {
                super.onPostExecute(result);
                iv.setImageBitmap(result);
            }
        }.execute();
    }


    /**
     * List adapter
     *
     * @author tasol
     */

    public class ImageAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        public ImageAdapter() {
            mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public int getCount() {
            return count;
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            {
                final ViewHolder holder;
                if (convertView == null) {
                    holder = new ViewHolder();
                    convertView = mInflater.inflate(R.layout.custom_gallery_item, null);
                    holder.imgThumb = (ImageView) convertView.findViewById(R.id.imgThumb);
                    holder.chkImage = (CheckBox) convertView.findViewById(R.id.chkImage);

                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                holder.chkImage.setId(position);
                holder.imgThumb.setId(position);
                holder.chkImage.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v;
                        int id = cb.getId();
                        if (thumbnailsselection[id]) {
                            cb.setChecked(false);
                            thumbnailsselection[id] = false;
                        } else {
                            cb.setChecked(true);
                            thumbnailsselection[id] = true;
                        }
                    }
                });
                holder.imgThumb.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        int id = holder.chkImage.getId();
                        if (thumbnailsselection[id]) {
                            holder.chkImage.setChecked(false);
                            thumbnailsselection[id] = false;
                        } else {
                            holder.chkImage.setChecked(true);
                            thumbnailsselection[id] = true;
                        }
                    }
                });
                try {
                    setBitmap(holder.imgThumb, ids[position]);
                } catch (Throwable e) {
                }
                holder.chkImage.setChecked(thumbnailsselection[position]);
                holder.id = position;
                return convertView;
            }

        }

        /**
         * Inner class
         *
         * @author tasol
         */
        class ViewHolder {
            ImageView imgThumb;
            CheckBox chkImage;
            int id;
        }
    }







}