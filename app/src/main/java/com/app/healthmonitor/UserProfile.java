package com.app.healthmonitor;

public class UserProfile {
    public String name,email,age,height,weight,sex;
    public String temperature,heartrate,oxygen;

    public UserProfile(){}

    public UserProfile(String name, String email, String age, String height, String weight, String sex) {
        this.name = name;
        this.email = email;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.sex = sex;
    }
}
