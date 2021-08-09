package com.alcanzar.cynapse.appDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.alcanzar.cynapse.model.AddForgeinPackageMYConferenceModel;
import com.alcanzar.cynapse.model.AddPackageModal;
import com.alcanzar.cynapse.model.BookTicketModal;
import com.alcanzar.cynapse.model.CaseStudyListModel;
import com.alcanzar.cynapse.model.CommunityForumModel;
import com.alcanzar.cynapse.model.ConferenceDetailsModel;
import com.alcanzar.cynapse.model.ConferencePackageModel;
import com.alcanzar.cynapse.model.ConferencePackageModelForgein;
import com.alcanzar.cynapse.model.DashBoardProductModel;
import com.alcanzar.cynapse.model.ImageModel;
import com.alcanzar.cynapse.model.JobMasterModel;
import com.alcanzar.cynapse.model.JobPaymentModel;
import com.alcanzar.cynapse.model.JobSpecializationModel;
import com.alcanzar.cynapse.model.MedicalWritingModel;
import com.alcanzar.cynapse.model.MyConferenceModel.BookingDetail;
import com.alcanzar.cynapse.model.MyConferenceModel.MyConferenceDetailsModel;
import com.alcanzar.cynapse.model.NotificationDashBoardModel;
import com.alcanzar.cynapse.model.OtherCategoryModel;
import com.alcanzar.cynapse.model.PackageSavedConferenceModel;
import com.alcanzar.cynapse.model.PackagesModel;
import com.alcanzar.cynapse.model.PdfDelModel;
import com.alcanzar.cynapse.utils.MyToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    //TODO:  information of database


    private static final String TAG = "DATABASE_HELPER";
    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "appDB.db";
    //TODO: table Jobs Master
    public static final String TABLE_JOBS_MASTER = "jobs_master";
    public static final String TABLE_PRODUCT_MASTER = "product_master";
    public static final String TABLE_PRODUCT_DETAIL_MASTER = "product_detail_master";
    public static final String TABLE_NOTIFICATION_MASTER = "notification_master";
    public static final String TABLE_CHAT_NOTIFICATION_MASTER = "TABLE_CHAT_NOTIFICATION_MASTER";
    public static final String TABLE_SELL_BUY_PRODUCT_MASTER = "sell_buy_product_master";
    public static final String TABLE_RECOMMENDED_APPLIED_JOBS_MASTER = "recommeded_applied_job_master";
    public static final String TABLE_OTHER_CATEGORY_MASTER = "TABLE_OTHER_CATEGORY_MASTER";
    public static final String TABLE_METRO_CITY_MASTER = "TABLE_METRO_CITY_MASTER";
    public static final String TABLE_CASE_STUDY_LIST_MASTER = "TABLE_CASE_STUDY_LIST_MASTER";
    public static final String TABLE_CASE_STUDY_ATTEND_LIST_MASTER = "TABLE_CASE_STUDY_ATTEND_LIST_MASTER";
    public static final String TABLE_CASE_STUDY_QUESTION_MASTER = "TABLE_CASE_STUDY_QUESTION_MASTER";
    public static final String TABLE_CASE_STUDY_QUESTION_ANS_MASTER = "TABLE_CASE_STUDY_QUESTION_ANS_MASTER";
    public static final String TABLE_CASE_STUDY_OPTION_MASTER = "TABLE_CASE_STUDY_OPTION_MASTER";
    public static final String TABLE_CASE_STUDY_ANSWER_MASTER = "TABLE_CASE_STUDY_ANSWER_MASTER";
    public static final String TABLE_PAYMENT_PLAN = "payment_plan";
    public static final String TABLE_POSTED_JOBS_MASTER = "TABLE_POSTED_JOBS_MASTER";
    public static final String TABLE_GETTING_PUBLISH_LIST = "TABLE_GETTING_PUBLISH_LIST";
    public static final String TABLE_COMMUNITY_FORUM_LIST = "TABLE_COMMUNITY_FORUM_LIST";
    public static final String TABLE_PAYMENT_CONFERENCE_DETAILS = "TABLE_PAYMENT_CONFERENCE_DETAILS";

    public static final String TABLE_TICKET_BOOKING = "TABLE_TICKET_BOOKING";
    public static final String TABLE_BOOKING_DETAILS = "TABLE_BOOKING_DETAILS";
    public static final String TABLE_DELETE_PDF = "TABLE_DELETE_PDF";


    //TODO: Jobs Master Columns
    public static final String discount_percentage = "discount_percentage";
    public static final String discount_description = "discount_description";
    public static final String gst = "gst";
    public static final String booking_stopped = "booking_stopped";
    private static final String KEY_ID = "id";
    public static final String KEY_JOB_ID = "job_id";
    private static final String KEY_JOB_TITLE = "job_title";
    private static final String KEY_JOB_TYPE = "job_type";
    private static final String KEY_SPECIALIZATION = "specialization";
    private static final String KEY_YEAR_EXP = "year_exp";
    private static final String KEY_CURRENT_CTC = "current_ctc";
    private static final String KEY_EXPECTED_CTC = "expected_ctc";
    private static final String KEY_CURRENT_EMPLOYER = "current_employer";
    private static final String KEY_PREFERRED_LOCATION = "preferred_location";
    private static final String KEY_RESUME = "resume";
    private static final String KEY_SKILLS_REQUIRED = "skills_required";
    private static final String KEY_ADD_DATE = "add_date";
    private static final String KEY_MODIFY_DATE = "modify_date";
    private static final String KEY_STATUS = "status";
    private static final String app_jobId = "app_jobId";
    private static final String app_medical_profile_id = "app_medical_profile_id";
    private static final String app_medical_profile_name = "app_medical_profile_name";
    private static final String app_job_title = "app_job_title";
    private static final String app_job_title_id = "app_job_title_id";
    private static final String app_specialization_id = "app_specialization_id";
    private static final String app_specialization_name = "app_specialization_name";
    private static final String app_sub_specialization_id = "app_sub_specialization_id";
    private static final String app_sub_specialization_name = "app_sub_specialization_name";
    private static final String app_year_of_experience = "app_year_of_experience";
    private static final String app_current_ctc = "app_current_ctc";
    private static final String app_expected_ctc = "app_expected_ctc";
    private static final String preferred_location = "preferred_location";
    private static final String resume_description = "resume_description";
    private static final String app_department_id = "app_department_id";
    private static final String app_department_name = "app_department_name";
    private static final String KEY_APPLIED_JOBS_ID = "applied_jobs_id";
    public static final String product_id = "product_id";
    private static final String product_name = "product_name";
    private static final String category_id = "category_id";
    private static final String category_name = "category_name";
    private static final String price = "price";
    private static final String condition_type = "condition_type";
    private static final String condition = "condition";
    private static final String age = "age";
    private static final String rooms = "rooms";
    private static final String country_code = "country_code";
    private static final String country_name = "country_name";
    private static final String state_code = "state_code";
    private static final String state_name = "state_name";
    private static final String city_id = "city_id";
    private static final String city_name = "city_name";
    private static final String practice_category_type = "practice_category_type";
    private static final String practice_category_name = "practice_category_name";
    private static final String practice_category = "practice_category";
    private static final String practice_type = "practice_type";
    private static final String specific_locality = "specific_locality";
    private static final String land_length = "land_length";
    private static final String land_width = "land_width";
    private static final String total_area = "total_area";
    private static final String build_up_area = "build_up_area";
    private static final String type = "type";
    private static final String primary_type = "primary_type";
    private static final String primary = "prima";
    private static final String practice_type_name = "primary_type_name";
    private static final String licence = "licence";
    private static final String no_of_bed = "no_of_bed";
    private static final String specification = "specification";
    private static final String deal_type = "deal_type";
    private static final String deal_type_name = "deal_type_name";
    private static final String description = "description";
    private static final String votes = "votes";
    private static final String comments = "comments";
    private static final String product_image = "product_image";
    private static final String add_date = "add_date";
    private static final String modify_date = "modify_date";
    private static final String seeking_name = "seeking_name";
    private static final String status = "status";
    private static final String sell_buy_status = "sell_buy_status";
    private static final String featured_product = "featured_product";
    private static final String featured_product_type = "featured_product_type";
    public static final String id = "id";
    public static final String job_id = "job_id";
    public static final String job_title_id = "job_title_id";
    public static final String id_city = "id_city";
    public static final String type_id = "type_id";
    private static final String message = "message";
    private static final String msg_type = "msg_type";
    private static final String time = "time";
    private static final String title = "title";
    private static final String image = "image";
    private static final String published_year = "published_year";
    private static final String url = "url";
    private static final String product_category_id = "product_category_id";
    private static final String medical_profile_id = "medical_profile_id";
    private static final String medical_profile_name = "medical_profile_name";
    private static final String target_audience_speciality = "target_audience_speciality";
    private static final String job_type_name = "job_type_name";
    private static final String sub_specialization_id = "sub_specialization_id";
    private static final String sub_specialization_name = "sub_specialization_name";
    private static final String location = "location";
    private static final String job_description = "job_description";
    private static final String post_req_status = "post_req_status";
    private static final String department_id = "department_id";
    private static final String department_name = "department_name";
    private static final String applied_date = "applied_date";
    private static final String applied_status = "applied_status";
    private static final String shortlist_status = "shortlist_status";
    private static final String name = "name";
    private static final String case_name = "case_name";
    private static final String case_heading = "case_heading";
    private static final String case_type = "case_type";
    private static final String total_questions = "total_questions";
    private static final String time_durations = "time_durations";
    private static final String sender_id = "sender_id";
    private static final String uuid = "uuid";
    private static final String reciever_id = "reciever_id";
    private static final String chat_id = "chat_id";
    private static final String conference_name = "conference_name";
    private static final String particular_country_name = "particular_country_name";
    private static final String particular_state_id = "particular_state_id";
    private static final String particular_state_name = "particular_state_name";
    private static final String attend_status = "attend_status";

    private static final String latitude = "latitude";
    private static final String logitude = "logitude";
    private static final String play_type = "play_type";
    private static final String job_shortlist = "job_shortlist";
    private static final String percentage = "percentage";

    private static final String ctc = "ctc";

    private static final String venue = "venue";
    private static final String event_host_name = "event_host_name";
    private static final String speciality = "speciality";
    private static final String contact = "contact";

    private static final String brochuers_file = "brochuers_file";
    private static final String brochuers_charge = "brochuers_charge";
    private static final String brochuers_days = "brochuers_days";


    private static final String registration_fee = "registration_fee";
    private static final String registration_days = "registration_days";
    private static final String event_sponcer = "event_sponcer";
    private static final String particular_country_id = "particular_country_id";
    private static final String available_seat = "available_seat";
    private static final String conference_description = "conference_description";

    public static final String conference_id = "conference_id";

    private static final String payment_status = "payment_status";
    private static final String show_like = "show_like";
    private static final String from_date = "from_date";
    private static final String to_date = "to_date";
    private static final String from_time = "from_time";
    private static final String to_time = "to_time";
    private static final String registered = "registered";
    private static final String interested = "interested";
    private static final String views = "views";
    private static final String cost = "cost";
    private static final String duration = "duration";

    public static final String ques_id = "ques_id";
    private static final String ques_name = "ques_name";
    private static final String correct_answer = "correct_answer";
    private static final String ques_status = "ques_status";
    public static final String option_id = "option_id";
    private static final String option_name = "option_name";
    private static final String is_correct = "is_correct";
    private static final String answer = "answer";
    private static final String case_sub_title = "case_sub_title";
    private static final String total_attempted_ques = "total_attempted_ques";

    public static final String TABLE_CONFERENCES_SHOW_DETAILS = "conferences_show_details";
    public static final String TABLE_MY_CONFERENCES_SHOW_DETAILS = "my_conferences_show_details";
    public static final String TABLE_SAVE_CONFERENCES_SHOW_DETAILS = "save_conferences_show_details";
    public static final String TABLE_GOING_CONFERENCES_SHOW_DETAILS = "going_conferences_show_details";


    private static final String particular_city_id = "particular_city_id";
    private static final String particular_city_name = "particular_city_name";
    private static final String address_type = "address_type";
    private static final String KEY_JOB_TITLE_ID = "job_title_id";

    public static final String TABLE_CONFERENCES_PACKAGE = "TABLE_CONFERENCES_PACKAGE";
    public static final String TABLE_CONFERENCES_PACK_CHARGE = "TABLE_CONFERENCES_PACK_CHARGE";
    public static final String TABLE_AddPackageMYConference = "TABLE_AddPackageMYConference";
    public static final String TABLE_AddPackageGoingConference = "TABLE_AddPackageGoingConference";
    public static final String TABLE_AddForgeingPackageMYConference = "TABLE_AddForgeingPackageMYConference";
    public static final String TABLE_AddForgeingPackageGoingConference = "TABLE_AddForgeingPackageGoingConference";
    public static final String TABLE_CONFERENCES_PACK_CHARGE_FORGEIN = "TABLE_CONFERENCES_PACK_CHARGE_FORGEIN";
    public static final String TABLE_MY_CONFERENCES_PACK_CHARGE = "TABLE_MY_CONFERENCES_PACK_CHARGE";
    public static final String TABLE_SAVE_CONFERENCES_PACK_CHARGE = "TABLE_SAVE_CONFERENCES_PACK_CHARGE";
    public static final String TABLE_GOING_CONFERENCES_PACK_CHARGE = "TABLE_GOING_CONFERENCES_PACK_CHARGE";
    public static final String TABLE_MY_CONFERENCES_SPECIAL = "TABLE_MY_CONFERENCES_SPECIAL";
    public static final String TABLE_MY_CONFERENCES_MEDICAL = "TABLE_MY_CONFERENCES_MEDICAL";
    public static final String TABLE_SPECIALITY_DETAILS = "speciality_details";
    public static final String TABLE_CONFERENCES_SHOW_IMAGES = "conferences_show_images";
    public static final String conference_type_id = "conference_type_id";
    public static final String without_notification_cost = "without_notification_cost";
    public static final String with_notification_cost = "with_notification_cost";

    public static final String credit_earnings = "credit_earnings";
    public static final String payment_mode = "payment_mode";
    public static final String total_days_price = "total_days_price";
    public static final String accomdation = "accomdation";
    public static final String member_concessions = "member_concessions";
    public static final String student_concessions = "student_concessions";
    public static final String price_hike_after_date = "price_hike_after_date";
    public static final String price_hike_after_percentage = "price_hike_after_percentage";
    public static final String activate_status = "activate_status";

    public static final String conference_pack_id = "conference_pack_id";
    private static final String conference_pack_day = "conference_pack_day";
    private static final String conference_pack_charge = "conference_pack_charge";

    public static final String special_id = "special_id";
    private static final String special_name = "special_name";
    private static final String image_name = "image_name";
    public static final String image_id = "image_id";

    public static final String medical_id = "medical_id";
    private static final String medical_name = "medical_name";
    public static final String package_id = "package_id";

    private static final String package_name = "package_name";
    private static final String package_total_price = "package_total_price";
    public static final String package_status = "package_status";
    public static final String package_sync_time = "package_sync_time";
    public static final String specialization_id = "specialization_id";
    private static final String specialization_name = "specialization_name";

    public static final String pdf_id = "pdf_id";
    private static final String pdf_url = "pdf_url";

    public static final String profile_category_id = "profile_category_id";
    public static final String BOOK_TICKET_TABLE = "booktickettable";

    public static String book_ticket_id = "btocketid";
    public static String book_ticket_day = "sdvsfv";
    public static String book_ticket_price = "btovsfbvscketid";
    public static String book_ticket_date = "dvs";
    public static String book_ticket_per = "cvsketid";
    public static String book_ticket_selected = "sdvwvgv";
    public static String getConference_type_name = "getConference_type_name";

    public static final String booking_conference_id = "booking_conference_id";
    public static final String no_of_seats = "no_of_seats";
    public static final String order_id = "order_id";
    public static final String total_amount = "total_amount";

    public static final String username = "username";
    public static final String phone_no = "phone_no";
    public static final String email = "email";

    //TODO: initialization of the database
    public DatabaseHelper(Context context) {
        //-----Add this line to access sqlite file from phone storage or sdcard -----//
        //  "/mnt/sdcard/"+DATABASE_NAME
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //super(context, "/mnt/sdcard/"+DATABASE_NAME, null, DATABASE_VERSION);
    }

    //TODO: this method will be called only once throughOut the database
    @Override
    public void onCreate(SQLiteDatabase db) {
        //TODO: Creating the Jobs Master Table
        String CREATE_JOBS_MASTER = "CREATE TABLE " + TABLE_JOBS_MASTER
                + " (" + id + " TEXT,"
                + KEY_JOB_ID + " TEXT,"
                + medical_profile_id + " TEXT,"
                + medical_profile_name + " TEXT,"
                + KEY_JOB_TITLE + " TEXT,"
                + KEY_JOB_TYPE + " TEXT,"
                + job_type_name + " TEXT,"
                + specialization_id + " TEXT,"
                + specialization_name + " TEXT,"
                + sub_specialization_id + " TEXT,"
                + sub_specialization_name + " TEXT,"
                + KEY_YEAR_EXP + " TEXT,"
                + KEY_CURRENT_CTC + " TEXT,"
                + KEY_EXPECTED_CTC + " TEXT,"
                + KEY_CURRENT_EMPLOYER + " TEXT,"
                + location + " TEXT,"
                + KEY_PREFERRED_LOCATION + " TEXT,"
                + KEY_RESUME + " TEXT,"
                + job_description + " TEXT,"
                + KEY_SKILLS_REQUIRED + " TEXT,"
                + KEY_ADD_DATE + " TEXT,"
                + KEY_MODIFY_DATE + " TEXT,"
                + post_req_status + " TEXT,"
                + department_id + " TEXT,"
                + department_name + " TEXT,"
                + KEY_STATUS + " TEXT"
                + ") ";

        String CREATE_TABLE_DELETE_PDF = "CREATE TABLE " + TABLE_DELETE_PDF
                + " (" + pdf_id + " TEXT,"
                + pdf_url + " TEXT,"
                + conference_id + " TEXT"
                + ") ";

        String CREATE_TABLE_COMMUNITY_FORUM_LIST = "CREATE TABLE " + TABLE_COMMUNITY_FORUM_LIST
                + " (" + id + " TEXT,"
                + title + " TEXT,"
                + specialization_name + " TEXT,"
                + description + " TEXT,"
                + votes + " TEXT,"
                + comments + "TEXT,"
                + views + "TEXT,"
                + add_date + "TEXT,"
                + modify_date + "TEXT"
                + ") ";
        String CREATE_PAYMENT_CONFERENCE_DETAILS = "CREATE TABLE " + TABLE_PAYMENT_CONFERENCE_DETAILS
                + " (" + id + " TEXT,"
                + conference_id + " TEXT,"
                + conference_name + " TEXT,"
                + from_date + " TEXT,"
                + to_date + " TEXT,"
                + from_time + " TEXT,"
                + to_time + " TEXT,"
                + venue + " TEXT,"
                + conference_type_id + " TEXT,"
                + total_days_price + " TEXT,"
                + payment_mode + " TEXT,"
                + with_notification_cost + " TEXT,"
                + without_notification_cost + " TEXT,"
                + medical_profile_name + " TEXT,"
                + target_audience_speciality + " TEXT"
                + ") ";

        String CREATE_BOOK_TICKET_TABLE = "CREATE TABLE " + BOOK_TICKET_TABLE
                + "(" + book_ticket_id + " TEXT,"
                + book_ticket_day + " TEXT,"
                + book_ticket_price + " TEXT,"
                + book_ticket_date + " TEXT,"
                + book_ticket_per + " TEXT,"
                + book_ticket_selected + " TEXT"
                + ")";

        String CREATE_GOING_CONFERENCE_DETAILS = "CREATE TABLE " + TABLE_GOING_CONFERENCES_SHOW_DETAILS
                + " (" + id + " TEXT,"
                + conference_id + " TEXT,"
                + conference_name + " TEXT,"
                + from_date + " TEXT,"
                + to_date + " TEXT,"
                + from_time + " TEXT,"
                + to_time + " TEXT,"
                + venue + " TEXT,"
                + event_host_name + " TEXT,"
                + speciality + " TEXT,"
                + contact + " TEXT,"
                + location + " TEXT,"
                + latitude + " TEXT,"
                + logitude + " TEXT,"
                + brochuers_file + " TEXT,"
                + brochuers_charge + " TEXT,"
                + brochuers_days + " TEXT,"
                + registration_fee + " TEXT,"
                + registration_days + " TEXT,"
                + event_sponcer + " TEXT,"
                + particular_country_id + " TEXT,"
                + particular_country_name + " TEXT,"
                + particular_state_id + " TEXT,"
                + particular_state_name + " TEXT,"
                + status + " TEXT,"
                + add_date + " TEXT,"
                + modify_date + " TEXT,"
                + payment_status + " TEXT,"
                + show_like + " TEXT,"
                + particular_city_id + " TEXT,"
                + particular_city_name + " TEXT,"
                + available_seat + " TEXT,"
                + conference_description + " TEXT,"
                + conference_pack_day + " TEXT,"
                + conference_pack_charge + " TEXT,"
                + conference_type_id + " TEXT,"
                + medical_profile_id + " TEXT,"
                + credit_earnings + " TEXT,"
                + payment_mode + " TEXT,"
                + total_days_price + " TEXT,"
                + accomdation + " TEXT,"
                + member_concessions + " TEXT,"
                + student_concessions + " TEXT,"
                + price_hike_after_date + " TEXT,"
                + price_hike_after_percentage + " TEXT,"
                + medical_profile_name + " TEXT,"
                + special_name + " TEXT,"
                + department_id + " TEXT,"
                + department_name + " TEXT,"
                + discount_percentage + " TEXT,"
                + discount_description + " TEXT"
                + ") ";


        String CREATE_POSTED_JOBS_MASTER = "CREATE TABLE " + TABLE_POSTED_JOBS_MASTER
                + " (" + id + " TEXT,"
                + KEY_JOB_ID + " TEXT,"
                + medical_profile_id + " TEXT,"
                + medical_profile_name + " TEXT,"
                + KEY_JOB_TITLE + " TEXT,"
                + KEY_JOB_TYPE + " TEXT,"
                + job_type_name + " TEXT,"
                + specialization_id + " TEXT,"
                + specialization_name + " TEXT,"
                + sub_specialization_id + " TEXT,"
                + sub_specialization_name + " TEXT,"
                + KEY_YEAR_EXP + " TEXT,"
                + KEY_CURRENT_CTC + " TEXT,"
                + KEY_EXPECTED_CTC + " TEXT,"
                + KEY_CURRENT_EMPLOYER + " TEXT,"
                + location + " TEXT,"
                + KEY_PREFERRED_LOCATION + " TEXT,"
                + KEY_RESUME + " TEXT,"
                + job_description + " TEXT,"
                + KEY_SKILLS_REQUIRED + " TEXT,"
                + KEY_ADD_DATE + " TEXT,"
                + KEY_MODIFY_DATE + " TEXT,"
                + post_req_status + " TEXT,"
                + department_id + " TEXT,"
                + department_name + " TEXT,"
                + KEY_STATUS + " TEXT,"
                + job_title_id + " TEXT"
                + ") ";
        String CREATE_PUB_LIST_MASTER = "CREATE TABLE " + TABLE_GETTING_PUBLISH_LIST
                + " (" + id + " TEXT,"
                + title + " TEXT,"
                + image + " TEXT,"
                + published_year + " TEXT,"
                + url + " TEXT,"
                + add_date + " TEXT,"
                + modify_date + " TEXT,"
                + status + " TEXT"
                + ") ";
//        String CREATE_MY_CONFERENCE_DETAILS = "CREATE TABLE " + TABLE_MY_CONFERENCES_SHOW_DETAILS
//                + " (" + id + " TEXT,"
//                + conference_id + " TEXT,"
//                + conference_name + " TEXT,"
//                + from_date + " TEXT,"
//                + to_date + " TEXT,"
//                + from_time + " TEXT,"
//                + to_time + " TEXT,"
//                + venue + " TEXT,"
//                + event_host_name + " TEXT,"
//                + speciality + " TEXT,"
//                + contact + " TEXT,"
//                + location + " TEXT,"
//                + latitude + " TEXT,"
//                + logitude + " TEXT,"
//                + brochuers_file + " TEXT,"
//                + brochuers_charge + " TEXT,"
//                + brochuers_days + " TEXT,"
//                + registration_fee + " TEXT,"
//                + registration_days + " TEXT,"
//                + event_sponcer + " TEXT,"
//                + particular_country_id + " TEXT,"
//                + particular_country_name + " TEXT,"
//                + particular_state_id + " TEXT,"
//                + particular_state_name + " TEXT,"
//                + status + " TEXT,"
//                + add_date + " TEXT,"
//                + modify_date + " TEXT,"
//                + registered + " TEXT,"
//                + interested + " TEXT,"
//                + views + " TEXT,"
//                + cost + " TEXT,"
//                + duration + " TEXT,"
//                + payment_status + " TEXT,"
//                + show_like + " TEXT,"
//                + particular_city_id + " TEXT,"
//                + particular_city_name + " TEXT,"
//                + address_type + " TEXT,"
//                + available_seat + " TEXT,"
//                + conference_description + " TEXT,"
//                + conference_type_id + " TEXT,"
//                + medical_profile_id + " TEXT,"
//                + credit_earnings + " TEXT,"
//                + payment_mode + " TEXT,"
//                + total_days_price + " TEXT,"
//                + accomdation + " TEXT,"
//                + member_concessions + " TEXT,"
//                + student_concessions + " TEXT,"
//                + price_hike_after_date + " TEXT,"
//                + price_hike_after_percentage + " TEXT"
//                + ") ";

        String CREATE_CONFERENCE_DETAILS = "CREATE TABLE " + TABLE_CONFERENCES_SHOW_DETAILS
                + " (" + id + " TEXT,"
                + conference_id + " TEXT,"
                + conference_name + " TEXT,"
                + from_date + " TEXT,"
                + to_date + " TEXT,"
                + from_time + " TEXT,"
                + to_time + " TEXT,"
                + venue + " TEXT,"
                + event_host_name + " TEXT,"
                + speciality + " TEXT,"
                + contact + " TEXT,"
                + location + " TEXT,"
                + latitude + " TEXT,"
                + logitude + " TEXT,"
                + brochuers_file + " TEXT,"
                + brochuers_charge + " TEXT,"
                + brochuers_days + " TEXT,"
                + registration_fee + " TEXT,"
                + registration_days + " TEXT,"
                + event_sponcer + " TEXT,"
                + particular_country_id + " TEXT,"
                + particular_country_name + " TEXT,"
                + particular_state_id + " TEXT,"
                + particular_state_name + " TEXT,"
                + status + " TEXT,"
                + add_date + " TEXT,"
                + modify_date + " TEXT,"
                + payment_status + " TEXT,"
                + show_like + " TEXT,"
                + particular_city_id + " TEXT,"
                + particular_city_name + " TEXT,"
                + available_seat + " TEXT,"
                + conference_description + " TEXT,"
                + conference_pack_day + " TEXT,"
                + conference_pack_charge + " TEXT,"
                + conference_type_id + " TEXT,"
                + medical_profile_id + " TEXT,"
                + credit_earnings + " TEXT,"
                + payment_mode + " TEXT,"
                + total_days_price + " TEXT,"
                + accomdation + " TEXT,"
                + member_concessions + " TEXT,"
                + student_concessions + " TEXT,"
                + price_hike_after_date + " TEXT,"
                + price_hike_after_percentage + " TEXT,"
                + getConference_type_name + " TEXT,"
                + discount_percentage + " TEXT,"
                + discount_description + " TEXT,"
                + gst + " TEXT,"
                + booking_stopped + " TEXT,"
                + special_name + " TEXT,"
                + medical_profile_name + " TEXT,"
                + department_id + " TEXT,"
                + department_name + " TEXT,"
                + activate_status + " TEXT"
                + ") ";

        String CREATE_RECOMMENDED_APPLIED_JOBS_MASTER = "CREATE TABLE " + TABLE_RECOMMENDED_APPLIED_JOBS_MASTER
                + " (" + id + " TEXT,"
                + KEY_JOB_ID + " TEXT,"
                + medical_profile_id + " TEXT,"
                + medical_profile_name + " TEXT,"
                + KEY_JOB_TITLE + " TEXT,"
                + KEY_JOB_TYPE + " TEXT,"
                + job_type_name + " TEXT,"
                + specialization_id + " TEXT,"
                + specialization_name + " TEXT,"
                + sub_specialization_id + " TEXT,"
                + sub_specialization_name + " TEXT,"
                + KEY_YEAR_EXP + " TEXT,"
                + KEY_CURRENT_CTC + " TEXT,"
                + KEY_EXPECTED_CTC + " TEXT,"
                + KEY_CURRENT_EMPLOYER + " TEXT,"
                + location + " TEXT,"
                + KEY_PREFERRED_LOCATION + " TEXT,"
                + KEY_RESUME + " TEXT,"
                + job_description + " TEXT,"
                + KEY_SKILLS_REQUIRED + " TEXT,"
                + KEY_ADD_DATE + " TEXT,"
                + KEY_MODIFY_DATE + " TEXT,"
                + post_req_status + " TEXT,"
                + department_id + " TEXT,"
                + department_name + " TEXT,"
                + applied_date + " TEXT,"
                + applied_status + " TEXT,"
                + shortlist_status + " TEXT,"
                + KEY_STATUS + " TEXT,"
                + app_jobId + " TEXT,"
                + app_medical_profile_id + " TEXT,"
                + app_medical_profile_name + " TEXT,"
                + app_job_title + " TEXT,"
                + app_job_title_id + " TEXT,"
                + app_specialization_id + " TEXT,"
                + app_specialization_name + " TEXT,"
                + app_sub_specialization_id + " TEXT,"
                + app_sub_specialization_name + " TEXT,"
                + app_year_of_experience + " TEXT,"
                + app_current_ctc + " TEXT,"
                + app_expected_ctc + " TEXT,"
                + resume_description + " TEXT,"
                + app_department_id + " TEXT,"
                + app_department_name + " TEXT,"
                + name + " TEXT,"
                + KEY_JOB_TITLE_ID + " TEXT"
                + ") ";
        String CREATE_PRODUCT_MASTER = "CREATE TABLE " + TABLE_PRODUCT_MASTER
                + " (" + product_id + " TEXT,"
                + product_name + " TEXT,"
                + category_id + " TEXT,"
                + category_name + " TEXT,"
                + price + " TEXT,"
                + condition_type + " TEXT,"
                + condition + " TEXT,"
                + age + " TEXT,"
                + practice_category_type + " TEXT,"
                + practice_category_name + " TEXT,"
                + practice_type + " TEXT,"
                + practice_type_name + " TEXT,"
                + specific_locality + " TEXT,"
                + land_length + " TEXT,"
                + land_width + " TEXT,"
                + total_area + " TEXT,"
                + build_up_area + " TEXT,"
                + type + " TEXT,"
                + primary_type + " TEXT,"
                + licence + " TEXT,"
                + no_of_bed + " TEXT,"
                + specification + " TEXT,"
                + deal_type + " TEXT,"
                + description + " TEXT,"
                + product_image + " TEXT,"
                + add_date + " TEXT,"
                + modify_date + " TEXT,"
                + status + " TEXT,"
                + featured_product + " TEXT,"
                + featured_product_type + " TEXT,"
                + specialization_name + " TEXT"
                + ") ";
        String CREATE_SELL_BUY_PRODUCT_MASTER_MASTER = "CREATE TABLE " + TABLE_SELL_BUY_PRODUCT_MASTER
                + " (" + id + " TEXT,"
                + product_id + " TEXT,"
                + product_name + " TEXT,"
                + category_id + " TEXT,"
                + category_name + " TEXT,"
                + price + " TEXT,"
                + condition_type + " TEXT,"
                + condition + " TEXT,"
                + age + " TEXT,"
                + practice_category_type + " TEXT,"
                + practice_category + " TEXT,"
                + practice_type + " TEXT,"
                + practice_type_name + " TEXT,"
                + rooms + " TEXT,"
                + country_code + " TEXT,"
                + country_name + " TEXT,"
                + state_code + " TEXT,"
                + state_name + " TEXT,"
                + city_id + " TEXT,"
                + city_name + " TEXT,"
                + specific_locality + " TEXT,"
                + land_length + " TEXT,"
                + land_width + " TEXT,"
                + total_area + " TEXT,"
                + build_up_area + " TEXT,"
                + primary_type + " TEXT,"
                + primary + " TEXT,"
                + licence + " TEXT,"
                + no_of_bed + " TEXT,"
                + specification + " TEXT,"
                + deal_type + " TEXT,"
                + deal_type_name + " TEXT,"
                + description + " TEXT,"
                + product_image + " TEXT,"
                + add_date + " TEXT,"
                + modify_date + " TEXT,"
                + status + " TEXT,"
                + sell_buy_status + " TEXT"
                + ") ";

        String CREATE_PRODUCT_DETAIL_MASTER = "CREATE TABLE " + TABLE_PRODUCT_DETAIL_MASTER
                + " (" + product_id + " TEXT,"
                + product_name + " TEXT,"
                + category_id + " TEXT,"
                + category_name + " TEXT,"
                + price + " TEXT,"
                + condition_type + " TEXT,"
                + condition + " TEXT,"
                + age + " TEXT,"
                + rooms + " TEXT,"
                + specific_locality + " TEXT,"
                + land_length + " TEXT,"
                + land_width + " TEXT,"
                + total_area + " TEXT,"
                + build_up_area + " TEXT,"
                + type + " TEXT,"
                + primary_type + " TEXT,"
                + licence + " TEXT,"
                + no_of_bed + " TEXT,"
                + specification + " TEXT,"
                + deal_type + " TEXT,"
                + description + " TEXT,"
                + product_image + " TEXT,"
                + add_date + " TEXT,"
                + modify_date + " TEXT,"
                + seeking_name + " TEXT,"
                + status + " TEXT"
                + ") ";
        String CREATE_NOTIFICATION_MASTER = "CREATE TABLE " + TABLE_NOTIFICATION_MASTER
                + " (" + id + " TEXT,"
                + uuid + " TEXT,"
                + product_id + " TEXT,"
                + sender_id + " TEXT,"
                + product_category_id + " TEXT,"
                + message + " TEXT,"
                + msg_type + " TEXT,"
                + add_date + " TEXT,"
                + modify_date + " TEXT,"
                + status + " TEXT,"
                + chat_id + " TEXT,"
                + time + " TEXT"
                + ") ";
        String CREATE_CHAT_NOTIFICATION_MASTER = "CREATE TABLE " + TABLE_CHAT_NOTIFICATION_MASTER
                + " (" + id + " TEXT,"
                + product_id + " TEXT,"
                + sender_id + " TEXT,"
                + reciever_id + " TEXT,"
                + message + " TEXT,"
                + add_date + " TEXT,"
                + modify_date + " TEXT,"
                + status + " TEXT,"
                + name + " TEXT,"
                + chat_id + " TEXT,"
                + time + " TEXT"
                + ") ";
        String OTHER_CATEGORY_MASTER = "CREATE TABLE " + TABLE_OTHER_CATEGORY_MASTER
                + " (" + id + " TEXT,"
                + type_id + " TEXT,"
                + name + " TEXT,"
                + status + " TEXT"
                + ") ";
        String CASE_STUDY_LIST_MASTER = "CREATE TABLE " + TABLE_CASE_STUDY_LIST_MASTER
                + " (" + id + " TEXT,"
                + case_name + " TEXT,"
                + case_sub_title + " TEXT,"
                + description + " TEXT,"
                + case_type + " TEXT,"
                + attend_status + " TEXT,"
                + add_date + " TEXT,"
                + modify_date + " TEXT,"
                + status + " TEXT"
                + ") ";

        String CASE_STUDY_ATTEND_LIST_MASTER = "CREATE TABLE " + TABLE_CASE_STUDY_ATTEND_LIST_MASTER
                + " (" + id + " TEXT,"
                + case_name + " TEXT,"
                + case_type + " TEXT,"
                + case_sub_title + " TEXT,"
                + total_questions + " TEXT,"
                + total_attempted_ques + " TEXT,"
                + add_date + " TEXT,"
                + modify_date + " TEXT,"
                + status + " TEXT"
                + ") ";

        String METRO_CITY_MASTER = "CREATE TABLE " + TABLE_METRO_CITY_MASTER
                + " (" + id_city + " TEXT,"
                + city_name + " TEXT,"
                + status + " TEXT"
                + ") ";

        String CASE_STUDY_QUESTION_MASTER = "CREATE TABLE " + TABLE_CASE_STUDY_QUESTION_MASTER
                // +" (" + id + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " (" + id + " TEXT,"
                + ques_id + " TEXT,"
                + case_name + " TEXT,"
                + case_heading + " TEXT,"
                + description + " TEXT,"
                + case_type + " TEXT,"
                + total_questions + " TEXT,"
                + time_durations + " TEXT,"
                + ques_name + " TEXT,"
                + ques_status + " TEXT"
                + ") ";

        String CASE_STUDY_QUESTION_ANS_MASTER = "CREATE TABLE " + TABLE_CASE_STUDY_QUESTION_ANS_MASTER
                // +" (" + id + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " (" + id + " TEXT,"
                + ques_name + " TEXT,"
                + option_name + " TEXT,"
                + is_correct + " TEXT,"
                + correct_answer + " TEXT"
                + ") ";

        String CASE_STUDY_OPTION_MASTER = "CREATE TABLE " + TABLE_CASE_STUDY_OPTION_MASTER
                // +" (" + id + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " (" + option_id + " TEXT,"
                + ques_id + " TEXT,"
                + option_name + " TEXT,"
                + is_correct + " TEXT"
                + ") ";
        String CASE_STUDY_ANSWER_MASTER = "CREATE TABLE " + TABLE_CASE_STUDY_ANSWER_MASTER
                // +" (" + id + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " (" + option_id + " TEXT,"
                + ques_id + " TEXT,"
                + option_name + " TEXT,"
                + answer + " TEXT"
                + ") ";

        String CREATE_PAYMENT_PLAN = "CREATE TABLE " + TABLE_PAYMENT_PLAN
                + " (" + id + " TEXT,"
                + play_type + " TEXT,"
                + job_shortlist + " TEXT,"
                + price + " TEXT,"
                + add_date + " TEXT,"
                + percentage + " TEXT,"
                + ctc + " TEXT,"
                + job_id + " TEXT,"
                + job_title_id + " TEXT"
                + ") ";

//        String CREATE_SAVE_CONFERENCE_DETAILS = "CREATE TABLE " + TABLE_SAVE_CONFERENCES_SHOW_DETAILS
//                + " (" + id + " TEXT,"
//                + conference_id + " TEXT,"
//                + conference_name + " TEXT,"
//                + from_date + " TEXT,"
//                + to_date + " TEXT,"
//                + from_time + " TEXT,"
//                + to_time + " TEXT,"
//                + venue + " TEXT,"
//                + event_host_name + " TEXT,"
//                + speciality + " TEXT,"
//                + contact + " TEXT,"
//                + location + " TEXT,"
//                + latitude + " TEXT,"
//                + logitude + " TEXT,"
//                + brochuers_file + " TEXT,"
//                + brochuers_charge + " TEXT,"
//                + brochuers_days + " TEXT,"
//                + registration_fee + " TEXT,"
//                + registration_days + " TEXT,"
//                + event_sponcer + " TEXT,"
//                + particular_country_id + " TEXT,"
//                + particular_country_name + " TEXT,"
//                + particular_state_id + " TEXT,"
//                + particular_state_name + " TEXT,"
//                + status + " TEXT,"
//                + add_date + " TEXT,"
//                + modify_date + " TEXT,"
//                + payment_status + " TEXT,"
//                + show_like + " TEXT,"
//                + particular_city_id + " TEXT,"
//                + particular_city_name + " TEXT,"
//                + address_type + " TEXT,"
//                + available_seat + " TEXT,"
//                + conference_description + " TEXT,"
//                + conference_type_id + " TEXT,"
//                + medical_profile_id + " TEXT,"
//                + credit_earnings + " TEXT,"
//                + payment_mode + " TEXT,"
//                + total_days_price + " TEXT,"
//                + accomdation + " TEXT,"
//                + member_concessions + " TEXT,"
//                + student_concessions + " TEXT,"
//                + price_hike_after_date + " TEXT,"
//                + price_hike_after_percentage + " TEXT"
//                + ") ";
        String CREATE_SAVE_CONFERENCE_DETAILS = "CREATE TABLE " + TABLE_SAVE_CONFERENCES_SHOW_DETAILS
                + " (" + id + " TEXT,"
                + conference_id + " TEXT,"
                + conference_name + " TEXT,"
                + from_date + " TEXT,"
                + to_date + " TEXT,"
                + from_time + " TEXT,"
                + to_time + " TEXT,"
                + venue + " TEXT,"
                + event_host_name + " TEXT,"
                + speciality + " TEXT,"
                + contact + " TEXT,"
                + location + " TEXT,"
                + latitude + " TEXT,"
                + logitude + " TEXT,"
                + brochuers_file + " TEXT,"
                + brochuers_charge + " TEXT,"
                + brochuers_days + " TEXT,"
                + registration_fee + " TEXT,"
                + registration_days + " TEXT,"
                + event_sponcer + " TEXT,"
                + particular_country_id + " TEXT,"
                + particular_country_name + " TEXT,"
                + particular_state_id + " TEXT,"
                + particular_state_name + " TEXT,"
                + status + " TEXT,"
                + add_date + " TEXT,"
                + modify_date + " TEXT,"
                + payment_status + " TEXT,"
                + show_like + " TEXT,"
                + particular_city_id + " TEXT,"
                + particular_city_name + " TEXT,"
                + available_seat + " TEXT,"
                + conference_description + " TEXT,"
                + conference_pack_day + " TEXT,"
                + conference_pack_charge + " TEXT,"
                + conference_type_id + " TEXT,"
                + medical_profile_id + " TEXT,"
                + credit_earnings + " TEXT,"
                + payment_mode + " TEXT,"
                + total_days_price + " TEXT,"
                + accomdation + " TEXT,"
                + member_concessions + " TEXT,"
                + student_concessions + " TEXT,"
                + price_hike_after_date + " TEXT,"
                + price_hike_after_percentage + " TEXT,"
                + getConference_type_name + " TEXT,"
                + discount_percentage + " TEXT,"
                + discount_description + " TEXT,"
                + gst + " TEXT,"
                + medical_profile_name + " TEXT,"
                + special_name + " TEXT,"
                + department_id + " TEXT,"
                + department_name + " TEXT,"
                + booking_stopped + " TEXT"
                + ") ";

        String CREATE_MY_CONFERENCE_DETAILS = "CREATE TABLE " + TABLE_MY_CONFERENCES_SHOW_DETAILS
                + " (" + id + " TEXT,"
                + conference_id + " TEXT,"
                + conference_name + " TEXT,"
                + from_date + " TEXT,"
                + to_date + " TEXT,"
                + from_time + " TEXT,"
                + to_time + " TEXT,"
                + venue + " TEXT,"
                + event_host_name + " TEXT,"
                + speciality + " TEXT,"
                + contact + " TEXT,"
                + location + " TEXT,"
                + latitude + " TEXT,"
                + logitude + " TEXT,"
                + brochuers_file + " TEXT,"
                + brochuers_charge + " TEXT,"
                + brochuers_days + " TEXT,"
                + registration_fee + " TEXT,"
                + registration_days + " TEXT,"
                + event_sponcer + " TEXT,"
                + particular_country_id + " TEXT,"
                + particular_country_name + " TEXT,"
                + particular_state_id + " TEXT,"
                + particular_state_name + " TEXT,"
                + status + " TEXT,"
                + add_date + " TEXT,"
                + modify_date + " TEXT,"
                + registered + " TEXT,"
                + interested + " TEXT,"
                + views + " TEXT,"
                + cost + " TEXT,"
                + duration + " TEXT,"
                + payment_status + " TEXT,"
                + show_like + " TEXT,"
                + particular_city_id + " TEXT,"
                + particular_city_name + " TEXT,"
                + address_type + " TEXT,"
                + available_seat + " TEXT,"
                + conference_description + " TEXT,"
                + conference_type_id + " TEXT,"
                + medical_profile_id + " TEXT,"
                + credit_earnings + " TEXT,"
                + payment_mode + " TEXT,"
                + total_days_price + " TEXT,"
                + accomdation + " TEXT,"
                + member_concessions + " TEXT,"
                + student_concessions + " TEXT,"
                + price_hike_after_date + " TEXT,"
                + price_hike_after_percentage + " TEXT,"
                + pdf_url + " TEXT,"
                + discount_percentage + " TEXT,"
                + discount_description + " TEXT,"
                + gst + " TEXT,"
                + booking_stopped + " TEXT,"
                + medical_profile_name + " TEXT,"
                + special_name + " TEXT,"
                + department_id + " TEXT,"
                + department_name + " TEXT"
                + ") ";

        String CREATE_GOING_CONFERENCE_PACK_CHARGE = "CREATE TABLE " + TABLE_GOING_CONFERENCES_PACK_CHARGE
                + " (" + conference_pack_id + " TEXT,"
                + conference_pack_day + " TEXT,"
                + conference_pack_charge + " TEXT"
                + ") ";

        String CREATE_SAVE_CONFERENCE_PACK_CHARGE = "CREATE TABLE " + TABLE_SAVE_CONFERENCES_PACK_CHARGE
                + " (" + conference_pack_id + " TEXT,"
                + conference_pack_day + " TEXT,"
                + conference_pack_charge + " TEXT"
                + ") ";


        String CREATE_MY_CONFERENCE_PACK_CHARGE = "CREATE TABLE " + TABLE_MY_CONFERENCES_PACK_CHARGE
                + " (" + conference_pack_id + " TEXT,"
                + conference_pack_day + " TEXT,"
                + conference_pack_charge + " TEXT"
                + ") ";


        String CREATE_CONFERENCE_PACK_CHARGE = "CREATE TABLE " + TABLE_CONFERENCES_PACK_CHARGE
                + " (" + conference_pack_id + " TEXT,"
                + conference_pack_day + " TEXT,"
                + conference_pack_charge + " TEXT"
                + ") ";

        String CREATE_TABLE_AddPackageMYConference = "CREATE TABLE " + TABLE_AddPackageMYConference
                + " (" + conference_pack_id + " TEXT,"
                + conference_pack_day + " TEXT,"
                + conference_pack_charge + " TEXT"
                + ") ";

        String CREATE_TABLE_AddPackageGoingConference = "CREATE TABLE " + TABLE_AddPackageGoingConference
                + " (" + conference_pack_id + " TEXT,"
                + conference_pack_day + " TEXT,"
                + conference_pack_charge + " TEXT"
                + ") ";

        String CREATE_TABLE_AddForgeingPackageMYConference = "CREATE TABLE " + TABLE_AddForgeingPackageMYConference
                + " (" + conference_pack_id + " TEXT,"
                + conference_pack_day + " TEXT,"
                + conference_pack_charge + " TEXT"
                + ") ";

        String CREATE_TABLE_AddForgeingPackageGoingConference = "CREATE TABLE " + TABLE_AddForgeingPackageGoingConference
                + " (" + conference_pack_id + " TEXT,"
                + conference_pack_day + " TEXT,"
                + conference_pack_charge + " TEXT"
                + ") ";


        String CREATE_TABLE_CONFERENCES_PACK_CHARGE_FORGEIN = "CREATE TABLE " + TABLE_CONFERENCES_PACK_CHARGE_FORGEIN
                + " (" + conference_pack_id + " TEXT,"
                + conference_pack_day + " TEXT,"
                + conference_pack_charge + " TEXT"
                + ") ";


        String CREATE_CONFERENCE_PACKAGE = "CREATE TABLE " + TABLE_CONFERENCES_PACKAGE
                + " (" + package_id + " TEXT,"
                + package_name + " TEXT,"
                + package_total_price + " TEXT,"
                + package_sync_time + " TEXT,"
                + package_status + " TEXT"
                + ") ";


        String CREATE_MY_CONFERENCE_SPECIAL = "CREATE TABLE " + TABLE_MY_CONFERENCES_SPECIAL
                + " (" + conference_id + " TEXT,"
                + special_id + " TEXT,"
                + special_name + " TEXT"
                + ") ";

        String CREATE_MY_CONFERENCE_MEDICAL = "CREATE TABLE " + TABLE_MY_CONFERENCES_MEDICAL
                + " (" + conference_id + " TEXT,"
                + medical_id + " TEXT,"
                + medical_name + " TEXT"
                + ") ";
        String CREATE_SPECIALITY = "CREATE TABLE " + TABLE_SPECIALITY_DETAILS
                + " (" + profile_category_id + " TEXT,"
                + specialization_id + " TEXT,"
                + specialization_name + " TEXT"
                + ") ";

        String CREATE_CONFERENCE_IMAGE = "CREATE TABLE " + TABLE_CONFERENCES_SHOW_IMAGES
                + " (" + image_id + " TEXT,"
                + image_name + " TEXT"
                + ") ";

        String CREATE_TICKET_BOOKING = "CREATE TABLE " + TABLE_TICKET_BOOKING
                + " (" + booking_conference_id + " TEXT,"
                + conference_id + " TEXT,"
                + name + " TEXT,"
                + no_of_seats + " TEXT,"
                + order_id + " TEXT,"
                + payment_status + " TEXT,"
                + status + " TEXT,"
                + total_amount + " TEXT" + ") ";

        String CREATE_BOOKING_DETAILS = "CREATE TABLE " + TABLE_BOOKING_DETAILS
                + " (" + booking_conference_id + " TEXT,"
                + username + " TEXT,"
                + phone_no + " TEXT,"
                + email + " TEXT" + ") ";


        db.execSQL(CREATE_PAYMENT_PLAN);
        db.execSQL(CREATE_BOOKING_DETAILS);
        db.execSQL(CREATE_TICKET_BOOKING);
        db.execSQL(CREATE_CONFERENCE_DETAILS);
        db.execSQL(CREATE_MY_CONFERENCE_DETAILS);
        db.execSQL(CREATE_PUB_LIST_MASTER);
        db.execSQL(CREATE_SAVE_CONFERENCE_DETAILS);
        db.execSQL(CREATE_JOBS_MASTER);
        db.execSQL(CREATE_PRODUCT_MASTER);
        db.execSQL(CREATE_PRODUCT_DETAIL_MASTER);
        db.execSQL(CREATE_NOTIFICATION_MASTER);
        db.execSQL(CREATE_SELL_BUY_PRODUCT_MASTER_MASTER);
        db.execSQL(CREATE_RECOMMENDED_APPLIED_JOBS_MASTER);
        db.execSQL(OTHER_CATEGORY_MASTER);
        db.execSQL(METRO_CITY_MASTER);
        db.execSQL(CREATE_CHAT_NOTIFICATION_MASTER);
        db.execSQL(CASE_STUDY_LIST_MASTER);
        db.execSQL(CASE_STUDY_QUESTION_MASTER);
        db.execSQL(CASE_STUDY_OPTION_MASTER);
        db.execSQL(CASE_STUDY_ANSWER_MASTER);
        db.execSQL(CREATE_GOING_CONFERENCE_DETAILS);
        db.execSQL(CASE_STUDY_ATTEND_LIST_MASTER);
        db.execSQL(CASE_STUDY_QUESTION_ANS_MASTER);
        db.execSQL(CREATE_POSTED_JOBS_MASTER);
        db.execSQL(CREATE_CONFERENCE_PACKAGE);
        db.execSQL(CREATE_CONFERENCE_PACK_CHARGE);
        db.execSQL(CREATE_TABLE_AddPackageMYConference);
        db.execSQL(CREATE_TABLE_AddPackageGoingConference);
        db.execSQL(CREATE_TABLE_AddForgeingPackageMYConference);
        db.execSQL(CREATE_TABLE_AddForgeingPackageGoingConference);
        db.execSQL(CREATE_TABLE_CONFERENCES_PACK_CHARGE_FORGEIN);
        db.execSQL(CREATE_MY_CONFERENCE_PACK_CHARGE);
        db.execSQL(CREATE_MY_CONFERENCE_SPECIAL);
        db.execSQL(CREATE_MY_CONFERENCE_MEDICAL);
        db.execSQL(CREATE_SAVE_CONFERENCE_PACK_CHARGE);
        db.execSQL(CREATE_GOING_CONFERENCE_PACK_CHARGE);
        db.execSQL(CREATE_CONFERENCE_IMAGE);
        db.execSQL(CREATE_SPECIALITY);//
        db.execSQL(CREATE_BOOK_TICKET_TABLE);
        db.execSQL(CREATE_TABLE_COMMUNITY_FORUM_LIST);
        db.execSQL(CREATE_PAYMENT_CONFERENCE_DETAILS);
        db.execSQL(CREATE_TABLE_DELETE_PDF);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO: drop the tables if they are already created


        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                db.execSQL("DROP TABLE IF EXISTS " + c.getString(0));
                Log.d("Database_Helper_table", c.getString(0));
//                Toast.makeText(activityName.this, "Table Name=> "+c.getString(0), Toast.LENGTH_LONG).show();
                c.moveToNext();
            }
        }

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TICKET_BOOKING);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKING_DETAILS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_JOBS_MASTER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT_MASTER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT_DETAIL_MASTER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIFICATION_MASTER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SELL_BUY_PRODUCT_MASTER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECOMMENDED_APPLIED_JOBS_MASTER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OTHER_CATEGORY_MASTER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_METRO_CITY_MASTER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONFERENCES_SHOW_DETAILS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MY_CONFERENCES_SHOW_DETAILS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SAVE_CONFERENCES_SHOW_DETAILS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CASE_STUDY_LIST_MASTER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CASE_STUDY_ATTEND_LIST_MASTER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CASE_STUDY_QUESTION_MASTER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CASE_STUDY_OPTION_MASTER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CASE_STUDY_ANSWER_MASTER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GOING_CONFERENCES_SHOW_DETAILS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CASE_STUDY_QUESTION_ANS_MASTER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PAYMENT_PLAN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_POSTED_JOBS_MASTER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GETTING_PUBLISH_LIST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONFERENCES_PACKAGE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONFERENCES_PACK_CHARGE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AddPackageMYConference);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AddForgeingPackageMYConference);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MY_CONFERENCES_PACK_CHARGE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MY_CONFERENCES_SPECIAL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MY_CONFERENCES_MEDICAL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SAVE_CONFERENCES_PACK_CHARGE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GOING_CONFERENCES_PACK_CHARGE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONFERENCES_SHOW_IMAGES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SPECIALITY_DETAILS);
        db.execSQL("DROP TABLE IF EXISTS " + BOOK_TICKET_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMUNITY_FORUM_LIST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PAYMENT_CONFERENCE_DETAILS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DELETE_PDF);
        //TODO: creating the tables again
        onCreate(db);
    }


    public ArrayList<ConferenceDetailsModel> getConferenceCategory(String tableName, String category, String name, String filter_type) {

        ArrayList<ConferenceDetailsModel> subList = new ArrayList<>();

        // String selectQuery = "SELECT  * FROM " + tableName + " WHERE " + conference_name + " like '%" + category + "%'";
        String selectQuery = null;
        if (filter_type.equalsIgnoreCase("2")) {
            selectQuery = "SELECT  * FROM " + tableName + " WHERE " + conference_name + " like '%" + category + "%' AND " + particular_city_name + " = '" + name + "'";
        } else if (filter_type.equalsIgnoreCase("3")) {
            selectQuery = "SELECT  * FROM " + tableName + " WHERE " + conference_name + " like '%" + category + "%' AND " + particular_state_name + " = '" + name + "'";
        } else if (filter_type.equalsIgnoreCase("4")) {
            selectQuery = "SELECT  * FROM " + tableName + " WHERE " + conference_name + " like '%" + category + "%' AND " + particular_country_name + " = '" + name + "'";
        }
//        String selectQuery = "SELECT  * FROM " + tableName + " WHERE " + conference_name + " like '%" + category + "%' AND "  + particular_country_name + " = '" +  name + "'";
        Log.e("QUERY", selectQuery);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        while (cursor.moveToNext()) {
            ConferenceDetailsModel eventlist = new ConferenceDetailsModel();
            eventlist.setId(cursor.getString(0));
            eventlist.setConference_id(cursor.getString(1));
            eventlist.setConference_name(cursor.getString(2));
            eventlist.setFrom_date(cursor.getString(3));
            eventlist.setTo_date(cursor.getString(4));
            eventlist.setFrom_time(cursor.getString(5));
            eventlist.setTo_time(cursor.getString(6));
            eventlist.setVenue(cursor.getString(7));
            eventlist.setEvent_host_name(cursor.getString(8));
            eventlist.setSpeciality(cursor.getString(9));
            eventlist.setContact(cursor.getString(10));
            eventlist.setLocation(cursor.getString(11));
            eventlist.setLatitude(cursor.getString(12));
            eventlist.setLogitude(cursor.getString(13));
            eventlist.setBrochuers_file(cursor.getString(14));
            eventlist.setBrochuers_charge(cursor.getString(15));
            eventlist.setBrochuers_days(cursor.getString(16));
            eventlist.setRegistration_fee(cursor.getString(17));
            eventlist.setRegistration_days(cursor.getString(18));
            eventlist.setEvent_sponcer(cursor.getString(19));
            eventlist.setParticular_country_id(cursor.getString(20));
            eventlist.setParticular_country_name(cursor.getString(21));
            eventlist.setParticular_state_id(cursor.getString(22));
            eventlist.setParticular_state_name(cursor.getString(23));
            eventlist.setStatus(cursor.getString(24));
            eventlist.setAdd_date(cursor.getString(25));
            eventlist.setModify_date(cursor.getString(26));
            eventlist.setPayment_status(cursor.getString(27));
            eventlist.setShow_like(cursor.getString(28));
            eventlist.setParticular_city_id(cursor.getString(29));
            eventlist.setParticular_city_name(cursor.getString(30));
            eventlist.setAvailable_seat(cursor.getString(31));
            eventlist.setConference_description(cursor.getString(32));
            eventlist.setConference_pack_day(cursor.getString(33));
            eventlist.setConference_pack_charge(cursor.getString(34));
            eventlist.setConference_type_id(cursor.getString(35));
            eventlist.setMedical_profile_id(cursor.getString(36));
            eventlist.setCredit_earnings(cursor.getString(37));
            eventlist.setPayment_mode(cursor.getString(38));
            eventlist.setTotal_days_price(cursor.getString(39));
            eventlist.setAccomdation(cursor.getString(40));
            eventlist.setMember_concessions(cursor.getString(41));
            eventlist.setStudent_concessions(cursor.getString(42));
            eventlist.setPrice_hike_after_date(cursor.getString(43));
            eventlist.setPrice_hike_after_percentage(cursor.getString(44));
            subList.add(eventlist);
        }

        Log.e("No_of_column", "-==" + cursor.getColumnCount());
        // Log.e("No_of_Giftttt","-=="+cursor.getString(0));
        cursor.close();
        Log.e("SIZE", " SIZE " + subList.size());
        return subList;
    }

    public void AddPdfDelModel(PdfDelModel model, boolean insert) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(pdf_id, model.getPdf_id());
        contentValues.put(pdf_url, model.getPdf_url());
        contentValues.put(conference_id, model.getConference_d());
        //contentValues.put(status, model.getStatus());
        if (insert)
            db.insert(TABLE_DELETE_PDF, null, contentValues);
        else
            db.update(TABLE_DELETE_PDF, contentValues, conference_id + " = ?", new String[]{String.valueOf(model.getConference_d())});
    }

    public void ADD_TICKET_BOOKING(MyConferenceDetailsModel model, boolean insert) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(booking_conference_id, model.getBookingConferenceId());
        contentValues.put(conference_id, model.getConferenceId());
        contentValues.put(name, model.getName());
        contentValues.put(no_of_seats, model.getNoOfSeats());
        contentValues.put(order_id, model.getOrderId());
        contentValues.put(payment_status, model.getPaymentStatus());
        contentValues.put(status, model.getStatus());
        contentValues.put(total_amount, model.getTotalAmount());
        //contentValues.put(status, model.getStatus());
        if (insert)
            db.insert(TABLE_TICKET_BOOKING, null, contentValues);
        else
            db.update(TABLE_TICKET_BOOKING, contentValues, booking_conference_id + " = ?", new String[]{String.valueOf(model.getBookingConferenceId())});
    }

    public void ADD_BOOKING_DETAILS(BookingDetail model, boolean insert) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(booking_conference_id, model.getBookingConferenceId());
        contentValues.put(username, model.getUsername());
        contentValues.put(phone_no, model.getPhoneNo());
        contentValues.put(email, model.getEmail());
        if (insert)
            db.insert(TABLE_BOOKING_DETAILS, null, contentValues);
        else
            db.update(TABLE_BOOKING_DETAILS, contentValues, booking_conference_id + " = ?", new String[]{String.valueOf(model.getBookingConferenceId())});
    }

    public void AddSpeciality(JobSpecializationModel model, boolean insert) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(profile_category_id, model.getProfile_category_id());
        contentValues.put(specialization_id, model.getSpecialization_id());
        contentValues.put(specialization_name, model.getSpecialization_name());
        //contentValues.put(status, model.getStatus());
        if (insert)
            db.insert(TABLE_SPECIALITY_DETAILS, null, contentValues);
        else
            db.update(TABLE_SPECIALITY_DETAILS, contentValues, specialization_id + " = ?", new String[]{String.valueOf(model.getSpecialization_id())});
    }

    public void AddConferencePaymentDetails(ConferenceDetailsModel jobMasterModel, boolean insert) {
        SQLiteDatabase db = this.getWritableDatabase();
        //TODO: here we are putting the data using the content values
        ContentValues contentValues = new ContentValues();
        contentValues.put(id, jobMasterModel.getId());
        contentValues.put(conference_id, jobMasterModel.getConference_id());
        contentValues.put(conference_name, jobMasterModel.getConference_name());
        contentValues.put(from_date, jobMasterModel.getFrom_date());
        contentValues.put(to_date, jobMasterModel.getTo_date());
        contentValues.put(from_time, jobMasterModel.getFrom_time());
        contentValues.put(to_time, jobMasterModel.getTo_time());
        contentValues.put(venue, jobMasterModel.getVenue());
        contentValues.put(conference_type_id, jobMasterModel.getConference_type_id());
        contentValues.put(total_days_price, jobMasterModel.getTotal_days_price());
        contentValues.put(payment_mode, jobMasterModel.getPayment_mode());
        contentValues.put(with_notification_cost, jobMasterModel.getWith_notification_cost());
        contentValues.put(without_notification_cost, jobMasterModel.getWithout_notification_cost());
        contentValues.put(medical_profile_name, jobMasterModel.getMedical_profile_name());
        contentValues.put(target_audience_speciality, jobMasterModel.getTarget_audience_speciality());

        //TODO: inserting rows
        if (insert)
            db.insert(TABLE_PAYMENT_CONFERENCE_DETAILS, null, contentValues);
        else
            db.update(TABLE_PAYMENT_CONFERENCE_DETAILS, contentValues, id + " = ?", new String[]{String.valueOf(jobMasterModel.getId())});

        db.close(); //TODO: closing the database after the insert operation
        Log.e("conferencedetails", jobMasterModel.getId() + " = " + jobMasterModel.getId());

    }

    public void AddGoingConferenceDetails(ConferenceDetailsModel jobMasterModel, boolean insert) {
        SQLiteDatabase db = this.getWritableDatabase();
        //TODO: here we are putting the data using the content values
        ContentValues contentValues = new ContentValues();
        contentValues.put(id, jobMasterModel.getId());
        contentValues.put(conference_id, jobMasterModel.getConference_id());
        contentValues.put(conference_name, jobMasterModel.getConference_name());
        contentValues.put(from_date, jobMasterModel.getFrom_date());
        contentValues.put(to_date, jobMasterModel.getTo_date());
        contentValues.put(from_time, jobMasterModel.getFrom_time());
        contentValues.put(to_time, jobMasterModel.getTo_time());
        contentValues.put(venue, jobMasterModel.getVenue());
        contentValues.put(event_host_name, jobMasterModel.getEvent_host_name());
        contentValues.put(speciality, jobMasterModel.getSpeciality());
        contentValues.put(contact, jobMasterModel.getContact());
        contentValues.put(location, jobMasterModel.getLocation());
        contentValues.put(latitude, jobMasterModel.getLatitude());
        contentValues.put(logitude, jobMasterModel.getLogitude());
        contentValues.put(brochuers_file, jobMasterModel.getBrochuers_file());
        contentValues.put(brochuers_charge, jobMasterModel.getBrochuers_charge());
        contentValues.put(brochuers_days, jobMasterModel.getBrochuers_days());
        contentValues.put(registration_fee, jobMasterModel.getRegistration_fee());
        contentValues.put(registration_days, jobMasterModel.getRegistration_days());
        contentValues.put(event_sponcer, jobMasterModel.getEvent_sponcer());
        contentValues.put(particular_country_id, jobMasterModel.getParticular_country_id());
        contentValues.put(particular_country_name, jobMasterModel.getParticular_country_name());
        contentValues.put(particular_state_id, jobMasterModel.getParticular_state_id());
        contentValues.put(particular_state_name, jobMasterModel.getParticular_state_name());
        contentValues.put(status, jobMasterModel.getStatus());
        contentValues.put(add_date, jobMasterModel.getAdd_date());
        contentValues.put(modify_date, jobMasterModel.getModify_date());
        contentValues.put(payment_status, jobMasterModel.getPayment_status());
        contentValues.put(show_like, jobMasterModel.getShow_like());
        contentValues.put(particular_city_id, jobMasterModel.getParticular_city_id());
        contentValues.put(particular_city_name, jobMasterModel.getParticular_city_name());
        contentValues.put(available_seat, jobMasterModel.getAvailable_seat());
        contentValues.put(conference_description, jobMasterModel.getConference_description());
        contentValues.put(conference_pack_day, jobMasterModel.getConference_pack_day());
        contentValues.put(conference_pack_charge, jobMasterModel.getConference_pack_charge());
        contentValues.put(conference_type_id, jobMasterModel.getConference_type_id());
        contentValues.put(medical_profile_id, jobMasterModel.getMedical_profile_id());
        contentValues.put(credit_earnings, jobMasterModel.getCredit_earnings());
        contentValues.put(payment_mode, jobMasterModel.getPayment_mode());
        contentValues.put(total_days_price, jobMasterModel.getTotal_days_price());
        contentValues.put(accomdation, jobMasterModel.getAccomdation());
        contentValues.put(member_concessions, jobMasterModel.getMember_concessions());
        contentValues.put(student_concessions, jobMasterModel.getStudent_concessions());
        contentValues.put(price_hike_after_date, jobMasterModel.getPrice_hike_after_date());
        contentValues.put(price_hike_after_percentage, jobMasterModel.getPrice_hike_after_percentage());
        contentValues.put(medical_profile_name, jobMasterModel.getMedical_profile_name());
        contentValues.put(special_name, jobMasterModel.getTarget_audience_speciality());
        contentValues.put(department_id, jobMasterModel.getDepartment_id());
        contentValues.put(department_name, jobMasterModel.getDepartment_name());
        contentValues.put(discount_percentage, jobMasterModel.getDiscount_percentage());
        contentValues.put(discount_description, jobMasterModel.getDiscount_description());
        //TODO: inserting rows
        if (insert)
            db.insert(TABLE_GOING_CONFERENCES_SHOW_DETAILS, null, contentValues);
        else
            db.update(TABLE_GOING_CONFERENCES_SHOW_DETAILS, contentValues, id + " = ?", new String[]{String.valueOf(jobMasterModel.getId())});

        db.close(); //TODO: closing the database after the insert operation
        Log.e("confgoingdetails", jobMasterModel.getId() + " = " + jobMasterModel.getId());

    }

    public ArrayList<ConferenceDetailsModel> getGoingConferenceDetails(String tableName, String idGift) {

        ArrayList<ConferenceDetailsModel> subList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + tableName + " WHERE " + status + " = '" + idGift + "'";
        // String selectQuery = "SELECT  * FROM " + tableName + " ORDER BY " + modify_date + " DESC LIMIT " + 100000000;
        Log.e("QUERY", selectQuery);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        while (cursor.moveToNext()) {
            ConferenceDetailsModel eventlist = new ConferenceDetailsModel();
            eventlist.setId(cursor.getString(0));
            eventlist.setConference_id(cursor.getString(1));
            eventlist.setConference_name(cursor.getString(2));
            eventlist.setFrom_date(cursor.getString(3));
            eventlist.setTo_date(cursor.getString(4));
            eventlist.setFrom_time(cursor.getString(5));
            eventlist.setTo_time(cursor.getString(6));
            eventlist.setVenue(cursor.getString(7));
            eventlist.setEvent_host_name(cursor.getString(8));
            eventlist.setSpeciality(cursor.getString(9));
            eventlist.setContact(cursor.getString(10));
            eventlist.setLocation(cursor.getString(11));
            eventlist.setLatitude(cursor.getString(12));
            eventlist.setLogitude(cursor.getString(13));
            eventlist.setBrochuers_file(cursor.getString(14));
            eventlist.setBrochuers_charge(cursor.getString(15));
            eventlist.setBrochuers_days(cursor.getString(16));
            eventlist.setRegistration_fee(cursor.getString(17));
            eventlist.setRegistration_days(cursor.getString(18));
            eventlist.setEvent_sponcer(cursor.getString(19));
            eventlist.setParticular_country_id(cursor.getString(20));
            eventlist.setParticular_country_name(cursor.getString(21));
            eventlist.setParticular_state_id(cursor.getString(22));
            eventlist.setParticular_state_name(cursor.getString(23));
            eventlist.setStatus(cursor.getString(24));
            eventlist.setAdd_date(cursor.getString(25));
            eventlist.setModify_date(cursor.getString(26));
            eventlist.setPayment_status(cursor.getString(27));
            eventlist.setShow_like(cursor.getString(28));
            eventlist.setParticular_city_id(cursor.getString(29));
            eventlist.setParticular_city_name(cursor.getString(30));
            eventlist.setAvailable_seat(cursor.getString(31));
            eventlist.setConference_description(cursor.getString(32));
            eventlist.setConference_pack_day(cursor.getString(33));
            eventlist.setConference_pack_charge(cursor.getString(34));
            eventlist.setConference_type_id(cursor.getString(35));
            eventlist.setMedical_profile_id(cursor.getString(36));
            eventlist.setCredit_earnings(cursor.getString(37));
            eventlist.setPayment_mode(cursor.getString(38));
            eventlist.setTotal_days_price(cursor.getString(39));
            eventlist.setAccomdation(cursor.getString(40));
            eventlist.setMember_concessions(cursor.getString(41));
            eventlist.setStudent_concessions(cursor.getString(42));
            eventlist.setPrice_hike_after_date(cursor.getString(43));
            eventlist.setPrice_hike_after_percentage(cursor.getString(44));
            eventlist.setMedical_profile_name(cursor.getString(45));
            eventlist.setTarget_audience_speciality(cursor.getString(46));
            eventlist.setDepartment_id(cursor.getString(47));
            eventlist.setDepartment_name(cursor.getString(48));
            eventlist.setDiscount_percentage(cursor.getString(49));
            eventlist.setDiscount_description(cursor.getString(50));

            subList.add(eventlist);
        }

        Log.e("No_of_column", "-==" + cursor.getColumnCount());
        // Log.e("No_of_Giftttt","-=="+cursor.getString(0));
        cursor.close();
        Log.e("SIZE", " SIZE " + subList.size());
        return subList;
    }


    public ArrayList<ConferenceDetailsModel> getGoingConferenceDetails(String tableName, String idGift, String  conference_idStr) {

        ArrayList<ConferenceDetailsModel> subList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + tableName + " WHERE " + conference_id + " = '" + conference_idStr + "'";
        // String selectQuery = "SELECT  * FROM " + tableName + " ORDER BY " + modify_date + " DESC LIMIT " + 100000000;
        Log.e("QUERY", selectQuery);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        while (cursor.moveToNext()) {
            ConferenceDetailsModel eventlist = new ConferenceDetailsModel();
            eventlist.setId(cursor.getString(0));
            eventlist.setConference_id(cursor.getString(1));
            eventlist.setConference_name(cursor.getString(2));
            eventlist.setFrom_date(cursor.getString(3));
            eventlist.setTo_date(cursor.getString(4));
            eventlist.setFrom_time(cursor.getString(5));
            eventlist.setTo_time(cursor.getString(6));
            eventlist.setVenue(cursor.getString(7));
            eventlist.setEvent_host_name(cursor.getString(8));
            eventlist.setSpeciality(cursor.getString(9));
            eventlist.setContact(cursor.getString(10));
            eventlist.setLocation(cursor.getString(11));
            eventlist.setLatitude(cursor.getString(12));
            eventlist.setLogitude(cursor.getString(13));
            eventlist.setBrochuers_file(cursor.getString(14));
            eventlist.setBrochuers_charge(cursor.getString(15));
            eventlist.setBrochuers_days(cursor.getString(16));
            eventlist.setRegistration_fee(cursor.getString(17));
            eventlist.setRegistration_days(cursor.getString(18));
            eventlist.setEvent_sponcer(cursor.getString(19));
            eventlist.setParticular_country_id(cursor.getString(20));
            eventlist.setParticular_country_name(cursor.getString(21));
            eventlist.setParticular_state_id(cursor.getString(22));
            eventlist.setParticular_state_name(cursor.getString(23));
            eventlist.setStatus(cursor.getString(24));
            eventlist.setAdd_date(cursor.getString(25));
            eventlist.setModify_date(cursor.getString(26));
            eventlist.setPayment_status(cursor.getString(27));
            eventlist.setShow_like(cursor.getString(28));
            eventlist.setParticular_city_id(cursor.getString(29));
            eventlist.setParticular_city_name(cursor.getString(30));
            eventlist.setAvailable_seat(cursor.getString(31));
            eventlist.setConference_description(cursor.getString(32));
            eventlist.setConference_pack_day(cursor.getString(33));
            eventlist.setConference_pack_charge(cursor.getString(34));
            eventlist.setConference_type_id(cursor.getString(35));
            eventlist.setMedical_profile_id(cursor.getString(36));
            eventlist.setCredit_earnings(cursor.getString(37));
            eventlist.setPayment_mode(cursor.getString(38));
            eventlist.setTotal_days_price(cursor.getString(39));
            eventlist.setAccomdation(cursor.getString(40));
            eventlist.setMember_concessions(cursor.getString(41));
            eventlist.setStudent_concessions(cursor.getString(42));
            eventlist.setPrice_hike_after_date(cursor.getString(43));
            eventlist.setPrice_hike_after_percentage(cursor.getString(44));
            eventlist.setMedical_profile_name(cursor.getString(45));
            eventlist.setTarget_audience_speciality(cursor.getString(46));
            eventlist.setDepartment_id(cursor.getString(47));
            eventlist.setDepartment_name(cursor.getString(48));
            eventlist.setDiscount_percentage(cursor.getString(49));
            eventlist.setDiscount_description(cursor.getString(50));

            subList.add(eventlist);
        }

        Log.e("No_of_column", "-==" + cursor.getColumnCount());
        // Log.e("No_of_Giftttt","-=="+cursor.getString(0));
        cursor.close();
        Log.e("SIZE", " SIZE " + subList.size());
        return subList;
    }

    public void AddConferImage(ImageModel imageModel, boolean insert) {
        SQLiteDatabase db = this.getWritableDatabase();
        //TODO: here we are putting the data using the content values
        ContentValues contentValues = new ContentValues();
        contentValues.put(image_id, imageModel.getImage_id());
        contentValues.put(image_name, imageModel.getImage_name());

        //TODO: inserting rows
        Log.d("contentvalujes", contentValues + "");
        if (insert)
            db.insert(TABLE_CONFERENCES_SHOW_IMAGES, null, contentValues);
        else
            db.update(TABLE_CONFERENCES_SHOW_IMAGES, contentValues, image_id + " = ?", new String[]{String.valueOf(imageModel.getImage_id())});

        db.close(); //TODO: closing the database after the insert operation


    }

    public void AddMyConferenceDetails(ConferenceDetailsModel jobMasterModel, boolean insert) {
        SQLiteDatabase db = this.getWritableDatabase();
        //TODO: here we are putting the data using the content values
        ContentValues contentValues = new ContentValues();
        contentValues.put(id, jobMasterModel.getId());
        contentValues.put(conference_id, jobMasterModel.getConference_id());
        contentValues.put(conference_name, jobMasterModel.getConference_name());
        contentValues.put(from_date, jobMasterModel.getFrom_date());
        contentValues.put(to_date, jobMasterModel.getTo_date());
        contentValues.put(from_time, jobMasterModel.getFrom_time());
        contentValues.put(to_time, jobMasterModel.getTo_time());
        contentValues.put(venue, jobMasterModel.getVenue());
        contentValues.put(event_host_name, jobMasterModel.getEvent_host_name());
        contentValues.put(speciality, jobMasterModel.getSpeciality());
        contentValues.put(contact, jobMasterModel.getContact());
        contentValues.put(location, jobMasterModel.getLocation());
        contentValues.put(latitude, jobMasterModel.getLatitude());
        contentValues.put(logitude, jobMasterModel.getLogitude());
        contentValues.put(brochuers_file, jobMasterModel.getBrochuers_file());
        contentValues.put(brochuers_charge, jobMasterModel.getBrochuers_charge());
        contentValues.put(brochuers_days, jobMasterModel.getBrochuers_days());
        contentValues.put(registration_fee, jobMasterModel.getRegistration_fee());
        contentValues.put(registration_days, jobMasterModel.getRegistration_days());
        contentValues.put(event_sponcer, jobMasterModel.getEvent_sponcer());
        contentValues.put(particular_country_id, jobMasterModel.getParticular_country_id());
        contentValues.put(particular_country_name, jobMasterModel.getParticular_country_name());
        contentValues.put(particular_state_id, jobMasterModel.getParticular_state_id());
        contentValues.put(particular_state_name, jobMasterModel.getParticular_state_name());
        contentValues.put(status, jobMasterModel.getStatus());
        contentValues.put(add_date, jobMasterModel.getAdd_date());
        contentValues.put(modify_date, jobMasterModel.getModify_date());
        contentValues.put(registered, jobMasterModel.getRegistered());
        contentValues.put(interested, jobMasterModel.getInterested());
        contentValues.put(views, jobMasterModel.getViews());
        contentValues.put(cost, jobMasterModel.getCost());
        contentValues.put(duration, jobMasterModel.getDuration());
        contentValues.put(payment_status, jobMasterModel.getPayment_status());
        contentValues.put(show_like, jobMasterModel.getShow_like());
        contentValues.put(particular_city_id, jobMasterModel.getParticular_city_id());
        contentValues.put(particular_city_name, jobMasterModel.getParticular_city_name());
        contentValues.put(address_type, jobMasterModel.getAddress_type());
        contentValues.put(available_seat, jobMasterModel.getAvailable_seat());
        contentValues.put(conference_description, jobMasterModel.getConference_description());
        contentValues.put(conference_type_id, jobMasterModel.getConference_type_id());
        contentValues.put(medical_profile_id, jobMasterModel.getMedical_profile_id());
        contentValues.put(credit_earnings, jobMasterModel.getCredit_earnings());
        contentValues.put(payment_mode, jobMasterModel.getPayment_mode());
        contentValues.put(total_days_price, jobMasterModel.getTotal_days_price());
        contentValues.put(accomdation, jobMasterModel.getAccomdation());
        contentValues.put(member_concessions, jobMasterModel.getMember_concessions());
        contentValues.put(student_concessions, jobMasterModel.getStudent_concessions());
        contentValues.put(price_hike_after_date, jobMasterModel.getPrice_hike_after_date());
        contentValues.put(price_hike_after_percentage, jobMasterModel.getPrice_hike_after_percentage());
        contentValues.put(pdf_url, jobMasterModel.getPdf_url());
        contentValues.put(discount_percentage, jobMasterModel.getDiscount_percentage());
        contentValues.put(discount_description, jobMasterModel.getDiscount_description());
        contentValues.put(gst, jobMasterModel.getGst());
        contentValues.put(booking_stopped, jobMasterModel.getBooking_stopped());
        contentValues.put(medical_profile_name, jobMasterModel.getMedical_profile_name());
        contentValues.put(special_name, jobMasterModel.getTarget_audience_speciality());
        contentValues.put(department_id, jobMasterModel.getDepartment_id());
        contentValues.put(department_name, jobMasterModel.getDepartment_name());
        //TODO: inserting rows
        if (insert)
            db.insert(TABLE_MY_CONFERENCES_SHOW_DETAILS, null, contentValues);
        else
            db.update(TABLE_MY_CONFERENCES_SHOW_DETAILS, contentValues, id + " = ?", new String[]{String.valueOf(jobMasterModel.getId())});

        db.close(); //TODO: closing the database after the insert operation
        Log.e("conferencedetails", jobMasterModel.getId() + " = " + jobMasterModel.getId());

    }

    public void myConferenceEditBooingStop(String bookingStop, String id1) {
        SQLiteDatabase db = this.getWritableDatabase();
        //TODO: here we are putting the data using the content values
        ContentValues contentValues = new ContentValues();
        contentValues.put(booking_stopped, bookingStop);
        //TODO: inserting rows
        long l = db.update(TABLE_MY_CONFERENCES_SHOW_DETAILS, contentValues, id + " = ?", new String[]{String.valueOf(id1)});

        if (l > -1) {
            Log.d("bobobob", "bobobobo");
        }

        db.close(); //TODO: closing the database after the insert operation
        //Log.e("conferencedetails", jobMasterModel.getId() + " = " + jobMasterModel.getId());
    }


    public ArrayList<ConferenceDetailsModel> getMyConferenceDetails(String tableName, String idGift) {

        ArrayList<ConferenceDetailsModel> subList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + tableName + " WHERE " + status + " = '" + idGift + "'";
        //   String selectQuery = "SELECT  * FROM " + tableName + " ORDER BY " + modify_date + " DESC LIMIT " + 100000000;
        //String selectQuery = "SELECT  * FROM " + tableName;
        Log.e("QUERYYYY", selectQuery);

//        ArrayList<ConferenceDetailsModel> subList = new ArrayList<>();
//
//        String selectQuery = "SELECT  * FROM " + tableName + " ORDER BY " + modify_date + " DESC LIMIT " + 100000000;
//        Log.e("QUERY", selectQuery);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        while (cursor.moveToNext()) {
            ConferenceDetailsModel eventlist = new ConferenceDetailsModel();
            eventlist.setId(cursor.getString(0));
            eventlist.setConference_id(cursor.getString(1));
            eventlist.setConference_name(cursor.getString(2));
            eventlist.setFrom_date(cursor.getString(3));
            eventlist.setTo_date(cursor.getString(4));
            eventlist.setFrom_time(cursor.getString(5));
            eventlist.setTo_time(cursor.getString(6));
            eventlist.setVenue(cursor.getString(7));
            eventlist.setEvent_host_name(cursor.getString(8));
            eventlist.setSpeciality(cursor.getString(9));
            eventlist.setContact(cursor.getString(10));
            eventlist.setLocation(cursor.getString(11));
            eventlist.setLatitude(cursor.getString(12));
            eventlist.setLogitude(cursor.getString(13));
            eventlist.setBrochuers_file(cursor.getString(14));
            eventlist.setBrochuers_charge(cursor.getString(15));
            eventlist.setBrochuers_days(cursor.getString(16));
            eventlist.setRegistration_fee(cursor.getString(17));
            eventlist.setRegistration_days(cursor.getString(18));
            eventlist.setEvent_sponcer(cursor.getString(19));
            eventlist.setParticular_country_id(cursor.getString(20));
            eventlist.setParticular_country_name(cursor.getString(21));
            eventlist.setParticular_state_id(cursor.getString(22));
            eventlist.setParticular_state_name(cursor.getString(23));
            eventlist.setStatus(cursor.getString(24));
            eventlist.setAdd_date(cursor.getString(25));
            eventlist.setModify_date(cursor.getString(26));
            eventlist.setRegistered(cursor.getString(27));
            eventlist.setInterested(cursor.getString(28));
            eventlist.setViews(cursor.getString(29));
            eventlist.setCost(cursor.getString(30));
            eventlist.setDuration(cursor.getString(31));
            eventlist.setPayment_status(cursor.getString(32));
            eventlist.setShow_like(cursor.getString(33));
            eventlist.setParticular_city_id(cursor.getString(34));
            eventlist.setParticular_city_name(cursor.getString(35));
            eventlist.setAddress_type(cursor.getString(36));
            eventlist.setAvailable_seat(cursor.getString(37));
            eventlist.setConference_description(cursor.getString(38));
            eventlist.setConference_type_id(cursor.getString(39));
            eventlist.setMedical_profile_id(cursor.getString(40));
            eventlist.setCredit_earnings(cursor.getString(41));
            eventlist.setPayment_mode(cursor.getString(42));
            eventlist.setTotal_days_price(cursor.getString(43));
            eventlist.setAccomdation(cursor.getString(44));
            eventlist.setMember_concessions(cursor.getString(45));
            eventlist.setStudent_concessions(cursor.getString(46));
            eventlist.setPrice_hike_after_date(cursor.getString(47));
            eventlist.setPrice_hike_after_percentage(cursor.getString(48));
            eventlist.setPdf_url(cursor.getString(49));
            eventlist.setDiscount_percentage(cursor.getString(50));
            eventlist.setDiscount_description(cursor.getString(51));
            eventlist.setGst(cursor.getString(52));
            eventlist.setBooking_stopped(cursor.getString(53));
            eventlist.setMedical_profile_name(cursor.getString(54));
            eventlist.setTarget_audience_speciality(cursor.getString(55));
            eventlist.setDepartment_id(cursor.getString(56));
            eventlist.setDepartment_name(cursor.getString(57));
            subList.add(eventlist);
        }

        Log.e("No_of_column", "-==" + cursor.getColumnCount());
        // Log.e("No_of_Giftttt","-=="+cursor.getString(0));
        cursor.close();
        Log.e("SIZE", " SIZE " + subList.size());
        return subList;
    }


    public ArrayList<ConferenceDetailsModel> getMyConferenceDetails(String tableName, String idGift,String conference_idStr) {

        ArrayList<ConferenceDetailsModel> subList = new ArrayList<>();
        //String selectQuery = "SELECT * FROM " + tableName + " WHERE " + status + " = '" + idGift + "'";

        String selectQuery = "SELECT * FROM " + tableName + " WHERE " + conference_id + " = '" + conference_idStr+"' ";

        //   String selectQuery = "SELECT  * FROM " + tableName + " ORDER BY " + modify_date + " DESC LIMIT " + 100000000;
        //String selectQuery = "SELECT  * FROM " + tableName;
        Log.e("QUERYYYY", selectQuery);

//        ArrayList<ConferenceDetailsModel> subList = new ArrayList<>();
//
//        String selectQuery = "SELECT  * FROM " + tableName + " ORDER BY " + modify_date + " DESC LIMIT " + 100000000;
//        Log.e("QUERY", selectQuery);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        while (cursor.moveToNext()) {
            ConferenceDetailsModel eventlist = new ConferenceDetailsModel();
            eventlist.setId(cursor.getString(0));
            eventlist.setConference_id(cursor.getString(1));
            eventlist.setConference_name(cursor.getString(2));
            eventlist.setFrom_date(cursor.getString(3));
            eventlist.setTo_date(cursor.getString(4));
            eventlist.setFrom_time(cursor.getString(5));
            eventlist.setTo_time(cursor.getString(6));
            eventlist.setVenue(cursor.getString(7));
            eventlist.setEvent_host_name(cursor.getString(8));
            eventlist.setSpeciality(cursor.getString(9));
            eventlist.setContact(cursor.getString(10));
            eventlist.setLocation(cursor.getString(11));
            eventlist.setLatitude(cursor.getString(12));
            eventlist.setLogitude(cursor.getString(13));
            eventlist.setBrochuers_file(cursor.getString(14));
            eventlist.setBrochuers_charge(cursor.getString(15));
            eventlist.setBrochuers_days(cursor.getString(16));
            eventlist.setRegistration_fee(cursor.getString(17));
            eventlist.setRegistration_days(cursor.getString(18));
            eventlist.setEvent_sponcer(cursor.getString(19));
            eventlist.setParticular_country_id(cursor.getString(20));
            eventlist.setParticular_country_name(cursor.getString(21));
            eventlist.setParticular_state_id(cursor.getString(22));
            eventlist.setParticular_state_name(cursor.getString(23));
            eventlist.setStatus(cursor.getString(24));
            eventlist.setAdd_date(cursor.getString(25));
            eventlist.setModify_date(cursor.getString(26));
            eventlist.setRegistered(cursor.getString(27));
            eventlist.setInterested(cursor.getString(28));
            eventlist.setViews(cursor.getString(29));
            eventlist.setCost(cursor.getString(30));
            eventlist.setDuration(cursor.getString(31));
            eventlist.setPayment_status(cursor.getString(32));
            eventlist.setShow_like(cursor.getString(33));
            eventlist.setParticular_city_id(cursor.getString(34));
            eventlist.setParticular_city_name(cursor.getString(35));
            eventlist.setAddress_type(cursor.getString(36));
            eventlist.setAvailable_seat(cursor.getString(37));
            eventlist.setConference_description(cursor.getString(38));
            eventlist.setConference_type_id(cursor.getString(39));
            eventlist.setMedical_profile_id(cursor.getString(40));
            eventlist.setCredit_earnings(cursor.getString(41));
            eventlist.setPayment_mode(cursor.getString(42));
            eventlist.setTotal_days_price(cursor.getString(43));
            eventlist.setAccomdation(cursor.getString(44));
            eventlist.setMember_concessions(cursor.getString(45));
            eventlist.setStudent_concessions(cursor.getString(46));
            eventlist.setPrice_hike_after_date(cursor.getString(47));
            eventlist.setPrice_hike_after_percentage(cursor.getString(48));
            eventlist.setPdf_url(cursor.getString(49));
            eventlist.setDiscount_percentage(cursor.getString(50));
            eventlist.setDiscount_description(cursor.getString(51));
            eventlist.setGst(cursor.getString(52));
            eventlist.setBooking_stopped(cursor.getString(53));
            eventlist.setMedical_profile_name(cursor.getString(54));
            eventlist.setTarget_audience_speciality(cursor.getString(55));
            eventlist.setDepartment_id(cursor.getString(56));
            eventlist.setDepartment_name(cursor.getString(57));
            subList.add(eventlist);
        }

        Log.e("No_of_column", "-==" + cursor.getColumnCount());
        // Log.e("No_of_Giftttt","-=="+cursor.getString(0));
        cursor.close();
        Log.e("SIZE", " SIZE " + subList.size());
        return subList;
    }

    public void AddCommunityForumList(CommunityForumModel model, boolean insert) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(id, model.getId());
        contentValues.put(title, model.getTitle());
        contentValues.put(specialization_name, model.getSpecialization_name());
        contentValues.put(description, model.getDescription());
        contentValues.put(votes, model.getVotes());
        contentValues.put(comments, model.getComments());
        contentValues.put(views, model.getViews());
        contentValues.put(add_date, model.getAdd_date());
        contentValues.put(modify_date, model.getModify_date());
        if (insert) {
            db.insert(TABLE_COMMUNITY_FORUM_LIST, null, contentValues);
        } else {
            db.update(TABLE_COMMUNITY_FORUM_LIST, contentValues, id + " = ?", new String[]{String.valueOf(model.getId())});
        }

    }

    public ArrayList<CommunityForumModel> getCommunityForumList(String tableName) {
        ArrayList<CommunityForumModel> subList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + tableName;
        Log.e("QUERY", selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        while (cursor.moveToNext()) {
            CommunityForumModel forumModel = new CommunityForumModel();
            forumModel.setId(cursor.getString(0));
            forumModel.setTitle(cursor.getString(1));
            forumModel.setSpecialization_name(cursor.getString(2));
            forumModel.setDescription(cursor.getString(3));
            forumModel.setVotes(cursor.getString(4));
            forumModel.setComments(cursor.getString(5));
            forumModel.setViews(cursor.getString(6));
            forumModel.setAdd_date(cursor.getString(7));
            forumModel.setModify_date(cursor.getString(8));
            subList.add(forumModel);
        }
        cursor.close();
        return subList;
    }

    public ArrayList<MyConferenceDetailsModel> getTicketBookingList(String tableName) {
        ArrayList<MyConferenceDetailsModel> subList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + tableName;
        Log.e("QUERY", selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        while (cursor.moveToNext()) {
            MyConferenceDetailsModel forumModel = new MyConferenceDetailsModel();
            forumModel.setBookingConferenceId(cursor.getString(0));
            forumModel.setConferenceId(cursor.getString(1));
            forumModel.setName(cursor.getString(2));
            forumModel.setNoOfSeats(cursor.getString(3));
            forumModel.setOrderId(cursor.getString(4));
            forumModel.setPaymentStatus(cursor.getString(5));
            forumModel.setStatus(cursor.getString(6));
            forumModel.setTotalAmount(cursor.getString(7));
            subList.add(forumModel);
        }
        cursor.close();
        return subList;
    }

    public ArrayList<BookingDetail> getBookingDetails(String tableName, String id) {
        ArrayList<BookingDetail> subList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + tableName + " WHERE " + booking_conference_id + " = '" + id + "'";
        Log.e("QUERY", selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        while (cursor.moveToNext()) {
            BookingDetail forumModel = new BookingDetail();
            forumModel.setBookingConferenceId(cursor.getString(0));
            forumModel.setUsername(cursor.getString(1));
            forumModel.setPhoneNo(cursor.getString(2));
            forumModel.setEmail(cursor.getString(3));
            subList.add(forumModel);
        }
        cursor.close();
        return subList;
    }

    public ArrayList<PdfDelModel> getPdfDel(String tableName, String id) {
        ArrayList<PdfDelModel> subList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + tableName + " WHERE " + conference_id + " = '" + id + "'";
        // String selectQuery = "SELECT * FROM " + tableName;
        Log.e("QUERY", selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        while (cursor.moveToNext()) {
            PdfDelModel ob = new PdfDelModel();
            ob.setPdf_id(cursor.getString(0));
            ob.setPdf_url(cursor.getString(1));
            ob.setConference_d(cursor.getString(2));
            subList.add(ob);
        }
        cursor.close();
        return subList;
    }


    public ArrayList<JobSpecializationModel> getSpeciality(String tableName, String id) {
        ArrayList<JobSpecializationModel> subList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + tableName + " WHERE " + specialization_id + " = " + id + "";
        // String selectQuery = "SELECT * FROM " + tableName;
        Log.e("QUERY", selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        while (cursor.moveToNext()) {
            JobSpecializationModel ob = new JobSpecializationModel();
            ob.setProfile_category_id(cursor.getString(0));
            ob.setSpecialization_id(cursor.getString(1));
            ob.setSpecialization_name(cursor.getString(2));
//            ob.setStatus(cursor.getString(3));
            subList.add(ob);
        }
        cursor.close();
        return subList;
    }

    public ArrayList<ConferenceDetailsModel> getConferenceCategory1(String tableName, String category) {

        ArrayList<ConferenceDetailsModel> subList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + tableName + " WHERE " + conference_name + " like '%" + category + "%'";
//        String selectQuery=null;
//        if(filter_type.equalsIgnoreCase("2"))
//        {
//            selectQuery = "SELECT  * FROM " + tableName + " WHERE " + conference_name + " like '%" + category + "%' AND "  + particular_city_name + " = '" +  name + "'";
//        }else if(filter_type.equalsIgnoreCase("3"))
//        {
//            selectQuery = "SELECT  * FROM " + tableName + " WHERE " + conference_name + " like '%" + category + "%' AND "  + particular_state_name + " = '" +  name + "'";
//        }else if (filter_type.equalsIgnoreCase("4"))
//        {
//            selectQuery = "SELECT  * FROM " + tableName + " WHERE " + conference_name + " like '%" + category + "%' AND "  + particular_country_name + " = '" +  name + "'";
//        }
//        String selectQuery = "SELECT  * FROM " + tableName + " WHERE " + conference_name + " like '%" + category + "%' AND "  + particular_country_name + " = '" +  name + "'";
        Log.e("QUERY", selectQuery);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        while (cursor.moveToNext()) {
            ConferenceDetailsModel eventlist = new ConferenceDetailsModel();
            eventlist.setId(cursor.getString(0));
            eventlist.setConference_id(cursor.getString(1));
            eventlist.setConference_name(cursor.getString(2));
            eventlist.setFrom_date(cursor.getString(3));
            eventlist.setTo_date(cursor.getString(4));
            eventlist.setFrom_time(cursor.getString(5));
            eventlist.setTo_time(cursor.getString(6));
            eventlist.setVenue(cursor.getString(7));
            eventlist.setEvent_host_name(cursor.getString(8));
            eventlist.setSpeciality(cursor.getString(9));
            eventlist.setContact(cursor.getString(10));
            eventlist.setLocation(cursor.getString(11));
            eventlist.setLatitude(cursor.getString(12));
            eventlist.setLogitude(cursor.getString(13));
            eventlist.setBrochuers_file(cursor.getString(14));
            eventlist.setBrochuers_charge(cursor.getString(15));
            eventlist.setBrochuers_days(cursor.getString(16));
            eventlist.setRegistration_fee(cursor.getString(17));
            eventlist.setRegistration_days(cursor.getString(18));
            eventlist.setEvent_sponcer(cursor.getString(19));
            eventlist.setParticular_country_id(cursor.getString(20));
            eventlist.setParticular_country_name(cursor.getString(21));
            eventlist.setParticular_state_id(cursor.getString(22));
            eventlist.setParticular_state_name(cursor.getString(23));
            eventlist.setStatus(cursor.getString(24));
            eventlist.setAdd_date(cursor.getString(25));
            eventlist.setModify_date(cursor.getString(26));
            eventlist.setPayment_status(cursor.getString(27));
            eventlist.setShow_like(cursor.getString(28));
            eventlist.setParticular_city_id(cursor.getString(29));
            eventlist.setParticular_city_name(cursor.getString(30));
            eventlist.setAvailable_seat(cursor.getString(31));
            eventlist.setConference_description(cursor.getString(32));
            eventlist.setConference_pack_day(cursor.getString(33));
            eventlist.setConference_pack_charge(cursor.getString(34));
            eventlist.setConference_type_id(cursor.getString(35));
            eventlist.setMedical_profile_id(cursor.getString(36));
            eventlist.setCredit_earnings(cursor.getString(37));
            eventlist.setPayment_mode(cursor.getString(38));
            eventlist.setTotal_days_price(cursor.getString(39));
            eventlist.setAccomdation(cursor.getString(40));
            eventlist.setMember_concessions(cursor.getString(41));
            eventlist.setStudent_concessions(cursor.getString(42));
            eventlist.setPrice_hike_after_date(cursor.getString(43));
            eventlist.setPrice_hike_after_percentage(cursor.getString(44));
            subList.add(eventlist);
        }

        Log.e("No_of_column", "-==" + cursor.getColumnCount());
        // Log.e("No_of_Giftttt","-=="+cursor.getString(0));
        cursor.close();
        Log.e("SIZE", " SIZE " + subList.size());
        return subList;
    }


    public void AddPaymentPlan(JobPaymentModel jobMasterModel, boolean insert) {
        SQLiteDatabase db = this.getWritableDatabase();
        //TODO: here we are putting the data using the content values
        ContentValues contentValues = new ContentValues();
        contentValues.put(id, jobMasterModel.getJob_id());
        contentValues.put(play_type, jobMasterModel.getJob_type());
        contentValues.put(job_shortlist, jobMasterModel.getJob_details());
        contentValues.put(price, jobMasterModel.getJob_pay());
        contentValues.put(add_date, jobMasterModel.getAdd_date());
        contentValues.put(percentage, jobMasterModel.getPercentage());
        contentValues.put(ctc, jobMasterModel.getCtc());
        contentValues.put(job_id, jobMasterModel.getJob_id_());
        contentValues.put(job_title_id, jobMasterModel.getJob_title_id());
        //TODO: inserting rows
        Log.d("contentvalujes", contentValues + "");
        if (insert)
            db.insert(TABLE_PAYMENT_PLAN, null, contentValues);
        else
            db.update(TABLE_PAYMENT_PLAN, contentValues, id + " = ?", new String[]{String.valueOf(jobMasterModel.getJob_id())});

        db.close(); //TODO: closing the database after the insert operation
        Log.e("jobbbbbbbbb", jobMasterModel.getJob_id() + " = " + jobMasterModel.getJob_id());

    }

    public ArrayList<JobPaymentModel> getJobPlanDetails(String tableName, String plan_type, String title_ID) {
        ArrayList<JobPaymentModel> subList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + tableName + " WHERE " + play_type + " = " + plan_type + " AND " + job_title_id + " = " + title_ID + "";
        // String selectQuery = "SELECT * FROM " + tableName;
        Log.e("QUERY", selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        while (cursor.moveToNext()) {
            JobPaymentModel ob = new JobPaymentModel();
            ob.setJob_id(cursor.getString(0));
            ob.setJob_type(cursor.getString(1));
            ob.setJob_details(cursor.getString(2));
            ob.setJob_pay(cursor.getString(3));
            ob.setAdd_date(cursor.getString(4));
            ob.setPercentage(cursor.getString(5));
            ob.setCtc(cursor.getString(6));
            ob.setJob_id_(cursor.getString(7));
            ob.setJob_title_id(cursor.getString(8));
            subList.add(ob);
        }
        cursor.close();
        return subList;
    }

    //TODO: adding dummy data
    public void AddJobMaster(JobMasterModel jobMasterModel, boolean insert) {
        SQLiteDatabase db = this.getWritableDatabase();
        //TODO: here we are putting the data using the content values
        ContentValues contentValues = new ContentValues();
        contentValues.put(id, jobMasterModel.getId());
        contentValues.put(KEY_JOB_ID, jobMasterModel.getJob_id());
        contentValues.put(medical_profile_id, jobMasterModel.getMedical_profile_id());
        contentValues.put(medical_profile_name, jobMasterModel.getMedical_profile_name());
        contentValues.put(KEY_JOB_TITLE, jobMasterModel.getJob_title());
        contentValues.put(KEY_JOB_TYPE, jobMasterModel.getJob_type());
        contentValues.put(job_type_name, jobMasterModel.getJob_type_name());
        contentValues.put(specialization_id, jobMasterModel.getSpecialization_id());
        contentValues.put(specialization_name, jobMasterModel.getSpecialization_name());
        contentValues.put(sub_specialization_id, jobMasterModel.getSub_specialization_id());
        contentValues.put(sub_specialization_name, jobMasterModel.getSub_specialization_name());
        contentValues.put(KEY_YEAR_EXP, jobMasterModel.getYear_of_experience());
        contentValues.put(KEY_CURRENT_CTC, jobMasterModel.getCurrent_ctc());
        contentValues.put(KEY_EXPECTED_CTC, jobMasterModel.getExpected_ctc());
        contentValues.put(KEY_CURRENT_EMPLOYER, jobMasterModel.getCurrent_employer());
        contentValues.put(location, jobMasterModel.getLocation());
        contentValues.put(KEY_PREFERRED_LOCATION, jobMasterModel.getPreferred_location());
        contentValues.put(KEY_RESUME, jobMasterModel.getResume());
        contentValues.put(job_description, jobMasterModel.getJob_description());
        contentValues.put(KEY_SKILLS_REQUIRED, jobMasterModel.getSkills_required());
        contentValues.put(KEY_ADD_DATE, jobMasterModel.getAdd_date());
        contentValues.put(KEY_MODIFY_DATE, jobMasterModel.getModify_date());
        contentValues.put(post_req_status, jobMasterModel.getPost_req_status());
        contentValues.put(department_id, jobMasterModel.getDepartment_id());
        contentValues.put(department_name, jobMasterModel.getDepartment_name());
        contentValues.put(KEY_STATUS, jobMasterModel.getStatus());
        //TODO: inserting rows
        if (insert)
            db.insert(TABLE_JOBS_MASTER, null, contentValues);
        else
            db.update(TABLE_JOBS_MASTER, contentValues, id + " = ?", new String[]{String.valueOf(jobMasterModel.getId())});

        db.close(); //TODO: closing the database after the insert operation
        Log.e("job", jobMasterModel.getId() + " = " + jobMasterModel.getId());

    }

    public void AddPostedJobMaster(JobMasterModel jobMasterModel, boolean insert) {
        SQLiteDatabase db = this.getWritableDatabase();
        //TODO: here we are putting the data using the content values
        ContentValues contentValues = new ContentValues();
        contentValues.put(id, jobMasterModel.getId());
        contentValues.put(KEY_JOB_ID, jobMasterModel.getJob_id());
        contentValues.put(medical_profile_id, jobMasterModel.getMedical_profile_id());
        contentValues.put(medical_profile_name, jobMasterModel.getMedical_profile_name());
        contentValues.put(KEY_JOB_TITLE, jobMasterModel.getJob_title());
        contentValues.put(KEY_JOB_TYPE, jobMasterModel.getJob_type());
        contentValues.put(job_type_name, jobMasterModel.getJob_type_name());
        contentValues.put(specialization_id, jobMasterModel.getSpecialization_id());
        contentValues.put(specialization_name, jobMasterModel.getSpecialization_name());
        contentValues.put(sub_specialization_id, jobMasterModel.getSub_specialization_id());
        contentValues.put(sub_specialization_name, jobMasterModel.getSub_specialization_name());
        contentValues.put(KEY_YEAR_EXP, jobMasterModel.getYear_of_experience());
        contentValues.put(KEY_CURRENT_CTC, jobMasterModel.getCurrent_ctc());
        contentValues.put(KEY_EXPECTED_CTC, jobMasterModel.getExpected_ctc());
        contentValues.put(KEY_CURRENT_EMPLOYER, jobMasterModel.getCurrent_employer());
        contentValues.put(location, jobMasterModel.getLocation());
        contentValues.put(KEY_PREFERRED_LOCATION, jobMasterModel.getPreferred_location());
        contentValues.put(KEY_RESUME, jobMasterModel.getResume());
        contentValues.put(job_description, jobMasterModel.getJob_description());
        contentValues.put(KEY_SKILLS_REQUIRED, jobMasterModel.getSkills_required());
        contentValues.put(KEY_ADD_DATE, jobMasterModel.getAdd_date());
        contentValues.put(KEY_MODIFY_DATE, jobMasterModel.getModify_date());
        contentValues.put(post_req_status, jobMasterModel.getPost_req_status());
        contentValues.put(department_id, jobMasterModel.getDepartment_id());
        contentValues.put(department_name, jobMasterModel.getDepartment_name());
        contentValues.put(KEY_STATUS, jobMasterModel.getStatus());
        contentValues.put(job_title_id, jobMasterModel.getJob_title_id());
        //TODO: inserting rows
        if (insert)
            db.insert(TABLE_POSTED_JOBS_MASTER, null, contentValues);
        else
            db.update(TABLE_POSTED_JOBS_MASTER, contentValues, id + " = ?", new String[]{String.valueOf(jobMasterModel.getId())});

        db.close(); //TODO: closing the database after the insert operation
        Log.e("job", jobMasterModel.getId() + " = " + jobMasterModel.getId());

    }

    public void AddGetPubListMaster(MedicalWritingModel jobMasterModel, boolean insert) {
        SQLiteDatabase db = this.getWritableDatabase();
        //TODO: here we are putting the data using the content values
        ContentValues contentValues = new ContentValues();
        contentValues.put(id, jobMasterModel.getId());
        contentValues.put(title, jobMasterModel.getTitle());
        contentValues.put(image, jobMasterModel.getImage());
        contentValues.put(published_year, jobMasterModel.getPublished_year());
        contentValues.put(url, jobMasterModel.getUrl());
        contentValues.put(add_date, jobMasterModel.getAdd_date());
        contentValues.put(modify_date, jobMasterModel.getModify_date());
        contentValues.put(status, jobMasterModel.getStatus());
        //TODO: inserting rows
        if (insert)
            db.insert(TABLE_GETTING_PUBLISH_LIST, null, contentValues);
        else
            db.update(TABLE_GETTING_PUBLISH_LIST, contentValues, id + " = ?", new String[]{String.valueOf(jobMasterModel.getId())});

        db.close(); //TODO: closing the database after the insert operation
        Log.e("publishList", jobMasterModel.getId() + " = " + jobMasterModel.getId());

    }


    //    public void AddMetroCityMaster(MetroCityModel jobMasterModel, boolean insert){
//            SQLiteDatabase db = this.getWritableDatabase();
//            //TODO: here we are putting the data using the content values
//            ContentValues contentValues = new ContentValues();
//            contentValues.put(id_city,jobMasterModel.getId_city());
//            contentValues.put(city_name,jobMasterModel.getProfile_category_name());
//            contentValues.put(status,jobMasterModel.getStatus());
//            //TODO: inserting rows
//        Log.d("contentvalujes",contentValues+"");
//        if (insert)
//            db.insert(TABLE_METRO_CITY_MASTER, null, contentValues);
//        else
//            db.update(TABLE_METRO_CITY_MASTER, contentValues, id_city + " = ?", new String[]{String.valueOf(jobMasterModel.getId_city())});
//
//        db.close(); //TODO: closing the database after the insert operation
//        Log.e("jobbbb", jobMasterModel.getId_city() + " = " + jobMasterModel.getId_city());
//
//    }
    public void AddRecommendedAppliedJobMaster(JobMasterModel jobMasterModel, boolean insert) {
        SQLiteDatabase db = this.getWritableDatabase();
        //TODO: here we are putting the data using the content values
        ContentValues contentValues = new ContentValues();
        contentValues.put(id, jobMasterModel.getId());
        contentValues.put(KEY_JOB_ID, jobMasterModel.getJob_id());
        contentValues.put(medical_profile_id, jobMasterModel.getMedical_profile_id());
        contentValues.put(medical_profile_name, jobMasterModel.getMedical_profile_name());
        contentValues.put(KEY_JOB_TITLE, jobMasterModel.getJob_title());
        contentValues.put(KEY_JOB_TYPE, jobMasterModel.getJob_type());
        contentValues.put(job_type_name, jobMasterModel.getJob_type_name());
        contentValues.put(specialization_id, jobMasterModel.getSpecialization_id());
        contentValues.put(specialization_name, jobMasterModel.getSpecialization_name());
        contentValues.put(sub_specialization_id, jobMasterModel.getSub_specialization_id());
        contentValues.put(sub_specialization_name, jobMasterModel.getSub_specialization_name());
        contentValues.put(KEY_YEAR_EXP, jobMasterModel.getYear_of_experience());
        contentValues.put(KEY_CURRENT_CTC, jobMasterModel.getCurrent_ctc());
        contentValues.put(KEY_EXPECTED_CTC, jobMasterModel.getExpected_ctc());
        contentValues.put(KEY_CURRENT_EMPLOYER, jobMasterModel.getCurrent_employer());
        contentValues.put(location, jobMasterModel.getLocation());
        contentValues.put(KEY_PREFERRED_LOCATION, jobMasterModel.getPreferred_location());
        contentValues.put(KEY_RESUME, jobMasterModel.getResume());
        contentValues.put(job_description, jobMasterModel.getJob_description());
        contentValues.put(KEY_SKILLS_REQUIRED, jobMasterModel.getSkills_required());
        contentValues.put(KEY_ADD_DATE, jobMasterModel.getAdd_date());
        contentValues.put(KEY_MODIFY_DATE, jobMasterModel.getModify_date());
        contentValues.put(post_req_status, jobMasterModel.getPost_req_status());
        contentValues.put(department_id, jobMasterModel.getDepartment_id());
        contentValues.put(department_name, jobMasterModel.getDepartment_name());
        contentValues.put(applied_date, jobMasterModel.getApplied_date());
        contentValues.put(applied_status, jobMasterModel.getApplied_status());
        contentValues.put(shortlist_status, jobMasterModel.getShortlist_status());
        contentValues.put(KEY_STATUS, jobMasterModel.getStatus());
        contentValues.put(app_jobId, jobMasterModel.getApp_jobId());
        contentValues.put(app_medical_profile_id, jobMasterModel.getApp_medical_profile_id());
        contentValues.put(app_medical_profile_name, jobMasterModel.getApp_medical_profile_name());
        contentValues.put(app_job_title, jobMasterModel.getApp_job_title());
        contentValues.put(app_job_title_id, jobMasterModel.getApp_job_title_id());
        contentValues.put(app_specialization_id, jobMasterModel.getApp_specialization_id());
        contentValues.put(app_specialization_name, jobMasterModel.getApp_specialization_name());
        contentValues.put(app_sub_specialization_id, jobMasterModel.getApp_sub_specialization_id());
        contentValues.put(app_sub_specialization_name, jobMasterModel.getApp_sub_specialization_name());
        contentValues.put(app_year_of_experience, jobMasterModel.getApp_year_of_experience());
        contentValues.put(app_current_ctc, jobMasterModel.getApp_current_ctc());
        contentValues.put(app_expected_ctc, jobMasterModel.getApp_expected_ctc());
        contentValues.put(resume_description, jobMasterModel.getResume_description());
        contentValues.put(app_department_id, jobMasterModel.getApp_department_id());
        contentValues.put(app_department_name, jobMasterModel.getApp_department_name());
        contentValues.put(name, jobMasterModel.getName());
        contentValues.put(KEY_JOB_TITLE_ID, jobMasterModel.getJob_title_id());
        //TODO: inserting rows
        if (insert)
            db.insert(TABLE_RECOMMENDED_APPLIED_JOBS_MASTER, null, contentValues);
        else
            db.update(TABLE_RECOMMENDED_APPLIED_JOBS_MASTER, contentValues, id + " = ?", new String[]{String.valueOf(jobMasterModel.getId())});

        db.close(); //TODO: closing the database after the insert operation
        Log.e("job", jobMasterModel.getId() + " = " + jobMasterModel.getId());

    }

    public ArrayList<ConferenceDetailsModel> getPaymentConferenceDetails(String tableName, String idGift) {

        ArrayList<ConferenceDetailsModel> subList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + tableName + " WHERE " + conference_id + " = '" + idGift + "'";
        //  String selectQuery = "SELECT  * FROM " + tableName + " ORDER BY " + modify_date + " DESC LIMIT " + 100000000;
        Log.e("QUERY", selectQuery);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        while (cursor.moveToNext()) {
            ConferenceDetailsModel eventlist = new ConferenceDetailsModel();
            eventlist.setId(cursor.getString(0));
            eventlist.setConference_id(cursor.getString(1));
            eventlist.setConference_name(cursor.getString(2));
            eventlist.setFrom_date(cursor.getString(3));
            eventlist.setTo_date(cursor.getString(4));
            eventlist.setFrom_time(cursor.getString(5));
            eventlist.setTo_time(cursor.getString(6));
            eventlist.setVenue(cursor.getString(7));
            eventlist.setConference_type_id(cursor.getString(8));
            eventlist.setTotal_days_price(cursor.getString(9));
            eventlist.setPayment_mode(cursor.getString(10));
            eventlist.setWith_notification_cost(cursor.getString(11));
            eventlist.setWithout_notification_cost(cursor.getString(12));
            eventlist.setMedical_profile_name(cursor.getString(13));
            eventlist.setTarget_audience_speciality(cursor.getString(14));
            subList.add(eventlist);
        }

        Log.e("No_of_column", "-==" + cursor.getColumnCount());
        // Log.e("No_of_Giftttt","-=="+cursor.getString(0));
        cursor.close();
        Log.e("SIZE", " SIZE " + subList.size());
        return subList;
    }

    public void AddProductMaster(DashBoardProductModel jobMasterModel, boolean insert) {
        SQLiteDatabase db = this.getWritableDatabase();
        //TODO: here we are putting the data using the content values
        ContentValues contentValues = new ContentValues();
        contentValues.put(product_id, jobMasterModel.getProduct_id());
        contentValues.put(product_name, jobMasterModel.getProduct_name());
        contentValues.put(category_id, jobMasterModel.getCategory_id());
        contentValues.put(category_name, jobMasterModel.getCategory_name());
        contentValues.put(price, jobMasterModel.getPrice());
        contentValues.put(condition_type, jobMasterModel.getCondition_type());
        contentValues.put(condition, jobMasterModel.getCondition());
        contentValues.put(age, jobMasterModel.getAge());
        contentValues.put(practice_category_type, jobMasterModel.getPractice_category_type());
        contentValues.put(practice_category_name, jobMasterModel.getPractice_category_name());
        contentValues.put(practice_type, jobMasterModel.getPractice_type());
        contentValues.put(practice_type_name, jobMasterModel.getPractice_type_name());
        contentValues.put(specific_locality, jobMasterModel.getSpecific_locality());
        contentValues.put(land_length, jobMasterModel.getLand_length());
        contentValues.put(land_width, jobMasterModel.getLand_width());
        contentValues.put(total_area, jobMasterModel.getTotal_area());
        contentValues.put(build_up_area, jobMasterModel.getBuild_up_area());
        contentValues.put(type, jobMasterModel.getType());
        contentValues.put(primary_type, jobMasterModel.getPrimary_type());
        contentValues.put(licence, jobMasterModel.getLicence());
        contentValues.put(no_of_bed, jobMasterModel.getNo_of_bed());
        contentValues.put(specification, jobMasterModel.getSpecification());
        contentValues.put(deal_type, jobMasterModel.getDeal_type());
        contentValues.put(description, jobMasterModel.getDescription());
        contentValues.put(product_image, jobMasterModel.getProduct_image());
        contentValues.put(add_date, jobMasterModel.getAdd_date());
        contentValues.put(modify_date, jobMasterModel.getModify_date());
        contentValues.put(status, jobMasterModel.getStatus());
        contentValues.put(featured_product, jobMasterModel.getFeatured_product());
        contentValues.put(featured_product_type, jobMasterModel.getFeatured_product_type());
        contentValues.put(specialization_name, jobMasterModel.getSpecialization_name());
        //TODO: inserting rows
        if (insert)
            db.insert(TABLE_PRODUCT_MASTER, null, contentValues);
        else
            db.update(TABLE_PRODUCT_MASTER, contentValues, product_id + " = ?", new String[]{String.valueOf(jobMasterModel.getProduct_id())});

        db.close(); //TODO: closing the database after the insert operation
        Log.e("product", jobMasterModel.getProduct_id() + " = " + jobMasterModel.getProduct_id());

    }

    public void AddSellBuyMaster(DashBoardProductModel jobMasterModel, boolean insert) {
        SQLiteDatabase db = this.getWritableDatabase();
        //TODO: here we are putting the data using the content values
        ContentValues contentValues = new ContentValues();
        contentValues.put(id, jobMasterModel.getId());
        contentValues.put(product_id, jobMasterModel.getProduct_id());
        contentValues.put(product_name, jobMasterModel.getProduct_name());
        contentValues.put(category_id, jobMasterModel.getCategory_id());
        contentValues.put(category_name, jobMasterModel.getCategory_name());
        contentValues.put(price, jobMasterModel.getPrice());
        contentValues.put(condition_type, jobMasterModel.getCondition_type());
        contentValues.put(condition, jobMasterModel.getCondition());
        contentValues.put(age, jobMasterModel.getAge());
        contentValues.put(practice_category_type, jobMasterModel.getPractice_category_type());
        contentValues.put(practice_category, jobMasterModel.getPractice_category_name());
        contentValues.put(practice_type, jobMasterModel.getPractice_type());
        contentValues.put(practice_type_name, jobMasterModel.getPractice_type_name());
        contentValues.put(rooms, jobMasterModel.getRooms());
        contentValues.put(country_code, jobMasterModel.getCountry_code());
        contentValues.put(country_name, jobMasterModel.getCountry_name());
        contentValues.put(state_code, jobMasterModel.getState_code());
        contentValues.put(state_name, jobMasterModel.getState_name());
        contentValues.put(city_id, jobMasterModel.getCity_id());
        contentValues.put(city_name, jobMasterModel.getCity_name());
        contentValues.put(specific_locality, jobMasterModel.getSpecific_locality());
        contentValues.put(land_length, jobMasterModel.getLand_length());
        contentValues.put(land_width, jobMasterModel.getLand_width());
        contentValues.put(total_area, jobMasterModel.getTotal_area());
        contentValues.put(build_up_area, jobMasterModel.getBuild_up_area());
        contentValues.put(primary_type, jobMasterModel.getPrimary_type());
        contentValues.put(primary, jobMasterModel.getPrimary());
        contentValues.put(licence, jobMasterModel.getLicence());
        contentValues.put(no_of_bed, jobMasterModel.getNo_of_bed());
        contentValues.put(specification, jobMasterModel.getSpecification());
        contentValues.put(deal_type, jobMasterModel.getDeal_type());
        contentValues.put(deal_type_name, jobMasterModel.getDeal_type_name());
        contentValues.put(description, jobMasterModel.getDescription());
        contentValues.put(product_image, jobMasterModel.getProduct_image());
        contentValues.put(add_date, jobMasterModel.getAdd_date());
        contentValues.put(modify_date, jobMasterModel.getModify_date());
        contentValues.put(status, jobMasterModel.getStatus());
        contentValues.put(sell_buy_status, jobMasterModel.getSell_buy_status());
        //TODO: inserting rows
        if (insert)
            db.insert(TABLE_SELL_BUY_PRODUCT_MASTER, null, contentValues);
        else
            db.update(TABLE_SELL_BUY_PRODUCT_MASTER, contentValues, id + " = ?", new String[]{String.valueOf(jobMasterModel.getId())});

        db.close(); //TODO: closing the database after the insert operation
        Log.e("sell_buy", jobMasterModel.getId() + " = " + jobMasterModel.getId());

    }

    public void AddProductDetailMaster(DashBoardProductModel jobMasterModel, boolean insert) {
        SQLiteDatabase db = this.getWritableDatabase();
        //TODO: here we are putting the data using the content values
        ContentValues contentValues = new ContentValues();
        contentValues.put(product_id, jobMasterModel.getProduct_id());
        contentValues.put(product_name, jobMasterModel.getProduct_name());
        contentValues.put(category_id, jobMasterModel.getCategory_id());
        contentValues.put(category_name, jobMasterModel.getCategory_name());
        contentValues.put(price, jobMasterModel.getPrice());
        contentValues.put(condition_type, jobMasterModel.getCondition_type());
        contentValues.put(condition, jobMasterModel.getCondition());
        contentValues.put(age, jobMasterModel.getAge());
        contentValues.put(rooms, jobMasterModel.getRooms());
        contentValues.put(specific_locality, jobMasterModel.getSpecific_locality());
        contentValues.put(land_length, jobMasterModel.getLand_length());
        contentValues.put(land_width, jobMasterModel.getLand_width());
        contentValues.put(total_area, jobMasterModel.getTotal_area());
        contentValues.put(build_up_area, jobMasterModel.getBuild_up_area());
        contentValues.put(type, jobMasterModel.getType());
        contentValues.put(primary_type, jobMasterModel.getPrimary_type());
        contentValues.put(licence, jobMasterModel.getLicence());
        contentValues.put(no_of_bed, jobMasterModel.getNo_of_bed());
        contentValues.put(specification, jobMasterModel.getSpecification());
        contentValues.put(deal_type, jobMasterModel.getDeal_type());
        contentValues.put(description, jobMasterModel.getDescription());
        contentValues.put(product_image, jobMasterModel.getProduct_image());
        contentValues.put(add_date, jobMasterModel.getAdd_date());
        contentValues.put(modify_date, jobMasterModel.getModify_date());
        contentValues.put(seeking_name, jobMasterModel.getSeeking_name());
        contentValues.put(status, jobMasterModel.getStatus());
        //TODO: inserting rows
        if (insert)
            db.insert(TABLE_PRODUCT_DETAIL_MASTER, null, contentValues);
        else
            db.update(TABLE_PRODUCT_DETAIL_MASTER, contentValues, product_id + " = ?", new String[]{String.valueOf(jobMasterModel.getProduct_id())});

        db.close(); //TODO: closing the database after the insert operation
        Log.e("prod_detail", jobMasterModel.getProduct_id() + " = " + jobMasterModel.getProduct_id());
    }

    public void AddNotificationMaster(NotificationDashBoardModel jobMasterModel, boolean insert) {
        SQLiteDatabase db = this.getWritableDatabase();
        //TODO: here we are putting the data using the content values
        ContentValues contentValues = new ContentValues();
        contentValues.put(id, jobMasterModel.getId());
        contentValues.put(uuid, jobMasterModel.getUuid());
        contentValues.put(product_id, jobMasterModel.getProduct_id());
        contentValues.put(sender_id, jobMasterModel.getSender_id());
        contentValues.put(product_category_id, jobMasterModel.getProduct_category_id());
        contentValues.put(message, jobMasterModel.getMsg());
        contentValues.put(msg_type, jobMasterModel.getMsgType());
        contentValues.put(add_date, jobMasterModel.getAdd_date());
        contentValues.put(modify_date, jobMasterModel.getModify_date());
        contentValues.put(status, jobMasterModel.getStatus());
        contentValues.put(chat_id, jobMasterModel.getChat_id());
        contentValues.put(time, jobMasterModel.getTime());
        //TODO: inserting rows
        if (insert)
            db.insert(TABLE_NOTIFICATION_MASTER, null, contentValues);
        else
            db.update(TABLE_NOTIFICATION_MASTER, contentValues, id + " = ?", new String[]{String.valueOf(jobMasterModel.getId())});

        db.close(); //TODO: closing the database after the insert operation
        Log.e("prod_detail", jobMasterModel.getId() + " = " + jobMasterModel.getId());
    }

    public void AddChatNotificationMaster(NotificationDashBoardModel jobMasterModel, boolean insert) {
        SQLiteDatabase db = this.getWritableDatabase();
        //TODO: here we are putting the data using the content values
        ContentValues contentValues = new ContentValues();
        contentValues.put(id, jobMasterModel.getId());
        contentValues.put(product_id, jobMasterModel.getProduct_id());
        contentValues.put(sender_id, jobMasterModel.getSender_id());
        contentValues.put(reciever_id, jobMasterModel.getReciever_id());
        contentValues.put(message, jobMasterModel.getMsg());
        contentValues.put(add_date, jobMasterModel.getAdd_date());
        contentValues.put(modify_date, jobMasterModel.getModify_date());
        contentValues.put(status, jobMasterModel.getStatus());
        contentValues.put(name, jobMasterModel.getName());
        contentValues.put(chat_id, jobMasterModel.getChat_id());
        contentValues.put(time, jobMasterModel.getTime());
        //TODO: inserting rows
        if (insert)
            db.insert(TABLE_CHAT_NOTIFICATION_MASTER, null, contentValues);
        else
            db.update(TABLE_CHAT_NOTIFICATION_MASTER, contentValues, id + " = ?", new String[]{String.valueOf(jobMasterModel.getId())});

        db.close(); //TODO: closing the database after the insert operation
        Log.e("prod_detail", jobMasterModel.getId() + " = " + jobMasterModel.getId());
    }

    public void AddOtherCategory(OtherCategoryModel model, boolean insert) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(id, model.getId());
        contentValues.put(type_id, model.getType_id());
        contentValues.put(name, model.getName());
        contentValues.put(status, model.getStatus());
        if (insert)
            db.insert(TABLE_OTHER_CATEGORY_MASTER, null, contentValues);
        else
            db.update(TABLE_OTHER_CATEGORY_MASTER, contentValues, id + " = ?", new String[]{String.valueOf(model.getId())});
    }

    public void AddCaseList(CaseStudyListModel model, boolean insert) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(id, model.getId());
        contentValues.put(case_name, model.getCase_name());
        contentValues.put(case_sub_title, model.getCase_sub_title());
        contentValues.put(description, model.getDescription());
        contentValues.put(case_type, model.getCase_type());
        contentValues.put(attend_status, model.getAttend_status());
        contentValues.put(add_date, model.getAdd_date());
        contentValues.put(modify_date, model.getModify_date());
        contentValues.put(status, model.getStatus());
        if (insert)
            db.insert(TABLE_CASE_STUDY_LIST_MASTER, null, contentValues);
        else
            db.update(TABLE_CASE_STUDY_LIST_MASTER, contentValues, id + " = ?", new String[]{String.valueOf(model.getId())});
    }

    public void AddCaseAttendList(CaseStudyListModel model, boolean insert) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(id, model.getId());
        contentValues.put(case_name, model.getCase_name());
        contentValues.put(case_type, model.getCase_type());
        contentValues.put(case_sub_title, model.getCase_sub_title());
        contentValues.put(total_questions, model.getTotal_questions());
        contentValues.put(total_attempted_ques, model.getTotal_attempted_ques());
        contentValues.put(add_date, model.getAdd_date());
        contentValues.put(modify_date, model.getModify_date());
        contentValues.put(status, model.getStatus());
        if (insert)
            db.insert(TABLE_CASE_STUDY_ATTEND_LIST_MASTER, null, contentValues);
        else
            db.update(TABLE_CASE_STUDY_ATTEND_LIST_MASTER, contentValues, id + " = ?", new String[]{String.valueOf(model.getId())});
    }

    public void AddCaseStudyQuesList(CaseStudyListModel model, boolean insert) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(id, model.getId());
        contentValues.put(ques_id, model.getQues_id());
        contentValues.put(case_name, model.getCase_name());
        contentValues.put(case_heading, model.getCase_heading());
        contentValues.put(description, model.getDescription());
        contentValues.put(case_type, model.getCase_type());
        contentValues.put(total_questions, model.getTotal_questions());
        contentValues.put(time_durations, model.getTime_durations());
        contentValues.put(ques_name, model.getQues_name());
        contentValues.put(ques_status, model.getQues_status());
        if (insert)
            db.insert(TABLE_CASE_STUDY_QUESTION_MASTER, null, contentValues);
        else
            db.update(TABLE_CASE_STUDY_QUESTION_MASTER, contentValues, id + " = ?", new String[]{String.valueOf(model.getId())});

    }

    public void AddCaseStudyQuesAnsList(CaseStudyListModel model, boolean insert) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(id, model.getId());
        contentValues.put(ques_name, model.getQues_name());
        contentValues.put(option_name, model.getOption_name());
        contentValues.put(is_correct, model.getIs_correct());
        contentValues.put(correct_answer, model.getCorrect_answer());
        if (insert)
            db.insert(TABLE_CASE_STUDY_QUESTION_ANS_MASTER, null, contentValues);
        else
            db.update(TABLE_CASE_STUDY_QUESTION_ANS_MASTER, contentValues, id + " = ?", new String[]{String.valueOf(model.getId())});

    }

    public void AddCaseStudyOptionList(CaseStudyListModel model, boolean insert) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(option_id, model.getOption_id());
        contentValues.put(ques_id, model.getQues_id());
        contentValues.put(option_name, model.getOption_name());
        contentValues.put(is_correct, model.getIs_correct());
        if (insert)
            db.insert(TABLE_CASE_STUDY_OPTION_MASTER, null, contentValues);
        else
            db.update(TABLE_CASE_STUDY_OPTION_MASTER, contentValues, option_id + " = ?", new String[]{String.valueOf(model.getOption_id())});

    }

    public void AddCaseStudyAnswerList(CaseStudyListModel model, boolean insert, boolean input) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(option_id, model.getOption_id());
        contentValues.put(ques_id, model.getQues_id());
        contentValues.put(option_name, model.getOption_name());
        contentValues.put(answer, model.getAnswer());
        if (insert)
            db.insert(TABLE_CASE_STUDY_ANSWER_MASTER, null, contentValues);
        else if (input) {
            db.update(TABLE_CASE_STUDY_ANSWER_MASTER, contentValues, ques_id + " = ?", new String[]{String.valueOf(model.getQues_id())});
        } else {
            db.update(TABLE_CASE_STUDY_ANSWER_MASTER, contentValues, option_id + " = ?", new String[]{String.valueOf(model.getOption_id())});
        }


    }

