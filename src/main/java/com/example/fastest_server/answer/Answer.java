package com.example.fastest_server.answer;


import com.example.fastest_server.question.Question;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "answers")
@Data
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column (name = "text")
    private String text;

    @Column (name = "is_right")
    private boolean isRight;

    @ManyToOne (fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "question_id",nullable = false)
    @JsonIgnore
    private Question question;

}
