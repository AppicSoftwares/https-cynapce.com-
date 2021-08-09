package com.alcanzar.cynapse.model;
public class JobPaymentModel {
 private String job_id="";
 private String job_type="";
 private String job_pay="";
    private String job_details="";
    private String add_date="";
    private String percentage="";
    private String ctc="";
    private String job_id_="";
    private String job_title_id="";

    public JobPaymentModel(String job_id, String job_type, String job_pay, String job_details ,String add_date,String percentage,String ctc) {
        this.job_id = job_id;
        this.job_type = job_type;
        this.job_pay = job_pay;
        this.job_details = job_details;
        this.add_date=add_date;
    }

    public String getJob_id() {
        return job_id;
    }

    public void setJob_id(String job_id) {
        this.job_id = job_id;
    }

    public String getJob_type() {
        return job_type;
    }

    public void setJob_type(String job_type) {
        this.job_type = job_type;
    }

    public String getJob_pay() {
        return job_pay;
    }

    public void setJob_pay(String job_pay) {
        this.job_pay = job_pay;
    }

    public String getJob_details() {
        return job_details;
    }

    public void setJob_details(String job_details) {
        this.job_details = job_details;
    }

    public String getAdd_date() {
        return add_date;
    }

    public void setAdd_date(String add_date) {
        this.add_date = add_date;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public String getCtc() {
        return ctc;
    }

    public void setCtc(String ctc) {
        this.ctc = ctc;
    }

    public JobPaymentModel() {

    }

    public String getJob_id_() {
        return job_id_;
    }

    public void setJob_id_(String job_id_) {
        this.job_id_ = job_id_;
    }

    public String getJob_title_id() {
        return job_title_id;
    }

    public void setJob_title_id(String job_title_id) {
        this.job_title_id = job_title_id;
    }
}
