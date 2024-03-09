package fr.ghiss.avis.service;

import fr.ghiss.avis.entite.Utilisateur;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Map;

public interface UtilisateurService extends UserDetailsService {

    void inscription(Utilisateur utilisateur);

    void activer(Map<String, String> activation);


}
