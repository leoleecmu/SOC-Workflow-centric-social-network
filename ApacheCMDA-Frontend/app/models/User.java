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
package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.databind.JsonNode;

import models.metadata.ClimateService;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.*;

import models.metadata.UserService;
import play.data.validation.Constraints;
import util.APICall;
import util.Constants;

@Entity
public class User {

//	@Id
//	@GeneratedValue(strategy = GenerationType.AUTO)
//	private long id;

	private String userId;

	@Id
	private String userName;
	@Constraints.Required
	public String email;
	@Constraints.Required
	public String password;

	@Constraints.Required
	private String firstName;
	@Constraints.Required
	private String lastName;
	private String middleInitial;

	private String affiliation;
	private String title;
	@Constraints.Required
	private String mailingAddress;
	private String phoneNumber;
	private String faxNumber;
	private String researchFields;
	private String highestDegree;

	private String subscribersNumber;
	private String collaboratorsNumber;

	public User() {

	}

	public User(String email, String password) {
		this.email = email;
		this.password = password;
	}

	public User(String userName, String password, String firstName,
				String lastName, String middleInitial, String affiliation,
				String title, String email, String mailingAddress,
				String phoneNumber, String faxNumber, String researchFields,
				String highestDegree) {

		super();
		this.userName = userName;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.middleInitial = middleInitial;
		this.affiliation = affiliation;
		this.title = title;
		this.email = email;
		this.mailingAddress = mailingAddress;
		this.phoneNumber = phoneNumber;
		this.faxNumber = faxNumber;
		this.researchFields = researchFields;
		this.highestDegree = highestDegree;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getMiddleInitial() {
		return middleInitial;
	}

	public String getAffiliation() {
		return affiliation;
	}

	public String getTitle() {
		return title;
	}

	public String getMailingAddress() {
		return mailingAddress;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public String getFaxNumber() {
		return faxNumber;
	}

	public String getResearchFields() {
		return researchFields;
	}

	public String getHighestDegree() {
		return highestDegree;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setMiddleInitial(String middleInitial) {
		this.middleInitial = middleInitial;
	}

	public void setAffiliation(String affiliation) {
		this.affiliation = affiliation;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setMailingAddress(String mailingAddress) {
		this.mailingAddress = mailingAddress;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void setFaxNumber(String faxNumber) {
		this.faxNumber = faxNumber;
	}

	public void setResearchFields(String researchFields) {
		this.researchFields = researchFields;
	}

	public void setHighestDegree(String highestDegree) {
		this.highestDegree = highestDegree;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getSubscribersNumber() {
		return subscribersNumber;
	}

	public void setSubscribersNumber(String subscribersNumber) {
		this.subscribersNumber = subscribersNumber;
	}

	public String getCollaboratorsNumber() {
		return collaboratorsNumber;
	}

	public void setCollaboratorsNumber(String collaboratorsNumber) {
		this.collaboratorsNumber = collaboratorsNumber;
	}

	static public ArrayList<User> getAllUser() {

		JsonNode allusers = UserService.getAllUsers();

		ArrayList<User> users = new ArrayList<User>();

		Iterator<JsonNode> it = allusers.get("allUsers").iterator() ;

		while(it.hasNext()){
			User user1 = new User();
			JsonNode now = it.next();
			user1.userName = now.get("userName").asText();
			user1.userId = now.get("userId").asText();
			users.add(user1);
		}
		return users;
	}
}

