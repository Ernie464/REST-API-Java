import io.restassured.RestAssured;
import io.restassured.response.Response;
//импортируем нужные нам библиотеки
import org.junit.jupiter.api.Test;
//импортируем атрибут тест, который имеется в библиотеке jUnit


public class HelloWorldTest {
    //создаем класс HelloWorldTest
    @Test
            //тег ставится, чтобы обозначить следующий за ним код как тест
    public void testHelloWorld(){
       Response response = RestAssured
               .get("https://playground.learnqa.ru/api/hello")
               //делаем гет запрос на адрес нашего API
               .andReturn();
       //верни нам результат запроса
        //В переменной response будет храниться информация об ответе на запрос
       response.prettyPrint();
       //печатаем текст ответа в удобном формате спец командой
    }
}
