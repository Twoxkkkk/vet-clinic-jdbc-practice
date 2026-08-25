package infrastructure.notification;

import application.shared.NotificationSender;
import domain.shared.Email;

public class EmailNotificationSender implements NotificationSender<Email> {

    @Override
    public void send(Email to, String subject, String body) {
        // TODO: Потенциальная отправка в брокер сообщений
        // Просто для шоукейса тут оставил да, я не хочу это делать
        // Пока что просто отправляю в System.out
        System.out.printf("[%s]%n%s%n%n%s", to.getValue(), subject, body);
    }

}
