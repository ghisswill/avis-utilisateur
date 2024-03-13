package fr.ghiss.avis;

import fr.ghiss.avis.entite.Role;
import fr.ghiss.avis.entite.Utilisateur;
import fr.ghiss.avis.enums.TypeDeRole;
import fr.ghiss.avis.repository.UtilisateurRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;

@AllArgsConstructor
@EnableScheduling
@SpringBootApplication
public class AvisUtilisateurApplication implements CommandLineRunner {

	UtilisateurRepository utilisateurRepository;
	PasswordEncoder passwordEncoder;
	public static void main(String[] args) {
		SpringApplication.run(AvisUtilisateurApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Utilisateur admin = Utilisateur.builder()
				.isActif(true)
				.nom("admin")
				.mdp(passwordEncoder.encode("admin"))
				.email("admin@test.fr")
				.role(Role.builder().libelle(TypeDeRole.ADMINISTRATEUR).build())
				.build();
		if(utilisateurRepository.findByEmail(admin.getEmail()).isEmpty())
			utilisateurRepository.save(admin);

		Utilisateur manger = Utilisateur.builder()
				.isActif(true)
				.nom("manger")
				.mdp(passwordEncoder.encode("manger"))
				.email("manger@test.fr")
				.role(Role.builder().libelle(TypeDeRole.MANAGER).build())
				.build();
//		if(utilisateurRepository.findByEmail(manger.getEmail()).isEmpty())
//			utilisateurRepository.save(manger);
	}
}
