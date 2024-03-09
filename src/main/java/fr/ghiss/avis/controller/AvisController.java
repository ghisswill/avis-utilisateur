package fr.ghiss.avis.controller;

import fr.ghiss.avis.entite.Avis;
import fr.ghiss.avis.service.AvisService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("avis")
public class AvisController {

    private final AvisService avisService;

    @PostMapping
    public ResponseEntity<Avis> creer(@RequestBody Avis avis) {
        return new ResponseEntity<>(avisService.creerAvis(avis), HttpStatus.CREATED);
    }
}
