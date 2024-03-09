package fr.ghiss.avis.service.impl;

import fr.ghiss.avis.entite.Validation;
import fr.ghiss.avis.service.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    JavaMailSender javaMailSender;

    @Override
    public void envoyer(Validation validation) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("Ghiss WILL application");
        mailMessage.setTo(validation.getUtilisateur().getEmail());
        mailMessage.setSubject("Votre code d'activation");

        String text = String.format("Bonjour %s,<br /> Votre code d'activation est %s",
                validation.getUtilisateur().getNom(), validation.getCode());
        mailMessage.setText(text);

        javaMailSender.send(mailMessage);
    }
}
