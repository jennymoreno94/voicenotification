package com.notificacionesvoz.utilidades;

import com.notificacionesvoz.dominio.modelo.TipoNotificacion;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests unitarios para AnalizadorComportamientoConductor
 */
public class AnalizadorComportamientoConductorTest {

    private AnalizadorComportamientoConductor analizador;

    @Before
    public void setUp() {
        analizador = new AnalizadorComportamientoConductor();
    }

    @Test
    public void testEsExcesoVelocidad_sobreUmbral_retornaVerdadero() {
        assertTrue(analizador.esExcesoVelocidad(95, 80)); // 15 km/h sobre el límite
    }

    @Test
    public void testEsExcesoVelocidad_bajoUmbral_retornaFalso() {
        assertFalse(analizador.esExcesoVelocidad(85, 80)); // 5 km/h sobre el límite
    }

    @Test
    public void testEsFrenadaBrusca_bajoUmbral_retornaVerdadero() {
        assertTrue(analizador.esFrenadaBrusca(-9.0f));
    }

    @Test
    public void testEsFrenadaBrusca_sobreUmbral_retornaFalso() {
        assertFalse(analizador.esFrenadaBrusca(-5.0f));
    }

    @Test
    public void testEsAceleracionBrusca_sobreUmbral_retornaVerdadero() {
        assertTrue(analizador.esAceleracionBrusca(5.0f));
    }

    @Test
    public void testEsAceleracionBrusca_bajoUmbral_retornaFalso() {
        assertFalse(analizador.esAceleracionBrusca(3.0f));
    }

    @Test
    public void testEsGiroBrusco_sobreUmbral_retornaVerdadero() {
        assertTrue(analizador.esGiroBrusco(6.0f));
        assertTrue(analizador.esGiroBrusco(-6.0f));
    }

    @Test
    public void testEsGiroBrusco_bajoUmbral_retornaFalso() {
        assertFalse(analizador.esGiroBrusco(4.0f));
    }

    @Test
    public void testAnalizarAceleracion_frenadaBrusca() {
        TipoNotificacion tipo = analizador.analizarAceleracion(-9.0f, 2.0f);
        assertEquals(TipoNotificacion.FRENADA_BRUSCA, tipo);
    }

    @Test
    public void testAnalizarAceleracion_aceleracionBrusca() {
        TipoNotificacion tipo = analizador.analizarAceleracion(5.0f, 2.0f);
        assertEquals(TipoNotificacion.ACELERACION_BRUSCA, tipo);
    }

    @Test
    public void testAnalizarAceleracion_giroBrusco() {
        TipoNotificacion tipo = analizador.analizarAceleracion(2.0f, 6.0f);
        assertEquals(TipoNotificacion.GIRO_BRUSCO, tipo);
    }

    @Test
    public void testEstablecerUmbrales_valoresPersonalizados() {
        analizador.establecerUmbralVelocidad(20);
        analizador.establecerUmbralFrenadaBrusca(-10.0f);
        
        assertEquals(20, analizador.obtenerUmbralVelocidad());
        assertEquals(-10.0f, analizador.obtenerUmbralFrenadaBrusca(), 0.01f);
    }
}
