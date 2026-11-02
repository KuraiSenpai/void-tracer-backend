package com.emperor.warframe.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.emperor.warframe.entity.WorldStateCache;

public interface WorldStateCacheRepository extends JpaRepository<WorldStateCache, Long> {

}
