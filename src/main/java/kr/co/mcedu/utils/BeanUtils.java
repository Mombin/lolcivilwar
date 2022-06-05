package kr.co.mcedu.utils;

public class BeanUtils {
    public static <T> T getBean(Class<T> classType) {
        return ApplicationContextProvider.getApplicationContext().getBean(classType);
    }
}
