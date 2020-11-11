package se.ecutb.uppgift2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import se.ecutb.uppgift2.entity.User;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class MyUserDetailsService implements UserDetailsService {

        @Autowired
        private UserService userService;

        @Override
        public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
            User user = userService.findByUserName(userName);
            if(user == null) {
                throw new UsernameNotFoundException("Cound not find " + userName);
            }
            return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassWord(), getGrantedAuthorities(user));
        }

        private Collection<GrantedAuthority> getGrantedAuthorities(User user){
            return user.getAcl().stream()
                    .map(authority -> new SimpleGrantedAuthority("ROLE_" + authority))
                    .collect(Collectors.toList());
        }

    }

