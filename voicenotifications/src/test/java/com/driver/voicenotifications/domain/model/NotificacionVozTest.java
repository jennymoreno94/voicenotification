package com.driver.voicenotifications.domain.model;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests unitarios para NotificacionVoz
 */
public class NotificacionVozTest {

    @Test
    public void testConstructor_conDatosValidos_creaNotificacion() {
        NotificacionVoz notificacion = new NotificacionVoz.Constructor()
                .establecerTipo(TipoNotificacion.EXCESO_VELOCIDAD)
                .establecerMensaje("Mensaje de prueba")
                .establecerPrioridad(NotificacionVoz.Prioridad.ALTA)
                .construir();

        assertNotNull(notificacion);
        assertEquals(TipoNotificacion.EXCESO_VELOCIDAD, notificacion.obtenerTipo());
        assertEquals("Mensaje de prueba", notificacion.obtenerMensaje());
        assertEquals(NotificacionVoz.Prioridad.ALTA, notificacion.obtenerPrioridad());
    }

    @Test(expected = IllegalStateException.class)
    public void testConstructor_sinTipo_lanzaExcepcion() {
        new NotificacionVoz.Constructor()
                .establecerMensaje("Mensaje de prueba")
                .construir();
    }

    @Test(expected = IllegalStateException.class)
    public void testConstructor_sinMensaje_lanzaExcepcion() {
        new NotificacionVoz.Constructor()
                .establecerTipo(TipoNotificacion.EXCESO_VELOCIDAD)
                .construir();
    }

    @Test(expected = IllegalStateException.class)
    public void testConstructor_conMensajeVacio_lanzaExcepcion() {
        new NotificacionVoz.Constructor()
                .establecerTipo(TipoNotificacion.EXCESO_VELOCIDAD)
                .establecerMensaje("")
                .construir();
    }

    @Test
    public void testPrioridad_niveles() {
        assertEquals(0, NotificacionVoz.Prioridad.BAJA.obtenerNivel());
        assertEquals(1, NotificacionVoz.Prioridad.NORMAL.obtenerNivel());
        assertEquals(2, NotificacionVoz.Prioridad.ALTA.obtenerNivel());
        assertEquals(3, NotificacionVoz.Prioridad.URGENTE.obtenerNivel());
    }

    @Test
    public void testEquals_mismosDatos_retornaVerdadero() {
        long marcaTiempo = System.currentTimeMillis();
        
        NotificacionVoz notificacion1 = new NotificacionVoz.Constructor()
                .establecerTipo(TipoNotificacion.EXCESO_VELOCIDAD)
                .establecerMensaje("Prueba")
                .establecerMarcaTiempo(marcaTiempo)
                .construir();

        NotificacionVoz notificacion2 = new NotificacionVoz.Constructor()
                .establecerTipo(TipoNotificacion.EXCESO_VELOCIDAD)
                .establecerMensaje("Prueba")
                .establecerMarcaTiempo(marcaTiempo)
                .construir();

        assertEquals(notificacion1, notificacion2);
    }
}
