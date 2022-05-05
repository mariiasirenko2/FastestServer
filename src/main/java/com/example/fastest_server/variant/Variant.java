package com.example.fastest_server.variant;


import com.example.fastest_server.variantquestion.VariantQuestion;
import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "variants")
@Data
public class Variant {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column (name = "student_name")
    private String studentName;

    @Column (name = "blanks")
    private String blanks;

    @Column (name = "mark")
    private int mark;

    @Column (name = "blank_scan")
    private String blankScan;

    @OneToMany(mappedBy = "variant")
    private Set<VariantQuestion>  variantQuestions;
}
