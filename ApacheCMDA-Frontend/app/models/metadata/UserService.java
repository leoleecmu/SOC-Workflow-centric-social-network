package models.metadata;

import com.fasterxml.jackson.databind.JsonNode;

import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;
import util.APICall;
import util.Constants;

public class UserService {

	private static final String VERIFY_USER_CALL = Constants.NEW_BACKEND+"user/login";
	private static final String VERIFY_USER_SINGUP = Constants.NEW_BACKEND+"user/addUser";

//	private static final String GET_ALL_USER_CALL = Constants.NEW_BACKEND+"userService/getAllUserServices/json";

	private static final String GET_USERID_BY_EMAIL = Constants.NEW_BACKEND+"user/getUserIdByEmail";
	private static final String GET_USER_BY_ID = Constants.NEW_BACKEND+"user/getUser";
	private static final String GET_ALL_USER = Constants.NEW_BACKEND+"user/getAllUsers";
	private static final String GET_USER_FRIENDS = Constants.NEW_BACKEND+"user/getUser";
	private static final String ADD_FRIEND = Constants.NEW_BACKEND+"user/addFriendToUser";
	private static final String DELETE_FRIEND = Constants.NEW_BACKEND+"user/deleteFriendFromUser";
	private static final String ADD_SUBSCRIBE = Constants.NEW_BACKEND+"user/addSubscribeUserToUser";
	private static final String DELETE_SUBSCRIBE = Constants.NEW_BACKEND+"user/deleteSubscribeUserFromUser";
	private static final String ADD_GROUP = Constants.NEW_BACKEND+"group/addGroup";
	private static final String ADD_GROUP_ADMIN = Constants.NEW_BACKEND+"user/addUserToGroupAsAdmin";
	private static final String GET_GROUP = Constants.NEW_BACKEND+"group/getGroup";
	private static final String DELETE_GROUP = Constants.NEW_BACKEND+"group/deleteGroup";
	private static final String GET_GROUP_ID_BY_NAME = Constants.NEW_BACKEND+"group/getGroupIdByName";
	private static final String ADD_USER_TO_GROUP = Constants.NEW_BACKEND+"user/addUserToGroupAsMember";
	private static final String DELETE_USER_FROM_GROUP = Constants.NEW_BACKEND+"user/deleteUserFromGroup";
	private static final String GET_USER_SUBSCRIBE_NUMBER = Constants.NEW_BACKEND+"user/getSubscribersNumber";
	private static final String GET_USER_COLLABORATOR_NUMBER = Constants.NEW_BACKEND+"user/getCollaboratorsNumber";



	public static JsonNode verifyUserAuthentity(JsonNode jsonData) {
		System.out.println(APICall.postAPI(VERIFY_USER_CALL, jsonData));
		return APICall.postAPI(VERIFY_USER_CALL, jsonData);
	}

	public static JsonNode verifyUserSUAuthentity(JsonNode jsonData) {
		return APICall.postAPI(VERIFY_USER_SINGUP, jsonData);
	}

	public static JsonNode getUserByEmail(JsonNode jsonData) {
		JsonNode idResponse = APICall.postAPI(GET_USERID_BY_EMAIL, jsonData);
		String id = idResponse.path("userId").asText();

		ObjectNode userData = Json.newObject();
		userData.put("userId", id);

		return APICall.postAPI(GET_USER_BY_ID, userData);
	}

	public static String getSubscribersNumber(String userId) {

		ObjectNode userData = Json.newObject();
		userData.put("userId", userId);
		JsonNode result = APICall.postAPI(GET_USER_SUBSCRIBE_NUMBER, userData);
		return result.get("subscribersNumber").asText();
	}

	public static String getCollaboratorsNumber(String userId) {

		ObjectNode userData = Json.newObject();
		userData.put("userId", userId);
		JsonNode result = APICall.postAPI(GET_USER_COLLABORATOR_NUMBER, userData);
		return result.get("collaboratorsNumber").asText();
	}

	public static JsonNode getAllUsers() {
		ObjectNode userData = Json.newObject();
		userData.put("userId", "null");
		return APICall.postAPI(GET_ALL_USER, userData);
	}

	public static JsonNode getAllFriends(String userID) {
		ObjectNode userData = Json.newObject();
		userData.put("userId", userID);
		return APICall.postAPI(GET_USER_FRIENDS, userData);
	}

	public static JsonNode addFriend(String userId, String friendID) {
		ObjectNode userData = Json.newObject();
		userData.put("userId", userId);
		userData.put("friendId", friendID);
		return APICall.postAPI(ADD_FRIEND, userData);
	}

