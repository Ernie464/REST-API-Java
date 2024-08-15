package LectureTasksLesson2;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;


public class RestAssuredTestTypeCheckRespCode {
    @Test
    public void testRestAssured(){
        Response response =  RestAssured
                .get("https://playground.learnqa.ru/api/check_type")
                .andReturn();

        int statusCode = response.getStatusCode();
        System.out.println(statusCode);

    }
}
