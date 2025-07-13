package com.ventinc.futbol_ranker.service;

import com.ventinc.futbol_ranker.model.UserPrincipal;
import com.ventinc.futbol_ranker.model.Users;
import com.ventinc.futbol_ranker.repo.UserRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailService implements UserDetailsService {

    private final UserRepo userRepo;

    public MyUserDetailService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    // Spring Security calls this method during authentication
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Find the user in the database by username
        Optional<Users> user = userRepo.findByUsername(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        // Convert Users entity to Spring Security's UserDetails format
        return new UserPrincipal(user.get());
    }
}
