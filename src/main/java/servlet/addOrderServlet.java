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
import model.DataSourceFactory;

public class addOrderServlet extends HttpServlet {

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
        try {
            //on importe le DAO
            DAO dao = new DAO(DataSourceFactory.getDataSource());
            //on récupère la session en cours si elle existe
            HttpSession session = request.getSession(false);
            //on récupère l'attribut userName contenu dans le scope session
            String customer_id = (String) session.getAttribute("userName");
            //on récupère les attributs contenus dans le scope request
            String product = request.getParameter("product");
            String quantity_to_add = request.getParameter("quantity_to_add");
            //si la quantité n'est pas indiqué, on renvoie une erreur sur la page
            if (quantity_to_add.equals("")) {
                throw new Exception("Veuillez indiquer la quantité");
            }
            int int_customer_id = Integer.parseInt(customer_id);
            int int_product = Integer.parseInt(product);
            int int_quantity_to_add = Integer.parseInt(quantity_to_add);
            //on ajoute une commande grâce à la méthode du dao
            dao.addOrder(int_customer_id, int_product, int_quantity_to_add);
            //on renvoie vers la servlet CustomerServlet
            response.sendRedirect("customerServlet");

        } catch (Exception ex) {
            Logger.getLogger("addOrderServlet").log(Level.SEVERE, "Action en erreur", ex);
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
