package controllers;

import models.Friend;
import models.User;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.allusers;
import views.html.editUser;
import views.html.forumDiscuss;


/**
 * Created by Shuai Wang on 11/20/15.
 */
public class UserController extends Controller {
    public static Result showUser(String name, int id) {
        return ok(editUser.render(name, id));
    }

    public static Result showAllUser() {
        return ok(allusers.render(User.getAllUser()));
    }

}
