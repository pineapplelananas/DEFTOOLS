package model;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import javax.sql.DataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.hsqldb.cmdline.SqlFile;
import org.hsqldb.cmdline.SqlToolError;

public class DAOTest {

    private static DataSource myDataSource; // La source de données à utiliser
    private static Connection myConnection;
    private DAO myDAO;

    @Before
    public void setUp() throws SQLException, IOException, SqlToolError {
        // On crée la connection vers la base de test "in memory"
        myDataSource = getDataSource();
        myConnection = myDataSource.getConnection();
        // On initialise la base avec le contenu d'un fichier de test
        String sqlFilePath = DAOTest.class.getResource("export.sql").getFile();
        SqlFile sqlFile = new SqlFile(new File(sqlFilePath));
        sqlFile.setConnection(myConnection);
        sqlFile.execute();
        sqlFile.closeReader();
        // On crée l'objet à tester
        myDAO = new DAO(myDataSource);
    }

    @Test
    public void doesCustomerExistsTestFail() throws DAOException {
        try {
            boolean result = myDAO.doesCustomerExists("eae", 0);
            assertFalse(result);
        } catch (DAOException e) {
            fail();
        }
    }

    @Test
    public void doesCustomerExistsTest() throws DAOException {
        String user_expected = "jumboeagle@example.com";
        int customer_id_expected = 1;
        try {
            assertTrue(myDAO.doesCustomerExists(user_expected, customer_id_expected));
        } catch (DAOException e) {
            fail();
        }
    }

    @Test
    public void getCustomerNameByIdTestFail() throws DAOException {
        try {
            myDAO.getCustomerNameById(4);
            fail();
        } catch (DAOException e) {

        }

    }

    @Test
    public void getCustomerNameByIdTest() throws DAOException {
        String customer_expected = "Jumbo Eagle Corp";
        try {
            String customer_found = myDAO.getCustomerNameById(1);

            assertEquals(customer_expected, customer_found);
        } catch (DAOException e) {
            fail();
        }

    }

    @Test
    public void addOrderTestFail() throws DAOException {
        try {
            myDAO.addOrder(-1, 0, 0);
            fail();
        } catch (DAOException e) {

        }
    }

    @Test
    public void addOrderTest() throws DAOException {
        try {
            int last_order_num_actual = myDAO.getLastOrderNum();
            myDAO.addOrder(1, 980001, 3);
            int last_order_num_next = myDAO.getLastOrderNum();
            assertEquals(last_order_num_actual, last_order_num_next - 1);
        } catch (DAOException e) {
            fail();
        }
    }

    @Test
    public void deleteOrderTestFail() throws DAOException {
        try {
            myDAO.deleteOrder(-1, -1);
            fail();
        } catch (DAOException e) {

        }
    }

    @Test
    public void deleteOrderTest() throws DAOException {
        try {
            int last_order_num_actual = myDAO.getLastOrderNum();
            myDAO.addOrder(1, 980001, 3);
            myDAO.deleteOrder(last_order_num_actual + 1, 1);
            int last_order_num_expected = myDAO.getLastOrderNum();
            assertEquals(last_order_num_actual, last_order_num_expected);
        } catch (DAOException e) {
            fail();
        }
    }

    @Test
    public void updateOrderTestFail() throws DAOException {
        try {
            myDAO.updateOrder(0, 0, 0);
            fail();
        } catch (DAOException e) {

        }
    }

    @Test
    public void updateOrderTest() throws DAOException, SQLException {
        int order_num_to_update = 10398001;
        int product_id_to_update = 980005;
        int quantity_to_update = 5;
        try {
            myDAO.updateOrder(order_num_to_update, product_id_to_update, quantity_to_update);
            assertEquals(product_id_to_update, myDAO.getProductIdFromOrder(order_num_to_update));
            assertEquals(quantity_to_update, myDAO.getQuantityFromOrder(order_num_to_update));
        } catch (SQLException | DAOException e) {
            fail();
        }
    }

    @Test
    public void getProductIdFromOrderTestFail() throws SQLException {
        try {
            myDAO.getProductIdFromOrder(-1);
            fail();
        } catch (DAOException e) {

        }
    }

