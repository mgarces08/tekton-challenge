package com.tekton.challenge.domain;

import com.tekton.challenge.domain.enumeration.ProcessStatus;
import com.tekton.challenge.domain.enumeration.ProcessType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.Instant;

@Entity
@Table(name = "failed_async_task")
public class FailedAsyncTask implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "process", nullable = false)
    private ProcessType process;

    @NotNull
    @Column(name = "error", nullable = false)
    private String error;

    @NotNull
    @Column(name = "data", nullable = false)
    private String data;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ProcessStatus status;

    @NotNull
    @Column(name = "retries", nullable = false)
    private Integer retries;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProcessType getProcess() {
        return process;
    }

    public void setProcess(ProcessType process) {
        this.process = process;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public ProcessStatus getStatus() {
        return status;
    }

    public void setStatus(ProcessStatus status) {
        this.status = status;
    }

    public Integer getRetries() {
        return retries;
    }

    public void setRetries(Integer retries) {
        this.retries = retries;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FailedAsyncTask)) {
            return false;
        }
        return getId() != null && getId().equals(((FailedAsyncTask) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
