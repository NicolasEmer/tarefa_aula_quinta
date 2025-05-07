package com.example.tarefa.service;

import com.example.tarefa.entity.Tarefa;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailNotificationService {

    private final JavaMailSender mailSender;

    @Value("${app.admin.email}")
    private String adminEmail;

    public void sendTaskNotification(Tarefa tarefa, String action) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom("nicolasemer22@gmail.com");
        msg.setTo(adminEmail);
        msg.setSubject("Tarefa " + action + " – ID " + tarefa.getId());
        msg.setText(
                "Olá Admin,\n\n" +
                        "A tarefa '" + tarefa.getDescricao() + "'\n" +
                        "foi " + action.toLowerCase() + " com sucesso.\n\n" +
                        "Data prevista: " + tarefa.getDataPrevista() + "\n" +
                        "Situação: "     + tarefa.getSituacao()   + "\n\n" +
                        "— Sistema de Gestão de Tarefas"
        );
        mailSender.send(msg);
    }
}
