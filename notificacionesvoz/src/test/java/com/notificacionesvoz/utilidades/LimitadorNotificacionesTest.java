package com.notificacionesvoz.utilidades;

import com.notificacionesvoz.dominio.modelo.TipoNotificacion;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests unitarios para LimitadorNotificaciones
 */
public class LimitadorNotificacionesTest {

    private LimitadorNotificaciones limitador;

    @Before
    public void setUp() {
        limitador = new LimitadorNotificaciones(1000); // 1 segundo de espera
    }

    @Test
    public void testPuedeNotificar_primerVez_retornaVerdadero() {
        assertTrue(limitador.puedeNotificar(TipoNotificacion.EXCESO_VELOCIDAD));
    }

    @Test
    public void testPuedeNotificar_dentroDelTiempoEspera_retornaFalso() {
        limitador.registrarNotificacion(TipoNotificacion.EXCESO_VELOCIDAD);
        assertFalse(limitador.puedeNotificar(TipoNotificacion.EXCESO_VELOCIDAD));
    }

    @Test
    public void testPuedeNotificar_despuesDelTiempoEspera_retornaVerdadero() throws InterruptedException {
        limitador.registrarNotificacion(TipoNotificacion.EXCESO_VELOCIDAD);
        Thread.sleep(1100); // Esperar más que el tiempo de espera
        assertTrue(limitador.puedeNotificar(TipoNotificacion.EXCESO_VELOCIDAD));
    }

    @Test
    public void testIntentarNotificar_primerVez_retornaVerdadero() {
        assertTrue(limitador.intentarNotificar(TipoNotificacion.EXCESO_VELOCIDAD));
    }

    @Test
    public void testIntentarNotificar_dentroDelTiempoEspera_retornaFalso() {
        limitador.intentarNotificar(TipoNotificacion.EXCESO_VELOCIDAD);
        assertFalse(limitador.intentarNotificar(TipoNotificacion.EXCESO_VELOCIDAD));
    }

    @Test
    public void testReiniciar_permiteNotificacionInmediata() {
        limitador.registrarNotificacion(TipoNotificacion.EXCESO_VELOCIDAD);
        assertFalse(limitador.puedeNotificar(TipoNotificacion.EXCESO_VELOCIDAD));
        
        limitador.reiniciar(TipoNotificacion.EXCESO_VELOCIDAD);
        assertTrue(limitador.puedeNotificar(TipoNotificacion.EXCESO_VELOCIDAD));
    }

    @Test
    public void testObtenerTiempoRestante_retornaValorCorrecto() {
        limitador.registrarNotificacion(TipoNotificacion.EXCESO_VELOCIDAD);
        long tiempoRestante = limitador.obtenerTiempoRestante(TipoNotificacion.EXCESO_VELOCIDAD);
        
        assertTrue(tiempoRestante > 0);
        assertTrue(tiempoRestante <= 1000);
    }

    @Test
    public void testDiferentesTipos_tiemposEsperaIndependientes() {
        limitador.registrarNotificacion(TipoNotificacion.EXCESO_VELOCIDAD);
        
        assertFalse(limitador.puedeNotificar(TipoNotificacion.EXCESO_VELOCIDAD));
        assertTrue(limitador.puedeNotificar(TipoNotificacion.FRENADA_BRUSCA));
    }
}
