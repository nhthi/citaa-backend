package com.citaa.citaa.service;

import com.citaa.citaa.controller.FounderController;
import com.citaa.citaa.model.Founder;
import com.citaa.citaa.repository.FounderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FounderService {
    @Autowired
    FounderRepository founderRepository;

    public Founder createFounder(Founder founder){
        return founderRepository.save(founder);
    }

    public List<Founder> getFounders(){
        return founderRepository.findAll();
    }
}
