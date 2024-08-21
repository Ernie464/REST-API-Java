package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class UserRegisterTest extends BaseTestCase {
    @Test
    //делаем тест на проверку поведения когда пытаемся создать аккаунт с уже зарегистрированными данными
    public void testCreateUserWithExistingEmail(){
        String email = "vinkotov@example.com";

        Map<String, String> userData = new HashMap<>();
        userData.put("email",email);
        userData = DataGenerator.getRegistrationData(userData);
        // в итоге имейл будет как выше, а остальные данные дефолтные
        Response responseCreateAuth = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .andReturn();

//        System.out.println(responseCreateAuth.asString());
//        System.out.println(responseCreateAuth.statusCode());
        Assertions.assertResponseCodeEquals(responseCreateAuth,400);
        Assertions.assertResponseTextEquals(responseCreateAuth,"Users with email '"+ email+ "' already exists");

    }
    @Test
    //делаем тест на проверку поведения когда пытаемся создать аккаунт с уже зарегистрированными данными
    public void testCreateUserSuccessfully(){
        String email = DataGenerator.getRandomEmail();
    // используем метод создания почт.ящика из DataGenerator
        Map<String, String> userData = DataGenerator.getRegistrationData();

        Response responseCreateAuth = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .andReturn();

//        System.out.println(responseCreateAuth.asString());
//        System.out.println(responseCreateAuth.statusCode());
        Assertions.assertResponseCodeEquals(responseCreateAuth,200);
        Assertions.assertJsonHasKey(responseCreateAuth,"id");
        // проверяем что у нас в ответе есть поле id


    }

}
