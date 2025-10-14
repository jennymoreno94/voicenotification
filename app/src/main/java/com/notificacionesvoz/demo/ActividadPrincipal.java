package com.notificacionesvoz.demo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.notificacionesvoz.dominio.modelo.TipoNotificacion;
import com.notificacionesvoz.dominio.modelo.ConfiguracionVoz;
import com.notificacionesvoz.presentacion.GestorNotificacionesVoz;
import com.notificacionesvoz.utilidades.AnalizadorComportamientoConductor;
import com.notificacionesvoz.utilidades.LimitadorNotificaciones;

import java.util.Locale;

/**
 * Actividad de demostración de la librería de notificaciones de voz
 */
public class ActividadPrincipal extends AppCompatActivity {

    private GestorNotificacionesVoz gestorVoz;
    private LimitadorNotificaciones limitador;
    private AnalizadorComportamientoConductor analizador;
    
    private TextView textoEstado;
    private Button botonExcesoVelocidad;
    private Button botonFrenadaBrusca;
    private Button botonAceleracionBrusca;
    private Button botonGiroBrusco;
    private Button botonSimularConduccion;
    private Button botonDetener;

    @Override
    protected void onCreate(Bundle estadoGuardado) {
        super.onCreate(estadoGuardado);
        setContentView(R.layout.activity_main);

        // Inicializar vistas
        inicializarVistas();

        // Inicializar componentes
        gestorVoz = GestorNotificacionesVoz.obtenerInstancia(this);
        limitador = new LimitadorNotificaciones(10000); // 10 segundos de cooldown
        analizador = new AnalizadorComportamientoConductor();

        // Registrar observador de ciclo de vida
        getLifecycle().addObserver(gestorVoz);

        // Configurar voz
        configurarVoz();

        // Observar eventos
        observarEventosVoz();

        // Configurar botones
        configurarBotones();

        // Actualizar estado
        actualizarEstado();
    }

    private void inicializarVistas() {
        textoEstado = findViewById(R.id.textStatus);
        botonExcesoVelocidad = findViewById(R.id.btnSpeedExcess);
        botonFrenadaBrusca = findViewById(R.id.btnHarshBraking);
        botonAceleracionBrusca = findViewById(R.id.btnHarshAcceleration);
        botonGiroBrusco = findViewById(R.id.btnSharpTurn);
        botonSimularConduccion = findViewById(R.id.btnSimulateDriving);
        botonDetener = findViewById(R.id.btnStop);
    }

    private void configurarVoz() {
        ConfiguracionVoz configuracion = ConfiguracionVoz.constructor()
                .establecerIdioma(new Locale("es", "ES"))
                .establecerVelocidadVoz(1.0f)
                .establecerTonoVoz(1.0f)
                .establecerHabilitado(true)
                .construir();

        gestorVoz.configurar(configuracion);
    }

    private void observarEventosVoz() {
        gestorVoz.obtenerEventos().observe(this, new Observer<GestorNotificacionesVoz.EventoNotificacion>() {
            @Override
            public void onChanged(GestorNotificacionesVoz.EventoNotificacion evento) {
                String mensaje = "Evento: " + evento.obtenerTipo() + " - " + evento.obtenerMensaje();
                textoEstado.setText(mensaje);
                
                if (evento.obtenerTipo() == GestorNotificacionesVoz.EventoNotificacion.Tipo.ERROR) {
                    Toast.makeText(ActividadPrincipal.this, 
                        "Error: " + evento.obtenerMensaje(), 
                        Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void configurarBotones() {
        botonExcesoVelocidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reproducirExcesoVelocidad();
            }
        });

        botonFrenadaBrusca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reproducirNotificacion(TipoNotificacion.FRENADA_BRUSCA);
            }
        });

        botonAceleracionBrusca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reproducirNotificacion(TipoNotificacion.ACELERACION_BRUSCA);
            }
        });

        botonGiroBrusco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reproducirNotificacion(TipoNotificacion.GIRO_BRUSCO);
            }
        });

        botonSimularConduccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simularConduccion();
            }
        });

        botonDetener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gestorVoz.detener();
                textoEstado.setText("Reproducción detenida");
            }
        });
    }

    private void reproducirExcesoVelocidad() {
        if (limitador.intentarNotificar(TipoNotificacion.EXCESO_VELOCIDAD)) {
            gestorVoz.reproducirExcesoVelocidad(120, 80);
            actualizarEstado();
        } else {
            long tiempoRestante = limitador.obtenerTiempoRestante(TipoNotificacion.EXCESO_VELOCIDAD);
            Toast.makeText(this, 
                "Espere " + (tiempoRestante / 1000) + " segundos", 
                Toast.LENGTH_SHORT).show();
        }
    }

    private void reproducirNotificacion(TipoNotificacion tipo) {
        if (limitador.intentarNotificar(tipo)) {
            gestorVoz.reproducir(tipo);
            actualizarEstado();
        } else {
            long tiempoRestante = limitador.obtenerTiempoRestante(tipo);
            Toast.makeText(this, 
                "Espere " + (tiempoRestante / 1000) + " segundos", 
                Toast.LENGTH_SHORT).show();
        }
    }

    private void simularConduccion() {
        Toast.makeText(this, "Simulando conducción...", Toast.LENGTH_SHORT).show();
        
        final Handler manejador = new Handler(Looper.getMainLooper());
        
        // Simular exceso de velocidad
        manejador.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (analizador.esExcesoVelocidad(120, 80)) {
                    reproducirExcesoVelocidad();
                }
            }
        }, 1000);
        
        // Simular frenada brusca
        manejador.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (analizador.esFrenadaBrusca(-9.0f)) {
                    reproducirNotificacion(TipoNotificacion.FRENADA_BRUSCA);
                }
            }
        }, 5000);
        
        // Simular aceleración brusca
        manejador.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (analizador.esAceleracionBrusca(5.0f)) {
                    reproducirNotificacion(TipoNotificacion.ACELERACION_BRUSCA);
                }
            }
        }, 9000);
    }

    private void actualizarEstado() {
        String estado = "Estado: ";
        if (gestorVoz.estaReproduciendo()) {
            estado += "Reproduciendo";
        } else if (gestorVoz.estaDisponible()) {
            estado += "Listo";
        } else {
            estado += "No disponible";
        }
        textoEstado.setText(estado);
    }

    @Override
    protected void onResume() {
        super.onResume();
        actualizarEstado();
    }
}
