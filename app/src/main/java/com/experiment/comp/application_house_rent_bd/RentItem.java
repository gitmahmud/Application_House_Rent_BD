package com.experiment.comp.application_house_rent_bd;

import android.util.Log;

import java.io.Serializable;
import java.sql.Timestamp;

public class RentItem implements Serializable,Comparable {
    private int _rentId;
    private String rentTitle;
    private Timestamp rentPosttime;
    private String rentDescription;
    private String rentArea;
    private String rentContact;
    private RentStatus rentStatus;
    private String rentImageName;
    private String rentGoogleMapUrl;


    public RentItem()
    {

    }

    public RentItem(int _rentId, String rentTitle, Timestamp rentPosttime, String rentDescription, String rentArea, String rentContact, RentStatus rentStatus, String rentImageName, String rentGoogleMapUrl) {
        this._rentId = _rentId;
        this.rentTitle = rentTitle;
        this.rentPosttime = rentPosttime;
        this.rentDescription = rentDescription;
        this.rentArea = rentArea;
        this.rentContact = rentContact;
        this.rentStatus = rentStatus;
        this.rentImageName = rentImageName;
        this.rentGoogleMapUrl = rentGoogleMapUrl;
    }

    public int get_rentId() {
        return _rentId;
    }

    public void set_rentId(int _rentId) {
        this._rentId = _rentId;
    }

    public String getRentTitle() {
        return rentTitle;
    }

    public void setRentTitle(String rentTitle) {
        this.rentTitle = rentTitle;
    }

    public Timestamp getRentPosttime() {
        return rentPosttime;
    }

    public void setRentPosttime(Timestamp rentPosttime) {
        this.rentPosttime = rentPosttime;
    }

    public String getRentDescription() {
        return rentDescription;
    }

    public void setRentDescription(String rentDescription) {
        this.rentDescription = rentDescription;
    }

    public String getRentArea() {
        return rentArea;
    }

    public void setRentArea(String rentArea) {
        this.rentArea = rentArea;
    }

    public String getRentContact() {
        return rentContact;
    }

    public void setRentContact(String rentContact) {
        this.rentContact = rentContact;
    }

    public RentStatus getRentStatus() {
        return rentStatus;
    }

    public void setRentStatus(RentStatus rentStatus) {
        this.rentStatus = rentStatus;
    }

    public String getRentImageName() {
        return rentImageName;
    }

    public void setRentImageName(String rentImageName) {
        this.rentImageName = rentImageName;
    }

    public String getRentGoogleMapUrl() {
        return rentGoogleMapUrl;
    }

    public void setRentGoogleMapUrl(String rentGoogleMapUrl) {
        this.rentGoogleMapUrl = rentGoogleMapUrl;
    }

    public void showAtLog()
    {
        Log.d("Shout","id "+String.valueOf(_rentId)+" Title "+rentTitle+" Status "+rentStatus+" image "+ rentImageName);
    }

    @Override
    public int compareTo(Object another) {
        return -this.get_rentId() + ((RentItem) another).get_rentId();
    }
}
