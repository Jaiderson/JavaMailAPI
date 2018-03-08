package mail;


import java.util.Date;
import util.Util;

public class Mensaje {
	
	private String fechaHoraEnvio;
	private String mailOrigen;
	private String asunto;
	private String contenidoMsn;
	
	public Mensaje(Date fechaEnvio, String mailOrigen, String asunto, String contenidoMsn) {
		super();
		this.fechaHoraEnvio = Util.getFecha(fechaEnvio);
		this.mailOrigen = mailOrigen;
		this.asunto = asunto;
		this.contenidoMsn = contenidoMsn;
	}

	public String getFechaHoraEnvio() {
		return fechaHoraEnvio;
	}
	
	public void setFechaHoraEnvio(String fechaHoraEnvio) {
		this.fechaHoraEnvio = fechaHoraEnvio;
	}
	
	public String getMailOrigen() {
		return mailOrigen;
	}
	
	public void setMailOrigen(String mailOrigen) {
		this.mailOrigen = mailOrigen;
	}
	
	public String getAsunto() {
		return asunto;
	}
	
	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}
	
	public String getContenidoMsn() {
		return contenidoMsn;
	}
	
	public void setContenidoMsn(String contenidoMsn) {
		this.contenidoMsn = contenidoMsn;
	}

	@Override
	public String toString() {
		return "Mensaje [fechaHoraEnvio=" + fechaHoraEnvio + ", mailOrigen=" + mailOrigen + ", asunto=" + asunto+ "]";
	}

}
