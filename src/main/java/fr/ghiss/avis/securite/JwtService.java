package fr.ghiss.avis.securite;

import fr.ghiss.avis.entite.Utilisateur;
import fr.ghiss.avis.service.UtilisateurService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@AllArgsConstructor
@Service
public class JwtService {

    private final String ENCRIPTION_KEY = "c98872e0fff13650ef330d9f851e267ded36776b87234b7c8daf1f4e41fb665d";
    private UtilisateurService utilisateurService;

    public Map<String, String> generate(String username) {
        Utilisateur utilisateur = (Utilisateur) this.utilisateurService.loadUserByUsername(username);
        return this.generateJwt(utilisateur);
    }

    public String extractUsername(String token) {
        return getClaim(token, Claims::getSubject);
    }

    public boolean isTokenExpired(String token) {
        Date expirationDate = getClaim(token, Claims::getExpiration);
        return expirationDate.before(new Date());
    }

    private <T> T getClaim(String token, Function<Claims, T> function) {
        Claims claims = getAllClaims(token);
        return function.apply(claims);
    }

    private Claims getAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(this.getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    @Deprecated
    private Map<String, String> generateJwt(Utilisateur utilisateur) {
        long currentTimeMillis = System.currentTimeMillis();
        long expirationTimeMillis = currentTimeMillis + 30 * 60 * 1000;

        Map<String, Object> claims = Map.of(
                "nom", utilisateur.getNom(),
                Claims.EXPIRATION, new Date(expirationTimeMillis),
                Claims.SUBJECT, utilisateur.getEmail()
        );
        String token = Jwts.builder()
                .issuedAt(new Date(currentTimeMillis))
                .expiration(new Date(expirationTimeMillis))
                .subject(utilisateur.getEmail())
                .claims(claims)

                .signWith(this.getKey(), SignatureAlgorithm.HS256)
                .compact();
        return Map.of("bearer", token);
    }

    private Key getKey() {
        //SignatureAlgorithm.ES512
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(ENCRIPTION_KEY))
                ;
    }
}
