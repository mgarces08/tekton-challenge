package com.tekton.challenge.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.Instant;

@Entity
@Table(name = "history")
public class History implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "endpoint", nullable = false)
    private String endpoint;

    @NotNull
    @Column(name = "params", nullable = false)
    private String params;

    @NotNull
    @Column(name = "response", nullable = false)
    private String response;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    public Long getId() {
        return id;
    }

    public History setId(Long id) {
        this.id = id;
        return this;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public History setEndpoint(String endpoint) {
        this.endpoint = endpoint;
        return this;
    }

    public String getParams() {
        return params;
    }

    public History setParams(String params) {
        this.params = params;
        return this;
    }

    public String getResponse() {
        return response;
    }

    public History setResponse(String response) {
        this.response = response;
        return this;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public History setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof History)) {
            return false;
        }
        return getId() != null && getId().equals(((History) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
