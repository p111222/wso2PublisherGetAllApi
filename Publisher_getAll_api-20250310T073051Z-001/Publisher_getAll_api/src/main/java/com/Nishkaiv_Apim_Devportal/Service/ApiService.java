// package com.Nishkaiv_Apim_Devportal.Service;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.*;
// import org.springframework.stereotype.Service;
// import org.springframework.web.client.RestTemplate;
// import com.fasterxml.jackson.databind.ObjectMapper;

// import java.util.Map;
// import java.util.concurrent.atomic.AtomicReference;

// @Service
// public class ApiService {

//     @Autowired
//     private RestTemplate restTemplate;
//     private final ObjectMapper objectMapper;

//     private final AtomicReference<String> token = new AtomicReference<>(); // Stores the token

//     public ApiService(ObjectMapper objectMapper) {
//         this.objectMapper = objectMapper;
//     }

//     /**
//      * Fetch APIs using the latest token
//      */
//     public ResponseEntity<Map<String, Object>> fetchApis() {
//         String apiUrl = "https://api.kriate.co.in:8344/api/am/publisher/v4/apis";
//         String apiToken = token.get();

//         if (apiToken == null || apiToken.isEmpty()) {
//             apiToken = fetchNewToken();
//             token.set(apiToken);
//         }

//         // Prepare request with Authorization header
//         HttpHeaders headers = new HttpHeaders();
//         headers.set("Authorization", "Bearer " + apiToken);
//         HttpEntity<String> entity = new HttpEntity<>(headers);

//         // Call API
//         ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, String.class);

//         // If token is expired (401), fetch a new token and retry
//         if (response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
//             apiToken = fetchNewToken();
//             token.set(apiToken);

//             headers.set("Authorization", "Bearer " + apiToken);
//             entity = new HttpEntity<>(headers);
//             response = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, String.class);
//         }

//         try {
//             Map<String, Object> jsonResponse = objectMapper.readValue(response.getBody(), Map.class);
//             return ResponseEntity.ok(jsonResponse);
//         } catch (Exception e) {
//             return ResponseEntity.status(500).body(Map.of("error", "Failed to parse JSON"));
//         }
//     }

//     /**
//      * Fetches a new token from the token API
//      */
//     private String fetchNewToken() {
//         String tokenUrl = "https://api.kriate.co.in:8344/oauth2/token";

//         HttpHeaders headers = new HttpHeaders();
//         headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//         headers.set("Authorization", "Basic YVFDcjR4ajhnVU9WUXBBcTFra3ozbWR5WkZvYTpmbHRZaHFrcG90NEY3R2VXZmp1QVRXU1BjY1lh");

//         String requestBody = "grant_type=password&username=admin&password=admin&scope=apim:api_create apim:api_manage";
//         HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

//         ResponseEntity<Map> response = restTemplate.exchange(tokenUrl, HttpMethod.POST, entity, Map.class);

//         if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
//             return response.getBody().get("access_token").toString();
//         } else {
//             throw new RuntimeException("Failed to fetch token");
//         }
//     }
// }

package com.Nishkaiv_Apim_Devportal.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

@Service
public class ApiService {

    @Autowired
    private RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    private String token; // Directly use a String to store the token

    public ApiService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Fetch APIs using the latest token
     */
    public ResponseEntity<Map<String, Object>> fetchApis() {
        String apiUrl = "https://api.kriate.co.in:8344/api/am/publisher/v4/apis";

        // Check and fetch a new token if not present or empty
        // if (token == null || token.isEmpty()) {
        token = fetchNewToken();
        System.out.println("fetchApi" + token);
        // }

        // Prepare request with Authorization header
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Call API
        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, String.class);

        // If token is expired (401), fetch a new token and retry
        if (response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            token = fetchNewToken();

            headers.set("Authorization", "Bearer " + token);
            entity = new HttpEntity<>(headers);
            response = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, String.class);
        }

        try {
            Map<String, Object> jsonResponse = objectMapper.readValue(response.getBody(), Map.class);
            return ResponseEntity.ok(jsonResponse);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Failed to parse JSON"));
        }
    }

    /**
     * Fetches a new token from the token API
     */
    private String fetchNewToken() {
        String tokenUrl = "https://api.kriate.co.in:8344/oauth2/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization",
                "Basic YVFDcjR4ajhnVU9WUXBBcTFra3ozbWR5WkZvYTpmbHRZaHFrcG90NEY3R2VXZmp1QVRXU1BjY1lh");

        String requestBody = "grant_type=password&username=admin&password=admin&scope=apim:api_create apim:api_manage";
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response = restTemplate.exchange(tokenUrl, HttpMethod.POST, entity, Map.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            String str = response.getBody().get("access_token").toString();

            System.out.println("fetchToken" + str);

            return response.getBody().get("access_token").toString();
        } else {
            throw new RuntimeException("Failed to fetch token");
        }
    }
}
