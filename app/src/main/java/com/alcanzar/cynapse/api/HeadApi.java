package com.alcanzar.cynapse.api;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.app.AppController;
import com.alcanzar.cynapse.utils.Util;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

public abstract class HeadApi {
    private ProgressDialog pDialog;
    public abstract void responseApi(JSONObject response);
    public abstract void errorApi(VolleyError error);
    public void postJsonApi(final Context context, final String url, final JSONObject headerObj, final String tag, final boolean bool) {
        if (Util.isNetConnected(context )) {
            if (bool) {
                pDialog = new ProgressDialog(context, R.style.DialogTheme);
                pDialog.setCancelable(false);
                pDialog.show();
                View v = LayoutInflater.from(context).inflate( R.layout.custom_progress_view, null, false);
                pDialog.setContentView(v);
            }
            final Request.Priority mPriority = Request.Priority.HIGH;
            final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    url, headerObj, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.e(tag, response.toString());
                    if (bool) {
                        try{
                            pDialog.dismiss();
                        }catch (IllegalArgumentException iae)
                        {
                            iae.printStackTrace();
                        }

                    }
                    responseApi(response);
                    Log.e(tag, "URL: " + url);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(tag, "Error: " + error.getMessage());
                    if (pDialog!=null) {
                        pDialog.dismiss();
                    }
                    if (error.getMessage() == null) {
                        Log.e(tag, "Please Try Again");
                    }
//                    else {
//                        Log.e(tag, "Error: " + error.getMessage() + " " + error.networkResponse.statusCode);
//                    }
                    errorApi(error);
                }
            }) {
                @Override
                public Priority getPriority() {
                    return mPriority;
                }
            };
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(18000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            AppController.getInstance().addToRequestQueue(jsonObjReq);
        }
    }
    void postJsonApiGet(final Context context, final String url, final String tag, final boolean bool) {
        if (Util.isNetConnected(context)) {
            if (bool) {
                pDialog = new ProgressDialog(context, R.style.DialogTheme);
                pDialog.setCancelable(false);
                pDialog.show();
                View v = LayoutInflater.from(context).inflate(R.layout.custom_progress_view, null, false);
                pDialog.setContentView(v);
            }
            final Request.Priority mPriority = Request.Priority.HIGH;
            Log.e("URL", url);
            final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, new JSONObject(), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.e(tag, response.toString());
                    if (pDialog!=null) {
                        pDialog.dismiss();
                    }
                    responseApi(response);
                    Log.e(tag, "URL: " + url);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(tag, "Error: " + error.getMessage());
                    if (pDialog!=null) {
                        pDialog.dismiss();
                    }
                    errorApi(error);
                }
            }) {
                @Override
                public Priority getPriority() {
                    return mPriority;
                }
            };
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(18000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            AppController.getInstance().addToRequestQueue(jsonObjReq);
        }
    }

}