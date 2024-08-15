package LectureTasksLesson3;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class jUnitHelloWorldTest {
    @ParameterizedTest
    //Тест зависящий от передаваемых в него параметров, тест будет перезапускаться столько раз сколько параметров в него передали
    @ValueSource(strings = {"", "John", "Peter"})
    //передали параметры для теста
    public void testHelloMethodWithParameters(String name){
        Map<String,String> queryParams = new HashMap<>();

        if(name.length() > 0){
            queryParams.put("name",name);
        }
        // указываем условие, что если строка в переменной name больше 15-ти символов, добавляем в queryParams параметр "name" со значением из переменной name
        //если параметр пустой, то ничего не передаем - наш хэш меп остается пустым

        JsonPath response = RestAssured
                .given()
                .queryParams(queryParams)
                .get("https://playground.learnqa.ru/api/hello")
                .jsonPath();
        String answer = response.getString("answer");
        //в переменную answer кладем то что получаем от сервиса
        String expectedName = (name.length() > 0)? name : "someone";
        //условие, что если длина строки в переменной name больше нуля, то кладем в expectedName имя из наших параметров
        //в обратном случае (после ":")  кладем в expectedName "someone"
        assertEquals( "Hello, " + expectedName, answer, "The answer is unexpected" );
        //сначала собираем ожидаемый ответ от сервера, затем сравниваем с тем что получили по факту от сервиса
    }




}
