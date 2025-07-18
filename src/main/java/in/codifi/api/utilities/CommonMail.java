package in.codifi.api.utilities;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import in.codifi.ambalal.config.ApplicationProperties;
import in.codifi.ambalal.entity.CredentialKey;

import in.codifi.ambalal.error.utility.ErrorCodeConstants;
import in.codifi.ambalal.error.utility.ErrorHandling;
import in.codifi.ambalal.error.utility.ErrorMessageConstants;
import in.codifi.ambalal.repository.CredentialKeyRepositiory;
import in.codifi.ambalal.repository.EmailTemplateRepository;
import in.codifi.api.utilities.CodifiUtil;
import in.codifi.api.utilities.CommonMethods;
import in.codifi.api.utilities.EkycConstants;
import in.codifi.ambalal.error.utility.MessageConstants;

@ApplicationScoped
public class CommonMail {
	
	@Inject
	EmailTemplateRepository emailTemplateRepository;
	
	@Inject
	CommonMethods commonMethods;

	@Inject
	ErrorHandling errorHandling;

	@Inject
	CredentialKeyRepositiory credentialKeyRepository;
	
	public String sendMail(List<String> mailIds, String subject, String msg) {
		StringBuilder builder = new StringBuilder();
		String success = EkycConstants.FAILED_MSG;
		try {
			Properties properties = new Properties();
			// Setup mail server
			properties.put(EkycConstants.CONST_MAIL_HOST, "mail1.ambalalshares.com");
			properties.put(EkycConstants.CONST_MAIL_USER, "ekyc");
			properties.put(EkycConstants.CONST_MAIL_PORT, "587");
			properties.put(EkycConstants.CONST_MAIL_SOC_FAC_PORT, "587");
			properties.put(EkycConstants.CONST_MAIL_AUTH, EkycConstants.TRUE);
			properties.put(EkycConstants.CONST_MAIL_DEBUG, EkycConstants.TRUE);
			properties.put(EkycConstants.CONST_MAIL_STARTTLS_ENABLE, EkycConstants.TRUE);
			properties.put(EkycConstants.CONST_MAIL_SSL_PROTOCOLS, EkycConstants.CONST_MAIL_TLS_V2);
			properties.put("mail.smtp.timeout", "60000"); // Timeout in milliseconds
			properties.put("mail.smtp.connectiontimeout", "60000");
			Session session = Session.getDefaultInstance(properties, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication("ekyc","24Apr@2025");
				}
			});
			try {
 
				System.out.println("the mail from" +"ekyc");
				System.out.println("the mail from" + "24Apr@2025"); // Must match the correct password
				System.out.println("the mail from" + "ekyc");
				builder.append(msg);
				MimeMessage message = new MimeMessage(session);
				message.setFrom(new InternetAddress("ekyc@ambalalshares.com"));
				message.addRecipients(Message.RecipientType.TO, getRecipients(mailIds));
				//message.addRecipients(Message.RecipientType.TO, "karthiga@codifi.in");
				message.setSubject(subject);
				BodyPart messageBodyPart1 = new MimeBodyPart();
				messageBodyPart1.setContent(builder.toString(), EkycConstants.CONSTANT_TEXT_HTML);
				Multipart multipart = new MimeMultipart();
				multipart.addBodyPart(messageBodyPart1);
				message.setContent(multipart);
				Transport.send(message);
				success = EkycConstants.SUCCESS_MSG;
				commonMethods.storeEmailLog(msg, subject, success, "sendMail", mailIds);
			} catch (MessagingException ex) {
				ex.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return success;
 
	}
 
	
	private static Address[] getRecipients(List<String> emails) {
		Address[] addresses = new Address[emails.size()];
		try {
			for (int i = 0; i < emails.size(); i++) {
				addresses[i] = new InternetAddress(emails.get(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
			}
		return addresses;
	}
}
