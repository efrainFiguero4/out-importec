package pe.edu.utp.outimportec.service;

public interface LoginService {
    boolean isAuthenticated();

    void iniciarSesion(String email, String contrasena);
}
