package nl.fhict.s4.restserver.security.jwt;

import enums.Role;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import objects.user.User;
import objects.user.UserInfo;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import repositories.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TokenAuthenticationService {

    public TokenAuthenticationService() {
        this.repository = new UserRepository();
    }

    private UserRepository repository;

    private long EXPIRATIONTIME = 1000 * 60 * 60 * 24 * 10; // 10 days

    private String secret = "ThisIsASecret";

    private String tokenPrefix = "Bearer";

    private String headerString = "Authorization";

    public void addAuthentication(HttpServletResponse response, String username) {

        // We generate a token now.
        String JWT = Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();

        response.addHeader(headerString, tokenPrefix + " " + JWT);
    }

    public Authentication getAuthentication(HttpServletRequest request) {

        String token = request.getHeader(headerString);

        if (token != null) {

            // parse the token.
            String username = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();

            if (username != null) // we managed to retrieve a user
            {
                User storedUser = repository.getByName(username);

                List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

                //registered
                if(storedUser.getRole() != null) {
                    grantedAuthorities.add(new SimpleGrantedAuthority(storedUser.getRole().toString()));
                }
                else grantedAuthorities.add(new SimpleGrantedAuthority(Role.USER.toString()));

                return new UsernamePasswordAuthenticationToken(
                        new UserInfo(String.valueOf(storedUser.getId()), storedUser.getUsername()), storedUser.getPassword(), grantedAuthorities
                );
            }
        }
        return null;
    }
}
