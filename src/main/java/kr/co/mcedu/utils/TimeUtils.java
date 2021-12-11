package kr.co.mcedu.utils;

import org.springframework.data.util.Pair;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class TimeUtils {
    public static final int SECONDS_AS_MILLISECONDS = 1000;
    public static final String FULL_PATTERN = "yyyyMMddHHmmss";
    public static final String DAY_HOUR_MIN_PATTERN = "yyyyMMddHHmm";
    public static final String DAY_HOUR_PATTERN = "yyyyMMddHH";
    public static final String DAY_PATTERN = "yyyyMMdd";
    public static final String HOUR_PATTERN = "HH";
    public static final String MIN_PATTERN = "mm";
    public static final Long minute = 60L;
    public static final Long hour = 60L * minute;
    public static final Long day = 24L * hour;
    public static final ZoneId zoneId = ZoneId.systemDefault();

    /**
     * 현재 시간 반환
     * @return Long timeMillis
     */
    public static Long now() {
        return System.currentTimeMillis();
    }

    /**
     * 현재 시간과의 차이 반환
     * @param time 비교대상
     * @return 현재시간 - 비교대상
     */
    public static Long timeDiff(Long time) {
        return System.currentTimeMillis() - time;
    }

    /**
     * 시간 -> 년월일시 패턴
     * @param value ISO_LOCAL_DATE_TIME 시간
     * @return yyyyMMddHH
     */
    public static String parseTodayDetail(String value) {
        return DateTimeFormatter.ofPattern(DAY_HOUR_PATTERN).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME.parse(value));
    }

    /**
     * 시간 -> 년월일시분초 패턴
     * @param value ISO_OFFSET_DATE_TIME 시간
     * @return yyyyMMddHHmmss
     */
    public static String parseTodayDetailOffset(String value) {
        return DateTimeFormatter.ofPattern(FULL_PATTERN).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME.parse(value));
    }

    public static String timeDetail(String pattern) {
        return timeDetail(0, pattern);
    }

    public static String timeDetail(int dayDif) {
        return timeDetail(dayDif, FULL_PATTERN);
    }

    public static String timeDetail(int dayDif, String pattern) {
        return timeDetail(dayDif, 0, pattern);
    }

    public static String timeDetail(int dayDif, int hourDif) {
        return timeDetail(dayDif, hourDif, FULL_PATTERN);
    }

    public static String timeDetail(int dayDif, int hourDif, String pattern) {
        return timeDetail(dayDif, hourDif, 0, pattern);
    }

    public static String timeDetail(int dayDif, int hourDif, int minDif) {
        return timeDetail(dayDif, hourDif, minDif, FULL_PATTERN);
    }

    public static String timeDetail(int dayDif, int hourDif, int minDif, String pattern) {
        return DateTimeFormatter.ofPattern(pattern).format(
                LocalDateTime.now()
                             .plusDays(dayDif)
                             .plusHours(hourDif)
                             .plusMinutes(minDif)
        );
    }
    /**
     * 문자열의 연속적인 시간을 '년', '월', '일' 이 붙은 시간으로 변환
     * @param time 문자열 형태의 시간
     * @param oriPattern 원래 패턴
     * @param newPattern 변경할 패턴
     * @return 변환된 시간
     */
    public static String convertTimeForKorean(String time, String oriPattern, String newPattern) {
        String convertedTime = " ";
        if(newPattern.contains("yyyy") && oriPattern.contains("yyyy")) {
            int idx = oriPattern.indexOf("yyyy");
            convertedTime += time.substring(idx, idx + 4) +  "년 ";
        }
        if(newPattern.contains("MM") && oriPattern.contains("MM")) {
            int idx = oriPattern.indexOf("MM");
            convertedTime += time.substring(idx, idx + 2) +  "월 ";
        }
        if(newPattern.contains("dd") && oriPattern.contains("dd")) {
            int idx = oriPattern.indexOf("dd");
            convertedTime += time.substring(idx, idx + 2) +  "일 ";
        }
        if(newPattern.contains("HH") && oriPattern.contains("HH")) {
            int idx = oriPattern.indexOf("HH");
            convertedTime += time.substring(idx, idx + 2) +  "시 ";
        }
        if(newPattern.contains("mm") && oriPattern.contains("mm")) {
            int idx = oriPattern.indexOf("mm");
            convertedTime += time.substring(idx, idx + 2) +  "분 ";
        }
        if(newPattern.contains("ss") && oriPattern.contains("ss")) {
            int idx = oriPattern.indexOf("ss");
            convertedTime += time.substring(idx, idx + 2) + "초";
        }

        return convertedTime.trim();
    }

    public static String getHourMin() {
        return timeDetail("HHmm");
    }

    public static String getHour()  {
        return timeDetail(HOUR_PATTERN);
    }

    public static String getMin()  {
        return timeDetail(MIN_PATTERN);
    }


    /**
     * 웹 노출용 문구로 변경
     */
    public static String convertViewTime(LocalDateTime createdDate){
        long diffSeconds = 0L;
        String last = "";
        if (Objects.nonNull(createdDate)) {
            diffSeconds = LocalDateTime.now().atZone(zoneId).toEpochSecond() - createdDate.atZone(zoneId).toEpochSecond();
            last = createdDate.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일"));
        }

        String result = "";
        if (diffSeconds == 0L) {
            result = "";
        } else if(diffSeconds < minute) {
            result = diffSeconds + "초 전";
        } else if(diffSeconds < hour) {
            result = (diffSeconds / minute) + "분 전";
        } else if(diffSeconds < day) {
            result = (diffSeconds / hour) + "시간 전";
        } else if(diffSeconds < 7 * day) {
            result = (diffSeconds / day) + "일 전";
        } else {
            result = last;
        }

        return result;
    }

    /**
     * First : Unit, Second Diff
     * @param target
     * @return
     */
    public static Pair<String, Long> diffFromCurrent(LocalDateTime target) {
        LocalDateTime current = LocalDateTime.now();
        if (target.isBefore(current)) {
            return Pair.of("", 0L);
        }
        long between = ChronoUnit.HOURS.between(current, target);
        String unit = "시간";
        if (between == 0) {
            between = ChronoUnit.MINUTES.between(current, target);
            unit = "분";
        }
        if (between == 0) {
            between = ChronoUnit.SECONDS.between(current, target);
            unit = "초";
        }
        return Pair.of(unit, between);
    }
}