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
    <meta name="keywords" content=" &raquo; Share Your Story , Hear Us Now , Consumer Voice for Communications Choice"/>
    <meta name="description"
          content="Experienced internet tracking for yourself? Have you experienced unwanted internet tracking, or tried unsuccessfully to get rid of tracking code? Do you ...">
    <meta charset="UTF-8"/>

    <link rel="profile" href="http://gmpg.org/xfn/11"/>
    <link rel="pingback" href="http://hearusnow.org/xmlrpc.php"/>
    <link rel="alternate" type="application/rss+xml" title="Hear Us Now (RSS)" href="http://hearusnow.org/feed"/>
    <link rel="alternate" type="application/atom+xml" title="Hear Us Now (Atom)" href="http://hearusnow.org/feed/atom"/>

    <!--[if lt IE 9]>
    <script src="https://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->

    <%-- SYS --%>
    <script type="text/javascript" src="<%= context %>/questionnaire/questionnaire.nocache.js"></script>
    <link rel="stylesheet" type="text/css" media="all" href="<%= context %>/style/main.css"/>
    <link rel="stylesheet" type="text/css" media="all" href="<%= context %>/template/hun/pfc.css"/>
    <link rel="stylesheet" type="text/css" media="all" href="<%= context %>/style/vertical-check.css"/>
    <link rel="stylesheet" type="text/css" media="all" href="<%= context %>/template/hun/style.css"/>

    <%-- SYS global variables --%>
    <script type="text/javascript">
        //Dictionary used by Survey Entry Point
        var requestParameters = {
            /* this would be the style for a dynamic page
             collectionId: "<%= (String)request.getAttribute("collectionId") %>",
             title: "<%= (String)request.getAttribute("title") %>",
             permalink: "<%= (String)request.getAttribute("permalink") %>",
             hasStyle: "<%= (String)request.getAttribute("hasStyle") %>" */
            collectionId: "48668",
            questionnaireTitle: 'Internet Tracking',
            permalink: "internet-tracking",
            hasStyle: "true"
        };
    </script>

    <%-- SYS loaded theme --%>
    <% String questionnaireStyle = (String) request.getAttribute("css");
        final boolean hasStyle = Boolean.valueOf((String) request.getAttribute("hasStyle")).booleanValue();
        if (hasStyle && !"".equals(questionnaireStyle)) { %>
    <style type="text/css">
        <%= questionnaireStyle  %>
    </style>
    <% } else { %>
    <link rel="stylesheet" type="text/css" media="all" href="<%= realContext %>/template/share/defaultStyle.css"/>
    <% } %>

    <jsp:include page="./WEB-INF/jsp/Analytics.jsp"/>

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

<body class="page page-id-18 page-template page-template-share_your_story-php"
      data-context-path="${pageContext.request.contextPath}">
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

<div style="display: none"><!-- hidden, but picked up by Facebook -->
    <img alt="Share this site!" src="<%= context %>/template/hun/sharing-widget.jpg" width="150" height="200"/>
    Consumers Union is an expert, independent, nonprofit organization whose mission is to work for a fair, just, and
    safe marketplace for all consumers and to empower consumers to protect themselves.
</div>

