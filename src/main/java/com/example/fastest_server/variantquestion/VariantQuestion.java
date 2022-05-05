package com.example.fastest_server.variantquestion;

import com.example.fastest_server.question.Question;
import com.example.fastest_server.variant.Variant;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table (name = "variant_question")
@Data
public class VariantQuestion {
    @EmbeddedId
    private VariantQuestionKey id;

    @ManyToOne
    @MapsId("variantId")
    @JoinColumn(name = "variant_id")
    private Variant variant;

    @ManyToOne
    @MapsId("questionId")
    @JoinColumn(name = "question_id")
    private Question question;


    @Column (name = "question_number")
    private int questionNumber;

    @Column (name = "letter_answer")
    private char letterAnswer;

}
