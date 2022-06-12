package com.example.fastest_server.variant;

import com.example.fastest_server.question.DocxReader;
import lombok.AllArgsConstructor;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class VariantService {
    private final VariantRepository variantRepository;

    public void setMarkToVariant (int idVariant,int mark){
        variantRepository.setMarkToVariant(idVariant,mark);
    };
    public List<Variant> getVariantsList(int idTest)  {
        return variantRepository.findByTestId(idTest);
    }
}
