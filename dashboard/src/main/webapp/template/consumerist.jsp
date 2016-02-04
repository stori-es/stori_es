<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<% request.setCharacterEncoding(
        "UTF-8"); // the charset stuff should set this on the Tomcat Connectors config, but for backup... except it seems ineffective %>
<% final String context = request.getContextPath().replace("^\\s*https://", "http://").trim(); %>
<% final String realContext = request.getContextPath().trim(); %>
<!--[if lt IE 7]> <html class="no-js lt-ie9 lt-ie8 lt-ie7" dir="ltr" lang="en-US" xmlns:og="http://opengraphprotocol.org/schema/" xmlns:fb="http://www.facebook.com/2008/fbml"> <![endif]-->
<!--[if IE 7]> <html class="no-js lt-ie9 lt-ie8" dir="ltr" lang="en-US" xmlns:og="http://opengraphprotocol.org/schema/" xmlns:fb="http://www.facebook.com/2008/fbml"> <![endif]-->
<!--[if IE 8]> <html class="no-js lt-ie9" dir="ltr" lang="en-US" xmlns:og="http://opengraphprotocol.org/schema/" xmlns:fb="http://www.facebook.com/2008/fbml"> <![endif]-->
<!--[if gt IE 8]><!-->
<html class="no-js" dir="ltr" lang="en-US" xmlns:og="http://opengraphprotocol.org/schema/"
      xmlns:fb="http://www.facebook.com/2008/fbml" xmlns="http://www.w3.org/1999/xhtml">
<!--<![endif]-->
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <title>stori.es | <%=(String) request.getAttribute("title") %>
    </title>
    <link rel="alternate" type="application/rss+xml" title="The Consumerist Feed" href="http://consumerist.com/feed/"/>
    <link rel="alternate" type="application/rss+xml" title="The Consumerist &raquo; Tell Us Your Story Comments Feed"
          href="http://consumerist.com/tell-us-your-story/feed"/>
    <meta property="fb:admins" content=""/>
    <meta property="og:title" content="Tell Us Your Story"/>
    <meta property="og:type" content="article"/>
    <meta property="og:site_name" content="The Consumerist"/>
    <meta property="og:image" content="http://consumerist.com/"/>

    <%--SYS Scripts --%>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>

    <!--[if lt IE 9]>
    <script src="https://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->

    <script type="text/javascript" src="<%= context %>/questionnairemvp/questionnairemvp.nocache.js"></script>

    <script type="text/javascript">
        var CKEDITOR_BASEPATH = '${pageContext.request.contextPath}/questionnaire/ckeditor/';
    </script>

    <link rel="stylesheet" type="text/css" media="all" href="<%= realContext %>/style/main.css"/>
    <link rel="stylesheet" type="text/css" media="all" href="<%= realContext %>/style/vertical-check.css"/>
    <link href="https://netdna.bootstrapcdn.com/font-awesome/3.1.1/css/font-awesome.css" rel="stylesheet">
    <style type="text/css" media="screen"><!--
    #questionnaireWidget {
        width: 650px;
    }

    --></style>

    <link rel='stylesheet' id='normalize-css' href='<%= realContext %>/template/consumerist/normalize.css'
          type='text/css' media='all'/>
    <link rel='stylesheet' id='main-styles-css' href='<%= realContext %>/template/consumerist/style.css' type='text/css'
          media='all'/>
    <link rel='stylesheet' id='jquery-ui-css' href='<%= realContext %>/template/consumerist/jquery-ui.css'
          type='text/css' media='all'/>

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

    <style type="text/css">
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

    <jsp:include page="./../WEB-INF/jsp/Analytics.jsp"/>

    <script type='text/javascript' src='<%= realContext %>/template/consumerist/modernizr-2.5.3.min.js'></script>
    <script type='text/javascript' src='<%= realContext %>/template/consumerist/jquery.cookie.js'></script>
    <script type='text/javascript'>
        var notices_ajax_script = {
            "ajaxurl": "http:\/\/consumerist.com\/wp-admin\/admin-ajax.php",
            "logged_in": "no"
        };
    </script>
    <script type='text/javascript' src='<%= realContext %>/template/consumerist/notifications.js'></script>
    <link rel="EditURI" type="application/rsd+xml" title="RSD" href="http://consumerist.com/xmlrpc.php?rsd"/>
    <link rel="wlwmanifest" type="application/wlwmanifest+xml"
          href="http://consumerist.com/wp-includes/wlwmanifest.xml"/>
    <link rel='prev' title='Edit Profile' href='http://consumerist.com/edit-profile'/>
    <link rel='next' title='Thanks for Registering' href='http://consumerist.com/register/thanks-for-registering'/>
