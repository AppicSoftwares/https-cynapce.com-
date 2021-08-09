package com.alcanzar.cynapse.model;
public class PdfDelModel {
 private String pdf_url="";
 private String pdf_id;
 private  String conference_d="";

    public PdfDelModel() {
    }

    public String getPdf_url() {
        return pdf_url;
    }

    public void setPdf_url(String pdf_url) {
        this.pdf_url = pdf_url;
    }

    public String getPdf_id() {
        return pdf_id;
    }

    public void setPdf_id(String pdf_id) {
        this.pdf_id = pdf_id;
    }

    public String getConference_d() {
        return conference_d;
    }

    public void setConference_d(String conference_d) {
        this.conference_d = conference_d;
    }
}
