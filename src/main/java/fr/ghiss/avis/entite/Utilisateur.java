package fr.ghiss.avis.entite;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "utilisateur")
public class Utilisateur implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "mot_de_passe")
    private String mdp;
    private String nom;
    private String email;
    private boolean isActif = false;
    @OneToOne(cascade = CascadeType.ALL)
    private  Role role;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.role.getLibelle().getAuthorities();
    }

    @Override
    public String getPassword() {
        return this.mdp;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.isActif;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.isActif;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.isActif;
    }

    @Override
    public boolean isEnabled() {
        return this.isActif;
    }
}
