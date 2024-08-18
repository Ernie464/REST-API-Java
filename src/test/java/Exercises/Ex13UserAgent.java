package Exercises;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class Ex13UserAgent {
    private static final Map<String, Map<String, String>> agentsHash = Map.of(
            "Mozilla/5.0 (Linux; U; Android 4.0.2; en-us; Galaxy Nexus Build/ICL53F) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30",
            Map.of("platform", "Mobile", "browser", "No", "device", "Android"),
            "Mozilla/5.0 (iPad; CPU OS 13_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/91.0.4472.77 Mobile/15E148 Safari/604.1",
            Map.of("platform", "Mobile", "browser", "Chrome", "device", "iOS"),
            "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)",
            Map.of("platform", "Googlebot", "browser", "Unknown", "device", "Unknown"),
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36 Edg/91.0.100.0",
            Map.of("platform", "Web", "browser", "Chrome", "device", "No"),
            "Mozilla/5.0 (iPad; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1",
            Map.of("platform", "Mobile", "browser", "No", "device", "iPhone"));

    @ParameterizedTest
    @ValueSource(strings = {"Mozilla/5.0 (Linux; U; Android 4.0.2; en-us; Galaxy Nexus Build/ICL53F) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30","Mozilla/5.0 (iPad; CPU OS 13_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/91.0.4472.77 Mobile/15E148 Safari/604.1", "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)","abcdefghijklm15","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36 Edg/91.0.100.0","Mozilla/5.0 (iPad; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1"})
    public void agentExtractor(String agentString){
        Map<String, String> headers = new HashMap<>();
        headers.put("user-agent",agentString);
        JsonPath respAgent = RestAssured
            .given()
            .headers(headers)
            .get("https://playground.learnqa.ru/ajax/api/user_agent_check")
            .jsonPath();
        respAgent.prettyPrint();

        String plat = respAgent.getString("platform");
        String brows = respAgent.getString("browser");
        String devi = respAgent.getString("device");

        Map<String, String> values01 = agentsHash.get(agentString);
        String platHash = values01.get("platform");
        Map<String, String> values02 = agentsHash.get(agentString);
        String browsHash = values02.get("browser");
        Map<String, String> values03 = agentsHash.get(agentString);
        String deviHash = values03.get("device");

        assertEquals(platHash, plat,"Wrong device");
        assertEquals(browsHash, brows,"Wrong device");
        assertEquals(deviHash, devi,"Wrong device");

    }

}
