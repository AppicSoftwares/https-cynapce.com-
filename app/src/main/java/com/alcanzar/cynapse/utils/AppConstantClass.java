package com.alcanzar.cynapse.utils;


import android.content.Context;

import com.alcanzar.cynapse.model.CityModel;
import com.alcanzar.cynapse.model.ConferencePackageModel;
import com.alcanzar.cynapse.model.JobDetailsModel;
import com.alcanzar.cynapse.model.StateModel;

import java.util.ArrayList;

public class AppConstantClass {
    //162.243.205.148
//    "http://13.232.58.92/cynapce/apis/";
//        private static final String HOST = "http://192.168.1.12/cynapce/apis/";

    //    private static final String ip="35.154.108.100/";
    private static final String ip = "http://cynapce.com/";
     //private static final String ip="http://162.243.205.148/";
    public static final String HOST = ip + "cynapce/apis/";
    //    public static final String HOST = "http://162.243.205.148/cynapce/apis/";
    private static final String HOST1 = ip + "cynapce/cases/";
    //    private static final String HOST1 = "http://162.243.205.148/cynapce/cases/";
    public static final String HOST4 = ip + "cynapce/app/";
    //    public static final String HOST4 = "http://162.243.205.148/cynapce/app/";
    private static final String HOST3 = ip + "cynapce/cases/";
    //    private static final String HOST3 = "http://162.243.205.148/cynapce/cases/";
    private static final String HOST2 = ip + "cynapce_soni/cases/";
    private static final String HOST5 = ip + "cynapce/Cynapis/";
    public static final String HOST6 = ip + "cynapce/Apis/";
//    private static final String HOST2 = "http://162.243.205.148/cynapce_soni/cases/";

