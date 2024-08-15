package LectureTasksLesson3;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class jUnitAssertTrue {
    @Test
    public void testAssertTrue(){
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/map")
                .andReturn();
        System.out.println(response.statusCode());
        assertTrue(response.statusCode() == 222, "Unexpected Status code" );

    }
}
