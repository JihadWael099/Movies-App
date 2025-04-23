package org.fawry.moviesapp.Service;

import org.fawry.moviesapp.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByName(username)
                .map(user -> {
                    GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().name());
                    return new org.springframework.security.core.userdetails.User(
                            user.getName(),
                            user.getPassword(),
                            List.of(authority)
                    );
                })
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }
}
