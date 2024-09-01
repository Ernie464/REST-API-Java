package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lib.BaseTestCase;
import lib.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

//Импорты для работы allure
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;

import lib.ApiCoreRequests;

import java.util.HashMap;
import java.util.Map;


// в коде достаточно много повторений
// как правило эти повторы убираются, за счет вынесения какой-либо логики во вспомогательные функции, которые используются в
// в разных тестах. Некоторые повторы мы вынесем в родительский класс чтобы переиспользовать их в тестах
// для этого мы создадим класс BaseTestCase, который будет расположен в отдельной директории - java/lib
// там будут находиться ключевые классы для наших тестов в нашем фреймворке

@Epic("Authorization Cases")
// тег значит - все следующие кейсы принадлежат проверке одной большой единой общей части - функционала авторизации
@Feature("Authorization")
// тег описывает название фичи
// теги используются для группировки тест кейсов
public class UserAuthTest extends BaseTestCase {
    // обращаем внимание на то что в импортах есть lib.BaseTestCase
    // теперь мы в before методе этого класса можем добавить проверки из BaseTestCase на наличие нужных заголовков и кук
    // просто использовав методы из родительского класса BaseTestCase
    String cookie;
    String header;
    int userIdOnAuth;

    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @BeforeEach

    public void loginUser(){
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);


 /* Первоначальный вариант ниже
        Response responseGetAuth = RestAssured
                .given()
                .body(authData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();
*/
        this.cookie = this.getCookie(responseGetAuth, "auth_sid");
        // передали переменную из BaseTestCase getCookie и getAuth из запроса выше
        this.header = this.getHeader(responseGetAuth, "x-csrf-token");
        // передали переменную из BaseTestCase getHeader и getAuth из запроса выше
        // теперь нам не нужно каждый раз вызывать assertTrue для проверок
        this.userIdOnAuth = this.getIntFromJson(responseGetAuth,"user_id");



    }

    @Test
    @Description("This test successfully authorizes user by email and password")
    // тег значит чтобы было понятно что именно проверяет данны конкретный тест
    @DisplayName("Test positive auth user")
    // тег значит то, в каком виде мы хотели бы видть название кейса в нашем allure отчете
    public void testAuthUser(){

        Response responseCheckAuth = apiCoreRequests
                .makeGetRequest(
                        "https://playground.learnqa.ru/api/user/auth",
                        this.header,
                        this.cookie
                );

/*      Первоначальный вариант без привязки к @Step из ApiCoreRequests
        Response responseCheckAuth = RestAssured
                .given()
                .header("x-csrf-token", this.header)
                .cookie("auth_sid", this.cookie )
                .get("https://playground.learnqa.ru/api/user/auth")
                .andReturn();

 */
        // раньше это был объет jsonPath
        // т.к. мы не работаем с json'ом напрямую в коде теста и спрятали всю работу во вспомогательные методы нам
        // необходимо получать в качестве ответа объект response и передавать его нашим методам, а парсить json уже внутри
        Assertions.assertJsonByName(responseCheckAuth,"user_id", this.userIdOnAuth);

//        раньше здесь был вот этот большой ассерт, который мы заменили функцией из lib\assertions
//        int userIdOnCheck = responseCheckAuth.getInt("user_id");
//        assertTrue(userIdOnCheck > 0, "Unexpected user id"+ userIdOnCheck);
//
//        assertEquals(
//                userIdOnAuth,
//                userIdOnCheck,
//                "user id from auth request is not equal to check request"
//        );
    }

    @Description("This test checks authorization status without sending auth cookie or token")
    @DisplayName("Test negative auth user")

    @ParameterizedTest
    @ValueSource(strings = {"cookie", "headers"})
    public void testAuthUserNeg(String condition){
        RequestSpecification spec = RestAssured.given();
        spec.baseUri("https://playground.learnqa.ru/api/user/auth");

        if (condition.equals("cookie")){
            Response responseForCheck = apiCoreRequests.makeGetRequestWithCookie(
                    "https://playground.learnqa.ru/api/user/auth",
                    this.cookie
            );
            Assertions.assertJsonByName(responseForCheck, "user_id", 0);
        }else if(condition.equals("headers")){
            Response responseForCheck = apiCoreRequests.makeGetRequestWithToken(
                    "https://playground.learnqa.ru/api/user/auth",
                    this.header
            );
            Assertions.assertJsonByName(responseForCheck, "user_id", 0);
        }else{
            throw new IllegalArgumentException("Condition value is known:" + condition);
        }





/*  Первоначальный вариант ниже
        if (condition.equals("cookie")){
            spec.cookie("auth_sid", this.cookie);
        }else if (condition.equals("headers")) {
            spec.header("x-csrf-token", this.header);
        }else{
            throw new IllegalArgumentException("Condition value is known:" + condition);
        }


        Response responseForCheck = spec.get().andReturn();
        // раньше это был объет jsonPath
        // т.к. мы не работаем с json'ом напрямую в коде теста и спрятали всю работу во вспомогательные методы нам
        // необходимо получать в качестве ответа объект response и передавать его нашим методам, а парсить json уже внутри

        Assertions.assertJsonByName(responseForCheck,"user_id",0);
//        раньше здесь был вот этот большой ассерт, который мы заменили функцией из lib\assertions
//        assertEquals(0, responseForCheck.getInt("user_id"), "user_id should be 0 for un-auth request");
 */
    }
}
