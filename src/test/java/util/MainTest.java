package util;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import com.google.common.collect.Lists;

import mail.*;

public class MainTest {
	
	public static void main(String[] args) throws AddressException, MessagingException, IOException{
		
		List<String> lista = Lists.newArrayList();
		lista.add("maria.canuta@hotmail.com");
		lista.add("teresa.gutierrez@gmail.com");
		//Puedes agregar n emails.
		
		enviarMail(lista);
		buscarMail();
	}
	
	private static void buscarMail() throws AddressException, MessagingException, IOException {
		
		System.out.println("Inicio Prueba Java leer mail");
		ReadMail readEmail = new ReadMail("imap.gmail.com", 993, "su.email@gmail.com", "Tu_Clave");
		
		List<Filtro> filtros = Lists.newArrayList();
		filtros.add(new Filtro("jaider.serrano@gmail.com.co", Filtro.Tipo.MAIL));
		filtros.add(new Filtro("Su codigo de seguridad es", Filtro.Tipo.MENSAJE)); // Es como un like busca el fragmento delmensaje que le pases.
		filtros.add(new Filtro(LocalDateTime.of(2017, 12, 19, 10, 31)));
		
		//Puedes agregar mas filtros,apoyate en la docuentacion de la carpeta help.
		
		List<Mensaje> msns = readEmail.findMail(filtros);
		for(Mensaje msn : msns){
			System.out.println(msn);
			System.out.println(msn.getContenidoMsn());
		}
		
	}
	
	private static void enviarMail(List<String> mailsReceptores) throws AddressException, MessagingException, IOException {
		System.out.println("Inicio Prueba enviar mail");
		
		List<String> adjuntos = Lists.newArrayList();
		adjuntos.add("E:/archivo 1.pdf");
		adjuntos.add("E:/archivo 2.docx");
		
		SendMail sendMail = new SendMail("smtp.gmail.com", 587, "su.email@gmail.com", "Tu_Clave");
		
		//Si no vas a enviar adjuntos envias null o unalista vacia.
		sendMail.sendEmail(mailsReceptores, "Asunto - Prueba", "Cuerpo mensaje: mail con adjuntos Verion 3", adjuntos);
		
		//Si no vas a enviar adjuntos envias null o unalista vacia como se muestra en la siguiente linea.
		//sendMail.sendEmail(mailsReceptores, "Asunto - Prueba", "Cuerpo mensaje: mail con adjuntos Verion 3", null);
		
	}
	
	
	
	
	
}
