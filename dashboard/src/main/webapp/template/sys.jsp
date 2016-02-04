<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<% request.setCharacterEncoding(
        "UTF-8"); // the charset stuff should set this on the Tomcat Connectors config, but for backup... except it seems ineffective %>
<% final String context = request.getContextPath().replace("^\\s*https://", "http://").trim(); %>
<% final String realContext = request.getContextPath().trim(); %>

<html dir="ltr" lang="en-US">
<head>
    <title>stori.es | <%=(String) request.getAttribute("title") %>
    </title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" dir="ltr" lang="en-US"/>
    <link rel="stylesheet" type="text/css" media="all" href="<%= realContext %>/style/main.css"/>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>

    <!--[if lt IE 9]>
    <script src="https://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->

    <%-- SYS --%>
    <script type="text/javascript" src="<%= context %>/questionnairemvp/questionnairemvp.nocache.js"></script>
    <script type="text/javascript">
        var CKEDITOR_BASEPATH = '${pageContext.request.contextPath}/questionnaire/ckeditor/';
    </script>
    <link rel="stylesheet" type="text/css" media="all" href="<%= realContext %>/style/main.css"/>
    <link rel="stylesheet" type="text/css" media="all" href="<%= realContext %>/template/sys/pfc.css"/>
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

    <link rel="stylesheet" type="text/css" media="all" href="<%= context %>/style/vertical-check.css"/>

    <jsp:include page="./../WEB-INF/jsp/Analytics.jsp"/>

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
    <script type="text/javascript">
        var _lastBodyHeight = -1;
        var _currentBodyHeight = 0;
        var _lastDocumentHeight = -1;
        var _currentDocumentHeight = 0;
        var _docBodyDiff = -1;
        var _iframeTimer;

        /**
         * For some reason, the 'document' does not resize when the body shrinks after switching to the 'Thank you.' page,
         * so in that case, the only way to get the embedded iframe to shrink back seems to be to recognize the shrunk
         * body and then respond with a new iframe height request (by messaging the parent). The body is slightly smaller
         * than the document, so we capture the difference in the initial sizing and then add that to the requested height
         * in the subsequent resizing. Seems like there should be a better way, but this does seem to work.
         */
        function checkHeight() {
            _lastDocumentHeight = _currentDocumentHeight;
            _currentDocumentHeight = $(document).height();
            _lastBodyHeight = _currentBodyHeight;
            _currentBodyHeight = $('body').height();
            if (_docBodyDiff < 0) {
                _docBodyDiff = _currentDocumentHeight - _currentBodyHeight;
            }
            if (_lastBodyHeight > _currentBodyHeight && _docBodyDiff > 0) {
                _currentDocumentHeight = _currentBodyHeight + _docBodyDiff;
            }
            if (_lastDocumentHeight != _currentDocumentHeight) { // update height
                window.parent.postMessage(_currentDocumentHeight, "*");
            }
        }

        $(document).ready(function () {
            _iframeTimer = setInterval("checkHeight()", 200);
        });
    </script>
</head>

<body data-context-path="${pageContext.request.contextPath}">
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

<!--  commenting out to make theme suitable for embedding
	<div id="header">
		<img alt="stori.es" src="<%= realContext %>/template/sys/stories-logo.png">
	</div>
	-->

<div id="default-main-content">
    <div id="questionnaireWidget">
        <%-- questionnaire display comes here!  --%>
    </div>
    <!--  commenting out to make theme suitable for embedding
		<div id="poweredBy-content" class="poweredBy-style">
			<a href="https://stori.es/"><img alt="Powered by stori.es" src="<%= realContext %>/images/powered-by-stories.png"/></a>
		</div>
	
		<div id="recent-stories">
			<%-- resent stories comes here!  --%>
		</div>
		-->
    <div style="clear:both"></div>
    <div id="reset"></div>
</div>
</body>
</html>
