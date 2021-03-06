package com.almundo.callcenter;

import com.almundo.callcenter.domain.Cargo;
import com.almundo.callcenter.domain.Empleado;
import com.almundo.callcenter.domain.Llamada;
import com.almundo.callcenter.model.Dispatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class CallcenterApplication {

    public static void main(String[] args) {
        Dispatcher dispatcher = new Dispatcher();
        Random random = new Random();
        List<Llamada> llamadas = new ArrayList<>();

        agregarEmpleados(dispatcher);

        IntStream.range(0, 20).forEach(i -> {
            Llamada llamada = new Llamada();
            llamada.setNumero(random.nextInt(10000));
            llamadas.add(llamada);
        });

        llamadas.stream().forEach(llamada -> dispatcher.dispatchCall(llamada));


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
