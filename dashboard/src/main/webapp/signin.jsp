<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<% request.setCharacterEncoding(
        "UTF-8"); // the charset stuff should set this on the Tomcat Connectors config, but for backup... except it seems ineffective %>
<% final String context = request.getContextPath().replace("^\\s*https://", "http://").trim(); %>
<% final String realContext = request.getContextPath().trim(); %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" dir="ltr" lang="en-US"/>
    <meta property="og:image" content="<%= realContext %>/template/share/sharing-widget.jpg"/>
    <meta property="og:image:width" content="150"/>
    <meta property="og:image:height" content="200"/>
    <meta property="description" content="Consumers Union is an expert, independent, nonprofit organization whose mission is to work for a fair, just, and
    safe marketplace for all consumers and to empower consumers to protect themselves."/>

    <title>Sign in</title>

    <link rel="stylesheet" type="text/css" media="all" href="<%= realContext %>/style/main.css"/>
    <link href="https://netdna.bootstrapcdn.com/font-awesome/3.2.1/css/font-awesome.css" rel="stylesheet">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>

    <script type="text/javascript" src="login/login.nocache.js"></script>

    <link rel="stylesheet" type="text/css" media="all" href="<%=realContext %>/template/buzz/style.css"/>
</head>
<body class="home blog cat-7-id" data-context-path="<%= realContext %>">
<%-- necessary for GWT history support --%>
<iframe src="javascript:''" id="__gwt_historyFrame" tabIndex='-1'
        style="position:absolute;width:0;height:0;border:0"></iframe>
<%-- JavaScript is an absolute must --%>
<noscript>
    <div style="width: 22em; position: absolute; left: 50%; margin-left: -11em; color: red; background-color: white; border: 1px solid red; padding: 4px; font-family: sans-serif">
        Your web browser must have JavaScript enabled
        in order for this application to display correctly.
    </div>
</noscript>

<form id="appLogin" action="javascript:void(0);" style="display: none">
    <input type="text" name="j_username" placeholder="Username / email"/>
    <input type="password" name="j_password" placeholder="Password"/>
    <input type="submit" name="submitButton" value="Sign in"/>
</form>
</body>
</html>
