package com.alcanzar.cynapse.model;

public class ConferenceDetailsModel {
    private String id = "";
    private String conference_id = "";
    private String conference_name = "";
    private String from_time = "";
    private String cost = "";
    private String duration = "";
    private String from_date = "";
    private String to_date = "";
    private String to_time = "";
    private String payment_status = "";
    private String show_like = "";

    private String conference_type_id = "";
    private String medical_profile_id = "";
    private String credit_earnings = "";
    private String payment_mode = "";
    private String total_days_price = "";

    private String accomdation = "";
    private String member_concessions = "";
    private String student_concessions = "";
    private String price_hike_after_date = "";
    private String price_hike_after_percentage = "";


    private String registered = "";
    private String interested = "";
    private String conference_pack_day = "";
    private String conference_pack_charge = "";

    private String views = "";

    private String venue = "";
    private String event_host_name = "";
    private String speciality = "";
    private String contact = "";
    private String location = "";
    private String latitude = "";
    private String logitude = "";
    private String brochuers_file = "";
    private String brochuers_charge = "";
    private String keynote_speakers = "";
    private String discount_percentage = "";
    private String discount_description = "";


    private String brochuers_days = "";
    private String registration_fee = "";
    private String registration_days = "";


    private String event_sponcer = "";
    private String particular_country_id = "";
    private String particular_country_name = "";

    private String particular_state_id = "";
    private String particular_state_name = "";
    private String status = "";

    private String add_date = "";
    private String particular_city_id = "";
    private String particular_city_name = "";
    private String address_type = "";

    private String available_seat = "";
    private String conference_description = "";
    private String modify_date = "";
    private String pdf_url="";

    private String without_notification_cost="";
    private String with_notification_cost="";
    private String conference_type_name="";
    private String booking_stopped="";
    private String medical_profile_name="";
    private String target_audience_speciality="";

    private String department_id = "";
    private String department_name = "";

    private String gst = "";

    private String activate_status = "";

    public String getActivate_status() {
        return activate_status;
    }

    public void setActivate_status(String activate_status) {
        this.activate_status = activate_status;
    }

    public String getPdf_url() {
        return pdf_url;
    }

    public void setPdf_url(String pdf_url) {
        this.pdf_url = pdf_url;
    }

    public String getModify_date() {
        return modify_date;
    }

    public void setModify_date(String modify_date) {
        this.modify_date = modify_date;
    }

//TODO: constructors

    public ConferenceDetailsModel() {

    }

