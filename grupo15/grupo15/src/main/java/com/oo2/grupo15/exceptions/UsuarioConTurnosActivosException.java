package com.oo2.grupo15.exceptions;

public class UsuarioConTurnosActivosException extends RuntimeException {
    public UsuarioConTurnosActivosException(Long usuarioId) {
        super("No se puede eliminar el usuario con ID: " + usuarioId + " porque tiene turnos activos asociados.");
    }
}