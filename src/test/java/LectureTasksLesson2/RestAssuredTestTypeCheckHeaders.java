package LectureTasksLesson2;

import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;


public class RestAssuredTestTypeCheckHeaders {
    @Test
    public void testRestAssured(){
        Map<String, String> headers = new HashMap<>();
        headers.put("MyHeader1","MyValue1");
        headers.put("MyHeader2","MyValue2");

        Response response =  RestAssured
                .given()
                .headers(headers)
                .when()
                .get("https://playground.learnqa.ru/api/show_all_headers")
                .andReturn();
        response.prettyPrint();
        //ответ придет в json формате, которые нам выведет prettyPrint
        //в ответе содержатся те заголовки, которые мы отправили с нашим запросом

        Headers repsonseHeaders = response.getHeaders();
        System.out.println(repsonseHeaders);

        //в ответе мы получим заголовки, которые придут с ответом

    }
}
