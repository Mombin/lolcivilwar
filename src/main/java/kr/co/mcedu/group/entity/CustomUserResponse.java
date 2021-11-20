package kr.co.mcedu.group.entity;

import kr.co.mcedu.common.entity.BaseTimeEntity;
import kr.co.mcedu.summoner.entity.SummonerEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.util.Pair;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Getter
@Setter
public class CustomUserResponse {
   private long seq;
   private Long groupSeq;
   private String nickname;
   private String summonerName;
   private Map<String, Pair<Integer, Integer>> positionWinRate;
   private int total;
   private int win;
   private LocalDateTime lastDate;
   private String accountId;
   private int profileIconId;
   private int summonerLevel;
   private boolean isRefreshTarget;
   private Long tierPoint;

   public CustomUserResponse(CustomUserEntity customUserEntity) {
      this.seq = customUserEntity.getSeq();
      this.groupSeq = Optional.ofNullable(customUserEntity.getGroup()).map(GroupEntity::getGroupSeq).orElse(null);
      this.nickname = Optional.ofNullable(customUserEntity.getNickname()).orElse("");
      this.summonerName = Optional.ofNullable(customUserEntity.getSummonerName()).orElse("");
      this.positionWinRate = new HashMap<>();
      this.total = 0;
      this.win = 0;
      this.lastDate = null;
      Optional<SummonerEntity> summonerEntity = Optional.ofNullable(customUserEntity.getSummonerEntity());
      this.accountId = summonerEntity.map(SummonerEntity::getAccountId).orElse("");
      this.profileIconId = summonerEntity.map(SummonerEntity::getProfileIconId).orElse(0);
      this.summonerLevel = summonerEntity.map(SummonerEntity::getSummonerLevel).orElse(0);
      this.isRefreshTarget = summonerEntity.map(BaseTimeEntity::getModifiedDate)
                                           .map(localDateTime -> localDateTime.plusHours(1)
                                                                              .isBefore(LocalDateTime.now()))
                                           .orElse(false);
      this.tierPoint = customUserEntity.getTierPoint();
   }

   public void totalIncrease() {
      this.total++;
   }

   public void winIncrease() {
      this.win++;
   }
}
