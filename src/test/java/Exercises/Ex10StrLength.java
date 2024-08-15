package Exercises;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Ex10StrLength {
    @ParameterizedTest
    @ValueSource(strings = {"1","abcdefghijklm15","abcdefghijklmn16",""})
        public void lengthCheck(String word){
            assertEquals(true, word.length()>15,"Your string should be longer than 15 symbols");
    }

}
