package com.example.demo.util;

import com.example.demo.domain.User;
import com.example.demo.dto.request.user.ChangePasswordRequest;
import com.example.demo.exception.InvalidPasswordException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.repository.spec.UserRepository;
import com.example.demo.service.spec.UserService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailSender {
    private final JavaMailSender mailSender;
    private final UserRepository userRepository;
    private final Generator generator;
    private final BCryptPasswordEncoder passwordEncoder;

    public void sendSetPasswordEmail(String email) throws MessagingException, UserNotFoundException, InvalidPasswordException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User with " + email + " not found"));

        String randomPassword = generator.generatePassword();

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("Set Password");
        mimeMessageHelper.setText("""
        <div>
            <p>Ваш сгенерированный пароль: <b>%s</b></p>
        </div>
        """.formatted(randomPassword), true);

        user.setPassword(passwordEncoder.encode(randomPassword));
        userRepository.save(user);
        mailSender.send(mimeMessage);
    }
}
