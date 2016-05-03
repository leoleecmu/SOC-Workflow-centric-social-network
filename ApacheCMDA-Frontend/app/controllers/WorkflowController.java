package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.metadata.WorkflowService;
import views.html.newWorkflow;

import play.mvc.*;
import util.APICall;
import util.APICall.ResponseType;
import views.html.*;

import play.data.*;
import play.libs.Json;
import play.*;
import static play.data.Form.*;

import models.*;
import models.metadata.UserService;
import org.apache.commons.codec.binary.Base64;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by Shuai Wang on 11/15/15.
 */
public class WorkflowController extends Controller {

    final static Form<WorkflowService> workflowServiceForm = Form
            .form(WorkflowService.class);

    final static Form<WorkflowService> editWorkflowForm = Form
            .form(WorkflowService.class);

    public static Result newWorkflow() {
        return ok(newWorkflow.render(workflowServiceForm, Friend.all(), Group.all()));
    }

    public static Result createNewWorkflow() {
        System.out.println("");

        Form<WorkflowService> dc = workflowServiceForm.bindFromRequest();
        ObjectNode jsonData = Json.newObject();

        try {

			String originalWorkflowServiceName = dc.field("Name").value();
			String newWorkflowServiceName = originalWorkflowServiceName.replace(' ', '-');

			if (newWorkflowServiceName != null && !newWorkflowServiceName.isEmpty()) {
				jsonData.put("name", newWorkflowServiceName);
			}

			jsonData.put("description", dc.field("Description").value());

//            Http.MultipartFormData dataBody = request().body().asMultipartFormData();
//            File imageFile = dataBody.getFile("Image_Preview").getFile();
//            byte[] imageBytes = Files.readAllBytes(imageFile.toPath());
//            jsonData.put("image", new String(Base64.encodeBase64(imageBytes)));

            jsonData.put("previewImage", dc.field("Url").value());
            jsonData.put("contributor", dc.field("Contributors").value());
            jsonData.put("tags", dc.field("Tags").value());
            jsonData.put("attributeWorkflow", dc.field("AttributeWorkflow").value());

            jsonData.put("tasks", dc.field("Tasks").value());
            jsonData.put("inputs", dc.field("Input").value());
            jsonData.put("outputs", dc.field("Output").value());

            jsonData.put("links", dc.field("Links").value());
            jsonData.put("instructions", dc.field("Instructions").value());
            jsonData.put("datasets", dc.field("Datasets").value());

//            jsonData.put("viewUserId", dc.filed("ViewUserID").value());
//            jsonData.put("editUserId", dc.filed("EditUserID").value());
//            jsonData.put("viewGroupId", dc.filed("ViewGroupID").value());
//            jsonData.put("editGroupId", dc.filed("EditGroupID").value());

			JsonNode response = WorkflowService.create(jsonData);
			Application.flashMsg(response);

		} catch (IllegalStateException e) {
			e.printStackTrace();
			Application.flashMsg(APICall
					.createResponse(ResponseType.CONVERSIONERROR));
		} catch (Exception e) {
			e.printStackTrace();
			Application.flashMsg(APICall.createResponse(ResponseType.UNKNOWN));
		}

		return redirect("/workflowHome/newWorkflow");
    }

    public static Result viewAllWorkflows() {
        List<WorkflowService> allWfServices = WorkflowService.all();

        // user access control
//        List<WorkflowService> accessWorkflows = new ArrayList<>();
//        for(WorkflowService wf: allWfServices) {
//            if(wf.getViewUserId() = session().get("currentUserId")) {
//                accessWorkflows.add(wf);
//            }
//        }
//        return ok(allWorkflows.render(accessWorkflows));

        return ok(allWorkflows.render(allWfServices));
    }

    public static Result downloadWorkflowService() {
        List<WorkflowService> user = WorkflowService.all();
        ObjectMapper mapper = new ObjectMapper();
        File file = new File("user.json");

        try {
            mapper.writeValue(file, user);
        } catch (IOException e) {
            e.printStackTrace();
        }

        response().setContentType("application/x-download");
        response().setHeader("Content-disposition",
                "attachment; filename=user.json");
        return ok(file);
    }


    public static Result oneWorkflowService(String id) {
        session("currentWFID", id);

        return ok(oneWorkflow.render(WorkflowService.getOneWorkflow(id)));
    }


    public static Result editWorkflow() {
        return ok(editOneWorkflow.render(session().get("currentWFID"), editWorkflowForm));
    }

    public static Result addTags() {
        Form<WorkflowService> dc = editWorkflowForm.bindFromRequest();
        ObjectNode jsonData = Json.newObject();

        try {
            jsonData.put("tags", dc.field("NewTag").value());
            jsonData.put("wfId", session().get("currentWFID"));
            JsonNode response = WorkflowService.addNewTag(jsonData);
        } catch (IllegalStateException e) {
            e.printStackTrace();
            Application.flashMsg(APICall
                    .createResponse(ResponseType.CONVERSIONERROR));
        } catch (Exception e) {
            e.printStackTrace();
            Application.flashMsg(APICall.createResponse(ResponseType.UNKNOWN));
        }

        return redirect(
                routes.WorkflowController.editWorkflow()
        );
    }

    public static Result addInputs() {
        Form<WorkflowService> dc = editWorkflowForm.bindFromRequest();
        ObjectNode jsonData = Json.newObject();

        try {
            jsonData.put("inputs", dc.field("NewInput").value());
            jsonData.put("wfId", session().get("currentWFID"));
            JsonNode response = WorkflowService.addNewInput(jsonData);
        } catch (IllegalStateException e) {
            e.printStackTrace();
            Application.flashMsg(APICall
                    .createResponse(ResponseType.CONVERSIONERROR));
        } catch (Exception e) {
            e.printStackTrace();
            Application.flashMsg(APICall.createResponse(ResponseType.UNKNOWN));
        }

        return redirect(
                routes.WorkflowController.editWorkflow()
        );
    }

