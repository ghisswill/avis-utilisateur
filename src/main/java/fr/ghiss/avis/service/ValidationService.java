package fr.ghiss.avis.service;

import fr.ghiss.avis.entite.Utilisateur;
import fr.ghiss.avis.entite.Validation;

public interface ValidationService {

    void enregistrer(Utilisateur utilisateur);

    Validation lireEnfonctionDuCode(String code);
}
