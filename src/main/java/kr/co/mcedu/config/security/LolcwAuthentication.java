package kr.co.mcedu.config.security;

import kr.co.mcedu.group.model.GroupAuthDto;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Map;

/**
 * LOL CIVIL WAR에 맞는 Authentication 구현체
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class LolcwAuthentication extends UsernamePasswordAuthenticationToken {

    private Map<Long, GroupAuthDto> groupAuth;
    private long userSeq;

    public LolcwAuthentication(final Object principal, final Object credentials) {
        super(principal, credentials);
    }

    public LolcwAuthentication(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }
}
