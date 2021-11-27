package kr.co.mcedu.group.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity(name = "group_invite")
@Table(name = "group_invite", schema = "lol")
@SequenceGenerator(sequenceName = "group_seq", initialValue = 1, allocationSize = 1, name = "group_seq_generator", schema = "lol")
public class GroupInvite {

    @Id
    @Column(name = "group_invite_seq")
    @GeneratedValue(generator = "group_invite_seq_generator", strategy = GenerationType.SEQUENCE)
    private Long groupInviteSeq;

    @Column(name = "group_seq")
    private Long groupSeq;

    @Column(name = "user_seq")
    private Long userSeq;
    @CreatedDate
    @Column(name = "invited_date")
    private LocalDateTime invitedDate = null;

    @Column(name = "modified_date")
    private LocalDateTime modifiedDate = null;

    @Column(name = "invited_user_seq")
    private Long invitedUserSeq;

    @Column(name = "invite_result")
    private Boolean inviteResult;

    @Column(name = "expire_result")
    private boolean expireResult;

}
