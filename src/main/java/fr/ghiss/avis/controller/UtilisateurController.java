package fr.ghiss.avis.controller;

import fr.ghiss.avis.dto.AuthentificationDTO;
import fr.ghiss.avis.entite.Utilisateur;
import fr.ghiss.avis.securite.JwtService;
import fr.ghiss.avis.service.UtilisateurService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
public class UtilisateurController {

    private AuthenticationManager authenticationManager;
    private UtilisateurService utilisateurService;
    private JwtService jwtService;

    @PostMapping("/inscription")
    public void inscription(@RequestBody Utilisateur utilisateur) {
        log.info("inscription");
        this.utilisateurService.inscription(utilisateur);
    }

    @PostMapping("/activation")
    public void activation(@RequestBody Map<String, String> activation) {
        log.info("inscription");
        this.utilisateurService.activer(activation);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<Map<String, String>> refreshToken(@RequestBody Map<String, String> refreshTokenRequest) {
        log.info("refresh token");
        return new ResponseEntity<>(this.jwtService.refreshToken(refreshTokenRequest), HttpStatus.OK);
    }

    @PostMapping("/connexion")
    public ResponseEntity<Map<String, String>> connexion(@RequestBody AuthentificationDTO authentificationDTO) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authentificationDTO.username(), authentificationDTO.password())
        );
        if(authenticate.isAuthenticated())
            return new ResponseEntity<>(this.jwtService.generate(authentificationDTO.username()), HttpStatus.OK);
        throw new SecurityException();
    }

    @PostMapping("/deconnexion")
    public void deconnexion() {
        log.info("deconnexion");
        this.jwtService.deconnexion();
    }

    @PostMapping("/modifier-mot-de-passe")
    public void modifierMotDePasse(@RequestBody Map<String, String> params) {
        this.utilisateurService.modifierMotDePasse(params);
    }

    @PostMapping("/nouveau-mot-de-passe")
    public void nouveauMotDePasse(@RequestBody Map<String, String> params) {
        this.utilisateurService.nouveauMotDePasse(params);
    }
}
