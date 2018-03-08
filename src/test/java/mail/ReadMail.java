package mail;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.AndTerm;
import javax.mail.search.SearchTerm;

import com.google.common.collect.Lists;

import util.Util;


public class ReadMail {
	
	private final Properties properties = new Properties();
	private Filtro fecha;
	private final String popMail;
	private final int puerto;
	private final String mail;
	private final String password; 
	private Session session;

	public ReadMail(String popMail, int puerto, String mail, String password) {
		super();
		this.popMail = popMail;
		this.puerto = puerto;
		this.mail = mail;
		this.password = password;
	}
	
	private void init() { 
		properties.put("mail.imap.starttls.enable", "false");
		properties.put("mail.imap.socketFactory.class","javax.net.ssl.SSLSocketFactory" );
		properties.put("mail.imap.socketFactory.fallback", "false");
		properties.put("mail.imap.port",this.puerto);
		properties.put("mail.imap.socketFactory.port", this.puerto);
		
		session = Session.getDefaultInstance(properties);
		session.setDebug(false);
	}

	public List<Mensaje> findMail(List<Filtro> filtros) throws MessagingException, IOException{		
		this.init();
		
		Store store = session.getStore("imap");
		store.connect(this.popMail, this.mail, this.password);
		Folder folder = store.getFolder("INBOX");
		folder.open(Folder.READ_ONLY);
		
		List<Mensaje> result = filtrarMensajes(filtros, folder);
		
		folder.close(false);
        store.close();
        return result;
	}
	
	private SearchTerm[] getSearchTerm(List<Filtro> filtros){
		if(filtros ==null ||filtros.isEmpty()){
			return new SearchTerm[0];
		}
		
		SearchTerm[] result = new SearchTerm[filtros.size()];
		for(int x = 0; x < filtros.size(); x++){
			result[x] = filtros.get(x).getSearchTerm();

			if(filtros.get(x).tipo == Filtro.Tipo.FECHA){
				this.fecha = filtros.get(x); 
			}
		}
		
		return result;
	}
	
	private List<Mensaje> filtrarMensajes(List<Filtro> filtros, Folder folder) throws MessagingException, IOException{
		SearchTerm searchTerm = new AndTerm(getSearchTerm(filtros));
		Message mensajes[] = folder.search(searchTerm);
		List<Mensaje> result = null;
		
		if(mensajes != null && mensajes.length > 0){  
			 result = this.fecha == null ? getMensages(mensajes) : getMensagesFecha(mensajes);
		}
		return result;
	}
	
	private List<Mensaje> getMensages(Message mensajes[]) throws MessagingException, IOException{
		List<Mensaje> result = Lists.newArrayList();
		String mailOrigen = null, asunto = null;
		Date ahora = new Date();
		
		for(Message msn : mensajes) {
			mailOrigen = msn.getFrom()[0].toString();
			asunto = msn.getSubject().toString();
			Date fec = Util.getFecha(this.fecha.getFecha()); //No se le asigna valor a la propiedad fecha en ningún momento (desde prueba gestor de solicitud)
			
			if(msn.getReceivedDate().after(fec) && msn.getReceivedDate().before(ahora)){
				result.add(new Mensaje(msn.getSentDate(), mailOrigen, asunto, getTextMessage(msn)));
			}
		}
		return result;	
	}

	private List<Mensaje> getMensagesFecha(Message mensajes[]) throws MessagingException, IOException{
		 String mailOrigen = null, asunto = null;
		 Date ahora = new Date();
		 List<Mensaje> result = Lists.newArrayList();
		 System.out.println();
        for(Message msn : mensajes) {
	       	 mailOrigen = msn.getFrom()[0].toString();
	       	 asunto = msn.getSubject().toString();
       		 Date fec = Util.getFecha(this.fecha.getFecha());
       		 
       		 System.out.println("Antes: "+Util.getFecha(fec)+" - Recibido: "+Util.getFecha(msn.getReceivedDate())+" - Ahora: "+Util.getFecha(ahora));
       		 
       		 if(msn.getReceivedDate().before(ahora) && msn.getReceivedDate().after(fec)){
       			 result.add(new Mensaje(msn.getSentDate(), mailOrigen, asunto, getTextMessage(msn)));
       		 }
       	 }
        System.out.println();
        return result;
	}
	
	
	private String getTextMessage(Message msn) throws MessagingException, IOException {
	    String result = "";
	    if (msn.isMimeType("text/plain")) {
	        result = msn.getContent().toString();
	    } else if (msn.isMimeType("multipart/*")) {
	        MimeMultipart mimeMultipart = (MimeMultipart) msn.getContent();
	        result = getTextFromMimeMultipart(mimeMultipart);
	    }
	    return result;
	}	
	
	private String getTextFromMimeMultipart(MimeMultipart mimeMultipart)  throws MessagingException, IOException{
	    String result = "";
	    int count = mimeMultipart.getCount();
	    for (int i = 0; i < count; i++) {
	        BodyPart bodyPart = mimeMultipart.getBodyPart(i);
	        if (bodyPart.isMimeType("text/plain")) {
	            result = result + "\n" + bodyPart.getContent();
	            break;
	        } 
	        else if (bodyPart.isMimeType("text/html")) {
	            String html = (String) bodyPart.getContent();
	            result = result + "\n" + org.jsoup.Jsoup.parse(html).text();
	        } 
	        else if (bodyPart.getContent() instanceof MimeMultipart){
	            result = result + getTextFromMimeMultipart((MimeMultipart)bodyPart.getContent());
	        }
	    }
	    return result;
	}

}
