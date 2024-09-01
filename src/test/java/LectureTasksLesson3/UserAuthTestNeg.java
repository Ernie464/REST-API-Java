package LectureTasksLesson3;

import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserAuthTestNeg {
    @Test
    //будем проверять авторизацию, а именно, то что передали верные email и пароль,
    //то что после авторизации получили нужные данные cookie  auth_sid, header x-csrf-token
    public void testAuthUser(){
        Map<String, String> authData = new HashMap<>();
        //Создаем HashMap для хранения логина и пароля для проверки аутентификации
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");
        //указываем почту+пароль с корректными данными нужными для аутентификации

        Response responseGetAuth = RestAssured
                .given()
                .body(authData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();
        //делаем запрос на урл для авторизации, с указанием данными логина

        Map<String, String> cookies = responseGetAuth.getCookies();
        Headers headers = responseGetAuth.getHeaders();
        int userIdOnAuth = responseGetAuth.jsonPath().getInt("user_id");
        //из строки полученной в результате ответа на запрос - responseGetAuth мы вытаскиваем три объекта, которые нам необходимо проверить
        //во-первых, выкачиваем куки
        //во-вторых, выкачиваем заголовки
        //в-третьих, выкачиваем из jsonPath инфу под ключем "user_id"
        //теперь мы сможем использовать их для наших тестов

        assertEquals(200, responseGetAuth.statusCode(),"Unexpected status code");
        //делаем проверку того что статус-код корректный и равен 200
        assertTrue(cookies.containsKey("auth_sid"), "Response doesn't have 'auth_sid' cookie");
        //делаем проверку того что в полученных куках есть кука auth_sid
        assertTrue(headers.hasHeaderWithName("x-csrf-token"), "Response doesn't have 'x-csrf-token' headers");
        //делаем проверку, что в хедерах есть хедер с названием x-csrf-token
        assertTrue(responseGetAuth.jsonPath().getInt("user_id") > 0, "user_id should be greater than 0");
        //делаем проверку что нам в  user_id пришел не 0

        //это был первый запрос, где мы делаем запрос для аутентификации
        //теперь сделаем запрос для проверки того, что мы действительно залогинились

        JsonPath responseCheckAuth = RestAssured
                .given()
                .header("x-csrf-token", responseGetAuth.getHeader("x-csrf-token") )
                //указываем какой хедер нам нужно применить при отправке запроса и то откуда его получаем
                .cookie("auth_sid", responseGetAuth.getCookie("auth_sid"))
                //аналогично header получаем нужные нам куки для запроса
                .get("https://playground.learnqa.ru/api/user/auth")
                .jsonPath();

        int userIdOnCheck = responseCheckAuth.getInt("user_id");
        assertTrue(userIdOnCheck > 0, "Unexpected user id"+ userIdOnCheck);
        //проверяем что user_id не равен нулю

        assertEquals(
                userIdOnAuth,
                userIdOnCheck,
                "user id from auth request is not equal to check request"
        );

    }

    // сделаем негативный тест где будем передавать только что то одно - либо куки, либо токен
    // сервис не должен считать нас авторизованными и должен вернуть user_id = 0
    // будет параметризованный тест для каждого из кейсов
    @ParameterizedTest
    @ValueSource(strings = {"cookie", "headers"})
    public void testAuthUserNeg(String condition){
        Map<String,String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");
        Response responseGetAuth = RestAssured
                .given()
                .body(authData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();
        Map<String, String> cookies = responseGetAuth.getCookies();
        Headers headers = responseGetAuth.getHeaders();
        // копирнули запрос из которого вытаскиваем куки и хедеры

        // теперь создаем новый тип сущности  RequestSpecification - специальный классб объектом которого будет наша переменная spec
        // c помощью этого механизма мы сможем выполнять нужный нам запрос объявляя его по частям

        RequestSpecification spec = RestAssured.given();
        // мы объявили переменную spec, теперь нам надо вызывая её, обогатить наш запрос данными
        spec.baseUri("https://playground.learnqa.ru/api/user/auth");
        // сначала мы указываем урл на который делаем запрос

        if (condition.equals("cookie")){
            spec.cookie("auth_sid", cookies.get("auth_sid"));
            // если переменная condition = cookie, то подставляем в spec только cookie
        }else if (condition.equals("headers")) {
            spec.header("x-csrf-token", headers.get("x-csrf-token"));
            // если переменная condition = headers, то подставляем в spec только header
        }else{
            throw new IllegalArgumentException("Condition value is known:" + condition);
            //делаем исключение на случай опечатки, и указываем что за ломаный condition был указан
        }
        JsonPath responseForCheck = spec.get().jsonPath();
        // т.к. в spec мы уже положили нужный урл мы его не указываем, и сразу же в этой строке парсим получаемый ответ
        // так же кроме url'a туда попадают и указанные нами параметры
        assertEquals(0, responseForCheck.getInt("user_id"), "user_id should be 0 for un-auth request");
        // ну и собственно наша проверка где мы сверяемся - т.к. аутентификация не прошла user_id должен быть равен 0

    }
}
