package controllers;

import models.metadata.SearchService;
import models.metadata.WorkflowService;
import play.mvc.*;
import util.APICall;
import views.html.*;
import play.data.*;
import play.libs.Json;
import play.*;
import static play.data.Form.*;

import models.*;
import java.util.List;
import java.util.ArrayList;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import util.Constants;

/**
 * Created by Shuai Wang on 11/20/15.
 */
public class ForumController extends Controller {

    final static Form<PO> postForm = Form.form(PO.class);

    public static Result startDiscuss(String wfname) {
        ObjectNode jsonData = Json.newObject();
        List<NewPost> postList = new ArrayList<NewPost>();

        String GET_POST_CALL=Constants.NEW_BACKEND+"post/getPostsUnderDomainAndDomainName";
        try{
            jsonData.put("domain", "workflow");
            jsonData.put("domainName", wfname);
            JsonNode postResponse = APICall.postAPI(GET_POST_CALL, jsonData).get("posts");

            for(int i = 0; i < postResponse.size(); i++) {
                Long id = Long.parseLong(postResponse.path(i).get("postId").asText());

                String title=postResponse.path(i).get("title").asText();
                String content=postResponse.path(i).get("content").asText();

                NewPost np=new NewPost(id,title,content);
                postList.add(np);
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        return ok(forumDiscuss.render(WorkflowService.all(), postList));
    }

    public static final String GET_POST_CALL = Constants.NEW_BACKEND + "post/getPost";
    public static final String ADD_REPLY = Constants.NEW_BACKEND + "reply/addReply";
    public static final String ADD_REPLY_TO_POST = Constants.NEW_BACKEND + "reply/addReplyToPost";


    public static Result toSingle2(long id){

        //get replies with post id
        List<NewReply> replyList = new ArrayList<>();
        ObjectNode jsonData = Json.newObject();

        try{
            System.out.println("..enter:" + id);

            jsonData.put("postId", id);
            JsonNode getPostResponse = APICall.postAPI(GET_POST_CALL, jsonData);

            JsonNode replyNode = getPostResponse.path("replies");

            for(int i = 0; i < replyNode.size(); i++) {
                JsonNode json = replyNode.path(i);

                String content = json.path("content").asText();
                replyList.add(new NewReply(content));
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        session("cid", id + "");
        return ok(forumsingle2.render(postForm, replyList));
    }


    public static class PO {

        public String content;
        public PO(){}

        public PO(String content){
            this.content=content;
        }
        public String getContent(){
            return content;
        }

        public void setContent(String content){
            this.content=content;
        }
    }

    public static Result createNewReply() {
        Form<PO> nu = postForm.bindFromRequest();
        ObjectNode jsonData = Json.newObject();
        ObjectNode jsonData2 = Json.newObject();

        long postid = 0;

        try {
            String content = nu.get().getContent();
            jsonData.put("content", content);

            JsonNode replyResponse = APICall.postAPI(ADD_REPLY, jsonData);
            long replyid = Long.parseLong(replyResponse.get("replyId").asText());

            //add reply to post
            jsonData2.put("replyId", replyid);
            long cid = Long.parseLong(session().get("cid"));
            jsonData2.put("postId", cid);

            JsonNode replyResponse2 = APICall.postAPI(ADD_REPLY_TO_POST, jsonData2);
            postid = Long.parseLong(replyResponse2.get("postId").asText());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return redirect(
                routes.ForumController.toSingle2(postid)
        );
    }

    public static Result search(String tag) {
        return ok(forumDiscuss.render(SearchService.searchGetWorkflows(tag), NewPost.all()));
    }
}
