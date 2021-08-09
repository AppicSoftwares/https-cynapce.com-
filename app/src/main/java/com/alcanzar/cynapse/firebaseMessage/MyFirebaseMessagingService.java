package com.alcanzar.cynapse.firebaseMessage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alcanzar.cynapse.R;
import com.alcanzar.cynapse.activity.GoingConferenceDetailsActivity;
import com.alcanzar.cynapse.activity.LoginSignUpActivity;
import com.alcanzar.cynapse.activity.MainActivity;
import com.alcanzar.cynapse.activity.MarketPlaceActivity;
import com.alcanzar.cynapse.activity.MessageActivity;
import com.alcanzar.cynapse.activity.MyConferencesActivity;
import com.alcanzar.cynapse.activity.MyDealsActivity;
import com.alcanzar.cynapse.activity.MyJobActivity;
import com.alcanzar.cynapse.activity.MyProfileActivity;
import com.alcanzar.cynapse.activity.NotificationsActivity;
import com.alcanzar.cynapse.activity.PaymentConference;
import com.alcanzar.cynapse.activity.RecommendedJobsActivity;
import com.alcanzar.cynapse.activity.TicketDetailsNew;
import com.alcanzar.cynapse.api.ConferenceMyListApi;
import com.alcanzar.cynapse.api.RequestJobListApi;
import com.alcanzar.cynapse.appDatabase.DatabaseHelper;
import com.alcanzar.cynapse.model.AddForgeinPackageMYConferenceModel;
import com.alcanzar.cynapse.model.ConferenceDetailsModel;
import com.alcanzar.cynapse.model.ImageModel;
import com.alcanzar.cynapse.model.PackageSavedConferenceModel;
import com.alcanzar.cynapse.utils.AppCustomPreferenceClass;
import com.alcanzar.cynapse.utils.Utils;
import com.android.volley.VolleyError;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    String notification_type = "", product_id = "", product_category_id = "", chat_id = "", sender_id = "", uuid = "";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            notification_type = remoteMessage.getData().get("tag");
            product_id = remoteMessage.getData().get("product_id");
            product_category_id = remoteMessage.getData().get("product_category_id");
            chat_id = remoteMessage.getData().get("chat_id");
            uuid = remoteMessage.getData().get("uuid");
            sender_id = remoteMessage.getData().get("sender_id");

            if (AppCustomPreferenceClass.readString(this, AppCustomPreferenceClass.UserId, "").equalsIgnoreCase("")) {
                Intent intent = new Intent(this, LoginSignUpActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                methodnotify(remoteMessage.getData().get("message"), pendingIntent);
            } else {
                sendNotification(remoteMessage.getData().get("message"), notification_type, product_id, product_category_id, chat_id, uuid, sender_id);
            }

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
                scheduleJob();
            } else {
                // Handle message within 10 seconds
                handleNow();
            }

        }
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.


        // Check if message contains a notification payload.
