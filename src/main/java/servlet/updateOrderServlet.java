
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

public class updateOrderServlet extends HttpServlet {

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
            //on instancie un DAO avec la base de données
            DAO dao = new DAO(DataSourceFactory.getDataSource());
            //on récupère tous les paramètres nécessaire à l'exécution de la requête
            String action = request.getParameter("action");
            String product_id = request.getParameter("product_id");
            String quantity = request.getParameter("quantity");
            //si la session est active, on récupère l'userName
            HttpSession session = request.getSession(false);
            String customer_id = (String) session.getAttribute("userName");

            if (action == null) {
                //si l'action est nulle, c'est qu'on arrive pour la premiere fois sur la page
                //on récupère les paramètres nécessaires, on les réajoute à la requête et on rappelle la même page
                String order_id = request.getParameter("order_id");
                String description = request.getParameter("description");
                String purchase_cost = request.getParameter("purchase_cost");
                String total_cost = request.getParameter("total_cost");
                request.setAttribute("customer_id", customer_id);
                request.setAttribute("order_id", order_id);
                request.setAttribute("product_id", product_id);
                request.setAttribute("quantity", quantity);
                request.setAttribute("description", description);
                request.setAttribute("purchase_cost", purchase_cost);
                request.setAttribute("total_cost", total_cost);
                request.setAttribute("productsResultList", dao.getAvailableProductList());
                request.getRequestDispatcher("protectedCustomer/orderDisplayPage.jsp").forward(request, response);
                //si l'action est "Modifier", on récupère les paramètres nécessaires à la requête
            } else if (action.equals("Modifier")) {
                String product_id_to_update = request.getParameter("product_id_to_update");
                String quantity_to_update = request.getParameter("quantity_to_update");
                String order_num_to_update = request.getParameter("order_id_to_update");
                int int_product_id_to_update = Integer.parseInt(product_id_to_update);
                int int_quantity_to_update = Integer.parseInt(quantity_to_update);
                int int_order_num_to_update = Integer.parseInt(order_num_to_update);
                //on exécute la requête
                dao.updateOrder(int_order_num_to_update, int_product_id_to_update, int_quantity_to_update);
                //on renvoie sur la servlet customerServlet
                response.sendRedirect("customerServlet");

            }

        } catch (DAOException | ServletException | IOException | NumberFormatException ex) {
            Logger.getLogger("updateOrderServlet").log(Level.SEVERE, "Action en erreur", ex);

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
