package fr.ghiss.avis.service;

import fr.ghiss.avis.entite.Avis;

import java.util.List;

public interface AvisService {

    Avis creerAvis(Avis avis);

    List<Avis> lsite();
}
