package tests;

import io.qameta.allure.Description;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class UserDeleteTest extends BaseTestCase {
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    @Description("Ex18 Test that attempts to delete undeletable user")
    @DisplayName("Test delete undeletable user")
    public void testDeleteVitaliy() {
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response authUserN2 = apiCoreRequests.
                makePostRequest("https://playground.learnqa.ru/api_dev/user/login", authData);
        String header = this.getHeader(authUserN2, "x-csrf-token");
        String cookie = this.getCookie(authUserN2, "auth_sid");

        Response deleteUser = apiCoreRequests.
                makeDeleteRequest("https://playground.learnqa.ru/api_dev/user/2", header, cookie);

        Assertions.assertResponseCodeEquals(deleteUser, 400);
        Assertions.assertResponseTextEquals(deleteUser, "{\"error\":\"Please, do not delete test users with ID 1, 2, 3, 4 or 5.\"}");
    }



    @Test
    @Description("Ex18 Test that deletes a regular user")
    @DisplayName("Test positive delete regular user")
    public void testDeleteRegularUser(){
        Map<String, String> userData = DataGenerator.getRegistrationData();
        JsonPath responseCreateUser = apiCoreRequests
                .makePostJsonPath("https://playground.learnqa.ru/api_dev/user/",userData);

        String userId = responseCreateUser.getString("id");

        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response LoginAsUser = apiCoreRequests.
                makePostRequest("https://playground.learnqa.ru/api_dev/user/login/",authData);

        String header = this.getHeader(LoginAsUser,"x-csrf-token");
        String cookie = this.getCookie(LoginAsUser,"auth_sid");

        Response deleteUser = apiCoreRequests.
                makeDeleteRequest("https://playground.learnqa.ru/api_dev/user/"+userId, header, cookie);

        Response getDeletedUserData = apiCoreRequests.
                makeGetRequest("https://playground.learnqa.ru/api_dev/user/"+userId,header, cookie);

        Assertions.assertResponseTextEquals(getDeletedUserData,"User not found");

    }

    @Test
    @Description("Ex18 Test that deletes a user#1 as user#2")
    @DisplayName("Test negative delete other user")
    public void testDeleteOtherUser(){

        Map<String, String> userData1 = DataGenerator.getRegistrationData();
        JsonPath responseCreateUser1 = (JsonPath) apiCoreRequests
                .makePostJsonPath("https://playground.learnqa.ru/api_dev/user/",userData1);

        String user1Id = responseCreateUser1.getString("id");

        Map<String, String> userData2 = DataGenerator.getRegistrationData();
        JsonPath responseCreateUser2 = (JsonPath) apiCoreRequests
                .makePostJsonPath("https://playground.learnqa.ru/api_dev/user/",userData2);

        String user2Id = responseCreateUser2.getString("id");

        Map<String , String> authData1 = new HashMap<>();
        authData1.put("email",userData1.get("email"));
        authData1.put("password",userData1.get("password"));

        Map<String , String> authData2 = new HashMap<>();
        authData2.put("email",userData2.get("email"));
        authData2.put("password",userData2.get("password"));

        Response LoginAsUser = apiCoreRequests.
                makePostRequest("https://playground.learnqa.ru/api_dev/user/login/",authData1);

        //save user#1 cookies for later
        String header = this.getHeader(LoginAsUser, "x-csrf-token");
        String cookie = this.getCookie(LoginAsUser,"auth_sid");

        Response DeleteUser2 = apiCoreRequests.
                makeDeleteRequest("https://playground.learnqa.ru/api_dev/user/"+user2Id,header,cookie);

        Assertions.assertResponseCodeEquals(DeleteUser2,400);
        Assertions.assertJsonByName(DeleteUser2,"error","This user can only delete their own account.");
    }








}
