import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;


public class RestAssuredTestTypeCheckRespCode303 {
    @Test
    public void testRestAssured(){
        Response response =  RestAssured
                .given()
                .redirects()
                .follow(true)
                //метод follow  отвечает за то, делаем ли мы переход по адресу указанному в заголовке в случае редиректа или нет
                // true - делаем
                // false - не делаем, даем ответ от перехода на изначальный урл
                .get("https://playground.learnqa.ru/api/get_303")
                .andReturn();

        int statusCode = response.getStatusCode();
        System.out.println(statusCode);

    }
}