</head>
<body class="page page-id-18594 page-template-default" data-context-path="${pageContext.request.contextPath}">

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

<div id="bodywrapper">
    <div class="container">
        <header id="banner" role="banner">
            <h1 class="site-title">
                <a href="http://consumerist.com" title="The Consumerist - Home">The Consumerist: Shoppers Bite Back</a>
            </h1>

            <div class="headsocial"></div>
            <!-- end .headsocial -->
            <div class="search-container">
                <a class="donate"
                   href="https://consumerreports.secure-donor.com/consumerist?source=3090000005">Donate</a>

                <form method="get" action="http://consumerist.com/">
                    <input type="hidden" name="IncludeBlogs" value="1"/>
                    <input type="hidden" name="limit" value="20"/>
                    <input type="text" id="search" class="query" name="s" value=""/>
                    <input id="search-submit" type="submit" value=" "/>
                </form>
            </div>
            <!-- end .search-container -->
            <div class="widget-sign-in widget">
                <div id="signin-widget-content" class="widget-content">
                    <a class="h-login" href="http://consumerist.com/login/">Login</a>
                    <a class="h-register" href="http://consumerist.com/register/">Register</a>
                </div>
                <!-- end #sigin-widget-content -->
            </div>
            <!-- end .widget-sign-in --> </header>
        <div id="content-container">
            <div id="content">
                <div id="body" class="container">
                    <div id="home">
                        <div id="home-content">
                            <div id="questionnaireWidget"><%--#SYS Widget Content--%></div>
                        </div>
                    </div>
                </div>
            </div>
            <!-- end .entry-content -->
            <footer class="entry-meta">
            </footer>
            <!-- end .entry-meta -->

            <div class="clear"></div>
        </div>
        <!-- end #content -->


        <aside id="sidebar" class="widget-area" role="complementary">
            <section id="social-widget-2" class="widget social-widget">
                <a id="rss" href="http://consumerist.com/feed/rss" target="_blank">RSS</a>
                <a id="twitter" href="http://twitter.com/consumerist" target="_blank">Twitter</a>
                <a id="facebook" href="http://www.facebook.com/theconsumerist?ref=ts" target="_blank">Facebook</a>
            </section>
            <section id="ad300x150-2" class="widget ad300x150">
                <iframe height="150" frameborder="0" width="300" scrolling="no" class="rightads" marginwidth="0"
                        marginheight="0" id="Right1" name="Right1"
                        src="https://oascentral.consumerreports.org/RealMedia/ads/adstream_sx.ads/consumerist/homepage/1111111111@Middle,Right1,Right2!Right1?article=112345&amp;XE&amp;status=active&amp;if_nt_CookieAccept=Y&amp;XE">
                </iframe>
            </section>
            <section id="ad125x125-2" class="widget ad125x125">
                <div id="first">
                    <iframe height="125" frameborder="0" width="125" scrolling="no" class="rightads" marginwidth="0"
                            marginheight="0" id="Right2" name="Right2"
                            src="https://oascentral.consumerreports.org/RealMedia/ads/adstream_sx.ads/consumerist/homepage/1234567890@Middle,Right1,Right2,Right3!Right2?article=112345&amp;XE&amp;status=active&amp;if_nt_CookieAccept=Y&amp;XE">
                    </iframe>
                </div>
                <div id="last">
                    <iframe height="125" frameborder="0" width="125" scrolling="no" class="rightads" marginwidth="0"
                            marginheight="0" id="Right3" name="Right3"
                            src="https://oascentral.consumerreports.org/RealMedia/ads/adstream_sx.ads/consumerist/homepage/1234567890@Middle,Right1,Right2,Right3!Right3?article=112345&amp;XE&amp;status=active&amp;if_nt_CookieAccept=Y&amp;XE">
                    </iframe>
                </div>
            </section>
            <section id="contact-widget-2" class="widget contact-widget">
                <h3 class="widget-title">Contact Us</h3>
                <ul>
                    <li>Comments Moderator: <a href="mailto:moderator@consumerist.com">EMAIL</a> | <a target="_blank"
                                                                                                      href="aim:goim?screenname=consumeristroz&amp;message=type+your+tip+here">AIM</a>
                    </li>
                    <li>Media Inquiries: <a href="mailto:media@consumerist.com">EMAIL</a></li>
                    <li>Tips to the Editors: <a href="mailto:tips@consumerist.com">EMAIL</a></li>
                    <li>Technical Support: <a href="mailto:support@consumerist.com">EMAIL</a></li>
                    <li>Permissions: <a href="mailto:permissions@consumermediallc.org">EMAIL</a></li>
                </ul>
            </section>
            <section id="ad300x150-3" class="widget ad300x150">
                <a href="https://consumerreports.secure-donor.com/consumerist?source=3090000005" target="_blank">
                    <img src="<%= realContext %>/template/consumerist/donate1300x150.jpg"/>
                </a>
            </section>
        </aside>
        <!-- end #sidebar -->
        <div class="clear"></div>
    </div>
    <!-- end #content-container -->
