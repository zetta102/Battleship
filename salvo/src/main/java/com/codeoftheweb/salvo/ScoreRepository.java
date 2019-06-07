package com.codeoftheweb.salvo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
interface ScoreRepository extends JpaRepository<Score, Long> {
    List<Score> findByScore(float score);
}