package filters;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ProtectedAdminPageFilter implements Filter {

    /**
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param chain The filter chain we are processing
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        try {
            //on récupère la session (si elle existe), sinon session est égal à null
            HttpSession session = ((HttpServletRequest) request).getSession(false);
            //il n'y a pas de session courante
            if (session == null) {
                //on redirige vers la page d'erreur
                ((HttpServletResponse) response).sendRedirect(((HttpServletRequest) request).getContextPath() + "/error.html");
                //une session est en cours mais aucun attribut userName n'est disponible dans le scope session, on redirige vers la page d'erreur
            } else if (session != null && session.getAttribute("userName") == null) {
                ((HttpServletResponse) response).sendRedirect(((HttpServletRequest) request).getContextPath() + "/error.html");
                //une session est en cours et il s'agit de l'admin
                //on passe le filtrage
            } else if (session != null && session.getAttribute("userName").equals("admin")) {
                chain.doFilter(request, response);
                //une session est en cours mais il ne s'agit pas de l'admin
                //on redirige vers la servlet logServlet
            } else {
                ((HttpServletResponse) response).sendRedirect(((HttpServletRequest) request).getContextPath() + "/logServlet");
            }
        } catch (IOException | ServletException t) {
        }

    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}
