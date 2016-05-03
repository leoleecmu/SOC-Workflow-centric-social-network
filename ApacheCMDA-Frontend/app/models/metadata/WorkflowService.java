package models.metadata;

import com.amazonaws.services.simpleworkflow.flow.annotations.Workflow;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.JsonObject;
import org.apache.commons.collections.map.HashedMap;
import play.libs.Json;
import scala.collection.immutable.Stream;
import scala.util.parsing.json.JSONArray;
import util.Constants;
import util.APICall;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wang on 12/2/15.
 */
public class WorkflowService {

    private String wfId;
    private String name;
    private String description;

    private String url;
    private List<String> contributors;

    // popularity part
    private String usageCount;
    private String viewCount;
    private String downloadCount;
    private String referenceCount;
    private String questionableCount;
    private String popularity;

    private List<List<String>> tasks;
    private List<List<String>> inputs;
    private List<List<String>> outputs;
    private List<List<String>> instructions;
    private List<List<String>> datasets;
    private List<List<String>> links;

    private List<String> tagsList;
    private List<WorkflowService> attributeWorkflows;

    // access control part
    private List<String> viewUserId;
    private List<String> editUserId;
    private List<String> viewGroupId;
    private List<String> editGroupId;

    private static final String ADD_WORKFLOW_CALL = Constants.NEW_BACKEND+"workflow/addWorkflow";
    private static final String ADD_TASK_CALL = Constants.NEW_BACKEND + "task/addTask";
    private static final String ADD_TASK_TO_WORKFLOW_CALL = Constants.NEW_BACKEND + "task/addTaskToWorkflow";
    private static final String ADD_INPUT_CALL = Constants.NEW_BACKEND + "input/addInput";
    private static final String ADD_INPUT_TO_WORKFLOW_CALL = Constants.NEW_BACKEND + "input/addInputToWorkflow";
    private static final String ADD_OUTPUT_CALL = Constants.NEW_BACKEND + "output/addOutput";
    private static final String ADD_OUTPUT_TO_WORKFLOW_CALL = Constants.NEW_BACKEND + "output/addOutputToWorkflow";
    private static final String ADD_INSTRUCTION_CALL = Constants.NEW_BACKEND + "instruction/addInstruction";
    private static final String ADD_INSTRUCTION_TO_WORKFLOW_CALL = Constants.NEW_BACKEND + "instruction/addInstructionToWorkflow";
    private static final String ADD_DATASET_CALL = Constants.NEW_BACKEND + "datasetV2/addDataset";
    private static final String ADD_DATASET_TO_WORKFLOW_CALL = Constants.NEW_BACKEND + "datasetV2/addDatasetToWorkflow";
    private static final String ADD_LINK_CALL = Constants.NEW_BACKEND + "link/addLink";
    private static final String ADD_LINK_TO_WORKFLOW_CALL = Constants.NEW_BACKEND + "link/addLinkToWorkflow";
    private static final String ADD_TAG_CALL = Constants.NEW_BACKEND + "tag/addTag";
    private static final String ADD_TAG_TO_WORKFLOW_CALL = Constants.NEW_BACKEND + "workflow/addTagToWorkflow";
    private static final String ADD_CONTRIBUTOR_TO_WORKFLOW_CALL = Constants.NEW_BACKEND + "user/addContributorToWorkflow";

    private static final String GET_WORKFLOW_BY_NAME_CALL = Constants.NEW_BACKEND + "workflow/getWorkflowIdByName";
    private static final String ADD_ATTRIBUTEWORKFLOW_CALL = Constants.NEW_BACKEND + "workflow/addAttributeWorkflowToWorkflow";

    private static final String GET_WORKFLOW_BY_ID_CALL = Constants.NEW_BACKEND + "workflow/getWorkflow";
    private static final String GET_ALL_WORKFLOWS_CALL = Constants.NEW_BACKEND + "workflow/getAllWorkflows";
    private static final String GET_WF_POPULARITY_CALL = Constants.NEW_BACKEND + "workflow/getWorkflowPopularity";

    private static final String ADD_POST_CALL = Constants.NEW_BACKEND + "post/addPost";
    private static final String ADD_POST_TO_USER_CALL = Constants.NEW_BACKEND + "post/addPostToUser";

