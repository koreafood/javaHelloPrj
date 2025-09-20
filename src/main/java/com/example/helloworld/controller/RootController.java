package com.example.helloworld.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {

    @GetMapping("/")
    public String root() {
        return "Hello World Application is running! Try /api/hello or /api/hello/json";
    }

    @GetMapping("/info")
    public ApplicationInfo getApplicationInfo() {
        return new ApplicationInfo(
            "Hello World Application",
            "1.0.0",
            "Spring Boot REST API with WAR packaging",
            new String[]{"/api/hello", "/api/hello/json"}
        );
    }

    // Inner class for application info response
    public static class ApplicationInfo {
        private String name;
        private String version;
        private String description;
        private String[] endpoints;

        public ApplicationInfo(String name, String version, String description, String[] endpoints) {
            this.name = name;
            this.version = version;
            this.description = description;
            this.endpoints = endpoints;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String[] getEndpoints() {
            return endpoints;
        }

        public void setEndpoints(String[] endpoints) {
            this.endpoints = endpoints;
        }
    }
}