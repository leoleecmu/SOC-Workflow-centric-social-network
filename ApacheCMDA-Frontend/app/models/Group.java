
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


public class Group extends Controller {
    public String Title;
    public String subtitle;
    public boolean isAdmin;
    public String groupId;

    public ArrayList<Friend> friends;

    public Group(String title, String groupId, String subt, ArrayList friends, boolean isAdmin) {
        this.Title=title;
        this.subtitle=subt;
        this.groupId = groupId;
        this.friends = friends;
        this.isAdmin = isAdmin;
    }

    public boolean getAdminBoolean() {
        return isAdmin;
    }

    /**
     * TEST SET
     */

    public static List<Group> all() {
        List<Group> groups = new ArrayList<Group>();

        ObjectNode jsonData = Json.newObject();
        jsonData.put("email", session().get("email"));
        JsonNode response = UserService.getUserByEmail(jsonData);
        Application.sessionMsg(response);

        String userID = response.path("userId").asText();

        JsonNode allUser = UserService.getAllGroups(userID);

        /* Groups the user as admin */
        Iterator<JsonNode> it = allUser.get("adminGroups").iterator() ;
        while(it.hasNext()){
            JsonNode now = it.next();
            String groupID = now.get("groupId").asText();
            JsonNode groupInfo = UserService.getGroupInfo(groupID);
            String groupName = groupInfo.get("name").asText();
            ArrayList<Friend> userList=new ArrayList<Friend>();
            Iterator<JsonNode> adminit = groupInfo.get("adminUsers").iterator();
            while(adminit.hasNext()){
                JsonNode user = adminit.next();
                Friend wo = new Friend(user.get("userName").asText(), user.get("userId").asInt());
                userList.add(wo);
            }
            Iterator<JsonNode> memberit = groupInfo.get("memberUsers").iterator();
            while(memberit.hasNext()){
                JsonNode user = memberit.next();
                Friend wo = new Friend(user.get("userName").asText(), user.get("userId").asInt());
                userList.add(wo);
            }
            Group myGroup = new Group(groupName, groupID, "", userList, true);
            groups.add(myGroup);
        }

        it = allUser.get("memberGroups").iterator() ;
        while(it.hasNext()){
            JsonNode now = it.next();
            String groupID = now.get("groupId").asText();
            JsonNode groupInfo = UserService.getGroupInfo(groupID);
            String groupName = groupInfo.get("name").asText();
            ArrayList<Friend> userList=new ArrayList<Friend>();
            Iterator<JsonNode> adminit = groupInfo.get("adminUsers").iterator();
            while(adminit.hasNext()){
                JsonNode user = adminit.next();
                Friend wo = new Friend(user.get("userName").asText(), user.get("userId").asInt());
                userList.add(wo);
            }
            Iterator<JsonNode> memberit = groupInfo.get("memberUsers").iterator();
            while(memberit.hasNext()){
                JsonNode user = memberit.next();
                Friend wo = new Friend(user.get("userName").asText(), user.get("userId").asInt());
                userList.add(wo);
            }
            Group myGroup = new Group(groupName, groupID, "", userList, false);
            groups.add(myGroup);
        }

        return groups;
    }

    public static List<String> allGroupNames() {
        List<String> groups = new ArrayList<String>();

        ObjectNode jsonData = Json.newObject();
        jsonData.put("email", session().get("email"));
        JsonNode response = UserService.getUserByEmail(jsonData);
        Application.sessionMsg(response);

        String userID = response.path("userId").asText();

        JsonNode allUser = UserService.getAllGroups(userID);

        /* Groups the user as admin */
        Iterator<JsonNode> it = allUser.get("adminGroups").iterator() ;
        while(it.hasNext()){
            JsonNode now = it.next();
            String groupID = now.get("groupId").asText();
            JsonNode groupInfo = UserService.getGroupInfo(groupID);
            String groupName = groupInfo.get("name").asText();
            groups.add(groupName);
        }

        it = allUser.get("memberGroups").iterator() ;
        while(it.hasNext()){
            JsonNode now = it.next();
            String groupID = now.get("groupId").asText();
            JsonNode groupInfo = UserService.getGroupInfo(groupID);
            String groupName = groupInfo.get("name").asText();
            groups.add(groupName);
        }
        return groups;
    }

    public String getTitle() {
        return Title;
    }

    public String getSubTitle() {
        return subtitle;
    }

    public ArrayList<Friend> getFriends() {
        return friends;
    }

    public String getAdmin() {
        if(this.isAdmin) {
            return "admin";
        } else {
            return "";
        }
    }
}