    public static JsonNode create(JsonNode jsonData) {
        System.out.println("");

        //add workflow, get ID
        JsonNode addResponse = APICall.postAPI(ADD_WORKFLOW_CALL, jsonData);
        String workflowId = addResponse.get("workflowId").asText();

        //add contributors
        ObjectNode contriJason = Json.newObject();
        contriJason.put("userId", jsonData.path("contributor").asText());
        contriJason.put("workflowId", workflowId);
        JsonNode addContributorToWfResponse = APICall.postAPI(ADD_CONTRIBUTOR_TO_WORKFLOW_CALL, contriJason);

        //add task, get taskID
        String[] taskDes = jsonData.path("tasks").asText().split(":");
        ObjectNode taskJson = Json.newObject();
        taskJson.put("name", taskDes[0]);
        taskJson.put("description", taskDes[1]);
        JsonNode addTaskResponse = APICall.postAPI(ADD_TASK_CALL, taskJson);
        String taskId = addTaskResponse.path("taskId").asText();
        //add task to workflow
        ObjectNode addTaskToWfJson = Json.newObject();
        addTaskToWfJson.put("taskId", taskId);
        addTaskToWfJson.put("workflowId", workflowId);
        JsonNode addTaskToWfResponse = APICall.postAPI(ADD_TASK_TO_WORKFLOW_CALL, addTaskToWfJson);


        //add input, get inputId
        String[] inputDes = jsonData.path("inputs").asText().split(":");
        ObjectNode inputJason = Json.newObject();
        inputJason.put("name", inputDes[0]);
        inputJason.put("description", inputDes[1]);
        JsonNode inputResponse = APICall.postAPI(ADD_INPUT_CALL, inputJason);
        String inputId = inputResponse.path("inputId").asText();
        // add input to workflow
        ObjectNode addInputToWfJson = Json.newObject();
        addInputToWfJson.put("inputId", inputId);
        addInputToWfJson.put("workflowId", workflowId);
        JsonNode addInputToWfResponse = APICall.postAPI(ADD_INPUT_TO_WORKFLOW_CALL, addInputToWfJson);


        //add output, get outputId
        String[] outputDes = jsonData.path("outputs").asText().split(":");
        ObjectNode outputJason = Json.newObject();
        outputJason.put("name", outputDes[0]);
        outputJason.put("description", outputDes[1]);
        JsonNode outputResponse = APICall.postAPI(ADD_OUTPUT_CALL, outputJason);
        String outputId = outputResponse.path("outputId").asText();
        // add output to workflow
        ObjectNode addOutputToWfJson = Json.newObject();
        addOutputToWfJson.put("outputId", outputId);
        addOutputToWfJson.put("workflowId", workflowId);
        JsonNode addOutputToWfResponse = APICall.postAPI(ADD_OUTPUT_TO_WORKFLOW_CALL, addOutputToWfJson);


        //add instructions, get instructionId
        String[] instruDes = jsonData.path("instructions").asText().split(":");
        ObjectNode insJson = Json.newObject();
        insJson.put("name", instruDes[0]);
        insJson.put("description", instruDes[1]);
        JsonNode instruResponse = APICall.postAPI(ADD_INSTRUCTION_CALL, insJson);
        String instrucId = instruResponse.path("instructionId").asText();
        // add instruction to workflow
        ObjectNode addInstruToWfJson = Json.newObject();
        addInstruToWfJson.put("instructionId", instrucId);
        addInstruToWfJson.put("workflowId", workflowId);
        JsonNode addInstruToWfResponse = APICall.postAPI(ADD_INSTRUCTION_TO_WORKFLOW_CALL, addInstruToWfJson);


        //add dataset, get datasetId
        String[] datasetArr = jsonData.path("datasets").asText().split(":");
        ObjectNode dsJson = Json.newObject();
        dsJson.put("name", datasetArr[0]);
        dsJson.put("content", datasetArr[1]);
        JsonNode dsResponse = APICall.postAPI(ADD_DATASET_CALL, dsJson);
        String dsId = dsResponse.path("datasetId").asText();
        // add dataset to workflow
        ObjectNode addDsToWfJson = Json.newObject();
        addDsToWfJson.put("datasetId", dsId);
        addDsToWfJson.put("workflowId", workflowId);
        JsonNode addDsToWfResponse = APICall.postAPI(ADD_DATASET_TO_WORKFLOW_CALL, addDsToWfJson);


        //add link, get LinkId
        String[] linkArr = jsonData.path("links").asText().split(",");
        ObjectNode linkJson = Json.newObject();
        linkJson.put("source", linkArr[0]);
        linkJson.put("sink", linkArr[1]);
        JsonNode linkResponse = APICall.postAPI(ADD_LINK_CALL, linkJson);
        String linkId = linkResponse.path("linkId").asText();
        // add link to workflow
        ObjectNode addLinkToWfJson = Json.newObject();
        addLinkToWfJson.put("linkId", linkId);
        addLinkToWfJson.put("workflowId", workflowId);
        JsonNode addLinkToWfResponse = APICall.postAPI(ADD_LINK_TO_WORKFLOW_CALL, addLinkToWfJson);


        //add tag, get tagId
        ObjectNode tagJson = Json.newObject();
        tagJson.put("name", jsonData.path("tags").asText());
        JsonNode tagResponse = APICall.postAPI(ADD_TAG_CALL, tagJson);
        String tagId = tagResponse.path("tagId").asText();
        // add tag to workflow
        ObjectNode addTagToWfJson = Json.newObject();
        addTagToWfJson.put("tagId", tagId);
        addTagToWfJson.put("workflowId", workflowId);
        JsonNode addTagToWfResponse = APICall.postAPI(ADD_TAG_TO_WORKFLOW_CALL, addTagToWfJson);


        //add attribute workflow
        ObjectNode attJson = Json.newObject();
        attJson.put("name", jsonData.path("attributeWorkflow").asText());
        JsonNode attResponse = APICall.postAPI(GET_WORKFLOW_BY_NAME_CALL, attJson);
        String attWfId = attResponse.path("workflowId").asText();
        //add attribute workflow to workflow
        ObjectNode addAttToWfJson = Json.newObject();
        addAttToWfJson.put("attributeWorkflowId", attWfId);
        addAttToWfJson.put("workflowId", workflowId);
        JsonNode addAttToWfResponse = APICall.postAPI(ADD_ATTRIBUTEWORKFLOW_CALL, addAttToWfJson);

        //add access control
//        ObjectNode accessJson = Json.newObject();
//        accessJson.put("viewUserId", jsonData.path("viewUserId").asText());
//        accessJson.put("editUserId", jsonData.path("editUserId").asText());
//        accessJson.put("viewGroupId", jsonData.path("viewGroupId").asText());
//        accessJson.put("editGroupId", jsonData.path("editGroupId").asText());
//        JsonNode accessResponse = APICall.postAPI(ADD_ACCESS_CONTROL_CALL, accessJson);


        //get workflow
        ObjectNode getJson = Json.newObject();
        getJson.put("workflowId", workflowId);
        JsonNode getResponse = APICall.postAPI(GET_WORKFLOW_BY_ID_CALL, getJson);


        return getResponse;
    }


