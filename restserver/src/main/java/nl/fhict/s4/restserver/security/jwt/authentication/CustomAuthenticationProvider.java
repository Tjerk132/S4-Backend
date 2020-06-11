package nl.fhict.s4.restserver.security.jwt.authentication;

import objects.user.User;
import objects.user.UserInfo;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import repositories.UserRepository;
import util.HashSaltAuthentication;

import java.util.ArrayList;
import java.util.List;


public class CustomAuthenticationProvider implements AuthenticationProvider  {

    public CustomAuthenticationProvider() {
        this.repository = new UserRepository();
        this.aes = new AESAlgorithm();
    }

    private UserRepository repository;

    private AESAlgorithm aes;

    @Override
    public Authentication authenticate(Authentication authentication) {

        String name = authentication.getName();
        Object credentials = authentication.getCredentials();

        if (!(credentials instanceof String)) {
            return null;
        }
        String password = credentials.toString();

//        try {
//            name = aes.decrypt(name);
//            password = aes.decrypt(password);
//        }
//        catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException e) {
//            return null;
//        }

        User storedUser = repository.getByName(name);

        if(HashSaltAuthentication.check(password, storedUser.getPassword())) {
            List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
            grantedAuthorities.add(new SimpleGrantedAuthority(storedUser.getRole().toString()));

            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    new UserInfo(String.valueOf(storedUser.getId()), storedUser.getUsername()), authentication.getCredentials(), grantedAuthorities
            );
            SecurityContextHolder.getContext().setAuthentication(auth);

            return auth;

        }
        else throw new BadCredentialsException("Authentication failed for " + name);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
