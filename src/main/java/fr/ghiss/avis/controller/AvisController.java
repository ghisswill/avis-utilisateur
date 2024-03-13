package fr.ghiss.avis.controller;

import fr.ghiss.avis.entite.Avis;
import fr.ghiss.avis.entite.Utilisateur;
import fr.ghiss.avis.service.AvisService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("avis")
public class AvisController {

    private final AvisService avisService;

    @PostMapping
    public ResponseEntity<Avis> creer(@RequestBody Avis avis) {
        return new ResponseEntity<>(avisService.creerAvis(avis), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Avis>> liste() {
        System.out.println((Utilisateur)SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return new ResponseEntity<>(avisService.getAvis(), HttpStatus.OK);
    }
}
