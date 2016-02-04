<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<% request.setCharacterEncoding("UTF-8"); %>
<% String context = request.getContextPath().replace("^\\s*https://", "http://").trim(); %>
<% String realContext = request.getContextPath().trim(); %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" dir="ltr" lang="en-US"/>
    <title>Unsubscribe</title>

    <link rel="stylesheet" type="text/css" media="all" href="<%= realContext %>/style/main.css"/>
    <link rel="stylesheet" type="text/css" media="all" href="<%= realContext %>/template/buzz/pfc.css"/>
    <link rel="stylesheet" type="text/css" media="all" href="<%=realContext %>/template/buzz/style.css"/>
    <link rel="stylesheet" type="text/css" media="all" href="<%= context %>/style/vertical-check.css"/>
    <style>
        #home-content {
            font-family: "Droid Sans", sans-serif;
            margin: 0 0 40px 20px;
        }

        #home-content h2 {
            color: #000;
            font-weight: bold;
        }

        #home-content a {
            color: #fff;
            background-color: #332b20;
            font-weight: normal;
            font-size: 14px;

            display: inline-block;
            padding: 8px 10px;
            margin-top: 24px;
            border: none;
        }

        #home-content a:hover, #home-content a:visited {
            color: #fff;
            text-decoration: none;
        }
    </style>
</head>
<body class="home blog cat-7-id" data-context-path="<%= realContext %>">

<div id="main">
    <div class="cu-logo"><a href="http://www.consumersunion.org"></a></div>
    <header>
        <div id="header" class="container">
            <div id="header-content">
                <div id="header-image">
                    <h1>stori.es</h1>

                    <p>The stori.es Platform for Digital Storytelling</p>
                    <a href="http://www.stori.es/" title="The stori.es Platform for Digital Storytelling"></a>
                </div>
                <%--#header-image--%>
                <div class="clear"></div>
            </div>
        </div>
    </header>
</div>
<div class="clear"></div>
<div id="body" class="container">
    <div id="home">
        <div id="home-content">
            <h2>Unsubscribe</h2>

            <div>
                You have been successfully unsubscribed from notifications about the addition of content to the stori.es
                collection "<%= request.getAttribute("collectionTitle") %>".
            </div>
            <a href="<%= context %>/signin.jsp">Sign in to stories</a>
        </div>
        <div class="clear"></div>
        <footer>
            <div id="footer" class="container">
                <div class="copyright">
                    &copy; 2013 <a href="http://www.consumersunion.org/">Consumers Union</a>.
                    Our <a href="http://www.stori.es/privacy-policy">Privacy Policy</a>,
                    <a href="http://www.stori.es/terms-of-service">Terms of Service</a> and
                    <a href="http://www.stori.es/software-as-a-service-agreement">Software as a Service Agreement</a>
                </div>
                <div class="clear"></div>
            </div>
        </footer>
</body>
</html>
