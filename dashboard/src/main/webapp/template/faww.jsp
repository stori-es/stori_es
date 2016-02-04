<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<% request.setCharacterEncoding("UTF-8"); %>
<% final String context = request.getContextPath().replace("^\\s*https://", "http://").trim(); %>
<% final String realContext = request.getContextPath().trim(); %>
<!DOCTYPE html>
<html>
<head>
    <title>stori.es | <%=(String) request.getAttribute("title") %>
    </title>
    <script type="text/javascript" src="<%= context %>/questionnairemvp/questionnairemvp.nocache.js"></script>
    <jsp:include page="./../WEB-INF/jsp/Analytics.jsp"/>
    <script type="text/javascript">
        var CKEDITOR_BASEPATH = '${pageContext.request.contextPath}/questionnaire/ckeditor/';
    </script>
    <link rel="stylesheet" type="text/css" media="all" href="<%= realContext %>/style/main.css"/>
    <link href="https://netdna.bootstrapcdn.com/font-awesome/3.1.1/css/font-awesome.css" rel="stylesheet">

    <%-- SYS global variables --%>
    <script type="text/javascript">
        //Dictionary used by Survey Entry Point
        var requestParameters = {
            collectionId: "<%= (String)request.getAttribute("collectionId") %>",
            permalink: "<%= (String)request.getAttribute("permalink") %>",
            isQuestionnaire: "<%= (String)request.getAttribute("isQuestionnaire") %>",
            hasStyle: "<%= (String)request.getAttribute("hasStyle") %>"
        };
    </script>

    <link href="<%= realContext %>/template/faww/ResponsiveBase.css" rel="stylesheet" type="text/css">
    <link href="<%= realContext %>/template/faww/DonFormResponsive.css" rel="stylesheet" type="text/css">
    <link rel="shortcut icon" href="https://secure.foodandwaterwatch.org/images/wrpr/FaviconBlue.ico"/>

    <style>
        #stories-submit-button {
            color: #9cdf46;
            background: black;

            font-family: "Oswald";
            font-size: 1.1em;
            text-decoration: none;
            border-radius: 0;
            border: none;

            margin-top: 20px;
        }
    </style>
</head>
<body class="page">
<iframe src="javascript:''" id="__gwt_historyFrame" tabIndex='-1'
        style="position:absolute;width:0;height:0;border:0"></iframe>
<noscript>
    <div style="width: 22em; position: absolute; left: 50%; margin-left: -11em; color: red; background-color: white; border: 1px solid red; padding: 4px; font-family: sans-serif">
        Your web browser must have JavaScript enabled
        in order for this application to display correctly.
    </div>
</noscript>

<link rel="image_src"
      href="https://secure.foodandwaterwatch.org/images/content/pagebuilder/FWW-logo-square-stacked.jpeg"/>
<link href="<%= realContext %>/template/faww/css" rel="stylesheet" type="text/css"/>
<link href="<%= realContext %>/template/faww/css(1)" rel="stylesheet" type="text/css"/>

<link rel="stylesheet" href="<%= realContext %>/template/faww/style.css" type="text/css" media="screen"/>
<link rel="stylesheet" href="<%= realContext %>/template/faww/print-min.css" type="text/css" media="print"/>
<div id="container">
    <div id="header">
        <div class="headercontainer">
            <div class="logo">
                <a href="http://www.foodandwaterwatch.org/#_ga=1.205290064.1596403285.1425924013">
                    <img class="logopic" src="<%= realContext %>/template/faww/FWW_logo_new.png" border="0"
                         alt="FWW_logo_new.png" width="150" height="66"/></a>
            </div>
        </div>
    </div>
    <!--end header-->
    <div class="main">
        <div id="questionnaireWidget"><%--#SYS Widget Content--%></div>
    </div>
    <div id="footer">
        <div id="footer_container">
            <div class="footer_content_column">
                <a href="http://www.foodandwaterwatch.org/"><img
                        src="<%= realContext %>/template/faww/FWW_Logo_bottom2.png" border="0"
                        alt="FWW_Logo_bottom2.png" width="138" height="100"></a></div>
            <div class="footer_content_nav">
                <a href="http://www.foodandwaterwatch.org/">HOME</a>
                <a href="http://www.foodandwaterwatch.org/about">ABOUT US</a>
                <a href="http://www.foodandwaterwatch.org/contact-us">CONTACT US</a>
                <a href="http://www.foodandwaterwatch.org/terms-of-service">TERMS OF SERVICE</a>
                <a href="http://www.foodandwaterwatch.org/privacy-policy">PRIVACY POLICY</a>
                <a href="https://secure.foodandwaterwatch.org/site/Donation2?df_id=3393&3393.donation=form1"><span>DONATE</span></a>

                <p>Food &amp; Water Watch is a 501(c)3 non-profit organization.</p>
            </div>
            <div class="mobile_nav">
                <a href="http://www.foodandwaterwatch.org">HOME</a>
                <a href="http://www.foodandwaterwatch.org/about">ABOUT US</a>
                <a href="https://secure.foodandwaterwatch.org/site/Donation2?df_id=3393&3393.donation=form1"><span>DONATE</span></a>
                <br>
                <a href="http://www.foodandwaterwatch.org/contact-us">CONTACT US</a>
                <a href="http://www.foodandwaterwatch.org/terms-of-service">TERMS OF SERVICE</a>
                <a href="http://www.foodandwaterwatch.org/privacy-policy">PRIVACY POLICY</a>
            </div>
            <div class="footer_content_column">
                <p>Food &amp; Water Watch Headquarters</p>

                <p>1616 P St. NW, Suite 300</p>

                <p>Washington, DC 20036</p>
            </div>
        </div>
    </div>
</div>
</body>
</html>
