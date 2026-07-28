package uce.edu.ec.api.domain.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Table(name = "usuario")
@Entity
public class Usuario extends PanacheEntityBase {

    @Id
    @Column(name = "usu_cedula")
    private String cedula;

    @Column(name = "usu_nombre")
    private String nombre;

    @Column(name = "usu_correo")
    private String correo;


    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    @Override
    public String toString() {
        return "Usuario = [cedula=" + cedula + ", nombre=" + nombre + ", correo=" + correo + "]";
    }

}
