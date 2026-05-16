/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abpaw.interfaces;

import java.util.List;

public interface CRUDable<T> {
    boolean insert(T entity);
    boolean update(T entity);
    boolean delete(int id);
    T getById(int id);
    List<T> getAll();
}