	public static JsonNode deleteFriend(String userId, String friendID) {
		ObjectNode userData = Json.newObject();
		userData.put("userId", userId);
		userData.put("friendId", friendID);
		return APICall.postAPI(DELETE_FRIEND, userData);
	}

	public static JsonNode addSubscribe(String userId, String subscribeUserId) {
		ObjectNode userData = Json.newObject();
		userData.put("userId", userId);
		userData.put("subscribeUserId", subscribeUserId);
		return APICall.postAPI(ADD_SUBSCRIBE, userData);
	}

	public static JsonNode deleteSubscribe(String userId, String subscribeUserId) {
		ObjectNode userData = Json.newObject();
		userData.put("userId", userId);
		userData.put("subscribeUserId", subscribeUserId);
		return APICall.postAPI(DELETE_SUBSCRIBE, userData);
	}

	public static JsonNode addGroup(String userId, String groupName, String isPublic) {
		ObjectNode userData = Json.newObject();
		userData.put("creatorId", userId);
		userData.put("name", groupName);
		if(isPublic.equals("public")) {
			userData.put("isPublic", "true");
		} else {
			userData.put("isPublic", "false");
		}
		JsonNode response = APICall.postAPI(ADD_GROUP, userData);
		String groupID = response.get("groupId").asText();
		ObjectNode userData1 = Json.newObject();
		userData1.put("userId", userId);
		userData1.put("groupId", groupID);
		return APICall.postAPI(ADD_GROUP_ADMIN, userData1);
	}

	public static JsonNode getAllGroups(String userID) {
		ObjectNode userData = Json.newObject();
		userData.put("userId", userID);
		return APICall.postAPI(GET_USER_FRIENDS, userData);
	}

	public static JsonNode getGroupInfo(String groupId) {
		ObjectNode userData = Json.newObject();
		userData.put("groupId", groupId);
		return APICall.postAPI(GET_GROUP, userData);
	}

	public static JsonNode deleteGroup(String groupId) {
		ObjectNode userData = Json.newObject();
		userData.put("groupId", groupId);
		return APICall.postAPI(DELETE_GROUP, userData);
	}

	public static JsonNode addUserToGroup(String userId, String groupName) {
		ObjectNode userData = Json.newObject();
		userData.put("name", groupName);

		JsonNode response = APICall.postAPI(GET_GROUP_ID_BY_NAME, userData);

		String groupId = response.get("groupId").asText();

		ObjectNode data = Json.newObject();
		data.put("userId", userId);
		data.put("groupId", groupId);

		return APICall.postAPI(ADD_USER_TO_GROUP, data);
	}

	public static JsonNode deleteUserFromGroup(String userId, String groupName) {
		ObjectNode userData = Json.newObject();
		userData.put("name", groupName);

		JsonNode response = APICall.postAPI(GET_GROUP_ID_BY_NAME, userData);

		String groupId = response.get("groupId").asText();

		ObjectNode data = Json.newObject();
		data.put("userId", userId);
		data.put("groupId", groupId);

		return APICall.postAPI(DELETE_USER_FROM_GROUP, data);
	}

//	public User getUserByEmail(String email) {
//		
//		List<User> allList = getAll();
//		for (User element : allList) {
//			String elementEmail = element.getEmail();
//			if (elementEmail.equals(email))
//				return element;
//		}
//		return null;
//	}
//	
//	public static List<User> getAll() {
//
//		List<User> allUsers = new ArrayList<User>();
//
//		JsonNode climateServicesNode = APICall
//				.callAPI(GET_ALL_USER_CALL);
//
//		if (climateServicesNode == null || climateServicesNode.has("error")
//				|| !climateServicesNode.isArray()) {
//			return climateServices;
//		}
//
//		for (int i = 0; i < climateServicesNode.size(); i++) {
//			JsonNode json = climateServicesNode.path(i);
//			ClimateService newService = new ClimateService();
//			newService.setId(json.path("id").asText());
//			newService.setClimateServiceName(json.get(
//					"name").asText());
//			newService.setPurpose(json.path("purpose").asText());
//			newService.setUrl(json.path("url").asText());
//			//newService.setCreateTime(json.path("createTime").asText());
//			newService.setScenario(json.path("scenario").asText());
//			newService.setVersion(json.path("versionNo").asText());
//			newService.setRootservice(json.path("rootServiceId").asText());
//			climateServices.add(newService);
//		}
//		return climateServices;
//	}

}
