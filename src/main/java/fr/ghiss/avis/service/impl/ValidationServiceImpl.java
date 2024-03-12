package fr.ghiss.avis.service.impl;

import fr.ghiss.avis.entite.Utilisateur;
import fr.ghiss.avis.entite.Validation;
import fr.ghiss.avis.repository.ValidationRepository;
import fr.ghiss.avis.service.NotificationService;
import fr.ghiss.avis.service.ValidationService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Random;

import static java.time.temporal.ChronoUnit.MINUTES;

@AllArgsConstructor
@Service
public class ValidationServiceImpl implements ValidationService {

    private ValidationRepository validationRepository;
    private NotificationService notificationService;

    @Override
    public void enregistrer(Utilisateur utilisateur) {
        Validation validation = new Validation();
        validation.setUtilisateur(utilisateur);
        Instant creation = Instant.now();
        validation.setCreation(creation);

        Instant expiration = creation.plus(10, MINUTES);
        validation.setExpire(expiration);

        Random random = new Random();
        int ransomInteger = random.nextInt(999999);
        String code = String.format("%6d", ransomInteger);
        validation.setCode(code);

        validationRepository.save(validation);
        notificationService.envoyer(validation);
    }

    @Override
    public  Validation lireEnfonctionDuCode(String code) {
        return validationRepository.findByCode(code).orElseThrow(()-> new RuntimeException("Votre code est invalide"));
    }

//    @Scheduled(cron = "0 */1 * * * *")
//    public void nettoyerTable() {
//        validationRepository.deleteAllByExpireBefore(Instant.now());
//    }
}
