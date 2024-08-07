import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
//импортируем атрибут тест, который имеется в библиотеке jUnit


public class RestAssuredTest {
    //создаем класс HelloWorldTest
    @Test
            //тег ставится, чтобы обозначить следующий за ним код как тест
    public void testRestAssured(){
       Response response = RestAssured
               //используем переменную restAssured, которую импортировали на первой строке
               //она имеет внутри себя те функции которые могут понадобится при составлении вызовов
                .get("https://playground.learnqa.ru/api/hello")
               //Параметр в паттерне builder
               //через точку вызываем эти методы
               //делаем гет запрос на адрес нашего API
               .andReturn();
                //Функция в паттерне builder
       //верни нам результат запроса
        //В переменной response будет храниться информация об ответе на запрос
       response.prettyPrint();
       //печатаем текст ответа в удобном формате спец командой
    }
}
