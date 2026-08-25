package application.shared;

// Под потенциальные дженерики идут все VO из ContactInfo
public interface NotificationSender<E> {

    void send(E to, String subject, String body);

}
