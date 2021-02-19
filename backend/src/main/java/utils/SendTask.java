package utils;

public class SendTask extends Thread {
    private String recepient;
    private String subject;
    private String messageContent;

    public SendTask(String recepient, String subject, String messageContent) {
        super();
        this.recepient = recepient;
        this.subject = subject;
        this.messageContent = messageContent;
    }

    public void run(){
        MailUtil.sendMail(recepient, subject, messageContent);
    }
}
