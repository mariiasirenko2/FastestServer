package com.example.fastest_server.variantquestion;

import com.example.fastest_server.answer.Chars;
import com.example.fastest_server.test.Test;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class VariantQuestionService {
    private final VariantQuestionRepository variantQuestionRepository;

    public void addVariantQuestion(VariantQuestion variantQuestion) {
        variantQuestionRepository.saveAndFlush(variantQuestion);
    }

    public List<Chars> getAnswers(int variantId){
        return variantQuestionRepository.getVariantAnswers(variantId);
    }


}