    public static List<WorkflowService> all() {
        List<WorkflowService> allWorkflows = new ArrayList<>();
        JsonNode node = APICall.postAPI(GET_ALL_WORKFLOWS_CALL, Json.newObject());
        JsonNode allwf = node.get("allWorkflows");

        if (allwf == null || allwf.has("error") || !allwf.isArray()) {
            return allWorkflows;
        }

        for (int i = 0; i < allwf.size(); i++) {
            JsonNode json = allwf.path(i);
            WorkflowService wfs = new WorkflowService();

            String id = json.path("workflowId").asText();
            wfs.setWfId(id);
            wfs.setName(json.path("name").asText());
            wfs.setDescription(json.path("description").asText());
            wfs.setUrl(json.path("previewImage").asText());
            wfs.setUsageCount(json.path("usageCount").asText());
            wfs.setViewCount(json.path("viewCount").asText());
            wfs.setReferenceCount(json.path("referenceCount").asText());
            wfs.setDownloadCount(json.path("downloadCount").asText());
            wfs.setQuestionableCount(json.path("questionableCount").asText());

            ObjectNode ppJson = Json.newObject();
            ppJson.put("workflowId", id);
            JsonNode ppResponse = APICall.postAPI(GET_WF_POPULARITY_CALL, ppJson);
            String pp = ppResponse.path("popularity").asText();
            wfs.setPopularity(pp);

            allWorkflows.add(wfs);
        }


        return allWorkflows;
    }

