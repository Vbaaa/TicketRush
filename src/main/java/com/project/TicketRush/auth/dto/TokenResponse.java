package com.project.TicketRush.auth.dto;
public record TokenResponse(String accessToken, long expiresInSeconds) {}