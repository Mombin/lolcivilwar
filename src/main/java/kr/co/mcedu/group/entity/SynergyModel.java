package kr.co.mcedu.group.entity;

import kr.co.mcedu.match.entity.MatchAttendeesEntity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class SynergyModel {
   private String nickname = "";
   private String summonerName = "";
   private int total = 0;
   private int win = 0;
   private LocalDateTime lastDate = null;
   private String lastDateString = "";
   private double rate = 0.0;


   private void add(MatchAttendeesEntity matchAttendeesEntity){

      Optional<CustomUserEntity> customUserEntity = Optional.ofNullable(matchAttendeesEntity.getCustomUserEntity());
      this.nickname = customUserEntity.map(CustomUserEntity::getNickname).orElse("");
      this.summonerName = customUserEntity.map(CustomUserEntity::getSummonerName).orElse("");
      if(matchAttendeesEntity.isMatchResult()){
         this.win += 1;
      }
      if(this.lastDate !=null){
         if(matchAttendeesEntity.getCreatedDate() == null){
            matchAttendeesEntity.setCreatedDate(LocalDateTime.now());
         }
         if(this.lastDate.isBefore(matchAttendeesEntity.getCreatedDate())){
            this.lastDate = matchAttendeesEntity.getCreatedDate();
         }
      }
      this.rate = (double)Math.round((double)this.win / (double)this.total * (double)1000) / 10.0D;

      if (this.lastDate != null) {
         this.lastDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
         this.lastDateString = this.lastDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
      } else {
         this.lastDateString = "";
      }


   }
}
