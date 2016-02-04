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
    <meta name="keywords"
          content=" &raquo; Share Your Biggest Food Safety Concern , Not In My Food , Know what you’re eating"/>
    <meta name="description"
          content="Share Your Biggest Food Safety Concern Worried about what’s in your food? It’s no wonder – processed foods are loaded with high fructose corn ...">
    <meta charset="UTF-8"/>

    <!--[if lt IE 9]>
    <script src="https://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->

    <link rel="profile" href="http://gmpg.org/xfn/11"/>
    <link rel="pingback" href="http://notinmyfood.org/xmlrpc.php"/>
    <link rel="alternate" type="application/rss+xml" title="Not In My Food (RSS)" href="http://notinmyfood.org/feed"/>
    <link rel="alternate" type="application/atom+xml" title="Not In My Food (Atom)"
          href="http://notinmyfood.org/feed/atom"/>


    <%-- SYS --%>
    <script type="text/javascript" src="<%= context %>/questionnairemvp/questionnairemvp.nocache.js"></script>
    <script type="text/javascript">
        var CKEDITOR_BASEPATH = '${pageContext.request.contextPath}/questionnaire/ckeditor/';
    </script>
    <link rel="stylesheet" type="text/css" media="all" href="<%= context %>/style/main.css"/>
    <link rel="stylesheet" type="text/css" media="all" href="<%= context %>/template/nimf/pfc.css"/>
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

    <link rel="stylesheet" type="text/css" media="all" href="<%= context %>/template/nimf/style.css"/>

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

<body class="page page-id-3084 page-template page-template-share_your_storyC-php"
      data-context-path="${pageContext.request.contextPath}">
<div style="display:none"><!-- hidden, but picked up by Facebook -->
    <img alt="Share this site!" src="<%= context %>/template/nimf/notinmyfood.org-sharing-widget.png" width="175"
         height="175"/>
    Not in My Food.org : Know what you're eating
</div>

