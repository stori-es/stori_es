<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<% request.setCharacterEncoding("UTF-8"); %>
<% final String context = request.getContextPath().replace("^\\s*https://", "http://").trim(); %>
<% final String realContext = request.getContextPath().trim(); %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

    <title>stori.es | <%=(String) request.getAttribute("title") %>
    </title>

    <meta name="viewport" content="width=device-width, user-scalable=yes, initial-scale=1">

    <jsp:include page="./../WEB-INF/jsp/Analytics.jsp"/>

    <link type="text/css" rel="stylesheet"
          href="//fast.fonts.net/cssapi/c05bbfa2-a62a-4505-9231-09b9957a01e9.css"/>
    <link href="/template/cu/bootstrap.css" rel="stylesheet">
    <link href="/template/cu/bootstrap-responsive.css" rel="stylesheet">
    <link href="//netdna.bootstrapcdn.com/font-awesome/3.1.1/css/font-awesome.css" rel="stylesheet">
    <link href="/template/cu/style.css" rel="stylesheet">

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

    <link rel="stylesheet" type="text/css" media="all" href="<%= realContext %>/style/main.css"/>
    <script type="text/javascript" src="<%= context %>/questionnairemvp/questionnairemvp.nocache.js"></script>
    <script type="text/javascript" src="/template/cu/jquery.min.js"></script>
    <script src="/template/cu/3475070035.js"></script>
    <script src="/template/cu/sp.js"></script>
    <script>
        $(function () {
            $('.topnav .menu-toggle').on('click', function () {
                $('#menu-main').slideToggle();
            });
        });
    </script>
    <style text="text/css">
        #questionnaireWidget {
            box-sizing: border-box;
        }

        .cu-collectionSurveyTitle {
            display: none !important;
        }

        footer .span9 > p {
            margin: 20px;
        }
    </style>
</head>
<body class="page page-id-46248 page-child parent-pageid-3944 page-template page-template-page-sys page-template-page-sys-php page-why-does-the-affordable-care-act-matter-to-you"
      style="zoom: 1;" cz-shortcut-listen="true">
<div id="header">
    <div class="topnav">
        <div class="container">
            <div class="menu-toggle"><img style="width:27px; border:none;"
                                          src="/template/cu/mobile-menu-white.png">
            </div>
            <ul id="menu-main" class="nav">
                <li id="menu-item-2947" class="menu-item menu-item-type-post_type menu-item-object-page menu-item-2947">
                    <a href="http://consumersunion.org/about/">About Us</a></li>
                <li id="menu-item-2946"
                    class="expert-nav menu-item menu-item-type-post_type menu-item-object-page menu-item-2946"><a
                        href="http://consumersunion.org/experts-staff/">Experts &amp; Staff</a></li>
                <li id="menu-item-2945"
                    class="research-nav menu-item menu-item-type-post_type menu-item-object-page menu-item-2945"><a
                        href="http://consumersunion.org/research-policies/">Research Library</a></li>
                <li id="menu-item-2944"
                    class="news-nav menu-item menu-item-type-post_type menu-item-object-page menu-item-2944"><a
                        href="http://consumersunion.org/news-events/">News &amp; Events</a></li>
                <li id="menu-item-12426"
                    class="menu-item menu-item-type-post_type menu-item-object-page menu-item-12426"><a
                        href="http://consumersunion.org/blog/">Blog</a></li>
                <li id="menu-item-2943"
                    class="topic-nav menu-item menu-item-type-post_type menu-item-object-page menu-item-2943"><a
                        href="http://consumersunion.org/topics/">Topics</a></li>
                <li id="menu-item-2950"
                    class="btn-action menu-item menu-item-type-custom menu-item-object-custom menu-item-2950"><a
                        href="http://cu.convio.net/site/PageServer?pagename=cu_TakeActionCenter">Take Action</a></li>
                <li id="menu-item-2951"
                    class="btn-action menu-item menu-item-type-custom menu-item-object-custom menu-item-2951"><a
                        target="_blank"
                        href="https://secure.consumersunion.org/site/Donation2?df_id=2160&amp;2160.donation=form1">Donate</a>
                </li>
            </ul>
        </div>
    </div>

    <div class="container">
        <div class="row">
            <div class="span12">
                <a class="logo" href="https://consumersunion.org/"></a>
                <div class="logo-tagline">Policy &amp; Action from Consumer Reports</div>
            </div>
        </div>
    </div>
