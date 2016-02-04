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
    <meta name="keywords" content=" &raquo; Share Your Story , Our Green Energy Future , "/>
    <meta name="description"
          content="Too hot? Too cold? Damp? Drafty? Your house may need an energy overhaul. Is your home cold, drafty or uncomfortable? Think your energy bills are ...">
    <meta charset="UTF-8"/>

    <link rel="alternate" type="application/rss+xml" title="Our Green Energy Future (RSS)"
          href="http://ourgreenenergyfuture.org/feed"/>
    <link rel="alternate" type="application/atom+xml" title="Our Green Energy Future (Atom)"
          href="http://ourgreenenergyfuture.org/feed/atom"/>

    <!--[if lt IE 9]>
    <script src="https://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->

    <%-- SYS --%>
    <script type="text/javascript" src="<%= context %>/questionnairemvp/questionnairemvp.nocache.js"></script>
    <script type="text/javascript">
        var CKEDITOR_BASEPATH = '${pageContext.request.contextPath}/questionnaire/ckeditor/';
    </script>
    <link rel="stylesheet" type="text/css" media="all" href="<%= context %>/style/main.css"/>
    <link rel="stylesheet" type="text/css" media="all" href="<%= context %>/template/ogef/pfc.css"/>
    <link rel="stylesheet" type="text/css" media="all" href="<%= context %>/style/vertical-check.css"/>
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

    <link rel="stylesheet" type="text/css" media="all" href="<%= context %>/template/ogef/style.css"/>

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

</head>

<body class="page page-id-403 page-template page-template-share_your_story-php"
      data-context-path="${pageContext.request.contextPath}">
<div style="display:none"><!-- hidden, but picked up by Facebook -->
    <img alt="Share this site!" src="<%= context %>/template/ogef/energy-sharing-widget.png" width="175" height="175"/>
    Consumers Union campaign "A Greener Future" is working for clean, renewable, affordable energy.
</div>

