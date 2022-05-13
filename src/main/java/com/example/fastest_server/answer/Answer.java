package com.example.fastest_server.answer;


import com.example.fastest_server.question.Question;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "answers")
@Data
@NoArgsConstructor
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

    public Answer(String text, boolean isRight, Question question) {
        this.text = text;
        this.isRight = isRight;
        this.question = question;
    }
}
