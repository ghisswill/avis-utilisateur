package fr.ghiss.avis.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static fr.ghiss.avis.enums.TypePermission.*;

@AllArgsConstructor
public enum TypeDeRole {
    UTILISATEUR (
            Set.of(UTILISATEUR_CREATE_AVIS)
    ),
    ADMINISTRATEUR (
            Set.of(
                    ADMINISTRATEUR_CREATE,
                    ADMINISTRATEUR_READ,
                    ADMINISTRATEUR_UPDATE,
                    ADMINISTRATEUR_DELETE,

                    MANAGER_CREATE,
                    MANAGER_READ,
                    MANAGER_UPDATE,
                    MANAGER_DELETE_AVIS
            )
    ),
    MANAGER (
         Set.of(
                 MANAGER_CREATE,
                 MANAGER_READ,
                 MANAGER_UPDATE,
                 MANAGER_DELETE_AVIS
         )
    );

    @Getter
    final Set<TypePermission> permissions;

    public Collection<? extends GrantedAuthority> getAuthorities() {

        List<SimpleGrantedAuthority> grantedAuthorities = new ArrayList<>(this.permissions.stream().map(
                permission -> new SimpleGrantedAuthority(permission.name())).toList());

        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return grantedAuthorities;
    }
}
