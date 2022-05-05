package com.example.fastest_server.variant;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class VariantService {
    private final VariantRepository variantRepository;
}
