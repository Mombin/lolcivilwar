package kr.co.mcedu.user.controller;

import kr.co.mcedu.user.model.request.JwtRequest;
import kr.co.mcedu.user.model.request.UserRegisterRequest;
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

@Slf4j
@RestController
@RequiredArgsConstructor
public class LoginController {
    private final UserDetailServiceImpl userDetailsService;
    @PostMapping("/authenticate")
    public ResponseEntity<Object> createAuthenticationToken(@RequestBody JwtRequest jwtRequest) {
        Map<String, String> result = new HashMap<>();
        String token = "";
        try {
            token = userDetailsService.login(jwtRequest);
        } catch (Exception e) {

            result.put("code", "99999");
            result.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
        }
        ResponseCookie cookie = ResponseCookie.from(ACCESS_TOKEN.getCookieName(), token).path("/").httpOnly(true).build();
        result.put("code", "00000");
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(result);
    }

    @PostMapping("/authenticate/out")
    public ResponseEntity<Object> logout(HttpServletRequest request) {
        ResponseCookie cookie = ResponseCookie.from(ACCESS_TOKEN.getCookieName(), "").path("/").maxAge(0).httpOnly(true).build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).build();
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