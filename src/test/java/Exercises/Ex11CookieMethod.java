package Exercises;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Ex11CookieMethod {
    @Test
        public void CookieExtraction()  {

            Response response = RestAssured
                    .get("https://playground.learnqa.ru/api/homework_cookie")
                    .andReturn();
            Map<String, String> qookies = response.getCookies();
//            System.out.println(qookies);
            Map<String, String> Mockies = new HashMap<>();
            Mockies.put("TestName","TestCookie");
            assertEquals(Mockies,qookies,"We received following cookies");
    }
}
