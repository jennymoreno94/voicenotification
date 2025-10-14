package com.notificacionesvoz.presentacion;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.notificacionesvoz.datos.fabrica.FabricaMensajesNotificacion;
import com.notificacionesvoz.datos.repositorio.RepositorioNotificacionesVozImpl;
import com.notificacionesvoz.dominio.modelo.TipoNotificacion;
import com.notificacionesvoz.dominio.modelo.ConfiguracionVoz;
import com.notificacionesvoz.dominio.modelo.NotificacionVoz;
import com.notificacionesvoz.dominio.repositorio.RepositorioNotificacionesVoz;
import com.notificacionesvoz.dominio.casosuso.ConfigurarVozCasoUso;
import com.notificacionesvoz.dominio.casosuso.ReproducirNotificacionCasoUso;

/**
 * Punto de entrada principal de la librería
 * Gestiona el ciclo de vida y proporciona API simple para los consumidores
 */
public class GestorNotificacionesVoz implements DefaultLifecycleObserver {
    
    private static volatile GestorNotificacionesVoz instancia;
    
    private final RepositorioNotificacionesVoz repositorio;
    private final ReproducirNotificacionCasoUso casoUsoReproducir;
    private final ConfigurarVozCasoUso casoUsoConfigurar;
    private final MutableLiveData<EventoNotificacion> eventosLiveData;
    
    private GestorNotificacionesVoz(@NonNull Context contexto) {
        RepositorioNotificacionesVozImpl repositorioImpl = new RepositorioNotificacionesVozImpl(contexto);
        this.repositorio = repositorioImpl;
        this.casoUsoReproducir = new ReproducirNotificacionCasoUso(repositorio);
        this.casoUsoConfigurar = new ConfigurarVozCasoUso(repositorio);
        this.eventosLiveData = new MutableLiveData<>();
        
        // Configurar escuchador para eventos
        repositorioImpl.establecerEscuchador(new RepositorioNotificacionesVozImpl.EscuchadorNotificacionesVoz() {
            @Override
            public void alIniciar(String idExpresion) {
                eventosLiveData.postValue(new EventoNotificacion(EventoNotificacion.Tipo.INICIADO, idExpresion));
            }

            @Override
            public void alCompletar(String idExpresion) {
                eventosLiveData.postValue(new EventoNotificacion(EventoNotificacion.Tipo.COMPLETADO, idExpresion));
            }

            @Override
            public void alOcurrirError(String idExpresion) {
                eventosLiveData.postValue(new EventoNotificacion(EventoNotificacion.Tipo.ERROR, idExpresion));
            }
        });
    }

    /**
     * Obtiene la instancia singleton (thread-safe)
     */
    @NonNull
    public static GestorNotificacionesVoz obtenerInstancia(@NonNull Context contexto) {
        if (instancia == null) {
            synchronized (GestorNotificacionesVoz.class) {
                if (instancia == null) {
                    instancia = new GestorNotificacionesVoz(contexto.getApplicationContext());
                }
            }
        }
        return instancia;
    }

    /**
     * Reproduce una notificación de voz
     */
    public void reproducir(@NonNull NotificacionVoz notificacion) {
        try {
            casoUsoReproducir.ejecutar(notificacion);
        } catch (Exception excepcion) {
            eventosLiveData.postValue(new EventoNotificacion(EventoNotificacion.Tipo.ERROR, excepcion.getMessage()));
        }
    }

    /**
     * Reproduce una notificación con mensaje predefinido
     */
    public void reproducir(@NonNull TipoNotificacion tipo) {
        String mensaje = FabricaMensajesNotificacion.obtenerMensajeEspanol(tipo);
        NotificacionVoz notificacion = new NotificacionVoz.Constructor()
                .establecerTipo(tipo)
                .establecerMensaje(mensaje)
                .establecerPrioridad(NotificacionVoz.Prioridad.NORMAL)
                .construir();
        reproducir(notificacion);
    }

    /**
     * Reproduce notificación de exceso de velocidad con datos
     */
    public void reproducirExcesoVelocidad(int velocidadActual, int limiteVelocidad) {
        String mensaje = FabricaMensajesNotificacion.obtenerMensajeExcesoVelocidad(velocidadActual, limiteVelocidad);
        NotificacionVoz notificacion = new NotificacionVoz.Constructor()
                .establecerTipo(TipoNotificacion.EXCESO_VELOCIDAD)
                .establecerMensaje(mensaje)
                .establecerPrioridad(NotificacionVoz.Prioridad.ALTA)
                .construir();
        reproducir(notificacion);
    }

    /**
     * Configura el motor de voz
     */
    public void configurar(@NonNull ConfiguracionVoz configuracion) {
        casoUsoConfigurar.ejecutar(configuracion);
    }

    /**
     * Detiene la reproducción actual
     */
    public void detener() {
        repositorio.detener();
    }

    /**
     * Verifica si está reproduciendo
     */
    public boolean estaReproduciendo() {
        return repositorio.estaReproduciendo();
    }

    /**
     * Verifica si el servicio está disponible
     */
    public boolean estaDisponible() {
        return repositorio.estaDisponible();
    }

    /**
     * Obtiene LiveData para observar eventos
     */
    @NonNull
    public LiveData<EventoNotificacion> obtenerEventos() {
        return eventosLiveData;
    }

    // Callbacks del ciclo de vida
    @Override
    public void onPause(@NonNull LifecycleOwner propietario) {
        // Pausar notificaciones cuando la app va a background
        detener();
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner propietario) {
        // Liberar recursos
        repositorio.finalizar();
        propietario.getLifecycle().removeObserver(this);
    }

    /**
     * Clase para eventos de notificaciones
     */
    public static class EventoNotificacion {
        public enum Tipo {
            INICIADO, COMPLETADO, ERROR
        }

        private final Tipo tipo;
        private final String mensaje;

        public EventoNotificacion(Tipo tipo, String mensaje) {
            this.tipo = tipo;
            this.mensaje = mensaje;
        }

        public Tipo obtenerTipo() {
            return tipo;
        }

        public String obtenerMensaje() {
            return mensaje;
        }
    }
}
