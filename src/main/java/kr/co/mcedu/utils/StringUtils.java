package kr.co.mcedu.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Random;
import java.util.function.Supplier;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StringUtils {
    private static final Random RANDOM = new Random();
    
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

    /**
     * 지정된 길이의 랜덤한 대문자, 소문자, 숫자형 문자를 생성
     * @param length 길이
     * @return 길이가 0이하인 경우 공백, 나머지 랜덤 대문자, 소문자, 숫자형 문자
     */
    public static String randomStringGenerate(int length) {
        return randomStrWithSupplier(length, StringUtils::randomStr);
    }

    /**
     * 랜덤한 대문자, 소문자, 숫자형 문자를 생성
     * @return 대문자, 소문자, 숫자형 문자 1개 
     */
    public static String randomStr() {
        String result;
        switch (RANDOM.nextInt(3)) {
            case 0:
                result = randomLowerStr();
                break;
            case 1:
                result = randomCapitalStr();
                break;
            default:
                result = randomNumberStr();
        }
        return result;
    }
    
    /**
     * 지정된 길이의 랜덤 대문자
     * @param length 길이
     * @return 길이가 0이하일경우 공백, 나머지 랜덤 대문자
     */
    public static String randomCapitalStr(int length) {
        return randomStrWithSupplier(length, StringUtils::randomCapitalStr);
    }

    /**
     * 랜덤 대문자
     * @return 랜덤 대문자 1개
     */
    public static String randomCapitalStr() {
        return String.valueOf((char) (RANDOM.nextInt(26) + 65));
    }

    /**
     * 지정된 길이의 숫자형 문자
     * @param length 길이
     * @return 길이가 0이하일경우 공백, 나머지 랜덤 숫자
     */
    public static String randomNumberStr(int length) {
        return randomStrWithSupplier(length, StringUtils::randomNumberStr);
    }

    /**
     * 랜덤 숫자형 문자
     * @return 랜덤 숫자 1개
     */
    public static String randomNumberStr() {
        return String.valueOf(RANDOM.nextInt(10));
    }

    /**
     * 지정된 길이의 랜덤 소문자
     * @param length 길이
     * @return 길이가 0이하일경우 공백, 나머지 랜덤 소문자
     */
    public static String randomLowerStr(int length) {
        return randomStrWithSupplier(length, StringUtils::randomLowerStr);
    }

    /**
     * 랜덤 소문자
     * @return 랜덤 소문자 1개
     */
    public static String randomLowerStr() {
        return String.valueOf((char) (RANDOM.nextInt(26) + 97));
    }

    /**
     * 문자 제공 함수를 이용하여 지정된 길이의 랜덤 문자
     * @param length 길이
     * @param randomSup 문자제공 함수
     * @return 길이가 0이하일경우 공백, 나머지 문자제공함수에 맞는 랜덤문자
     */
    private static String randomStrWithSupplier(int length, Supplier<String> randomSup) {
        if (length <= 0) {
            return "";
        }
        StringBuilder temp = new StringBuilder();

        for (int i = 0; i < length; i++) {
            temp.append(randomSup.get());
        }

        return temp.toString();
    }

    /**
     * LOLCW TAG 생성
     * 생성규칙 대문자 3자리 + 숫자 2자리
     * @return LOLCW TAG
     * @since 2021. 11. 22
     */
    public static String getLolcwTag() {
        return randomCapitalStr(3) + randomNumberStr(2);
    }
}
