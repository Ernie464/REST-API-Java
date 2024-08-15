package LectureTasksLesson3;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class jUnitAssertEquals2 {
    @Test
    public void testAssertEquals() {
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/map")
                .andReturn();
        System.out.println(response.statusCode());
        assertEquals(200, response.statusCode(), "Unexpected Status code");


    }

    @Test
    public void testAssertEquals2_2() {
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/map23")
                .andReturn();
        System.out.println(response.statusCode());
        assertEquals(404, response.statusCode(), "Unexpected Status code");

    }
}
