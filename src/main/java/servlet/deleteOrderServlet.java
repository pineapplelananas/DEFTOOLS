package servlet;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.DAO;
import model.DAOException;
import model.DataSourceFactory;

public class deleteOrderServlet extends HttpServlet {

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
        try {
            //on importe le DAO
            DAO dao = new DAO(DataSourceFactory.getDataSource());
            //on récupère la session en cours si elle existe
            HttpSession session = request.getSession(false);
            //on récupère l'attribut userName contenu dans le scope session
            String customer_id = (String) session.getAttribute("userName");
            //ainsi que les attributs contenus dans le scope request
            String order_id = request.getParameter("order_id");
            //on les parse
            int int_customer_id = Integer.parseInt(customer_id);
            int int_order_id = Integer.parseInt(order_id);
            //on exécute la méthode du DAO permettant de supprimer une ligne
            dao.deleteOrder(int_order_id, int_customer_id);
            //on renvoie vers la servlet customerServlet
            response.sendRedirect("customerServlet");

        } catch (NumberFormatException | DAOException | IOException ex) {
            Logger.getLogger("deleteOrderServlet").log(Level.SEVERE, "Action en erreur", ex);
            String customer_id = request.getParameter("customer_id");
            request.setAttribute("customer_id", customer_id);
            request.setAttribute("message", ex.getMessage());
            RequestDispatcher rd = request.getRequestDispatcher("customerServlet");
            rd.forward(request, response);

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
