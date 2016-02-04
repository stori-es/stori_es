<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<% request.setCharacterEncoding("UTF-8"); %>
<% final String context = request.getContextPath().replace("^\\s*https://", "http://").trim(); %>
<% final String realContext = request.getContextPath().trim(); %>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en-US" data-ember-extension="1">
<head profile="http://gmpg.org/xfn/11">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Tennessee Justice Center — Serving Families in Need</title>
    <meta name="robots" content="noodp, noydir">
    <meta name="description" content="Serving Families in Need">

    <link rel="shortcut icon" href="/template/tjc/tjclogo1.png">
    <link rel="canonical" href="tjc.jsp">
    <link rel="alternate" type="application/rss+xml" title="Tennessee Justice Center RSS Feed"
          href="http://www.tnjustice.org/feed/">
    <link rel="alternate" type="application/rss+xml" title="Tennessee Justice Center »  Comments Feed"
          href="http://www.tnjustice.org/main/feed/">
    <link rel="pingback" href="http://www.tnjustice.org/xmlrpc.php">

    <link rel="stylesheet" href="/template/tjc/style.css" type="text/css" media="screen, projection">
    <link rel="stylesheet" href="/template/tjc/layout.css" type="text/css" media="screen, projection">
    <!--[if lte IE 8]>
    <link rel="stylesheet" href="/template/tjc/ie.css?121110-143423" type="text/css" media="screen, projection"/>
    <![endif]-->
    <link rel="stylesheet" href="/template/tjc/custom.css" type="text/css" media="screen, projection">

    <script type="text/javascript" async="" src="/tjc/ga.js"></script>
    <script type="text/javascript">
        var _gaq = _gaq || [];
        _gaq.push(['_setAccount', 'UA-21892405-1']);
        _gaq.push(['_trackPageview']);
        (function () {
            var ga = document.createElement('script');
            ga.type = 'text/javascript';
            ga.async = true;
            ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
            var s = document.getElementsByTagName('script')[0];
            s.parentNode.insertBefore(ga, s);
        })();
        //]]></script>

    <!--
    Plugin: Google meta tag Site Verification Plugin
    Tracking Code.
    -->
    <meta name="google-site-verification" content="s6OCLFYluiLXmi53OHlxj6W20gJiTZzZE3dk9RGNjNE">

    <%-- stori.es variables --%>
    <script type="text/javascript">
        var CKEDITOR_BASEPATH = '${pageContext.request.contextPath}/questionnaire/ckeditor/';
    </script>

    <script type="text/javascript">
        //Dictionary used by Survey Entry Point
        var requestParameters = {
            collectionId: "<%= (String)request.getAttribute("collectionId") %>",
            permalink: "<%= (String)request.getAttribute("permalink") %>",
            isQuestionnaire: "<%= (String)request.getAttribute("isQuestionnaire") %>",
            hasStyle: "<%= (String)request.getAttribute("hasStyle") %>"
        };
    </script>
    <jsp:include page="./../WEB-INF/jsp/Analytics.jsp"/>

    <!--[if lt IE 9]>
    <script src="https://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->
    <%-- SYS --%>
    <script type="text/javascript" src="<%= context %>/questionnairemvp/questionnairemvp.nocache.js"></script>
</head>
<body class="custom main home_dude" data-ember-extension="1" cz-shortcut-listen="true">
<%-- GWT history support --%>
<iframe src="javascript:''" id="__gwt_historyFrame" tabIndex='-1'
        style="position: absolute; width: 0; height: 0; border: 0"></iframe>

<%-- JavaScript required --%>
<noscript>
    <div style="width: 22em; position: absolute; left: 50%; margin-left: -11em; color: red; background-color: white; border: 1px solid red; padding: 4px; font-family: sans-serif">
        Your web browser must have JavaScript enabled
        in order for this application to display correctly.
    </div>
</noscript>

<div id="container">
    <div id="page">
        <div id="header">
            <p id="header_links">

                <a href="http://feeds.feedburner.com/TennesseeJustice" target="_blank"><img src="/template/tjc/rss.png"
                                                                                            alt="RSS Icon"
                                                                                            width="28"></a>

                <a href="http://www.facebook.com/tnjustice" target="_blank"><img src="/template/tjc/facebook.png"
                                                                                 alt="Facebook Icon" width="28"></a>

            </p></div>
        <div class="menu-main-container">
            <ul id="menu-main" class="menu">
                <li id="menu-item-504"
                    class="menu-item menu-item-type-post_type menu-item-object-page current-menu-item page_item page-item-292 current_page_item menu-item-504">
                    <a title="Home" href="/template/tjc/tennessee_justice_center.html">Home</a></li>
                <li id="menu-item-505" class="menu-item menu-item-type-post_type menu-item-object-page menu-item-505"><a
                        href="http://www.tnjustice.org/about/">About</a></li>
                <li id="menu-item-510" class="menu-item menu-item-type-post_type menu-item-object-page menu-item-510"><a
                        href="http://www.tnjustice.org/help/">Need Help?</a></li>
                <li id="menu-item-512" class="menu-item menu-item-type-post_type menu-item-object-page menu-item-512"><a
                        href="http://www.tnjustice.org/resources/">Issues &amp; Resources</a></li>
                <li id="menu-item-508"
                    class="menu-item menu-item-type-post_type menu-item-object-page menu-item-has-children menu-item-508">
                    <a href="http://www.tnjustice.org/get-involved/">Get Involved</a>
                    <ul class="sub-menu">
                        <li id="menu-item-5340"
                            class="menu-item menu-item-type-post_type menu-item-object-page menu-item-5340"><a
                                href="http://www.tnjustice.org/get-involved/volunteer-2/">Volunteer</a></li>
                    </ul>
                </li>
                <li id="menu-item-513"
                    class="menu-item menu-item-type-taxonomy menu-item-object-category menu-item-513"><a
                        href="http://www.tnjustice.org/category/press/">Press</a></li>
                <li id="menu-item-511" class="menu-item menu-item-type-post_type menu-item-object-page menu-item-511"><a
                        href="http://www.tnjustice.org/clients/">Clients</a></li>
                <li id="menu-item-506" class="menu-item menu-item-type-post_type menu-item-object-page menu-item-506"><a
                        href="http://www.tnjustice.org/blog/">Blog</a></li>
                <li id="menu-item-509" class="menu-item menu-item-type-post_type menu-item-object-page menu-item-509"><a
                        href="http://www.tnjustice.org/donate/">Donate</a></li>
                <li id="menu-item-507" class="menu-item menu-item-type-post_type menu-item-object-page menu-item-507"><a
                        href="http://www.tnjustice.org/contact/">Contact</a></li>
            </ul>
        </div>
        <div id="feature_box">
        </div>
        <div id="content_box" class="no_sidebars">
            <div id="content">

                <div class="post_box top" id="post-292">
                    <div class="headline_area">
                        <h2></h2>
                    </div>
                    <div class="format_text">
                        <div id="questionnaireWidget"><%-- stori.es content here --%></div>
                    </div>
                </div>
            </div>

        </div>
    </div>
</div>
<!--[if lte IE 8]>
<div id="ie_clear"></div>
<![endif]-->
<center>
    Tennessee Justice Center | 301 Charlotte Ave | Nashville, TN 37201
    <br>
    <a href="http://www.facebook.com/tnjustice" target="_blank"><img src="/template/tjc/facebook.png"
                                                                     alt="Facebook Icon" width="28"></a>
</center>
</body>
</html>