//    public void AddGoingConferenceDetails(ConferenceDetailsModel jobMasterModel, boolean insert) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        //TODO: here we are putting the data using the content values
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(id, jobMasterModel.getId());
//        contentValues.put(conference_id, jobMasterModel.getConference_id());
//        contentValues.put(conference_name, jobMasterModel.getConference_name());
//        contentValues.put(from_date, jobMasterModel.getFrom_date());
//        contentValues.put(to_date, jobMasterModel.getTo_date());
//        contentValues.put(from_time, jobMasterModel.getFrom_time());
//        contentValues.put(to_time, jobMasterModel.getTo_time());
//        contentValues.put(venue, jobMasterModel.getVenue());
//        contentValues.put(event_host_name, jobMasterModel.getEvent_host_name());
//        contentValues.put(speciality, jobMasterModel.getSpeciality());
//        contentValues.put(contact, jobMasterModel.getContact());
//        contentValues.put(location, jobMasterModel.getLocation());
//        contentValues.put(latitude, jobMasterModel.getLatitude());
//        contentValues.put(logitude, jobMasterModel.getLogitude());
//        contentValues.put(brochuers_file, jobMasterModel.getBrochuers_file());
//        contentValues.put(brochuers_charge, jobMasterModel.getBrochuers_charge());
//        contentValues.put(brochuers_days, jobMasterModel.getBrochuers_days());
//        contentValues.put(registration_fee, jobMasterModel.getRegistration_fee());
//        contentValues.put(registration_days, jobMasterModel.getRegistration_days());
//        contentValues.put(event_sponcer, jobMasterModel.getEvent_sponcer());
//        contentValues.put(particular_country_id, jobMasterModel.getParticular_country_id());
//        contentValues.put(particular_country_name, jobMasterModel.getParticular_country_name());
//        contentValues.put(particular_state_id, jobMasterModel.getParticular_state_id());
//        contentValues.put(particular_state_name, jobMasterModel.getParticular_state_name());
//        contentValues.put(status, jobMasterModel.getStatus());
//        contentValues.put(add_date, jobMasterModel.getAdd_date());
//        contentValues.put(modify_date, jobMasterModel.getModify_date());
//        contentValues.put(payment_status, jobMasterModel.getPayment_status());
//        contentValues.put(show_like, jobMasterModel.getShow_like());
//        contentValues.put(particular_city_id, jobMasterModel.getParticular_city_id());
//        contentValues.put(particular_city_name, jobMasterModel.getParticular_city_name());
//        contentValues.put(address_type, jobMasterModel.getAddress_type());
//        //TODO: inserting rows
//        if (insert)
//            db.insert(TABLE_GOING_CONFERENCES_SHOW_DETAILS, null, contentValues);
//        else
//            db.update(TABLE_GOING_CONFERENCES_SHOW_DETAILS, contentValues, id + " = ?", new String[]{String.valueOf(jobMasterModel.getId())});
//
//        db.close(); //TODO: closing the database after the insert operation
//        Log.e("confgoingdetails", jobMasterModel.getId() + " = " + jobMasterModel.getId());
//
//    }


    public ArrayList<DashBoardProductModel> getMarketProducts(String tableName, String idGift, String status_) {
        String selectQuery = "";
        ArrayList<DashBoardProductModel> subList = new ArrayList<>();

        if (idGift.equalsIgnoreCase("")) {
            selectQuery = "SELECT  * FROM " + tableName + " WHERE " + status + " = " + status_ + " ORDER BY " + modify_date + " DESC LIMIT " + 100000000;
        } else {
            selectQuery = "SELECT  * FROM " + tableName + " WHERE " + category_id + " = " + idGift + " AND " + status + " = " + status_ + " ORDER BY " + modify_date + " DESC LIMIT " + 100000000;
        }


        Log.e("QUERY", selectQuery);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        while (cursor.moveToNext()) {
            DashBoardProductModel eventlist = new DashBoardProductModel();
            // eventlist.setEVENT_ID_local(Integer.parseInt(cursor.getString(0)));

            eventlist.setProduct_id(cursor.getString(0));
            eventlist.setProduct_name(cursor.getString(1));
            eventlist.setCategory_id(cursor.getString(2));
            eventlist.setCategory_name(cursor.getString(3));
            eventlist.setPrice(cursor.getString(4));
            eventlist.setCondition_type(cursor.getString(5));
            eventlist.setCondition(cursor.getString(6));
            eventlist.setAge(cursor.getString(7));
            eventlist.setPractice_category_type(cursor.getString(8));
            eventlist.setPractice_category_name(cursor.getString(9));
            eventlist.setPractice_type(cursor.getString(10));
            eventlist.setPractice_type_name(cursor.getString(11));
            eventlist.setSpecific_locality(cursor.getString(12));
            eventlist.setLand_length(cursor.getString(13));
            eventlist.setLand_width(cursor.getString(14));
            eventlist.setTotal_area(cursor.getString(15));
            eventlist.setBuild_up_area(cursor.getString(16));
            eventlist.setType(cursor.getString(17));
            eventlist.setPrimary_type(cursor.getString(18));
            eventlist.setLicence(cursor.getString(19));
            eventlist.setNo_of_bed(cursor.getString(20));
            eventlist.setSpecification(cursor.getString(21));
            eventlist.setDeal_type(cursor.getString(22));
            eventlist.setDescription(cursor.getString(23));
            eventlist.setProduct_image(cursor.getString(24));
            eventlist.setAdd_date(cursor.getString(25));
            eventlist.setModify_date(cursor.getString(26));
            eventlist.setStatus(cursor.getString(27));
            eventlist.setFeatured_product(cursor.getString(28));
            eventlist.setFeatured_product_type(cursor.getString(29));
            eventlist.setSpecialization_name(cursor.getString(30));
            subList.add(eventlist);
        }

        Log.e("No_of_column", "-==" + cursor.getColumnCount());
        // Log.e("No_of_Giftttt","-=="+cursor.getString(0));
        cursor.close();
        Log.e("SIZE", " SIZE " + subList.size());
        return subList;
    }

    public boolean updateData(HashMap<String, String> columnValue, String whereColumn, String whereValue, String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        for (String key : columnValue.keySet()) {
            contentValues.put(key, columnValue.get(key)
            );
        }

        Log.e("UPDATE FROM 3", "DRUG");
        db.update(tableName, contentValues, whereColumn + "=?", new String[]{whereValue});
        return true;
    }

    public void deleteData(String value, String table_name, String column) {
        SQLiteDatabase db = this.getWritableDatabase();
        //return db.delete(table_name, column + " = ? ", new String[]{value});
        //Execute sql query to remove from database
        //NOTE: When removing by String in SQL, value must be enclosed with ''
        db.execSQL("DELETE FROM " + table_name + " WHERE " + column + "= '" + value + "'");

        Log.e("QUERYDELETE", "= " + "DELETE FROM " + table_name + " WHERE " + column + "= '" + value + "'");

        //Close the database
        db.close();
    }

    public ArrayList<DashBoardProductModel> getSellBuyProducts(String tableName, String idGift) {

        ArrayList<DashBoardProductModel> subList = new ArrayList<>();
        String selectQuery = "";
        if (idGift.equalsIgnoreCase("1")) {
            selectQuery = "SELECT  * FROM " + tableName + " WHERE " + sell_buy_status + " = " + idGift + " AND " + status + " = " + "1" + " ORDER BY " + modify_date + " DESC LIMIT " + 100000000;
        } else {
            //selectQuery = "SELECT  * FROM " + tableName + " WHERE " + sell_buy_status + " = " + idGift + " ORDER BY " + modify_date + " DESC LIMIT " + 100000000;
            selectQuery = "SELECT  * FROM " + tableName + " WHERE " + sell_buy_status + " = " + idGift + " AND " + status + " = " + "1" + " ORDER BY " + modify_date + " DESC LIMIT " + 100000000;
        }

        Log.e("QUERY", selectQuery);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        while (cursor.moveToNext()) {
            DashBoardProductModel eventlist = new DashBoardProductModel();
            // eventlist.setEVENT_ID_local(Integer.parseInt(cursor.getString(0)));

            eventlist.setId(cursor.getString(0));
            eventlist.setProduct_id(cursor.getString(1));
            eventlist.setProduct_name(cursor.getString(2));
            eventlist.setCategory_id(cursor.getString(3));
            eventlist.setCategory_name(cursor.getString(4));
            eventlist.setPrice(cursor.getString(5));
            eventlist.setCondition_type(cursor.getString(6));
            eventlist.setCondition(cursor.getString(7));
            eventlist.setAge(cursor.getString(8));
            eventlist.setPractice_category_type(cursor.getString(9));
            eventlist.setPractice_category_name(cursor.getString(10));
            eventlist.setPractice_type(cursor.getString(11));
            eventlist.setPractice_type_name(cursor.getString(12));
            eventlist.setRooms(cursor.getString(13));
            eventlist.setCountry_code(cursor.getString(14));
            eventlist.setCountry_name(cursor.getString(15));
            eventlist.setState_code(cursor.getString(16));
            eventlist.setState_name(cursor.getString(17));
            eventlist.setCity_id(cursor.getString(18));
            eventlist.setCity_name(cursor.getString(19));
            eventlist.setSpecific_locality(cursor.getString(20));
            eventlist.setLand_length(cursor.getString(21));
            eventlist.setLand_width(cursor.getString(22));
            eventlist.setTotal_area(cursor.getString(23));
            eventlist.setBuild_up_area(cursor.getString(24));
            eventlist.setPrimary_type(cursor.getString(25));
            eventlist.setPrimary(cursor.getString(26));
            eventlist.setLicence(cursor.getString(27));
            eventlist.setNo_of_bed(cursor.getString(28));
            eventlist.setSpecification(cursor.getString(29));
            eventlist.setDeal_type(cursor.getString(30));
            eventlist.setDeal_type_name(cursor.getString(31));
            eventlist.setDescription(cursor.getString(32));
            eventlist.setProduct_image(cursor.getString(33));
            eventlist.setAdd_date(cursor.getString(34));
            eventlist.setModify_date(cursor.getString(35));
            eventlist.setStatus(cursor.getString(36));
            eventlist.setSell_buy_status(cursor.getString(37));
            subList.add(eventlist);
        }

        Log.e("No_of_column", "-==" + cursor.getColumnCount());
        // Log.e("No_of_Giftttt","-=="+cursor.getString(0));
        cursor.close();
        Log.e("SIZE", " SIZE " + subList.size());
        return subList;
    }

    public ArrayList<NotificationDashBoardModel> getNotification(String tableName, String idGift) {

        ArrayList<NotificationDashBoardModel> subList = new ArrayList<>();


        // String selectQuery = "SELECT  * FROM " + tableName + " WHERE " + category_id +" = "+ idGift + "";
        //String selectQuery = "SELECT  * FROM " + tableName + "";
        String selectQuery = "SELECT * FROM " + tableName + " WHERE " + status + " != '" + "3" + "'" + " ORDER BY " + modify_date + " DESC LIMIT " + 100000000;
        //String selectQuery = "SELECT  * FROM " + tableName + " ORDER BY " + modify_date + " DESC LIMIT " + 100000000;
        Log.e("QUERY", selectQuery);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        while (cursor.moveToNext()) {
            NotificationDashBoardModel eventlist = new NotificationDashBoardModel();
            // eventlist.setEVENT_ID_local(Integer.parseInt(cursor.getString(0)));

            eventlist.setId(cursor.getString(0));
            eventlist.setUuid(cursor.getString(1));
            eventlist.setProduct_id(cursor.getString(2));
            eventlist.setSender_id(cursor.getString(3));
            eventlist.setProduct_category_id(cursor.getString(4));
            eventlist.setMsg(cursor.getString(5));
            eventlist.setMsgType(cursor.getString(6));
            eventlist.setAdd_date(cursor.getString(7));
            eventlist.setModify_date(cursor.getString(8));
            eventlist.setStatus(cursor.getString(9));
            eventlist.setChat_id(cursor.getString(10));
            eventlist.setTime(cursor.getString(11));
            subList.add(eventlist);
        }

        Log.e("No_of_column", "-==" + cursor.getColumnCount());
        // Log.e("No_of_Giftttt","-=="+cursor.getString(0));
        cursor.close();
        Log.e("SIZE", " SIZE " + subList.size());
        return subList;
    }

    public ArrayList<BookTicketModal> getBookTicketData(String tableName) {
        ArrayList<BookTicketModal> ticketModalList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String SelectQuery = "SELECT * FROM " + tableName;
        Cursor cursor = db.rawQuery(SelectQuery, null);
        while (cursor.moveToNext()) {

            BookTicketModal ticketModal = new BookTicketModal();
            ticketModal.setId(cursor.getString(0));
            ticketModal.setDayCount(cursor.getString(1));
            ticketModal.setPrice(cursor.getString(2));
            ticketModal.setDate(cursor.getString(3));
            ticketModal.setPercentage(cursor.getString(4));
            ticketModalList.add(ticketModal);

        }
        cursor.close();
        return ticketModalList;
    }

    public ArrayList<NotificationDashBoardModel> getChatNotification(String tableName, String idGift) {

        ArrayList<NotificationDashBoardModel> subList = new ArrayList<>();


        // String selectQuery = "SELECT  * FROM " + tableName + " WHERE " + category_id +" = "+ idGift + "";
        //String selectQuery = "SELECT  * FROM " + tableName + "";
        String selectQuery = "SELECT  * FROM " + tableName + " ORDER BY " + modify_date + " DESC LIMIT " + 100000000;
        Log.e("QUERY", selectQuery);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        while (cursor.moveToNext()) {
            NotificationDashBoardModel eventlist = new NotificationDashBoardModel();
            // eventlist.setEVENT_ID_local(Integer.parseInt(cursor.getString(0)));

            eventlist.setId(cursor.getString(0));
            eventlist.setProduct_id(cursor.getString(1));
            eventlist.setSender_id(cursor.getString(2));
            eventlist.setReciever_id(cursor.getString(3));
            eventlist.setMsg(cursor.getString(4));
            eventlist.setAdd_date(cursor.getString(5));
            eventlist.setModify_date(cursor.getString(6));
            eventlist.setStatus(cursor.getString(7));
            eventlist.setName(cursor.getString(8));
            eventlist.setChat_id(cursor.getString(9));
            eventlist.setTime(cursor.getString(10));
            subList.add(eventlist);
        }

        Log.e("No_of_column", "-==" + cursor.getColumnCount());
        // Log.e("No_of_Giftttt","-=="+cursor.getString(0));
        cursor.close();
        Log.e("SIZE", " SIZE " + subList.size());
        return subList;
    }

    public ArrayList<OtherCategoryModel> getOtherCategory(String tableName, String id) {
        ArrayList<OtherCategoryModel> subList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + tableName + " WHERE " + type_id + " = " + id + "";
        Log.e("QUERY", selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        while (cursor.moveToNext()) {
            OtherCategoryModel ob = new OtherCategoryModel();
            ob.setId(cursor.getString(0));
            ob.setType_id(cursor.getString(1));
            ob.setName(cursor.getString(2));
            ob.setStatus(cursor.getString(3));
            subList.add(ob);
        }
        cursor.close();
        return subList;
    }

    public ArrayList<CaseStudyListModel> getCaseStudyQuestion(String tableName, String id_) {
        String selectQuery = "";
        ArrayList<CaseStudyListModel> subList = new ArrayList<>();
        if (id_.equalsIgnoreCase("")) {
            selectQuery = "SELECT * FROM " + tableName;
        } else {
            selectQuery = "SELECT * FROM " + tableName + " WHERE " + id + " = " + id_ + "";
        }


        Log.e("QUERY", selectQuery);
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        while (cursor.moveToNext()) {
            CaseStudyListModel ob = new CaseStudyListModel();
            // ob.setQues_id(cursor.getInt(0));
            ob.setId(cursor.getString(0));
            ob.setQues_id(cursor.getString(1));
            ob.setCase_name(cursor.getString(2));
            ob.setCase_heading(cursor.getString(3));
            ob.setDescription(cursor.getString(4));
            ob.setCase_type(cursor.getString(5));
            ob.setTotal_questions(cursor.getString(6));
            ob.setTime_durations(cursor.getString(7));
            ob.setQues_name(cursor.getString(8));
            ob.setQues_status(cursor.getString(9));
            subList.add(ob);
        }
        cursor.close();
        return subList;
    }

    public ArrayList<CaseStudyListModel> getCaseStudyQuestionAns(String tableName, String id_) {
        String selectQuery = "";
        ArrayList<CaseStudyListModel> subList = new ArrayList<>();
        if (id_.equalsIgnoreCase("")) {
            selectQuery = "SELECT * FROM " + tableName;
        } else {
            selectQuery = "SELECT * FROM " + tableName + " WHERE " + id + " = " + id_ + "";
        }


        Log.e("QUERY", selectQuery);
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        while (cursor.moveToNext()) {
            CaseStudyListModel ob = new CaseStudyListModel();
            // ob.setQues_id(cursor.getInt(0));
            ob.setId(cursor.getString(0));
            ob.setQues_name(cursor.getString(1));
            ob.setOption_name(cursor.getString(2));
            ob.setIs_correct(cursor.getString(3));
            ob.setCorrect_answer(cursor.getString(4));
            subList.add(ob);
        }
        cursor.close();
        return subList;
    }

    public ArrayList<CaseStudyListModel> getCaseStudyOptions(String tableName, String id) {
        ArrayList<CaseStudyListModel> subList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + tableName + " WHERE " + ques_id + " = " + id + "";
        Log.e("QUERY", selectQuery);
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        while (cursor.moveToNext()) {
            CaseStudyListModel ob = new CaseStudyListModel();
            // ob.setQues_id(cursor.getInt(0));
            ob.setOption_id(cursor.getString(0));
            ob.setQues_id(cursor.getString(1));
            ob.setOption_name(cursor.getString(2));
            ob.setIs_correct(cursor.getString(3));
            subList.add(ob);
        }
        cursor.close();
        return subList;
    }

    public ArrayList<CaseStudyListModel> getCaseStudyAnswers(String tableName) {
        ArrayList<CaseStudyListModel> subList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + tableName;
        Log.e("QUERY", selectQuery);
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        while (cursor.moveToNext()) {
            CaseStudyListModel ob = new CaseStudyListModel();
            // ob.setQues_id(cursor.getInt(0));
            ob.setOption_id(cursor.getString(0));
            ob.setQues_id(cursor.getString(1));
            ob.setOption_name(cursor.getString(2));
            ob.setAnswer(cursor.getString(3));
            subList.add(ob);
        }
        cursor.close();
        return subList;
    }

    public ArrayList<CaseStudyListModel> getCaseStudyList(String tableName, String id_) {
        ArrayList<CaseStudyListModel> subList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + tableName + " WHERE " + status + " = " + id_ + "";
        Log.e("QUERY", selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        while (cursor.moveToNext()) {
            CaseStudyListModel ob = new CaseStudyListModel();
            ob.setId(cursor.getString(0));
            ob.setCase_name(cursor.getString(1));
            ob.setCase_sub_title(cursor.getString(2));
            ob.setDescription(cursor.getString(3));
            ob.setCase_type(cursor.getString(4));
            ob.setAttend_status(cursor.getString(5));
            ob.setAdd_date(cursor.getString(6));
            ob.setModify_date(cursor.getString(7));
            ob.setStatus(cursor.getString(8));
            subList.add(ob);
        }
        cursor.close();
        return subList;
    }

    public ArrayList<MedicalWritingModel> getPublicationList(String tableName, String id_) {
        ArrayList<MedicalWritingModel> subList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + tableName + " WHERE " + status + " = " + id_ + "";
        Log.e("QUERY", selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        while (cursor.moveToNext()) {
            MedicalWritingModel ob = new MedicalWritingModel();
            ob.setId(cursor.getString(0));
            ob.setTitle(cursor.getString(1));
            ob.setImage(cursor.getString(2));
            ob.setPublished_year(cursor.getString(3));
            ob.setUrl(cursor.getString(4));
            ob.setAdd_date(cursor.getString(5));
            ob.setModify_date(cursor.getString(6));
            ob.setStatus(cursor.getString(7));
            subList.add(ob);
        }
        cursor.close();
        return subList;
    }

    public ArrayList<CaseStudyListModel> getCaseStudyAttendList(String tableName, String id_) {
        ArrayList<CaseStudyListModel> subList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + tableName + " WHERE " + status + " = " + id_ + "";
        Log.e("QUERY", selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        while (cursor.moveToNext()) {
            CaseStudyListModel ob = new CaseStudyListModel();
            ob.setId(cursor.getString(0));
            ob.setCase_name(cursor.getString(1));
            ob.setCase_type(cursor.getString(2));
            ob.setCase_sub_title(cursor.getString(3));
            ob.setTotal_questions(cursor.getString(4));
            ob.setTotal_attempted_ques(cursor.getString(5));
            ob.setAdd_date(cursor.getString(6));
            ob.setModify_date(cursor.getString(7));
            ob.setStatus(cursor.getString(8));
            subList.add(ob);
        }
        cursor.close();
        return subList;
    }

    //    public ArrayList<MetroCityModel> getMetroCity(String tableName)
//    {
//        ArrayList<MetroCityModel> subList = new ArrayList<>();
//        String selectQuery = "SELECT * FROM " + tableName;
//        Log.e("QUERY", selectQuery);
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery,null);
//        while (cursor.moveToNext())
//        {
//            MetroCityModel model = new MetroCityModel();
//            model.setId_city(cursor.getString(0));
//            model.setProfile_category_name(cursor.getString(1));
//            model.setStatus(cursor.getString(2));
//            subList.add(model);
//        }
//        Log.e("No_of_column","-=="+cursor.getColumnCount());
//        // Log.e("No_of_Giftttt","-=="+cursor.getString(0));
//        cursor.close();
//        Log.e("SIZE", " SIZE " + subList.size());
//        return subList;
//
//    }


    public ArrayList<JobMasterModel> getJobList(String tableName, String idGift, String status_) {
        String selectQuery = "";
        ArrayList<JobMasterModel> subList = new ArrayList<>();

        if (idGift.equalsIgnoreCase("") && status_.equalsIgnoreCase("")) {
            selectQuery = "SELECT  * FROM " + tableName;
        } else {
            selectQuery = "SELECT  * FROM " + tableName + " WHERE " + post_req_status + " = " + idGift + " AND " + status + " = " + status_ + "";
        }

        // String selectQuery = "SELECT  * FROM " + tableName + "";
        Log.e("QUERY", selectQuery);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        while (cursor.moveToNext()) {
            JobMasterModel eventlist = new JobMasterModel();
            // eventlist.setEVENT_ID_local(Integer.parseInt(cursor.getString(0)));

            eventlist.setId(cursor.getString(0));
            eventlist.setJob_id(cursor.getString(1));
            eventlist.setMedical_profile_id(cursor.getString(2));
            eventlist.setMedical_profile_name(cursor.getString(3));
            eventlist.setJob_title(cursor.getString(4));
            eventlist.setJob_type(cursor.getString(5));
            eventlist.setJob_type_name(cursor.getString(6));
            eventlist.setSpecialization_id(cursor.getString(7));
            eventlist.setSpecialization_name(cursor.getString(8));
            eventlist.setSub_specialization_id(cursor.getString(9));
            eventlist.setSub_specialization_name(cursor.getString(10));
            eventlist.setYear_of_experience(cursor.getString(11));
            eventlist.setCurrent_ctc(cursor.getString(12));
            eventlist.setExpected_ctc(cursor.getString(13));
            eventlist.setCurrent_employer(cursor.getString(14));
            eventlist.setLocation(cursor.getString(15));
            eventlist.setPreferred_location(cursor.getString(16));
            eventlist.setResume(cursor.getString(17));
            eventlist.setJob_description(cursor.getString(18));
            eventlist.setSkills_required(cursor.getString(19));
            eventlist.setAdd_date(cursor.getString(20));
            eventlist.setModify_date(cursor.getString(21));
            eventlist.setPost_req_status(cursor.getString(22));
            eventlist.setDepartment_id(cursor.getString(23));
            eventlist.setDepartment_name(cursor.getString(24));
            eventlist.setStatus(cursor.getString(25));
            subList.add(eventlist);
        }

        Log.e("No_of_column", "-==" + cursor.getColumnCount());
        // Log.e("No_of_Giftttt","-=="+cursor.getString(0));
        cursor.close();
        Log.e("SIZE", " SIZE " + subList.size());
        return subList;
    }

    public ArrayList<JobMasterModel> getJobList(String tableName, String idGift) {
        String selectQuery = "";
        ArrayList<JobMasterModel> subList = new ArrayList<>();

        selectQuery = "SELECT  * FROM " + tableName + " WHERE " + id + " = " + idGift;

        // String selectQuery = "SELECT  * FROM " + tableName + "";
        Log.e("QUERY", selectQuery);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        while (cursor.moveToNext()) {
            JobMasterModel eventlist = new JobMasterModel();
            // eventlist.setEVENT_ID_local(Integer.parseInt(cursor.getString(0)));

            eventlist.setId(cursor.getString(0));
            eventlist.setJob_id(cursor.getString(1));
            eventlist.setMedical_profile_id(cursor.getString(2));
            eventlist.setMedical_profile_name(cursor.getString(3));
            eventlist.setJob_title(cursor.getString(4));
            eventlist.setJob_type(cursor.getString(5));
            eventlist.setJob_type_name(cursor.getString(6));
            eventlist.setSpecialization_id(cursor.getString(7));
            eventlist.setSpecialization_name(cursor.getString(8));
            eventlist.setSub_specialization_id(cursor.getString(9));
            eventlist.setSub_specialization_name(cursor.getString(10));
            eventlist.setYear_of_experience(cursor.getString(11));
            eventlist.setCurrent_ctc(cursor.getString(12));
            eventlist.setExpected_ctc(cursor.getString(13));
            eventlist.setCurrent_employer(cursor.getString(14));
            eventlist.setLocation(cursor.getString(15));
            eventlist.setPreferred_location(cursor.getString(16));
            eventlist.setResume(cursor.getString(17));
            eventlist.setJob_description(cursor.getString(18));
            eventlist.setSkills_required(cursor.getString(19));
            eventlist.setAdd_date(cursor.getString(20));
            eventlist.setModify_date(cursor.getString(21));
            eventlist.setPost_req_status(cursor.getString(22));
            eventlist.setDepartment_id(cursor.getString(23));
            eventlist.setDepartment_name(cursor.getString(24));
            eventlist.setStatus(cursor.getString(25));
            subList.add(eventlist);
        }

        Log.e("No_of_column", "-==" + cursor.getColumnCount());
        // Log.e("No_of_Giftttt","-=="+cursor.getString(0));
        cursor.close();
        Log.e("SIZE", " SIZE " + subList.size());
        return subList;
    }


    public ArrayList<JobMasterModel> getPostedJobList(String tableName, String idGift, String status_) {
        String selectQuery = "";
        ArrayList<JobMasterModel> subList = new ArrayList<>();
        if (idGift.equalsIgnoreCase("")) {
            selectQuery = "SELECT  * FROM " + tableName + " WHERE " + status + " = " + status_ + " ORDER BY " + add_date + " DESC LIMIT " + 100000000;
            ;
        } else {
            selectQuery = "SELECT  * FROM " + tableName + " WHERE " + KEY_JOB_ID + " = '" + idGift + "'";
        }

        // String selectQuery = "SELECT  * FROM " + tableName + "";
        Log.e("QUERY", selectQuery);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        while (cursor.moveToNext()) {
            JobMasterModel eventlist = new JobMasterModel();
            // eventlist.setEVENT_ID_local(Integer.parseInt(cursor.getString(0)));

            eventlist.setId(cursor.getString(0));
            eventlist.setJob_id(cursor.getString(1));
            eventlist.setMedical_profile_id(cursor.getString(2));
            eventlist.setMedical_profile_name(cursor.getString(3));
            eventlist.setJob_title(cursor.getString(4));
            eventlist.setJob_type(cursor.getString(5));
            eventlist.setJob_type_name(cursor.getString(6));
            eventlist.setSpecialization_id(cursor.getString(7));
            eventlist.setSpecialization_name(cursor.getString(8));
            eventlist.setSub_specialization_id(cursor.getString(9));
            eventlist.setSub_specialization_name(cursor.getString(10));
            eventlist.setYear_of_experience(cursor.getString(11));
            eventlist.setCurrent_ctc(cursor.getString(12));
            eventlist.setExpected_ctc(cursor.getString(13));
            eventlist.setCurrent_employer(cursor.getString(14));
            eventlist.setLocation(cursor.getString(15));
            eventlist.setPreferred_location(cursor.getString(16));
            eventlist.setResume(cursor.getString(17));
            eventlist.setJob_description(cursor.getString(18));
            eventlist.setSkills_required(cursor.getString(19));
            eventlist.setAdd_date(cursor.getString(20));
            eventlist.setModify_date(cursor.getString(21));
            eventlist.setPost_req_status(cursor.getString(22));
            eventlist.setDepartment_id(cursor.getString(23));
            eventlist.setDepartment_name(cursor.getString(24));
            eventlist.setStatus(cursor.getString(25));
            eventlist.setJob_title_id(cursor.getString(26));
            subList.add(eventlist);
        }

        Log.e("No_of_column", "-==" + cursor.getColumnCount());
        // Log.e("No_of_Giftttt","-=="+cursor.getString(0));
        cursor.close();
        Log.e("SIZE", " SIZE " + subList.size());
        return subList;
    }

    public ArrayList<JobMasterModel> getRecommendedAppliedJobList(String tableName, String idGift, String ids, String status_) {
        String selectQuery = "";
        ArrayList<JobMasterModel> subList = new ArrayList<>();

        if (ids.equalsIgnoreCase("")) {
            selectQuery = "SELECT  * FROM " + tableName + " WHERE " + post_req_status + " = " + idGift + " AND " + status + " = " + status_ + " ORDER BY " + modify_date + " DESC LIMIT " + 100000000;
        } else {
            selectQuery = "SELECT  * FROM " + tableName + " WHERE " + post_req_status + " = " + idGift + " AND " + id + " = " + ids + " AND " + status + " = " + status_ + " ORDER BY " + modify_date + " DESC LIMIT " + 100000000;
        }


        // String selectQuery = "SELECT  * FROM " + tableName + "";
        Log.e("QUERY", selectQuery);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        while (cursor.moveToNext()) {
            JobMasterModel eventlist = new JobMasterModel();
            // eventlist.setEVENT_ID_local(Integer.parseInt(cursor.getString(0)));

            eventlist.setId(cursor.getString(0));
            eventlist.setJob_id(cursor.getString(1));
            eventlist.setMedical_profile_id(cursor.getString(2));
            eventlist.setMedical_profile_name(cursor.getString(3));
            eventlist.setJob_title(cursor.getString(4));
            eventlist.setJob_type(cursor.getString(5));
            eventlist.setJob_type_name(cursor.getString(6));
            eventlist.setSpecialization_id(cursor.getString(7));
            eventlist.setSpecialization_name(cursor.getString(8));
            eventlist.setSub_specialization_id(cursor.getString(9));
            eventlist.setSub_specialization_name(cursor.getString(10));
            eventlist.setYear_of_experience(cursor.getString(11));
            eventlist.setCurrent_ctc(cursor.getString(12));
            eventlist.setExpected_ctc(cursor.getString(13));
            eventlist.setCurrent_employer(cursor.getString(14));
            eventlist.setLocation(cursor.getString(15));
            eventlist.setPreferred_location(cursor.getString(16));
            eventlist.setResume(cursor.getString(17));
            eventlist.setJob_description(cursor.getString(18));
            eventlist.setSkills_required(cursor.getString(19));
            eventlist.setAdd_date(cursor.getString(20));
            eventlist.setModify_date(cursor.getString(21));
            eventlist.setPost_req_status(cursor.getString(22));
            eventlist.setDepartment_id(cursor.getString(23));
            eventlist.setDepartment_name(cursor.getString(24));
            eventlist.setApplied_date(cursor.getString(25));
            eventlist.setApplied_status(cursor.getString(26));
            eventlist.setShortlist_status(cursor.getString(27));
            eventlist.setStatus(cursor.getString(28));
            eventlist.setApp_jobId(cursor.getString(29));
            eventlist.setApp_medical_profile_id(cursor.getString(30));
            eventlist.setApp_medical_profile_name(cursor.getString(31));
            eventlist.setApp_job_title(cursor.getString(32));
            eventlist.setApp_job_title_id(cursor.getString(33));
            eventlist.setApp_specialization_id(cursor.getString(34));
            eventlist.setApp_specialization_name(cursor.getString(35));
            eventlist.setApp_sub_specialization_id(cursor.getString(36));
            eventlist.setApp_sub_specialization_name(cursor.getString(37));
            eventlist.setApp_year_of_experience(cursor.getString(38));
            eventlist.setApp_current_ctc(cursor.getString(39));
            eventlist.setApp_expected_ctc(cursor.getString(40));
            eventlist.setResume_description(cursor.getString(41));
            eventlist.setApp_department_id(cursor.getString(42));
            eventlist.setApp_department_name(cursor.getString(43));
            eventlist.setName(cursor.getString(44));
            eventlist.setJob_title_id(cursor.getString(45));
            subList.add(eventlist);
        }
        Log.e("No_of_column", "-==" + cursor.getColumnCount());
        // Log.e("No_of_Giftttt","-=="+cursor.getString(0));
        cursor.close();
        Log.e("SIZE", " SIZE " + subList.size());
        return subList;
    }

    public void deleteTableName(String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean b = doesTableExist(db, tableName);
        if (b)
            db.execSQL("DELETE FROM " + tableName);
        db.close();
    }

    public boolean doesTableExist(SQLiteDatabase db, String tableName) {
        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + tableName + "'", null);

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }

    public void dropTable(String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE " + TABLE_CASE_STUDY_LIST_MASTER);

        String CASE_STUDY_LIST_MASTER = "CREATE TABLE " + TABLE_CASE_STUDY_LIST_MASTER
                + " (" + id + " TEXT,"
                + case_name + " TEXT,"
                + add_date + " TEXT,"
                + modify_date + " TEXT,"
                + status + " TEXT"
                + ") ";
        db.execSQL(CASE_STUDY_LIST_MASTER);
        db.close();

    }

    public ArrayList<DashBoardProductModel> getMarketProductsDetails(String tableName, String idGift) {

        ArrayList<DashBoardProductModel> subList = new ArrayList<>();


        String selectQuery = "SELECT  * FROM " + tableName + " WHERE " + product_id + " = '" + idGift + "'";

        Log.e("QUERY", selectQuery);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        while (cursor.moveToNext()) {
            DashBoardProductModel eventlist = new DashBoardProductModel();
            // eventlist.setEVENT_ID_local(Integer.parseInt(cursor.getString(0)));

            eventlist.setProduct_id(cursor.getString(0));
            eventlist.setProduct_name(cursor.getString(1));
            eventlist.setCategory_id(cursor.getString(2));
            eventlist.setCategory_name(cursor.getString(3));
            eventlist.setPrice(cursor.getString(4));
            eventlist.setCondition_type(cursor.getString(5));
            eventlist.setCondition(cursor.getString(6));
            eventlist.setAge(cursor.getString(7));
            eventlist.setRooms(cursor.getString(8));
            eventlist.setSpecific_locality(cursor.getString(9));
            eventlist.setLand_length(cursor.getString(10));
            eventlist.setLand_width(cursor.getString(11));
            eventlist.setTotal_area(cursor.getString(12));
            eventlist.setBuild_up_area(cursor.getString(13));
            eventlist.setType(cursor.getString(14));
            eventlist.setPrimary_type(cursor.getString(15));
            eventlist.setLicence(cursor.getString(16));
            eventlist.setNo_of_bed(cursor.getString(17));
            eventlist.setSpecification(cursor.getString(18));
            eventlist.setDeal_type(cursor.getString(19));
            eventlist.setDescription(cursor.getString(20));
            eventlist.setProduct_image(cursor.getString(21));
            eventlist.setAdd_date(cursor.getString(22));
            eventlist.setModify_date(cursor.getString(23));
            eventlist.setSeeking_name(cursor.getString(24));
            eventlist.setStatus(cursor.getString(25));
            subList.add(eventlist);
        }

        Log.e("No_of_column", "-==" + cursor.getColumnCount());
        // Log.e("No_of_Giftttt","-=="+cursor.getString(0));
        cursor.close();
        Log.e("SIZE", " SIZE " + subList.size());
        return subList;
    }

    public ArrayList<ConferenceDetailsModel> getConferenceLoaction(String tableName, String loc) {

        ArrayList<ConferenceDetailsModel> subList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + tableName + " WHERE " + venue + " like '%" + loc + "%'";
        Log.e("QUERYyyy", selectQuery);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        while (cursor.moveToNext()) {
            ConferenceDetailsModel eventlist = new ConferenceDetailsModel();
            eventlist.setId(cursor.getString(0));
            eventlist.setConference_id(cursor.getString(1));
            eventlist.setConference_name(cursor.getString(2));
            eventlist.setFrom_date(cursor.getString(3));
            eventlist.setTo_date(cursor.getString(4));
            eventlist.setFrom_time(cursor.getString(5));
            eventlist.setTo_time(cursor.getString(6));
            eventlist.setVenue(cursor.getString(7));
            eventlist.setEvent_host_name(cursor.getString(8));
            eventlist.setSpeciality(cursor.getString(9));
            eventlist.setContact(cursor.getString(10));
            eventlist.setLocation(cursor.getString(11));
            eventlist.setLatitude(cursor.getString(12));
            eventlist.setLogitude(cursor.getString(13));
            eventlist.setBrochuers_file(cursor.getString(14));
            eventlist.setBrochuers_charge(cursor.getString(15));
            eventlist.setBrochuers_days(cursor.getString(16));
            eventlist.setRegistration_fee(cursor.getString(17));
            eventlist.setRegistration_days(cursor.getString(18));
            eventlist.setEvent_sponcer(cursor.getString(19));
            eventlist.setParticular_country_id(cursor.getString(20));
            eventlist.setParticular_country_name(cursor.getString(21));
            eventlist.setParticular_state_id(cursor.getString(22));
            eventlist.setParticular_state_name(cursor.getString(23));
            eventlist.setStatus(cursor.getString(24));
            eventlist.setAdd_date(cursor.getString(25));
            eventlist.setModify_date(cursor.getString(26));
            eventlist.setPayment_status(cursor.getString(27));
            eventlist.setShow_like(cursor.getString(28));
            subList.add(eventlist);
        }

        Log.e("No_of_column", "-==" + cursor.getColumnCount());
        // Log.e("No_of_Giftttt","-=="+cursor.getString(0));
        cursor.close();
        Log.e("SIZE", " SIZE " + subList.size());
        return subList;
    }

    public ArrayList<ConferenceDetailsModel> getConferenceState(String tableName, String stateName) {

        ArrayList<ConferenceDetailsModel> subList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + tableName + " WHERE " + particular_state_name + " = '" + stateName + "'";
        Log.e("QUERY", selectQuery);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        while (cursor.moveToNext()) {
            ConferenceDetailsModel eventlist = new ConferenceDetailsModel();
            eventlist.setId(cursor.getString(0));
            eventlist.setConference_id(cursor.getString(1));
            eventlist.setConference_name(cursor.getString(2));
            eventlist.setFrom_date(cursor.getString(3));
            eventlist.setTo_date(cursor.getString(4));
            eventlist.setFrom_time(cursor.getString(5));
            eventlist.setTo_time(cursor.getString(6));
            eventlist.setVenue(cursor.getString(7));
            eventlist.setEvent_host_name(cursor.getString(8));
            eventlist.setSpeciality(cursor.getString(9));
            eventlist.setContact(cursor.getString(10));
            eventlist.setLocation(cursor.getString(11));
            eventlist.setLatitude(cursor.getString(12));
            eventlist.setLogitude(cursor.getString(13));
            eventlist.setBrochuers_file(cursor.getString(14));
            eventlist.setBrochuers_charge(cursor.getString(15));
            eventlist.setBrochuers_days(cursor.getString(16));
            eventlist.setRegistration_fee(cursor.getString(17));
            eventlist.setRegistration_days(cursor.getString(18));
            eventlist.setEvent_sponcer(cursor.getString(19));
            eventlist.setParticular_country_id(cursor.getString(20));
            eventlist.setParticular_country_name(cursor.getString(21));
            eventlist.setParticular_state_id(cursor.getString(22));
            eventlist.setParticular_state_name(cursor.getString(23));
            eventlist.setStatus(cursor.getString(24));
            eventlist.setAdd_date(cursor.getString(25));
            eventlist.setModify_date(cursor.getString(26));
            eventlist.setPayment_status(cursor.getString(27));
            eventlist.setShow_like(cursor.getString(28));
            eventlist.setParticular_city_id(cursor.getString(29));
            eventlist.setParticular_city_name(cursor.getString(30));
            eventlist.setAvailable_seat(cursor.getString(31));
            eventlist.setConference_description(cursor.getString(32));
            eventlist.setConference_pack_day(cursor.getString(33));
            eventlist.setConference_pack_charge(cursor.getString(34));
            eventlist.setConference_type_id(cursor.getString(35));
            eventlist.setMedical_profile_id(cursor.getString(36));
            eventlist.setCredit_earnings(cursor.getString(37));
            eventlist.setPayment_mode(cursor.getString(38));
            eventlist.setTotal_days_price(cursor.getString(39));
            eventlist.setAccomdation(cursor.getString(40));
            eventlist.setMember_concessions(cursor.getString(41));
            eventlist.setStudent_concessions(cursor.getString(42));
            eventlist.setPrice_hike_after_date(cursor.getString(43));
            eventlist.setPrice_hike_after_percentage(cursor.getString(44));
            subList.add(eventlist);
        }

        Log.e("No_of_column", "-==" + cursor.getColumnCount());
        // Log.e("No_of_Giftttt","-=="+cursor.getString(0));
        cursor.close();
        Log.e("SIZE", " SIZE " + subList.size());
        return subList;
    }

//    public void AddSaveConferenceDetails(ConferenceDetailsModel jobMasterModel, boolean insert) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        //TODO: here we are putting the data using the content values
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(id, jobMasterModel.getId());
//        contentValues.put(conference_id, jobMasterModel.getConference_id());
//        contentValues.put(conference_name, jobMasterModel.getConference_name());
//        contentValues.put(from_date, jobMasterModel.getFrom_date());
//        contentValues.put(to_date, jobMasterModel.getTo_date());
//        contentValues.put(from_time, jobMasterModel.getFrom_time());
//        contentValues.put(to_time, jobMasterModel.getTo_time());
//        contentValues.put(venue, jobMasterModel.getVenue());
//        contentValues.put(event_host_name, jobMasterModel.getEvent_host_name());
//        contentValues.put(speciality, jobMasterModel.getSpeciality());
//        contentValues.put(contact, jobMasterModel.getContact());
//        contentValues.put(location, jobMasterModel.getLocation());
//        contentValues.put(latitude, jobMasterModel.getLatitude());
//        contentValues.put(logitude, jobMasterModel.getLogitude());
//        contentValues.put(brochuers_file, jobMasterModel.getBrochuers_file());
//        contentValues.put(brochuers_charge, jobMasterModel.getBrochuers_charge());
//        contentValues.put(brochuers_days, jobMasterModel.getBrochuers_days());
//        contentValues.put(registration_fee, jobMasterModel.getRegistration_fee());
//        contentValues.put(registration_days, jobMasterModel.getRegistration_days());
//        contentValues.put(event_sponcer, jobMasterModel.getEvent_sponcer());
//        contentValues.put(particular_country_id, jobMasterModel.getParticular_country_id());
//        contentValues.put(particular_country_name, jobMasterModel.getParticular_country_name());
//        contentValues.put(particular_state_id, jobMasterModel.getParticular_state_id());
//        contentValues.put(particular_state_name, jobMasterModel.getParticular_state_name());
//        contentValues.put(status, jobMasterModel.getStatus());
//        contentValues.put(add_date, jobMasterModel.getAdd_date());
//        contentValues.put(modify_date, jobMasterModel.getModify_date());
//        contentValues.put(payment_status, jobMasterModel.getPayment_status());
//        contentValues.put(show_like, jobMasterModel.getShow_like());
//        contentValues.put(particular_city_id, jobMasterModel.getParticular_city_id());
//        contentValues.put(particular_city_name, jobMasterModel.getParticular_city_name());
//        contentValues.put(address_type, jobMasterModel.getAddress_type());
//        contentValues.put(available_seat, jobMasterModel.getAvailable_seat());
//        contentValues.put(conference_description, jobMasterModel.getConference_description());
//        contentValues.put(conference_type_id, jobMasterModel.getConference_type_id());
//        contentValues.put(medical_profile_id, jobMasterModel.getMedical_profile_id());
//        contentValues.put(credit_earnings, jobMasterModel.getCredit_earnings());
//        contentValues.put(payment_mode, jobMasterModel.getPayment_mode());
//        contentValues.put(total_days_price, jobMasterModel.getTotal_days_price());
//        contentValues.put(accomdation, jobMasterModel.getAccomdation());
//        contentValues.put(member_concessions, jobMasterModel.getMember_concessions());
//        contentValues.put(student_concessions, jobMasterModel.getStudent_concessions());
//        contentValues.put(price_hike_after_date, jobMasterModel.getPrice_hike_after_date());
//        contentValues.put(price_hike_after_percentage, jobMasterModel.getPrice_hike_after_percentage());
//        //TODO: inserting rows
//        if (insert)
//            db.insert(TABLE_SAVE_CONFERENCES_SHOW_DETAILS, null, contentValues);
//        else
//            db.update(TABLE_SAVE_CONFERENCES_SHOW_DETAILS, contentValues, id + " = ?", new String[]{String.valueOf(jobMasterModel.getId())});
//
//        db.close(); //TODO: closing the database after the insert operation
//        Log.e("conferencedetails", jobMasterModel.getId() + " = " + jobMasterModel.getId());
//
//    }

    public void AddSaveConferenceDetails(ConferenceDetailsModel jobMasterModel, boolean insert) {
        SQLiteDatabase db = this.getWritableDatabase();
        //TODO: here we are putting the data using the content values
        ContentValues contentValues = new ContentValues();
        contentValues.put(id, jobMasterModel.getId());
        contentValues.put(conference_id, jobMasterModel.getConference_id());
        contentValues.put(conference_name, jobMasterModel.getConference_name());
        contentValues.put(from_date, jobMasterModel.getFrom_date());
        contentValues.put(to_date, jobMasterModel.getTo_date());
        contentValues.put(from_time, jobMasterModel.getFrom_time());
        contentValues.put(to_time, jobMasterModel.getTo_time());
        contentValues.put(venue, jobMasterModel.getVenue());
        contentValues.put(event_host_name, jobMasterModel.getEvent_host_name());
        contentValues.put(speciality, jobMasterModel.getSpeciality());
        contentValues.put(contact, jobMasterModel.getContact());
        contentValues.put(location, jobMasterModel.getLocation());
        contentValues.put(latitude, jobMasterModel.getLatitude());
        contentValues.put(logitude, jobMasterModel.getLogitude());
        contentValues.put(brochuers_file, jobMasterModel.getBrochuers_file());
        contentValues.put(brochuers_charge, jobMasterModel.getBrochuers_charge());
        contentValues.put(brochuers_days, jobMasterModel.getBrochuers_days());
        contentValues.put(registration_fee, jobMasterModel.getRegistration_fee());
        contentValues.put(registration_days, jobMasterModel.getRegistration_days());
        contentValues.put(event_sponcer, jobMasterModel.getEvent_sponcer());
        contentValues.put(particular_country_id, jobMasterModel.getParticular_country_id());
        contentValues.put(particular_country_name, jobMasterModel.getParticular_country_name());
        contentValues.put(particular_state_id, jobMasterModel.getParticular_state_id());
        contentValues.put(particular_state_name, jobMasterModel.getParticular_state_name());
        contentValues.put(status, jobMasterModel.getStatus());
        contentValues.put(add_date, jobMasterModel.getAdd_date());
        contentValues.put(modify_date, jobMasterModel.getModify_date());
        contentValues.put(payment_status, jobMasterModel.getPayment_status());
        contentValues.put(show_like, jobMasterModel.getShow_like());
        contentValues.put(particular_city_id, jobMasterModel.getParticular_city_id());
        contentValues.put(particular_city_name, jobMasterModel.getParticular_city_name());
        contentValues.put(available_seat, jobMasterModel.getAvailable_seat());
        contentValues.put(conference_description, jobMasterModel.getConference_description());
        contentValues.put(conference_pack_day, jobMasterModel.getConference_pack_day());
        contentValues.put(conference_pack_charge, jobMasterModel.getConference_pack_charge());
        contentValues.put(conference_type_id, jobMasterModel.getConference_type_id());
        contentValues.put(medical_profile_id, jobMasterModel.getMedical_profile_id());
        contentValues.put(credit_earnings, jobMasterModel.getCredit_earnings());
        contentValues.put(payment_mode, jobMasterModel.getPayment_mode());
        contentValues.put(total_days_price, jobMasterModel.getTotal_days_price());
        contentValues.put(accomdation, jobMasterModel.getAccomdation());
        contentValues.put(member_concessions, jobMasterModel.getMember_concessions());
        contentValues.put(student_concessions, jobMasterModel.getStudent_concessions());
        contentValues.put(price_hike_after_date, jobMasterModel.getPrice_hike_after_date());
        contentValues.put(price_hike_after_percentage, jobMasterModel.getPrice_hike_after_percentage());
        contentValues.put(getConference_type_name, jobMasterModel.getConference_type_name());
        contentValues.put(discount_percentage, jobMasterModel.getDiscount_percentage());
        contentValues.put(discount_description, jobMasterModel.getDiscount_description());
        contentValues.put(gst, jobMasterModel.getGst());
        contentValues.put(medical_profile_name, jobMasterModel.getMedical_profile_name());
        contentValues.put(special_name, jobMasterModel.getTarget_audience_speciality());
        contentValues.put(department_id, jobMasterModel.getDepartment_id());
        contentValues.put(booking_stopped, jobMasterModel.getBooking_stopped());


        //TODO: inserting rows
        if (insert)
            db.insert(TABLE_SAVE_CONFERENCES_SHOW_DETAILS, null, contentValues);
        else
            db.update(TABLE_SAVE_CONFERENCES_SHOW_DETAILS, contentValues, id + " = ?", new String[]{String.valueOf(jobMasterModel.getId())});

        db.close(); //TODO: closing the database after the insert operation
        Log.e("conferencedetails", jobMasterModel.getId() + " = " + jobMasterModel.getId());

    }

    public ArrayList<ConferenceDetailsModel> getConferenceLike(String tableName, String category) {

        ArrayList<ConferenceDetailsModel> subList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + tableName + " WHERE " + conference_id + " = '" + category + "'";
        Log.e("QUERY", selectQuery);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        while (cursor.moveToNext()) {
            ConferenceDetailsModel eventlist = new ConferenceDetailsModel();
            eventlist.setId(cursor.getString(0));
            eventlist.setConference_id(cursor.getString(1));
            eventlist.setConference_name(cursor.getString(2));
            eventlist.setFrom_date(cursor.getString(3));
            eventlist.setTo_date(cursor.getString(4));
            eventlist.setFrom_time(cursor.getString(5));
            eventlist.setTo_time(cursor.getString(6));
            eventlist.setVenue(cursor.getString(7));
            eventlist.setEvent_host_name(cursor.getString(8));
            eventlist.setSpeciality(cursor.getString(9));
            eventlist.setContact(cursor.getString(10));
            eventlist.setLocation(cursor.getString(11));
            eventlist.setLatitude(cursor.getString(12));
            eventlist.setLogitude(cursor.getString(13));
            eventlist.setBrochuers_file(cursor.getString(14));
            eventlist.setBrochuers_charge(cursor.getString(15));
            eventlist.setBrochuers_days(cursor.getString(16));
            eventlist.setRegistration_fee(cursor.getString(17));
            eventlist.setRegistration_days(cursor.getString(18));
            eventlist.setEvent_sponcer(cursor.getString(19));
            eventlist.setParticular_country_id(cursor.getString(20));
            eventlist.setParticular_country_name(cursor.getString(21));
            eventlist.setParticular_state_id(cursor.getString(22));
            eventlist.setParticular_state_name(cursor.getString(23));
            eventlist.setStatus(cursor.getString(24));
            eventlist.setAdd_date(cursor.getString(25));
            eventlist.setModify_date(cursor.getString(26));
            eventlist.setPayment_status(cursor.getString(27));
            eventlist.setShow_like(cursor.getString(28));
            eventlist.setParticular_city_id(cursor.getString(29));
            eventlist.setParticular_city_name(cursor.getString(30));
            subList.add(eventlist);
        }

        Log.e("No_of_column", "-==" + cursor.getColumnCount());
        // Log.e("No_of_Giftttt","-=="+cursor.getString(0));
        cursor.close();
        Log.e("SIZE", " SIZE " + subList.size());
        return subList;
    }

    public ArrayList<ConferenceDetailsModel> getConferenceCountry(String tableName, String countryName) {

        ArrayList<ConferenceDetailsModel> subList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + tableName + " WHERE " + particular_country_name + " = '" + countryName + "'";

        //  String selectQuery = "SELECT  * FROM " + tableName + " WHERE " + particular_country_id + " = " + country_id + "";
        Log.e("QUERY", selectQuery);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        while (cursor.moveToNext()) {
            ConferenceDetailsModel eventlist = new ConferenceDetailsModel();
            eventlist.setId(cursor.getString(0));
            eventlist.setConference_id(cursor.getString(1));
            eventlist.setConference_name(cursor.getString(2));
            eventlist.setFrom_date(cursor.getString(3));
            eventlist.setTo_date(cursor.getString(4));
            eventlist.setFrom_time(cursor.getString(5));
            eventlist.setTo_time(cursor.getString(6));
            eventlist.setVenue(cursor.getString(7));
            eventlist.setEvent_host_name(cursor.getString(8));
            eventlist.setSpeciality(cursor.getString(9));
            eventlist.setContact(cursor.getString(10));
            eventlist.setLocation(cursor.getString(11));
            eventlist.setLatitude(cursor.getString(12));
            eventlist.setLogitude(cursor.getString(13));
            eventlist.setBrochuers_file(cursor.getString(14));
            eventlist.setBrochuers_charge(cursor.getString(15));
            eventlist.setBrochuers_days(cursor.getString(16));
            eventlist.setRegistration_fee(cursor.getString(17));
            eventlist.setRegistration_days(cursor.getString(18));
            eventlist.setEvent_sponcer(cursor.getString(19));
            eventlist.setParticular_country_id(cursor.getString(20));
            eventlist.setParticular_country_name(cursor.getString(21));
            eventlist.setParticular_state_id(cursor.getString(22));
            eventlist.setParticular_state_name(cursor.getString(23));
            eventlist.setStatus(cursor.getString(24));
            eventlist.setAdd_date(cursor.getString(25));
            eventlist.setModify_date(cursor.getString(26));
            eventlist.setPayment_status(cursor.getString(27));
            eventlist.setShow_like(cursor.getString(28));
            eventlist.setParticular_city_id(cursor.getString(29));
            eventlist.setParticular_city_name(cursor.getString(30));
            eventlist.setAvailable_seat(cursor.getString(31));
            eventlist.setConference_description(cursor.getString(32));
            eventlist.setConference_pack_day(cursor.getString(33));
            eventlist.setConference_pack_charge(cursor.getString(34));
            eventlist.setConference_type_id(cursor.getString(35));
            eventlist.setMedical_profile_id(cursor.getString(36));
            eventlist.setCredit_earnings(cursor.getString(37));
            eventlist.setPayment_mode(cursor.getString(38));
            eventlist.setTotal_days_price(cursor.getString(39));
            eventlist.setAccomdation(cursor.getString(40));
            eventlist.setMember_concessions(cursor.getString(41));
            eventlist.setStudent_concessions(cursor.getString(42));
            eventlist.setPrice_hike_after_date(cursor.getString(43));
            eventlist.setPrice_hike_after_percentage(cursor.getString(44));
            subList.add(eventlist);
        }

        Log.e("No_of_column", "-==" + cursor.getColumnCount());
        // Log.e("No_of_Giftttt","-=="+cursor.getString(0));
        cursor.close();
        Log.e("SIZE", " SIZE " + subList.size());
        return subList;
    }


//    public ArrayList<ConferenceDetailsModel> getGoingConferenceDetails(String tableName, String idGift) {
//
//        ArrayList<ConferenceDetailsModel> subList = new ArrayList<>();
//
//        String selectQuery = "SELECT  * FROM " + tableName + " ORDER BY " + modify_date + " DESC LIMIT " + 100000000;
//        Log.e("QUERY", selectQuery);
//
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, null);
//
//
//        while (cursor.moveToNext()) {
//            ConferenceDetailsModel eventlist = new ConferenceDetailsModel();
//            eventlist.setId(cursor.getString(0));
//            eventlist.setConference_id(cursor.getString(1));
//            eventlist.setConference_name(cursor.getString(2));
//            eventlist.setFrom_date(cursor.getString(3));
//            eventlist.setTo_date(cursor.getString(4));
//            eventlist.setFrom_time(cursor.getString(5));
//            eventlist.setTo_time(cursor.getString(6));
//            eventlist.setVenue(cursor.getString(7));
//            eventlist.setEvent_host_name(cursor.getString(8));
//            eventlist.setSpeciality(cursor.getString(9));
//            eventlist.setContact(cursor.getString(10));
//            eventlist.setLocation(cursor.getString(11));
//            eventlist.setLatitude(cursor.getString(12));
//            eventlist.setLogitude(cursor.getString(13));
//            eventlist.setBrochuers_file(cursor.getString(14));
//            eventlist.setBrochuers_charge(cursor.getString(15));
//            eventlist.setBrochuers_days(cursor.getString(16));
//            eventlist.setRegistration_fee(cursor.getString(17));
//            eventlist.setRegistration_days(cursor.getString(18));
//            eventlist.setEvent_sponcer(cursor.getString(19));
//            eventlist.setParticular_country_id(cursor.getString(20));
//            eventlist.setParticular_country_name(cursor.getString(21));
//            eventlist.setParticular_state_id(cursor.getString(22));
//            eventlist.setParticular_state_name(cursor.getString(23));
//            eventlist.setStatus(cursor.getString(24));
//            eventlist.setAdd_date(cursor.getString(25));
//            eventlist.setModify_date(cursor.getString(26));
//            eventlist.setPayment_status(cursor.getString(27));
//            eventlist.setShow_like(cursor.getString(28));
//            eventlist.setParticular_city_id(cursor.getString(29));
//            eventlist.setParticular_city_name(cursor.getString(30));
//            eventlist.setAddress_type(cursor.getString(31));
//            eventlist.setAvailable_seat(cursor.getString(32));
//            eventlist.setConference_description(cursor.getString(33));
//            eventlist.setConference_type_id(cursor.getString(34));
//            eventlist.setMedical_profile_id(cursor.getString(35));
//            eventlist.setCredit_earnings(cursor.getString(36));
//            eventlist.setPayment_mode(cursor.getString(37));
//            eventlist.setTotal_days_price(cursor.getString(38));
//            eventlist.setAccomdation(cursor.getString(39));
//            eventlist.setMember_concessions(cursor.getString(40));
//            eventlist.setStudent_concessions(cursor.getString(41));
//            eventlist.setPrice_hike_after_date(cursor.getString(42));
//            eventlist.setPrice_hike_after_percentage(cursor.getString(43));
//            subList.add(eventlist);
//        }
//
//        Log.e("No_of_column", "-==" + cursor.getColumnCount());
//        // Log.e("No_of_Giftttt","-=="+cursor.getString(0));
//        cursor.close();
//        Log.e("SIZE", " SIZE " + subList.size());
//        return subList;
//    }

    public ArrayList<ConferenceDetailsModel> getConferenceCity(String tableName, String stateName) {

        ArrayList<ConferenceDetailsModel> subList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + tableName + " WHERE " + particular_city_name + " = '" + stateName + "'";
        Log.e("QUERY", selectQuery);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        while (cursor.moveToNext()) {
            ConferenceDetailsModel eventlist = new ConferenceDetailsModel();
            eventlist.setId(cursor.getString(0));
            eventlist.setConference_id(cursor.getString(1));
            eventlist.setConference_name(cursor.getString(2));
            eventlist.setFrom_date(cursor.getString(3));
            eventlist.setTo_date(cursor.getString(4));
            eventlist.setFrom_time(cursor.getString(5));
            eventlist.setTo_time(cursor.getString(6));
            eventlist.setVenue(cursor.getString(7));
            eventlist.setEvent_host_name(cursor.getString(8));
            eventlist.setSpeciality(cursor.getString(9));
            eventlist.setContact(cursor.getString(10));
            eventlist.setLocation(cursor.getString(11));
            eventlist.setLatitude(cursor.getString(12));
            eventlist.setLogitude(cursor.getString(13));
            eventlist.setBrochuers_file(cursor.getString(14));
            eventlist.setBrochuers_charge(cursor.getString(15));
            eventlist.setBrochuers_days(cursor.getString(16));
            eventlist.setRegistration_fee(cursor.getString(17));
            eventlist.setRegistration_days(cursor.getString(18));
            eventlist.setEvent_sponcer(cursor.getString(19));
            eventlist.setParticular_country_id(cursor.getString(20));
            eventlist.setParticular_country_name(cursor.getString(21));
            eventlist.setParticular_state_id(cursor.getString(22));
            eventlist.setParticular_state_name(cursor.getString(23));
            eventlist.setStatus(cursor.getString(24));
            eventlist.setAdd_date(cursor.getString(25));
            eventlist.setModify_date(cursor.getString(26));
            eventlist.setPayment_status(cursor.getString(27));
            eventlist.setShow_like(cursor.getString(28));
            eventlist.setParticular_city_id(cursor.getString(29));
            eventlist.setParticular_city_name(cursor.getString(30));
            eventlist.setAvailable_seat(cursor.getString(31));
            eventlist.setConference_description(cursor.getString(32));
            eventlist.setConference_pack_day(cursor.getString(33));
            eventlist.setConference_pack_charge(cursor.getString(34));
            eventlist.setConference_type_id(cursor.getString(35));
            eventlist.setMedical_profile_id(cursor.getString(36));
            eventlist.setCredit_earnings(cursor.getString(37));
            eventlist.setPayment_mode(cursor.getString(38));
            eventlist.setTotal_days_price(cursor.getString(39));
            eventlist.setAccomdation(cursor.getString(40));
            eventlist.setMember_concessions(cursor.getString(41));
            eventlist.setStudent_concessions(cursor.getString(42));
            eventlist.setPrice_hike_after_date(cursor.getString(43));
            eventlist.setPrice_hike_after_percentage(cursor.getString(44));
            subList.add(eventlist);
        }

        Log.e("No_of_column", "-==" + cursor.getColumnCount());
        // Log.e("No_of_Giftttt","-=="+cursor.getString(0));
        cursor.close();
        Log.e("SIZE", " SIZE " + subList.size());
        return subList;
    }

    public ArrayList<ConferenceDetailsModel> getSaveConferenceDetails(String tableName, String idGift) {

        ArrayList<ConferenceDetailsModel> subList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + tableName + " WHERE " + show_like + " = '" + idGift + "'";
        //  String selectQuery = "SELECT  * FROM " + tableName + " ORDER BY " + modify_date + " DESC LIMIT " + 100000000;
        Log.e("QUERY", selectQuery);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        while (cursor.moveToNext()) {
            ConferenceDetailsModel eventlist = new ConferenceDetailsModel();
            eventlist.setId(cursor.getString(0));
            eventlist.setConference_id(cursor.getString(1));
            eventlist.setConference_name(cursor.getString(2));
            eventlist.setFrom_date(cursor.getString(3));
            eventlist.setTo_date(cursor.getString(4));
            eventlist.setFrom_time(cursor.getString(5));
            eventlist.setTo_time(cursor.getString(6));
            eventlist.setVenue(cursor.getString(7));
            eventlist.setEvent_host_name(cursor.getString(8));
            eventlist.setSpeciality(cursor.getString(9));
            eventlist.setContact(cursor.getString(10));
            eventlist.setLocation(cursor.getString(11));
            eventlist.setLatitude(cursor.getString(12));
            eventlist.setLogitude(cursor.getString(13));
            eventlist.setBrochuers_file(cursor.getString(14));
            eventlist.setBrochuers_charge(cursor.getString(15));
            eventlist.setBrochuers_days(cursor.getString(16));
            eventlist.setRegistration_fee(cursor.getString(17));
            eventlist.setRegistration_days(cursor.getString(18));
            eventlist.setEvent_sponcer(cursor.getString(19));
            eventlist.setParticular_country_id(cursor.getString(20));
            eventlist.setParticular_country_name(cursor.getString(21));
            eventlist.setParticular_state_id(cursor.getString(22));
            eventlist.setParticular_state_name(cursor.getString(23));
            eventlist.setStatus(cursor.getString(24));
            eventlist.setAdd_date(cursor.getString(25));
            eventlist.setModify_date(cursor.getString(26));
            eventlist.setPayment_status(cursor.getString(27));
            eventlist.setShow_like(cursor.getString(28));
            eventlist.setParticular_city_id(cursor.getString(29));
            eventlist.setParticular_city_name(cursor.getString(30));
            eventlist.setAddress_type(cursor.getString(31));
            eventlist.setAvailable_seat(cursor.getString(32));
            eventlist.setConference_description(cursor.getString(33));
            eventlist.setConference_type_id(cursor.getString(34));
            eventlist.setMedical_profile_id(cursor.getString(35));
            eventlist.setCredit_earnings(cursor.getString(36));
            eventlist.setPayment_mode(cursor.getString(37));
            eventlist.setTotal_days_price(cursor.getString(38));
            eventlist.setAccomdation(cursor.getString(39));
            eventlist.setMember_concessions(cursor.getString(40));
            eventlist.setStudent_concessions(cursor.getString(41));
            eventlist.setPrice_hike_after_date(cursor.getString(42));
            eventlist.setPrice_hike_after_percentage(cursor.getString(43));
            subList.add(eventlist);
        }

        Log.e("No_of_column", "-==" + cursor.getColumnCount());
        // Log.e("No_of_Giftttt","-=="+cursor.getString(0));
        cursor.close();
        Log.e("SIZE", " SIZE " + subList.size());
        return subList;
    }

    public void AddConferenceDetails(ConferenceDetailsModel jobMasterModel, boolean insert) {
        SQLiteDatabase db = this.getWritableDatabase();
        //TODO: here we are putting the data using the content values
        ContentValues contentValues = new ContentValues();
        contentValues.put(id, jobMasterModel.getId());
        contentValues.put(conference_id, jobMasterModel.getConference_id());
        contentValues.put(conference_name, jobMasterModel.getConference_name());
        contentValues.put(from_date, jobMasterModel.getFrom_date());
        contentValues.put(to_date, jobMasterModel.getTo_date());
        contentValues.put(from_time, jobMasterModel.getFrom_time());
        contentValues.put(to_time, jobMasterModel.getTo_time());
        contentValues.put(venue, jobMasterModel.getVenue());
        contentValues.put(event_host_name, jobMasterModel.getEvent_host_name());
        contentValues.put(speciality, jobMasterModel.getSpeciality());
        contentValues.put(contact, jobMasterModel.getContact());
        contentValues.put(location, jobMasterModel.getLocation());
        contentValues.put(latitude, jobMasterModel.getLatitude());
        contentValues.put(logitude, jobMasterModel.getLogitude());
        contentValues.put(brochuers_file, jobMasterModel.getBrochuers_file());
        contentValues.put(brochuers_charge, jobMasterModel.getBrochuers_charge());
        contentValues.put(brochuers_days, jobMasterModel.getBrochuers_days());
        contentValues.put(registration_fee, jobMasterModel.getRegistration_fee());
        contentValues.put(registration_days, jobMasterModel.getRegistration_days());
        contentValues.put(event_sponcer, jobMasterModel.getEvent_sponcer());
        contentValues.put(particular_country_id, jobMasterModel.getParticular_country_id());
        contentValues.put(particular_country_name, jobMasterModel.getParticular_country_name());
        contentValues.put(particular_state_id, jobMasterModel.getParticular_state_id());
        contentValues.put(particular_state_name, jobMasterModel.getParticular_state_name());
        contentValues.put(status, jobMasterModel.getStatus());
        contentValues.put(add_date, jobMasterModel.getAdd_date());
        contentValues.put(modify_date, jobMasterModel.getModify_date());
        contentValues.put(payment_status, jobMasterModel.getPayment_status());
        contentValues.put(show_like, jobMasterModel.getShow_like());
        contentValues.put(particular_city_id, jobMasterModel.getParticular_city_id());
        contentValues.put(particular_city_name, jobMasterModel.getParticular_city_name());
        contentValues.put(available_seat, jobMasterModel.getAvailable_seat());
        contentValues.put(conference_description, jobMasterModel.getConference_description());
        contentValues.put(conference_pack_day, jobMasterModel.getConference_pack_day());
        contentValues.put(conference_pack_charge, jobMasterModel.getConference_pack_charge());
        contentValues.put(conference_type_id, jobMasterModel.getConference_type_id());
        contentValues.put(medical_profile_id, jobMasterModel.getMedical_profile_id());
        contentValues.put(credit_earnings, jobMasterModel.getCredit_earnings());
        contentValues.put(payment_mode, jobMasterModel.getPayment_mode());
        contentValues.put(total_days_price, jobMasterModel.getTotal_days_price());
        contentValues.put(accomdation, jobMasterModel.getAccomdation());
        contentValues.put(member_concessions, jobMasterModel.getMember_concessions());
        contentValues.put(student_concessions, jobMasterModel.getStudent_concessions());
        contentValues.put(price_hike_after_date, jobMasterModel.getPrice_hike_after_date());
        contentValues.put(price_hike_after_percentage, jobMasterModel.getPrice_hike_after_percentage());
        contentValues.put(getConference_type_name, jobMasterModel.getConference_type_name());
        contentValues.put(discount_percentage, jobMasterModel.getDiscount_percentage());
        contentValues.put(discount_description, jobMasterModel.getDiscount_description());
        contentValues.put(gst, jobMasterModel.getGst());
        contentValues.put(booking_stopped, jobMasterModel.getBooking_stopped());
        contentValues.put(special_name, jobMasterModel.getTarget_audience_speciality());
        contentValues.put(medical_profile_name, jobMasterModel.getMedical_profile_name());
        contentValues.put(department_id, jobMasterModel.getDepartment_id());
        contentValues.put(department_name, jobMasterModel.getDepartment_name());
        contentValues.put(activate_status, jobMasterModel.getActivate_status());

        //TODO: inserting rows
        if (insert)
            db.insert(TABLE_CONFERENCES_SHOW_DETAILS, null, contentValues);
        else
            db.update(TABLE_CONFERENCES_SHOW_DETAILS, contentValues, id + " = ?", new String[]{String.valueOf(jobMasterModel.getId())});

        db.close(); //TODO: closing the database after the insert operation
        Log.e("conferencedetails", jobMasterModel.getId() + " = " + jobMasterModel.getId());

    }


//    public ArrayList<ConferenceDetailsModel> getMyConferenceDetails(String tableName, String idGift) {
//
//        ArrayList<ConferenceDetailsModel> subList = new ArrayList<>();
//
//        String selectQuery = "SELECT  * FROM " + tableName + " ORDER BY " + modify_date + " DESC LIMIT " + 100000000;
//        //String selectQuery = "SELECT  * FROM " + tableName;
//        Log.e("QUERYYYY", selectQuery);
//
////        ArrayList<ConferenceDetailsModel> subList = new ArrayList<>();
////
////        String selectQuery = "SELECT  * FROM " + tableName + " ORDER BY " + modify_date + " DESC LIMIT " + 100000000;
////        Log.e("QUERY", selectQuery);
//
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, null);
//
//
//        while (cursor.moveToNext()) {
//            ConferenceDetailsModel eventlist = new ConferenceDetailsModel();
//            eventlist.setId(cursor.getString(0));
//            eventlist.setConference_id(cursor.getString(1));
//            eventlist.setConference_name(cursor.getString(2));
//            eventlist.setFrom_date(cursor.getString(3));
//            eventlist.setTo_date(cursor.getString(4));
//            eventlist.setFrom_time(cursor.getString(5));
//            eventlist.setTo_time(cursor.getString(6));
//            eventlist.setVenue(cursor.getString(7));
//            eventlist.setEvent_host_name(cursor.getString(8));
//            eventlist.setSpeciality(cursor.getString(9));
//            eventlist.setContact(cursor.getString(10));
//            eventlist.setLocation(cursor.getString(11));
//            eventlist.setLatitude(cursor.getString(12));
//            eventlist.setLogitude(cursor.getString(13));
//            eventlist.setBrochuers_file(cursor.getString(14));
//            eventlist.setBrochuers_charge(cursor.getString(15));
//            eventlist.setBrochuers_days(cursor.getString(16));
//            eventlist.setRegistration_fee(cursor.getString(17));
//            eventlist.setRegistration_days(cursor.getString(18));
//            eventlist.setEvent_sponcer(cursor.getString(19));
//            eventlist.setParticular_country_id(cursor.getString(20));
//            eventlist.setParticular_country_name(cursor.getString(21));
//            eventlist.setParticular_state_id(cursor.getString(22));
//            eventlist.setParticular_state_name(cursor.getString(23));
//            eventlist.setStatus(cursor.getString(24));
//            eventlist.setAdd_date(cursor.getString(25));
//            eventlist.setModify_date(cursor.getString(26));
//            eventlist.setRegistered(cursor.getString(27));
//            eventlist.setInterested(cursor.getString(28));
//            eventlist.setViews(cursor.getString(29));
//            eventlist.setCost(cursor.getString(30));
//            eventlist.setDuration(cursor.getString(31));
//            eventlist.setPayment_status(cursor.getString(32));
//            eventlist.setShow_like(cursor.getString(33));
//            eventlist.setParticular_city_id(cursor.getString(34));
//            eventlist.setParticular_city_name(cursor.getString(35));
//            eventlist.setAddress_type(cursor.getString(36));
//            eventlist.setAvailable_seat(cursor.getString(37));
//            eventlist.setConference_description(cursor.getString(38));
//            eventlist.setConference_type_id(cursor.getString(39));
//            eventlist.setMedical_profile_id(cursor.getString(40));
//            eventlist.setCredit_earnings(cursor.getString(41));
//            eventlist.setPayment_mode(cursor.getString(42));
//            eventlist.setTotal_days_price(cursor.getString(43));
//            eventlist.setAccomdation(cursor.getString(44));
//            eventlist.setMember_concessions(cursor.getString(45));
//            eventlist.setStudent_concessions(cursor.getString(46));
//            eventlist.setPrice_hike_after_date(cursor.getString(47));
//            eventlist.setPrice_hike_after_percentage(cursor.getString(48));
//            subList.add(eventlist);
//        }
//
//        Log.e("No_of_column", "-==" + cursor.getColumnCount());
//        // Log.e("No_of_Giftttt","-=="+cursor.getString(0));
//        cursor.close();
//        Log.e("SIZE", " SIZE " + subList.size());
//        return subList;
//    }

