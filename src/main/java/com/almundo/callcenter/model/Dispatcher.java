package com.almundo.callcenter.model;

import com.almundo.callcenter.domain.Cargo;
import com.almundo.callcenter.domain.Empleado;
import com.almundo.callcenter.domain.EstadoLLamada;
import com.almundo.callcenter.domain.Llamada;
import com.almundo.callcenter.exception.EmpleadoException;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Dispatcher {

    private ExecutorService executorService;

    private List<Empleado> empleadosEnLlamada = Collections.synchronizedList(new ArrayList<>());
    private List<Empleado> empleadosDisponibles = Collections.synchronizedList(new ArrayList<>());
    private Random random;

    public Dispatcher() {
        executorService = Executors.newFixedThreadPool(10);
        random = new Random();
    }

    public void dispatchCall(Llamada llamada) {
        executorService.submit(asignarEmpleadoALlamada(llamada));
    }

    public void shutdownExecutor() {
        executorService.shutdown();
    }

    public void agregarNuevoEmpleado(Empleado empleado) {
        empleadosDisponibles.add(empleado);
    }

    public void removerEmpleado(Empleado empleado) throws EmpleadoException {
        if (!empleadosDisponibles.remove(empleado)) {
            synchronized (empleadosEnLlamada) {
                empleadosEnLlamada.stream()
                        .filter(e -> empleado.getIdentificacion().equals(e.getIdentificacion()))
                        .findFirst()
                        .orElseThrow(() -> new EmpleadoException("Empleado no encontrado"));
                throw new EmpleadoException("Empleado en llamada");
            }
        }
    }

    private Runnable asignarEmpleadoALlamada(Llamada llamada) {
        return () -> {
            System.out.println("Asignando llamada: " + llamada.getNumero() + " - " + LocalTime.now().toString());

            Optional<Empleado> operador = buscarEmpleadosDisponiblesPorCargo(Cargo.OPERADOR);
            Optional<Empleado> supervisor = buscarEmpleadosDisponiblesPorCargo(Cargo.SUPERVISOR);
            Optional<Empleado> director = buscarEmpleadosDisponiblesPorCargo(Cargo.DIRECTOR);

            if (operador.isPresent()) {
                atenderLlamada(llamada, operador.get());
            } else if (supervisor.isPresent()) {
                atenderLlamada(llamada, supervisor.get());
            } else if (director.isPresent()) {
                atenderLlamada(llamada, director.get());
            } else {
                llamada.setEstado(EstadoLLamada.EN_ESPERA);
            }

            System.out.println("Llamada " + llamada.getNumero() + " : " + llamada.getEstado().name());
            if (llamada.getEmpleado() != null)
                System.out.println("Atendida por " + llamada.getEmpleado().getNombre() + ", cargo: " + llamada.getEmpleado().getCargo().name() + ", duracion: " + llamada.getDuracion());
        };
    }

    private void liberarEmpleado(Empleado empleado) {
        empleadosEnLlamada.remove(empleado);
        empleadosDisponibles.add(empleado);
    }

    private void asignarEmpleado(Empleado empleado) {
        empleadosDisponibles.remove(empleado);
        empleadosEnLlamada.add(empleado);
    }

    private Optional<Empleado> buscarEmpleadosDisponiblesPorCargo(Cargo cargo) {
        synchronized (empleadosEnLlamada) {
            return empleadosDisponibles.stream()
                    .filter(empleado -> empleado.getCargo().equals(cargo))
                    .findFirst();
        }
    }

    private void atenderLlamada(Llamada llamada, Empleado empleado) {
        int min = 5;
        int max = 10;
        asignarEmpleado(empleado);
        llamada.setEmpleado(empleado);

        int duracion = random.nextInt((max - min) + 1) + min;
        try {
            TimeUnit.SECONDS.sleep((long) duracion);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        llamada.setDuracion(duracion);
        llamada.setEstado(EstadoLLamada.ATENDIDA);
        liberarEmpleado(empleado);
    }
}
