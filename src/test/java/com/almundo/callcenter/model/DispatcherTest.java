package com.almundo.callcenter.model;

import com.almundo.callcenter.domain.Cargo;
import com.almundo.callcenter.domain.Empleado;
import com.almundo.callcenter.domain.EstadoLLamada;
import com.almundo.callcenter.domain.Llamada;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

public class DispatcherTest {

    private Dispatcher dispatcher;

    @Before
    public void init() {
        dispatcher = new Dispatcher();
    }

    @Test
    public void dispatchCall() throws InterruptedException {

        Random random = new Random();
        List<Llamada> llamadas = new ArrayList<>();

        agregarEmpleados(dispatcher);

        IntStream.range(0, 10).forEach(i -> {
            Llamada llamada = new Llamada();
            llamada.setNumero(random.nextInt(10000));
            llamadas.add(llamada);
        });

        llamadas.stream().forEach(llamada -> dispatcher.dispatchCall(llamada));

        TimeUnit.SECONDS.sleep(20);

        assertEquals(3l, llamadas.stream().filter(llamada -> llamada.getEstado().equals(EstadoLLamada.ATENDIDA)).count());
    }

    @After
    public void end() {
        dispatcher.shutdownExecutor();
    }

    private static void agregarEmpleados(Dispatcher dispatcher) {
        dispatcher.agregarNuevoEmpleado(crearEmpleado(Cargo.OPERADOR, "Daniel", 1L));
        dispatcher.agregarNuevoEmpleado(crearEmpleado(Cargo.SUPERVISOR, "Antonio", 2L));
        dispatcher.agregarNuevoEmpleado(crearEmpleado(Cargo.DIRECTOR, "Diana", 3L));
    }

    private static Empleado crearEmpleado(Cargo cargo, String nombre, Long identificacion) {
        Empleado empleado = new Empleado();
        empleado.setCargo(cargo);
        empleado.setIdentificacion(identificacion);
        empleado.setNombre(nombre);

        return empleado;
    }
}