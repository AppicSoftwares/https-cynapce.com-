package com.alcanzar.cynapse.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class AppCustomPreferenceClass {

    public static final String promo_plan_type="promo_plan_type";
    public static final String promo_price ="promo_price";
    public static final String promo_price_conf ="promo_price_conf";
    public static final String promo_code ="promo_price2";
    public static final String promo_code_conf ="promo_code_conf";
//    public static final String promo_price3 ="promo_price3";
    public static final String Price="price";
    public static final String UserId ="userId";
    public static final String username ="username";
    public static final String authToken ="authToken";
    public static final String name ="name";
    public static final String medical_profile_id ="medical_profile_id";
    public static final String medical_profile_name ="medical_profile_name";
    public static final String email ="email";
    public static final String phoneNumber ="phoneNumber";
    public static final String Country_name ="country_name";
    public static final String Country_id ="country_id";
    public static final String address ="address";
    public static final String state_id ="state_id";
    public static final String dob ="dob";
    public static final String occupation ="occupation";
    public static final String city_id ="city_id";
    public static final String profile_image ="profile_image";
    public static final String title_id ="title_id";
    public static final String sync_time ="sync_time";
    public static final String noti_sync_time ="noti_sync_time";
    public static final String chat_noti_sync_time ="chat_noti_sync_time";
    public static final String count_noti_sync_time ="count_noti_sync_time";
    public static final String deal_sync_time ="deal_sync_time";
    public static final String job_ra_sync_time ="job_ra_sync_time";
    public static final String app_job_ra_sync_time ="app_job_ra_sync_time";
    public static final String specilization_id ="specilization_id";
    public static final String sub_specialization_id ="sub_specialization_id";
    public static final String highest_degree_id="highest_degree_id";
    public static final String year_of_study="year_of_study";
    public static final String department_id="department_id";

    public static final String other_cat_sync_time ="other_cat_sync_time";
    public static final String Latitude ="Latitude";
    public static final String Longitude ="Longitude";
    public static final String sync_time_allconfer ="sync_time_allconfer";
    public static final String sync_time_myconference ="sync_time_myconference";
    public static final String sync_time_cases ="sync_time_cases";
    public static final String sync_time_attend_cases ="sync_time_attend_cases";
    public static final String sync_time_attend_ans_cases ="sync_time_attend_ans_cases";
    public static final String sync_time_posted_jobs ="sync_time_posted_jobs";
    public static final String sync_time_publish_list ="sync_time_publish_list";

    public static final String conference_city ="conference_city";
    public static final String conference_state ="conference_state";
    public static  final String filter_type="filter_type";
    public static  final String login_type="login_type";
    public static  final String loginDetaIls = "loginDetaIls";
    public static  final String conference_loc="conference_loc";
    public static  final String conference_categ="conference_categ";
    public static  final String tabPress="tabPress";
    public static final String sync_time_myconference_frag ="sync_time_myconference_frag";
    public static final String sync_time_saveconference_frag ="sync_time_saveconference_frag";
    public static final String change_like_status ="change_like_status";
    public static  final String filter_show="filter_show";
    public static  final String medical_profile_category_name="medical_profile_category_name";

    public static final String pay_plan_sync_time ="pay_plan_sync_time";
    public static final String all_pdf_list ="all_pdf_list";
    public static final String COLOR_CLICK_CHECK = "COLOR_CLICK_CHECK";
    public static String dataTest="02-07-2018";
    public static String currentDate1="";
    public static final String checkSelectStateAdapter= "dvsgwsrgwr";
    public static final String COMMAND = "command";
    public static final String ACCESS_CODE = "access_code";
    public static final String MERCHANT_ID = "merchant_id";
    public static final String ORDER_ID = "order_id";
    public static final String AMOUNT = "amount";
    public static final String CURRENCY = "currency";
    public static final String ENC_VAL = "enc_val";
    public static final String REDIRECT_URL = "redirect_url";
    public static final String CANCEL_URL = "cancel_url";
    public static final String RSA_KEY_URL = "rsa_key_url";
    public static final String PARAMETER_SEP = "&";
    public static final String PARAMETER_EQUALS = "=";
    public static int selectedTheme = -1;
    public static  String boolPdfDel="boolPdfDel";
    public static final String email_verified ="email_verified";
    public static final String mobile_verified ="phoneNumber";

    public static final String DeviceId = "DeviceId";
    public static final String pdf_name_code = "pdf_name_code";
    public static final String pdf_name = "pdf_name";


    public static final long MENU_DELAY = 300;
    private boolean isOverrideResultScreen = true;


    //TODO: used for the data putting and saving
    private static SharedPreferences.Editor getEditor(Context context){
        return getPreferences(context).edit();
    }
    //TODO: used for the saved data retrieve
    private static SharedPreferences getPreferences(Context context){
        String PREF_NAME = "Cynapse";
        return context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
    }
    //TODO: used to save the string variables
    public static void writeString(Context context,String key,String value){
         getEditor(context).putString(key,value).commit() ;
    }
    //TODO: used to read the string variables
    public static String readString(Context context, String key, String defaultValue){
        return getPreferences(context).getString(key,defaultValue);
    }
    //TODO: used to remove the individual key data
    public static void removeKey(Context context, String key) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.remove(key);
        editor.commit();
    }
    //TODO: used to remove the complete app data
    public static void removeAll(Context context) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.clear();
        editor.commit();
    }
    public boolean isOverrideResultScreen() {
        return isOverrideResultScreen;
    }

    public void setOverrideResultScreen(boolean overrideResultScreen) {
        isOverrideResultScreen = overrideResultScreen;
    }
}
