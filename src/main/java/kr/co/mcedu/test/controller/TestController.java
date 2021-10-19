package kr.co.mcedu.test.controller;

import kr.co.mcedu.test.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/test")
public class TestController {
    public final TestService testService;
    @GetMapping("/hello")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok(testService.getProperty());
    }
}