//        if (remoteMessage.getNotification() != null) {
//            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
//        }
//        sendNotification(remoteMessage.getNotification().getBody());
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]


    // [START on_new_token]

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token);
    }
    // [END on_new_token]

    /**
     * Schedule a job using FirebaseJobDispatcher.
     */
    private void scheduleJob() {
        // [START dispatch_job]
//        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
//        Job myJob = dispatcher.newJobBuilder()
//                .setService(MyJobService.class)
//                .setTag("my-job-tag")
//                .build();
//        dispatcher.schedule(myJob);
        // [END dispatch_job]
    }

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");
    }

    /**
     * Persist token to third-party servers.
     * <p>
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
        AppCustomPreferenceClass.writeString(this, AppCustomPreferenceClass.DeviceId, token);
        Log.e("Token value", "Token value " + token);
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String messageBody, String noty_type, String product_id, String product_category_id, String chat_id, String uuid, String sender_id) {
        if (!messageBody.contains("rejected")) {
            if (noty_type.equals("2")) {
//            Intent intent = new Intent(this, MyDealsActivity.class);
//            intent.putExtra("FromNoti", "1");
//           // PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//           // PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
//            // Adds the back stack
//            stackBuilder.addParentStack(MainActivity.class);
//            // Adds the Intent to the top of the stack
//            stackBuilder.addNextIntent(intent);
//// Gets a PendingIntent containing the entire back stack
//            PendingIntent pendingIntent = stackBuilder.getPendingIntent(0,
//                    PendingIntent.FLAG_UPDATE_CURRENT);
//            //   Html.fromHtml("<h2>Title</h2><br><p>Description here</p>")
//
//            methodnotify(messageBody,pendingIntent);
                Intent resultIntent = new Intent(this, MyDealsActivity.class);
                resultIntent.putExtra("FromNoti", "2");
// Create the TaskStackBuilder and add the intent, which inflates the back stack
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
                stackBuilder.addNextIntentWithParentStack(resultIntent);
// Get the PendingIntent containing the entire back stack
                PendingIntent resultPendingIntent =
                        stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                methodnotify(messageBody, resultPendingIntent);


            } else if (noty_type.equals("13")) {
//            Intent intent = new Intent(this, MyProfileActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//            methodnotify(messageBody,pendingIntent);

                // Create an Intent for the activity you want to start
                Intent resultIntent = new Intent(this, MyProfileActivity.class);
                resultIntent.putExtra("edit_disable", "false");

// Create the TaskStackBuilder and add the intent, which inflates the back stack
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
                stackBuilder.addNextIntentWithParentStack(resultIntent);
// Get the PendingIntent containing the entire back stack
                PendingIntent resultPendingIntent =
                        stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                methodnotify(messageBody, resultPendingIntent);


            } else if (noty_type.equals("5")) {
//            Intent intent = new Intent(this, MarketPlaceActivity.class);
//            intent.putExtra("prod_id",product_id);
//            intent.putExtra("cat_id",product_category_id);
//            intent.putExtra("from_noti","true");
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//            methodnotify(messageBody,pendingIntent);


                Intent resultIntent = new Intent(this, MarketPlaceActivity.class);
                resultIntent.putExtra("prod_id", product_id);
                resultIntent.putExtra("cat_id", product_category_id);
                resultIntent.putExtra("from_noti", "true");
// Create the TaskStackBuilder and add the intent, which inflates the back stack
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
                stackBuilder.addNextIntentWithParentStack(resultIntent);
// Get the PendingIntent containing the entire back stack
                PendingIntent resultPendingIntent =
                        stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                methodnotify(messageBody, resultPendingIntent);

            } else if (noty_type.equals("6")) {
//            Intent intent = new Intent(this, RecommendedJobsActivity.class);
//            intent.putExtra("recommend","1");
//            intent.putExtra("job_id","");
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//            methodnotify(messageBody,pendingIntent);

                Intent resultIntent = new Intent(this, RecommendedJobsActivity.class);
                resultIntent.putExtra("recommend", "1");
                resultIntent.putExtra("job_id", product_id);
                resultIntent.putExtra("applicants_uuid", "");
// Create the TaskStackBuilder and add the intent, which inflates the back stack
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
                stackBuilder.addNextIntentWithParentStack(resultIntent);
// Get the PendingIntent containing the entire back stack
                PendingIntent resultPendingIntent =
                        stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                methodnotify(messageBody, resultPendingIntent);

            } else if (noty_type.equals("7")) {
//            Intent intent = new Intent(this, RecommendedJobsActivity.class);
//            intent.putExtra("recommend","0");
//            intent.putExtra("job_id",product_id);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//            methodnotify(messageBody,pendingIntent);

                Intent resultIntent = new Intent(this, RecommendedJobsActivity.class);
                resultIntent.putExtra("recommend", "0");
                resultIntent.putExtra("job_id", product_id);
                resultIntent.putExtra("applicants_uuid", sender_id);
// Create the TaskStackBuilder and add the intent, which inflates the back stack
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
                stackBuilder.addNextIntentWithParentStack(resultIntent);
// Get the PendingIntent containing the entire back stack
                PendingIntent resultPendingIntent =
                        stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                methodnotify(messageBody, resultPendingIntent);
            } else if (noty_type.equals("4")) {
//            Intent intent = new Intent(this, PaymentConference.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//            methodnotify(messageBody,pendingIntent);

                Intent resultIntent = new Intent(this, PaymentConference.class);
// Create the TaskStackBuilder and add the intent, which inflates the back stack
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
                stackBuilder.addNextIntentWithParentStack(resultIntent);
// Get the PendingIntent containing the entire back stack
                PendingIntent resultPendingIntent =
                        stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                methodnotify(messageBody, resultPendingIntent);

            } else if (noty_type.equals("12")) {
//            Intent intent = new Intent(this, MessageActivity.class);
//            intent.putExtra("sender_id",sender_id);
//            intent.putExtra("reciever_id",uuid);
//            intent.putExtra("prod_id",product_id);
//            intent.putExtra("chat_id",chat_id);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//            methodnotify(messageBody,pendingIntent);

                Intent resultIntent = new Intent(this, MessageActivity.class);
                resultIntent.putExtra("sender_id", sender_id);
                resultIntent.putExtra("reciever_id", uuid);
                resultIntent.putExtra("prod_id", product_id);
                resultIntent.putExtra("chat_id", chat_id);
// Create the TaskStackBuilder and add the intent, which inflates the back stack
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
                stackBuilder.addNextIntentWithParentStack(resultIntent);
// Get the PendingIntent containing the entire back stack
                PendingIntent resultPendingIntent =
                        stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                methodnotify(messageBody, resultPendingIntent);

            } else if (noty_type.equals("14")) {
//            Intent intent = new Intent(this, MessageActivity.class);
//            intent.putExtra("sender_id",sender_id);
//            intent.putExtra("reciever_id",uuid);
//            intent.putExtra("prod_id",product_id);
//            intent.putExtra("chat_id",chat_id);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//            methodnotify(messageBody,pendingIntent);
                Intent resultIntent = new Intent(this, MainActivity.class);
                // Create the TaskStackBuilder and add the intent, which inflates the back stack
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
                stackBuilder.addNextIntentWithParentStack(resultIntent);
                // Get the PendingIntent containing the entire back stack
                PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                methodnotify(messageBody, resultPendingIntent);
            } else if (noty_type.equals("15")) {
                Intent resultIntent = new Intent(this, MainActivity.class);
                // Create the TaskStackBuilder and add the intent, which inflates the back stack
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
                stackBuilder.addNextIntentWithParentStack(resultIntent);
                // Get the PendingIntent containing the entire back stack
                PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                methodnotify(messageBody, resultPendingIntent);
            } else if (noty_type.equals("16")) {
                Intent resultIntent = new Intent(this, MainActivity.class);
                // Create the TaskStackBuilder and add the intent, which inflates the back stack
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
                stackBuilder.addNextIntentWithParentStack(resultIntent);
                // Get the PendingIntent containing the entire back stack
                PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                methodnotify(messageBody, resultPendingIntent);
            } else if (noty_type.equals("10")) {
                Intent resultIntent = new Intent(this, MyJobActivity.class);
                // Create the TaskStackBuilder and add the intent, which inflates the back stack
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
                stackBuilder.addNextIntentWithParentStack(resultIntent);
                // Get the PendingIntent containing the entire back stack
                PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                methodnotify(messageBody, resultPendingIntent);
            } else if (noty_type.equals("20")) {
                Intent resultIntent = new Intent(this, PaymentConference.class);
                resultIntent.putExtra("conference_id", product_id);
                // Create the TaskStackBuilder and add the intent, which inflates the back stack
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
                stackBuilder.addNextIntentWithParentStack(resultIntent);
                // Get the PendingIntent containing the entire back stack
                PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                methodnotify(messageBody, resultPendingIntent);
            }

            //========================payment done of notification============================
            else if (noty_type.equals("21")) {
                Intent resultIntent = new Intent(this, MyConferencesActivity.class);
                resultIntent.putExtra("conference_id", product_id);
                resultIntent.putExtra("notificationBOB", "notification");
                // Create the TaskStackBuilder and add the intent, which inflates the back stack
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
                stackBuilder.addNextIntentWithParentStack(resultIntent);
                // Get the PendingIntent containing the entire back stack
                PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                methodnotify(messageBody, resultPendingIntent);
            }
            //========================payment done of conference============================
            else if (noty_type.equals("22")) {
                Intent resultIntent = new Intent(this, MyConferencesActivity.class);
                resultIntent.putExtra("conference_id", product_id);
                resultIntent.putExtra("notificationBOB1", "notification");
                // Create the TaskStackBuilder and add the intent, which inflates the back stack
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
                stackBuilder.addNextIntentWithParentStack(resultIntent);
                // Get the PendingIntent containing the entire back stack
                PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                methodnotify(messageBody, resultPendingIntent);
            }
        } else {
            // Create an Intent for the activity you want to start
            Intent resultIntent = new Intent(this, NotificationsActivity.class);
// Create the TaskStackBuilder and add the intent, which inflates the back stack
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addNextIntentWithParentStack(resultIntent);
// Get the PendingIntent containing the entire back stack
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            methodnotify(messageBody, resultPendingIntent);
        }

//        Intent intent = new Intent(this, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
//                PendingIntent.FLAG_UPDATE_CURRENT);


    }

    public void methodnotify(String messageBody, PendingIntent pendingIntent) {
        String channelId = getString(R.string.name);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.app_icon)
                        .setContentTitle("Cynapce")
//                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant") NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(genNotifyId() /* ID of notification */, notificationBuilder.build());
    }

    public int genNotifyId() {
        Random r = new Random(System.currentTimeMillis());
        return ((1 + r.nextInt(2)) * 10000 + r.nextInt(10000));
    }

    ArrayList<ConferenceDetailsModel> arrayList = null;
}
