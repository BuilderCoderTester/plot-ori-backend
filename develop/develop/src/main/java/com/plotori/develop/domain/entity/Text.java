package com.plotori.develop.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "texts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Text {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String author;

    @Column(name = "original_passage", columnDefinition = "TEXT")
    private String originalPassage;

    @Column(name = "publication_year")
    private Integer publicationYear;

    private String genre;

    @Column(name = "canonical_status")
    private String canonicalStatus;  // e.g., "CANONICAL", "COMMUNITY"

    @OneToMany(mappedBy = "text", cascade = CascadeType.ALL)
    @Builder.Default
    private Set<Submission> submissions = new HashSet<>();
}
