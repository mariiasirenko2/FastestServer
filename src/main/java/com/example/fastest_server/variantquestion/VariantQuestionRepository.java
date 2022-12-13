package com.example.fastest_server.variantquestion;

import com.example.fastest_server.answer.Chars;
import com.example.fastest_server.variant.Variant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface VariantQuestionRepository extends JpaRepository<VariantQuestion,Integer> {

    @Query(value = "SELECT letter_answer FROM variant_question WHERE variant_id = :idVariant ORDER BY question_number", nativeQuery = true)
     List<Chars> getVariantAnswers(@Param("idVariant") int idVariant);


}
