<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<% request.setCharacterEncoding(
        "UTF-8"); // the charset stuff should set this on the Tomcat Connectors config, but for backup... except it seems ineffective %>
<% final String context = request.getContextPath().replace("^\\s*https://", "http://").trim(); %>
<% final String realContext = request.getContextPath().trim(); %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" dir="ltr" lang="en-US"/>
    <title>stori.es | <%=(String) request.getAttribute("title") %>
    </title>

    <link rel="stylesheet" type="text/css" media="all" href="<%= realContext %>/style/main.css"/>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>

    <%-- SYS --%>
    <script type="text/javascript" src="<%= context %>/questionnairemvp/questionnairemvp.nocache.js"></script>
    <link rel="stylesheet" type="text/css" media="all" href="<%= realContext %>/style/main.css"/>
    <link rel="stylesheet" type="text/css" media="all" href="<%= realContext %>/template/buzz/pfc.css"/>
    <link href="https://netdna.bootstrapcdn.com/font-awesome/3.1.1/css/font-awesome.css" rel="stylesheet">

    <script type="text/javascript">
        var CKEDITOR_BASEPATH = '${pageContext.request.contextPath}/questionnaire/ckeditor/';
    </script>

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

    <link rel="stylesheet" type="text/css" media="all" href="<%=realContext %>/template/buzz/style.css"/>
    <link rel="stylesheet" type="text/css" media="all" href="<%= context %>/style/vertical-check.css"/>
    <jsp:include page="./../WEB-INF/jsp/Analytics.jsp"/>

    <!--[if lt IE 9]>
    <script src="https://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->

    <style type="text/css">
        .broken_link, a.broken_link {
            text-decoration: line-through;
        }

        table.stories-inputGroup, td.cu-questionnaireHeader div {
            *margin-bottom: 17px;
        }

        .cu-collection-survey-thak-you-label {
            *font-size: 20px;
            *font-weight: bold;
            *margin: 10px 0;
            *text-align: center;
        }

        #questionnaireWidget div > table {
            *width: 100%;
        }

        iframe.hasRichTextToolbar {
            *border: 0.5px solid black;
            *height: 160px;
            *width: 463px;
        }

        .gwt-RichTextToolbar {
            *border-bottom-style: none;
        }
    </style>

</head>
<body class="home blog cat-7-id" data-context-path="${pageContext.request.contextPath}">
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

<div style="display:none"><!-- hidden, but picked up by Facebook -->
    <img alt="Share this site!" src="<%= realContext %>/template/share/sharing-widget.jpg" width="150" height="200"/>
    Consumers Union is an expert, independent, nonprofit organization whose mission is to work for a fair, just, and
    safe marketplace for all consumers and to empower consumers to protect themselves.
</div>


<div id="main">
    <div class="cu-logo"><a href="http://www.consumersunion.org"></a></div>
    <header>
        <div id="header" class="container">
            <div id="header-content">
                <div id="header-image">
                    <h1>stori.es</h1>

                    <p>The stori.es Platform for Digital Storytelling</p>
                    <a href="https://stori.es/buzz/" title="The stori.es Platform for Digital Storytelling"></a>
                </div>
                <%--#header-image--%>
                <div class="clear"></div>
            </div>
        </div>
    </header>

    <div class="clear"></div>
    <div id="body" class="container">
        <div id="home">
            <div id="home-content">
                <div id="questionnaireWidget"><%--#SYS Widget Content--%></div>
                <div id="poweredBy-content" class="poweredBy-style">
                    <a href="https://stori.es/"><img alt="Powered by stori.es"
                                                     src="<%= realContext %>/images/powered-by-stories.png"/></a>
                </div>
            </div>
        </div>
    </div>
    <div class="clear"></div>
    <footer>
        <div id="footer" class="container">
            <div class="copyright">
                &copy; 2012 <a href="http://www.consumersunion.org/">Consumers Union</a>.
                Our <a href="http://www.consumersunion.org/about/privacy.htm">Privacy Policy</a>.
            </div>
            <div class="clear"></div>
        </div>
    </footer>
</div>
</body>
</html>
