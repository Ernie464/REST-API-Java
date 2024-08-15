package LectureTasksLesson3;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class jUnitAssertEquals {
    @Test
    public void testAssertEquals(){
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/map")
                .andReturn();
        System.out.println(response.statusCode());
        assertEquals( 200, response.statusCode(), "Unexpected Status code" );
//  assertEquals(то какой ответ ожидаем получить, то место откуда планируем брать ответ, сообщение об ошибке)


    }
}
