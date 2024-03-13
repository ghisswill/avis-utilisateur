package fr.ghiss.avis.controller;

import fr.ghiss.avis.entite.Utilisateur;
import fr.ghiss.avis.service.UtilisateurService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("utilisateur")
@AllArgsConstructor
public class UtilisateurController {

    UtilisateurService utilisateurService;

    @PreAuthorize("hasAuthority('ADMINISTRATEUR_CREATE')")
    @GetMapping
    public ResponseEntity<List<Utilisateur>> liste () {
        return new ResponseEntity<>(this.utilisateurService.liste(), HttpStatus.OK);
    }
}
