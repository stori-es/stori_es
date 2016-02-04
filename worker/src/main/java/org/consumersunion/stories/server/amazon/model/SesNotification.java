package org.consumersunion.stories.server.amazon.model;

public class SesNotification {
    private SesNotificationType notificationType;
    private Mail mail;
    private Bounce bounce;
    private Complaint complaint;
    private Delivery delivery;

    public SesNotificationType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(SesNotificationType notificationType) {
        this.notificationType = notificationType;
    }

    public Mail getMail() {
        return mail;
    }

    public void setMail(Mail mail) {
        this.mail = mail;
    }

    public Bounce getBounce() {
        return bounce;
    }

    public void setBounce(Bounce bounce) {
        this.bounce = bounce;
    }

    public Complaint getComplaint() {
        return complaint;
    }

    public void setComplaint(Complaint complaint) {
        this.complaint = complaint;
    }

    public Delivery getDelivery() {
        return delivery;
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
    }
}
