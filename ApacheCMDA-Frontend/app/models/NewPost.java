package models;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import controllers.Application;
import models.metadata.UserService;
import play.api.mvc.Controller;
import play.libs.Json;
import util.APICall;
import util.Constants;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by guoxi_000 on 11/19/2015.
 */
public class NewPost extends play.mvc.Controller {

    private long id;
    public String image;
    private String domain;               // [workflow|service|group]
    private String domainName;           // [workflowName|serviceName|groupName]
    public String Id;
    public String userName;
    private String title;                // post title
    private String content;              // post content
    private String attachment;           // attachment link of image, pdf or video
    private boolean isQuestion;          // When a user intends to post, she has an option to
                                         // decide whether to mark it as a discussion or a
                                         // question (if it is a question, then the starter of
                                         // the thread will be able to select a later reply as an
                                         // answer (i.e., the “mark as answer” as in Stackoverflow style).
    private int questionableCount;
    public String[] tags;
    private User newUser;

    private List<NewReply> newReplies = new ArrayList<NewReply>();

    public NewPost() {}
    
    public NewPost(long id,String title,String content){
    	 this.title = title;
         this.content = content;
         this.id=id;
    }

    public NewPost(String domain, String domainName, String title, String content, String attachment, boolean isQuestion, int questionableCount) {
        this.domain = domain;
        this.domainName = domainName;
        this.title = title;
        this.content = content;
        this.attachment = attachment;
        this.isQuestion = isQuestion;
        this.questionableCount = questionableCount;
    }

    public NewPost(String Id, String domain, String domainName, String title, String content, String attachment, String userName) {
        this.image = "http://graphics.wsj.com/six-degrees-of-lebron-james/img/LeBron_head.jpg";
        this.Id = Id;
        this.domain = domain;
        this.domainName = domainName;
        this.title = title;
        this.content = content;
        this.attachment = attachment;
        this.userName = userName;
    }

    public static List<NewPost> all() {
        List<NewPost> posts = new ArrayList<NewPost>();

        ObjectNode jsonData = Json.newObject();
        jsonData.put("email", session().get("email"));
        JsonNode response = UserService.getUserByEmail(jsonData);
        Application.sessionMsg(response);

        String userID = response.path("userId").asText();

        JsonNode allfriends = UserService.getAllFriends(userID);
        Iterator<JsonNode> it = allfriends.get("subscribeUsers").iterator();
        while (it.hasNext()) {
            JsonNode now = it.next();
            String friendID = now.get("userId").asText();
            String userName = now.get("userName").asText();
            JsonNode newUser = UserService.getAllFriends(friendID);
            Iterator<JsonNode> newIT = newUser.get("posts").iterator();
            while (newIT.hasNext()) {
                JsonNode post = newIT.next();
                String[] testTags = new String[]{"nba", "18655", "data"};
                NewPost post1 = new NewPost(post.get("postId").asText(), post.get("domain").asText(), post.get("domainName").asText(), post.get("title").asText(), post.get("content").asText(), post.get("attachment").asText(), userName);

                //Post post1 = new Post(post.get("Id").asText(), post.get("domain").asText(), post.get("domainName").asText(), post.get("title").asText(), post.get("content").asText(), post.get("attachment").asText());
                post1.setTags(testTags);
                posts.add(post1);
            }
        }

        return posts;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public boolean isQuestion() {
        return isQuestion;
    }

    public void setQuestion(boolean question) {
        isQuestion = question;
    }

    public int getQuestionableCount() {
        return questionableCount;
    }

    public void setQuestionableCount(int questionableCount) {
        this.questionableCount = questionableCount;
    }

    public User getNewUser() {
        return newUser;
    }

    public void setNewUser(User newUser) {
        this.newUser = newUser;
    }

//    public Set<NewTag> getPostTags() {
//        return postTags;
//    }

//    public void setPostTags(Set<NewTag> postTags) {
//        this.postTags = postTags;
//    }

    public List<NewReply> getNewReplies() {
        return newReplies;
    }

    public void setNewReplies(List<NewReply> newReplies) {
        this.newReplies = newReplies;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }
}