package com.example.fastest_server.variant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface VariantRepository extends JpaRepository<Variant, Integer> {

    @Query(value = "SELECT * FROM variants WHERE id IN (" +
            "SELECT variant_id FROM variant_question WHERE question_id IN " +
            "(SELECT id FROM questions WHERE test_id = :idTest)) ORDER BY student_name", nativeQuery = true)

    List<Variant> findByTestId(@Param("idTest") int idTest);

    @Modifying
    @Query(value = "update variants set mark = :mark where id= :idVariant", nativeQuery = true)
    void setMarkToVariant (@Param("idVariant") int idVariant, @Param("mark") int mark);

}
