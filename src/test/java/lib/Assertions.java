package lib;

import io.restassured.response.Response;

import static org.hamcrest.Matchers.hasKey;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Assertions {
    public static void assertJsonByName(Response Response, String name, int expectedValue){
        // на вход эта функция будет получать объект с ответом сервера(чтобы вытянуть из него текст), имя(по которому
        // надо будет искать значение в json), и ожидаемое значение
        Response.then().assertThat().body("$", hasKey(name));

        int value = Response.jsonPath().getInt(name);
        assertEquals(expectedValue, value, "Json value is not equal to expected value");
        // на выходе будет происходить assert где функция будет сравнивать expectedValue, value полученное из JSON'a и
        // если оно правильно то асерт проходит и если нет, то отразится сообщение

        // тут мы уже сразу делаем сравнение ожидаемого с фактическим и возвращаем результат - сходится или нет, а не
        // возвращаем сначала объект, и только после этого можем его сравнить с ожиданием
    }
}
