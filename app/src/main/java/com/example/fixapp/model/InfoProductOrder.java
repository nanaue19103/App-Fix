package com.example.fixapp.model;

public class InfoProductOrder {
    String idProduct;
    String imgPr;
    String namePr;
    int colorPr;
    String note;
    String date;
    Double price;
    String status;
    String size;
    int quantityPr;

    public InfoProductOrder() {
    }



    public InfoProductOrder(String idProduct, String imgPr, String namePr, int colorPr, String note, String date, Double price, String status, String size, int quantityPr) {
        this.idProduct = idProduct;
        this.imgPr = imgPr;
        this.namePr = namePr;
        this.colorPr = colorPr;
        this.note = note;
        this.date = date;
        this.price = price;
        this.status = status;
        this.size = size;
        this.quantityPr = quantityPr;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }



    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }





    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(String idProduct) {
        this.idProduct = idProduct;
    }

    public String getImgPr() {
        return imgPr;
    }

    public void setImgPr(String imgPr) {
        this.imgPr = imgPr;
    }

    public String getNamePr() {
        return namePr;
    }

    public void setNamePr(String namePr) {
        this.namePr = namePr;
    }

    public int getColorPr() {
        return colorPr;
    }

    public void setColorPr(int colorPr) {
        this.colorPr = colorPr;
    }

    public int getQuantityPr() {
        return quantityPr;
    }

    public void setQuantityPr(int quantityPr) {
        this.quantityPr = quantityPr;
    }
}
