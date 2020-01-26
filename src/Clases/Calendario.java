/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 *
 * @author avice
 */
public class Calendario {
    private DateFormat dateFormat;
    private Date date = new Date();
    private Calendar c = new GregorianCalendar();
    private String dia, mes, anyo, fecha, hora, minutos, segundos, am_pm;
    
    public Calendario() {
//        hora = Integer.toString(c.get(Calendar.HOUR));
//        minutos = Integer.toString(c.get(Calendar.MINUTE));
//        segundos = Integer.toString(c.get(Calendar.SECOND));
        switch(c.get(Calendar.AM_PM)) {
            case 0:
                am_pm = "am";
                break;
            case 1:
                am_pm = "pm";
                break;
        }
        dateFormat = new SimpleDateFormat("dd");
        dia = dateFormat.format(date);
        dateFormat = new SimpleDateFormat("MM");
        mes = dateFormat.format(date);
        dateFormat = new SimpleDateFormat("yyyy");
        anyo = dateFormat.format(date);
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        fecha = dateFormat.format(date);
        dateFormat = new SimpleDateFormat("hh:mm:ss");
        hora = dateFormat.format(date) + " " + am_pm; 
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