<div id="main">
    <div class="cu-logo"><a href="http://www.consumersunion.org"></a></div>
    <header>
        <div id="header" class="container">
            <div id="header-content">
                <div id="header-image">
                    <h1>Our Green Energy Future</h1>

                    <p></p>
                    <a href="http://ourgreenenergyfuture.org/" title=""></a>
                </div>
                <!--#header-image-->
                <div id="sidebar-header">
                    <div class="widget-area">
                        <div class="textwidget">
                            <div id="social-networking">
                                <ul>
                                    <li class="twitter"><a href="http://twitter.com/consumersunion"
                                                           title="Follow us on Twitter"></a></li>
                                    <li class="facebook"><a
                                            href="http://www.facebook.com/pages/Consumers-Union/17274984050"
                                            title="Find us on Facebook"></a></li>
                                    <li class="rss"><a href="/feed/" title="Subscribe to this blog's RSS feed"></a></li>
                                </ul>
                            </div>
                        </div>
                    </div>
                    <div class="widget-area">
                        <form method="get" class="searchform" action="http://ourgreenenergyfuture.org">
                            <div>
                                <input type="text" value="" name="s" id="s"/>
                                <input type="submit" id="searchsubmit" value="Search"/>
                            </div>
                        </form>
                    </div>
                </div>
                <!--#sidebar-header-->
                <div class="clear"></div>
                <nav class="primary">
                    <div class="menu-header-menu-container">
                        <ul id="menu-header-menu" class="menu">
                            <li id="menu-item-423"
                                class="menu-item menu-item-type-custom menu-item-object-custom menu-item-423"><a
                                    href="http://cu.convio.net/energy">Act Now</a></li>
                            <li id="menu-item-444"
                                class="menu-item menu-item-type-post_type menu-item-object-page menu-item-444"><a
                                    href="http://ourgreenenergyfuture.org/blog">Blog</a></li>
                            <li id="menu-item-442"
                                class="menu-item menu-item-type-post_type menu-item-object-page menu-item-442"><a
                                    href="http://ourgreenenergyfuture.org/topics">Topics</a>
                                <ul class="sub-menu">
                                    <li id="menu-item-425"
                                        class="menu-item menu-item-type-taxonomy menu-item-object-category menu-item-425">
                                        <a href="http://ourgreenenergyfuture.org/topics/renewables">Renewables</a></li>
                                    <li id="menu-item-426"
                                        class="menu-item menu-item-type-taxonomy menu-item-object-category menu-item-426">
                                        <a href="http://ourgreenenergyfuture.org/topics/energy_efficiency">Energy
                                            Efficiency</a>
                                        <ul class="sub-menu">
                                            <li id="menu-item-435"
                                                class="menu-item menu-item-type-taxonomy menu-item-object-category menu-item-435">
                                                <a href="http://ourgreenenergyfuture.org/topics/energy_efficiency/lighting">Lighting</a>
                                            </li>
                                        </ul>
                                    </li>
                                    <li id="menu-item-428"
                                        class="menu-item menu-item-type-taxonomy menu-item-object-category menu-item-428">
                                        <a href="http://ourgreenenergyfuture.org/topics/fuel_economy">Fuel Economy</a>
                                    </li>
                                </ul>
                            </li>
                            <li id="menu-item-443"
                                class="menu-item menu-item-type-post_type menu-item-object-page menu-item-443"><a
                                    href="http://ourgreenenergyfuture.org/newsroom">Newsroom</a></li>
                            <li id="menu-item-427"
                                class="menu-item menu-item-type-taxonomy menu-item-object-category menu-item-427"><a
                                    href="http://ourgreenenergyfuture.org/topics/energy_codes">Energy Codes</a>
                                <ul class="sub-menu">
                                    <li id="menu-item-429"
                                        class="menu-item menu-item-type-custom menu-item-object-custom menu-item-429"><a
                                            href="http://ourgreenenergyfuture.org/posts/geography/maine">Maine</a></li>
                                    <li id="menu-item-430"
                                        class="menu-item menu-item-type-custom menu-item-object-custom menu-item-430"><a
                                            href="http://ourgreenenergyfuture.org/posts/geography/minnesota">Minnesota</a>
                                    </li>
                                    <li id="menu-item-434"
                                        class="menu-item menu-item-type-custom menu-item-object-custom menu-item-434"><a
                                            href="http://ourgreenenergyfuture.org/posts/geography/new_hampshire">New
                                        Hampshire</a></li>
                                    <li id="menu-item-431"
                                        class="menu-item menu-item-type-custom menu-item-object-custom menu-item-431"><a
                                            href="http://ourgreenenergyfuture.org/posts/geography/ohio">Ohio</a></li>
                                </ul>
                            </li>
                            <li id="menu-item-447"
                                class="menu-item menu-item-type-post_type menu-item-object-page current-menu-item page_item page-item-403 current_page_item menu-item-447">
                                <a href="http://ourgreenenergyfuture.org/share-your-story">Share Your Story</a></li>
                            <li id="menu-item-446"
                                class="menu-item menu-item-type-post_type menu-item-object-page menu-item-446"><a
                                    href="http://ourgreenenergyfuture.org/video">Video</a></li>
                            <li id="menu-item-445"
                                class="menu-item menu-item-type-post_type menu-item-object-page menu-item-445"><a
                                    href="http://ourgreenenergyfuture.org/about">About Us</a></li>
                        </ul>
                    </div>
                </nav>
                <!--.primary-->
            </div>
            <!--#header-content-->


        </div>
        <!--#header-->
    </header>

    <div class="clear"></div>

    <div id="body" class="container">

        <div id="page-content">
            <div id="questionnaireWidget"><%--#SYS Widget Content--%></div>
        </div>
        <!--#content-->

    </div>
    <!--#body, .container-->

    <div class="clear"></div>
    <div id="poweredBy-content" class="poweredBy-style">
        <a href="https://stori.es/"><img alt="Powered by stori.es"
                                         src="<%= realContext %>/images/powered-by-stories.png"/></a>
    </div>
    <footer>
        <div id="footer" class="container">
            <nav class="footer-menu">
                <ul id="menu-footer-menu" class="menu">
                    <li id="menu-item-377"
                        class="menu-item menu-item-type-custom menu-item-object-custom menu-item-377"><a
                            href="http://cu.convio.net/energy">Act Now</a></li>
                    <li id="menu-item-438"
                        class="menu-item menu-item-type-post_type menu-item-object-page menu-item-438"><a
                            href="http://ourgreenenergyfuture.org/blog">Blog</a></li>
                    <li id="menu-item-439"
                        class="menu-item menu-item-type-post_type menu-item-object-page menu-item-439"><a
                            href="http://ourgreenenergyfuture.org/topics">Topics</a></li>
                    <li id="menu-item-440"
                        class="menu-item menu-item-type-post_type menu-item-object-page menu-item-440"><a
                            href="http://ourgreenenergyfuture.org/newsroom">Newsroom</a></li>
                    <li id="menu-item-424"
                        class="menu-item menu-item-type-taxonomy menu-item-object-category menu-item-424"><a
                            href="http://ourgreenenergyfuture.org/topics/energy_codes">Energy Codes</a></li>
                    <li id="menu-item-449"
                        class="menu-item menu-item-type-post_type menu-item-object-page current-menu-item page_item page-item-403 current_page_item menu-item-449">
                        <a href="http://ourgreenenergyfuture.org/share-your-story">Share Your Story</a></li>
                    <li id="menu-item-448"
                        class="menu-item menu-item-type-post_type menu-item-object-page menu-item-448"><a
                            href="http://ourgreenenergyfuture.org/video">Video</a></li>
                    <li id="menu-item-441"
                        class="menu-item menu-item-type-post_type menu-item-object-page menu-item-441"><a
                            href="http://ourgreenenergyfuture.org/about">About Us</a></li>
                </ul>
            </nav>

            <div id="sidebar-footer">
                <div class="widget-area"><h4>Topics</h4>
                    <ul id="almost_all_categories_widget">
                        <li class="cat-item cat-item-3"><a href="http://ourgreenenergyfuture.org/topics/energy_codes"
                                                           title="Energy codes are minimum energy efficiency requirements for construction. Homes constructed to meet the model energy code have lower utility bills, are more comfortable and have tremendous environmental benefits.">Energy
                            Codes</a>
                        </li>
                        <li class="cat-item cat-item-4"><a
                                href="http://ourgreenenergyfuture.org/topics/energy_efficiency"
                                title="Whether it be your refrigerator, your light bulbs or your car, the more energy efficient your products are, the less energy you use and the more money you save. Help the environment and your pocketbook at the same time! ">Energy
                            Efficiency</a>
                            <ul class='children'>
                                <li class="cat-item cat-item-6"><a
                                        href="http://ourgreenenergyfuture.org/topics/energy_efficiency/lighting"
                                        title="View all posts filed under Lighting">Lighting</a>
                                </li>
                            </ul>
                        </li>
                        <li class="cat-item cat-item-5"><a href="http://ourgreenenergyfuture.org/topics/fuel_economy"
                                                           title="Better fuel economy standards will save consumers money and increase vehicle choice. ">Fuel
                            Economy</a>
                        </li>
                        <li class="cat-item cat-item-7"><a href="http://ourgreenenergyfuture.org/topics/renewables"
                                                           title="Renewable energy, such as wind, solar and hydro, reduces our dependence on fossil fuels. The more energy we get through renewables, the less pollution we emit, leading to healthier people and a healthier planet. ">Renewables</a>
                        </li>
                    </ul>
                </div>
                <div class="widget-area"><h4>Recent Posts</h4>
                    <ul>
                        <li>
                            <a href="http://ourgreenenergyfuture.org/posts/520-public-health-versus-industry-profits-sometimes-it-really-is-that-simple"
                               title="Public Health Versus Industry Profits: Sometimes It Really Is that Simple">Public
                                Health Versus Industry Profits: Sometimes It Really Is that Simple</a></li>
                        <li>
                            <a href="http://ourgreenenergyfuture.org/posts/510-fuel-economy-most-important-feature-for-car-purchasers"
                               title="Fuel Economy Most Important Feature for Car Purchasers">Fuel Economy Most
                                Important Feature for Car Purchasers</a></li>
                        <li><a href="http://ourgreenenergyfuture.org/posts/473-energy-codes-update"
                               title="Energy Codes Update">Energy Codes Update</a></li>
                    </ul>
                </div>
                <div class="widget-area"><h4>Contact us</h4>

                    <div class="textwidget">Shannon Baker-Branstetter <br/>
                        Policy Counsel<br/>
                        1101 17th Street, NW Suite 500<br/>
                        Washington DC 20036<br/>
                        <a href="mailto:sbaker-branstetter@consumer.org">sbaker-branstetter@consumer.org</a></div>
                </div>
            </div>

            <div class="clear"></div>

            <p class="center">
                &copy; 2012 <a href="http://www.consumersunion.org/">Consumers Union</a>.
                Our <a href="http://www.consumersunion.org/about/privacy.htm">Privacy Policy</a>.
            </p>
        </div>
        <!--#footer-->
    </footer>

</div>
<!--#main-->

</body>
</html>
