package controllers;

import models.Friend;
import models.Group;
import models.Post;
import models.User;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.allusers;
import views.html.workflow_home;


/**
 * Created by Shuai Wang on 11/20/15.
 */
public class SubscribeController extends Controller {
    public static Result addSubscribe(String id) {
        if(id != null) {
            Friend.addSubscribe(id);
        }
        return ok(allusers.render(User.getAllUser()));
    }

    public static Result deleteSubscribe(String id) {
        if(id != null) {
            Friend.deleteSubscribe(id);
        }
        User user = new User(session().get("email"), session().get("password"));
        return ok(workflow_home.render(user, Post.all(), Friend.all(), Friend.allSubscribe(), Group.all()));
    }
}
