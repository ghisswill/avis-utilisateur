package fr.ghiss.avis.securite;

import fr.ghiss.avis.entite.Jwt;
import fr.ghiss.avis.entite.Utilisateur;
import fr.ghiss.avis.repository.JwtRepository;
import fr.ghiss.avis.service.UtilisateurService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Transactional
@AllArgsConstructor
@Service
public class JwtService {

    public static final String BEARER = "bearer";
    private final String ENCRIPTION_KEY = "c98872e0fff13650ef330d9f851e267ded36776b87234b7c8daf1f4e41fb665d";
    private final UtilisateurService utilisateurService;
    private final JwtRepository jwtRepository;

    public Jwt tokenByValeur(String valeur) {
        return jwtRepository.findByValeurAndDesactiveAndExpire(valeur, false, false)
                .orElseThrow(()->new RuntimeException("Token inconnu"));
    }

    public Map<String, String> generate(String username) {
        Utilisateur utilisateur = (Utilisateur) this.utilisateurService.loadUserByUsername(username);
        disableToken(utilisateur);
        Map<String, String> jwtMap = this.generateJwt(utilisateur);
        Jwt jwt = Jwt.builder().valeur(jwtMap.get(BEARER)).desactive(false).expire(false).utilisateur(utilisateur).build();
        this.jwtRepository.save(jwt);
        return jwtMap;
    }

    public String extractUsername(String token) {
        return getClaim(token, Claims::getSubject);
    }

    public boolean isTokenExpired(String token) {
        Date expirationDate = getClaim(token, Claims::getExpiration);
        return expirationDate.before(new Date());
    }

    public void deconnexion() {
        Utilisateur utilisateur = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Jwt jwt = this.jwtRepository
                .findUtilisateurTokenValid(utilisateur.getEmail(), false, false)
                .orElseThrow(()-> new RuntimeException("Token invalide"));
        jwt.setDesactive(true);
        jwt.setExpire(true);
        jwtRepository.save(jwt);
    }

    private void disableToken(Utilisateur utilisateur) {
        List<Jwt> jwts = jwtRepository.findUtilisateurAllToken(utilisateur.getEmail())
                .map(jwt -> {
                    jwt.setExpire(true);
                    jwt.setDesactive(true);
                    return jwt;
                }).toList();
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
        return Map.of(BEARER, token);
    }

    private Key getKey() {
        //SignatureAlgorithm.ES512
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(ENCRIPTION_KEY))
                ;
    }

    @Scheduled(cron = "0 */1 * * * *")
    public void removeUselessJwt() {
        log.info("Suppression des token à {}", Instant.now());
        this.jwtRepository.deleteAllByExpireAndDesactive(true, true);
    }
}
