package Exercises;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class Ex7LongRedirect {
    @Test

    public void testRestAssured(){
        String LongLink = "https://playground.learnqa.ru/api/long_redirect";
        int RespCode = 0;
        while (LongLink != null) {
            Response response = RestAssured
                    .given()
                    .redirects()
                    .follow(false)
                    .get(LongLink)
                    .andReturn();
            RespCode = response.getStatusCode();
            if (RespCode == 200){
                break;
            };
            String FinalLink = response.getHeader("Location");
            System.out.println(FinalLink);
            LongLink = FinalLink;

        }
    }
}