    public static final String signUpNew = HOST6 + "signUpNew";
    public static final String HOST_IMAGE = HOST4 + "webroot/img/profile_images/";
    public static final String signUp = HOST + "signUp";
    public static final String updateProfile = HOST + "updateProfile";
    public static final String medicalProfile = HOST + "getProfileCategory";
    public static final String profileSpecialization = HOST + "getProfileSpecialization";
    public static final String highestDegree = HOST + "highestDegree";
    public static final String getAllCountry = HOST + "getAllCountry";
    public static final String getState = HOST + "getState";
    public static final String getNotification = HOST + "notification";
    public static final String getProductDetail = HOST + "getProductDetail";
    public static final String getCity = HOST + "getCity";
    public static final String getAllProductCategory = HOST + "getAllCategory";
    public static final String login = HOST + "login";
    public static final String forgotPassword = HOST + "forgotPassword";
    public static final String changePassword = HOST + "changePassword";
    public static final String jobPost = HOST + "jobPost";
    public static final String requestJobPost = HOST + "requestJobPost";
    public static final String addProduct = HOST + "AddProduct";
    public static final String getAllProduct = HOST + "getAllProduct";
    public static final String getCategoryWiseProduct = HOST + "getCategoryWiseProduct";
    public static final String logout = HOST + "logout";
    //public static final String postPublishedProfile = HOST + "postPublishedProfile";
    public static final String Post_Published_Profile = HOST + "Post_Published_Profile";
    public static final String buyingProductRequest = HOST + "buyingProductRequest";
    public static final String buyProduct = HOST + "buyProduct";
    public static final String getMyDeal = HOST + "getMyDeal";
    public static final String getTitle = HOST + "getTitle";
    public static final String getSubSpecialization = HOST + "getSubSpecialization";
    public static final String getDepartment = HOST + "getDepartment";
    public static final String requestJobList = HOST + "requestJobList";
    public static final String postJobList = HOST + "postJobList";
    public static final String getRecommendedJobList = HOST + "getRecommendedJobList";
    public static final String applyForRequestJob = HOST + "applyForRequestJob";
    public static final String appliedRequestJobList = HOST + "appliedRequestJobList";
    public static final String shortListofJobPost = HOST + "shortListofJobPost";
    public static final String getProfile = HOST + "getProfile";
    public static final String changeNotificationStatus = HOST + "changeNotificationStatus";
    public static final String otherCategory = HOST + "otherCategory";
    public static final String addHospitalProduct = HOST + "addHospitalProduct";
    public static final String addPracticeProduct = HOST + "addPracticeProduct";
    public static final String addTieUpsProduct = HOST + "addTieUpsProduct";
    public static final String addEquipmentProduct = HOST + "addEquipmentProduct";
    public static final String addBooksProduct = HOST + "addBooksProduct";
    public static final String getMetroCity = HOST + "getMetroCity";
    public static final String getChatMessage = HOST + "getChatMessage";
    public static final String imInterested = HOST + "imInterested";
    public static final String postChatMessage = HOST + "postChatMessage";
    public static final String getMessage = HOST + "getMessage";
    public static final String changeMessageStatus = HOST + "changeMessageStatus";
    public static final String profileUpdateRequest = HOST + "profileUpdateRequest";
    public static final String getCounter = HOST + "getCounter";
    public static final String getYearOfStudy = HOST + "getYearOfStudy";
    public static final String getAllProducts = HOST + "getAllProducts";
    public static final String getAllMyConference = HOST + "getAllMyConference";
    public static final String getAllConference = HOST + "getAllConference";
    public static final String addConference = HOST + "addConference";
    public static final String getConference = HOST + "getConference";
    public static final String conferenceSeatBooking = HOST + "conferenceSeatBooking";
    public static final String updateConferenceDetail = HOST + "updateConferenceDetail";
    public static final String get_all_cases = HOST1 + "get_all_cases";
    public static final String get_all_questions = HOST1 + "get_all_questions";
    public static final String likeConference = HOST + "likeConference";
    public static final String deleteBroushers = HOST1 + "deleteBroushers";
    public static final String post_answers = HOST1 + "post_answers";
    public static final String get_all_attend_cases = HOST1 + "get_all_attend_cases";
    public static final String attend_cases_ques_ans = HOST1 + "attend_cases_ques_ans";
    public static final String delete_all_data = HOST1 + "delete_all_data";
    public static final String updateSocialMediaProfile = HOST + "updateSocialMediaProfile";
    public static final String get_jobs_packages = HOST1 + "get_jobs_packages";
    public static final String validate_promocode = HOST1 + "validate_promocode";
    public static final String check_package_status = HOST1 + "check_package_status";
    public static final String update_package_status = HOST1 + "update_package_status";
    public static final String PostJobPaymentData = HOST1 + "PostJobPaymentData";//////////////job_payment_status
    public static final String getPerfferedData = HOST1 + "getPerfferedData";
    public static final String postedJobList = HOST1 + "postedJobList";
    public static final String getPublicationList = HOST1 + "getPublicationList";
    public static final String post_conference_pakages = HOST + "post_conference_pakages";
    public static final String getConferenceType = HOST + "getConferenceType";
    public static final String postFeedbacks = HOST1 + "postFeedbacks";
    public static ArrayList<JobDetailsModel> detial_arrayList = new ArrayList<>();
    public static final String conferenceOrder = HOST1 + "conferenceOrder";
    public static final String post_device_data = HOST + "post_device_data";
    public static String dataTest = "";
    public static String currentDate1 = "";
    // public static final String validate_conference_promocode ="http://162.243.205.148/cynapce/cases/validate_conference_promocode/";
    //public static final String validate_conference_promocode ="http://162.243.205.148/cynapce/apis/validate_conference_promocode/";
    public static ArrayList<ConferencePackageModel> notyListModal = new ArrayList<>();
    public static final String TRANS_URL = "https://secure.ccavenue.com/transaction/initTrans";
    public static ArrayList<StateModel> statelistMain;
    public static ArrayList<CityModel> cityListFillData;
    // private static  final  String HOST3="http://cynapce.com/cases/";


    //http://cynapce.com/cynapce/cases/getConferenceUserInfo

    public static final String validate_conference_promocode = HOST1 + "validate_conference_promocode";
    public static final String getCcevnue = HOST3 + "getCcevnue";
    public static final String getConferenceUserInfo = HOST3 + "getConferenceUserInfo";
    public static final String post_review_amt = HOST3 + "post_review_amt";
    public static final String getConferenceDetail = HOST + "getConferenceDetail";
    public static String change = "";
    public static final String validateOTP = HOST + "validateOTP";
    public static final String resendOTP = HOST + "resendOTP";
    public static final String shorlistedCandidates = HOST + "shorlistedCandidates";
    public static final String requestDirectJobPost = HOST + "requestDirectJobPost";
    public static final String getAllMyPlans = HOST + "getAllMyPlans";

    public static final String stopConferenceBooking = HOST + "stopConferenceBooking";
    public static final String conference_search = HOST5 + "conference_search";
    public static final String getAllMyProducts = HOST + "getAllMyProducts";
    public static final String getbookedConference = HOST5 + "getbookedConference";
    public static final String chkProfile = HOST6 + "chkProfile";
    public static final String uploadResume = HOST + "uploadResume";
    public static final String getTargetDepartment = HOST6 + "getTargetDepartment";

    public static Context mActivity = null;

////////////////////////////////////////////////////////////////////////////////////

    public static final String jobSearchFilter = ip + "cynapce/Cynapis/job_search";
    public static final String hospitalSearchFilter = ip + "cynapce/Cynapis/hospital_search";

    public static final String updateUsername = HOST6 + "updateUsername";


}
