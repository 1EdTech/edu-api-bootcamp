package org.oneedtech.eduapi.bootcamp.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class DiscoveryController {
    private static final String OPENAPI_FILE = "oas/eduapi_v1p0_openapi3.json";

    @GetMapping("/1edtech/eduapi/base/v1p0/discovery")
    public ResponseEntity<Resource> getDiscoveryDocument(HttpServletRequest request) {
        String acceptHeader = request.getHeader("Accept");
        if (acceptHeader == null || !acceptHeader.contains(MediaType.APPLICATION_JSON_VALUE)) {
            return ResponseEntity.status(406).build(); // 406 Not Acceptable
        }
        try {
            ClassPathResource resource = new ClassPathResource(OPENAPI_FILE);

            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(resource);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