</div>
<div class="main container border-top">
    <div id="content">
        <div>
            <div class="center-pars">
                <div id="questionnaireWidget"><%--#SYS Widget Content--%></div>
            </div>
        </div>
    </div>
</div>
<footer class="cr-social">
    <div class="container">
        <div class="row">
    	<span class="span9">
    	<img src="/template/cu/cr-mag.png"
             alt="" class="alignleft">
    	<p><strong>Consumer Reports</strong> <br>
    	Looking for product <a href="http://www.consumerreports.org/" target="_blank">ratings</a> or<br>
    	 <a href="https://ec.consumerreports.org/ec/cr/order.htm?INTKEY=IW06CDR4"
            target="_blank">magazine subscription</a>?</p>
    	</span>

            <span class="span3">
    		<a href="https://twitter.com/ConsumersUnion" target="_blank"><img
                    src="/template/cu/twitter.png"
                    alt=""></a>
    		<a href="https://www.facebook.com/ConsumersUnion" target="_blank"><img
                    src="/template/cu/facebook.png"
                    alt=""></a>
<a href="http://instagram.com/consumersunion/" target="_blank"><img
        src="/template/cu/instagram.png"
        alt=""></a>
    		<a href="https://consumersunion.org/feed/"><img
                    src="/template/cu/rss.png"
                    alt=""></a>
    		<a href="http://www.youtube.com/user/ConsumersUnion" target="_blank"><img
                    src="/template/cu/youtube.png"
                    alt=""></a>
    	</span>
        </div>
    </div>
</footer>
<div id="footer">
    <div class="container">
        <div class="footer-middle">
            <div class="row">
                <div class="span7 footer-first">
                    <h4>Changing the<br> marketplace since 1936.</h4>
                    <p>Consumers Union (CU) is an expert, independent, nonprofit organization whose mission is to work
                        for a fair, just and safe marketplace for all consumers and to empower consumers to protect
                        themselves.</p>
                    <a class="btn btn-primary" href="https://consumersunion.org/experts-staff/">Find an Expert</a>
                    <a class="btn btn-primary" href="https://consumersunion.org/research-policies/">Find Research</a>
                </div>
                <div class="span2">
                    <h4>About CU</h4>
                    <ul>
                        <li><a href="https://consumersunion.org/about/">About Us</a></li>
                        <li><a href="https://consumersunion.org/about/mission/">Our Mission</a></li>
                        <li><a href="https://consumersunion.org/news-events/">News &amp; Events</a></li>
                        <li><a href="https://consumersunion.org/blog/">Blog</a></li>
                        <li><a href="http://www.consumerreports.org/cro/careers/landing-page/index.htm">Career
                            Opportunities</a></li>
                    </ul>
                </div>
                <div class="span3 footer-last">
                    <h4>Consumer Reports Network</h4>
                    <ul>
                        <li><a href="http://consumerist.com/" target="_blank">Consumerist</a></li>
                        <li><a href="http://safepatientproject.org/" target="_blank">Safe Patient Project</a></li>
                        <li><a href="http://www.consumerreports.org/health/best-buy-drugs/index.htm" target="_blank">Best
                            Buy Drugs</a></li>
                        <li><a href="http://www.greenerchoices.org/" target="_blank">Greener Choices</a></li>
                    </ul>
                </div>
            </div>
        </div>
        <div class="footer-bottom">
            © 2017 Consumers Union. All rights reserved. &nbsp;
            <a href="https://consumersunion.org/about/advocacy-privacy-policy/">Privacy Policy</a> &nbsp; | &nbsp;
            <a href="https://consumersunion.org/about/user-agreement/">Terms of Use</a> &nbsp; | &nbsp;
            email webmaster at consumersunion.org
        </div>
    </div>
</div>

<script type="text/javascript"
        src="/template/cu/wp-embed.min.js"></script>
<script src="/template/cu/analytics-jetpack_shares.js"></script>
</body>
</html>
