package com.example.fastest_server.variant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VariantRepository extends JpaRepository<Variant, Integer> {

    @Query(value = "SELECT * FROM variants WHERE id IN (" +
            "SELECT variant_id FROM variant_question WHERE question_id IN " +
            "(SELECT id FROM questions WHERE test_id = :idTest))", nativeQuery = true)
    public List<Variant> findByTestId(@Param("idTest") int idTest);

}
