<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
    <head>
        <title>Bienvenue sur l'application de gestion de commandes</title>
        <link rel="stylesheet" href="css/logPagecss.css" type="text/css">
        <script>
            function validateForm(event)
            {
                var regex = /^[0-9]+$/;
                event.preventDefault();
                //on vérifie que le champ user n'est pas vide
                if (document.loginform.loginParam.value === "")
                {
                    alert("Veuillez indiquer un nom d'utilisateur");
                    return false;
                    //on vérifie que les données rentrées dans le champ password est bien un nombre
                } else if (!((document.loginform.passwordParam.value).match(regex))) {
                    alert("Le mot de passe doit être un nombre");
                    return false;
                    //si tout est vérifié, on envoie le submit
                } else {
                    document.loginform.submit();
                }
            }
        </script>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    </head>
    <body>
        <div class="login-page">
            <div class="logEncart">
                <h1>Login</h1>
                <!--formulaire qui va envoyer les paramètres loginParam et passwordParam pour l'authentification à la servlet logServlet-->
                <form method='POST' name='loginform' onSubmit='return validateForm(event);'>
                    <input name="loginParam" placeholder="username"><br/>
                    <input type="password" name="passwordParam"  placeholder="password"><br/>
                    <input type="hidden" name="action" value="login">
                    <button type="submit" value="S'identifier">S'identifier</button>
                </form>
            </div>
            <div class='errorMessage'><h1>${errorMessage}</h1></div>
        </div>

    </body>
</html>
