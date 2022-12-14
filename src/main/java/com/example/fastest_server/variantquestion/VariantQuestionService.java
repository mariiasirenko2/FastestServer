package com.example.fastest_server.variantquestion;

import com.example.fastest_server.answer.Chars;
import com.example.fastest_server.test.Test;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class VariantQuestionService {
    @Autowired
    private final VariantQuestionRepository variantQuestionRepository;

    public VariantQuestion addVariantQuestion(VariantQuestion variantQuestion) {
        return variantQuestionRepository.saveAndFlush(variantQuestion);
    }

    public List<Chars> getAnswers(Long variantId) {
        return variantQuestionRepository.getVariantAnswers(variantId);
    }


}