    @Test
    public void getProductIdFromOrderTest() throws SQLException {
        int order_num_expected = 10398001;
        int product_id_expected = 980001;
        try {
            int product_id_actual = myDAO.getProductIdFromOrder(order_num_expected);
            assertEquals(product_id_actual, product_id_expected);
        } catch (DAOException | SQLException f) {
            fail();
        }
    }

    @Test
    public void getQuantityFromOrderTestFail() throws SQLException {
        try {
            myDAO.getQuantityFromOrder(-1);
            fail();
        } catch (DAOException e) {

        }
    }

    @Test
    public void getQuantityFromOrderTest() throws SQLException {
        int order_num_expected = 10398001;
        int quantity_expected = 10;
        try {
            int quantity_actual = myDAO.getQuantityFromOrder(order_num_expected);
            assertEquals(quantity_actual, quantity_expected);
        } catch (DAOException | SQLException f) {
            fail();
        }
    }

    @Test
    public void getLastOrderNumTest() throws DAOException {
        int last_order_num_expected = 30298004;
        try {
            int last_order_num_found = myDAO.getLastOrderNum();
            assertEquals(last_order_num_expected, last_order_num_found);
        } catch (DAOException e) {
            fail();
        }
    }

    @Test
    public void getPurchaseCostTest() throws DAOException {
        int product_id_expected = 980001;
        double purchase_cost_expected = 1095.00;
        try {
            double purchase_cost_actual = myDAO.getPurchaseCost(product_id_expected);
            assertEquals(purchase_cost_actual, purchase_cost_expected, 0.001);
        } catch (DAOException e) {
            fail();
        }
    }

    @Test
    public void getAllOrdersTest() {
        int customer_id_expected = 2;
        int number_of_orders_expected = 2;
        try {
            int number_of_orders_actual = myDAO.getAllOrders(customer_id_expected).size();
            assertEquals(number_of_orders_expected, number_of_orders_actual);
        } catch (DAOException e) {
            fail();
        }
    }

    @Test
    public void getAvailableProductListTest() {
        int number_of_products_expected = 25;
        try {
            int number_of_products_actual = myDAO.getAvailableProductList().size();
            assertEquals(number_of_products_actual, number_of_products_expected);
        } catch (DAOException e) {
            fail();
        }
    }

    @Test
    public void getSalesByCustomerTest() {
        int number_of_revenues_expected = 12;
        String date_debut = "2000-01-01";
        String date_fin = "2017-11-11";

        try {
            int number_of_revenue_actual = myDAO.getSalesByCustomer(date_debut, date_fin).size();
            assertEquals(number_of_revenue_actual, number_of_revenues_expected);

            fail();
        } catch (SQLException ex) {
        } catch (ParseException ex) {
        }
    }

    @Test
    public void getSalesByGeoTest() {
        int number_of_revenues_expected = 5;
        String date_debut = "2000-01-01";
        String date_fin = "2017-11-11";
        try {
            int number_of_revenue_actual = myDAO.getSalesByGeo(date_debut, date_fin).size();
            assertEquals(number_of_revenue_actual, number_of_revenues_expected);
        } catch (SQLException ex) {
        } catch (ParseException ex) {
        }
    }

    @Test
    public void getSalesByProductCatTest() throws ParseException {
        int number_of_revenues_expected = 5;
        String date_debut = "2000-01-01";
        String date_fin = "2017-11-11";
        try {
            List<Revenue> list = myDAO.getSalesByProductCat(date_debut, date_fin);
            int number_of_revenue_actual = list.size();
            assertEquals(number_of_revenue_actual, number_of_revenues_expected);
        } catch (SQLException ex) {
        } catch (ParseException ex) {
        }
    }

    @After
    public void tearDown() throws SQLException {
        myConnection.close();
        myDAO = null; // Pas vraiment utile
    }

    public static DataSource getDataSource() throws SQLException {
        org.hsqldb.jdbc.JDBCDataSource ds = new org.hsqldb.jdbc.JDBCDataSource();
        ds.setDatabase("jdbc:hsqldb:mem:testcase;shutdown=true");
        ds.setUser("sa");
        ds.setPassword("sa");
        return ds;
    }
}
