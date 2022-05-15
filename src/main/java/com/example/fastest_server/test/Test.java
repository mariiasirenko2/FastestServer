package com.example.fastest_server.test;

import com.example.fastest_server.question.Question;
import com.example.fastest_server.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "tests")
@Data
@NoArgsConstructor
public class Test {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column (name = "test_name")
    private String testName;

    @Column (name = "students_list")
    private String[] students;

    @ManyToOne (fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    @JsonIgnore
    private User owner;

    @OneToMany(mappedBy = "test", cascade = CascadeType.ALL)
    private Collection<Question> questions;

    public Test(String testName, User owner) {
        this.testName = testName;
        this.owner = owner;
    }

    public void setQuestionsKeys() {
        for (Question i : questions) {
            i.setTest(this);
        }
    }

    public void setStudents(List<String> students) {
        this.students = new String[students.size()];
        students.toArray(this.students);
    }
}