//    public ArrayList<ConferenceDetailsModel> getConferenceDetails(String tableName, String idGift) {
//
//        ArrayList<ConferenceDetailsModel> subList = new ArrayList<>();
//
//        String selectQuery = "SELECT  * FROM " + tableName + " ORDER BY " + modify_date + " DESC LIMIT " + 100000000;
//        Log.e("QUERY", selectQuery);
//
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, null);
//
//
//        while (cursor.moveToNext()) {
//            ConferenceDetailsModel eventlist = new ConferenceDetailsModel();
//            eventlist.setId(cursor.getString(0));
//            eventlist.setConference_id(cursor.getString(1));
//            eventlist.setConference_name(cursor.getString(2));
//            eventlist.setFrom_date(cursor.getString(3));
//            eventlist.setTo_date(cursor.getString(4));
//            eventlist.setFrom_time(cursor.getString(5));
//            eventlist.setTo_time(cursor.getString(6));
//            eventlist.setVenue(cursor.getString(7));
//            eventlist.setEvent_host_name(cursor.getString(8));
//            eventlist.setSpeciality(cursor.getString(9));
//            eventlist.setContact(cursor.getString(10));
//            eventlist.setLocation(cursor.getString(11));
//            eventlist.setLatitude(cursor.getString(12));
//            eventlist.setLogitude(cursor.getString(13));
//            eventlist.setBrochuers_file(cursor.getString(14));
//            eventlist.setBrochuers_charge(cursor.getString(15));
//            eventlist.setBrochuers_days(cursor.getString(16));
//            eventlist.setRegistration_fee(cursor.getString(17));
//            eventlist.setRegistration_days(cursor.getString(18));
//            eventlist.setEvent_sponcer(cursor.getString(19));
//            eventlist.setParticular_country_id(cursor.getString(20));
//            eventlist.setParticular_country_name(cursor.getString(21));
//            eventlist.setParticular_state_id(cursor.getString(22));
//            eventlist.setParticular_state_name(cursor.getString(23));
//            eventlist.setStatus(cursor.getString(24));
//            eventlist.setAdd_date(cursor.getString(25));
//            eventlist.setModify_date(cursor.getString(26));
//            eventlist.setPayment_status(cursor.getString(27));
//            eventlist.setShow_like(cursor.getString(28));
//            eventlist.setParticular_city_id(cursor.getString(29));
//            eventlist.setParticular_city_name(cursor.getString(30));
//            eventlist.setAvailable_seat(cursor.getString(31));
//            eventlist.setConference_description(cursor.getString(32));
//            eventlist.setConference_pack_day(cursor.getString(33));
//            eventlist.setConference_pack_charge(cursor.getString(34));
//            eventlist.setConference_type_id(cursor.getString(35));
//            eventlist.setMedical_profile_id(cursor.getString(36));
//            eventlist.setCredit_earnings(cursor.getString(37));
//            eventlist.setPayment_mode(cursor.getString(38));
//            eventlist.setTotal_days_price(cursor.getString(39));
//            eventlist.setAccomdation(cursor.getString(40));
//            eventlist.setMember_concessions(cursor.getString(41));
//            eventlist.setStudent_concessions(cursor.getString(42));
//            eventlist.setPrice_hike_after_date(cursor.getString(43));
//            eventlist.setPrice_hike_after_percentage(cursor.getString(44));
//
//            subList.add(eventlist);
//        }
//
//        Log.e("No_of_column", "-==" + cursor.getColumnCount());
//        // Log.e("No_of_Giftttt","-=="+cursor.getString(0));
//        cursor.close();
//        Log.e("SIZE", " SIZE " + subList.size());
//        return subList;
//    }

    public ArrayList<ConferenceDetailsModel> getConferenceCategory(String tableName, String category) {

        ArrayList<ConferenceDetailsModel> subList = new ArrayList<>();


        String selectQuery = "SELECT  * FROM " + tableName + " WHERE " + conference_name + " like '%" + category + "%'";
        Log.e("QUERY", selectQuery);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        while (cursor.moveToNext()) {
            ConferenceDetailsModel eventlist = new ConferenceDetailsModel();
            eventlist.setId(cursor.getString(0));
            eventlist.setConference_id(cursor.getString(1));
            eventlist.setConference_name(cursor.getString(2));
            eventlist.setFrom_date(cursor.getString(3));
            eventlist.setTo_date(cursor.getString(4));
            eventlist.setFrom_time(cursor.getString(5));
            eventlist.setTo_time(cursor.getString(6));
            eventlist.setVenue(cursor.getString(7));
            eventlist.setEvent_host_name(cursor.getString(8));
            eventlist.setSpeciality(cursor.getString(9));
            eventlist.setContact(cursor.getString(10));
            eventlist.setLocation(cursor.getString(11));
            eventlist.setLatitude(cursor.getString(12));
            eventlist.setLogitude(cursor.getString(13));
            eventlist.setBrochuers_file(cursor.getString(14));
            eventlist.setBrochuers_charge(cursor.getString(15));
            eventlist.setBrochuers_days(cursor.getString(16));
            eventlist.setRegistration_fee(cursor.getString(17));
            eventlist.setRegistration_days(cursor.getString(18));
            eventlist.setEvent_sponcer(cursor.getString(19));
            eventlist.setParticular_country_id(cursor.getString(20));
            eventlist.setParticular_country_name(cursor.getString(21));
            eventlist.setParticular_state_id(cursor.getString(22));
            eventlist.setParticular_state_name(cursor.getString(23));
            eventlist.setStatus(cursor.getString(24));
            eventlist.setAdd_date(cursor.getString(25));
            eventlist.setModify_date(cursor.getString(26));
            eventlist.setPayment_status(cursor.getString(27));
            eventlist.setShow_like(cursor.getString(28));
            eventlist.setParticular_city_id(cursor.getString(29));
            eventlist.setParticular_city_name(cursor.getString(30));
            eventlist.setAvailable_seat(cursor.getString(31));
            eventlist.setConference_description(cursor.getString(32));
            subList.add(eventlist);
        }

        Log.e("No_of_column", "-==" + cursor.getColumnCount());
        // Log.e("No_of_Giftttt","-=="+cursor.getString(0));
        cursor.close();
        Log.e("SIZE", " SIZE " + subList.size());
        return subList;
    }

    public ArrayList<ConferencePackageModel> getConfSavePackCharge(String tableName, String conference_pck_id) {
        ArrayList<ConferencePackageModel> subList = new ArrayList<>();
        //String selectQuery = "SELECT  * FROM " + tableName + " ORDER BY " + conference_pack_day + " ASC  WHERE " + conference_pack_id + " = '" + conference_pck_id + "'";
        String selectQuery = "SELECT  * FROM " + tableName + " WHERE " + conference_pack_id + " = '" + conference_pck_id + "' ORDER BY " + conference_pack_day + " ASC LIMIT " + 100000000;
        // String selectQuery = "SELECT  * FROM " + tableName + " WHERE " + conference_pack_id + " = '" + conference_pck_id + "' ORDER BY ASC LIMIT "+ 100000;
        //String selectQuery = "SELECT  * FROM " + tableName + " WHERE " + conference_pack_id + " = '" + conference_pck_id + "'";
        Log.e("QUERYPACKAGES", selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        while (cursor.moveToNext()) {
            ConferencePackageModel ob = new ConferencePackageModel();
            ob.setConference_pack_id(cursor.getString(0));
            ob.setConference_pack_day(cursor.getString(1));
            ob.setConference_pack_charge(cursor.getString(2));
            subList.add(ob);
        }
        cursor.close();
        return subList;
    }

    public ArrayList<ConferencePackageModel> getConfPackCharge(String tableName, String conference_pck_id) {
        ArrayList<ConferencePackageModel> subList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + tableName + " WHERE " + conference_pack_id + " = '" + conference_pck_id + "' ORDER BY " + conference_pack_day + " ASC LIMIT " + 100000000;
        // String selectQuery = "SELECT * FROM " + tableName;
        Log.e("QUERYPACKAGES", selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        while (cursor.moveToNext()) {
            ConferencePackageModel ob = new ConferencePackageModel();
            ob.setConference_pack_id(cursor.getString(0));
            ob.setConference_pack_day(cursor.getString(1));
            ob.setConference_pack_charge(cursor.getString(2));
            subList.add(ob);
        }
        cursor.close();
        return subList;
    }

    public ArrayList<PackagesModel> getConfPackDetails(String tableName, String sync_time, String pack_status) {
        ArrayList<PackagesModel> subList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + tableName + " WHERE " + package_sync_time + " = '" + sync_time + "' AND " + package_status + " = '" + pack_status + "'";
        // String selectQuery = "SELECT * FROM " + tableName;
        Log.e("QUERYPACKAGES", selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        while (cursor.moveToNext()) {
            PackagesModel ob = new PackagesModel();
            ob.setPackage_id(cursor.getString(0));
            ob.setPackage_name(cursor.getString(1));
            ob.setPackage_total_price(cursor.getString(2));
            ob.setPackage_sync_time(cursor.getString(3));
            ob.setPackage_status(cursor.getString(4));
            subList.add(ob);
        }
        cursor.close();
        return subList;
    }


    public ArrayList<ConferencePackageModel> getNormalPackDetails(String conference_pack_id1) {
        ArrayList<ConferencePackageModel> subList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_CONFERENCES_PACK_CHARGE + " WHERE " + conference_pack_id + " = '" + conference_pack_id1 + "' ";
        // String selectQuery = "SELECT * FROM " + tableName;
        Log.e("QUERYPACKAGES", selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        while (cursor.moveToNext()) {
            ConferencePackageModel ob = new ConferencePackageModel();
            ob.setConference_pack_charge(cursor.getString(2));
            ob.setConference_pack_day(cursor.getString(1));
            subList.add(ob);
        }
        cursor.close();
        return subList;
    }

    public ArrayList<PackageSavedConferenceModel> getPackageMYConference(String conference_pack_id1) {
        ArrayList<PackageSavedConferenceModel> subList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_AddPackageMYConference + " WHERE " + conference_pack_id + " = '" + conference_pack_id1 + "' ";
        // String selectQuery = "SELECT * FROM " + tableName;
        Log.e("QUERYPACKAGES", selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        while (cursor.moveToNext()) {
            PackageSavedConferenceModel ob = new PackageSavedConferenceModel();
            ob.setConference_pack_charge(cursor.getString(2));
            ob.setConference_pack_day(cursor.getString(1));
            subList.add(ob);
        }
        cursor.close();
        return subList;
    }

    public ArrayList<AddForgeinPackageMYConferenceModel> getForgeinPackageMyConference(String conference_pack_id1) {
        ArrayList<AddForgeinPackageMYConferenceModel> subList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_AddForgeingPackageMYConference + " WHERE " + conference_pack_id + " = '" + conference_pack_id1 + "' ";
        // String selectQuery = "SELECT * FROM " + tableName;
        Log.e("QUERYPACKAGES", selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        while (cursor.moveToNext()) {
            AddForgeinPackageMYConferenceModel ob = new AddForgeinPackageMYConferenceModel();
            ob.setConference_pack_charge(cursor.getString(2));
            ob.setConference_pack_day(cursor.getString(1));
            subList.add(ob);
        }
        cursor.close();
        return subList;
    }


    public ArrayList<ConferencePackageModel> getPackageGoingConference(String conference_pack_id1) {
        ArrayList<ConferencePackageModel> subList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_AddPackageGoingConference + " WHERE " + conference_pack_id + " = '" + conference_pack_id1 + "' ";
        // String selectQuery = "SELECT * FROM " + tableName;
        Log.e("QUERYPACKAGES", selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        while (cursor.moveToNext()) {
            ConferencePackageModel ob = new ConferencePackageModel();
            ob.setConference_pack_charge(cursor.getString(2));
            ob.setConference_pack_day(cursor.getString(1));
            subList.add(ob);
        }
        cursor.close();
        return subList;
    }

    public ArrayList<ConferencePackageModelForgein> getForgeinPackageGoingConference(String conference_pack_id1) {
        ArrayList<ConferencePackageModelForgein> subList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_AddForgeingPackageGoingConference + " WHERE " + conference_pack_id + " = '" + conference_pack_id1 + "' ";
        // String selectQuery = "SELECT * FROM " + tableName;
        Log.e("QUERYPACKAGES", selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        while (cursor.moveToNext()) {
            ConferencePackageModelForgein ob = new ConferencePackageModelForgein();
            ob.setConference_pack_charge(cursor.getString(2));
            ob.setConference_pack_day(cursor.getString(1));
            subList.add(ob);
        }
        cursor.close();
        return subList;
    }

    public ArrayList<ConferencePackageModelForgein> getForgeingPackDetails(String conference_pack_id1) {
        ArrayList<ConferencePackageModelForgein> subList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_CONFERENCES_PACK_CHARGE_FORGEIN + " WHERE " + conference_pack_id + " = '" + conference_pack_id1 + "' ";
        // String selectQuery = "SELECT * FROM " + tableName;
        Log.e("QUERYPACKAGES", selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        while (cursor.moveToNext()) {
            ConferencePackageModelForgein ob = new ConferencePackageModelForgein();
            ob.setConference_pack_charge(cursor.getString(2));
            ob.setConference_pack_day(cursor.getString(1));
            subList.add(ob);
        }
        cursor.close();
        return subList;
    }


    public void AddConferPackCharge(ConferencePackageModel packConfModel, boolean insert) {
        SQLiteDatabase db = this.getWritableDatabase();
        //TODO: here we are putting the data using the content values

        ContentValues contentValues = new ContentValues();
        contentValues.put(conference_pack_id, packConfModel.getConference_pack_id());
        contentValues.put(conference_pack_day, packConfModel.getConference_pack_day());
        contentValues.put(conference_pack_charge, packConfModel.getConference_pack_charge());


        //TODO: inserting rows
        Log.d("contentvalujesss", contentValues + "");
        if (insert)
            db.insert(TABLE_CONFERENCES_PACK_CHARGE, null, contentValues);
        else
            db.update(TABLE_CONFERENCES_PACK_CHARGE, contentValues, conference_pack_id + " = ?", new String[]{String.valueOf(packConfModel.getConference_pack_id())});

        db.close(); //TODO: closing the database after the insert operation
    }

    public void AddPackageMYConference(PackageSavedConferenceModel packConfModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        //TODO: here we are putting the data using the content values

        ContentValues contentValues = new ContentValues();
        contentValues.put(conference_pack_id, packConfModel.getConference_pack_id());
        contentValues.put(conference_pack_day, packConfModel.getConference_pack_day());
        contentValues.put(conference_pack_charge, packConfModel.getConference_pack_charge());


        //TODO: inserting rows
        Log.d("contentvalujesss", contentValues + "");
        db.insert(TABLE_AddPackageMYConference, null, contentValues);

        db.close(); //TODO: closing the database after the insert operation
    }

    public void AddPackageGoingConference(ConferencePackageModel packConfModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        //TODO: here we are putting the data using the content values

        ContentValues contentValues = new ContentValues();
        contentValues.put(conference_pack_id, packConfModel.getConference_pack_id());
        contentValues.put(conference_pack_day, packConfModel.getConference_pack_day());
        contentValues.put(conference_pack_charge, packConfModel.getConference_pack_charge());

        //TODO: inserting rows
        Log.d("contentvalujesss", contentValues + "");
        db.insert(TABLE_AddPackageGoingConference, null, contentValues);

        db.close(); //TODO: closing the database after the insert operation
    }

    public void AddForgeinPackageMYConference(AddForgeinPackageMYConferenceModel packConfModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        //TODO: here we are putting the data using the content values

        ContentValues contentValues = new ContentValues();
        contentValues.put(conference_pack_id, packConfModel.getConference_pack_id());
        contentValues.put(conference_pack_day, packConfModel.getConference_pack_day());
        contentValues.put(conference_pack_charge, packConfModel.getConference_pack_charge());

        //TODO: inserting rows
        Log.d("contentvalujesss", contentValues + "");
        db.insert(TABLE_AddForgeingPackageMYConference, null, contentValues);
        db.close(); //TODO: closing the database after the insert operation
    }

    public void AddForgeinPackageGoingConference(ConferencePackageModelForgein packConfModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        //TODO: here we are putting the data using the content values

        ContentValues contentValues = new ContentValues();
        contentValues.put(conference_pack_id, packConfModel.getConference_pack_id());
        contentValues.put(conference_pack_day, packConfModel.getConference_pack_day());
        contentValues.put(conference_pack_charge, packConfModel.getConference_pack_charge());

        //TODO: inserting rows
        Log.d("contentvalujesss", contentValues + "");
        db.insert(TABLE_AddForgeingPackageGoingConference, null, contentValues);
        db.close(); //TODO: closing the database after the insert operation
    }


    public void deleteForgeinPAckage(String value) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_CONFERENCES_PACK_CHARGE_FORGEIN + " WHERE " + conference_pack_id + "= '" + value + "' ");
        db.close();
    }

    public void deleteNormalPAckage(String value) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_CONFERENCES_PACK_CHARGE + " WHERE " + conference_pack_id + "= '" + value + "' ");
        db.close();
    }

    public void deleteAddPackageSavedConference(String value) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_AddPackageMYConference + " WHERE " + conference_pack_id + "= '" + value + "' ");
        db.close();
    }

    public void deleteAddPackageGoingConference(String value) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_AddPackageGoingConference + " WHERE " + conference_pack_id + "= '" + value + "' ");
        db.close();
    }

    public void deleteAddForgeinPackageMyConference(String value) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_AddForgeingPackageMYConference + " WHERE " + conference_pack_id + "= '" + value + "' ");
        db.close();
    }

    public void deleteAddForgeinPackageGoingConference(String value) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_AddForgeingPackageGoingConference + " WHERE " + conference_pack_id + "= '" + value + "' ");
        db.close();
    }

    public void AddConferPackChargeForgein(ConferencePackageModelForgein packConfModel, boolean insert) {
        SQLiteDatabase db = this.getWritableDatabase();
        //TODO: here we are putting the data using the content values

        ContentValues contentValues = new ContentValues();
        contentValues.put(conference_pack_id, packConfModel.getConference_pack_id());
        contentValues.put(conference_pack_day, packConfModel.getConference_pack_day());
        contentValues.put(conference_pack_charge, packConfModel.getConference_pack_charge());


        //TODO: inserting rows
        Log.d("contentvalujesss", contentValues + "");
        if (insert)
            db.insert(TABLE_CONFERENCES_PACK_CHARGE_FORGEIN, null, contentValues);
        else
            db.update(TABLE_CONFERENCES_PACK_CHARGE_FORGEIN, contentValues, conference_pack_id + " = ?", new String[]{String.valueOf(packConfModel.getConference_pack_id())});

        db.close(); //TODO: closing the database after the insert operation
    }

    public void AddConferMyPackCharge(ConferencePackageModel packConfModel, boolean insert) {
        SQLiteDatabase db = this.getWritableDatabase();
        //TODO: here we are putting the data using the content values

        ContentValues contentValues = new ContentValues();
        contentValues.put(conference_pack_id, packConfModel.getConference_pack_id());
        contentValues.put(conference_pack_day, packConfModel.getConference_pack_day());
        contentValues.put(conference_pack_charge, packConfModel.getConference_pack_charge());


        //TODO: inserting rows
        Log.d("contentvalujesss", contentValues + "");
        if (insert)
            db.insert(TABLE_MY_CONFERENCES_PACK_CHARGE, null, contentValues);
        else
            db.update(TABLE_MY_CONFERENCES_PACK_CHARGE, contentValues, conference_pack_id + " = ?", new String[]{String.valueOf(packConfModel.getConference_pack_id())});

        db.close(); //TODO: closing the database after the insert operation
    }


    public void AddConferSavePackCharge(ConferencePackageModel packConfModel, boolean insert) {
        SQLiteDatabase db = this.getWritableDatabase();
        //TODO: here we are putting the data using the content values

        ContentValues contentValues = new ContentValues();
        contentValues.put(conference_pack_id, packConfModel.getConference_pack_id());
        contentValues.put(conference_pack_day, packConfModel.getConference_pack_day());
        contentValues.put(conference_pack_charge, packConfModel.getConference_pack_charge());


        //TODO: inserting rows
        Log.d("contentvalujesss", contentValues + "");
        if (insert)
            db.insert(TABLE_SAVE_CONFERENCES_PACK_CHARGE, null, contentValues);
        else
            db.update(TABLE_SAVE_CONFERENCES_PACK_CHARGE, contentValues, conference_pack_id + " = ?", new String[]{String.valueOf(packConfModel.getConference_pack_id())});

        db.close(); //TODO: closing the database after the insert operation
    }


    public void AddConferGoingPackCharge(ConferencePackageModel packConfModel, boolean insert) {
        SQLiteDatabase db = this.getWritableDatabase();
        //TODO: here we are putting the data using the content values

        ContentValues contentValues = new ContentValues();
        contentValues.put(conference_pack_id, packConfModel.getConference_pack_id());
        contentValues.put(conference_pack_day, packConfModel.getConference_pack_day());
        contentValues.put(conference_pack_charge, packConfModel.getConference_pack_charge());


        //TODO: inserting rows
        Log.d("contentvalujesss", contentValues + "");
        if (insert)
            db.insert(TABLE_GOING_CONFERENCES_PACK_CHARGE, null, contentValues);
        else
            db.update(TABLE_GOING_CONFERENCES_PACK_CHARGE, contentValues, conference_pack_id + " = ?", new String[]{String.valueOf(packConfModel.getConference_pack_id())});

        db.close(); //TODO: closing the database after the insert operation
    }


    //    public ArrayList<ConferenceDetailsModel> getSaveConferenceDetails(String tableName, String idGift,String statuss) {
