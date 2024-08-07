import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;


public class RestAssuredTestTypeCheckRespCode404 {
    @Test
    public void testRestAssured(){
        Response response =  RestAssured
                .get("https://playground.learnqa.ru/api/something")
                .andReturn();

        int statusCode = response.getStatusCode();
        System.out.println(statusCode);

    }
}
