package com.example.fastest_server.answer;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AnswerService {
    @Autowired
    public final AnswerRepository answerRepository;
}
