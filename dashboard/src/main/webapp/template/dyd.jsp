<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<% request.setCharacterEncoding(
        "UTF-8"); // the charset stuff should set this on the Tomcat Connectors config, but for backup... except it seems ineffective %>
<% final String context = request.getContextPath().replace("^\\s*https://", "http://").trim(); %>
<% final String realContext = request.getContextPath().trim(); %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" dir="ltr" lang="en-US"/>
    <title>stori.es | <%=(String) request.getAttribute("title") %>
    </title>

    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>
    <jsp:include page="./../WEB-INF/jsp/Analytics.jsp"/>

    <!--[if lt IE 9]>
    <script src="https://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->

    <%-- SYS --%>
    <script type="text/javascript" src="<%= context %>/questionnairemvp/questionnairemvp.nocache.js"></script>
    <script type="text/javascript">
        var CKEDITOR_BASEPATH = '${pageContext.request.contextPath}/questionnaire/ckeditor/';
    </script>
    <link rel="stylesheet" type="text/css" media="all" href="<%= realContext %>/style/main.css"/>
    <link rel="stylesheet" type="text/css" media="all" href="<%= realContext %>/template/dyd/pfc.css"/>

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

    <link rel="stylesheet" type="text/css" media="all" href="<%=realContext %>/template/dyd/style.css"/>
    <link rel="stylesheet" type="text/css" media="all" href="<%= context %>/style/vertical-check.css"/>
    <link href="https://netdna.bootstrapcdn.com/font-awesome/3.1.1/css/font-awesome.css" rel="stylesheet">

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
<body class="home blog cat-98-id" data-context-path="${pageContext.request.contextPath}">
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
    <img alt="Share this site!" src="<%= realContext %>/template/dyd/sharing-widget.jpg" width="150" height="200"/>
    Consumers Union is an expert, independent, nonprofit organization whose mission is to work for a fair, just, and
    safe marketplace for all consumers and to empower consumers to protect themselves.
</div>

