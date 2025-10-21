package com.notificacionesvoz.utilidades;

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
        limitador = new LimitadorNotificaciones(1000); // 1 segundo de enfriamiento
    }

    @Test
    public void testIntentarNotificar_primerVez_retornaVerdadero() {
        assertTrue(limitador.intentarNotificar("alerta"));
    }

    @Test
    public void testIntentarNotificar_dentroDelEnfriamiento_retornaFalso() {
        limitador.intentarNotificar("alerta");
        assertFalse(limitador.intentarNotificar("alerta"));
    }

    @Test
    public void testIntentarNotificar_despuesDelEnfriamiento_retornaVerdadero() throws InterruptedException {
        limitador.intentarNotificar("alerta");
        Thread.sleep(1100); // Esperar más que el período de enfriamiento
        assertTrue(limitador.intentarNotificar("alerta"));
    }

    @Test
    public void testIntentarNotificar_sinCategoria_usaPredeterminada() {
        assertTrue(limitador.intentarNotificar());
        assertFalse(limitador.intentarNotificar()); // Segunda vez debe fallar
    }

    @Test
    public void testReiniciar_permiteNotificacionInmediata() {
        limitador.intentarNotificar("alerta");
        assertFalse(limitador.intentarNotificar("alerta"));
        
        limitador.reiniciar("alerta");
        assertTrue(limitador.intentarNotificar("alerta"));
    }

    @Test
    public void testObtenerEnfriamientoRestante_retornaValorCorrecto() {
        limitador.intentarNotificar("alerta");
        long tiempoRestante = limitador.obtenerEnfriamientoRestante("alerta");
        
        assertTrue(tiempoRestante > 0);
        assertTrue(tiempoRestante <= 1000);
    }

    @Test
    public void testObtenerEnfriamientoRestante_sinNotificacion_retornaCero() {
        long tiempoRestante = limitador.obtenerEnfriamientoRestante("nueva_categoria");
        assertEquals(0, tiempoRestante);
    }

    @Test
    public void testDiferentesCategorias_enfriamientosIndependientes() {
        limitador.intentarNotificar("alerta");
        
        assertFalse(limitador.intentarNotificar("alerta"));
        assertTrue(limitador.intentarNotificar("recordatorio")); // Diferente categoría
    }

    @Test
    public void testReiniciarTodo_limpiaTodasLasCategorias() {
        limitador.intentarNotificar("alerta");
        limitador.intentarNotificar("recordatorio");
        
        assertFalse(limitador.intentarNotificar("alerta"));
        assertFalse(limitador.intentarNotificar("recordatorio"));
        
        limitador.reiniciarTodo();
        
        assertTrue(limitador.intentarNotificar("alerta"));
        assertTrue(limitador.intentarNotificar("recordatorio"));
    }
}
