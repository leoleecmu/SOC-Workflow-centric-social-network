package models.metadata;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Friend;
import models.Post;
import play.libs.Json;
import util.APICall;
import util.Constants;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by guoxi_000 on 12/10/2015.
 */
public class SearchService {

    private static final String GET_TAG_ID = Constants.NEW_BACKEND+"tag/getTagIdByName";
    private static final String SEARCH = Constants.NEW_BACKEND+"tag/getTag";

    public String tag;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    static public List<WorkflowService> searchGetWorkflows(String tag) {
        List<WorkflowService> workflows = new ArrayList<WorkflowService>();

        ObjectNode userData = Json.newObject();
        userData.put("name", tag);
        JsonNode idResponse = APICall.postAPI(GET_TAG_ID, userData);

        System.out.println(tag + "==================================="+ idResponse);

        if(idResponse != null && idResponse.get("tagId") != null) {
            String id = idResponse.get("tagId").asText();

            userData = Json.newObject();
            userData.put("tagId", id);
            idResponse = APICall.postAPI(SEARCH, userData);
            Iterator<JsonNode> it = idResponse.get("workflows").iterator();
            ;
            while (it.hasNext()) {
                JsonNode workflow = it.next();
                WorkflowService workflowService = new WorkflowService();
                workflowService.setName(workflow.get("name").asText());
                workflows.add(workflowService);
            }
        } else {
            workflows = WorkflowService.all();
        }
        return workflows;
    }

    static public List<Post> searchGetPosts(String tag) {
        List<Post> posts = new ArrayList<Post>();

        ObjectNode userData = Json.newObject();
        userData.put("name", tag);
        JsonNode idResponse = APICall.postAPI(GET_TAG_ID, userData);

        if(idResponse != null && idResponse.get("tagId") != null) {

            String id = idResponse.get("tagId").asText();

            userData = Json.newObject();
            userData.put("tagId", id);
            idResponse = APICall.postAPI(SEARCH, userData);
            Iterator<JsonNode> it = idResponse.get("posts").iterator();
            ;
            while (it.hasNext()) {
                JsonNode node = it.next();
                Post post = new Post(node.get("title").asText());
                posts.add(post);
            }
        } else {
            posts = Post.all();
        }
        return posts;
    }
}
