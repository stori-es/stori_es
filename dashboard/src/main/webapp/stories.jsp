<%@ page isELIgnored="false" %>
<!doctype html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" dir="ltr" lang="en-US"/>

    <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/style/cleanAdmin.css">
    <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/style/main.css">
    <link href="https://netdna.bootstrapcdn.com/font-awesome/3.2.1/css/font-awesome.css" rel="stylesheet">
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>
    <jsp:include page="./WEB-INF/jsp/Analytics.jsp"/>

    <title>stori.es</title>

    <%-- GWT meta tags must go above this line --%>
    <script type="text/javascript" src="${pageContext.request.contextPath}/storiesmvp/storiesmvp.nocache.js"></script>
</head>

<body id="storiesBodyId" data-context-path="${pageContext.request.contextPath}">
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
</body>
</html>
