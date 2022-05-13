package com.example.fastest_server.test;

import com.example.fastest_server.question.Question;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TestService {

    private final TestRepository testRepository;

    public void addTest(Test test) {
        testRepository.saveAndFlush(test);
    }
}
