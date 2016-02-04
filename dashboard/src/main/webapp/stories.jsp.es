<%@ page isELIgnored ="false" %>
<!doctype html>
<!-- The DOCTYPE declaration above will set the     -->
<!-- browser's rendering engine into                -->
<!-- "Standards Mode". Replacing this declaration   -->
<!-- with a "Quirks Mode" doctype is not supported. -->

<html>
  <head>
    <meta name="gwt:property" content="locale=es" />
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <link type="text/css" rel="stylesheet" href="style/main.css">

    <title>stori.es</title>
    <!-- GWT meta tags must go above this line -->
    <script type="text/javascript" language="javascript" src="stories/stories.nocache.js"></script>
  </head>
  <body data-context-path="${pageContext.request.contextPath}">
    <!-- necessary for GWT history support -->
    <iframe src="javascript:''" id="__gwt_historyFrame" tabIndex='-1' style="position:absolute;width:0;height:0;border:0"></iframe>
    <!-- JavaScript is an absolute must -->
    <noscript>
      <div style="width: 22em; position: absolute; left: 50%; margin-left: -11em; color: red; background-color: white; border: 1px solid red; padding: 4px; font-family: sans-serif">
        Your web browser must have JavaScript enabled
        in order for this application to display correctly.
      </div>
    </noscript>

    <div id="appMain"></div>
  </body>
</html>
