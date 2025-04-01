package com.Nishkaiv_Apim_Devportal.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.Nishkaiv_Apim_Devportal.Service.ApiService;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api")
public class ApimController {

    @Autowired
    private  ApiService apiService;

    // ✅ Expose an endpoint to fetch APIs from WSO2
    @GetMapping("/getAll")
    public ResponseEntity<Map<String, Object>> fetchApis() {
        return apiService.fetchApis();
    }
}
