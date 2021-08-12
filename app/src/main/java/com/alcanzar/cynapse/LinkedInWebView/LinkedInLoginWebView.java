package com.alcanzar.cynapse.LinkedInWebView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ParseException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.alcanzar.cynapse.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class LinkedInLoginWebView extends AppCompatActivity {

    /****YOUR LINKEDIN APP INFO HERE*********/
    private static final String API_KEY = "81yitgg4o00v93";
    private static final String SECRET_KEY = "NX79xiiGYu1WAiu5";
    //This is any string we want to use. This will be used for avoid CSRF attacks. You can generate one here: http://strongpasswordgenerator.com/
    private static final String STATE = "E3ZYKC1T6H2yP4z";
    private static final String REDIRECT_URI = "https://cynapce.com/homes/privacy_policy";

    private static final String SCOPES = "r_emailaddress";
    /*********************************************/

    //These are constants used for build the urls
    private static final String AUTHORIZATION_URL = "https://www.linkedin.com/uas/oauth2/authorization";
    private static final String ACCESS_TOKEN_URL = "https://www.linkedin.com/uas/oauth2/accessToken";
    private static final String SECRET_KEY_PARAM = "client_secret";
    private static final String RESPONSE_TYPE_PARAM = "response_type";
    private static final String GRANT_TYPE_PARAM = "grant_type";
    private static final String GRANT_TYPE = "authorization_code";
    private static final String RESPONSE_TYPE_VALUE = "code";
    private static final String CLIENT_ID_PARAM = "client_id";
    private static final String SCOPE_PARAM = "scope";
    private static final String STATE_PARAM = "state";
    private static final String REDIRECT_URI_PARAM = "redirect_uri";
    /*---------------------------------------*/
    private static final String QUESTION_MARK = "?";
    private static final String AMPERSAND = "&";
    private static final String EQUALS = "=";

    private WebView webView;
    private ProgressDialog pd;
    String bankUrl;
    WebSettings webSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linked_in_login_web_vieqw);

        webView = (WebView) findViewById(R.id.main_activity_web_view);
        webView.requestFocus(View.FOCUS_DOWN);

        webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);


        String authUrl = getAuthorizationUrl();
        Log.i("Authorize", "Loading Auth Url: " + authUrl);
        //Load the authorization URL into the webView
        webView.loadUrl(authUrl);


        //webView.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");

        webView.setWebViewClient(new WebViewClient() {
            /*@Override
            public boolean shouldOverrideUrlLoading(WebView webView, String url) {
                bankUrl = url;
                return false;
            }*/
            @SuppressWarnings("deprecation")
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                bankUrl = url;
                System.out.println(">>>>>>>>>>>>>>shouldOverrideUrlLoading11>>>>>>>>>>>>>>>>>>" + bankUrl);

                if (url.contains("error=user_cancelled_login")) {
                    LinkedInLoginWebView.this.finish();
                } else if (url.startsWith(REDIRECT_URI)) {
                    Log.i("Authorize", "");
                    Uri uri = Uri.parse(url);
                    //We take from the url the authorizationToken and the state token. We have to check that the state token returned by the Service is the same we sent.
                    //If not, that means the request may be a result of CSRF and must be rejected.
                    String stateToken = uri.getQueryParameter(STATE_PARAM);
                    if (stateToken == null || !stateToken.equals(STATE)) {
                        Log.e("Authorize", "State token doesn't match");
                        return true;
                    }

                    //If the user doesn't allow authorization to our application, the authorizationToken Will be null.
                    String authorizationToken = uri.getQueryParameter(RESPONSE_TYPE_VALUE);
                    if (authorizationToken == null) {
                        Log.i("Authorize", "The user doesn't allow authorization.");
                        return true;
                    }
                    Log.i("Authorize", "Auth token received: " + authorizationToken);

                    //Generate URL for requesting Access Token
                    String accessTokenUrl = getAccessTokenUrl(authorizationToken);
                    //We make the request in a AsyncTask
                    new PostRequestAsyncTask().execute(accessTokenUrl);

                } else {
                    //Default behaviour
                    Log.i("Authorize", "Redirecting to: " + url);
                    webView.loadUrl(url);
                    return true;
                }
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(webView, url);

                //webView.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");


                // calling load Waiting for otp fragment

                bankUrl = url;
                Log.d("BANKURLLL", bankUrl);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

            }
        });

    }

    private static String getAuthorizationUrl() {
        return AUTHORIZATION_URL
                + QUESTION_MARK + RESPONSE_TYPE_PARAM + EQUALS + RESPONSE_TYPE_VALUE
                + AMPERSAND + CLIENT_ID_PARAM + EQUALS + API_KEY
                + AMPERSAND + STATE_PARAM + EQUALS + STATE
                + AMPERSAND + REDIRECT_URI_PARAM + EQUALS + REDIRECT_URI
                + AMPERSAND + SCOPE_PARAM + EQUALS + SCOPES;

        //https://www.linkedin.com/oauth/v2/authorization?response_type=code&client_id=81yitgg4o00v93&redirect_uri=https://cynapce.com/homes/privacy_policy&scope=r_emailaddress
    }

    private class PostRequestAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show(LinkedInLoginWebView.this, "", "Please Wait...", true);
        }

        @Override
        protected String doInBackground(String... urls) {
            if (urls.length > 0) {

                String accessToken = "";

                String url = urls[0];
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpost = new HttpPost(url);
                try {
                    HttpResponse response = httpClient.execute(httpost);
                    if (response != null) {
                        //If status is OK 200
                        if (response.getStatusLine().getStatusCode() == 200) {
                            String result = EntityUtils.toString(response.getEntity());
                            //Convert the string result to a JSON Object
                            JSONObject resultJson = new JSONObject(result);
                            //Extract data from JSON Response
                            int expiresIn = resultJson.has("expires_in") ? resultJson.getInt("expires_in") : 0;
                            accessToken = resultJson.has("access_token") ? resultJson.getString("access_token") : null;

                            if (expiresIn > 0 && accessToken != null) {
                                Log.i("Authorize", "This is the access Token: " + accessToken + ". It will expires in " + expiresIn + " secs");

                                return accessToken;
                            }
                        }
                    }
                } catch (IOException e) {
                    Log.e("Authorize", "Error Http response " + e.getLocalizedMessage());
                } catch (ParseException e) {
                    Log.e("Authorize", "Error Parsing Http response " + e.getLocalizedMessage());
                } catch (JSONException e) {
                    Log.e("Authorize", "Error Parsing Http response " + e.getLocalizedMessage());
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String status) {
            if (pd != null && pd.isShowing()) {
                pd.dismiss();
            }

            if (status != null)
                new fetchProfile(status).execute();
        }

    }

    private class fetchProfile extends AsyncTask<String, Void, String> {

        String token = "";

        fetchProfile(String token) {
            this.token = token;
        }

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show(LinkedInLoginWebView.this, "", "Please Wait...", true);
        }

        @Override
        protected String doInBackground(String... urls) {

            StringBuffer response = null;

            try {
                // Sending get request
                URL url = new URL("https://api.linkedin.com/v2/emailAddress?q=members&projection=(elements*(handle~))");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestProperty("Authorization", "Bearer " + token);
                //e.g. bearer token= eyJhbGciOiXXXzUxMiJ9.eyJzdWIiOiPyc2hhcm1hQHBsdW1zbGljZS5jb206OjE6OjkwIiwiZXhwIjoxNTM3MzQyNTIxLCJpYXQiOjE1MzY3Mzc3MjF9.O33zP2l_0eDNfcqSQz29jUGJC-_THYsXllrmkFnk85dNRbAw66dyEKBP5dVcFUuNTA8zhA83kk3Y41_qZYx43T
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestMethod("GET");


                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String output;

                response = new StringBuffer();
                while ((output = in.readLine()) != null) {
                    response.append(output);
                }

                in.close();
                // printing result from response
                System.out.println("Response:-" + response.toString());


            } catch (Exception e) {

            }

            return response.toString();
        }

        @Override
        protected void onPostExecute(String status) {

            if (pd != null && pd.isShowing()) {
                pd.dismiss();
            }

            String s = status;
            System.out.println(s);

            clearCookis();

            Intent intent = new Intent();
            intent.putExtra("res", s);
            setResult(100, intent);

            finish();

        }

    }

    private void clearCookis() {
        CookieManager cookieManager = CookieManager.getInstance();
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.removeAllCookies(new ValueCallback<Boolean>() {
                @Override
                public void onReceiveValue(Boolean value) {
                    //Removed?
                }
            });
            cookieManager.flush();
        } else {
            CookieSyncManager.createInstance(LinkedInLoginWebView.this);
            cookieManager.removeAllCookie();
        }

        webView.clearCache(true);
    }

    private static String getAccessTokenUrl(String authorizationToken) {
        return ACCESS_TOKEN_URL
                + QUESTION_MARK
                + GRANT_TYPE_PARAM + EQUALS + GRANT_TYPE
                + AMPERSAND
                + RESPONSE_TYPE_VALUE + EQUALS + authorizationToken
                + AMPERSAND
                + CLIENT_ID_PARAM + EQUALS + API_KEY
                + AMPERSAND
                + REDIRECT_URI_PARAM + EQUALS + REDIRECT_URI
                + AMPERSAND
                + SECRET_KEY_PARAM + EQUALS + SECRET_KEY;
    }

}
