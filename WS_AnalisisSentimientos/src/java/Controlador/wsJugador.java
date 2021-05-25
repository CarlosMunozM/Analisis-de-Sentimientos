/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import DAO.PersonaDAO;
import Modelo.Persona;
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
 * @author Personal
 */
public class wsJugador extends HttpServlet {

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

            String json = "";
            Gson gson;
            Persona persona;
            PersonaDAO personaDAO;
            ArrayList<Persona> listaPersonas = new ArrayList<Persona>();

            switch (accion) {
                case "mostrarJugadores":
                    try {
                        personaDAO = new PersonaDAO();
                        listaPersonas = personaDAO.listarJugadores();
                        if (listaPersonas != null) {
                            gson = new Gson();
                            json = new Gson().toJson(listaPersonas);
                            out.print(json);
                        } else {
                            out.print("error");
                        }

                    } catch (Exception ex) {
                        System.out.print(ex.getMessage());
                        out.print(ex.getMessage());
                    }
                    break;

                case "eliminarJugador":
                    try {
                        persona = new Persona();
                        personaDAO = new PersonaDAO();

                        persona.setId_persona(Integer.parseInt(request.getParameter("idJugador")));

                        if (personaDAO.eliminarJugador(persona)) {
                            out.print("correcto");
                        } else {
                            out.print("error");
                        }
                    } catch (Exception ex) {
                        System.out.print(ex.getMessage());
                        out.print(ex.getMessage());
                    }
                    break;

                case "modificarJugador":
                    try {
                        persona = new Persona();
                        personaDAO = new PersonaDAO();
                        
                        persona.setId_persona(Integer.parseInt(request.getParameter("id_persona")));
                        persona.setNombres(request.getParameter("nombres"));
                        persona.setApellidos(request.getParameter("apellidos"));
                        persona.setTelefono(request.getParameter("telefono"));
                        persona.setFechaNacimiento(request.getParameter("fecha_nacimiento"));
                        persona.setGenero(request.getParameter("genero"));

                        if (personaDAO.modificarJugador(persona)) {
                            out.print("correcto");
                        } else {
                            out.print("error");
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
