<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="protectedCustomer/css/orderDisplayPagecss.css" type="text/css">

        <title>Vous souhaitez modifier une commande</title>
    </head>
    <body>
        <div class="info">
            <h1>${customer_name}</h1><br>
            <h3>N° de Commande : ${order_id}</h3><br>
            <h3>N° de produit : ${product_id}</h3><br>
            <h3>Description : ${description}</h3><br>
            <h3>Quantité : ${quantity}</h3><br>
            <h3>Prix : ${purchase_cost}</h3><br>
            <h3>Prix total : ${total_cost}</h3>
        </div>
        <div class="addEncart">
            <h1>Pour modifier la commande</h1><br>
            <form method='POST'>
                <label for="product">Selectionnez le produit :</label>
                <select name="product_id_to_update" id="product">
                    <c:forEach var="productL" items="${productsResultList}">
                        <c:choose>
                            <c:when test="${productL.prod_id eq product_id}"><%--
                            ce if permet de selectionner par défaut le produit de la commande--%>
                                <option selected="selected" value="${productL.prod_id}">${productL.prod_id} ${productL.purchase_cost}€ ${productL.description}</option>
                            </c:when>
                            <c:otherwise>
                                <option value="${productL.prod_id}">${productL.prod_id} ${productL.purchase_cost}€ ${productL.description}</option>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </select>
                Selectionnez la quantité : <input name="quantity_to_update" type="number" step="1" min="1" max="500" size="5" value="1" >
                <input type="hidden" name="order_id_to_update" value="${order_id}">
                <input type="hidden" name="customer_id" value="${customer_id}">
                <input ID="update" type="submit" name="action" value="Modifier">
            </form>
            <a class="button" href="customerServlet">Annuler</a>
        </div>

    </body>
</html>