//
//        ArrayList<ConferenceDetailsModel> subList = new ArrayList<>();
//        String selectQuery = "SELECT * FROM " + tableName + " WHERE " + show_like + " = '" + idGift + "' AND " + status + " = '" + statuss + "'";
//
//        // String selectQuery = "SELECT  * FROM " + tableName + " WHERE " + show_like + " = '" + idGift + "'";
//        //  String selectQuery = "SELECT  * FROM " + tableName + " ORDER BY " + modify_date + " DESC LIMIT " + 100000000;
//        Log.e("QUERY", selectQuery);
//
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, null);
//
//
//        while (cursor.moveToNext()) {
//            ConferenceDetailsModel eventlist = new ConferenceDetailsModel();
//            eventlist.setId(cursor.getString(0));
//            eventlist.setConference_id(cursor.getString(1));
//            eventlist.setConference_name(cursor.getString(2));
//            eventlist.setFrom_date(cursor.getString(3));
//            eventlist.setTo_date(cursor.getString(4));
//            eventlist.setFrom_time(cursor.getString(5));
//            eventlist.setTo_time(cursor.getString(6));
//            eventlist.setVenue(cursor.getString(7));
//            eventlist.setEvent_host_name(cursor.getString(8));
//            eventlist.setSpeciality(cursor.getString(9));
//            eventlist.setContact(cursor.getString(10));
//            eventlist.setLocation(cursor.getString(11));
//            eventlist.setLatitude(cursor.getString(12));
//            eventlist.setLogitude(cursor.getString(13));
//            eventlist.setBrochuers_file(cursor.getString(14));
//            eventlist.setBrochuers_charge(cursor.getString(15));
//            eventlist.setBrochuers_days(cursor.getString(16));
//            eventlist.setRegistration_fee(cursor.getString(17));
//            eventlist.setRegistration_days(cursor.getString(18));
//            eventlist.setEvent_sponcer(cursor.getString(19));
//            eventlist.setParticular_country_id(cursor.getString(20));
//            eventlist.setParticular_country_name(cursor.getString(21));
//            eventlist.setParticular_state_id(cursor.getString(22));
//            eventlist.setParticular_state_name(cursor.getString(23));
//            eventlist.setStatus(cursor.getString(24));
//            eventlist.setAdd_date(cursor.getString(25));
//            eventlist.setModify_date(cursor.getString(26));
//            eventlist.setPayment_status(cursor.getString(27));
//            eventlist.setShow_like(cursor.getString(28));
//            eventlist.setParticular_city_id(cursor.getString(29));
//            eventlist.setParticular_city_name(cursor.getString(30));
//            eventlist.setAvailable_seat(cursor.getString(31));
//            eventlist.setConference_description(cursor.getString(32));
//            eventlist.setConference_pack_day(cursor.getString(33));
//            eventlist.setConference_pack_charge(cursor.getString(34));
//            eventlist.setConference_type_id(cursor.getString(35));
//            eventlist.setMedical_profile_id(cursor.getString(36));
//            eventlist.setCredit_earnings(cursor.getString(37));
//            eventlist.setPayment_mode(cursor.getString(38));
//            eventlist.setTotal_days_price(cursor.getString(39));
//            eventlist.setAccomdation(cursor.getString(40));
//            eventlist.setMember_concessions(cursor.getString(41));
//            eventlist.setStudent_concessions(cursor.getString(42));
//            eventlist.setPrice_hike_after_date(cursor.getString(43));
//            eventlist.setPrice_hike_after_percentage(cursor.getString(44));
//
//            subList.add(eventlist);
//        }
//
//        Log.e("No_of_column", "-==" + cursor.getColumnCount());
//        // Log.e("No_of_Giftttt","-=="+cursor.getString(0));
//        cursor.close();
//        Log.e("SIZE", " SIZE " + subList.size());
//        return subList;
//    }
    public ArrayList<ConferenceDetailsModel> getSaveConferenceDetails(String tableName, String idGift, String statuss) {

        ArrayList<ConferenceDetailsModel> subList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + tableName + " WHERE " + show_like + " = '" + idGift + "' AND " + status + " = '" + statuss + "'";

        // String selectQuery = "SELECT  * FROM " + tableName + " WHERE " + show_like + " = '" + idGift + "'";
        //  String selectQuery = "SELECT  * FROM " + tableName + " ORDER BY " + modify_date + " DESC LIMIT " + 100000000;
        Log.e("QUERY", selectQuery);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        while (cursor.moveToNext()) {
            ConferenceDetailsModel eventlist = new ConferenceDetailsModel();
            eventlist.setId(cursor.getString(0));
            eventlist.setConference_id(cursor.getString(1));
            eventlist.setConference_name(cursor.getString(2));
            eventlist.setFrom_date(cursor.getString(3));
            eventlist.setTo_date(cursor.getString(4));
            eventlist.setFrom_time(cursor.getString(5));
            eventlist.setTo_time(cursor.getString(6));
            eventlist.setVenue(cursor.getString(7));
            eventlist.setEvent_host_name(cursor.getString(8));
            eventlist.setSpeciality(cursor.getString(9));
            eventlist.setContact(cursor.getString(10));
            eventlist.setLocation(cursor.getString(11));
            eventlist.setLatitude(cursor.getString(12));
            eventlist.setLogitude(cursor.getString(13));
            eventlist.setBrochuers_file(cursor.getString(14));
            eventlist.setBrochuers_charge(cursor.getString(15));
            eventlist.setBrochuers_days(cursor.getString(16));
            eventlist.setRegistration_fee(cursor.getString(17));
            eventlist.setRegistration_days(cursor.getString(18));
            eventlist.setEvent_sponcer(cursor.getString(19));
            eventlist.setParticular_country_id(cursor.getString(20));
            eventlist.setParticular_country_name(cursor.getString(21));
            eventlist.setParticular_state_id(cursor.getString(22));
            eventlist.setParticular_state_name(cursor.getString(23));
            eventlist.setStatus(cursor.getString(24));
            eventlist.setAdd_date(cursor.getString(25));
            eventlist.setModify_date(cursor.getString(26));
            eventlist.setPayment_status(cursor.getString(27));
            eventlist.setShow_like(cursor.getString(28));
            eventlist.setParticular_city_id(cursor.getString(29));
            eventlist.setParticular_city_name(cursor.getString(30));
            eventlist.setAvailable_seat(cursor.getString(31));
            eventlist.setConference_description(cursor.getString(32));
            eventlist.setConference_pack_day(cursor.getString(33));
            eventlist.setConference_pack_charge(cursor.getString(34));
            eventlist.setConference_type_id(cursor.getString(35));
            eventlist.setMedical_profile_id(cursor.getString(36));
            eventlist.setCredit_earnings(cursor.getString(37));
            eventlist.setPayment_mode(cursor.getString(38));
            eventlist.setTotal_days_price(cursor.getString(39));
            eventlist.setAccomdation(cursor.getString(40));
            eventlist.setMember_concessions(cursor.getString(41));
            eventlist.setStudent_concessions(cursor.getString(42));
            eventlist.setPrice_hike_after_date(cursor.getString(43));
            eventlist.setPrice_hike_after_percentage(cursor.getString(44));
            eventlist.setConference_type_name(cursor.getString(45));
            eventlist.setDiscount_percentage(cursor.getString(46));
            eventlist.setDiscount_description(cursor.getString(47));
            eventlist.setGst(cursor.getString(48));
            eventlist.setMedical_profile_name(cursor.getString(49));
            eventlist.setTarget_audience_speciality(cursor.getString(50));
            eventlist.setDepartment_id(cursor.getString(51));
            eventlist.setDepartment_name(cursor.getString(52));
            eventlist.setBooking_stopped(cursor.getString(53));
            subList.add(eventlist);
        }

        Log.e("No_of_column", "-==" + cursor.getColumnCount());
        // Log.e("No_of_Giftttt","-=="+cursor.getString(0));
        cursor.close();
        Log.e("SIZE", " SIZE " + subList.size());
        return subList;
    }


    public ArrayList<ConferenceDetailsModel> getConferenceDetails(String tableName, String idGift,String activate_statuss) {

        ArrayList<ConferenceDetailsModel> subList = new ArrayList<>();

        //String selectQuery = "SELECT * FROM " + tableName + " WHERE " + show_like + " = '" + idGift + "' AND " + status + " = '" + statuss + "'";
        try {

            String selectQuery = "SELECT * FROM " + tableName + " WHERE " + status + " = '" + idGift + "' AND " + activate_status + " = '" + activate_statuss + "' ORDER BY " + from_date + " DESC LIMIT " + 100000000;
            //  String selectQuery = "SELECT  * FROM " + tableName + " ORDER BY " + modify_date + " DESC LIMIT " + 100000000;
            Log.e("QUERY", selectQuery);

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {
                ConferenceDetailsModel eventlist = new ConferenceDetailsModel();
                eventlist.setId(cursor.getString(0));
                eventlist.setConference_id(cursor.getString(1));
                eventlist.setConference_name(cursor.getString(2));
                eventlist.setFrom_date(cursor.getString(3));
                eventlist.setTo_date(cursor.getString(4));
                eventlist.setFrom_time(cursor.getString(5));
                eventlist.setTo_time(cursor.getString(6));
                eventlist.setVenue(cursor.getString(7));
                eventlist.setEvent_host_name(cursor.getString(8));
                eventlist.setSpeciality(cursor.getString(9));
                eventlist.setContact(cursor.getString(10));
                eventlist.setLocation(cursor.getString(11));
                eventlist.setLatitude(cursor.getString(12));
                eventlist.setLogitude(cursor.getString(13));
                eventlist.setBrochuers_file(cursor.getString(14));
                eventlist.setBrochuers_charge(cursor.getString(15));
                eventlist.setBrochuers_days(cursor.getString(16));
                eventlist.setRegistration_fee(cursor.getString(17));
                eventlist.setRegistration_days(cursor.getString(18));
                eventlist.setEvent_sponcer(cursor.getString(19));
                eventlist.setParticular_country_id(cursor.getString(20));
                eventlist.setParticular_country_name(cursor.getString(21));
                eventlist.setParticular_state_id(cursor.getString(22));
                eventlist.setParticular_state_name(cursor.getString(23));
                eventlist.setStatus(cursor.getString(24));
                eventlist.setAdd_date(cursor.getString(25));
                eventlist.setModify_date(cursor.getString(26));
                eventlist.setPayment_status(cursor.getString(27));
                eventlist.setShow_like(cursor.getString(28));
                eventlist.setParticular_city_id(cursor.getString(29));
                eventlist.setParticular_city_name(cursor.getString(30));
                eventlist.setAvailable_seat(cursor.getString(31));
                eventlist.setConference_description(cursor.getString(32));
                eventlist.setConference_pack_day(cursor.getString(33));
                eventlist.setConference_pack_charge(cursor.getString(34));
                eventlist.setConference_type_id(cursor.getString(35));
                eventlist.setMedical_profile_id(cursor.getString(36));
                eventlist.setCredit_earnings(cursor.getString(37));
                eventlist.setPayment_mode(cursor.getString(38));
                eventlist.setTotal_days_price(cursor.getString(39));
                eventlist.setAccomdation(cursor.getString(40));
                eventlist.setMember_concessions(cursor.getString(41));
                eventlist.setStudent_concessions(cursor.getString(42));
                eventlist.setPrice_hike_after_date(cursor.getString(43));
                eventlist.setPrice_hike_after_percentage(cursor.getString(44));
                eventlist.setConference_type_name(cursor.getString(45));
                eventlist.setDiscount_percentage(cursor.getString(46));
                eventlist.setDiscount_description(cursor.getString(47));
                eventlist.setGst(cursor.getString(48));
                eventlist.setBooking_stopped(cursor.getString(49));
                eventlist.setTarget_audience_speciality(cursor.getString(50));
                eventlist.setMedical_profile_name(cursor.getString(51));
                eventlist.setDepartment_id(cursor.getString(52));
                eventlist.setDepartment_name(cursor.getString(53));
                eventlist.setActivate_status(cursor.getString(54));
                subList.add(eventlist);
            }

            Log.e("No_of_column", "-==" + cursor.getColumnCount());
            // Log.e("No_of_Giftttt","-=="+cursor.getString(0));
            cursor.close();
            Log.e("SIZE", " SIZE " + subList.size());
        }

        catch (SQLiteException e)
        {
            Log.e("SIZEBOBSQLITE", " SIZE " + e.getMessage());
        }

        return subList;
    }

