package kr.co.mcedu.group.entity;

import kr.co.mcedu.match.entity.MatchAttendeesEntity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SynergyModel {
   private String nickname = "";
   private String summonerName = "";
   private int total = 0;
   private int win = 0;
   private LocalDateTime lastDate = null;
   private String lastDateString = "";
   private double rate = 0.0;

   private void add(MatchAttendeesEntity matchAttendeesEntity){
      this.nickname = matchAttendeesEntity.getCustomUserEntity() != null ?  matchAttendeesEntity.getCustomUserEntity().getNickname() : "" ;
      this.summonerName = matchAttendeesEntity.getCustomUserEntity() != null ? matchAttendeesEntity.getCustomUserEntity().getSummonerName() : "";
      this.total += 1;
      if(matchAttendeesEntity.isMatchResult()){
         this.win += 1;

      }else{
         this.win += 0;
      }
      if(this.lastDate !=null){
         if(matchAttendeesEntity.getCreatedDate() == null){
            matchAttendeesEntity.setCreatedDate(LocalDateTime.now());
         }
         if(this.lastDate.isBefore(matchAttendeesEntity.getCreatedDate()) != false){
            this.lastDate = matchAttendeesEntity.getCreatedDate();
         }
      }
      this.rate = (double)Math.round((double)this.win / (double)this.total * (double)1000) / 10.0D;
      this.lastDateString = this.lastDate  != null ? this.lastDate.format(DateTimeFormatter.ofPattern("YYYY-MM-dd")) != null ? this.lastDate.format(DateTimeFormatter.ofPattern("YYYY-MM-dd")) : "" : "";


   }
}
