package com.codeoftheweb.salvo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
interface PlayerRepository extends JpaRepository<Player, Long> {
    Player findByeMail(@Param("mail") String mail);
}
