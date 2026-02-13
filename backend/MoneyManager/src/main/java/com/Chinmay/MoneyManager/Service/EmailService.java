package com.Chinmay.MoneyManager.Service;

public interface EmailService {
    void sendActivationEmail(String to, String subject, String text);
}
