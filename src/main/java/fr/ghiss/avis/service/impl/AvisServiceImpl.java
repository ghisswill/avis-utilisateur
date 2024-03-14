package fr.ghiss.avis.service.impl;

import fr.ghiss.avis.entite.Avis;
import fr.ghiss.avis.entite.Utilisateur;
import fr.ghiss.avis.repository.AvisRepository;
import fr.ghiss.avis.service.AvisService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class AvisServiceImpl implements AvisService {

    private final AvisRepository avisRepository;


    @Override
    public Avis creerAvis(Avis avis) {
        Utilisateur utilisateur = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        avis.setUtilisateur(utilisateur);
        return avisRepository.save(avis);
    }

    @Override
    public List<Avis> lsite() {
        List<Avis> liste = new ArrayList<>();
        for (Avis avis : avisRepository.findAll()) {
            liste.add(avis);
        }
        return liste;
    }
}
