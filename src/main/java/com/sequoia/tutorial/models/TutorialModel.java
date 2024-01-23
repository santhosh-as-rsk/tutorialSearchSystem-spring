package com.sequoia.tutorial.models;
import jakarta.persistence.*;

@Entity
@Table(name = "tutorial")
public class TutorialModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String links;
    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "source_id")
    private SourceModel sourceId;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "subTopics_id")
    private SubTopicsModel subTopicsId;
    @Column(name = "is_active")
    private Boolean active;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLinks() {
        return links;
    }

    public void setLinks(String links) {
        this.links = links;
    }

    public SourceModel getSourceId() {
        return sourceId;
    }

    public void setSourceId(SourceModel sourceId) {
        this.sourceId = sourceId;
    }

    public SubTopicsModel getSubTopicsId() {
        return subTopicsId;
    }

    public void setSubTopicsId(SubTopicsModel subTopicsId) {
        this.subTopicsId = subTopicsId;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public TutorialModel(Long id, String links, SourceModel sourceId, SubTopicsModel subTopicsId, Boolean active) {
        this.id = id;
        this.links = links;
        this.sourceId = sourceId;
        this.subTopicsId = subTopicsId;
        this.active = active;
    }

    public TutorialModel() {
    }
}