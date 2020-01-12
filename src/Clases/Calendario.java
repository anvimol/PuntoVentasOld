/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 *
 * @author avice
 */
public class Calendario {

    Calendar c = new GregorianCalendar();
    private String dia, mes, anyo, fecha, hora, minutos, segundos, am_pm;
    
    public Calendario() {
        hora = Integer.toString(c.get(Calendar.HOUR));
        minutos = Integer.toString(c.get(Calendar.MINUTE));
        segundos = Integer.toString(c.get(Calendar.SECOND));
        switch(c.get(Calendar.AM_PM)) {
            case 0:
                am_pm = "am";
                break;
            case 1:
                am_pm = "pm";
                break;
        }
        dia = Integer.toString(c.get(Calendar.DAY_OF_MONTH));
        mes = Integer.toString(c.get(Calendar.MONTH) + 1);
        anyo = Integer.toString(c.get(Calendar.YEAR));
        fecha = dia + "/" + mes + "/" + anyo;
        hora += ":"+minutos+":"+segundos+" "+am_pm; 
    }

    public String getDia() {
        return dia;
    }

    public String getMes() {
        return mes;
    }

    public String getAnyo() {
        return anyo;
    }

    public String getFecha() {
        return fecha;
    }

    public String getHora() {
        return hora;
    }
    
    
}
