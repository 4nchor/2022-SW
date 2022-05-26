package com.libienz.se_2022_closet.startApp_1.ootd;

public class CommentModel {

    public String uid;
    public String comment;

    public CommentModel(){ }
    public CommentModel(String uid, String comment) {
        this.uid = uid;
        this.comment = comment;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
