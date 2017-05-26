package com.example.pooja.pk_electronics.model;

/**
 * Created by pooja on 5/15/2017.
 */

public class DashboardModel {
    int id;
    String Pname;
    String price1;
    String price2;

    public DashboardModel() {

    }

    public String getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(String updatedOn) {
        this.updatedOn = updatedOn;
    }

    String updatedOn;

    public String getPrice1() {
        return price1;
    }

    public void setPrice1(String price1) {
        this.price1 = price1;
    }

    public String getPrice2() {
        return price2;
    }

    public void setPrice2(String price2) {
        this.price2 = price2;
    }

    public String getPrice3() {
        return price3;
    }

    public void setPrice3(String price3) {
        this.price3 = price3;
    }

    String price3;

    public DashboardModel(int id, String product_name, String price1, String price2, String price3,String updated) {
        this.id=id;
        this.Pname=product_name;
        this.price1=price1;
        this.price2=price2;
        this.price3=price3;
        this.updatedOn=updated;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPname() {
        return Pname;
    }

    public void setPname(String pname) {
        Pname = pname;
    }



}
