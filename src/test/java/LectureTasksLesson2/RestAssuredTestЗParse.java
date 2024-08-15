package LectureTasksLesson2;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;


public class RestAssuredTestЗParse {
    @Test
    public void testRestAssured(){
        Map<String, String> params = new HashMap<>();
        params.put("name", "John");

        JsonPath response = RestAssured
               .given()
               .queryParams(params)
               .get("https://playground.learnqa.ru/api/hello")
               .jsonPath();
        //мы заменили экзекьютор нашего билдера - теперь мы говорим, ответом на запрос будет json  и мы хотим получить овете в виде объекта jsonPath
        //c помощью которого мы будем работать с этим json-ом
        // тип ответа тоже изменился, вместо response у нас теперь jsonPath, см. строка №19

        String answer = response.get("answer");
        System.out.println(answer);
        //теперь у переменной response имеются методы для работать с json, через функцию .get мы можем получить значению ключа ("...")
        //у нашего ключа 'answer' имеется значение 'Hello, <name>' name мы задаем в строке №13


        /* альтернативный вариант с заменой
        String answer = response.get("answer2");
        if (name == null){
            System.out.println("The key 'answer2' is absent");
        } else {
            System.out.println(name);
        }

        */
    }
}
