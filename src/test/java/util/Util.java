package util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Util {

	/**
	 * 
	 * @param fecha a formatear
	 * @return Fecha en formato dd/MM/yyyy HH:mm:ss
	 */
	public static String getFecha(Date fecha){
		if(fecha == null){
			return "";
		}
		
		LocalDateTime fec = LocalDateTime.ofInstant(new Date(fecha.getTime()).toInstant(), ZoneId.systemDefault());
		return fec.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
	}
	
	public static Date getFecha(LocalDateTime fecha){
		if(fecha == null){
			return null;
		}
		
	    Instant instant = fecha.atZone(ZoneId.systemDefault()).toInstant();
	    return Date.from(instant);		
	}
	
	
}
