package com.example.fastest_server.variant;


import com.example.fastest_server.variantquestion.VariantQuestion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "variants")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Variant {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column (name = "student_name")
    private String studentName;

    @Column (name = "mark")
    private int mark;

    @OneToMany(mappedBy = "variant", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<VariantQuestion>  variantQuestions;

    public void addVariantQuestion(VariantQuestion variantQuestion) {
        variantQuestions.add(variantQuestion);
    }

    public Variant(String studentName) {
        this.studentName = studentName;
        this.variantQuestions = new HashSet<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Variant variant = (Variant) o;
        return id == variant.id && mark == variant.mark && studentName.equals(variant.studentName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, studentName, mark);
    }
}

