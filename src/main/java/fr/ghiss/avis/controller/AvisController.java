package fr.ghiss.avis.controller;

import fr.ghiss.avis.entite.Avis;
import fr.ghiss.avis.entite.Utilisateur;
import fr.ghiss.avis.service.AvisService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(value = "avis", produces = MediaType.APPLICATION_JSON_VALUE)
public class AvisController {

    private final AvisService avisService;

    @PostMapping
    public ResponseEntity<Avis> creer(@RequestBody Avis avis) {
        return new ResponseEntity<>(avisService.creerAvis(avis), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMINISTRATEUR')")
    @GetMapping
    public ResponseEntity<List<Avis>> Liste() {
        Utilisateur principal = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(principal);
        return new ResponseEntity<>(avisService.lsite(), HttpStatus.CREATED);
    }
}
