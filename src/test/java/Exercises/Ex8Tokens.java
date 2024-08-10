package Exercises;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;


public class Ex8Tokens {
    @Test
    public void testRestAssured() throws InterruptedException {
        String token = "";
        int time = 0;
        JsonPath response = RestAssured
            .get("https://playground.learnqa.ru/ajax/api/longtime_job")
            .jsonPath();
        time = response.get("seconds");
        token = response.get("token");
        response.prettyPrint();
        System.out.println(time);
        System.out.println(token);

        response = RestAssured
                .given()
                .queryParam("token",token)
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .jsonPath();
        response.prettyPrint();

        Thread.sleep(time*1000);

        response = RestAssured
                .given()
                .queryParam("token",token)
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .jsonPath();
        response.prettyPrint();

    }
}
