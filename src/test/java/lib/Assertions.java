package lib;

import io.restassured.response.Response;

import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.not;
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
    public static void assertJsonByName(Response Response, String name, String expectedValue){
        Response.then().assertThat().body("$", hasKey(name));

        String value = Response.jsonPath().getString(name);
        assertEquals(expectedValue, value, "Json value is not equal to expected value");

    }



    public static void assertResponseTextEquals(Response Response, String expectedAnswer){
        //метод для проверки того что текст ответа сервера равен ожидаемому, см. UserRegisterTest
        assertEquals(
                expectedAnswer,
                Response.asString(),
                "Response text is not as expected"
        );
    }
    public static void assertResponseCodeEquals(Response Response, int expectedStatusCode) {
        //метод для проверки того что статус код ответа сервера равен ожидаемому, см. UserRegisterTest
        assertEquals(
                expectedStatusCode,
                Response.statusCode(),
                "Response status code is not as expected"
        );
    }


    public static void assertJsonHasField(Response Response, String unexpectedFieldName) {
        Response.then().assertThat().body("$", hasKey(unexpectedFieldName));
    }


    public static void assertJsonHasFields(Response Response, String[] expectedFieldNames){
        for (String expectedFieldName : expectedFieldNames)
            Assertions.assertJsonHasField(Response, expectedFieldName);
        // expectedFieldNames - это массив, и через цикл мы вызываем для каждого элемента массива метод assertJsonHasField
    }

    public static void assertJsonHasNotField(Response Response, String unexpectedFieldName){
        Response.then().assertThat().body("$", not(hasKey(unexpectedFieldName)));
    }

    public static void assertJsonHasKey(Response Response, String expectedFieldName){
        Response.then().assertThat().body("$", hasKey(expectedFieldName));


    }





}
