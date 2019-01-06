package com.embeddedmeng.message.thrift;

import com.embeddedmeng.messageapi.service.thrift.MessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.List;
import java.util.Properties;

@Service
public class MessageServiceImpl implements MessageService.Iface {

    @Value("${spring.mail.username}")
    private String MAIL_USER;   //邮件服务器登录用户名

    @Value("${spring.mail.password}")
    private String MAIL_PASSWORD;   //邮件服务器登录密码

    @Value("${spring.mail.username}")
    private String MAIL_FROM_SMTP;  //发送邮件地址

    @Value("${spring.mail.host}")
    private String MAIL_HOST;  //发送邮件SMTP host地址

    @Override
    public boolean sendEmail(List<String> toArray, String subject, String text, String filename, String filepath) {
        System.out.println("发送邮件");

        Properties props = new Properties();
        //设置服务器验证
        props.setProperty("mail.smtp.auth", "true");
        //设置传输协议
        props.setProperty("mail.transport.protocol", "smtp");
        //选择服务类型
        props.setProperty("mail.host", MAIL_HOST);
        //通过认证创建一个session实例
        Session session = Session.getInstance(props,
                new Authenticator()
                {
                    protected PasswordAuthentication getPasswordAuthentication()
                    {
                        return new PasswordAuthentication(MAIL_USER, MAIL_PASSWORD);
                    }
                }
        );
        //显示邮件发送过程中的交互信息
        session.setDebug(true);

        Message msg = new MimeMessage(session);
        Transport transport = null;
        try {
            //邮件发送人
            msg.setFrom(new InternetAddress(MAIL_FROM_SMTP));
            //邮件主题
            msg.setSubject(subject);
            //邮件内容
            msg.setText(text);
            int len = toArray.size();
            InternetAddress address[]=new InternetAddress[len];
            for (int i = 0; i < toArray.size(); i++) {
                address[i]=new InternetAddress(toArray.get(i));
            }
            //邮件接收方
            msg.addRecipients(Message.RecipientType.TO, address);

            //设置邮件消息内容、包含附件
            Multipart msgPart = new MimeMultipart();
            msg.setContent(msgPart);

            MimeBodyPart body = new MimeBodyPart(); //正文
//            MimeBodyPart attach = new MimeBodyPart(); //附件

            msgPart.addBodyPart(body);
//            msgPart.addBodyPart(attach);

            //设置正文内容
            body.setContent(text, "text/html;charset=utf-8");
            //设置附件内容
//            attach.setDataHandler(new DataHandler(new FileDataSource(filepath)));
//            attach.setFileName(filename);

            msg.saveChanges();

            transport.send(msg);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }finally{
            try {
                if(transport!=null){
                    transport.close();
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
