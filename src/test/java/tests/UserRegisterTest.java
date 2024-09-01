package tests;

import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

public class UserRegisterTest extends BaseTestCase {

    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();
    @Test
    //делаем тест на проверку поведения когда пытаемся создать аккаунт с уже зарегистрированными данными
    public void testCreateUserWithExistingEmail(){
        String email = "vinkotov@example.com";

        Map<String, String> userData = new HashMap<>();
        userData.put("email",email);
        userData = DataGenerator.getRegistrationData(userData);
        // в итоге имейл будет как выше, а остальные данные дефолтные

        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData);

/*      Response responseCreateAuth = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .andReturn();

        System.out.println(responseCreateAuth.asString());
        System.out.println(responseCreateAuth.statusCode());
*/

        Assertions.assertResponseCodeEquals(responseCreateAuth,400);
        Assertions.assertResponseTextEquals(responseCreateAuth,"Users with email '"+ email+ "' already exists");

    }
    @Test
    //делаем тест на проверку поведения когда пытаемся создать аккаунт с уже зарегистрированными данными
    public void testCreateUserSuccessfully(){
        String email = DataGenerator.getRandomEmail();
    // используем метод создания почт.ящика из DataGenerator
        Map<String, String> userData = DataGenerator.getRegistrationData();
        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/",userData);
/*        Response responseCreateAuth = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .andReturn();

        System.out.println(responseCreateAuth.asString());
        System.out.println(responseCreateAuth.statusCode());

 */
        Assertions.assertResponseCodeEquals(responseCreateAuth,200);
        Assertions.assertJsonHasKey(responseCreateAuth,"id");
        // проверяем что у нас в ответе есть поле id
    }
    @Test
    @Description("this test tries to register a user with incorrect email(no @)")
    @DisplayName("Test negative email(no @)")
    public void testRegisterUserWithNoAtEmail(){
        Map<String, String> userData = new HashMap<>();
        userData.put("email",DataGenerator.getNoAtEmail());
        userData.put("password", "123");
        userData.put("username","learnqa");
        userData.put("firstName","learnqa");
        userData.put("lastName","learnqa");
        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/",userData);
/*
        System.out.println(responseCreateAuth.asString());
        System.out.println(responseCreateAuth.statusCode());
*/
        Assertions.assertResponseCodeEquals(responseCreateAuth,400);
        Assertions.assertResponseTextEquals(responseCreateAuth,"Invalid email format");
    }

    @ParameterizedTest
    @ValueSource(strings = {"email", "password","username","firstName","lastName"})
    @Description("this test tries to register a user without one of the fields")
    @DisplayName("Test a field is missing")
    public void testRegisterWithoutOneField(String fieldValue){
        if(fieldValue.equals("email")){
            Map<String, String> userData = DataGenerator.getNoEmailRegistrationData();
            Response responseCreateAuth = apiCoreRequests
                    .makePostRequest("https://playground.learnqa.ru/api/user/",userData);
            Assertions.assertResponseCodeEquals(responseCreateAuth,400);
            Assertions.assertResponseTextEquals(responseCreateAuth,"The following required params are missed: " + fieldValue);
        } else if (fieldValue.equals("password")) {
            Map<String, String> userData = DataGenerator.getNoPasswordRegistrationData();
            Response responseCreateAuth = apiCoreRequests
                    .makePostRequest("https://playground.learnqa.ru/api/user/",userData);
            Assertions.assertResponseCodeEquals(responseCreateAuth,400);
            Assertions.assertResponseTextEquals(responseCreateAuth,"The following required params are missed: " + fieldValue);
        } else if (fieldValue.equals("username")) {
            Map<String, String> userData = DataGenerator.getNoUsernameRegistrationData();
            Response responseCreateAuth = apiCoreRequests
                    .makePostRequest("https://playground.learnqa.ru/api/user/",userData);
            Assertions.assertResponseCodeEquals(responseCreateAuth,400);
            Assertions.assertResponseTextEquals(responseCreateAuth,"The following required params are missed: " + fieldValue);
        } else if (fieldValue.equals("firstName")) {
            Map<String, String> userData = DataGenerator.getNoFirstnameRegistrationData();
            Response responseCreateAuth = apiCoreRequests
                    .makePostRequest("https://playground.learnqa.ru/api/user/",userData);
            Assertions.assertResponseCodeEquals(responseCreateAuth,400);
            Assertions.assertResponseTextEquals(responseCreateAuth,"The following required params are missed: " + fieldValue);
        } else if (fieldValue.equals("lastName")) {
            Map<String, String> userData = DataGenerator.getNoLastnameRegistrationData();
            Response responseCreateAuth = apiCoreRequests
                    .makePostRequest("https://playground.learnqa.ru/api/user/",userData);
            Assertions.assertResponseCodeEquals(responseCreateAuth,400);
            Assertions.assertResponseTextEquals(responseCreateAuth,"The following required params are missed: " + fieldValue);
        } else{
            throw new IllegalArgumentException("Every field value is filled");
        }
    }
    @Test
    @Description("this test tries to register a user with 1 symbol firstName")
    @DisplayName("Test negative 1 symbol firstName")
    public void testRegisterOneSymbolName(){
        Map<String, String> userData = new HashMap<>();
        userData.put("email",DataGenerator.getRandomEmail());
        userData.put("password", "123");
        userData.put("username","learnqa");
        userData.put("firstName",DataGenerator.getRandomFirstName(1));
        userData.put("lastName","learnqa");
        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/",userData);
        Assertions.assertResponseCodeEquals(responseCreateAuth,200);
        Assertions.assertJsonHasKey(responseCreateAuth,"id");
    }

    @Test
    @Description("this test tries to register a user with more than 250 symbols firstName")
    @DisplayName("Test negative >250 symbols firstName")
    public void testRegisterTooLongName(){
        Map<String, String> userData = new HashMap<>();
        userData.put("email",DataGenerator.getRandomEmail());
        userData.put("password", "123");
        userData.put("username","learnqa");
        userData.put("firstName",DataGenerator.getRandomFirstName(251));
        userData.put("lastName","learnqa");
        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/",userData);
        Assertions.assertResponseCodeEquals(responseCreateAuth,400);
        Assertions.assertResponseTextEquals(responseCreateAuth,"The value of 'firstName' field is too long");
    }



}
