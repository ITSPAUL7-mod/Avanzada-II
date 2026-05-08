package uce.edu.pa2.video;

public class Videojuego {

    private String nombre;
    private String plataforma;
    private Double precio;

    public Videojuego() {
        
    }

    public Videojuego(String nombre, String plataforma, Double precio) {
        this.nombre = nombre;
        this.plataforma = plataforma;
        this.precio = precio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPlataforma() {
        return plataforma;
    }

    public void setPlataforma(String plataforma) {
        this.plataforma = plataforma;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    
    





}
