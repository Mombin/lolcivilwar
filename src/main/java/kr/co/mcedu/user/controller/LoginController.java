package kr.co.mcedu.user.controller;

import kr.co.mcedu.config.exception.ServiceException;
import kr.co.mcedu.user.model.request.JwtRequest;
import kr.co.mcedu.user.model.request.UserRegisterRequest;
import kr.co.mcedu.user.service.WebUserService;
import kr.co.mcedu.user.service.impl.UserDetailServiceImpl;
import kr.co.mcedu.utils.ResponseWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

import static kr.co.mcedu.config.security.TokenType.ACCESS_TOKEN;
import static kr.co.mcedu.config.security.TokenType.REFRESH_TOKEN;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LoginController {
    private final UserDetailServiceImpl userDetailsService;
    private final WebUserService webUserService;

    @PostMapping("/authenticate")
    public ResponseEntity<Object> createAuthenticationToken(@RequestBody JwtRequest jwtRequest) {
        Map<String, String> result = new HashMap<>();
        String refreshToken = "";
        try {
            refreshToken = userDetailsService.login(jwtRequest);
        } catch (Exception e) {
            result.put("code", "99999");
            result.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
        }
        ResponseEntity.BodyBuilder bodyBuilder = ResponseEntity.ok();
        ResponseCookie refreshTokenCookie = ResponseCookie.from(REFRESH_TOKEN.getCookieName(), refreshToken).path("/").httpOnly(true).build();
        try {
            ResponseCookie accessToken = ResponseCookie.from(ACCESS_TOKEN.getCookieName(), webUserService.getAccessToken(refreshToken)).path("/").httpOnly(true).build();
            bodyBuilder.header(HttpHeaders.SET_COOKIE, accessToken.toString());
        } catch (ServiceException exception) {
            log.error("", exception);
        }

        result.put("code", "00000");
        return bodyBuilder.header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString()).body(result);
    }

    @PostMapping("/authenticate/out")
    public ResponseEntity<Object> logout(HttpServletRequest request) {
        ResponseCookie refreshCookie = ResponseCookie.from(REFRESH_TOKEN.getCookieName(), "").path("/").maxAge(0).httpOnly(true).build();
        ResponseCookie accessCookie = ResponseCookie.from(ACCESS_TOKEN.getCookieName(), "").path("/").maxAge(0).httpOnly(true).build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, refreshCookie.toString(), accessCookie.toString()).build();
    }

    @GetMapping("/user/checkId/{userId}")
    public Object idCheck(@PathVariable String userId) {
        log.info("LoginController > idCheck : {}", userId);
        Map<String, Object> result = new HashMap<>();
        result.put("id", userId);
        result.put("result", !userDetailsService.checkUserByUserId(userId));
        return new ResponseWrapper().setData(result).build();
    }

    @GetMapping("/user/checkEmail/{email}")
    public Object emailCheck(@PathVariable String email) {
        log.info("LoginController > emailCheck : {}", email);
        Map<String, Object> result = new HashMap<>();
        result.put("email", email);
        result.put("result", !userDetailsService.checkUserByEmail(email));
        return new ResponseWrapper().setData(result).build();
    }

    @PostMapping("/user/register")
    public Object register(@RequestBody UserRegisterRequest request) {
        log.info("LoginController > register : {}", request.getId());
        try {
            return new ResponseWrapper().setData(userDetailsService.registerUser(request)).build();
        } catch (Exception exception) {
            return ResponseWrapper.fail(exception.getMessage()).build();
        }
    }
}