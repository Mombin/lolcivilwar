package kr.co.mcedu.group.entity;

import kr.co.mcedu.common.entity.BaseTimeEntity;
import kr.co.mcedu.user.entity.WebUserEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity(name = "group_auth")
@Table(name = "group_auth", schema = "lol")
@SequenceGenerator(sequenceName = "group_auth_seq", initialValue = 1, allocationSize = 1, name = "group_auth_seq_generator", schema = "lol")
public class GroupAuthEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(generator = "group_auth_seq_generator", strategy = GenerationType.SEQUENCE)
    @Column(name = "group_auth_seq")
    private Long  id ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_seq", referencedColumnName = "group_seq", insertable = true, updatable = true)
    private GroupEntity group = null;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq", referencedColumnName = "user_seq", insertable = true, updatable = true)
    private WebUserEntity webUser  = null;

    @Column(name = "group_auth")
    @Enumerated(EnumType.STRING)
    private GroupAuthEnum groupAuth = null;

}
