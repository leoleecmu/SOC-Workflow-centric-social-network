package models;

import com.google.gson.JsonObject;

import javax.persistence.*;
import java.util.Date;


public class NewReply {

    private long id;

    private String content;
    private Date date;
    private boolean isAnswer;

    private NewPost newPost;

    public NewReply() {}
    public NewReply(String cont){
    	this.content=cont;
    }
    public NewReply(String content, Date date, boolean isAnswer) {
        this.content = content;
        this.date = date;
        this.isAnswer = isAnswer;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isAnswer() {
        return isAnswer;
    }

    public void setAnswer(boolean answer) {
        isAnswer = answer;
    }

    public NewPost getNewPost() {
        return newPost;
    }

    public void setNewPost(NewPost newPost) {
        this.newPost = newPost;
    }

}
