package com.sequoia.tutorial.models;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tutorial")
public class TutorialModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String links;
    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "source_id")
    private SourceModel sourceId;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "subTopics_id")
    private SubTopicsModel subTopicsId;
    @Column(name = "is_active")
    private Boolean active;
}