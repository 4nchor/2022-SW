package com.libienz.se_2022_closet.startApp_1.data;

public class User {

    public User(String email, String nickname, String gender) {
        this.email = email;
        this.nickname = nickname;
        this.gender = gender;
    }
    private String email;
    private String nickname;
    private String gender;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) { this.email = email; }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) { this.nickname = nickname; }


    public String getGender() {
        return gender;
    }

    public void setGender(String gender) { this.gender = gender; }



    @Override
    public String toString() {
        return email + "/"  + "/" + nickname + "/" + gender;
    }
}
