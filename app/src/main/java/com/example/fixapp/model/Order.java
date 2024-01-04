package com.example.fixapp.model;

import java.util.List;

public class Order {
    private String id,idBuyer,idSeller;
    private Double total;

    private String address;
    private String numberPhone;
    private Boolean paid;

    private List<InfoProductOrder> listProduct;

    public Order(String id, String idBuyer, String idSeller, Double total,String address, String numberPhone, Boolean paid, List<InfoProductOrder> listProduct) {
        this.id = id;
        this.idBuyer = idBuyer;
        this.idSeller = idSeller;
        this.total = total;
        this.address = address;
        this.numberPhone = numberPhone;
        this.paid = paid;
        this.listProduct = listProduct;
    }

    public Order() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdBuyer() {
        return idBuyer;
    }

    public void setIdBuyer(String idBuyer) {
        this.idBuyer = idBuyer;
    }

    public String getIdSeller() {
        return idSeller;
    }

    public void setIdSeller(String idSeller) {
        this.idSeller = idSeller;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNumberPhone() {
        return numberPhone;
    }

    public void setNumberPhone(String numberPhone) {
        this.numberPhone = numberPhone;
    }



    public Boolean getPaid() {
        return paid;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }


    public List<InfoProductOrder> getListProduct() {
        return listProduct;
    }

    public void setListProduct(List<InfoProductOrder> listProduct) {
        this.listProduct = listProduct;
    }

}
