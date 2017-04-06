package com.sample.demo.modal;


/**
 * Created by 464394 on 2/13/2017.
 */

public class Books {
    public String startDate;
    public String objType;
    public String endDate;
    public String name;
    public String loginRequired;
    public String url;
    public String icon;
    /** The quantity. */
    private String orderQty;

    public String getQuantity() {
        return orderQty;
    }

    public void setQuantity(String quantity) {
        this.orderQty = quantity;
    }
}
