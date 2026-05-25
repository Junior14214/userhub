package com.github.ailton.userhub.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_user_external_project")
@Getter
@Setter
@Builder
@NoArgsConstructor
public class ExternalProject {

    @EmbeddedId
    private ExternalProjectId id;

    @Column(nullable=false, length=120)
    private String name;

    @ManyToOne(fetch=FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name="user_id")
    private User user;

    @Builder
    public ExternalProject(ExternalProjectId id, String name, User user){
        this.id = id;
        this.name = name;
        this.user = user;
    }
    
}
