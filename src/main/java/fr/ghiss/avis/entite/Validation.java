package fr.ghiss.avis.entite;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "validation")
public class Validation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private Instant creation;
    private Instant expire;
    private Instant activation;
    private String code;
    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE})
    private Utilisateur utilisateur;
}
