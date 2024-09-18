package com.citaa.citaa.service;

import com.citaa.citaa.config.JwtProvider;
import com.citaa.citaa.model.*;
import com.citaa.citaa.repository.ExpertRepository;
import com.citaa.citaa.repository.InvestorRepository;
import com.citaa.citaa.repository.StartupRepository;
import com.citaa.citaa.repository.UserRepository;
import com.citaa.citaa.request.RequestUser;
import com.citaa.citaa.request.UpdateUserRequest;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    StartupRepository startupRepository;
    @Autowired
    ExpertRepository expertRepository;
    @Autowired
    InvestorRepository investorRepository;


    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User getProfile(String jwt) throws Exception {
        String username = jwtProvider.getUsernameFromJwtToken(jwt);
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new Exception("User not found");
        }
        return user;
    }

    public User findById(int id) throws Exception {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new Exception("User not found!"));
        switch (user.getAccount().getRole()) {
            case "ROLE_STARTUP" -> {
                Startup profile = startupRepository.findById(id).get();
                profile.getAccount().setUsername(null);
                profile.getAccount().setPassword(null);
                return profile;
            }
            case "ROLE_EXPERT" -> {
                Expert profile = expertRepository.findById(id).get();
                profile.getAccount().setUsername(null);
                profile.getAccount().setPassword(null);
                return profile;
            }
            case "ROLE_INVESTOR" -> {
                Investor profile = investorRepository.findById(id).get();
                profile.getAccount().setUsername(null);
                profile.getAccount().setPassword(null);
                return profile;
            }
        }
        return user;
    }

    public User updateUser(String jwt, UpdateUserRequest req) throws Exception {
        String username = jwtProvider.getUsernameFromJwtToken(jwt);
        User updateUser = userRepository.findByUsername(username);

        if (updateUser == null) {
            throw new Exception("User not found");
        }
        if (req.getFullName() != null) {
            updateUser.setFullName(req.getFullName());
        }
        if (req.getAddress() != null) {
            updateUser.setAddress(req.getAddress());
        }
        if (req.getEmail() != null) {
            updateUser.setEmail(req.getEmail());
        }
        if (req.isGender() != updateUser.isGender()) {
            updateUser.setGender(req.isGender());
        }
        if (req.getDob() != null) {
            updateUser.setDob(req.getDob());
        }
        if (req.getAvatar() != null) {
            updateUser.setAvatar(req.getAvatar());
        }
        if (req.getBio() != null) {
            updateUser.setBio(req.getBio());
        }
        if (req.getCoverPhoto() != null) {
            updateUser.setCoverPhoto(req.getCoverPhoto());
        }
        userRepository.save(updateUser);

        switch (updateUser.getAccount().getRole()) {
            case "ROLE_STARTUP" -> {
                Startup updateStartup = startupRepository.findById(updateUser.getId())
                        .orElseThrow(() -> new Exception("Startup not found!"));
                if (req.getStudentId() != null) {
                    updateStartup.setStudentId(req.getStudentId());
                }
                if (req.getCohort() != null) {
                    updateStartup.setCohort(req.getCohort());
                }
                if (req.getCollege() != null) {
                    updateStartup.setCollege(req.getCollege());
                }
                startupRepository.save(updateStartup);
                return updateStartup;
            }
            case "ROLE_EXPERT" -> {
                Expert updateExpert = expertRepository.findById(updateUser.getId())
                        .orElseThrow(() -> new Exception("Expert not found!"));
                if (req.getCollege() != null) {
                    updateExpert.setCollege(req.getCollege());
                }
                if (req.getField() != null) {
                    updateExpert.setField(req.getField());
                }
                if (req.getEducation() != null) {
                    updateExpert.setEducation(req.getEducation());
                }
                if (req.getCertifications() != null) {
                    updateExpert.setCertifications(req.getCertifications());
                }
                if (req.getExperienceYears() != updateExpert.getExperienceYears()) {
                    updateExpert.setExperienceYears(req.getExperienceYears());
                }
                expertRepository.save(updateExpert);
                return updateExpert;
            }
            case "ROLE_INVESTOR" -> {
                Investor updateInvestor = investorRepository.findById(updateUser.getId())
                        .orElseThrow(() -> new Exception("Investor not found!"));
                if (req.getRiskTolerance() != null) {
                    updateInvestor.setRiskTolerance(req.getRiskTolerance());
                }
                if (req.getField() != null) {
                    updateInvestor.setField(req.getField());
                }
                if (req.getCompanyName() != null) {
                    updateInvestor.setCompanyName(req.getCompanyName());
                }
                if (req.getInvestmentAmount() != 0) {
                    updateInvestor.setInvestmentAmount(req.getInvestmentAmount());
                }
                if (req.getExperienceYears() != updateInvestor.getExperienceYears()) {
                    updateInvestor.setExperienceYears(req.getExperienceYears());
                }
                investorRepository.save(updateInvestor);
                return updateInvestor;
            }
        }

        return updateUser;
    }
}
