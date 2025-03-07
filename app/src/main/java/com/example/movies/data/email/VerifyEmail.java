package com.example.movies.data.email;

import android.annotation.SuppressLint;
import android.content.Context;

import com.example.movies.utils.Utils;

import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class VerifyEmail {

    //Variables
    @SuppressLint("StaticFieldLeak")
    private final Context mContext;
    private Session mSession;

    private final String mEmail;
    private final String mSubject;
    private final String mMessage;
    private ExecutorService executorService;

    //Constructor
    public VerifyEmail(Context mContext, String mEmail, String mSubject, String mMessage) {
        this.mContext = mContext;
        this.mEmail = mEmail;
        this.mSubject = mSubject;
        this.mMessage = mMessage;
    }


    public void sendEmail(){
        executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                //Creating properties
                Properties props = new Properties();

                //Configuring properties for gmail
                //If you are not using gmail you may need to change the values
                props.put("mail.smtp.host", "smtp.gmail.com");
                props.put("mail.smtp.socketFactory.port", "465");
                props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.port", "465");

                //Creating a new session
                mSession = Session.getDefaultInstance(props,
                        new javax.mail.Authenticator() {
                            //Authenticating the password
                            protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(Utils.EMAIL, Utils.PASSWORD);
                            }
                        });

                try {
                    //Creating MimeMessage object
                    MimeMessage mm = new MimeMessage(mSession);

                    //Setting sender address
                    mm.setFrom(new InternetAddress(Utils.EMAIL));
                    //Adding receiver
                    mm.addRecipient(Message.RecipientType.TO, new InternetAddress(mEmail));
                    //Adding subject
                    mm.setSubject(mSubject);
                    //Adding message
                    mm.setText(mMessage);
                    //Sending email
                    Transport.send(mm);

//                    BodyPart messageBodyPart = new MimeBodyPart();
//
//                    messageBodyPart.setText("File Điểm");
//
//                    Multipart multipart = new MimeMultipart();
//
//                    multipart.addBodyPart(messageBodyPart);
//
//                    messageBodyPart = new MimeBodyPart();
//
//                    String filePath = "C:\\Users\\duy khan\\Desktop\\text.txt";
//
//                    DataSource source = new FileDataSource(filePath);
//
//                    messageBodyPart.setDataHandler(new DataHandler(source));
//
//                    messageBodyPart.setFileName(filePath);
//
//                    multipart.addBodyPart(messageBodyPart);
//
//                    mm.setContent(multipart);

                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
