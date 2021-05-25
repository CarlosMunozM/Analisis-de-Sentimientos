/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import DAO.PersonaDAO;
import Modelo.Imagen;
import Modelo.Persona;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author carlos
 */
public class wsLogin extends HttpServlet {

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
            /* TODO output your page here. You may use following sample code. */

            //para que reconozca la ñ y los acentos
            request.setCharacterEncoding("UTF-8");

            String accion = request.getParameter("accion");

            String user = "", clave = "", json = "";
            Gson gson;
            ArrayList<String> arrayList;
            Persona persona;
            PersonaDAO personaDAO;

            switch (accion) {
                case "login":

                    try {
                        persona = new Persona();
                        personaDAO = new PersonaDAO();

                        user = request.getParameter("usr");
                        clave = request.getParameter("clave");

                        persona.setUsuario(user);
                        persona.setClave(clave);
                        persona = personaDAO.login(persona);

                        if (persona != null) {
                            gson = new Gson();
                            json = new Gson().toJson(persona);
                            out.print(json);
                        } else {
                            out.print("error");
                        }
                    } catch (Exception ex) {
                        System.out.print(ex.getMessage());
                        out.print(ex.getMessage());
                    }
                    break;
                case "registrarJugador":
                    try {
                        persona = new Persona();
                        personaDAO = new PersonaDAO();

                        persona.setNombres(request.getParameter("nombres"));
                        persona.setApellidos(request.getParameter("apellidos"));
                        persona.setTelefono(request.getParameter("telefono"));

                        //SimpleDateFormat parseador = new SimpleDateFormat("yyyy-MM-dd");
                        persona.setFechaNacimiento(request.getParameter("fecha_nacimiento"));

                        persona.setUsuario(request.getParameter("usuario"));
                        persona.setClave(request.getParameter("clave"));
                        persona.setGenero(request.getParameter("genero"));

                        if (personaDAO.registrarJugador(persona)) {
                            out.print("correcto");
                        } else {
                            out.print("error");
                        }

                    } catch (Exception ex) {
                        System.out.print("error");
                        out.print(ex.getMessage());
                    }
                    break;
                case "buscarUsuario":

                    try {
                        persona = new Persona();
                        personaDAO = new PersonaDAO();
                        persona.setUsuario(request.getParameter("usuario"));
                        if (personaDAO.buscarUsuario(persona)) {
                            out.print("usuarioOcupado");
                        } else {
                            out.print("usuarioLibre");
                        }

                    } catch (Exception ex) {
                        System.out.print("error");
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
