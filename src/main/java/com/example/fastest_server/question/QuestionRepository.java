package com.example.fastest_server.question;

import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository  extends JpaRepository<Question,Integer> {
}
