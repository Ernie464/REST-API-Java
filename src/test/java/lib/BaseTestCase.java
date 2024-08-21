package lib;

import io.restassured.http.Cookies;
import io.restassured.http.Headers;
import io.restassured.response.Response;

import java.util.Map;

import static org.hamcrest.Matchers.hasKey;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BaseTestCase {
    protected String getHeader(Response Response, String name){
        // передаем в этом методе заголовки
        Headers headers = Response.getHeaders();

        assertTrue(headers.hasHeaderWithName(name), "Response doesn't have a header with name " + name);
        // проверка на то что в поле пришло какое то значение
        return headers.getValue(name);
        // если значение есть то мы его возвращаем, если нет то тест упадет
    }
    protected String getCookie(Response Response, String name){
        // передаем в этом методе куки
        Map<String, String> cookies = Response.getCookies();

        assertTrue(cookies.containsKey(name), "Response doesn't have a cookie with name " + name);
        // проверка на то что в поле пришло какое то значение
        return cookies.get(name);
        // если значение есть то мы его возвращаем, если нет то тест упадет
    }
    protected int getIntFromJson (Response Response, String name){
        Response.then().assertThat().body("$", hasKey(name));
        // теперь необходимо убедиться, что в нашем json есть нужное поле, для этого мы напишем первую строку в которой
        // используем функцию hasKey
        // "$" - указывает на то что мы ищем этот ключ в корне нашего json
        return Response.jsonPath().getInt(name);
        // эта строка воспроизведется только если этот ключ в json действительно есть



    }

}
