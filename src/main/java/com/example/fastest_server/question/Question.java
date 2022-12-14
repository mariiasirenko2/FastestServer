package com.example.fastest_server.question;

import com.example.fastest_server.answer.Answer;
import com.example.fastest_server.test.Test;
import com.example.fastest_server.variantquestion.VariantQuestion;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "questions")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "text")
    private String text;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "test_id", nullable = false)
    @JsonIgnore
    private Test test;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private Collection<Answer> answers;


    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<VariantQuestion> variantQuestions;

    public void addVariantQuestion(VariantQuestion variantQuestion) {
        variantQuestions.add(variantQuestion);
    }

    public void addAnswer(Answer answer) {
        answers.add(answer);
    }

    public Question(String text, Collection<Answer> answers) {
        this.text = text;
        this.answers = answers;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", text='" + text + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return id == question.id && text.equals(question.text) && test.equals(question.test) && answers.equals(question.answers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text, test, answers);
    }
}
