package com.example.fastest_server.question;

import com.example.fastest_server.answer.Answer;
import com.example.fastest_server.test.Test;
import com.example.fastest_server.variantquestion.VariantQuestion;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.util.Collection;
import java.util.Set;

@Entity
@Table(name = "questions")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column (name = "text")
    private String text;

    @ManyToOne (fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "test_id",nullable = false)
    @JsonIgnore
    private Test test;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private Collection<Answer> answers;


    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private Set<VariantQuestion> variantQuestions;

    public void addAnswer(Answer answer) {
        answers.add(answer);
    }

    public Question(String text, Collection<Answer> answers) {
        this.text = text;
        this.answers = answers;
    }

}
