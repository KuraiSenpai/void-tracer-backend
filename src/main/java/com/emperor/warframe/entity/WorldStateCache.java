package com.emperor.warframe.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "world_state_cache")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorldStateCache {
    @Id
    private Long id = 1L;

    @Column(columnDefinition = "TEXT")
    private String jsonData;
}
