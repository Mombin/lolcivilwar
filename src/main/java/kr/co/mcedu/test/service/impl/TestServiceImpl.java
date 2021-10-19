package kr.co.mcedu.test.service.impl;

import kr.co.mcedu.test.repository.TestRepository;
import kr.co.mcedu.test.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {
    public final TestRepository testRepository;
    @Override
    public String getProperty() {
        return testRepository.getTestEntity().getPropertyValue1();
    }
}
