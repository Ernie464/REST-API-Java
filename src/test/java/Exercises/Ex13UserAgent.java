package Exercises;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class Ex13UserAgent {
    Map<String, String> matritsa = new HashMap<>();

//    @ParameterizedTest

    @Test

//    @ValueSource(strings = {"Mozilla/5.0 (Linux; U; Android 4.0.2; en-us; Galaxy Nexus Build/ICL53F) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30","Mozilla/5.0 (iPad; CPU OS 13_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/91.0.4472.77 Mobile/15E148 Safari/604.1", "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)","abcdefghijklm15","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36 Edg/91.0.100.0","Mozilla/5.0 (iPad; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1"})

    public void AgentExtractor(){
//    public void agentExtractor(String agentString){
        Map<String, String[]>matritsa = new HashMap<>();
        matritsa.put("Mozilla/5.0 (Linux; U; Android 4.0.2; en-us; Galaxy Nexus Build/ICL53F) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30", ["Mobile","No","Android"]);
        matritsa.put("Mozilla/5.0 (iPad; CPU OS 13_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/91.0.4472.77 Mobile/15E148 Safari/604.1", ["Mobile","Chrome","iOS"]);
        matritsa.put("Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)", ["Googlebot","Unknown", "Unknown"]);
        matritsa.put("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36 Edg/91.0.100.0", ["Web", "Chrome", "No"]);
        matritsa.put("Mozilla/5.0 (iPad; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1",["Web", "Chrome", "No"]);

        String agentString = "Mozilla/5.0 (Linux; U; Android 4.0.2; en-us; Galaxy Nexus Build/ICL53F) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30";
        Map<String, String> headers = new HashMap<>();
        headers.put("user-agent",agentString);
        JsonPath respAgent = RestAssured
            .given()
            .headers(headers)
            .get("https://playground.learnqa.ru/ajax/api/user_agent_check")
            .jsonPath();
        respAgent.prettyPrint();

        String[] agentArr = matritsa.get(agentString);

        String plat = respAgent.getString("platform");
        assertEquals(agentArr[0], plat,"Wrong platform");
        String brows = respAgent.getString("browser");
        assertEquals(agentArr[1], brows,"Browser usage check failed");
        String devi = respAgent.getString("device");
        assertEquals(agentArr[2], devi,"Wrong device");



    }

}
