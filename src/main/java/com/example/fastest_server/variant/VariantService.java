package com.example.fastest_server.variant;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class VariantService {
    @Autowired
    private final VariantRepository variantRepository;

    public void setMarkToVariant(Long idVariant, Integer mark) {
        variantRepository.setMarkToVariant(idVariant, mark);
    }

    public List<Variant> getVariantsList(Long idTest) {
        return variantRepository.findByTestId(idTest);
    }

    public List<Variant> findByTestId(Long idTest) {
        return variantRepository.findByTestId(idTest);
    }

    public Variant addVariant(Variant variant) {
        return variantRepository.saveAndFlush(variant);
    }
}
