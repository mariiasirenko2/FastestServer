package com.example.fastest_server.variantquestion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
public class VariantQuestionKey implements Serializable {
    @Column(name = "variant_id")
    protected int variantId;

    @Column(name = "question_id")
    protected int questionId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VariantQuestionKey that = (VariantQuestionKey) o;
        return variantId == that.variantId && questionId == that.questionId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(variantId, questionId);
    }
}

