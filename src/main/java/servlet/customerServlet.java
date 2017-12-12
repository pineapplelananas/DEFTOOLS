package servlet;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.DAO;
import model.DAOException;
import model.DataSourceFactory;
import model.LogException;

public class customerServlet extends HttpServlet {

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
            //si on n'a pas d'attribut objet, on throw une exception destinée à renvoyer vers logServlet
            if (null == customer_id) {
                throw new LogException("Vous devez vous authentifier");
            }
            //sinon, on envoie les attributs contenus dans les scope request et session
            request.setAttribute("ordersResultList", dao.getAllOrders(Integer.parseInt(customer_id)));
            request.setAttribute("productsResultList", dao.getAvailableProductList());
            session.setAttribute("customer_name", dao.getCustomerNameById(Integer.parseInt(customer_id)));
            //on appelle la page orderPage.jsp
            request.getRequestDispatcher("protectedCustomer/orderPage.jsp").forward(request, response);

        } catch (LogException ex) {
            //si on catch une erreur LogException, on renvoie vers la page logServlet
            request.setAttribute("message", ex.getMessage());
            response.sendRedirect("logServlet");

        } catch (NumberFormatException | DAOException | ServletException | IOException ex) {
            Logger.getLogger("customerServlet").log(Level.SEVERE, "Action en erreur", ex);
            request.setAttribute("message", ex.getMessage());
            request.getRequestDispatcher("customerServlet.jsp").forward(request, response);

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
