package com.contextsupport.notification.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.contextsupport.notification.dto.CreateNotificationRequest;
import com.contextsupport.notification.dto.NotificationResponse;
import com.contextsupport.notification.service.NotificationService;

@RestController
@RequestMapping
@Validated
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/notifications")
    public List<NotificationResponse> getNotifications(@AuthenticationPrincipal UserDetails userDetails) {
        return notificationService.getPendingNotifications();
    }

    @PostMapping("/notifications")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public NotificationResponse createNotification(@RequestBody @Validated CreateNotificationRequest request) {
        return notificationService.createNotification(request);
    }
}
