package kr.co.mcedu.user.entity;

import kr.co.mcedu.common.entity.BaseTimeEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Getter
@Setter
@ToString
@DynamicUpdate
@Entity(name = "web_user")
@Table(name = "web_user", schema = "lol")
@SequenceGenerator(sequenceName = "web_user_seq", initialValue = 1, allocationSize = 1, name = "web_user_seq_generator", schema = "lol")
public class WebUserEntity extends BaseTimeEntity {
    @Id
    @Column(name = "user_seq")
    @GeneratedValue(generator = "web_user_seq_generator", strategy = GenerationType.SEQUENCE)
    private Long userSeq;
    private String userId;
    private String password;
    private String authority;
    private String email;
    private Boolean confirm = false;
    private String refreshToken;
}