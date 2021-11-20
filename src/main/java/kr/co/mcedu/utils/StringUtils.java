package kr.co.mcedu.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Random;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StringUtils {
    private static final Random RANDOM = new Random();
    public static String randomStringGenerate(int size) {
        if(size < 0 ){
            return "";
        }
        StringBuilder temp = new StringBuilder();
        for (int i = 0; i < size; i++) {
            switch (RANDOM.nextInt(3)) {
                case 0:
                    temp.append((char) (RANDOM.nextInt(26) + 97));
                    break;
                case 1:
                    temp.append((char) (RANDOM.nextInt(26) + 65));
                    break;
                case 2:
                    temp.append(RANDOM.nextInt(10));
                    break;
            }

        }
        return temp.toString();
    }
    /**
     * String empty check
     * @param str 문자열
     * @return empty 여부
     */
    public static boolean isEmpty(String str) {
        return (str == null) || str.isEmpty();
    }

    /**
     * String not empty Check
     * @param str 문자열
     * @return empty 여부
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }
}
