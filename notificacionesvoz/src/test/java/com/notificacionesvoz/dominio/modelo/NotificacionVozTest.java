package com.notificacionesvoz.dominio.modelo;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests unitarios para NotificacionVoz
 */
public class NotificacionVozTest {

    @Test
    public void testConstructor_conDatosValidos_creaNotificacion() {
        NotificacionVoz notificacion = new NotificacionVoz.Constructor()
                .establecerMensaje("Mensaje de prueba")
                .establecerPrioridad(NotificacionVoz.Prioridad.ALTA)
                .establecerCategoria("test")
                .construir();

        assertNotNull(notificacion);
        assertEquals("Mensaje de prueba", notificacion.obtenerMensaje());
        assertEquals(NotificacionVoz.Prioridad.ALTA, notificacion.obtenerPrioridad());
        assertEquals("test", notificacion.obtenerCategoria());
    }

    @Test
    public void testConstructor_sinCategoria_creaNotificacion() {
        // La categoría es opcional
        NotificacionVoz notificacion = new NotificacionVoz.Constructor()
                .establecerMensaje("Mensaje de prueba")
                .construir();
        
        assertNotNull(notificacion);
        assertNull(notificacion.obtenerCategoria());
    }

    @Test(expected = IllegalStateException.class)
    public void testConstructor_sinMensaje_lanzaExcepcion() {
        new NotificacionVoz.Constructor()
                .establecerCategoria("test")
                .construir();
    }

    @Test(expected = IllegalStateException.class)
    public void testConstructor_conMensajeVacio_lanzaExcepcion() {
        new NotificacionVoz.Constructor()
                .establecerCategoria("test")
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
                .establecerMensaje("Prueba")
                .establecerCategoria("test")
                .establecerMarcaTiempo(marcaTiempo)
                .construir();

        NotificacionVoz notificacion2 = new NotificacionVoz.Constructor()
                .establecerMensaje("Prueba")
                .establecerCategoria("test")
                .establecerMarcaTiempo(marcaTiempo)
                .construir();

        assertEquals(notificacion1, notificacion2);
    }
}
