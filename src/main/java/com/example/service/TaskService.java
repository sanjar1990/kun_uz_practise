package com.example.service;

import com.example.dto.TaskDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TaskService {
    private String url="http://localhost:8080/task";

    public void taskList(){
        RestTemplate restTemplate = new RestTemplate();
        TaskDTO dto=restTemplate.getForObject(url, TaskDTO.class);
    }
}
