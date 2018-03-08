package mail;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import javax.mail.search.BodyTerm;
import javax.mail.search.ComparisonTerm;
import javax.mail.search.FromStringTerm;
import javax.mail.search.ReceivedDateTerm;
import javax.mail.search.SearchTerm;
import javax.mail.search.SubjectTerm;
/**
 * La clase Filtro se usa para encapsularlos posibles parmetros de busqueda
 * de un correo electronico.
 * 
 * @author Jaider Adriam Serrano Sepulveda
 *
 */
public class Filtro {
	
	public enum Tipo {
		MAIL,
		ASUNTO,
		MENSAJE,
		FECHA;
	}
	
	public final Tipo tipo;
	private LocalDateTime fecha;
	private String filtro;
	
	public Filtro(String filtro, Tipo tipo){
		this.filtro = filtro;
		this.tipo = tipo;
	}
	
	public Filtro(LocalDateTime fecha){
		this.fecha = fecha;
		this.tipo = Tipo.FECHA;
	}

	public LocalDateTime getFecha() {
		return fecha;
	}

	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}

	public String getFiltro() {
		return filtro;
	}

	public void setFiltro(String filtro) {
		this.filtro = filtro;
	}
	
	public SearchTerm getSearchTerm(){
		SearchTerm result = null;
		switch(this.tipo){
			case MAIL:
				result = new FromStringTerm(this.filtro);
			break;
			
			case ASUNTO:
				result = new SubjectTerm(this.filtro);
			break;

			case MENSAJE:
				result = new BodyTerm(this.filtro);
			break;
			
			case FECHA:
				Date f = Date.from(fecha.atZone(ZoneId.systemDefault()).toInstant());
				result = new ReceivedDateTerm(ComparisonTerm.EQ, f);
			break;
		}
		
		return result;
	}

	@Override
	public String toString() {
		return "Filtro [tipo=" + tipo + ", fecha=" + fecha + ", filtro=" + filtro + "]";
	}

}
