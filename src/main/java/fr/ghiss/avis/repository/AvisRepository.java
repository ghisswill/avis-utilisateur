package fr.ghiss.avis.repository;

import fr.ghiss.avis.entite.Avis;
import org.springframework.data.repository.CrudRepository;

public interface AvisRepository extends CrudRepository<Avis, Integer> {
}
