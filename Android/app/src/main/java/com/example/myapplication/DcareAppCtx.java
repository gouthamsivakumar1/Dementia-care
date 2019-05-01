package com.example.myapplication;

import android.app.Application;
import android.content.Intent;

public class DcareAppCtx extends Application {
    public String user_name;
    public int user_id;
    public int cursor;
    public int location_tracking_cursor;
    /* category 1 for patient, 2 for bystander 3 for admin */
    public int user_category;
    public void setUser_id(String user_id) {
        this.user_id = Integer.parseInt(user_id);
    }
    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
    public void setCursor(String cursor) {
        this.cursor = Integer.parseInt(cursor);
    }
    public void setUser_category(String cat) {
        if (cat.equalsIgnoreCase("patient") == true) {
            user_category = 1;
        } else if (cat.equalsIgnoreCase("bystander") == true) {
            user_category = 2;
        } else if (cat.equalsIgnoreCase("admin") == true){
            user_category = 3;
        }
    }

};