    public static WorkflowService getOneWorkflow(String id) {
        WorkflowService wfs = new WorkflowService();

        ObjectNode wfJson = Json.newObject();
        wfJson.put("workflowId", id);
        JsonNode wfResponse = APICall.postAPI(GET_WORKFLOW_BY_ID_CALL, wfJson);

        wfs.setWfId(id);
        wfs.setName(wfResponse.path("name").asText());
        wfs.setDescription(wfResponse.path("description").asText());
        wfs.setUrl(wfResponse.path("previewImage").asText());
        wfs.setUsageCount(wfResponse.path("usageCount").asText());
        wfs.setViewCount(wfResponse.path("viewCount").asText());
        wfs.setReferenceCount(wfResponse.path("referenceCount").asText());
        wfs.setDownloadCount(wfResponse.path("downloadCount").asText());
        wfs.setQuestionableCount(wfResponse.path("questionableCount").asText());

        // set access control, but no interface in backend.
//        wfs.setViewUserId(wfResponse.path("viewUserId").asText());
//        wfs.setEditUserId(wfResponse.path("editUserId").asText());
//        wfs.setViewGroupId(wfResponse.path("viewGroupId").asText());
//        wfs.setEditGroupId(wfResponse.path("editGroupId").asText());

        // set popularity
        ObjectNode ppJson = Json.newObject();
        ppJson.put("workflowId", id);
        JsonNode ppResponse = APICall.postAPI(GET_WF_POPULARITY_CALL, ppJson);
        String pp = ppResponse.path("popularity").asText();
        wfs.setPopularity(pp);

        //set contributors
        JsonNode contriNode = wfResponse.get("contributors");
        List<String> contriList = new ArrayList<>();
        for(int i = 0; i < contriNode.size(); i++) {
            JsonNode json = contriNode.path(i);

            String name = json.path("userName").asText();
            contriList.add(name);
        }
        wfs.setContributors(contriList);

        // set tags
        JsonNode tagsNode = wfResponse.get("tags");
        List<String> tagList = new ArrayList<>();
        for(int i = 0; i < tagsNode.size(); i++) {
            JsonNode json = tagsNode.path(i);

            String name = json.path("name").asText();
            tagList.add(name);
        }
        wfs.setTagsList(tagList);

        // set tasks
        JsonNode tasksNode = wfResponse.get("tasks");
        List<List<String>> taskList = new ArrayList<>();
        for(int i = 0; i < tasksNode.size(); i++) {
            JsonNode json = tasksNode.path(i);

            List<String> temp = new ArrayList<>();
            String tId = json.path("taskId").asText();
            String name = json.path("name").asText();
            String decription = json.path("description").asText();
            temp.add(tId);
            temp.add(name);
            temp.add(decription);
            taskList.add(temp);
        }
        wfs.setTasks(taskList);

        // set inputs
        JsonNode inputNode = wfResponse.get("inputs");
        List<List<String>> inputList = new ArrayList<>();
        for(int i = 0; i < inputNode.size(); i++) {
            JsonNode json = inputNode.path(i);

            List<String> temp = new ArrayList<>();
            String iId = json.path("inputId").asText();
            String name = json.path("name").asText();
            String description = json.path("description").asText();
            temp.add(iId);
            temp.add(name);
            temp.add(description);
            inputList.add(temp);
        }
        wfs.setInputs(inputList);

        // set outputs
        JsonNode outputNode = wfResponse.get("outputs");
        List<List<String>> outputList = new ArrayList<>();
        for(int i = 0; i < outputNode.size(); i++) {
            JsonNode json = outputNode.path(i);

            List<String> temp = new ArrayList<>();
            String oId = json.path("outputId").asText();
            String name = json.path("name").asText();
            String description = json.path("description").asText();
            temp.add(oId);
            temp.add(name);
            temp.add(description);
            outputList.add(temp);
        }
        wfs.setOutputs(outputList);

        // set instructions
        JsonNode instructionNode = wfResponse.get("instructions");
        List<List<String>> instructionList = new ArrayList<>();
        for(int i = 0; i < instructionNode.size(); i++) {
            JsonNode json = instructionNode.path(i);

            List<String> temp = new ArrayList<>();
            String iId = json.path("instructionId").asText();
            String name = json.path("name").asText();
            String description = json.path("description").asText();
            temp.add(iId);
            temp.add(name);
            temp.add(description);
            instructionList.add(temp);
        }
        wfs.setInstructions(instructionList);

        // set datasets
        JsonNode datasetNode = wfResponse.get("datasets");
        List<List<String>> datasetList = new ArrayList<>();
        for(int i = 0; i < datasetNode.size(); i++) {
            JsonNode json = datasetNode.path(i);

            List<String> temp = new ArrayList<>();
            String dId = json.path("datasetId").asText();
            String name = json.path("name").asText();
            String content = json.path("content").asText();
            temp.add(dId);
            temp.add(name);
            temp.add(content);
            datasetList.add(temp);
        }
        wfs.setDatasets(datasetList);

        // set links
        JsonNode linkNode = wfResponse.get("links");
        List<List<String>> linkList = new ArrayList<>();
        for(int i = 0; i < linkNode.size(); i++) {
            JsonNode json = linkNode.path(i);

            List<String> temp = new ArrayList<>();
            String lId = json.path("linkId").asText();
            String source = json.path("source").asText();
            String sink = json.path("sink").asText();
            temp.add(lId);
            temp.add(source);
            temp.add(sink);
            linkList.add(temp);
        }
        wfs.setLinks(linkList);


        // set attribute workflow
        JsonNode attributeNode = wfResponse.get("attributeWorkflows");
        List<WorkflowService> attriList = new ArrayList<>();
        for(int i = 0; i < attributeNode.size(); i++) {
            JsonNode json = attributeNode.path(i);

            WorkflowService temp = new WorkflowService();

            temp.setWfId(json.path("workflowId").asText());
            temp.setName(json.path("name").asText());
            temp.setDescription(json.path("description").asText());
            temp.setUrl(json.path("previewImage").asText());

            attriList.add(temp);
        }
        wfs.setAttributeWorkflows(attriList);


        return wfs;
    }

