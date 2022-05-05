package com.example.fastest_server.variantquestion;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class VariantQuestionKey implements Serializable {
    @Column(name = "variant_id")
    protected int variantId;

    @Column(name = "question_id")
    protected int questionId;

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

}

