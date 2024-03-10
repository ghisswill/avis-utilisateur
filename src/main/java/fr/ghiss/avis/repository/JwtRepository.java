package fr.ghiss.avis.repository;

import fr.ghiss.avis.entite.Jwt;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.stream.Stream;

public interface JwtRepository extends CrudRepository<Jwt, Integer> {

    Optional<Jwt> findByValeurAndDesactiveAndExpire(String valeur, boolean desactive, boolean expire);

    @Query("FROM Jwt j WHERE j.desactive = :desactive AND j.expire = :expire AND j.utilisateur.email = :email")
    Optional<Jwt> findUtilisateurTokenValid(String email, boolean desactive, boolean expire);

    @Query("FROM Jwt j WHERE  j.utilisateur.email = :email")
    Stream<Jwt> findUtilisateurAllToken(String email);

}