    public static JsonNode addNewTag(JsonNode jsonData) {
        String tag = jsonData.path("tags").asText();
        String id = jsonData.path("wfId").asText();

        //add tag, get tagId
        ObjectNode tagJson = Json.newObject();
        tagJson.put("name", tag);
        JsonNode tagResponse = APICall.postAPI(ADD_TAG_CALL, tagJson);
        String tagId = tagResponse.path("tagId").asText();
        // add tag to workflow
        ObjectNode addTagToWfJson = Json.newObject();
        addTagToWfJson.put("tagId", tagId);
        addTagToWfJson.put("workflowId", id);
        JsonNode addTagToWfResponse = APICall.postAPI(ADD_TAG_TO_WORKFLOW_CALL, addTagToWfJson);

        return addTagToWfResponse;
    }

    public static JsonNode addNewInput(JsonNode jsonData) {
        String id = jsonData.path("wfId").asText();

        //add input, get inputId
        String[] inputDes = jsonData.path("inputs").asText().split(":");
        ObjectNode inputJason = Json.newObject();
        inputJason.put("name", inputDes[0]);
        inputJason.put("description", inputDes[1]);
        JsonNode inputResponse = APICall.postAPI(ADD_INPUT_CALL, inputJason);
        String inputId = inputResponse.path("inputId").asText();
        // add input to workflow
        ObjectNode addInputToWfJson = Json.newObject();
        addInputToWfJson.put("inputId", inputId);
        addInputToWfJson.put("workflowId", id);
        JsonNode addInputToWfResponse = APICall.postAPI(ADD_INPUT_TO_WORKFLOW_CALL, addInputToWfJson);

        return addInputToWfResponse;
    }

    public static JsonNode addNewOutput(JsonNode jsonData) {
        String id = jsonData.path("wfId").asText();

        //add output, get outputId
        String[] outputDes = jsonData.path("outputs").asText().split(":");
        ObjectNode outputJason = Json.newObject();
        outputJason.put("name", outputDes[0]);
        outputJason.put("description", outputDes[1]);
        JsonNode outputResponse = APICall.postAPI(ADD_OUTPUT_CALL, outputJason);
        String outputId = outputResponse.path("outputId").asText();
        // add output to workflow
        ObjectNode addOutputToWfJson = Json.newObject();
        addOutputToWfJson.put("outputId", outputId);
        addOutputToWfJson.put("workflowId", id);
        JsonNode addOutputToWfResponse = APICall.postAPI(ADD_OUTPUT_TO_WORKFLOW_CALL, addOutputToWfJson);

        return addOutputToWfResponse;
    }

