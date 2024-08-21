package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.Assertions;
import lib.BaseTestCase;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class UserGetTest extends BaseTestCase {
    @Test
    public void testGetUserDataNotAuth(){
        //тест на просмотр деталей пользователя по id
        //если мы присылаем все поля подтверждающие факт авторизации запрашиваемого ползователя, мы увидим всю информацию
        //если мы присылаем не все - только логин

        Response responseUserData = RestAssured
                .get("https://playground.learnqa.ru/api/user/2")
                .andReturn();

//        System.out.println(responseUserData.asString());

// c помощью нового метода в assertions - assertJsonHasNotField нам необходимо зафиксировать, что мы получаем поле username, а так же, что не
// получаем других полей которые не должны получить

        Assertions.assertJsonHasField(responseUserData, "username");
        Assertions.assertJsonHasNotField(responseUserData, "firstName");
        Assertions.assertJsonHasNotField(responseUserData, "lastName");
        Assertions.assertJsonHasNotField(responseUserData, "email");
    }

    @Test
    public void testGetUserDetailsAuthSameUser(){
        Map<String, String> authData = new HashMap<>();
        authData.put("email","vinkotov@example.com");
        authData.put("password","123");

        Response responseGetAuth = RestAssured
                .given()
                .body(authData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();

        String header = this.getHeader(responseGetAuth,"x-csrf-token");
        String cookie = this.getCookie(responseGetAuth,"auth_sid");

        Response responseUserData = RestAssured
                .given()
                .header("x-csrf-token", header)
                .cookie("auth_sid", cookie)
                .get("https://playground.learnqa.ru/api/user/2")
                .andReturn();
//        Assertions.assertJsonHasField(responseUserData,"username");
//        Assertions.assertJsonHasField(responseUserData,"firstName");
//        Assertions.assertJsonHasField(responseUserData,"lastName");
//        Assertions.assertJsonHasField(responseUserData,"email");
//          Выше - валидный вариант, но можно заменить его другим ассертом - assertJsonHasFields, которая будет работать
//          аналогичным образом, только на вход будет получать сразу все ключи в наличии которых она должна будет убедиться

        String[] expectedFields = {"username","firstName", "lastName", "email"};
        Assertions.assertJsonHasFields(responseUserData, expectedFields);


    }

}
