package kr.co.mcedu.group.entity;

import kr.co.mcedu.summoner.entity.SummonerEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity(name = "custom_user")
@Table(name = "custom_user", schema = "lol")
@SQLDelete(sql = "UPDATE lol.custom_user SET del_yn = true WHERE seq = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "del_yn = false")
@Getter
@Setter
public class CustomUserEntity {
    @Id
    @Column(name = "seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_seq", insertable = true, updatable = true, referencedColumnName = "group_seq")
    private GroupEntity groupEntity = null;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "summoner_id")
    private String summonerName;

    @Column(name = "del_yn", columnDefinition = "boolean default false")
    private Boolean delYn = false ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", referencedColumnName = "account_id")
    private SummonerEntity summonerEntity = null;

    public CustomUserEntity(String nickname, String summonerId) {
        this.nickname = nickname;
        this.summonerName = summonerId;
    }

    public CustomUserResponse toCustomUserResponse(){
        return new CustomUserResponse(this);
    }

    @PreRemove
    private void deleteUser () {
        this.delYn = true;
    }

}