    public static Result addOutputs() {
        Form<WorkflowService> dc = editWorkflowForm.bindFromRequest();
        ObjectNode jsonData = Json.newObject();

        try {
            jsonData.put("outputs", dc.field("NewOutput").value());
            jsonData.put("wfId", session().get("currentWFID"));
            JsonNode response = WorkflowService.addNewOutput(jsonData);
        } catch (IllegalStateException e) {
            e.printStackTrace();
            Application.flashMsg(APICall
                    .createResponse(ResponseType.CONVERSIONERROR));
        } catch (Exception e) {
            e.printStackTrace();
            Application.flashMsg(APICall.createResponse(ResponseType.UNKNOWN));
        }

        return redirect(
                routes.WorkflowController.editWorkflow()
        );
    }

    public static Result addTasks() {
        Form<WorkflowService> dc = editWorkflowForm.bindFromRequest();
        ObjectNode jsonData = Json.newObject();

        try {
            jsonData.put("tasks", dc.field("NewTask").value());
            jsonData.put("wfId", session().get("currentWFID"));
            JsonNode response = WorkflowService.addNewTask(jsonData);
        } catch (IllegalStateException e) {
            e.printStackTrace();
            Application.flashMsg(APICall
                    .createResponse(ResponseType.CONVERSIONERROR));
        } catch (Exception e) {
            e.printStackTrace();
            Application.flashMsg(APICall.createResponse(ResponseType.UNKNOWN));
        }

        return redirect(
                routes.WorkflowController.editWorkflow()
        );
    }

    public static Result addInstructions() {
        Form<WorkflowService> dc = editWorkflowForm.bindFromRequest();
        ObjectNode jsonData = Json.newObject();

        try {
            jsonData.put("instructions", dc.field("NewInstruction").value());
            jsonData.put("wfId", session().get("currentWFID"));
            JsonNode response = WorkflowService.addNewInstruction(jsonData);
        } catch (IllegalStateException e) {
            e.printStackTrace();
            Application.flashMsg(APICall
                    .createResponse(ResponseType.CONVERSIONERROR));
        } catch (Exception e) {
            e.printStackTrace();
            Application.flashMsg(APICall.createResponse(ResponseType.UNKNOWN));
        }

        return redirect(
                routes.WorkflowController.editWorkflow()
        );
    }

    public static Result addDatasets() {
        Form<WorkflowService> dc = editWorkflowForm.bindFromRequest();
        ObjectNode jsonData = Json.newObject();

        try {
            jsonData.put("datasets", dc.field("NewDataset").value());
            jsonData.put("wfId", session().get("currentWFID"));
            JsonNode response = WorkflowService.addNewDataset(jsonData);
        } catch (IllegalStateException e) {
            e.printStackTrace();
            Application.flashMsg(APICall
                    .createResponse(ResponseType.CONVERSIONERROR));
        } catch (Exception e) {
            e.printStackTrace();
            Application.flashMsg(APICall.createResponse(ResponseType.UNKNOWN));
        }

        return redirect(
                routes.WorkflowController.editWorkflow()
        );
    }

    public static Result addLinks() {
        Form<WorkflowService> dc = editWorkflowForm.bindFromRequest();
        ObjectNode jsonData = Json.newObject();

        try {
            jsonData.put("links", dc.field("NewLink").value());
            jsonData.put("wfId", session().get("currentWFID"));
            JsonNode response = WorkflowService.addNewLink(jsonData);
        } catch (IllegalStateException e) {
            e.printStackTrace();
            Application.flashMsg(APICall
                    .createResponse(ResponseType.CONVERSIONERROR));
        } catch (Exception e) {
            e.printStackTrace();
            Application.flashMsg(APICall.createResponse(ResponseType.UNKNOWN));
        }

        return redirect(
                routes.WorkflowController.editWorkflow()
        );
    }

    public static Result addAttributes() {
        Form<WorkflowService> dc = editWorkflowForm.bindFromRequest();
        ObjectNode jsonData = Json.newObject();

        try {
            jsonData.put("attributeWorkflow", dc.field("NewAttribute").value());
            jsonData.put("wfId", session().get("currentWFID"));
            JsonNode response = WorkflowService.addNewAttribute(jsonData);
        } catch (IllegalStateException e) {
            e.printStackTrace();
            Application.flashMsg(APICall
                    .createResponse(ResponseType.CONVERSIONERROR));
        } catch (Exception e) {
            e.printStackTrace();
            Application.flashMsg(APICall.createResponse(ResponseType.UNKNOWN));
        }

        return redirect(
                routes.WorkflowController.editWorkflow()
        );
    }

    public static Result addPosts() {
        Form<WorkflowService> dc = editWorkflowForm.bindFromRequest();
        ObjectNode jsonData = Json.newObject();

        try {
            jsonData.put("domain", "workflow");
            jsonData.put("domainName", dc.field("domainName").value());
            jsonData.put("title", dc.field("title").value());
            jsonData.put("content", dc.field("content").value());
            jsonData.put("userID", dc.field("userID").value());

            JsonNode response = WorkflowService.addNewPost(jsonData);
        } catch (IllegalStateException e) {
            e.printStackTrace();
            Application.flashMsg(APICall
                    .createResponse(ResponseType.CONVERSIONERROR));
        } catch (Exception e) {
            e.printStackTrace();
            Application.flashMsg(APICall.createResponse(ResponseType.UNKNOWN));
        }

        return redirect(
                routes.WorkflowController.editWorkflow()
        );
    }


}
