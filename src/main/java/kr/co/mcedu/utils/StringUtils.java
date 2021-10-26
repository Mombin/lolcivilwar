package kr.co.mcedu.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StringUtils {
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
