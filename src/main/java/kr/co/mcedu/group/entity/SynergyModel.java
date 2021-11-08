package kr.co.mcedu.group.entity;

import kr.co.mcedu.match.entity.MatchAttendeesEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Getter
@Setter
public class SynergyModel {
   private String nickname = "";
   private String summonerName = "";
   private int total = 0;
   private int win = 0;
   private LocalDateTime lastDate = null;
   private String lastDateString = "";
   private double rate = 0.0;


   public void add(MatchAttendeesEntity matchAttendeesEntity){
      Optional<CustomUserEntity> customUserEntity = Optional.ofNullable(matchAttendeesEntity.getCustomUserEntity());
      this.nickname = customUserEntity.map(CustomUserEntity::getNickname).orElse("");
      this.summonerName = customUserEntity.map(CustomUserEntity::getSummonerName).orElse("");
      this.total += 1;
      if(matchAttendeesEntity.isMatchResult()){
         this.win += 1;
      }

      if (this.lastDate == null || this.lastDate.isBefore(matchAttendeesEntity.getCreatedDate())) {
         this.lastDate = matchAttendeesEntity.getCreatedDate();
      }

      this.rate = Math.round((double)this.win / (double)this.total * 1000) / 10.0D;
      this.lastDateString = Optional.ofNullable(this.lastDate).map(localDateTime -> localDateTime.format(
              DateTimeFormatter.ofPattern("yyyy-MM-dd"))).orElse("");
   }
}