<div id="main">
    <div class="cu-logo"><a href="http://www.consumersunion.org"></a></div>
    <header>
        <div id="header" class="container">
            <div id="header-content">
                <div id="header-image">
                    <h1>Defend Your Dollars</h1>

                    <p>We support reforms to the financial marketplace to curb bad practices by banks and lenders.</p>
                    <a title="We support reforms to the financial marketplace to curb bad practices by banks and lenders."
                       href="http://defendyourdollars.org/"></a>
                </div>
                <%--#header-image--%>
                <div id="sidebar-header">
                    <div class="widget-area">
                        <div class="textwidget">
                            <div id="social-networking">
                                <ul>
                                    <li class="twitter"><a href="http://twitter.com/CU_Money"
                                                           title="Follow us on Twitter"></a></li>
                                    <li class="facebook"><a
                                            href="http://www.facebook.com/pages/Consumers-Union/17274984050"
                                            title="Find us on Facebook"></a></li>
                                    <li class="rss"><a href="http://http://defendyourdollars.org/feed/"
                                                       title="Subscribe to this blog's RSS feed"></a></li>
                                </ul>
                            </div>
                        </div>
                    </div>
                    <div class="widget-area">
                        <form method="get" class="searchform" action="http://defendyourdollars.org">
                            <div>
                                <input type="text" value="" name="s" id="s"/>
                                <input type="submit" id="searchsubmit" value="Search"/>
                            </div>
                        </form>
                    </div>
                </div>
                <%--#sidebar-header--%>
                <div class="clear"></div>
                <nav class="primary">
                    <div class="menu-header-menu-container">
                        <ul id="menu-header-menu" class="menu">
                            <li id="menu-item-1977"
                                class="menu-item menu-item-type-custom menu-item-object-custom current_page_item menu-item-home menu-item-1977">
                                <a href="http://defendyourdollars.org">Home</a></li>
                            <li id="menu-item-17"
                                class="menu-item menu-item-type-custom menu-item-object-custom menu-item-17"><a
                                    href="http://cu.convio.net/dyd">Act Now</a></li>
                            <li id="menu-item-1994"
                                class="menu-item menu-item-type-post_type menu-item-object-page current-menu-item menu-item-1994">
                                <a href="http://defendyourdollars.org/share-your-story">Share Your Story</a></li>
                            <li id="menu-item-1995"
                                class="menu-item menu-item-type-post_type menu-item-object-page menu-item-1995"><a
                                    href="http://defendyourdollars.org/topics">Topics</a>
                                <ul class="sub-menu">
                                    <li id="menu-item-24"
                                        class="menu-item menu-item-type-taxonomy menu-item-object-category menu-item-24">
                                        <a href="http://defendyourdollars.org/topics/banking">Banking</a></li>
                                    <li id="menu-item-28"
                                        class="menu-item menu-item-type-taxonomy menu-item-object-category menu-item-28">
                                        <a href="http://defendyourdollars.org/topics/cfpb">CFPB</a></li>
                                    <li id="menu-item-30"
                                        class="menu-item menu-item-type-taxonomy menu-item-object-category menu-item-30">
                                        <a href="http://defendyourdollars.org/topics/credit">Credit</a>
                                        <ul class="sub-menu">
                                            <li id="menu-item-31"
                                                class="menu-item menu-item-type-taxonomy menu-item-object-category menu-item-31">
                                                <a href="http://defendyourdollars.org/topics/credit/credit_cards">Credit
                                                    Cards</a></li>
                                            <li id="menu-item-32"
                                                class="menu-item menu-item-type-taxonomy menu-item-object-category menu-item-32">
                                                <a href="http://defendyourdollars.org/topics/credit/credit_reports">Credit
                                                    Reports</a></li>
                                            <li id="menu-item-34"
                                                class="menu-item menu-item-type-taxonomy menu-item-object-category menu-item-34">
                                                <a href="http://defendyourdollars.org/topics/credit/debt_collection">Debt
                                                    Collection</a></li>
                                            <li id="menu-item-35"
                                                class="menu-item menu-item-type-taxonomy menu-item-object-category menu-item-35">
                                                <a href="http://defendyourdollars.org/topics/credit/debt_relief">Debt
                                                    Relief</a></li>
                                            <li id="menu-item-38"
                                                class="menu-item menu-item-type-taxonomy menu-item-object-category menu-item-38">
                                                <a href="http://defendyourdollars.org/topics/credit/mortgages">Mortgages</a>
                                            </li>
                                            <li id="menu-item-1968"
                                                class="menu-item menu-item-type-taxonomy menu-item-object-category menu-item-1968">
                                                <a href="http://defendyourdollars.org/topics/credit/student_loans">Student
                                                    Loans</a></li>
                                        </ul>
                                    </li>
                                    <li id="menu-item-42"
                                        class="menu-item menu-item-type-taxonomy menu-item-object-category menu-item-42">
                                        <a href="http://defendyourdollars.org/topics/payments">Payments</a>
                                        <ul class="sub-menu">
                                            <li id="menu-item-43"
                                                class="menu-item menu-item-type-taxonomy menu-item-object-category menu-item-43">
                                                <a href="http://defendyourdollars.org/topics/payments/gift_cards">Gift
                                                    Cards</a></li>
                                            <li id="menu-item-44"
                                                class="menu-item menu-item-type-taxonomy menu-item-object-category menu-item-44">
                                                <a href="http://defendyourdollars.org/topics/payments/government_issued_cards">Gov&#8217;t
                                                    Issued Cards</a></li>
                                            <li id="menu-item-1969"
                                                class="menu-item menu-item-type-taxonomy menu-item-object-category menu-item-1969">
                                                <a href="http://defendyourdollars.org/topics/payments/mobile">Mobile</a>
                                            </li>
                                            <li id="menu-item-45"
                                                class="menu-item menu-item-type-taxonomy menu-item-object-category menu-item-45">
                                                <a href="http://defendyourdollars.org/topics/payments/payroll_cards">Payroll
                                                    Cards</a></li>
                                            <li id="menu-item-47"
                                                class="menu-item menu-item-type-taxonomy menu-item-object-category menu-item-47">
                                                <a href="http://defendyourdollars.org/topics/payments/prepaid_cards">Prepaid
                                                    Cards</a></li>
                                        </ul>
                                    </li>
                                    <li id="menu-item-48"
                                        class="menu-item menu-item-type-taxonomy menu-item-object-category menu-item-48">
                                        <a href="http://defendyourdollars.org/topics/privacy">Privacy</a>
                                        <ul class="sub-menu">
                                            <li id="menu-item-49"
                                                class="menu-item menu-item-type-taxonomy menu-item-object-category menu-item-49">
                                                <a href="http://defendyourdollars.org/topics/privacy/security_breaches">Security
                                                    Breaches</a></li>
                                            <li id="menu-item-50"
                                                class="menu-item menu-item-type-taxonomy menu-item-object-category menu-item-50">
                                                <a href="http://defendyourdollars.org/topics/privacy/security_freeze">Security
                                                    Freeze</a></li>
                                        </ul>
                                    </li>
                                </ul>
                            </li>
                            <li id="menu-item-1993"
                                class="menu-item menu-item-type-post_type menu-item-object-page menu-item-1993"><a
                                    href="http://defendyourdollars.org/blog">Blog</a></li>
                            <li id="menu-item-1996"
                                class="menu-item menu-item-type-post_type menu-item-object-page menu-item-1996"><a
                                    href="http://defendyourdollars.org/video">Video</a></li>
                            <li id="menu-item-1997"
                                class="menu-item menu-item-type-post_type menu-item-object-page menu-item-1997"><a
                                    href="http://defendyourdollars.org/about">About Us</a></li>
                            <li id="menu-item-53"
                                class="menu-item menu-item-type-custom menu-item-object-custom menu-item-53"><a
                                    href="http://cu.convio.net/site/PageServer?pagename=DYD_EOY_donate_finance">Donate</a>
                            </li>
                        </ul>
                    </div>
                </nav>
                <%--.primary--%>
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
            <nav class="footer-menu">
                <ul id="menu-footer-menu" class="menu">
                    <li id="menu-item-1979"
                        class="menu-item menu-item-type-custom menu-item-object-custom current-menu-item current_page_item menu-item-home menu-item-1979">
                        <a href="http://defendyourdollars.org">Home</a></li>
                    <li id="menu-item-10" class="menu-item menu-item-type-custom menu-item-object-custom menu-item-10">
                        <a href="http://cu.convio.net/dyd">Act Now</a></li>
                    <li id="menu-item-2000"
                        class="menu-item menu-item-type-post_type menu-item-object-page menu-item-2000"><a
                            href="http://defendyourdollars.org/share-your-story">Share Your Story</a></li>
                    <li id="menu-item-1999"
                        class="menu-item menu-item-type-post_type menu-item-object-page menu-item-1999"><a
                            href="http://defendyourdollars.org/topics">Topics</a></li>
                    <li id="menu-item-2001"
                        class="menu-item menu-item-type-post_type menu-item-object-page menu-item-2001"><a
                            href="http://defendyourdollars.org/blog">Blog</a></li>
                    <li id="menu-item-75" class="menu-item menu-item-type-custom menu-item-object-custom menu-item-75">
                        <a href="http://defendyourdollars.org/complain">Complain</a></li>
                    <li id="menu-item-1998"
                        class="menu-item menu-item-type-post_type menu-item-object-page menu-item-1998"><a
                            href="http://defendyourdollars.org/video">Video</a></li>
                    <li id="menu-item-2002"
                        class="menu-item menu-item-type-post_type menu-item-object-page menu-item-2002"><a
                            href="http://defendyourdollars.org/about">About Us</a></li>
                    <li id="menu-item-54" class="menu-item menu-item-type-custom menu-item-object-custom menu-item-54">
                        <a href="http://cu.convio.net/site/PageServer?pagename=DYD_EOY_donate_finance">Donate</a></li>
                </ul>
            </nav>
            <div id="sidebar-footer">
                <div class="widget-area">
                    <h4>Recent Posts</h4>
                    <ul>
                        <li>
                            <a href="http://defendyourdollars.org/posts/2322-victory-congress-votes-to-extend-stafford-student-loan"
                               title="Victory! Congress Votes To Extend Stafford Student Loan Rate">Victory! Congress
                                Votes To Extend Stafford Student Loan Rate</a></li>
                        <li><a href="http://defendyourdollars.org/posts/2317-whats-behind-the-magic-prepaid-card"
                               title="What’s Behind the Magic Prepaid Card">What’s Behind the Magic Prepaid Card</a>
                        </li>
                        <li>
                            <a href="http://defendyourdollars.org/posts/2308-cfpb-launches-public-database-of-credit-card-complaints"
                               title="CFPB Launches Public Database of Credit Card Complaints">CFPB Launches Public
                                Database of Credit Card Complaints</a></li>
                    </ul>
                </div>
                <div class="widget-area">
                    <h4>Contact Us</h4>

                    <div class="textwidget">Financial Services Team<br/>
                        Consumers Union<br/>
                        1535 Mission Street<br/>
                        San Francisco, CA 94103<br/>
                        <a href="mailto:money@consumersunion.org">money@consumersunion.org</a><br/>
                        <a href="mailto:media@consumersunion.org">media@consumersunion.org</a></div>
                </div>
                <div class="widget-area">
                    <h4>Our Work</h4>

                    <div class="textwidget">
                        <ul>
                            <li><a href="http://www.safepatientproject.org">Patient Safety</a></li>
                            <li><a href="http://www.prescriptionforchange.org">Health Care</a></li>
                            <li><a href="http://www.buysafeeatwell.org">Food &amp; Products</a></li>
                            <li><a href="http://www.consumersunion.org/energy/">Energy</a></li>
                            <li><a href="http://www.hearusnow.org/">Phones &amp; Media</a></li>
                        </ul>
                    </div>
                </div>
            </div>
            <div class="clear"></div>
            <p class="center">
                &copy; 2012 <a href="http://www.consumersunion.org/">Consumers Union</a>.
                Our <a href="http://www.consumersunion.org/about/privacy.htm">Privacy Policy</a>.
            </p>
        </div>
        <%--#footer--%>
    </footer>
</div>
</body>
</html>