</div>
<!-- end .container -->
<div id="poweredBy-content" class="poweredBy-style">
    <a href="https://stori.es/"><img alt="Powered by stori.es" src="<%= realContext %>/images/powered-by-stories.png"/></a>
</div>
<footer id="footer" role="contentinfo">
    <div class="container">
        <div class="man"></div>
        <nav class="footer-nav">
            <div class="menu-footer-menu-container">
                <ul id="menu-footer-menu" class="menu">
                    <li id="menu-item-10087768"
                        class="menu-item menu-item-type-post_type menu-item-object-page menu-item-10087768"><a
                            href="http://consumerist.com/about-us">About/Contact Us</a></li>
                    <li id="menu-item-10087769"
                        class="menu-item menu-item-type-post_type menu-item-object-page menu-item-10087769"><a
                            href="http://consumerist.com/privacy-policy">Privacy Policy</a></li>
                    <li id="menu-item-10087770"
                        class="menu-item menu-item-type-post_type menu-item-object-page menu-item-10087770"><a
                            href="http://consumerist.com/comments-code">Comments Code</a></li>
                    <li id="menu-item-10087771"
                        class="menu-item menu-item-type-post_type menu-item-object-page menu-item-10087771"><a
                            href="http://consumerist.com/no-commercial-use">No Commercial Use</a></li>
                    <li id="menu-item-10087772"
                        class="menu-item menu-item-type-post_type menu-item-object-page menu-item-10087772"><a
                            href="http://consumerist.com/user-agreement">User Agreement</a></li>
                    <li><a href="http://www.consumerreports.org/" target="_blank" class="cr"><img
                            src="<%= realContext %>/template/consumerist/footer_from_new.gif"
                            alt="in partnership with Consumer Reports" width="199" height="15"></a></li>
                </ul>
            </div>
        </nav>
        <div class="copyright">&copy; 2005-2012 Consumer Media LLC except where noted.</div>
        <!-- end .copyright -->
    </div>
    <!-- end .container -->
</footer>

<div id="donate-button">
    <a href="https://consumerreports.secure-donor.com/consumerist?source=3090000005"> <img
            src="<%= realContext %>/template/consumerist/donate.gif" width="22" height="73" alt="Donate"></a>
</div>
<!-- end #donate-button -->

<script type='text/javascript' src='<%= realContext %>/template/consumerist/slides.min.jquery.js'></script>
<script type='text/javascript' src='<%= realContext %>/template/consumerist/scripts.js'></script>


<!-- Chartbeat Analytics -->
<script type="text/javascript">
    var _sf_async_config = {};
    _sf_async_config.uid = 28487;
    _sf_async_config.domain = "consumerist.com";
    (function () {
        function loadChartbeat() {
            window._sf_endpt = (new Date()).getTime();
            var e = document.createElement("script");
            e.setAttribute("language", "javascript");
            e.setAttribute("type", "text/javascript");
            e.setAttribute(
                    "src",
                    (("https:" == document.location.protocol) ? "https://a248.e.akamai.net/chartbeat.download.akamai.com/102508/"
                            : "http://static.chartbeat.com/")
                    + "js/chartbeat.js");
            document.body.appendChild(e);
        }

        var oldonload = window.onload;
        window.onload = (typeof window.onload != "function") ? loadChartbeat
                : function () {
            oldonload();
            loadChartbeat();
        };
    })();
</script>

</body>
</html>
