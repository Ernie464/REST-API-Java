package LectureTasksLesson3;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

// в коде достаточно много повторений
// как правило эти повторы убираются, за счет вынесения какой-либо логики во вспомогательные функции, которые используются в
// в разных тестах. Некоторые повторы мы вынесем в родительский класс чтобы переиспользовать их в тестах
// для этого мы создадим класс BaseTestCase, который будет расположен в отдельной директории - java/lib
// там будут находиться ключевые классы для наших тестов в нашем фреймворке
// продолжение в файле tests/UserAuthTest и lib/BaseTestCase

public class UserAuthTestBaseCase {
    String cookie;
    String header;
    int userIdOnAuth;
    // создали переменные вне классов тестов, которые будем подтягивать и заполнять через тег BeforeEach

    @BeforeEach
    //Код в рамках этого тега будет запускаться перед запуском каждого теста в то классе в котором они написаны
    //Прописываем логику с которой наши тесты будут начинаться:
    public void loginUser(){
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");
        Response responseGetAuth = RestAssured
                .given()
                .body(authData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();

        this.cookie = responseGetAuth.getCookie("auth_sid");
        this.header = responseGetAuth.getHeader("x-csrf-token");
        this.userIdOnAuth = responseGetAuth.jsonPath().getInt("user_id");
        //this.~~~ - указатель позволяющий делать переменную полем класса и как следствие дает возможность передавать
        // её значение в другие функции
        //Из тестов ниже мы вынесли отправку запроса на метод user/login а также наполнение переменных cookie и header
        //соответствующими значениями
    }

    @Test
    public void testAuthUser(){

        JsonPath responseCheckAuth = RestAssured
                .given()
                .header("x-csrf-token", this.header)
                .cookie("auth_sid", this.cookie )
                .get("https://playground.learnqa.ru/api/user/auth")
                .jsonPath();

        int userIdOnCheck = responseCheckAuth.getInt("user_id");
        assertTrue(userIdOnCheck > 0, "Unexpected user id"+ userIdOnCheck);

        assertEquals(
                userIdOnAuth,
                userIdOnCheck,
                "user id from auth request is not equal to check request"
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"cookie", "headers"})
    public void testAuthUserNeg(String condition){
        RequestSpecification spec = RestAssured.given();
        spec.baseUri("https://playground.learnqa.ru/api/user/auth");

        if (condition.equals("cookie")){
            spec.cookie("auth_sid", this.cookie);
        }else if (condition.equals("headers")) {
            spec.header("x-csrf-token", this.header);
        }else{
            throw new IllegalArgumentException("Condition value is known:" + condition);
        }
        JsonPath responseForCheck = spec.get().jsonPath();
        assertEquals(0, responseForCheck.getInt("user_id"), "user_id should be 0 for un-auth request");

    }
}
