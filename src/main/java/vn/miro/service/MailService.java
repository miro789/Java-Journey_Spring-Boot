package vn.miro.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.from}")
    private String emailFrom;

    public String sendEmail(String recipients, String subject, String content, MultipartFile[] files) throws MessagingException {
        log.info("Sending ...");

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UFT=8");
        helper.setFrom(emailFrom); // TODO assign name

        if (recipients.contains(", ")) {
            helper.setTo(InternetAddress.parse(recipients));
        } else {
            helper.setTo(recipients);
        }

        if(files != null) {
            for (MultipartFile file: files) {
                helper.addAttachment(Objects.requireNonNull(file.getOriginalFilename()), file);
            }
        }

        helper.setSubject(subject);
        helper.setText(content, true);

        mailSender.send(message);
        log.info("Email has been send successfully, recipients={}", recipients);

        return "sent";

    }


}