    public static JsonNode addNewTask(JsonNode jsonData) {
        String id = jsonData.path("wfId").asText();

        //add task, get taskID
        String[] taskDes = jsonData.path("tasks").asText().split(":");
        ObjectNode taskJson = Json.newObject();
        taskJson.put("name", taskDes[0]);
        taskJson.put("description", taskDes[1]);
        JsonNode addTaskResponse = APICall.postAPI(ADD_TASK_CALL, taskJson);
        String taskId = addTaskResponse.path("taskId").asText();
        //add task to workflow
        ObjectNode addTaskToWfJson = Json.newObject();
        addTaskToWfJson.put("taskId", taskId);
        addTaskToWfJson.put("workflowId", id);
        JsonNode addTaskToWfResponse = APICall.postAPI(ADD_TASK_TO_WORKFLOW_CALL, addTaskToWfJson);

        return addTaskToWfResponse;
    }

    public static JsonNode addNewInstruction(JsonNode jsonData) {
        String id = jsonData.path("wfId").asText();

        //add instructions, get instructionId
        String[] instruDes = jsonData.path("instructions").asText().split(":");
        ObjectNode insJson = Json.newObject();
        insJson.put("name", instruDes[0]);
        insJson.put("description", instruDes[1]);
        JsonNode instruResponse = APICall.postAPI(ADD_INSTRUCTION_CALL, insJson);
        String instrucId = instruResponse.path("instructionId").asText();
        // add instruction to workflow
        ObjectNode addInstruToWfJson = Json.newObject();
        addInstruToWfJson.put("instructionId", instrucId);
        addInstruToWfJson.put("workflowId", id);
        JsonNode addInstruToWfResponse = APICall.postAPI(ADD_INSTRUCTION_TO_WORKFLOW_CALL, addInstruToWfJson);

        return addInstruToWfResponse;
    }

    public static JsonNode addNewDataset(JsonNode jsonData) {
        String id = jsonData.path("wfId").asText();

        //add dataset, get datasetId
        String[] datasetArr = jsonData.path("datasets").asText().split(":");
        ObjectNode dsJson = Json.newObject();
        dsJson.put("name", datasetArr[0]);
        dsJson.put("content", datasetArr[1]);
        JsonNode dsResponse = APICall.postAPI(ADD_DATASET_CALL, dsJson);
        String dsId = dsResponse.path("datasetId").asText();
        // add dataset to workflow
        ObjectNode addDsToWfJson = Json.newObject();
        addDsToWfJson.put("datasetId", dsId);
        addDsToWfJson.put("workflowId", id);
        JsonNode addDsToWfResponse = APICall.postAPI(ADD_DATASET_TO_WORKFLOW_CALL, addDsToWfJson);

        return addDsToWfResponse;
    }

    public static JsonNode addNewLink(JsonNode jsonData) {
        String id = jsonData.path("wfId").asText();

        //add link, get LinkId
        String[] linkArr = jsonData.path("links").asText().split(",");
        ObjectNode linkJson = Json.newObject();
        linkJson.put("source", linkArr[0]);
        linkJson.put("sink", linkArr[1]);
        JsonNode linkResponse = APICall.postAPI(ADD_LINK_CALL, linkJson);
        String linkId = linkResponse.path("linkId").asText();
        // add link to workflow
        ObjectNode addLinkToWfJson = Json.newObject();
        addLinkToWfJson.put("linkId", linkId);
        addLinkToWfJson.put("workflowId", id);
        JsonNode addLinkToWfResponse = APICall.postAPI(ADD_LINK_TO_WORKFLOW_CALL, addLinkToWfJson);

        return addLinkToWfResponse;
    }

    public static JsonNode addNewAttribute(JsonNode jsonData) {
        String id = jsonData.path("wfId").asText();

        //add attribute workflow
        ObjectNode attJson = Json.newObject();
        attJson.put("name", jsonData.path("attributeWorkflow").asText());
        JsonNode attResponse = APICall.postAPI(GET_WORKFLOW_BY_NAME_CALL, attJson);
        String attWfId = attResponse.path("workflowId").asText();
        //add attribute workflow to workflow
        ObjectNode addAttToWfJson = Json.newObject();
        addAttToWfJson.put("attributeWorkflowId", attWfId);
        addAttToWfJson.put("workflowId", id);
        JsonNode addAttToWfResponse = APICall.postAPI(ADD_ATTRIBUTEWORKFLOW_CALL, addAttToWfJson);

        return addAttToWfResponse;
    }

