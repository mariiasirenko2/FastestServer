package com.example.fastest_server.answer;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AnswerService {

    public final AnswerRepository answerRepository;
}
