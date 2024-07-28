import io.restassured.RestAssured;
import io.restassured.response.Response;
//импортируем нужные нам библиотеки
import org.junit.jupiter.api.Test;



public class HelloWorldTest {
    @Test
    public void testHelloWorld(){
       Response response = RestAssured
               .get("https://playground.learnqa.ru/api/hello")
               //делаем гет запрос на адрес нашего API
               .andReturn();
       //верни нам результат запроса
        //В переменной reponse будет храниться информация об ответе на запрос
       response.prettyPrint();
       //печатаем текст ответа в удобном формате спец командой
    }
}