<div id="main">
    <div class="cu-logo"><a href="http://www.consumersunion.org"></a></div>
    <header>
        <div id="header" class="container">
            <div id="header-content">
                <div id="header-image">
                    <h1>Not In My Food</h1>

                    <p>Know what you’re eating</p>
                    <a href="http://notinmyfood.org/" title="Know what you’re eating"></a>
                </div>
                <!--#header-image-->
                <div id="sidebar-header">
                    <div class="widget-area">
                        <div class="textwidget">
                            <div id="social-networking">
                                <ul>
                                    <li class="twitter"><a href="http://twitter.com/notinmyfood"
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
                        <form method="get" class="searchform" action="http://notinmyfood.org">
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
                            <li id="menu-item-2995"
                                class="menu-item menu-item-type-custom menu-item-object-custom menu-item-2995"><a
                                    href="http://cu.convio.net/nimf">Act Now</a></li>
                            <li id="menu-item-3033"
                                class="menu-item menu-item-type-post_type menu-item-object-page menu-item-3033"><a
                                    href="http://notinmyfood.org/blog">Blog</a></li>
                            <li id="menu-item-3035"
                                class="menu-item menu-item-type-post_type menu-item-object-page menu-item-3035"><a
                                    href="http://notinmyfood.org/newsroom">Newsroom</a></li>
                            <li id="menu-item-3050"
                                class="menu-item menu-item-type-post_type menu-item-object-page menu-item-3050"><a
                                    href="http://notinmyfood.org/topics">Topics</a>
                                <ul class="sub-menu">
                                    <li id="menu-item-3053"
                                        class="menu-item menu-item-type-taxonomy menu-item-object-category menu-item-3053">
                                        <a href="http://notinmyfood.org/topics/food-safety/food-contaminants/arsenic">Arsenic</a>
                                    </li>
                                    <li id="menu-item-3054"
                                        class="menu-item menu-item-type-taxonomy menu-item-object-category menu-item-3054">
                                        <a href="http://notinmyfood.org/topics/food-safety/food-contaminants/bpa">Bisphenol
                                            A (BPA)</a></li>
                                    <li id="menu-item-3055"
                                        class="menu-item menu-item-type-taxonomy menu-item-object-category menu-item-3055">
                                        <a href="http://notinmyfood.org/topics/food-safety/meat-without-drugs">Meat
                                            without Drugs</a></li>
                                    <li id="menu-item-3056"
                                        class="menu-item menu-item-type-taxonomy menu-item-object-category menu-item-3056">
                                        <a href="http://notinmyfood.org/topics/food-safety/food-labeling/gmos">Label
                                            GMOs</a></li>
                                </ul>
                            </li>
                            <li id="menu-item-3087"
                                class="menu-item menu-item-type-post_type menu-item-object-page current-menu-item page_item page-item-3084 current_page_item menu-item-3087">
                                <a href="http://notinmyfood.org/share-your-story">Share Your Story</a></li>
                            <li id="menu-item-3062"
                                class="menu-item menu-item-type-post_type menu-item-object-page menu-item-3062"><a
                                    href="http://notinmyfood.org/video">Video</a></li>
                            <li id="menu-item-3064"
                                class="menu-item menu-item-type-post_type menu-item-object-page menu-item-3064"><a
                                    href="http://notinmyfood.org/about">About Us</a></li>
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
                    <li id="menu-item-2998"
                        class="menu-item menu-item-type-custom menu-item-object-custom menu-item-2998"><a
                            href="http://cu.convio.net/nimf">Act Now</a></li>
                    <li id="menu-item-3032"
                        class="menu-item menu-item-type-post_type menu-item-object-page menu-item-3032"><a
                            href="http://notinmyfood.org/blog">Blog</a></li>
                    <li id="menu-item-3034"
                        class="menu-item menu-item-type-post_type menu-item-object-page menu-item-3034"><a
                            href="http://notinmyfood.org/newsroom">Newsroom</a></li>
                    <li id="menu-item-3051"
                        class="menu-item menu-item-type-post_type menu-item-object-page menu-item-3051"><a
                            href="http://notinmyfood.org/topics">Topics</a></li>
                    <li id="menu-item-3045"
                        class="menu-item menu-item-type-post_type menu-item-object-page menu-item-3045"><a
                            href="http://notinmyfood.org/share-your-story">Share Your Story</a></li>
                    <li id="menu-item-3063"
                        class="menu-item menu-item-type-post_type menu-item-object-page menu-item-3063"><a
                            href="http://notinmyfood.org/video">Video</a></li>
                    <li id="menu-item-3080"
                        class="menu-item menu-item-type-post_type menu-item-object-page menu-item-3080"><a
                            href="http://notinmyfood.org/about">About Us</a></li>
                </ul>
            </nav>

            <div id="sidebar-footer">
                <div class="widget-area"><h4>Recent Posts</h4>
                    <ul>
                        <li>
                            <a href="http://notinmyfood.org/posts/3225-31-companies-tell-rep-slaughter-whats-in-the-beef"
                               title="31 companies tell Rep. Slaughter what’s in the beef">31 companies tell Rep.
                                Slaughter what’s in the beef</a></li>
                        <li>
                            <a href="http://notinmyfood.org/posts/3201-cu-urges-grocery-stores-to-sell-meat-without-drugs"
                               title="CU urges grocery stores to sell Meat Without Drugs">CU urges grocery stores to
                                sell Meat Without Drugs</a></li>
                        <li>
                            <a href="http://notinmyfood.org/posts/3199-fda-ordered-to-follow-its-own-orders-35-years-later"
                               title="FDA ordered to follow its own orders, 35 years later">FDA ordered to follow its
                                own orders, 35 years later</a></li>
                    </ul>
                </div>
                <div class="widget-area"><h4>Topics</h4>

                    <div class="menu-widget-menu-container">
                        <ul id="menu-widget-menu" class="menu">
                            <li id="menu-item-3057"
                                class="menu-item menu-item-type-taxonomy menu-item-object-category menu-item-3057"><a
                                    href="http://notinmyfood.org/topics/food-safety/food-contaminants/arsenic">Arsenic</a>
                            </li>
                            <li id="menu-item-3058"
                                class="menu-item menu-item-type-taxonomy menu-item-object-category menu-item-3058"><a
                                    href="http://notinmyfood.org/topics/food-safety/food-contaminants/bpa">Bisphenol A
                                (BPA)</a></li>
                            <li id="menu-item-3059"
                                class="menu-item menu-item-type-taxonomy menu-item-object-category menu-item-3059"><a
                                    href="http://notinmyfood.org/topics/food-safety/meat-without-drugs">Meat without
                                Drugs</a></li>
                            <li id="menu-item-3060"
                                class="menu-item menu-item-type-taxonomy menu-item-object-category menu-item-3060"><a
                                    href="http://notinmyfood.org/topics/food-safety/food-labeling/gmos">Label GMOs</a>
                            </li>
                        </ul>
                    </div>
                </div>
                <div class="widget-area"><h4>Contact Us</h4>

                    <div class="textwidget">Not in My Food<br/>
                        A Project of Consumers Union<br/>
                        1101 17th Street NW, Suite 500<br/>
                        Washington, DC 20036<br/>
                        Phone: (202) 462-6262<br/>
                        Fax: (202) 265-9548<br/>
                        Email: <a href="mailto:food@consumersunion.org">food@consumersunion.org</a></div>
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
