package tests;

import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import lib.ApiCoreRequests;

import java.util.HashMap;
import java.util.Map;


public class UserEditTest extends BaseTestCase {

    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    // Тут мы будем проверять возможность по изменению данных пользователя. Нам надо будет создать пользователя, затем
    // отредактировать его данные, затем проверить корректность редактирования. Для этого нам нужно будет авторизоваться
    // созданным пользователем
    public void testEditJustCreatedTest(){

        //generate user
        Map<String, String> userData = DataGenerator.getRegistrationData();

        JsonPath responseCreateAuth = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .jsonPath();

        String userId = responseCreateAuth.getString("id");


        //login as created user

        Map<String, String> authData = new HashMap<>();
        authData.put("email",userData.get("email"));
        authData.put("password",userData.get("password"));

        Response responseGetAuth = RestAssured
                .given()
                .body(authData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();
//        String cookielist = responseGetAuth.getCookies().toString();
//        System.out.println(cookielist);


        //Edit
        String newName = "Changed Name";
        Map<String , String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response responseEditUser = RestAssured
                .given()
                .header("x-csrf-token", this.getHeader(responseGetAuth, "x-csrf-token"))
                .cookie("auth_sid", this.getCookie(responseGetAuth,"auth_sid"))
                .body(editData)
                .put("https://playground.learnqa.ru/api/user/" + userId)
                .andReturn();
        // закинули куки для авторизации, параметр который хотим изменить в боди, ссылке добавили id пользователя

        // GET

        Response responseUserData = RestAssured
                .given()
                .header("x-csrf-token", this.getHeader(responseGetAuth, "x-csrf-token"))
                .cookie("auth_sid", this.getCookie(responseGetAuth,"auth_sid"))
                .get("https://playground.learnqa.ru/api/user/" + userId)
                .andReturn();

//        System.out.println(responseUserData.asString());
        Assertions.assertJsonByName(responseUserData,"firstName", newName);


    }
    @Test
    @Description("Ex17 Test that attempts to edit as user#1's data without authorization")
    @DisplayName("Test negative edit user without auth")
    public void testEditUserWhileUnAuth(){
        Map<String, String> userData = DataGenerator.getRegistrationData();
        JsonPath responseCreateUser = (JsonPath) apiCoreRequests
                .makePostJsonPath("https://playground.learnqa.ru/api/user/",userData);

        String userId = responseCreateUser.getString("id");

        String newName = "Changed Name";
        Map<String , String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response responseEditUser = apiCoreRequests.
                makePutRequest("https://playground.learnqa.ru/api/user/" + userId,editData);

        Map<String , String> authData = new HashMap<>();
        authData.put("email",userData.get("email"));
        authData.put("password",userData.get("password"));

        Response LoginAsUser = apiCoreRequests.
                makePostRequest("https://playground.learnqa.ru/api/user/login/",authData);

        String header = this.getHeader(LoginAsUser, "x-csrf-token");
        String cookie = this.getCookie(LoginAsUser,"auth_sid");


        Response checkUserData = apiCoreRequests
                .makeGetRequest("https://playground.learnqa.ru/api/user/" + userId, header, cookie);

        Assertions.assertJsonByName(checkUserData,"firstName", userData.get("firstName"));
    }
    @Test
    @Description("Ex17 Test that attempts to edit as user#1's data as user#2")
    @DisplayName("Test negative edit user#1 as user#2")
    public void testEditUserAsOtherUser(){
        Map<String, String> userData1 = DataGenerator.getRegistrationData();
        JsonPath responseCreateUser1 = (JsonPath) apiCoreRequests
                .makePostJsonPath("https://playground.learnqa.ru/api/user/",userData1);

        String user1Id = responseCreateUser1.getString("id");

        Map<String, String> userData2 = DataGenerator.getRegistrationData();
        JsonPath responseCreateUser2 = (JsonPath) apiCoreRequests
                .makePostJsonPath("https://playground.learnqa.ru/api/user/",userData2);

        String user2Id = responseCreateUser2.getString("id");

        //login as user#1
        Map<String , String> authData1 = new HashMap<>();
        authData1.put("email",userData1.get("email"));
        authData1.put("password",userData1.get("password"));

        Map<String , String> authData2 = new HashMap<>();
        authData2.put("email",userData2.get("email"));
        authData2.put("password",userData2.get("password"));

        Response LoginAsUser = apiCoreRequests.
                makePostRequest("https://playground.learnqa.ru/api/user/login/",authData1);

        //save user#1 cookies for later
        String header = this.getHeader(LoginAsUser, "x-csrf-token");
        String cookie = this.getCookie(LoginAsUser,"auth_sid");

        String newName = "Changed Name";
        Map<String , String> editData = new HashMap<>();
        editData.put("firstName", newName);

        //as user#1 try to change the firstName of user2
        Response ChangeUser2Name = apiCoreRequests
                .makeAuthPutRequest("https://playground.learnqa.ru/api/user/" + user2Id,header,cookie,editData);

        //login as user#2

        Response LoginAsUser2 = apiCoreRequests.
                makePostRequest("https://playground.learnqa.ru/api/user/login/",authData2);

        //save user#2 cookies for later
        String header2 = this.getHeader(LoginAsUser2, "x-csrf-token");
        String cookie2 = this.getCookie(LoginAsUser2,"auth_sid");


        Response checkUser2Data = apiCoreRequests
                .makeGetRequest("https://playground.learnqa.ru/api/user/" + user2Id, header2, cookie2);

        Assertions.assertJsonByName(checkUser2Data,"firstName", userData2.get("firstName"));
    }

    @Test
    @Description("Ex17 Test that attempts assign a new no-@-email")
    @DisplayName("Test negative change to new no-@-email")
    public void testEditUserNoAtEmail(){
        Map<String, String> userData = DataGenerator.getRegistrationData();
        JsonPath responseCreateUser = (JsonPath) apiCoreRequests
                .makePostJsonPath("https://playground.learnqa.ru/api/user/",userData);

        String userId = responseCreateUser.getString("id");

        Map<String , String> authData = new HashMap<>();
        authData.put("email",userData.get("email"));
        authData.put("password",userData.get("password"));

        Response LoginAsUser = apiCoreRequests.
                makePostRequest("https://playground.learnqa.ru/api/user/login/",authData);

        String header = this.getHeader(LoginAsUser, "x-csrf-token");
        String cookie = this.getCookie(LoginAsUser,"auth_sid");

        String badEmail = "124124poprikoluexample.test";
        Map<String , String> editData = new HashMap<>();
        editData.put("email", badEmail);

        Response ChangeUserEmail = apiCoreRequests
                .makeAuthPutRequest("https://playground.learnqa.ru/api/user/" + userId,header,cookie,editData);

        Assertions.assertResponseCodeEquals(ChangeUserEmail,400);
        Assertions.assertResponseTextEquals(ChangeUserEmail,"{\"error\":\"Invalid email format\"}");
    }

    @Test
    @Description("Ex17 Test that attempts assign a new 1symbol firstName")
    @DisplayName("Test negative change to new 1-symbol-1stName")
    public void testEditUserShortFirstName(){
        Map<String, String> userData = DataGenerator.getRegistrationData();
        JsonPath responseCreateUser = (JsonPath) apiCoreRequests
                .makePostJsonPath("https://playground.learnqa.ru/api/user/",userData);

        String userId = responseCreateUser.getString("id");

        Map<String , String> authData = new HashMap<>();
        authData.put("email",userData.get("email"));
        authData.put("password",userData.get("password"));

        Response LoginAsUser = apiCoreRequests.
                makePostRequest("https://playground.learnqa.ru/api/user/login/",authData);

        String header = this.getHeader(LoginAsUser, "x-csrf-token");
        String cookie = this.getCookie(LoginAsUser,"auth_sid");

        String shortFName = "s";
        Map<String , String> editData = new HashMap<>();
        editData.put("firstName", shortFName);

        Response ChangeUserEmail = apiCoreRequests
                .makeAuthPutRequest("https://playground.learnqa.ru/api/user/" + userId,header,cookie,editData);

        Assertions.assertResponseCodeEquals(ChangeUserEmail,400);
        Assertions.assertResponseTextEquals(ChangeUserEmail,"{\"error\":\"The value for field `firstName` is too short\"}");








    }








}
