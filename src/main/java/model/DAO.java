package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;

public class DAO {

    private final DataSource myDataSource;

    /**
     * Construit le DAO avec sa source de données
     *
     * @param dataSource la source de données à utiliser
     */
    public DAO(DataSource dataSource) {
        this.myDataSource = dataSource;
    }

    /**
     * Nous indique si l'adresse mail et l'id rentrée nous renvoie un client
     * existant
     *
     * @param _mail l'adresse mail du client
     * @param _customer_id l'id du client
     * @return le booléen vrai si le client existe, faux sinon
     * @throws DAOException
     */
    public boolean doesCustomerExists(String _mail, int _customer_id) throws DAOException {
        String sql = "SELECT * FROM CUSTOMER WHERE CUSTOMER_ID=? AND EMAIL=?";
        boolean result = true;
        try (Connection connection = myDataSource.getConnection(); // Ouvrir une connexion
                PreparedStatement stmt = connection.prepareStatement(sql);) {
            stmt.setInt(1, _customer_id);
            stmt.setString(2, _mail);
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                result = false;
            }
        } catch (SQLException ex) {
            Logger.getLogger("DAO").log(Level.SEVERE, null, ex);
            throw new DAOException(ex.getMessage());
        }
        return result;
    }

    /**
     * Récuperer le nom du client à partir de son id client
     *
     * @param _customer_id l'id du client
     * @return un string contenant le nom du client
     * @throws DAOException
     */
    public String getCustomerNameById(int _customer_id) throws DAOException {
        String result = "";
        String sql = "SELECT NAME FROM CUSTOMER WHERE CUSTOMER_ID=?";
        try (Connection connection = myDataSource.getConnection(); // Ouvrir une connexion
                PreparedStatement stmt = connection.prepareStatement(sql);) {

            stmt.setInt(1, _customer_id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                result = rs.getString("NAME");
            }
            if (result.equals("")) {
                throw new DAOException("mauvais numéro de client");
            }

        } catch (SQLException ex) {
            Logger.getLogger("DAO").log(Level.SEVERE, null, ex);
            throw new DAOException(ex.getMessage());
        }
        return result;

    }

    /**
     * Permet d'ajouter une commande à la base de données
     *
     * @param _customer_id l'id du client
     * @param _product_id l'id du produit ajouté
     * @param _quantity_to_add la quantité de produits à ajouter
     * @throws DAOException
     */
    public void addOrder(int _customer_id, int _product_id, int _quantity_to_add) throws DAOException {
        String sql = "INSERT INTO PURCHASE_ORDER VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = myDataSource.getConnection(); // Ouvrir une connexion
                PreparedStatement stmt = connection.prepareStatement(sql);) {

            int last_order_num = getLastOrderNum();
            int order_num = last_order_num + 1;
            int customer_id = _customer_id;
            int product_id = _product_id;
            int quantity = _quantity_to_add;
            float purchase_cost = getPurchaseCost(product_id);

            Calendar calendar = Calendar.getInstance();
            java.sql.Date sales_date = new java.sql.Date(calendar.getTime().getTime());
            String freight_company = "FR Express";

            stmt.setInt(1, order_num);
            stmt.setInt(2, customer_id);
            stmt.setInt(3, product_id);
            stmt.setInt(4, quantity);
            stmt.setFloat(5, purchase_cost);
            stmt.setDate(6, sales_date);
            stmt.setDate(7, sales_date);
            stmt.setString(8, freight_company);

            int rs = stmt.executeUpdate();
            if (rs == 0) {
                throw new DAOException("aucun produit n'a été ajouté");
            }

        } catch (SQLException ex) {
            Logger.getLogger("DAO").log(Level.SEVERE, null, ex);
            throw new DAOException(ex.getMessage());
        }

    }

    /**
     * Permet de supprimer une ligne Commande dans la base de données
     *
     * @param _order_id l'id de la commande à supprimer
     * @param _customer_id l'id du client associé à la commande
     * @throws DAOException
     */
    public void deleteOrder(int _order_id, int _customer_id) throws DAOException {
        String sql = "DELETE FROM PURCHASE_ORDER WHERE ORDER_NUM=?";
        try (Connection connection = myDataSource.getConnection(); // Ouvrir une connexion
                PreparedStatement stmt = connection.prepareStatement(sql);) {
            stmt.setInt(1, _order_id);
            int rs = stmt.executeUpdate();
            if (rs == 0) {
                throw new DAOException("la commande n'existe pas");
            }

        } catch (SQLException ex) {
            Logger.getLogger("DAO").log(Level.SEVERE, null, ex);
            throw new DAOException(ex.getMessage());
        }

    }

    /**
     * permet de modifier une commande dans la base de données
     *
     * @param _order_num le numéro de la commande à modifier
     * @param _product_id_to_update l'id du produit à modifier dans la commande
     * @param _quantity_to_update la quantité à modifier dans la commande
     * @throws DAOException
     */
    public void updateOrder(int _order_num, int _product_id_to_update, int _quantity_to_update) throws DAOException {
        String sql = "UPDATE PURCHASE_ORDER SET PRODUCT_ID=?, QUANTITY=? WHERE ORDER_NUM=?";
        try (Connection connection = myDataSource.getConnection(); // Ouvrir une connexion
                PreparedStatement stmt = connection.prepareStatement(sql);) {
            stmt.setInt(1, _product_id_to_update);
            stmt.setInt(2, _quantity_to_update);
            stmt.setInt(3, _order_num);
            int rs = stmt.executeUpdate();
            if (rs == 0) {
                throw new DAOException("la commande n'a pas été modifée");
            }

        } catch (SQLException ex) {
            Logger.getLogger("DAO").log(Level.SEVERE, null, ex);
            throw new DAOException(ex.getMessage());
        }
    }

    /**
     * Permet de connaître l'id du produit à partir de la commande associée
     *
     * @param _order_num le numéro de la commande
     * @return l'id du produit commandé
     * @throws SQLException
     * @throws DAOException
     */
    public int getProductIdFromOrder(int _order_num) throws SQLException, DAOException {
        String sql = "SELECT PRODUCT_ID FROM PURCHASE_ORDER WHERE ORDER_NUM=?";
        int result = 0;
        try (Connection connection = myDataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql);) {
            stmt.setInt(1, _order_num);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                result = rs.getInt("PRODUCT_ID");
            }
            if (result == 0) {
                throw new DAOException("mauvais numéro de commande");
            }
        } catch (SQLException ex) {
            Logger.getLogger("DAO").log(Level.SEVERE, null, ex);
            throw new DAOException(ex.getMessage());
        }
        return result;
    }

    /**
     * Permet de connaître la quantité commandée dans une commande précise
     *
     * @param _order_num le numéro de la commande
     * @return la quantité au format int
     * @throws SQLException
     * @throws DAOException
     */
    public int getQuantityFromOrder(int _order_num) throws SQLException, DAOException {
        String sql = "SELECT QUANTITY FROM PURCHASE_ORDER WHERE ORDER_NUM=?";
        int result = 0;
        try (Connection connection = myDataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql);) {
            stmt.setInt(1, _order_num);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                result = rs.getInt("QUANTITY");
            }
            if (result == 0) {
                throw new DAOException("mauvais numéro de commande");
            }
        } catch (SQLException ex) {
            Logger.getLogger("DAO").log(Level.SEVERE, null, ex);
            throw new DAOException(ex.getMessage());
        }
        return result;
    }

    /**
     * permet de connaître le numéro de la dernière commande générée dans la
     * Base de données précédemment triée sur les numéros de commande
     *
     * @return le numéro de la dernière commande
     * @throws DAOException
     */
    public int getLastOrderNum() throws DAOException {
        int lastOrderNum = 1;
        String sql = "SELECT MAX(ORDER_NUM) AS MAXPURCHASE FROM PURCHASE_ORDER";
        try (Connection connection = myDataSource.getConnection();
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                lastOrderNum = rs.getInt("MAXPURCHASE");
            }
        } catch (SQLException e) {
            throw new DAOException(e.getMessage());
        }

        return lastOrderNum;

    }

    /**
     * Permet de connaître le prix de vente d'un produit
     *
     * @param _product_id l'id du produit
     * @return son prix de vente
     * @throws DAOException
     */
    public float getPurchaseCost(int _product_id) throws DAOException {
        float purchase_cost = 0;
        String sql = "SELECT PURCHASE_COST FROM PRODUCT WHERE PRODUCT_ID=?";
        try (Connection connection = myDataSource.getConnection(); // Ouvrir une connexion
                PreparedStatement stmt = connection.prepareStatement(sql);) {
            stmt.setInt(1, _product_id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                purchase_cost = rs.getFloat("PURCHASE_COST");
            }

        } catch (SQLException ex) {
            Logger.getLogger("DAO").log(Level.SEVERE, null, ex);
            throw new DAOException(ex.getMessage());
        }
        return purchase_cost;
    }

    /**
     * Permet de récupérer la liste de toutes les commandes d'un client précis
     * dans la Base de données
     *
     * @param _customer_id l'id du client
     * @return la liste d'OrderEntity associées au client
     * @throws DAOException
     */
    public ArrayList<OrderEntity> getAllOrders(int _customer_id) throws DAOException {
        ArrayList<OrderEntity> result_list = new ArrayList();
        String sql = "SELECT * FROM PURCHASE_ORDER INNER JOIN PRODUCT ON PURCHASE_ORDER.PRODUCT_ID=PRODUCT.PRODUCT_ID WHERE CUSTOMER_ID=?";
        try (Connection connection = myDataSource.getConnection(); // Ouvrir une connexion
                PreparedStatement stmt = connection.prepareStatement(sql);) {
            stmt.setInt(1, _customer_id);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int order_num = rs.getInt("ORDER_NUM");
                int customer_id = rs.getInt("CUSTOMER_ID");
                int product_id = rs.getInt("PRODUCT_ID");
                String description = rs.getString("DESCRIPTION");
                int quantity = rs.getInt("QUANTITY");
                float purchase_cost = rs.getFloat("PURCHASE_COST");
                float total_cost = (float) (Math.round(purchase_cost * (float) quantity * 100) / 100.0); //pour arrondir à la 2e decimale
                Date sales_date = rs.getDate("SALES_DATE");
                Date shipping_date = rs.getDate("SHIPPING_DATE");
                String shipping_company = rs.getString("FREIGHT_COMPANY");

                OrderEntity new_entity = new OrderEntity(order_num, customer_id, product_id, description, quantity, purchase_cost, total_cost, sales_date, shipping_date, shipping_company);
                result_list.add(new_entity);

            }
        } catch (SQLException ex) {
            Logger.getLogger("DAO").log(Level.SEVERE, null, ex);
            throw new DAOException(ex.getMessage());
        }

        return result_list;
    }

    /**
     * Permet de connaître les produits disponibles à la vente
     *
     * @return la liste de ProductEntity disponibles
     * @throws DAOException
     */
    public ArrayList<ProductEntity> getAvailableProductList() throws DAOException {
        ArrayList<ProductEntity> result_list = new ArrayList();
        String sql = "SELECT * FROM PRODUCT WHERE PRODUCT.AVAILABLE='TRUE'";
        try (Connection connection = myDataSource.getConnection();
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int product_id = rs.getInt("PRODUCT_ID");
                float purchase_cost = rs.getFloat("PURCHASE_COST");
                String description = rs.getString("DESCRIPTION");
                ProductEntity new_product = new ProductEntity(product_id, purchase_cost, description);
                result_list.add(new_product);
            }
        } catch (SQLException e) {
            throw new DAOException(e.getMessage());
        }
        return result_list;
    }

    /**
     * Permet de connaître le chiffre d'affaires par catégorie de produit
     * réalisé entre deux certaines dates
     *
     * @param date_debut le début de la période étudiée
     * @param date_fin la fin de la période étudiée
     * @return une liste d'entités Revenue
     * @throws SQLException
     * @throws ParseException
     */
    public List<Revenue> getSalesByProductCat(String date_debut, String date_fin) throws SQLException, ParseException {

        //on récupère la date de début et de fin en string
        //on la converti en format Date
        Date date_debut_format = new SimpleDateFormat("yyyy-MM-dd").parse(date_debut);
        Date date_fin_format = new SimpleDateFormat("yyyy-MM-dd").parse(date_fin);

        List<Revenue> result = new LinkedList<>();

        String sql = "SELECT SUM(QUANTITY*PURCHASE_COST),PRODUCT_CODE.DESCRIPTION FROM PRODUCT INNER JOIN PURCHASE_ORDER ON PRODUCT.PRODUCT_ID = PURCHASE_ORDER.PRODUCT_ID    INNER JOIN PRODUCT_CODE ON PRODUCT_CODE.PROD_CODE=PRODUCT.PRODUCT_CODE WHERE SALES_DATE BETWEEN ? AND ? GROUP BY PRODUCT_CODE.DESCRIPTION";
        try (Connection connection = myDataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)) {

            //on converti les dates au format Date.sql
            java.sql.Date date_debut_sql = new java.sql.Date(date_debut_format.getTime());
            java.sql.Date date_fin_sql = new java.sql.Date(date_fin_format.getTime());

            //on place les paramètres dans le preparedStatement
            stmt.setDate(1, date_debut_sql);
            stmt.setDate(2, date_fin_sql);

            //on exécute la requête
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                //on créé une entity Revenue avec chaque ligne du résultat de la requête SQL
                Float chiffre_affaire = rs.getFloat("1");
                String code_produit = rs.getString("DESCRIPTION");
                Revenue new_section = new Revenue(code_produit, chiffre_affaire);
                //on l'ajoute à la liste de résultats
                result.add(new_section);
            }
        }

        return result;
        //Visualiser les chiffres d'affaire par zone géographique, en choisissant la période 
        //(date de début / date de fin) sur laquelle doit porter la statistique.

        //Visualiser les chiffres d'affaire par client, en choisissant la période 
        //(date de début / date de fin) sur laquelle doit porter la statistique.
    }

    /**
     * Permet de connaître le chiffre d'affaires par zone géographique réalisé
     * entre deux certaines dates
     *
     * @param date_debut le début de la période étudiée
     * @param date_fin la fin de la période étudiée
     * @return une liste d'entités Revenue
     * @throws SQLException
     * @throws ParseException
     */
    public List<Revenue> getSalesByGeo(String date_debut, String date_fin) throws SQLException, ParseException {

        //on récupère la date de début et de fin en string
        //on la converti en format Date
        Date date_debut_format = new SimpleDateFormat("yyyy-MM-dd").parse(date_debut);
        Date date_fin_format = new SimpleDateFormat("yyyy-MM-dd").parse(date_fin);

        List<Revenue> result = new LinkedList<>();

        String sql = " SELECT SUM(QUANTITY*PURCHASE_COST),CUSTOMER.STATE FROM  "
                + "PURCHASE_ORDER  INNER JOIN PRODUCT ON  PURCHASE_ORDER.PRODUCT_ID = "
                + "PRODUCT.PRODUCT_ID INNER JOIN CUSTOMER ON PURCHASE_ORDER.CUSTOMER_ID = "
                + "CUSTOMER.CUSTOMER_ID WHERE SALES_DATE BETWEEN ? AND ? GROUP BY CUSTOMER.STATE";

        try (Connection connection = myDataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)) {

            //on converti les dates au format Date.sql
            java.sql.Date date_debut_sql = new java.sql.Date(date_debut_format.getTime());
            java.sql.Date date_fin_sql = new java.sql.Date(date_fin_format.getTime());

            //on place les paramètres dans le preparedStatement
            stmt.setDate(1, date_debut_sql);
            stmt.setDate(2, date_fin_sql);

            //on exécute la requête
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                //on créé une entity Revenue avec chaque ligne du résultat de la requête SQL
                Float chiffre_affaire = rs.getFloat("1");
                String code_produit = rs.getString("STATE");
                Revenue new_section = new Revenue(code_produit, chiffre_affaire);
                //on l'ajoute à la liste de résultats
                result.add(new_section);
            }
        }

        return result;
    }

    /**
     * Permet de connaître le chiffre d'affaires par client réalisé entre deux
     * certaines dates
     *
     * @param date_debut le début de la période étudiée
     * @param date_fin la fin de la période étudiée
     * @return une liste d'entités Revenue
     * @throws SQLException
     * @throws ParseException
     */
    public List<Revenue> getSalesByCustomer(String date_debut, String date_fin) throws SQLException, ParseException {

        //on récupère la date de début et de fin en string
        //on la converti en format Date
        Date date_debut_format = new SimpleDateFormat("yyyy-MM-dd").parse(date_debut);
        Date date_fin_format = new SimpleDateFormat("yyyy-MM-dd").parse(date_fin);

        List<Revenue> result = new LinkedList<>();

        String sql = "SELECT SUM(QUANTITY*PURCHASE_COST),CUSTOMER.NAME FROM  PURCHASE_ORDER  INNER JOIN PRODUCT ON  PURCHASE_ORDER.PRODUCT_ID = PRODUCT.PRODUCT_ID INNER JOIN CUSTOMER on PURCHASE_ORDER.CUSTOMER_ID = CUSTOMER.CUSTOMER_ID WHERE SALES_DATE BETWEEN ? AND ? GROUP BY CUSTOMER.NAME";
        try (Connection connection = myDataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)) {

            //on converti les dates au format Date.sql
            java.sql.Date date_debut_sql = new java.sql.Date(date_debut_format.getTime());
            java.sql.Date date_fin_sql = new java.sql.Date(date_fin_format.getTime());

            //on place les paramètres dans le preparedStatement
            stmt.setDate(1, date_debut_sql);
            stmt.setDate(2, date_fin_sql);

            //on exécute la requête
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                //on créé une entity Revenue avec chaque ligne du résultat de la requête SQL
                Float chiffre_affaire = rs.getFloat("1");
                String code_produit = rs.getString("NAME");

                Revenue new_section = new Revenue(code_produit, chiffre_affaire);
                //on l'ajoute à la liste de résultats
                result.add(new_section);
            }
        }
        return result;
    }
}
