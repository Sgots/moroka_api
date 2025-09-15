package com.springboot.backend.model;

// src/main/java/com/springboot/backend/api/dto/AttachPlanResponse.java

public record AttachPlanResponse(
        Long planId,
        Long fieldId,
        Integer areaId,
        Long patchId,
        String message
) {}
