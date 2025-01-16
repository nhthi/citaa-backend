package com.citaa.citaa.service;

import com.citaa.citaa.model.*;
import com.citaa.citaa.repository.ConnectionRequestRepository;
import com.citaa.citaa.response.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConnectionService {

    @Autowired
    private ConnectionRequestRepository connectionRequestRepository;
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

        MessageResponse res = new MessageResponse();
        res.setStatus(200);
        res.setMessage("Phàn hồi yêu cầu kết nối thành công");
        return res;
    }

    public Page<ConnectionRequest> getConnectionRequestsByInvestor(String jwt, String status, int year, int pageSize, int pageNumber,List<String> fields) throws Exception {
        User user = userService.findByJwt(jwt);
        List<ConnectionRequest> connections =  connectionRequestRepository.filterConnections(user.getId(),status,year);

        if (fields != null && !fields.isEmpty()) {
            connections = connections.stream()
                    .filter(connection -> fields.stream().anyMatch(field -> field.equalsIgnoreCase(connection.getProject().getField())))
                    .collect(Collectors.toList());
        }

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        int startIndex = (int) pageable.getOffset();
        int endIndex = Math.min(startIndex + pageable.getPageSize(), connections.size());

        // Kiểm tra startIndex và endIndex hợp lệ
        if (startIndex > endIndex || startIndex > connections.size()) {
            return Page.empty(pageable);
        }

        List<ConnectionRequest> pageContent = connections.subList(startIndex, endIndex);
        return new PageImpl<>(pageContent, pageable, connections.size());
    }

    public Page<ConnectionRequest> getConnectionRequestsByProjectId(int id, int pageSize, int pageNumber) throws Exception {

        List<ConnectionRequest> connections = connectionRequestRepository.findByProjectId(id);

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        int startIndex = (int) pageable.getOffset();
        int endIndex = Math.min(startIndex + pageable.getPageSize(), connections.size());

        // Kiểm tra startIndex và endIndex hợp lệ
        if (startIndex > endIndex || startIndex > connections.size()) {
            return Page.empty(pageable);
        }

        List<ConnectionRequest> pageContent = connections.subList(startIndex, endIndex);
        return new PageImpl<>(pageContent, pageable, connections.size());
    }

    public Page<ConnectionRequest> getConnectionRequestsByStartup(int userId,String status, int pageSize, int pageNumber) throws Exception {
        List<ConnectionRequest> connections = connectionRequestRepository.findByStartup(userId,status);

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        int startIndex = (int) pageable.getOffset();
        int endIndex = Math.min(startIndex + pageable.getPageSize(), connections.size());

        // Kiểm tra startIndex và endIndex hợp lệ
        if (startIndex > endIndex || startIndex > connections.size()) {
            return Page.empty(pageable);
        }

        List<ConnectionRequest> pageContent = connections.subList(startIndex, endIndex);
        return new PageImpl<>(pageContent, pageable, connections.size());
    }
    public List<Object[]> getTop3InvestorMostConnection(int year, int month) {
        List<Object[]> results = connectionRequestRepository.findTopInvestorWithConnections(year,month);
        return results.stream()
                .limit(3)
                .collect(Collectors.toList());
    }

}
