package com.example.fastest_server.test;

import com.example.fastest_server.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestRepository extends JpaRepository<Test, Integer> {
    List<Test> findByOwner(User owner);
}
