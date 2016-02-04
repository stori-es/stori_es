﻿
<%@ page isELIgnored="false" %>
<!doctype html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" dir="ltr" lang="en-US"/>

    <title>stori.es</title>

    <link href="//netdna.bootstrapcdn.com/font-awesome/3.1.1/css/font-awesome.css" rel="stylesheet">

    <jsp:include page="./WEB-INF/jsp/Analytics.jsp"/>

    <script type="text/javascript">
        var CKEDITOR_BASEPATH = '${pageContext.request.contextPath}/surveymvp/ckeditor/';
        var storiesConfig = {corsDomain: "127.0.0.1", corsPort: "8888"};
    </script>
    <script type="text/javascript" src="surveymvp/surveymvp.nocache.js"></script>
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

<div>
    <div style="width: 300px; float: left; margin-right: 10px;">
        <p>
            Lorem ipsum dolor sit amet, ea fierent mediocritatem sit, nec cu melius omnium. Qui quidam officiis
            dissentiunt at. Qui eu voluptua intellegebat, te nec unum veri soleat, ius amet commune delicata ne.
            Adversarium vituperatoribus mel cu. Ex mel case labore theophrastus. Et nihil detracto mel,
            sed ex rebum alterum, solet doctus omittantur sea id.
        </p>

        <p>
            Vix ne recteque percipitur, has dicit percipit eu, ei modus appareat consectetuer vel.
            Aliquam dissentiet in quo. Ius eu persius fabulas, primis recusabo sea id. Has tincidunt omittantur
            adversarium at, pri munere assentior percipitur no, cu error veniam nostrud mea. Ei eam cibo mentitum,
            ea augue suscipiantur pro. Pro et case liber intellegam, affert instructior in qui,
            ut graece persequeris usu.
        </p>
    </div>

    <div id="survey" class="stories-questionnaire-a-voice-from-the-tennessee-gap"
         style="width: 600px; float: left;"></div>

    <div style="clear: both;"></div>
</div>
</body>
</html>
