package uce.edu.ec.api.domain.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Table(name = "vendedor")
@Entity
public class Vendedor extends PanacheEntityBase {

    @Id
    @Column(name = "ven_cedula_vendedor")
    private String cedulaVendedor;

    @Column(name = "ven_nombre")
    private String nombre;

    @Column(name = "ven_telefono")
    private String telefono;

    public String getCedulaVendedor() {
        return cedulaVendedor;
    }

    public void setCedulaVendedor(String cedulaVendedor) {
        this.cedulaVendedor = cedulaVendedor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @Override
    public String toString() {
        return "Vendedor [cedulaVendedor=" + cedulaVendedor + ", nombre=" + nombre + ", telefono=" + telefono + "]";
    }

}
