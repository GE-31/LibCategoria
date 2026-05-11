package com.geordy.libcategoria.dto;

public class CategoriaDTO {

    private int idcategoria;
    private String nombrecategoria;
    private String descripcion;

    public CategoriaDTO() {}

    public CategoriaDTO(int idcategoria, String nombrecategoria, String descripcion) {
        this.idcategoria = idcategoria;
        this.nombrecategoria = nombrecategoria;
        this.descripcion = descripcion;
    }

    public int getIdcategoria() { return idcategoria; }
    public void setIdcategoria(int idcategoria) { this.idcategoria = idcategoria; }

    public String getNombrecategoria() { return nombrecategoria; }
    public void setNombrecategoria(String nombrecategoria) { this.nombrecategoria = nombrecategoria; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    @Override
    public String toString() {
        return "CategoriaDTO{idcategoria=" + idcategoria
                + ", nombrecategoria='" + nombrecategoria + "'"
                + ", descripcion='" + descripcion + "'}";
    }
}
