package controllers;

import models.Friend;
import models.Group;
import models.Post;
import models.User;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.allusers;
import views.html.editUser;
import views.html.workflow_home;


/**
 * Created by Shuai Wang on 11/20/15.
 */
public class FriendController extends Controller {
    public static Result addFriend(String id) {
        if(id != null) {
            Friend.addFriend(id);
        }
        return ok(allusers.render(User.getAllUser()));
    }

    public static Result deleteFriend(String id) {
        if(id != null) {
            Friend.deleteFriend(id);
        }
        User user = new User(session().get("email"), session().get("password"));
        return ok(workflow_home.render(user, Post.all(), Friend.all(), Friend.allSubscribe(), Group.all()));
    }

    public static Result acceptFriend(String id) {
        return ok(allusers.render(User.getAllUser()));
    }
}
