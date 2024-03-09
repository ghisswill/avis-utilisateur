package fr.ghiss.avis.service.impl;

import fr.ghiss.avis.TypeDeRole;
import fr.ghiss.avis.entite.Role;
import fr.ghiss.avis.entite.Utilisateur;
import fr.ghiss.avis.entite.Validation;
import fr.ghiss.avis.repository.UtilisateurRepository;
import fr.ghiss.avis.service.UtilisateurService;
import fr.ghiss.avis.service.ValidationService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UtilisateurServiceImpl implements UtilisateurService {

    private UtilisateurRepository utilisateurRepository;
    private BCryptPasswordEncoder passwordEncoder;
    private ValidationService validationService;

    @Override
    public void inscription(Utilisateur utilisateur) {

        if(!utilisateur.getEmail().contains("@") && !utilisateur.getEmail().contains("."))
            throw new RuntimeException("votre mail est invalide");

        Optional<Utilisateur> utilisateurOptional = this.utilisateurRepository.findByEmail(utilisateur.getEmail());
        if(utilisateurOptional.isPresent())
            throw new RuntimeException("Votre email est deja utilisé");

        String mdpCrypte = this.passwordEncoder.encode(utilisateur.getPassword());
        utilisateur.setMdp(mdpCrypte);

        Role roleUtilisateur = new Role();
        roleUtilisateur.setLibelle(TypeDeRole.UTILISATEUR);
        utilisateur.setRole(roleUtilisateur);

        utilisateur = this.utilisateurRepository.save(utilisateur);
        this.validationService.enregistrer(utilisateur);
    }

    @Override
    public void activer(Map<String, String> activation) {
        Validation validation = validationService.lireEnfonctionDuCode(activation.get("code"));

        if(Instant.now().isAfter(validation.getExpire()))
            throw new RuntimeException("Votre code a expiré");

        Utilisateur utilisateurActive = utilisateurRepository
                .findById(validation.getUtilisateur().getId())
                .orElseThrow(() -> new RuntimeException("Utilisateur inconnu"));
        utilisateurActive.setActif(true);
        utilisateurRepository.save(utilisateurActive);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.utilisateurRepository
                .findByEmail(username)
                .orElseThrow(()-> new UsernameNotFoundException("Aucun utilisateur ne correspond à cet identifiant " + username));
    }
}