    public ConferenceDetailsModel(String id, String conference_id, String conference_name, String from_time,
                                  String cost, String duration, String from_date, String to_date, String to_time,
                                  String payment_status, String show_like, String conference_type_id, String medical_profile_id,
                                  String credit_earnings, String payment_mode, String total_days_price, String accomdation,
                                  String member_concessions, String student_concessions, String price_hike_after_date,
                                  String price_hike_after_percentage, String registered, String interested, String conference_pack_day,
                                  String conference_pack_charge, String views, String venue, String event_host_name, String speciality,
                                  String contact, String location, String latitude, String logitude, String brochuers_file,
                                  String brochuers_charge, String brochuers_days, String registration_fee, String registration_days,
                                  String event_sponcer, String particular_country_id, String particular_country_name, String particular_state_id,
                                  String particular_state_name, String status, String add_date, String particular_city_id, String particular_city_name,
                                  String address_type, String available_seat, String conference_description, String modify_date) {
        this.id = id;
        this.conference_id = conference_id;
        this.conference_name = conference_name;
        this.from_time = from_time;
        this.cost = cost;
        this.duration = duration;
        this.from_date = from_date;
        this.to_date = to_date;
        this.to_time = to_time;
        this.payment_status = payment_status;
        this.show_like = show_like;
        this.conference_type_id = conference_type_id;
        this.medical_profile_id = medical_profile_id;
        this.credit_earnings = credit_earnings;
        this.payment_mode = payment_mode;
        this.total_days_price = total_days_price;
        this.accomdation = accomdation;
        this.member_concessions = member_concessions;
        this.student_concessions = student_concessions;
        this.price_hike_after_date = price_hike_after_date;
        this.price_hike_after_percentage = price_hike_after_percentage;
        this.registered = registered;
        this.interested = interested;
        this.conference_pack_day = conference_pack_day;
        this.conference_pack_charge = conference_pack_charge;
        this.views = views;
        this.venue = venue;
        this.event_host_name = event_host_name;
        this.speciality = speciality;
        this.contact = contact;
        this.location = location;
        this.latitude = latitude;
        this.logitude = logitude;
        this.brochuers_file = brochuers_file;
        this.brochuers_charge = brochuers_charge;
        this.brochuers_days = brochuers_days;
        this.registration_fee = registration_fee;
        this.registration_days = registration_days;
        this.event_sponcer = event_sponcer;
        this.particular_country_id = particular_country_id;
        this.particular_country_name = particular_country_name;
        this.particular_state_id = particular_state_id;
        this.particular_state_name = particular_state_name;
        this.status = status;
        this.add_date = add_date;
        this.particular_city_id = particular_city_id;
        this.particular_city_name = particular_city_name;
        this.address_type = address_type;
        this.available_seat = available_seat;
        this.conference_description = conference_description;
        this.modify_date = modify_date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getConference_id() {
        return conference_id;
    }

    public void setConference_id(String conference_id) {
        this.conference_id = conference_id;
    }

    public String getConference_name() {
        return conference_name;
    }

    public void setConference_name(String conference_name) {
        this.conference_name = conference_name;
    }

    public String getFrom_time() {
        return from_time;
    }

    public void setFrom_time(String from_time) {
        this.from_time = from_time;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getFrom_date() {
        return from_date;
    }

    public void setFrom_date(String from_date) {
        this.from_date = from_date;
    }

    public String getTo_date() {
        return to_date;
    }

    public void setTo_date(String to_date) {
        this.to_date = to_date;
    }

    public String getTo_time() {
        return to_time;
    }

    public void setTo_time(String to_time) {
        this.to_time = to_time;
    }

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }

    public String getShow_like() {
        return show_like;
    }

    public void setShow_like(String show_like) {
        this.show_like = show_like;
    }

    public String getConference_type_id() {
        return conference_type_id;
    }

    public void setConference_type_id(String conference_type_id) {
        this.conference_type_id = conference_type_id;
    }

    public String getMedical_profile_id() {
        return medical_profile_id;
    }

    public void setMedical_profile_id(String medical_profile_id) {
        this.medical_profile_id = medical_profile_id;
    }

    public String getCredit_earnings() {
        return credit_earnings;
    }

    public void setCredit_earnings(String credit_earnings) {
        this.credit_earnings = credit_earnings;
    }

    public String getPayment_mode() {
        return payment_mode;
    }

    public void setPayment_mode(String payment_mode) {
        this.payment_mode = payment_mode;
    }

    public String getTotal_days_price() {
        return total_days_price;
    }

    public void setTotal_days_price(String total_days_price) {
        this.total_days_price = total_days_price;
    }

    public String getAccomdation() {
        return accomdation;
    }

    public void setAccomdation(String accomdation) {
        this.accomdation = accomdation;
    }

    public String getMember_concessions() {
        return member_concessions;
    }

    public void setMember_concessions(String member_concessions) {
        this.member_concessions = member_concessions;
    }

    public String getStudent_concessions() {
        return student_concessions;
    }

    public void setStudent_concessions(String student_concessions) {
        this.student_concessions = student_concessions;
    }

    public String getPrice_hike_after_date() {
        return price_hike_after_date;
    }

    public void setPrice_hike_after_date(String price_hike_after_date) {
        this.price_hike_after_date = price_hike_after_date;
    }

    public String getPrice_hike_after_percentage() {
        return price_hike_after_percentage;
    }

    public void setPrice_hike_after_percentage(String price_hike_after_percentage) {
        this.price_hike_after_percentage = price_hike_after_percentage;
    }

    public String getRegistered() {
        return registered;
    }

    public void setRegistered(String registered) {
        this.registered = registered;
    }

    public String getInterested() {
        return interested;
    }

    public void setInterested(String interested) {
        this.interested = interested;
    }

    public String getConference_pack_day() {
        return conference_pack_day;
    }

    public void setConference_pack_day(String conference_pack_day) {
        this.conference_pack_day = conference_pack_day;
    }

    public String getConference_pack_charge() {
        return conference_pack_charge;
    }

    public void setConference_pack_charge(String conference_pack_charge) {
        this.conference_pack_charge = conference_pack_charge;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getEvent_host_name() {
        return event_host_name;
    }

    public void setEvent_host_name(String event_host_name) {
        this.event_host_name = event_host_name;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLogitude() {
        return logitude;
    }

    public void setLogitude(String logitude) {
        this.logitude = logitude;
    }

    public String getBrochuers_file() {
        return brochuers_file;
    }

    public void setBrochuers_file(String brochuers_file) {
        this.brochuers_file = brochuers_file;
    }

    public String getBrochuers_charge() {
        return brochuers_charge;
    }

    public void setBrochuers_charge(String brochuers_charge) {
        this.brochuers_charge = brochuers_charge;
    }

    public String getBrochuers_days() {
        return brochuers_days;
    }

    public void setBrochuers_days(String brochuers_days) {
        this.brochuers_days = brochuers_days;
    }

    public String getRegistration_fee() {
        return registration_fee;
    }

    public void setRegistration_fee(String registration_fee) {
        this.registration_fee = registration_fee;
    }

    public String getRegistration_days() {
        return registration_days;
    }

    public void setRegistration_days(String registration_days) {
        this.registration_days = registration_days;
    }

    public String getEvent_sponcer() {
        return event_sponcer;
    }

    public void setEvent_sponcer(String event_sponcer) {
        this.event_sponcer = event_sponcer;
    }

    public String getParticular_country_id() {
        return particular_country_id;
    }

    public void setParticular_country_id(String particular_country_id) {
        this.particular_country_id = particular_country_id;
    }

    public String getParticular_country_name() {
        return particular_country_name;
    }

    public void setParticular_country_name(String particular_country_name) {
        this.particular_country_name = particular_country_name;
    }

    public String getParticular_state_id() {
        return particular_state_id;
    }

    public void setParticular_state_id(String particular_state_id) {
        this.particular_state_id = particular_state_id;
    }

    public String getParticular_state_name() {
        return particular_state_name;
    }

    public void setParticular_state_name(String particular_state_name) {
        this.particular_state_name = particular_state_name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAdd_date() {
        return add_date;
    }

    public void setAdd_date(String add_date) {
        this.add_date = add_date;
    }

    public String getParticular_city_id() {
        return particular_city_id;
    }

    public void setParticular_city_id(String particular_city_id) {
        this.particular_city_id = particular_city_id;
    }

    public String getParticular_city_name() {
        return particular_city_name;
    }

    public void setParticular_city_name(String particular_city_name) {
        this.particular_city_name = particular_city_name;
    }

    public String getAddress_type() {
        return address_type;
    }

    public void setAddress_type(String address_type) {
        this.address_type = address_type;
    }

    public String getAvailable_seat() {
        return available_seat;
    }

    public void setAvailable_seat(String available_seat) {
        this.available_seat = available_seat;
    }

    public String getConference_description() {
        return conference_description;
    }

    public void setConference_description(String conference_description) {
        this.conference_description = conference_description;
    }

    public String getWithout_notification_cost() {
        return without_notification_cost;
    }

    public String getWith_notification_cost() {
        return with_notification_cost;
    }

    public void setWithout_notification_cost(String with_notification_cost) {
        this.without_notification_cost = with_notification_cost;
    }

    public void setWith_notification_cost(String with_notification_cost) {
        this.with_notification_cost = with_notification_cost;
    }

    public String getDiscount_percentage() {
        return discount_percentage;
    }

    public void setDiscount_percentage(String discount_percentage) {
        this.discount_percentage = discount_percentage;
    }

    public String getDiscount_description() {
        return discount_description;
    }

    public void setDiscount_description(String discount_description) {
        this.discount_description = discount_description;
    }

    public String getConference_type_name() {
        return conference_type_name;
    }

    public void setConference_type_name(String conference_type_name) {
        this.conference_type_name = conference_type_name;
    }

    public String getGst() {
        return gst;
    }

    public void setGst(String gst) {
        this.gst = gst;
    }

    public String getBooking_stopped() {
        return booking_stopped;
    }

    public void setBooking_stopped(String booking_stopped1) {
        this.booking_stopped = booking_stopped1;
    }

    public String getMedical_profile_name() {
        return medical_profile_name;
    }

    public void setMedical_profile_name(String medical_profile_name) {
        this.medical_profile_name = medical_profile_name;
    }

    public String getTarget_audience_speciality() {
        return target_audience_speciality;
    }

    public void setTarget_audience_speciality(String target_audience_speciality) {
        this.target_audience_speciality = target_audience_speciality;
    }

    public String getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(String department_id) {
        this.department_id = department_id;
    }

    public String getDepartment_name() {
        return department_name;
    }

    public void setDepartment_name(String department_name) {
        this.department_name = department_name;
    }
}
