/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.databind.JsonNode;

import com.fasterxml.jackson.databind.node.ObjectNode;
import controllers.Application;
import models.metadata.ClimateService;

import java.util.*;

import javax.persistence.*;

import models.metadata.UserService;
import play.data.validation.Constraints;
import play.libs.Json;
import play.mvc.Controller;
import util.APICall;
import util.Constants;

import java.util.ArrayList;
import java.util.List;
public class Friend  extends Controller {

    private String userName;
    private String firstName;
    private String lastName;
    private String title;
    private String email;
    private String phoneNumber;
    private Integer id;



    public Friend(String username, int id) {
        this.userName=username;
        this.id = id;
    }

    public static List<Friend> all() {
        ObjectNode jsonData = Json.newObject();
        jsonData.put("email", session().get("email"));
        JsonNode response = UserService.getUserByEmail(jsonData);
        Application.sessionMsg(response);

        String userID = response.path("userId").asText();

        JsonNode allfriends = UserService.getAllFriends(userID);
        Iterator<JsonNode> it = allfriends.get("friends").iterator() ;

        List<Friend> friends = new ArrayList<Friend>();
        while(it.hasNext()){
            JsonNode now = it.next();
            friends.add(new Friend(now.get("userName").asText(), now.get("userId").asInt()));
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
        return friends;
    }

    public static List<Friend> allSubscribe() {
        ObjectNode jsonData = Json.newObject();
        jsonData.put("email", session().get("email"));
        JsonNode response = UserService.getUserByEmail(jsonData);
        Application.sessionMsg(response);

        String userID = response.path("userId").asText();

        JsonNode allfriends = UserService.getAllFriends(userID);
        Iterator<JsonNode> it = allfriends.get("subscribeUsers").iterator() ;

        List<Friend> friends = new ArrayList<Friend>();
        while(it.hasNext()){
            JsonNode now = it.next();
            friends.add(new Friend(now.get("userName").asText(), now.get("userId").asInt()));
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
        return friends;
    }

    public static String addFriend(String id) {
        ObjectNode jsonData = Json.newObject();
        jsonData.put("email", session().get("email"));
        JsonNode response = UserService.getUserByEmail(jsonData);
        Application.sessionMsg(response);

        String userID = response.path("userId").asText();

        JsonNode allfriends = UserService.addFriend(userID, id);
        return "success";
    }

    public static String deleteFriend(String id) {
        ObjectNode jsonData = Json.newObject();
        jsonData.put("email", session().get("email"));
        JsonNode response = UserService.getUserByEmail(jsonData);
        Application.sessionMsg(response);

        String userID = response.path("userId").asText();

        JsonNode allfriends = UserService.deleteFriend(userID, id);
        return "success";
    }

    public static String addSubscribe(String id) {
        ObjectNode jsonData = Json.newObject();
        jsonData.put("email", session().get("email"));
        JsonNode response = UserService.getUserByEmail(jsonData);
        Application.sessionMsg(response);

        String userID = response.path("userId").asText();

        JsonNode allfriends = UserService.addSubscribe(userID, id);
        return "success";
    }

    public static String deleteSubscribe(String id) {
        ObjectNode jsonData = Json.newObject();
        jsonData.put("email", session().get("email"));
        JsonNode response = UserService.getUserByEmail(jsonData);
        Application.sessionMsg(response);

        String userID = response.path("userId").asText();

        JsonNode allfriends = UserService.deleteSubscribe(userID, id);
        return "success";
    }

    public String getName(){
        return this.userName;
    }

    public Integer getID() {
        return this.id;
    }

    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
    }

    public Friend() {
    }

    public Friend(String userName,String firstName,
                  String lastName,
                  String title, String email,
                  String phoneNumber) {

        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.title = title;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }




    public String getTitle() {
        return title;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }


    //
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}