<div id="main">
    <div class="cu-logo"><a href="http://www.consumersunion.org"></a></div>
    <header>
        <div id="header" class="container">
            <div id="header-content">
                <div id="header-image">
                    <h1>Hear Us Now</h1>

                    <p>Consumer Voice for Communications Choice</p>
                    <a href="http://hearusnow.org/" title="Consumer Voice for Communications Choice"></a>
                </div>
                <!--#header-image-->
                <div id="sidebar-header">
                    <div class="widget-area">
                        <div class="textwidget">
                            <div id="social-networking">
                                <ul>
                                    <li class="twitter"><a href="http://twitter.com/CUtelecom"
                                                           title="Follow us on Twitter"></a></li>
                                    <li class="facebook"><a
                                            href="http://www.facebook.com/pages/Consumers-Union/17274984050"
                                            title="Find us on Facebook"></a></li>
                                    <li class="rss"><a href="http://hearusnow.org/feed/"
                                                       title="Subscribe to this blog's RSS feed"></a></li>
                                </ul>
                            </div>
                        </div>
                    </div>
                    <div class="widget-area">
                        <form method="get" class="searchform" action="http://hearusnow.org">
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
                            <li id="menu-item-15"
                                class="menu-item menu-item-type-custom menu-item-object-custom menu-item-15"><a
                                    href="http://cu.convio.net/hun">Act Now</a></li>
                            <li id="menu-item-23"
                                class="menu-item menu-item-type-post_type menu-item-object-page menu-item-23"><a
                                    href="http://hearusnow.org/blog">Blog</a></li>
                            <li id="menu-item-25"
                                class="menu-item menu-item-type-post_type menu-item-object-page menu-item-25"><a
                                    href="http://hearusnow.org/newsroom">Newsroom</a></li>
                            <li id="menu-item-24"
                                class="menu-item menu-item-type-post_type menu-item-object-page current-menu-item page_item page-item-18 current_page_item menu-item-24">
                                <a href="http://hearusnow.org/share-your-story">Share Your Story</a></li>
                            <li id="menu-item-22"
                                class="menu-item menu-item-type-post_type menu-item-object-page menu-item-22"><a
                                    href="http://hearusnow.org/topics">Topics</a>
                                <ul class="sub-menu">
                                    <li id="menu-item-38"
                                        class="menu-item menu-item-type-taxonomy menu-item-object-category menu-item-38">
                                        <a href="http://hearusnow.org/topics/cable_satellite">Cable &#038; Satellite</a>
                                    </li>
                                    <li id="menu-item-39"
                                        class="menu-item menu-item-type-taxonomy menu-item-object-category menu-item-39">
                                        <a href="http://hearusnow.org/topics/internet">Internet</a></li>
                                    <li id="menu-item-40"
                                        class="menu-item menu-item-type-taxonomy menu-item-object-category menu-item-40">
                                        <a href="http://hearusnow.org/topics/phone">Phone</a></li>
                                    <li id="menu-item-41"
                                        class="menu-item menu-item-type-taxonomy menu-item-object-category menu-item-41">
                                        <a href="http://hearusnow.org/topics/tv_radio">TV &#038; Radio</a></li>
                                </ul>
                            </li>
                            <li id="menu-item-1077"
                                class="menu-item menu-item-type-post_type menu-item-object-page menu-item-1077"><a
                                    href="http://hearusnow.org/video">Video</a></li>
                            <li id="menu-item-37"
                                class="menu-item menu-item-type-custom menu-item-object-custom menu-item-37"><a
                                    href="http://cu.convio.net/site/SPageServer?pagename=HUN_EOY_donate_telecom">Donate</a>
                            </li>
                            <li id="menu-item-16"
                                class="menu-item menu-item-type-post_type menu-item-object-page menu-item-16"><a
                                    href="http://hearusnow.org/about">About</a></li>
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

        <div id="slimmed-content">
            <div id="questionnaireWidget"><%--#SYS Widget Content--%></div>
        </div>
        <!--#content-->

        <div id="sidebar" class="wide-sidebar">
            <h3>Read stories you've shared with us</h3>

            <div id="wide-sidebar-content">
                <p>Here are the most recently published stories from consumers like you.</p>

                <div id="recent-stories">
                    <div class="post" id="post-804">
                        <div class="post-content">
                            <h5>
                                <a href="http://hearusnow.org/story/time_warner_did_not_return_my_calls_and_uses_defective_equipment"
                                   rel="bookmark"
                                   title="Permanent Link to Time Warner Did Not Return My Calls and Uses Defective Equipment">Time
                                    Warner Did Not Return My Calls and Uses Defective Equipment</a></h5>

                            <p>After experiencing many, many problems with Time Warner I encountered a rude supervisor.
                                There&#8217;s no way to contact that level supervisor but to leave a message&#8230;and
                                even then you have to go through three levels of employees. I left over seven messages
                                to contact me for three different supervisors. The employees&#8230;</p>
                        </div>
                        <!--.post-content-->
                        <div class="story-author">
                            Donni of Tarzana, <a href="http://hearusnow.org/posts/geography/california" rel="tag">California</a>
                        </div>
                        <!--.story-author-->
                    </div>
                    <!--.post-->
                    <div class="post" id="post-835">
                        <div class="post-content">
                            <h5><a href="http://hearusnow.org/story/expensive_scam_from_vtech" rel="bookmark"
                                   title="Permanent Link to Expensive Scam from VTech">Expensive Scam from VTech</a>
                            </h5>

                            <p>I went online to buy anti-virus protection for $9.99. The company charged my card $29.99.
                                It was VTech who tried to sell me all kinds of stuff I didn&#8217;t need. They were in
                                my computer with my permission but I think they put something in to track me because
                                they called&#8230;</p>
                        </div>
                        <!--.post-content-->
                        <div class="story-author">
                            Dianna of Defuniak Springs, <a href="http://hearusnow.org/posts/geography/florida"
                                                           rel="tag">Florida</a></div>
                        <!--.story-author-->
                    </div>
                    <!--.post-->
                    <div class="post" id="post-833">
                        <div class="post-content">
                            <h5>
                                <a href="http://hearusnow.org/story/staying_connected_is_too_expensive_on_a_fixed_income"
                                   rel="bookmark"
                                   title="Permanent Link to Staying Connected Is Too Expensive On A Fixed Income">Staying
                                    Connected Is Too Expensive On A Fixed Income</a></h5>

                            <p>I called ATT to see if I could get a better rate after my 2 year contract expired,
                                explaining to them that my cell phone does not work at my home, so I am forced to have
                                both landline and cell phone, but did not get a discount. Instead, they gave&#8230;</p>
                        </div>
                        <!--.post-content-->
                        <div class="story-author">
                            Cynthia of Frazier Park, <a href="http://hearusnow.org/posts/geography/california"
                                                        rel="tag">California</a></div>
                        <!--.story-author-->
                    </div>
                    <!--.post-->
                    <div class="post" id="post-832">
                        <div class="post-content">
                            <h5><a href="http://hearusnow.org/story/my_spam_is_out_of_control" rel="bookmark"
                                   title="Permanent Link to My Spam Is Out of Control">My Spam Is Out of Control</a>
                            </h5>

                            <p>I am still receiving spam mail in my inbox when I use both AT&amp;T net mail and also
                                outlook. I use the best security, Avasti, and still get junk mail. I was advised to get
                                a new email address. It helped, but my old email address is necessary for all
                                my&#8230;</p>
                        </div>
                        <!--.post-content-->
                        <div class="story-author">
                            Cynthia of Frazier Park, <a href="http://hearusnow.org/posts/geography/california"
                                                        rel="tag">California</a></div>
                        <!--.story-author-->
                    </div>
                    <!--.post-->
                    <div class="post" id="post-836">
                        <div class="post-content">
                            <h5><a href="http://hearusnow.org/story/charged_for_text_messages_i_never_made"
                                   rel="bookmark" title="Permanent Link to Charged For Text Messages I Never Made">Charged
                                For Text Messages I Never Made</a></h5>

                            <p>My AT&amp;T wireless bill a few months back had additional text messaging charges from
                                Bermuda. I called Customer Service and was told that &quot;I must have been in Bermuda&quot;
                                for those extra charges to take place. I replied I had not left the country in over 10
                                years and asked the&#8230;</p>
                        </div>
                        <!--.post-content-->
                        <div class="story-author">
                            C of Moberly, <a href="http://hearusnow.org/posts/geography/missouri" rel="tag">Missouri</a>
                        </div>
                        <!--.story-author-->
                    </div>
                    <!--.post-->
                    <div class="post" id="post-831">
                        <div class="post-content">
                            <h5><a href="http://hearusnow.org/story/qwest_is_overcharging_me" rel="bookmark"
                                   title="Permanent Link to Qwest Is Overcharging Me">Qwest Is Overcharging Me</a></h5>

                            <p>I have a promotional fee that has not been applied.&nbsp;Qwest is&nbsp;charging me full
                                retail when they promised an introductory amount for 2 years.</p>
                        </div>
                        <!--.post-content-->
                        <div class="story-author">
                            Bill of Peoria, <a href="http://hearusnow.org/posts/geography/arizona" rel="tag">Arizona</a>
                        </div>
                        <!--.story-author-->
                    </div>
                    <!--.post-->
                    <div class="post" id="post-834">
                        <div class="post-content">
                            <h5><a href="http://hearusnow.org/story/my_verizon_rates_continue_to_escalate"
                                   rel="bookmark" title="Permanent Link to My Verizon Rates Continue To Escalate">My
                                Verizon Rates Continue To Escalate</a></h5>

                            <p>My Verizon rates have continued to escalate despite my terminating more and more of my
                                services and bundling the rest. They randomly &quot;unbundle&quot; my services and
                                charge me full price, claiming that I unbundled them and then charge me huge service
                                fees to rebundle them. One time, they charged me $160.00&#8230;</p>
                        </div>
                        <!--.post-content-->
                        <div class="story-author">
                            Lin of Gloucester, <a href="http://hearusnow.org/posts/geography/massachusetts" rel="tag">Massachusetts</a>
                        </div>
                        <!--.story-author-->
                    </div>
                    <!--.post-->
                    <div class="post" id="post-795">
                        <div class="post-content">
                            <h5><a href="http://hearusnow.org/story/new_att_phone_has_slow_upload_speeds" rel="bookmark"
                                   title="Permanent Link to New AT&#038;T Phone has Slow Upload Speeds">New AT&#038;T
                                Phone has Slow Upload Speeds</a></h5>

                            <p>I am one of many people who got excited about the new Motorola Atrix 4G phone when AT&amp;T
                                advertised that they will be carrying it. The Atrix 4G is a powerful phone and has a lot
                                of great features. Unfortunately, since owning this phone, I have been experiencing very
                                slow data&#8230;</p>
                        </div>
                        <!--.post-content-->
                        <div class="story-author">
                            Zack of Allentown, <a href="http://hearusnow.org/posts/geography/pennsylvania" rel="tag">Pennsylvania</a>
                        </div>
                        <!--.story-author-->
                    </div>
                    <!--.post-->
                    <div class="post" id="post-797">
                        <div class="post-content">
                            <h5><a href="http://hearusnow.org/story/charged_43_for_going_one_minute_over_plan_limit"
                                   rel="bookmark"
                                   title="Permanent Link to Charged $43 for Going One Minute Over Plan Limit">Charged
                                $43 for Going One Minute Over Plan Limit</a></h5>

                            <p>I had a difficult time with Verizon customer service when I was charged $43 for being a
                                minute and six seconds over my monthly minutes. Verizon will not allow roll-over
                                minutes. As a senior, I don&#8217;t normally use my phone beyond 400 minutes on a 700
                                minute p/mo plan. My husband&#8230;</p>
                        </div>
                        <!--.post-content-->
                        <div class="story-author">
                            Erma of Gladstone, <a href="http://hearusnow.org/posts/geography/missouri" rel="tag">Missouri</a>
                        </div>
                        <!--.story-author-->
                    </div>
                    <!--.post-->
                    <div class="post" id="post-794">
                        <div class="post-content">
                            <h5><a href="http://hearusnow.org/story/sprint_wont_fully_refund_me_for_overcharges"
                                   rel="bookmark"
                                   title="Permanent Link to Sprint Won&#8217;t Fully Refund Me for Overcharges">Sprint
                                Won&#8217;t Fully Refund Me for Overcharges</a></h5>

                            <p>I have been a Sprint cell phone customer for many years. I recently changed from a &quot;month
                                to month&quot; plan for my service to a 2 year contract. I noticed that I was still
                                being charged $10/month for being on a &quot;month to month&quot; plan. When I
                                complained and asked for&#8230;</p>
                        </div>
                        <!--.post-content-->
                        <div class="story-author">
                            Henry of Cupertino, <a href="http://hearusnow.org/posts/geography/california" rel="tag">California</a>
                        </div>
                        <!--.story-author-->
                    </div>
                    <!--.post-->
                </div>
                <!--#recent-stories-->
                <div class="clear"></div>
                <p class="more"><a href="http://hearusnow.org/stories">Read more stories &raquo;</a></p>
            </div>
            <!--#wide-sidebar-content-->
        </div>
        <!--#sidebar-->


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
                    <li id="menu-item-262"
                        class="menu-item menu-item-type-custom menu-item-object-custom menu-item-home menu-item-262"><a
                            href="http://hearusnow.org/">Home</a></li>
                    <li id="menu-item-13" class="menu-item menu-item-type-custom menu-item-object-custom menu-item-13">
                        <a href="http://cu.convio.net/hun">Act Now</a></li>
                    <li id="menu-item-27" class="menu-item menu-item-type-post_type menu-item-object-page menu-item-27">
                        <a href="http://hearusnow.org/blog">Blog</a></li>
                    <li id="menu-item-29" class="menu-item menu-item-type-post_type menu-item-object-page menu-item-29">
                        <a href="http://hearusnow.org/newsroom">Newsroom</a></li>
                    <li id="menu-item-28"
                        class="menu-item menu-item-type-post_type menu-item-object-page current-menu-item page_item page-item-18 current_page_item menu-item-28">
                        <a href="http://hearusnow.org/share-your-story">Share Your Story</a></li>
                    <li id="menu-item-26" class="menu-item menu-item-type-post_type menu-item-object-page menu-item-26">
                        <a href="http://hearusnow.org/topics">Topics</a></li>
                    <li id="menu-item-1076"
                        class="menu-item menu-item-type-post_type menu-item-object-page menu-item-1076"><a
                            href="http://hearusnow.org/video">Video</a></li>
                    <li id="menu-item-36" class="menu-item menu-item-type-custom menu-item-object-custom menu-item-36">
                        <a href="http://cu.convio.net/site/SPageServer?pagename=HUN_EOY_donate_telecom">Donate</a></li>
                    <li id="menu-item-14" class="menu-item menu-item-type-post_type menu-item-object-page menu-item-14">
                        <a href="http://hearusnow.org/about">About</a></li>
                </ul>
            </nav>

            <div id="sidebar-footer">
                <div class="widget-area"><h4>Topics</h4>
                    <ul>
                        <li class="cat-item cat-item-9"><a href="http://hearusnow.org/topics/cable_satellite"
                                                           title="The cable company keeps raising rates and changing what&#039;s in your package of channels. And most consumers only have one choice for a cable provider. Make sure you have the latest developments.">Cable
                            &amp; Satellite</a>
                        </li>
                        <li class="cat-item cat-item-6"><a href="http://hearusnow.org/topics/internet"
                                                           title="You and your family rely on the internet for information and as a marketplace for the products and services you need. We&#039;re dedicated to an open internet that fosters innovation and free speech, and gives you direct access to entertainment, information and products.">Internet</a>
                        </li>
                        <li class="cat-item cat-item-8"><a href="http://hearusnow.org/topics/phone"
                                                           title="Ever gotten a cell phone bill that you just couldn&#039;t believe--data charges that you couldn&#039;t track to any service, overages on minutes or texts that cost you hundreds of dollars? We want to give you more control over your service and your bills.">Phone</a>
                        </li>
                        <li class="cat-item cat-item-7"><a href="http://hearusnow.org/topics/tv_radio"
                                                           title="Putting the control of scarce broadcast properties into the hands of a just a few large companies can limit and skew access to information.  We oppose media consolidation and support diverse broadcast ownership, greater competition, and new technologies that could bring TV and radio content directly to you from the web.">TV
                            &amp; Radio</a>
                        </li>
                        <li class="cat-item cat-item-1"><a href="http://hearusnow.org/topics/uncategorized"
                                                           title="View all posts filed under Uncategorized">Uncategorized</a>
                        </li>
                    </ul>
                </div>
                <div class="widget-area"><h4>Contact Us</h4>

                    <div class="textwidget">Hear Us Now<br/>
                        A Project of Consumers Union<br/>
                        1101 17th Street NW, Suite 500<br/>
                        Washington, DC 20036<br/>
                        Phone: (202) 462-6262<br/>
                        Fax: (202) 265-9548<br/>
                        Email: <a href="mailto:hun@consumersunion.org">hun@consumersunion.org</a></div>
                </div>
                <div class="widget-area"><h4>Our Work</h4>

                    <div class="textwidget">
                        <ul>
                            <li>
                                <a href="http://www.consumersunion.org/health.html">Health Care</a>
                            </li>
                            <li>
                                <a href="http://www.consumersunion.org/food.html">Food</a>
                            </li>
                            <li>
                                <a href="http://www.defendyourdollars.org/">Money</a>
                            </li>
                            <li>
                                <a href="http://www.consumersunion.org/products.html">Product Safety</a>
                            </li>
                            <li>
                                <a href="http://www.consumersunion.org/other.html">Other</a>
                            </li>
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
        <!--#footer-->
    </footer>

</div>
<!--#main-->
</body>
</html>
