package com.design.service.impl.padload;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.design.common.assist.DesignException;
import com.design.common.enums.DesignEx;
import com.google.common.base.Throwables;

@Service
public class MailUtil {
	
	private static Logger log = LoggerFactory.getLogger(MailUtil.class);
	
	@Value("${mail.send.account}")
    private String myEmailAccount;
	
	@Value("${mail.send.pwd}")
	private String myEmailPassword;
	
	@Value("${mail.host}")
    private String myEmailSMTPHost;
    
	@Value("${mail.protocol}")
    private String protocol;
	
	@Value("${mail.auth}")
    private boolean auth;
	
    private Session session;
    
    public Session getSession(){
    	if(session==null){
    		synchronized (this) {
    			Properties props = new Properties();
                props.setProperty("mail.transport.protocol",protocol);
                props.setProperty("mail.host", myEmailSMTPHost);
                props.setProperty("mail.smtp.auth", "true");
                session = Session.getDefaultInstance(props);
			}
            
    	}
    	return session;
    }
    
    public void transport(String subject,String content,String... receiveMailAccount){
    	Session session = getSession();
    	try {
			MimeMessage message = createMimeMessage(subject,content,receiveMailAccount);
			Transport transport = session.getTransport();
			transport.connect(myEmailAccount, myEmailPassword);
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
		} catch (Exception e) {
			log.info("邮件发送失败");
			log.info(Throwables.getStackTraceAsString(e));
			throw new DesignException(DesignEx.INTERNAL_ERROR);
		}
    }
    
	/**
     * 创建一封只包含文本的简单邮件
     *
     * @param session 和服务器交互的会话
     * @param sendMail 发件人邮箱
     * @param receiveMail 收件人邮箱
     * @return
	 * @throws MessagingException 
     */
    private MimeMessage createMimeMessage(String subject,String content,String[] receiveMails) throws MessagingException{
        // 1. 创建一封邮件
        MimeMessage message = new MimeMessage(getSession());

        try {
			// 2. From: 发件人
			message.setFrom(new InternetAddress(myEmailAccount, "尺寸", "UTF-8"));

			// 3. To: 收件人（可以增加多个收件人、抄送、密送）
			InternetAddress[] to_address = new InternetAddress[receiveMails.length];
			for(int i=0,len=receiveMails.length;i<len;i++){
				to_address[i] = new InternetAddress(receiveMails[i],null, "UTF-8");
			}
			message.setRecipients(MimeMessage.RecipientType.TO, to_address);
		} catch (UnsupportedEncodingException e) {
			
		}
        

        // 4. Subject: 邮件主题
        message.setSubject(subject, "UTF-8");

        // 5. Content: 邮件正文（可以使用html标签）
        message.setContent(content, "text/html;charset=UTF-8");

        // 6. 设置发件时间
        message.setSentDate(new Date());

        // 7. 保存设置
        message.saveChanges();

        return message;
    }
    
    public void transport2(String subject,String content,Map<String,String> map,String... receiveMailAccount){
    	Session session = getSession();
    	try {
			MimeMessage message = createMessage(subject,content,map,receiveMailAccount);
			Transport transport = session.getTransport();
			transport.connect(myEmailAccount, myEmailPassword);
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
		} catch (Exception e) {
			log.info("邮件发送失败");
			log.info(Throwables.getStackTraceAsString(e));
			throw new DesignException(DesignEx.INTERNAL_ERROR);
		}
    }
    
    public MimeMessage createMessage(String subject,String content,Map<String,String> map,String[] receiveMails) throws Exception{
        // 1. 创建一封邮件
        MimeMessage message = new MimeMessage(getSession());

        try {
			// 2. From: 发件人
			message.setFrom(new InternetAddress(myEmailAccount, "尺寸", "UTF-8"));

			// 3. To: 收件人（可以增加多个收件人、抄送、密送）
			for(String receiveMail:receiveMails){
				message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(receiveMail,null, "UTF-8"));
			}
		} catch (UnsupportedEncodingException e) {
			
		}
        

        // 4. Subject: 邮件主题
        message.setSubject(subject, "UTF-8");
        
        MimeMultipart allMultipart=new MimeMultipart();
        MimeBodyPart contentpart= createContent(content,map);
        allMultipart.addBodyPart(contentpart);
        // 5. Content: 邮件正文（可以使用html标签）
        message.setContent(allMultipart, "text/html;charset=UTF-8");

        // 6. 设置发件时间
        message.setSentDate(new Date());

        // 7. 保存设置
        message.saveChanges();

        return message;
    }
    
    public MimeBodyPart createContent(String content,Map<String,String> map)throws Exception{  
        //创建代表组合Mime消息的MimeMultipart对象，将该MimeMultipart对象保存到MimeBodyPart对象  
        MimeBodyPart contentPart=new MimeBodyPart();  
        MimeMultipart contentMultipart=new MimeMultipart("related");  
          
        //创建用于保存HTML正文的MimeBodyPart对象，并将它保存到MimeMultipart中  
        MimeBodyPart htmlbodypart=new MimeBodyPart();  
        htmlbodypart.setContent(content,"text/html;charset=UTF-8");  
        contentMultipart.addBodyPart(htmlbodypart);  
          
        if(map!=null && map.size()>0) {  
             Set<Entry<String,String>> set=map.entrySet();  
             for (Iterator<Entry<String,String>> iterator = set.iterator(); iterator.hasNext();) {  
                Entry<String,String> entry = iterator.next();  
                  
                //创建用于保存图片的MimeBodyPart对象，并将它保存到MimeMultipart中  
                MimeBodyPart gifBodyPart=new MimeBodyPart();  
                FileDataSource fds=new FileDataSource(entry.getValue());//图片所在的目录的绝对路径  
                  
                gifBodyPart.setDataHandler(new DataHandler(fds));  
                gifBodyPart.setContentID(entry.getKey());   //cid的值  
                contentMultipart.addBodyPart(gifBodyPart);  
            }  
        }  
          
        //将MimeMultipart对象保存到MimeBodyPart对象  
        contentPart.setContent(contentMultipart);  
        return contentPart;  
    }
}
