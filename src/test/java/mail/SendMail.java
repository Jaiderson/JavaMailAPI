package mail;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class SendMail {

	private final Properties properties = new Properties();	
	private final String servMail;
	private final int puerto;
	private final String mailEmisor;
	private final String password; 
	private Session session;

	/**
	 * Constructor para recibir las propiedades cuano se usa el protocolo SMTP.
	 */
	public SendMail(String servMail, int puerto, String mailEmisor, String password) {
		super();
		this.servMail = servMail;
		this.puerto = puerto;
		this.mailEmisor = mailEmisor;
		this.password = password;
	}
	
	private void init() { 
		properties.put("mail.smtp.mail.sender",this.mailEmisor);
		properties.put("mail.smtp.host", this.servMail);		
		properties.put("mail.smtp.port",this.puerto);		
		properties.put("mail.smtp.user", this.mailEmisor);
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
 
		session = Session.getDefaultInstance(properties);
		session.setDebug(true);
	}

	public void sendEmail(List<String> mailReceptors, String asunto, String mensaje, List<String> adjuntos) throws AddressException, MessagingException, IOException { 
		if(mailReceptors.isEmpty()){
			System.err.println("¡Lista de correos destinatarios vacia!");
			return;
		}
		
		init();
		MimeMessage message = new MimeMessage(session);
		message.setSubject(asunto);
		message.setFrom(new InternetAddress((String)properties.get("mail.smtp.mail.sender")));
		
		for(String receptor : mailReceptors){
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(receptor));			
		}
		
		MimeMultipart multiPart = this.addAttached(adjuntos, message);
		BodyPart body = new MimeBodyPart();
		body.setText(mensaje);
		multiPart.addBodyPart(body);
		message.setContent(multiPart);
		
		Transport trans = session.getTransport("smtp");
		trans.connect((String)properties.get("mail.smtp.user"), password);
		trans.sendMessage(message, message.getAllRecipients());
		trans.close();
	}
	
	private MimeMultipart addAttached(List<String> adjuntos, MimeMessage message) throws MessagingException, IOException {
		MimeMultipart multiParte = new MimeMultipart();
		
		if(adjuntos != null && !adjuntos.isEmpty()) {
			MimeBodyPart adjunto = null; 
			
			for(String ruta : adjuntos) {
				adjunto = new MimeBodyPart();
				adjunto.attachFile(ruta);
				multiParte.addBodyPart(adjunto);
			}
		}
	
		return multiParte;
	}
 	
}
