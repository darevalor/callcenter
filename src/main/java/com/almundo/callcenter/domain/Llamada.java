package com.almundo.callcenter.domain;

public class Llamada {
    private int numero;
    private int duracion;
    private EstadoLLamada estado;
    private Empleado empleado;

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public EstadoLLamada getEstado() {
        return estado;
    }

    public void setEstado(EstadoLLamada estado) {
        this.estado = estado;
    }
}
