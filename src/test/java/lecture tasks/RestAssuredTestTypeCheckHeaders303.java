import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;


public class RestAssuredTestTypeCheckHeaders303 {
    @Test
    public void testRestAssured(){
        Map<String, String> headers = new HashMap<>();
        headers.put("MyHeader1","MyValue1");
        headers.put("MyHeader2","MyValue2");

        Response response =  RestAssured
                .given()
                .redirects()
                .follow(false)
                .get("https://playground.learnqa.ru/api/get_303")
                .andReturn();
        response.prettyPrint();
        //ответ придет в json формате, которые нам выведет prettyPrint
        //в ответе содержатся те заголовки, которые мы отправили с нашим запросом

        String locationHeader = response.getHeader("Location");
        System.out.println(locationHeader);
        //в ответе только 1 header - Location
    }
}
