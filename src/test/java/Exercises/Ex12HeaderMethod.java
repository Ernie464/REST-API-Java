package Exercises;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Ex12HeaderMethod {
    @Test
    public void HeaderExtraction(){

        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/homework_header")

                .andReturn();
        Object restHeaders = response.getHeaders();
//        System.out.println(restHeaders);
        String susHeader = response.getHeader("x-secret-homework-header");
        String stubb = "stub header";
        assertEquals(stubb,susHeader,"We received following x-secret-homework-header ");





    }
}
