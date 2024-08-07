package Exercises;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;


public class Ex5ParsingObject {
    @Test
    public void testRestAssured(){
        JsonPath response = RestAssured
                .given()
                .get("https://playground.learnqa.ru/api/get_json_homework")
                .jsonPath();
        response.prettyPrint();

        Object answer = response.get("messages");

        System.out.println(answer);
    }
}