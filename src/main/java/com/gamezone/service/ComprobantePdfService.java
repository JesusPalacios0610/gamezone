package com.gamezone.service;

import com.gamezone.model.Compra;
import com.gamezone.model.Usuario;

import org.openpdf.text.Document;
import org.openpdf.text.DocumentException;
import org.openpdf.text.Element;
import org.openpdf.text.Font;
import org.openpdf.text.FontFactory;
import org.openpdf.text.PageSize;
import org.openpdf.text.Paragraph;
import org.openpdf.text.Phrase;
import org.openpdf.text.pdf.PdfPCell;
import org.openpdf.text.pdf.PdfPTable;
import org.openpdf.text.pdf.PdfWriter;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.time.format.DateTimeFormatter;

@Service
public class ComprobantePdfService {

    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public void generarPdf(
            Compra compra,
            Usuario usuario,
            OutputStream outputStream
    ) throws DocumentException, IOException {

        Document documento = new Document(PageSize.A4, 45, 45, 45, 45);

        PdfWriter.getInstance(documento, outputStream);

        documento.open();

        agregarEncabezado(documento);
        agregarDatosOperacion(documento, compra, usuario);
        agregarProducto(documento, compra);
        agregarTotal(documento, compra);
        agregarAdvertencia(documento);

        documento.close();
    }

    private void agregarEncabezado(Document documento)
            throws DocumentException {

        Font fuenteTitulo = FontFactory.getFont(
                FontFactory.HELVETICA_BOLD,
                22
        );

        Font fuenteSubtitulo = FontFactory.getFont(
                FontFactory.HELVETICA,
                11
        );

        Paragraph titulo = new Paragraph("GAMEZONE", fuenteTitulo);
        titulo.setAlignment(Element.ALIGN_CENTER);

        Paragraph subtitulo = new Paragraph(
                "Comprobante de compra simulado",
                fuenteSubtitulo
        );

        subtitulo.setAlignment(Element.ALIGN_CENTER);
        subtitulo.setSpacingAfter(25);

        documento.add(titulo);
        documento.add(subtitulo);
    }

    private void agregarDatosOperacion(
            Document documento,
            Compra compra,
            Usuario usuario
    ) throws DocumentException {

        PdfPTable tabla = new PdfPTable(2);

        tabla.setWidthPercentage(100);
        tabla.setWidths(new float[]{1.4f, 2.6f});
        tabla.setSpacingAfter(20);

        agregarFila(
                tabla,
                "Código de operación",
                valorSeguro(compra.getCodigoOperacion())
        );

        agregarFila(
                tabla,
                "Fecha",
                compra.getFecha() != null
                        ? compra.getFecha().format(FORMATO_FECHA)
                        : "-"
        );

        agregarFila(
                tabla,
                "Cliente",
                valorSeguro(usuario.getNombre())
        );

        agregarFila(
                tabla,
                "Correo",
                valorSeguro(usuario.getCorreo())
        );

        agregarFila(
                tabla,
                "Método de pago",
                valorSeguro(compra.getMetodoPago())
        );

        agregarFila(
                tabla,
                "Tarjeta",
                "**** **** **** "
                        + valorSeguro(compra.getUltimosDigitosTarjeta())
        );

        agregarFila(
                tabla,
                "Estado",
                valorSeguro(compra.getEstado())
        );

        documento.add(tabla);
    }

    private void agregarProducto(
            Document documento,
            Compra compra
    ) throws DocumentException {

        Font fuenteTitulo = FontFactory.getFont(
                FontFactory.HELVETICA_BOLD,
                13
        );

        Paragraph titulo = new Paragraph(
                "Detalle de la compra",
                fuenteTitulo
        );

        titulo.setSpacingAfter(10);
        documento.add(titulo);

        PdfPTable tabla = new PdfPTable(2);

        tabla.setWidthPercentage(100);
        tabla.setWidths(new float[]{3f, 1f});
        tabla.setSpacingAfter(20);

        agregarEncabezadoTabla(tabla, "Producto");
        agregarEncabezadoTabla(tabla, "Precio");

        agregarCelda(tabla, valorSeguro(compra.getJuego()));

        agregarCelda(
                tabla,
                String.format("S/ %.2f", obtenerPrecio(compra))
        );

        documento.add(tabla);
    }

    private void agregarTotal(
            Document documento,
            Compra compra
    ) throws DocumentException {

        Font fuenteTotal = FontFactory.getFont(
                FontFactory.HELVETICA_BOLD,
                16
        );

        Paragraph total = new Paragraph(
                String.format(
                        "TOTAL PAGADO: S/ %.2f",
                        obtenerPrecio(compra)
                ),
                fuenteTotal
        );

        total.setAlignment(Element.ALIGN_RIGHT);
        total.setSpacingAfter(25);

        documento.add(total);
    }

    private void agregarAdvertencia(Document documento)
            throws DocumentException {

        Font fuenteAdvertencia = FontFactory.getFont(
                FontFactory.HELVETICA_OBLIQUE,
                10
        );

        Paragraph advertencia = new Paragraph(
                "Comprobante simulado para fines académicos. "
                        + "Sin validez tributaria.",
                fuenteAdvertencia
        );

        advertencia.setAlignment(Element.ALIGN_CENTER);

        documento.add(advertencia);
    }

    private void agregarFila(
            PdfPTable tabla,
            String etiqueta,
            String valor
    ) {

        Font fuenteEtiqueta = FontFactory.getFont(
                FontFactory.HELVETICA_BOLD,
                10
        );

        PdfPCell celdaEtiqueta = new PdfPCell(
                new Phrase(etiqueta, fuenteEtiqueta)
        );

        PdfPCell celdaValor = new PdfPCell(
                new Phrase(valor)
        );

        celdaEtiqueta.setPadding(8);
        celdaValor.setPadding(8);

        tabla.addCell(celdaEtiqueta);
        tabla.addCell(celdaValor);
    }

    private void agregarEncabezadoTabla(
            PdfPTable tabla,
            String texto
    ) {

        Font fuente = FontFactory.getFont(
                FontFactory.HELVETICA_BOLD,
                11
        );

        PdfPCell celda = new PdfPCell(
                new Phrase(texto, fuente)
        );

        celda.setPadding(8);

        tabla.addCell(celda);
    }

    private void agregarCelda(
            PdfPTable tabla,
            String texto
    ) {

        PdfPCell celda = new PdfPCell(
                new Phrase(texto)
        );

        celda.setPadding(8);

        tabla.addCell(celda);
    }

    private double obtenerPrecio(Compra compra) {
        return compra.getPrecio() != null
                ? compra.getPrecio()
                : 0.0;
    }

    private String valorSeguro(String valor) {
        return valor != null && !valor.isBlank()
                ? valor
                : "-";
    }
}