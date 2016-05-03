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

package controllers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map.Entry;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import controllers.Application;
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

/**
 * Created by Shuai Wang on 11/10/15.
 */
public class Application extends Controller {
	final static Form<Login> loginForm = Form.form(Login.class);
	final static Form<User> userForm = Form.form(User.class);
	private static User user;

	//1st function: login
	public static Result login() {
		return ok(login.render(loginForm));
	}
	public static class Login {

		public String email;
		public String password;

		public Login(){}

		public Login(String email,String password){
			this.email=email;
			this.password=password;
		}
		public String getEmail(){
			return email;
		}
		public void setEmail(String email){
			this.email=email;
		}
		public String getPassword(){
			return password;
		}
		public void setPassword(String password){
			this.password=password;
		}
	}

	public static Result authenticate() {
		Form<Login> lg = loginForm.bindFromRequest();
		ObjectNode jsonData = Json.newObject();
		try{
			jsonData.put("email", lg.get().getEmail());
			jsonData.put("password", lg.get().getPassword());

			JsonNode response =	UserService.verifyUserAuthentity(jsonData);
			if (response.get("success") == null) {
				System.out.println(">>input is invalid...");
				return badRequest(login.render(loginForm));
			}
		}catch(Exception e){
			e.printStackTrace();
		}

		session().clear();
		session("email", lg.get().getEmail());
		session("password", lg.get().getPassword());

		return redirect(
				routes.Application.workflowHome()
		);
	}

	//2nd function: signup
	public static Result signup() {
		return ok(signup.render(userForm));
	}

	public static Result createNewUser(){
		Form<User> nu = userForm.bindFromRequest();

		ObjectNode jsonData = Json.newObject();
		String userName = null;

		try{
			userName = nu.field("firstName").value()+" "+(nu.field("middleInitial")).value()
					+" "+(nu.field("lastName")).value();

			jsonData.put("userName", userName);
			jsonData.put("firstName", nu.get().getFirstName());
			jsonData.put("middleInitial", nu.get().getMiddleInitial());
			jsonData.put("lastName", nu.get().getLastName());
			jsonData.put("password", nu.get().getPassword());
			jsonData.put("affiliation", nu.get().getAffiliation());
			jsonData.put("title", nu.get().getTitle());
			jsonData.put("email", nu.get().getEmail());
			jsonData.put("mailingAddress", nu.get().getMailingAddress());
			jsonData.put("phoneNumber", nu.get().getPhoneNumber());
			jsonData.put("faxNumber", nu.get().getFaxNumber());
			jsonData.put("researchFields", nu.get().getResearchFields());
			jsonData.put("highestDegree", nu.get().getHighestDegree());

			JsonNode response = UserService.verifyUserSUAuthentity(jsonData);

			System.out.println(">>>>id: " + response.path("userId").asText());

			return redirect(routes.Application.createSuccess());

		}catch (IllegalStateException e) {
			e.printStackTrace();
			Application.flashMsg(APICall
					.createResponse(ResponseType.CONVERSIONERROR));
		} catch (Exception e) {
			e.printStackTrace();
			Application.flashMsg(APICall
					.createResponse(ResponseType.UNKNOWN));
		}
		return ok(signup.render(nu));
	}

	public static Result createSuccess(){
		return ok(createSuccess.render());
	}

	//3rd function: redirect to workflow home page
	public static Result workflowHome() {
		if(session().isEmpty()) {
			System.out.println(">>session is empty");
			return ok(login.render(loginForm));
		}
		user = new User(session().get("email"), session().get("password"));

		System.out.println(">>success");
		//get all user information from backend

		ObjectNode jsonData = Json.newObject();
		jsonData.put("email", session().get("email"));
		JsonNode response = UserService.getUserByEmail(jsonData);

		Application.sessionMsg(response);

		//supplement user information
		user.setUserId(response.path("userId").asText());
		user.setUserName(response.path("userName").asText());
		user.setTitle(response.path("title").asText());
		user.setResearchFields(response.path("researchFields").asText());
		user.setMailingAddress(response.path("mailingAddress").asText());

		user.setAffiliation(response.path("affiliation").asText());
		user.setFaxNumber(response.path("faxNumber").asText());
		user.setPhoneNumber(response.path("phoneNumber").asText());
		user.setHighestDegree(response.path("highestDegree").asText());

		session("currentUserId", user.getUserId());

		user.setSubscribersNumber(UserService.getSubscribersNumber(response.path("userId").asText()));
		user.setCollaboratorsNumber(UserService.getCollaboratorsNumber(response.path("userId").asText()));

		return ok(workflow_home.render(user, Post.all(), Friend.all(), Friend.allSubscribe(), Group.all()));
	}


	public static void sessionMsg(JsonNode jsonNode) {
		session().clear();
		Iterator<Entry<String, JsonNode>> it = jsonNode.fields();
		while (it.hasNext()) {
			Entry<String, JsonNode> field = it.next();
			session(field.getKey(), field.getValue().asText());
		}
	}


	public static Result index() {
		return ok(index.render(""));
	}

	public static void flashMsg(JsonNode jsonNode) {
		Iterator<Entry<String, JsonNode>> it = jsonNode.fields();
		while (it.hasNext()) {
			Entry<String, JsonNode> field = it.next();
			flash(field.getKey(), field.getValue().asText());
		}
	}
}
