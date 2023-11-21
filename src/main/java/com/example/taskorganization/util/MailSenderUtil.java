package com.example.taskorganization.util;

import com.example.taskorganization.annotation.Log;
import com.example.taskorganization.dao.entity.UserEntity;
import com.example.taskorganization.model.dto.MailDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpRequest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

@RequiredArgsConstructor
@Component
@Log
public class MailSenderUtil {

    private final JavaMailSender javaMailSender;

    @Async
    public void sendMail(MailDto mailDto) throws MessagingException, UnsupportedEncodingException {

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("no-reply", "no-reply");
        helper.setTo(mailDto.getToAddress());
        helper.setSubject(mailDto.getSubject());

        helper.setText(mailDto.getContent(), true);

        javaMailSender.send(message);


    }


}
