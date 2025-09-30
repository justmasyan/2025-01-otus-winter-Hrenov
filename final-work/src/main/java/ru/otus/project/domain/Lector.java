package ru.otus.project.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.List;
import java.util.Objects;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "lectors")
@Entity
public class Lector {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String firstName;

    private String lastName;

    private String jobTitle;

    @OneToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @Fetch(FetchMode.SUBSELECT)
    @ManyToMany(mappedBy = "lectors", fetch = FetchType.LAZY)
    private List<Course> courses;

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        Lector lector = (Lector) object;
        return id == lector.id && Objects.equals(firstName, lector.firstName) && Objects.equals(lastName, lector.lastName) && Objects.equals(jobTitle, lector.jobTitle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, jobTitle);
    }
}