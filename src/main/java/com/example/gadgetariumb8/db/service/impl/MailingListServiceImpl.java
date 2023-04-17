package com.example.gadgetariumb8.db.service.impl;

import com.example.gadgetariumb8.db.dto.response.SimpleResponse;
import com.example.gadgetariumb8.db.exception.exceptions.MessageSendingException;
import com.example.gadgetariumb8.db.model.MallingList;
import com.example.gadgetariumb8.db.model.MallingListSubscriber;
import com.example.gadgetariumb8.db.repository.MailingListRepository;
import com.example.gadgetariumb8.db.repository.MailingListSubscriberRepository;
import com.example.gadgetariumb8.db.service.MailingListService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MailingListServiceImpl implements MailingListService {

    private final MailingListRepository mailingListRepository;
    private final MailingListSubscriberRepository subscriberRepository;
    private final JavaMailSender javaMailSender;
    private final Configuration config;

    @Override
    public SimpleResponse sendEmail(MallingList mail) {
        List<MallingListSubscriber> subscriberList = subscriberRepository.findAll();

        Map<String, Object> model = new HashMap<>();
        model.put("description", mail.getDescription());
        model.put("image", mail.getImage());
        model.put("name", mail.getName());
        model.put("start", mail.getDateOfStart());
        model.put("finish", mail.getDateOfFinish());

        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            Template template = config.getTemplate("email-template.html");
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);

            for (MallingListSubscriber subscriber : subscriberList) {
                mimeMessageHelper.setTo(subscriber.getUserEmail());
                mimeMessageHelper.setText(html, true);
                mimeMessageHelper.setSubject("Gadgetarium");
                mimeMessageHelper.setFrom("ilyazovorozali08@gmail.com");
                javaMailSender.send(message);
            }
        } catch (IOException | TemplateException | MessagingException e) {
            throw new MessageSendingException("Ошибка при отправке сообщения!");
        }

        mailingListRepository.save(mail);
        return SimpleResponse.builder()
                .status(HttpStatus.OK)
                .description("Сообщение успешно отправлено всем подписчикам.")
                .build();
    }

    @Override
    public SimpleResponse subscribe(MallingListSubscriber subscriber) {
        subscriberRepository.save(subscriber);
        return SimpleResponse.builder()
                .status(HttpStatus.CREATED)
                .description("Подписчик успешно добавлен в базу данных.")
                .build();
    }

}
