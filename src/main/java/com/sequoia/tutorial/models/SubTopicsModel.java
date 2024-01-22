package com.sequoia.tutorial.models;

import jakarta.persistence.*;

@Entity
@Table(name = "subtopics")
public class SubTopicsModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "topics_id")
    private TopicsModel topicsID;
    @Column(name = "is_active")
    private Boolean active;

    public SubTopicsModel() {
    }

    public SubTopicsModel(Long id, String name, TopicsModel topicsID, Boolean active) {
        this.id = id;
        this.name = name;
        this.topicsID = topicsID;
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

    public TopicsModel getTopicsID() {
        return topicsID;
    }

    public void setTopicsID(TopicsModel topicsID) {
        this.topicsID = topicsID;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}