    public static JsonNode addNewPost(JsonNode jsonData) {
        String id = jsonData.path("wfId").asText();

        ObjectNode postJson = Json.newObject();
        postJson.put("domain", jsonData.path("domain").asText());
        postJson.put("domainName", jsonData.path("domainName").asText());
        postJson.put("title", jsonData.path("title").asText());
        postJson.put("content", jsonData.path("content").asText());
        postJson.put("attachment", "https://localizationlocalisation.wordpress.com/tag/workflow/");
        postJson.put("isQuestion", true);
        JsonNode postResponse = APICall.postAPI(ADD_POST_CALL, postJson);

        String pid = postResponse.path("postId").asText();
        String uid = jsonData.path("userID").asText();
        ObjectNode addJson = Json.newObject();
        addJson.put("postId", pid);
        addJson.put("userId", uid);
        JsonNode addResponse = APICall.postAPI(ADD_POST_TO_USER_CALL, addJson);

        return addResponse;
    }



    // getters and setters
    public String getWfId() {
        return wfId;
    }

    public void setWfId(String wfId) {
        this.wfId = wfId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsageCount() {
        return usageCount;
    }

    public void setUsageCount(String usageCount) {
        this.usageCount = usageCount;
    }

    public String getViewCount() {
        return viewCount;
    }

    public void setViewCount(String viewCount) {
        this.viewCount = viewCount;
    }

    public String getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(String downloadCount) {
        this.downloadCount = downloadCount;
    }

    public String getReferenceCount() {
        return referenceCount;
    }

    public void setReferenceCount(String referenceCount) {
        this.referenceCount = referenceCount;
    }

    public String getQuestionableCount() {
        return questionableCount;
    }

    public void setQuestionableCount(String questionableCount) {
        this.questionableCount = questionableCount;
    }

    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    public List<String> getTagsList() {
        return tagsList;
    }

    public void setTagsList(List<String> tagsList) {
        this.tagsList = tagsList;
    }

    public List<List<String>> getTasks() {
        return tasks;
    }

    public void setTasks(List<List<String>> tasks) {
        this.tasks = tasks;
    }

    public List<List<String>> getInputs() {
        return inputs;
    }

    public void setInputs(List<List<String>> inputs) {
        this.inputs = inputs;
    }

    public List<List<String>> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<List<String>> outputs) {
        this.outputs = outputs;
    }

    public List<List<String>> getInstructions() {
        return instructions;
    }

    public void setInstructions(List<List<String>> instructions) {
        this.instructions = instructions;
    }

    public List<List<String>> getDatasets() {
        return datasets;
    }

    public void setDatasets(List<List<String>> datasets) {
        this.datasets = datasets;
    }

    public List<List<String>> getLinks() {
        return links;
    }

    public void setLinks(List<List<String>> links) {
        this.links = links;
    }

    public List<WorkflowService> getAttributeWorkflows() {
        return attributeWorkflows;
    }

    public void setAttributeWorkflows(List<WorkflowService> attributeWorkflows) {
        this.attributeWorkflows = attributeWorkflows;
    }

    public List<String> getViewUserId() {
        return viewUserId;
    }

    public void setViewUserId(List<String> viewUserId) {
        this.viewUserId = viewUserId;
    }

    public List<String> getEditUserId() {
        return editUserId;
    }

    public void setEditUserId(List<String> editUserId) {
        this.editUserId = editUserId;
    }

    public List<String> getViewGroupId() {
        return viewGroupId;
    }

    public void setViewGroupId(List<String> viewGroupId) {
        this.viewGroupId = viewGroupId;
    }

    public List<String> getEditGroupId() {
        return editGroupId;
    }

    public void setEditGroupId(List<String> editGroupId) {
        this.editGroupId = editGroupId;
    }

    public List<String> getContributors() {
        return contributors;
    }

    public void setContributors(List<String> contributors) {
        this.contributors = contributors;
    }
}
