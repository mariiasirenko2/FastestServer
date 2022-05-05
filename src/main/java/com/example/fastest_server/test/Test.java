package com.example.fastest_server.test;

import com.example.fastest_server.question.Question;
import com.example.fastest_server.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "tests")
@Data
public class Test {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column (name = "test_name")
    private String testName;

    @Column (name ="question_file")
    private String fileWithQuestions;

    @Column (name = "student_file")
    private String fileWithStudents;

    @ManyToOne (fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    @JsonIgnore
    private User owner;

    @OneToMany(mappedBy = "test", cascade = CascadeType.ALL)
    private Collection<Question> test;
}
