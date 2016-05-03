package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Group;
import models.metadata.GroupService;
import models.metadata.UserService;
import models.metadata.WorkflowService;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;


/**
 * Created by Shuai Wang on 11/20/15.
 */
public class GroupController extends Controller {
    final static Form<GroupService> groupServiceForm = Form
            .form(GroupService.class);

    public static Result showGroups() {
        return ok(groups.render(Group.all()));
    }

    public static Result deleteGroup(String id) {
        UserService.deleteGroup(id);
        return ok(groups.render(Group.all()));
    }

    public static Result newGroup() {
        return ok(newGroup.render(groupServiceForm));
    }

    public static Result addUserToGroup(String userId) {
        return ok(addToGroup.render(groupServiceForm, Group.allGroupNames(),userId));
    }

    public static Result addUserToGroupSubmit(String userId) {
        Form<GroupService> dc = groupServiceForm.bindFromRequest();
        String name = dc.field("name").value();
        UserService.addUserToGroup(userId, name);
        return ok(groups.render(Group.all()));
    }

    public static Result deleteUserFromGroup(String userId, String groupName) {
        UserService.deleteUserFromGroup(userId, groupName);
        return ok(groups.render(Group.all()));
    }

    public static Result createNewGroup() {
        Form<GroupService> dc = groupServiceForm.bindFromRequest();
        String name = dc.field("name").value();
        String isPublic = dc.field("isPublic").value().equals("") ? "public" : dc.field("isPublic").value();
        ObjectNode jsonData = Json.newObject();
        jsonData.put("email", session().get("email"));
        JsonNode response = UserService.getUserByEmail(jsonData);
        Application.sessionMsg(response);

        String userID = response.path("userId").asText();

        UserService.addGroup(userID, name, isPublic);
        return redirect("/workflowHome");
    }
}
