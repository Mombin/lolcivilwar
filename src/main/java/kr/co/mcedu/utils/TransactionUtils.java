package kr.co.mcedu.utils;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TransactionUtils {

    @Transactional
    public void transaction(Runnable runnable) {
        runnable.run();
    }
}
