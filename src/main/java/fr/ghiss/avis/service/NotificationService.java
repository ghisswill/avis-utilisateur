package fr.ghiss.avis.service;

import fr.ghiss.avis.entite.Validation;

public interface NotificationService {

    void envoyer(Validation validation);
}
