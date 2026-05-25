package com.github.ailton.userhub.domain;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ExternalProjectId implements Serializable {

    @Column(name="id", length=200)
    private String id;

    @Column(name="user_id", length=36)
    private String userId;
    
}
