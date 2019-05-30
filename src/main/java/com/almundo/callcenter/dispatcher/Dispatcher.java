package com.almundo.callcenter.dispatcher;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Dispatcher {

    private ExecutorService executorService;

    public Dispatcher() {
        executorService = Executors.newFixedThreadPool(10);
    }

    public void dispatchCall() {
        Runnable asignarLlamada = () -> {
            System.out.println("Asignando llamada");
        };

        executorService.submit(asignarLlamada);
    }
}
