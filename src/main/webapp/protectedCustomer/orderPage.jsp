<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="protectedCustomer/css/orderPagecss.css" type="text/css">
        <title>Gérer vos commandes</title>
        <script>
            var button1 = document.querySelector('form[name="logoutForm"] > button');
            button.addEventListener(function () {
            if (confirm){
            document.querySelector("form[name="logoutForm"]").submit();
            }
            });
            var button2 = document.querySelector('form[name="deleteForm"] > button');
            button.addEventListener(function () {
            if (confirm) {
            document.querySelector("form[name="deleteForm"]").submit();
            }
            });
            var button3 = document.querySelector('form[name="updateForm"] > button');
            button.addEventListener(function () {
            document.querySelector("form[name="updateForm"]").submit();
            });
        </script>
    </head>
    <body>
        <h1>Bienvenue ${customer_name}</h1>
        <table class='table'>
            <tr> <th>N° Commande</th> <th>N° Produit</th><th>Description</th><th>Quantité</th> <th>Tarif unitaire</th><th>Prix total</th><th>Date de vente</th><th>Date d'envoi</th><th>Compagnie de transport</th></tr>
                    <c:forEach var="order" items="${ordersResultList}">
                <tr>
                    <td>${order.order_num}</td>
                    <td>${order.product_id}</td>
                    <td>${order.description}</td>
                    <td>${order.quantity}</td>
                    <td>${order.purchase_cost}</td>
                    <td>${order.total_cost}</td>
                    <td>${order.sales_date}</td>
                    <td>${order.shipping_date}</td>
                    <td>${order.shipping_company}</td>
                    <td>
                        <form class="delete" name="deleteForm" action="deleteOrderServlet" method="post">
                            <input type="hidden" name="order_id" value="${order.order_num}">
                            <button onclick="return confirm('Êtes-vous sûr de vouloir supprimer cette commande?')">Supprimer</button>
                        </form>
                    </td>
                    <td>
                        <form class="update" name="updateForm" action="updateOrderServlet" method="post">
                            <input type="hidden" name="order_id" value="${order.order_num}">
                            <input type="hidden" name="product_id" value="${order.product_id}">
                            <input type="hidden" name="description" value="${order.description}">
                            <input type="hidden" name="quantity" value="${order.quantity}">
                            <input type="hidden" name="purchase_cost" value="${order.purchase_cost}">
                            <input type="hidden" name="total_cost" value="${order.total_cost}">
                            <button>Modifier</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </table>
        </br>
        </br>
        <div class='addEncart'>
            <h2>Ajouter une commande</h2>
            <form action="addOrderServlet" method='POST'>
                <select name="product" id="product">
                    <c:forEach var="productL" items="${productsResultList}">
                        <option value="${productL.prod_id}">${productL.prod_id} ${productL.purchase_cost}€ ${productL.description}</option>
                    </c:forEach>
                </select>
                <input name="quantity_to_add" type="number" step="1" min="1" max="500" size="5" placeholder="Quantité">
                <input type="hidden" name="customer_id" value="${customer_id}">
                <input id="addButton" type="submit" name="action" value="Ajouter">
            </form>
            <h3>${message}</h3>
        </div>
        <form class="logout" name="logoutForm" action="logServlet" method="post">
            <input type="hidden" name="action" value="logout">
            <button id=logout" onclick="return confirm('Êtes-vous sûr de vouloir vous deconnecter ?')" >Deconnexion</button>
        </form>
    </body>
</html>
