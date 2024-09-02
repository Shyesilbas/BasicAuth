package org.example.basicauth.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CombinedUserDetailsService implements UserDetailsService {
    private final UserDetailsServiceImpl userDetailsService;
    private final CustomerDetailsServiceImpl customerDetailsService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            return userDetailsService.loadUserByUsername(username);
        } catch (UsernameNotFoundException ex) {
            return customerDetailsService.loadUserByUsername(username);
        }
    }

}
