package kr.co.mcedu.group.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
public class CustomUserResponse {
   private long seq;
   
   private Long groupSeq;
   
   private String nickname;
   
   private String summonerName;
   
   private Map positionWinRate;
   
   private int total;
   
   private int win;
   
   private LocalDateTime lastDate;
   
   private String accountId;
   
   private int profileIconId;
   
   private int summonerLevel;
   
   private boolean isRefreshTarget;

   public CustomUserResponse(CustomUserEntity customUserEntity) {
   }
}
