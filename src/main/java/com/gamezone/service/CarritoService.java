package com.gamezone.service;

import com.gamezone.model.Carrito;
import com.gamezone.model.CarritoItem;
import com.gamezone.model.Juego;
import com.gamezone.repository.CarritoItemRepository;
import com.gamezone.repository.CarritoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CarritoService {

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private CarritoItemRepository itemRepository;

    public Carrito obtenerCarrito(String correoUsuario) {
        Carrito carrito = carritoRepository.findByUsuario(correoUsuario);

        if (carrito == null) {
            carrito = new Carrito();
            carrito.setUsuario(correoUsuario);
            carrito = carritoRepository.save(carrito);
        }

        return carrito;
    }

    public void agregarJuego(String correoUsuario, Juego juego) {
        Carrito carrito = obtenerCarrito(correoUsuario);

        CarritoItem item = new CarritoItem();
        item.setNombreJuego(juego.getNombre());
        item.setPrecio(juego.getPrecioFinal());
        item.setImagen(juego.getImagen());
        item.setCantidad(1);
        item.setCarrito(carrito);

        itemRepository.save(item);
    }

    public void eliminarItem(Long id) {
        itemRepository.deleteById(id);
    }

    public Double calcularTotal(Carrito carrito) {
        return carrito.getItems()
                .stream()
                .mapToDouble(CarritoItem::getSubtotal)
                .sum();
    }

    public void vaciarCarrito(Carrito carrito) {
        carrito.getItems().clear();
        carritoRepository.save(carrito);
    }
}