/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import DAO.ImagenDAO;
import DAO.JuegoDAO;
import DAO.JuegoImagenDAO;
import Modelo.Imagen;
import Modelo.Juego;
import Modelo.JuegoImagen;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author carlos
 */
public class wsJuego extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {

            //para que reconozca la ñ y los acentos
            request.setCharacterEncoding("UTF-8");

            String accion = request.getParameter("accion");
            String analisis = "", json = "";
            int idJuego = 0;
            Gson gson;
            Juego juego;
            Imagen imagen;
            JuegoImagen juegoImagen;
            JuegoDAO juegoDAO;
            ImagenDAO imagenDAO;
            JuegoImagenDAO juegoImagenDAO;

            ArrayList<Imagen> listaImagenes = new ArrayList<Imagen>();

            switch (accion) {
                case "analizar":
                    try {
                        juegoDAO = new JuegoDAO();
                        analisis = juegoDAO.analizarSentimientos(request.getParameter("frase"));
                        out.println(analisis);

                    } catch (Exception ex) {
                        System.out.print(ex.getMessage());
                        out.print(ex.getMessage());
                    }
                    break;
                case "obtenerImg":
                    try {

                        imagenDAO = new ImagenDAO();

                        listaImagenes = imagenDAO.listarImagenes();

                        if (listaImagenes != null) {
                            gson = new Gson();
                            json = new Gson().toJson(listaImagenes);
                            out.print(json);
                        } else {
                            out.print("error");
                        }
                    } catch (Exception ex) {
                        System.out.print(ex.getMessage());
                        out.print(ex.getMessage());
                    }
                    break;
                case "registrarJuego":
                    try {
                        juego = new Juego();
                        juegoDAO = new JuegoDAO();

                        analisis = juegoDAO.analizarSentimientos(request.getParameter("frase"));
                        juego.getPersona().setId_persona(Integer.parseInt(request.getParameter("idPersona")));

                        idJuego = juegoDAO.registrarJuego(juego, analisis);

                        if (idJuego != 0) {
                            out.print(String.valueOf(idJuego));
                        } else {
                            out.print("error");
                        }

                    } catch (Exception ex) {
                        System.out.print(ex.getMessage());
                        out.print(ex.getMessage());
                    }
                    break;
                case "registrarJuegoImagen":
                    try {
                        juegoImagen = new JuegoImagen();
                        juegoDAO = new JuegoDAO();

                        juegoImagen.getJuego().setId_juego(Integer.parseInt(request.getParameter("idJuego")));
                        juegoImagen.getImagen().setId_imagen(Integer.parseInt(request.getParameter("idImagen")));
                        juegoImagen.setOpinion(request.getParameter("opinion"));

                        if (juegoDAO.registrarJuegoImagen(juegoImagen)) {
                            out.print("correcto");
                        } else {
                            out.print("error");
                        }

                    } catch (Exception ex) {
                        System.out.print(ex.getMessage());
                        out.print(ex.getMessage());
                    }
                    break;
                case "obtenerAnalisis":
                    try {
                        juego = new Juego();
                        juegoDAO = new JuegoDAO();
                        
                        juego.setId_juego(Integer.parseInt(request.getParameter("idJuego")));
                        
                        analisis = juegoDAO.obtenerAnalisis(juego);
                        if (!analisis.equals("")) {
                            out.print(analisis);
                        } else {
                            out.print("error");
                        }
                    } catch (Exception ex) {
                        System.out.print(ex.getMessage());
                        out.print(ex.getMessage());
                    }
                    break;
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
