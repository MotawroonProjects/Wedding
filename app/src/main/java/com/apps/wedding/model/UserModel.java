package com.apps.wedding.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class UserModel extends StatusResponse {
    private Data data;

    public Data getData() {
        return data;
    }

    public class Data implements Serializable {
        private int id;
        private String user_type;
        private String logo;
        private String name;
        private String phone_code;
        private String phone;
        private String gender;
        private double latitude;
        private double longitude;
        private String address;
        private String token;

        public int getId() {
            return id;
        }

        public String getUser_type() {
            return user_type;
        }

        public String getLogo() {
            return logo;
        }

        public String getName() {
            return name;
        }

        public String getPhone_code() {
            return phone_code;
        }

        public String getPhone() {
            return phone;
        }

        public String getGender() {
            return gender;
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public String getAddress() {
            return address;
        }

        public String getToken() {
            return token;
        }
    }

}
