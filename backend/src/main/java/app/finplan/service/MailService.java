package app.finplan.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    @Value("${app.mail.from}")
    private String from;

    public void sendConfirmationCode(String to, String code) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom("admin@mixa.su");
        msg.setTo(to);
        msg.setSubject("Подтверждение регистрации");
        msg.setText("Ваш код подтверждения: " + code);
        mailSender.send(msg);
    }
}

