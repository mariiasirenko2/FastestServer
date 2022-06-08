package com.example.fastest_server.variantquestion;

import com.example.fastest_server.answer.Chars;
import com.example.fastest_server.question.Question;
import com.example.fastest_server.variant.Variant;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table (name = "variant_question")
@Data
@NoArgsConstructor
public class VariantQuestion {
    @EmbeddedId
    private VariantQuestionKey id;

    @JsonIgnore
    @ManyToOne (fetch = FetchType.LAZY)
    @MapsId("variantId")
    @JoinColumn(name = "variant_id")
    private Variant variant;

    @JsonIgnore
    @ManyToOne (fetch = FetchType.LAZY)
    @MapsId("questionId")
    @JoinColumn(name = "question_id")
    private Question question;


    @Column (name = "question_number")
    private int questionNumber;

    @Column (name = "letter_answer")
    @Enumerated(EnumType.STRING)
    private Chars letterAnswer;


    public VariantQuestion(Variant variant, Question question) {
        this.variant = variant;
        this.question = question;
        this.id = new VariantQuestionKey(variant.getId(), question.getId());
    }

}
