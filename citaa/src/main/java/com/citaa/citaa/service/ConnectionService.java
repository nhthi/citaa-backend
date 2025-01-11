package com.citaa.citaa.service;

import com.citaa.citaa.model.*;
import com.citaa.citaa.repository.ConnectionHistoryRepository;
import com.citaa.citaa.repository.ConnectionRequestRepository;
import com.citaa.citaa.response.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ConnectionService {

    @Autowired
    private ConnectionRequestRepository connectionRequestRepository;
    @Autowired
    private ConnectionHistoryRepository connectionHistoryRepository;
    @Autowired
    UserService userService;
    @Autowired
    ProjectService projectService;


    public MessageResponse createConnectionRequest(String jwt, int projectId,ConnectionInfo connectionInfo) throws Exception {
        User user = userService.findByJwt(jwt);
        Project project = projectService.findProjectById(projectId);
        ConnectionRequest request = new ConnectionRequest();
        request.setConnectionInfo(connectionInfo);
        request.setInvestor(user); // Gán ID nhà đầu tư
        request.setProject(project);   // Gán ID dự án
        request.setStatus("PENDING");
        request.setRequestDate(LocalDateTime.now());
        connectionRequestRepository.save(request);
        MessageResponse res = new MessageResponse();
        res.setStatus(200);
        res.setMessage("Gửi yêu cầu kết nối thành công");
        return res;
    }

    public MessageResponse respondToRequest(int requestId, String status,String response) {
        ConnectionRequest request = connectionRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy yêu cầu kết nối"));
        request.setStatus(status);
        request.setResponseDate(LocalDateTime.now());
        request.setResponse(response);
        connectionRequestRepository.save(request);

        ConnectionHistory history = new ConnectionHistory();
        history.setConnectionRequest(request);
        history.setAction(status);
        history.setActionDate(LocalDateTime.now());
        connectionHistoryRepository.save(history);
        MessageResponse res = new MessageResponse();
        res.setStatus(200);
        res.setMessage("Phàn hồi yêu cầu kết nối thành công");
        return res;
    }

    public List<ConnectionRequest> getConnectionRequestsByInvestor(String jwt) throws Exception {
        User user = userService.findByJwt(jwt);
        return connectionRequestRepository.findByUserId(user.getId());
    }
}
