package uce.edu.pa2.api;

public class Venta {

    
 private String cliente;
 private double total;
 
 public Venta(String cliente, double total) {
    this.cliente = cliente;
    this.total = total;
 }
 public String getCliente() {
    return cliente;
 }
 public void setCliente(String cliente) {
    this.cliente = cliente;
 }
 public double getTotal() {
    return total;
 }
 public void setTotal(double total) {
    this.total = total;
 }

}
