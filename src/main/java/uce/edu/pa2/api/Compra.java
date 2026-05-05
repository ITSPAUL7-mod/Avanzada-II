package uce.edu.pa2.api;

public class Compra {
 private String cliente;
 private Double subTotal;
 private Double total;
 
    
 public Compra(){

 }


 public Compra(String cliente, Double subTotal) {
    this.cliente = cliente;
    this.subTotal = subTotal;
}


 public String getCliente() {
    return cliente;
 }


 public void setCliente(String cliente) {
    this.cliente = cliente;
 }


 public Double getSubTotal() {
    return subTotal;
 }


 public void setSubTotal(Double subTotal) {
    this.subTotal = subTotal;
 }


 public Double getTotal() {
    return total;
 }


 public void setTotal(Double total) {
    this.total = total;
 }
 


}
