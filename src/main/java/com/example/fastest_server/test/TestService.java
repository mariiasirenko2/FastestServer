package com.example.fastest_server.test;

import com.example.fastest_server.user.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TestService {

    private final TestRepository testRepository;

    public void addTest(Test test) {
        testRepository.saveAndFlush(test);
    }

    public Test getTestById(int id) {
        return testRepository.getById(id);
    }

    public List<Test> getTestByOwner(User owner) {
        return testRepository.findByOwner(owner);
    }
}