//    public ArrayList<ConferenceDetailsModel> getConferenceDetails(String tableName, String idGift , String conferenceID) {
//
//        ArrayList<ConferenceDetailsModel> subList = new ArrayList<>();
//        String selectQuery = "SELECT * FROM " + tableName + " WHERE " + status + " = '" + idGift + "' AND " +conference_id+ " = '" +conferenceID+ "' ORDER BY " + from_date + " DESC LIMIT " + 100000000;
//        //  String selectQuery = "SELECT  * FROM " + tableName + " ORDER BY " + modify_date + " DESC LIMIT " + 100000000;
//        Log.e("QUERY", selectQuery);
//
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, null);
//
//        while (cursor.moveToNext()) {
//            ConferenceDetailsModel eventlist = new ConferenceDetailsModel();
//            eventlist.setId(cursor.getString(0));
//            eventlist.setConference_id(cursor.getString(1));
//            eventlist.setConference_name(cursor.getString(2));
//            eventlist.setFrom_date(cursor.getString(3));
//            eventlist.setTo_date(cursor.getString(4));
//            eventlist.setFrom_time(cursor.getString(5));
//            eventlist.setTo_time(cursor.getString(6));
//            eventlist.setVenue(cursor.getString(7));
//            eventlist.setEvent_host_name(cursor.getString(8));
//            eventlist.setSpeciality(cursor.getString(9));
//            eventlist.setContact(cursor.getString(10));
//            eventlist.setLocation(cursor.getString(11));
//            eventlist.setLatitude(cursor.getString(12));
//            eventlist.setLogitude(cursor.getString(13));
//            eventlist.setBrochuers_file(cursor.getString(14));
//            eventlist.setBrochuers_charge(cursor.getString(15));
//            eventlist.setBrochuers_days(cursor.getString(16));
//            eventlist.setRegistration_fee(cursor.getString(17));
//            eventlist.setRegistration_days(cursor.getString(18));
//            eventlist.setEvent_sponcer(cursor.getString(19));
//            eventlist.setParticular_country_id(cursor.getString(20));
//            eventlist.setParticular_country_name(cursor.getString(21));
//            eventlist.setParticular_state_id(cursor.getString(22));
//            eventlist.setParticular_state_name(cursor.getString(23));
//            eventlist.setStatus(cursor.getString(24));
//            eventlist.setAdd_date(cursor.getString(25));
//            eventlist.setModify_date(cursor.getString(26));
//            eventlist.setPayment_status(cursor.getString(27));
//            eventlist.setShow_like(cursor.getString(28));
//            eventlist.setParticular_city_id(cursor.getString(29));
//            eventlist.setParticular_city_name(cursor.getString(30));
//            eventlist.setAvailable_seat(cursor.getString(31));
//            eventlist.setConference_description(cursor.getString(32));
//            eventlist.setConference_pack_day(cursor.getString(33));
//            eventlist.setConference_pack_charge(cursor.getString(34));
//            eventlist.setConference_type_id(cursor.getString(35));
//            eventlist.setMedical_profile_id(cursor.getString(36));
//            eventlist.setCredit_earnings(cursor.getString(37));
//            eventlist.setPayment_mode(cursor.getString(38));
//            eventlist.setTotal_days_price(cursor.getString(39));
//            eventlist.setAccomdation(cursor.getString(40));
//            eventlist.setMember_concessions(cursor.getString(41));
//            eventlist.setStudent_concessions(cursor.getString(42));
//            eventlist.setPrice_hike_after_date(cursor.getString(43));
//            eventlist.setPrice_hike_after_percentage(cursor.getString(44));
//            eventlist.setConference_type_name(cursor.getString(45));
//            eventlist.setDiscount_percentage(cursor.getString(46));
//            eventlist.setDiscount_description(cursor.getString(47));
//            eventlist.setGst(cursor.getString(48));
//            eventlist.setBooking_stopped(cursor.getString(49));
//            eventlist.setTarget_audience_speciality(cursor.getString(50));
//            eventlist.setMedical_profile_name(cursor.getString(51));
//            eventlist.setDepartment_id(cursor.getString(52));
//            eventlist.setDepartment_name(cursor.getString(53));
//            subList.add(eventlist);
//        }
//
//        Log.e("No_of_column", "-==" + cursor.getColumnCount());
//        // Log.e("No_of_Giftttt","-=="+cursor.getString(0));
//        cursor.close();
//        Log.e("SIZE", " SIZE " + subList.size());
//        return subList;
//    }

    public boolean CheckIsDataAlreadyInDBorNot(String TableName, String columnName, String fieldValue) {
        SQLiteDatabase sqldb = this.getReadableDatabase();
        String Query = "SELECT * FROM " + TableName + " WHERE " + columnName + " = '" + fieldValue + "'";
        Log.e("mybag", "check" + Query);
        try {
            Cursor cursor = sqldb.rawQuery(Query, null);
            if (cursor.getCount() <= 0) {
                cursor.close();
                return false;
            }
            cursor.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void deleteTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        // query to obtain the names of all tables in your database
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        List<String> tables = new ArrayList<>();

// iterate over the result set, adding every table name to a list
        while (c.moveToNext()) {
            tables.add(c.getString(0));
        }

// call DROP TABLE on every table name
// call DELETE TABLE on every table name
        for (String table : tables) {
            // String dropQuery = "DROP TABLE IF EXISTS " + table;
            db.execSQL("DELETE FROM " + table);
            // db.close();
            // db.execSQL(dropQuery);
            c.close();
        }
    }
}
