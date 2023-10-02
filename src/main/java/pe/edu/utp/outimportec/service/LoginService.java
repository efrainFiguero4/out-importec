package pe.edu.utp.outimportec.service;

public interface LoginService {
    boolean isAuthenticated();

    public void iniciarSesion(String email, String contrasena);
}
