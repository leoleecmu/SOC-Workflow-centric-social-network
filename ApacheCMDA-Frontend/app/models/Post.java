package models;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import controllers.Application;
import models.metadata.UserService;
import play.libs.Json;
import play.mvc.Controller;
import util.APICall;
import util.Constants;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by guoxi_000 on 11/19/2015.
 */
public class Post extends Controller {
    public String image;
    public String Id;
    public String domain;
    public String domainName;
    public String title;
    public String content;
    public String attachment;
    public String userName;
    public String[] tags;

    private static final String GET_POSTS_CALL = Constants.NEW_BACKEND+"climateService/getAllPosts/json";

    public Post(String title) {
        this.title = title;
    }

    public Post(String Id, String domain, String domainName, String title, String content, String attachment, String userName) {
        this.image = "http://graphics.wsj.com/six-degrees-of-lebron-james/img/LeBron_head.jpg";
        this.Id = Id;
        this.domain = domain;
        this.domainName = domainName;
        this.title = title;
        this.content = content;
        this.attachment = attachment;
        this.userName = userName;
    }

    public static List<Post> all() {
        List<Post> posts = new ArrayList<Post>();

        ObjectNode jsonData = Json.newObject();
        jsonData.put("email", session().get("email"));
        JsonNode response = UserService.getUserByEmail(jsonData);
        Application.sessionMsg(response);

        String userID = response.path("userId").asText();

        JsonNode allfriends = UserService.getAllFriends(userID);
        Iterator<JsonNode> it = allfriends.get("subscribeUsers").iterator() ;
        while(it.hasNext()){
            JsonNode now = it.next();
            String friendID = now.get("userId").asText();
            String userName = now.get("userName").asText();
            JsonNode newUser = UserService.getAllFriends(friendID);
            Iterator<JsonNode> newIT = newUser.get("posts").iterator();
            while(newIT.hasNext()) {
                JsonNode post = newIT.next();
                String[] testTags = new String[] {"nba", "18655", "data"};
                Post post1 = new Post(post.get("postId").asText(), post.get("domain").asText(), post.get("domainName").asText(), post.get("title").asText(), post.get("content").asText(), post.get("attachment").asText(), userName);

                //Post post1 = new Post(post.get("Id").asText(), post.get("domain").asText(), post.get("domainName").asText(), post.get("title").asText(), post.get("content").asText(), post.get("attachment").asText());
                post1.setTags(testTags);
                posts.add(post1);
            }
        }

        /*
        JsonNode postsNode = APICall
                .callAPI(GET_POSTS_CALL);

        if (postsNode == null || postsNode.has("error")
                || !postsNode.isArray()) {
            return posts;
        }

        for (int i = 0; i < postsNode.size(); i++) {
            JsonNode json = postsNode.path(i);
            Post post = new Post(json.path("image").asText(),json.path("text").asText(),json.path("title").asText());
            posts.add(post);
        }
        */
        return posts;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public String getImage() {
        return image;
    }

    public String getTextHead() {
        return title;
    }

    public String getText() {
        return content;
    }
}
