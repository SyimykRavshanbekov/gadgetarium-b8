package com.example.gadgetariumb8.db.service.impl;

import com.example.gadgetariumb8.db.dto.request.MailingListRequest;
import com.example.gadgetariumb8.db.dto.request.MailingListSubscriberRequest;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
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
@Slf4j
public class MailingListServiceImpl implements MailingListService {

    private final MailingListRepository mailingListRepository;
    private final MailingListSubscriberRepository subscriberRepository;
    private final JavaMailSender javaMailSender;
    private final Configuration config;

    @Override
    public SimpleResponse sendEmail(MailingListRequest mail) {
        log.info("Отправка электронной почты!");
        List<MallingListSubscriber> subscriberList = subscriberRepository.findAll();

        Map<String, Object> model = new HashMap<>();
        model.put("description", mail.description());
        model.put("image", mail.image());
        model.put("name", mail.name());
        model.put("start", mail.dateOfStart());
        model.put("finish", mail.dateOfFinish());

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
                mimeMessageHelper.setFrom("Gadgetarium@gmail.com");
                javaMailSender.send(message);
            }
        } catch (IOException | TemplateException | MessagingException e) {
            log.error("Ошибка при отправке сообщения!");
            throw new MessageSendingException("Ошибка при отправке сообщения!");
        }

        MallingList mallingList = MallingList.builder()
                .image(mail.image())
                .name(mail.name())
                .description(mail.description())
                .dateOfStart(mail.dateOfStart())
                .dateOfFinish(mail.dateOfFinish())
                .build();

        mailingListRepository.save(mallingList);
        log.info("Сообщение успешно отправлено всем подписчикам.");
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("Сообщение успешно отправлено всем подписчикам.")
                .build();
    }

    @Override
    public SimpleResponse subscribe(MailingListSubscriberRequest subscriber) {
        log.info("Subscribing");
        MallingListSubscriber mallingListSubscriber = MallingListSubscriber.builder()
                .userEmail(subscriber.userEmail())
                .build();

        subscriberRepository.save(mallingListSubscriber);
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(subscriber.userEmail());
        simpleMailMessage.setFrom("ilyazovorozali08@gmail.com");
        simpleMailMessage.setSubject("Gadgetarium");
        simpleMailMessage.setText("Здраствуйте! Вы успешно подписались на Gadgetarium");
        javaMailSender.send(simpleMailMessage);
        log.info("Подписчик успешно добавлен в базу данных.");
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.CREATED)
                .message("Подписчик успешно добавлен в базу данных.")
                .build();
    }

}
