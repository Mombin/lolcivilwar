package kr.co.mcedu.user.entity;

import kr.co.mcedu.common.entity.BaseTimeEntity;
import kr.co.mcedu.user.model.UserAlarmType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Where;
import javax.persistence.*;

/**
 * user_alarm table entity
 */
@Getter
@Setter
@ToString
@Entity(name = "user_alarm")
@Table(schema = "lol", name = "user_alarm")
@Where(clause = "is_delete = false")
@SequenceGenerator(sequenceName = "user_alarm_seq", initialValue = 1, allocationSize = 1, name = "user_alarm_seq_generator", schema = "lol")
public class UserAlarmEntity extends BaseTimeEntity {
    @Id
    @Column(name = "alarm_seq")
    @GeneratedValue(generator = "user_alarm_seq_generator", strategy = GenerationType.SEQUENCE)
    private Long alarmSeq;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq", insertable = true, updatable = true, referencedColumnName = "user_seq")
    private WebUserEntity webUserEntity;
    private String message;
    private String landingUrl;
    @Enumerated(EnumType.STRING)
    private UserAlarmType alarmType;
    private Boolean isRead = false;
    private Boolean isDelete = false;
}