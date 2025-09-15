package com.springboot.backend.model;

// src/main/java/com/springboot/backend/api/dto/AttachPlanRequest.java

public record AttachPlanRequest(
        Long planId,
        Long fieldId,   // optional
        Long areaId,    // optional
        Long patchId    // optional
) {}

