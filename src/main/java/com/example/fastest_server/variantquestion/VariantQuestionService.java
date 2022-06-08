package com.example.fastest_server.variantquestion;

import com.example.fastest_server.test.Test;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class VariantQuestionService {
    private final VariantQuestionRepository variantQuestionRepository;

    public void addVariantQuestion(VariantQuestion variantQuestion) {
        variantQuestionRepository.saveAndFlush(variantQuestion);
    }
}
