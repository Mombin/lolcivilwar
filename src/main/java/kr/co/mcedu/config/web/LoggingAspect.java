package kr.co.mcedu.config.web;

import kr.co.mcedu.utils.SessionUtils;
import kr.co.mcedu.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Aspect
@Slf4j
@Component
public class LoggingAspect {

    private static final Set<String> methods = new HashSet<>(Arrays.asList("POST", "PUT"));

    @Pointcut("within(kr.co.mcedu.*.controller..*)")
    public void onRequest() {
    }

    @Around("kr.co.mcedu.config.web.LoggingAspect.onRequest()")
    public Object requestLogging(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String session = StringUtils.randomStringGenerate(6);
        try {
            log.info("{} - Request: {} {} {}, id : {}, ip : {}", session, request.getMethod(), request.getRequestURL(), paramMapToString(request.getParameterMap()),
                    SessionUtils.getId(), SessionUtils.getIp());
            if (methods.contains(String.valueOf(request.getMethod()).toUpperCase())) {
                List<String> collect = request.getReader().lines().collect(Collectors.toList());

                if (!collect.isEmpty() && !collect.get(0).equals("{}")) {
                    log.info("Body: {} ", collect);
                }
            }
        } catch (Exception exception) {
            log.info("LoggingAspect : ", exception);
        }

        long start = System.currentTimeMillis();
        try {
            return proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
        } finally {
            long end = System.currentTimeMillis();
            log.info("{} - Service Duration {}ms", session, end - start);
        }
    }

    private String paramMapToString(Map<String, String[]> paraStringMap) {
        return paraStringMap.entrySet().stream()
                            .map(entry -> String.format("%s : %s", entry.getKey(), Arrays.toString(entry.getValue())))
                            .collect(Collectors.joining(", "));
    }
}

