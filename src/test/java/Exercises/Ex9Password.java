package Exercises;

import io.restassured.RestAssured;
import io.restassured.http.Cookie;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import org.junit.jupiter.api.Test;


import java.util.Map;

public class Ex9Password {
    @Test
    public void testRestAssured() {
        String[] passArr = {"qwerty", "password", "1234567", "12345678", "12345", "iloveyou", "111111", "123123", "abc123", "qwerty123", "1q2w3e4r", "admin", "qwertyuiop", "654321", "555555", "lovely", "7777777", "welcome", "888888", "dragon", "princess", "123qwe", "password1", "666666", "sunshine", "football", "monkey", "!@#$%^&*", "aa123456", "charlie", "donald", "letmein", "login", "starwars", "passw0rd", "master", "hello", "freedom", "whatever", "qazwsx", "trustno1", "1234567890", "1234", "solo", "121212", "flower", "hottie", "loveme", "zaq1zaq1", "baseball", "1qaz2wsx", "ashley", "bailey", "shadow", "michael", "superman", "jesus", "ninja", "mustang", "photoshop", "adobe123", "azerty", "0", "access", "696969", "batman"};
        for (int i = 0; i < passArr.length; i++) {
            String lowgin = "super_admin";
            String passaword = passArr[i];
//            String passaword = passArr[17];
//            System.out.println(passArr[i]);
            Response response = RestAssured
                    .given()
                    .queryParam("login", lowgin)
                    .queryParam("password", passaword)
                    .post("https://playground.learnqa.ru/ajax/api/get_secret_password_homework")
                    .andReturn();
//            response.prettyPrint();
            Map<String, String> responseCookies = response.getCookies();
//            System.out.println(responseCookies);
            String number_auth = responseCookies.get("auth_cookie");
            Response response2 = RestAssured
                    .given()
                    .cookie("auth_cookie",number_auth)
                    .post("https://playground.learnqa.ru/ajax/api/check_auth_cookie")
                    .andReturn();
            String final_message = response2.getBody().asString();
//            System.out.println(final_message);
            boolean contains = final_message.contains("You are NOT authorized");
            if (contains == false){
                System.out.println("Correct password:");
                System.out.println(passArr[i]);
                break;
            }

        }

    }
}