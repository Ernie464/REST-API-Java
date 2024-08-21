package lib;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class DataGenerator {
    public static String getRandomEmail(){
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
// в переменную timestamp мы клдаем уникальное число - текущую дату и время
        return "learnqa" + timestamp + "@example.com";
// уникальное число добавляем в наш адрес генерируемой электронной почты перед доменом
    }

    public static Map<String,String> getRegistrationData(){
        Map<String, String> data = new HashMap<>();
        data.put("email",DataGenerator.getRandomEmail());
        data.put("password", "123");
        data.put("username","learnqa");
        data.put("firstName","learnqa");
        data.put("lastName","learnqa");

        return data;
    }
    // два метода названных одинаково, если вызвать один без параметров, то нам вернутся дефолтные значения, включая случайно
    // созданный email. Но нам не всегда будет удобно использовать этот метод - например, если нам нужно будет проверить уже созданного пользователя.
    // в таком случае нам необходимо чтобы метод вернул нам конкретно нашего пользователя.
    // для этого мы создали второй метод(ниже), в него можно передать hashmap, в котором мы указываем данные самостоятельно


    public static Map<String, String> getRegistrationData(Map<String, String> nonDefaultValues){
        Map<String, String> defaultValues = DataGenerator.getRegistrationData();

        Map<String, String> userData = new HashMap<>();
        String[] keys = {"email","password","username","firstName","lastName"};
        for (String key : keys) {
            if (nonDefaultValues.containsKey(key)){
                userData.put(key, nonDefaultValues.get(key));
            }else{
                userData.put(key,defaultValues.get(key));
            }

        }
        return userData;
    }






}
