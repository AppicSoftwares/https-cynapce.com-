package com.alcanzar.cynapse.model.MyConferenceModel;

import java.util.List;

public class MyConferenceDetailsModel {

    private String conferenceId;
    private String bookingConferenceId;
    private String name;
    private String noOfSeats;
    private String totalAmount;
    private String orderId;
    private String paymentStatus;
    private String status;
    private List<BookingDetail> bookingDetail = null;


    public String getConferenceId() {
        return conferenceId;
    }

    public void setConferenceId(String conferenceId) {
        this.conferenceId = conferenceId;
    }

    public String getBookingConferenceId() {
        return bookingConferenceId;
    }

    public void setBookingConferenceId(String bookingConferenceId) {
        this.bookingConferenceId = bookingConferenceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNoOfSeats() {
        return noOfSeats;
    }

    public void setNoOfSeats(String noOfSeats) {
        this.noOfSeats = noOfSeats;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<BookingDetail> getBookingDetail() {
        return bookingDetail;
    }

    public void setBookingDetail(List<BookingDetail> bookingDetail) {
        this.bookingDetail = bookingDetail;
    }

}
