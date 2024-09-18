package com.citaa.citaa.service;


import com.citaa.citaa.config.JwtProvider;
import com.citaa.citaa.model.Startup;
import com.citaa.citaa.repository.StartupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StartupService {
    @Autowired
    private StartupRepository startupRepository;
    @Autowired
    private JwtProvider jwtProvider;

    public Startup getStartupByJwt(String jwt) throws Exception {
        String username = jwtProvider.getUsernameFromJwtToken(jwt);
        Startup startup = startupRepository.findByUsername(username);
        if (startup == null) {
            throw new Exception("Start up not found");
        }
        return startup;
    }
}
