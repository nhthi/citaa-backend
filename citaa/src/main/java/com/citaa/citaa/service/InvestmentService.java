package com.citaa.citaa.service;

import com.citaa.citaa.model.Investment;
import com.citaa.citaa.model.Investor;
import com.citaa.citaa.model.Project;
import com.citaa.citaa.repository.InvestmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class InvestmentService {

    @Autowired
    UserService userService;
    @Autowired
    private InvestmentRepository investmentRepository;

    public Investment createInvestment(int projectId, int investorId, double amount, String details) {
        Investment investment = Investment.builder()
                .project(Project.builder().id(projectId).build())
                .investor(Investor.builder().id(investorId).build())
                .amount(amount)
                .investmentDate(LocalDateTime.now())
                .investmentDetails(details)
                .build();

        return investmentRepository.save(investment);
    }

    public Investment getById(int id) throws Exception {
        Optional<Investment> opt = investmentRepository.findById(id);

        if(opt.isEmpty()){
            throw new Exception("Not found !");
        }
        return opt.get();

    }

    public Investment feedback(int investmentId, String content) throws Exception {

        Investment investment = getById(investmentId);
        investment.setFeedBack(content);
        return investmentRepository.save(investment);
    }
}
