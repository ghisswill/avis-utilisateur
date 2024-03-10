package fr.ghiss.avis.repository;

import fr.ghiss.avis.entite.Validation;
import org.springframework.data.repository.CrudRepository;

import java.time.Instant;
import java.util.Optional;

public interface ValidationRepository extends CrudRepository<Validation, Integer> {

    Optional<Validation> findByCode(String code);

    void deleteAllByExpireBefore(Instant now);
}
