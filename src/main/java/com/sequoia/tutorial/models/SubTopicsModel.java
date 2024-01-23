package com.sequoia.tutorial.models;

import jakarta.persistence.*;

@Entity
@Table(name = "subtopics")
public class SubTopicsModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String name;
    @ManyToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "topics_id")
    private TopicsModel topicsId;
    @Column(name = "is_active")
    private Boolean active;

    public SubTopicsModel() {
    }

    public SubTopicsModel(Long id, String name, TopicsModel topicsId, Boolean active) {
        this.id = id;
        this.name = name;
        this.topicsId = topicsId;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TopicsModel getTopicsId() {
        return topicsId;
    }

    public void setTopicsId(TopicsModel topicsID) {
        this.topicsId = topicsID;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}

