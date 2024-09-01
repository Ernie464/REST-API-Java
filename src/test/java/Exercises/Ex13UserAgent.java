package Exercises;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class Ex13UserAgent {
    private static final Map<String, Map<String, String>> agentsHash;

    static {
        Map<String, Map<String, String>> tempMap = new HashMap<>();

        Map<String, String> map1 = new HashMap<>();
        map1.put("platform", "Mobile");
        map1.put("browser", "No");
        map1.put("device", "Android");
        tempMap.put("Mozilla/5.0 (Linux; U; Android 4.0.2; en-us; Galaxy Nexus Build/ICL53F) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30",
                Collections.unmodifiableMap(map1));

        Map<String, String> map2 = new HashMap<>();
        map2.put("platform", "Mobile");
        map2.put("browser", "Chrome");
        map2.put("device", "iOS");
        tempMap.put("Mozilla/5.0 (iPad; CPU OS 13_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/91.0.4472.77 Mobile/15E148 Safari/604.1",
                Collections.unmodifiableMap(map2));

        Map<String, String> map3 = new HashMap<>();
        map3.put("platform", "Googlebot");
        map3.put("browser", "Unknown");
        map3.put("device", "Unknown");
        tempMap.put("Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)",
                Collections.unmodifiableMap(map3));

        Map<String, String> map4 = new HashMap<>();
        map4.put("platform", "Web");
        map4.put("browser", "Chrome");
        map4.put("device", "No");
        tempMap.put("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36 Edg/91.0.100.0",
                Collections.unmodifiableMap(map4));

        Map<String, String> map5 = new HashMap<>();
        map5.put("platform", "Mobile");
        map5.put("browser", "No");
        map5.put("device", "iPhone");
        tempMap.put("Mozilla/5.0 (iPad; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1",
                Collections.unmodifiableMap(map5));

        agentsHash = Collections.unmodifiableMap(tempMap);
    }

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

        assertEquals(platHash, plat,"Wrong platform");
        assertEquals(browsHash, brows,"Wrong browser");
        assertEquals(deviHash, devi,"Wrong device");

    }

}
