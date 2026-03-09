package com.emperor.warframe.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.emperor.warframe.entity.WorldStateCache;

@Repository
public interface WorldStateCacheRepository extends JpaRepository<WorldStateCache, Long> {

}
