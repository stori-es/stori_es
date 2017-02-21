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

    <meta name="keywords"
          content=" &raquo; Share Your Story , Your Health Security , A new direction for health care&#8230;"/>
    <meta name="description" content=" &raquo; Share Your Story | A new direction for health care&#8230;"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" dir="ltr" lang="en-US"/>

    <link rel="profile" href="http://gmpg.org/xfn/11"/>
    <link rel="pingback" href="http://yourhealthsecurity.org/wordpress/xmlrpc.php"/>
    <link rel="alternate" type="application/rss+xml" title="Your Health Security (RSS)"
          href="http://yourhealthsecurity.org/feed"/>
    <link rel="alternate" type="application/atom+xml" title="Your Health Security (Atom)"
          href="http://yourhealthsecurity.org/feed/atom"/>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>

    <%-- SYS --%>
    <script type="text/javascript" src="<%= realContext %>/questionnairemvp/questionnairemvp.nocache.js"></script>
    <script type="text/javascript">
        var CKEDITOR_BASEPATH = '${pageContext.request.contextPath}/questionnaire/ckeditor/';
    </script>
    <link rel="stylesheet" type="text/css" media="all" href="<%= realContext %>/style/main.css"/>
    <link rel="stylesheet" type="text/css" media="all" href="<%= realContext %>/template/yhs/pfc.css"/>
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

    <!--[if lt IE 9]>
    <script src="https://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->

    <script type="text/javascript">
        var TB_pluginPath = 'http://yourhealthsecurity.org/wordpress/wp-content/plugins/tweet-blender';
        var TB_config = {
            'widget_show_photos': true,
            'widget_show_source': true,
            'widget_show_header': true,
            'general_link_screen_names': true,
            'general_link_hash_tags': true,
            'general_link_urls': true,
            'widget_check_sources': true,
            'widget_show_user': true
        };
    </script>

    <link rel="alternate" type="application/rss+xml" title="Your Health Security &raquo; Share Your Story Comments Feed"
          href="http://yourhealthsecurity.org/share_your_story/feed"/>

    <link rel="stylesheet" type="text/html" href="<%=realContext %>/template/yhs/skin.css" media="screen"/>
    <link rel='stylesheet' id='cfq-css' href='<%=realContext %>/template/yhs/style.css' type='text/css' media='all'/>
    <link rel='stylesheet' id='wp-pagenavi-css' href='<%=realContext %>/template/yhs/pagenavi-css.css' type='text/html'
          media='all'/>

    <link rel="EditURI" type="application/rsd+xml" title="RSD"
          href="http://yourhealthsecurity.org/wordpress/xmlrpc.php?rsd"/>
    <link rel="wlwmanifest" type="application/wlwmanifest+xml"
          href="http://yourhealthsecurity.org/wordpress/wp-includes/wlwmanifest.xml"/>
    <link rel='prev' title='The New Law' href='http://yourhealthsecurity.org/the_new_law'/>
    <link rel='next' title='Insurance' href='http://yourhealthsecurity.org/insurance'/>
    <meta name="generator" content="WordPress 3.3.1"/>

    <link rel="stylesheet" type="text/html" href="<%=realContext %>/template/yhs/recaptcha.css"/>
    <link type="text/html" media="screen" rel="stylesheet" href="<%=realContext %>/template/yhs/tweets.css"/>
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

    <jsp:include page="./../WEB-INF/jsp/Analytics.jsp"/>

</head>

<body class="page page-id-9 page-template page-template-share_your_story-php"
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

<div style="display:none"><!-- hidden, but picked up by Facebook -->
    <img alt="Prescription for Change" src="<%= realContext %>/template/yhs/yhs-sharing-widget.png" width="150"
         height="200"/>
    A new direction for healthcare...
</div>

<div id="main">
    <div class="cu-logo">
        <a href="http://www.consumersunion.org"></a>
    </div>
    <header>
        <div id="header" class="container">
            <div id="header-content">
                <div id="header-image">
                    <h1>Your Health Security</h1>

                    <p>A new direction for health care&#8230;</p>
                    <a href="http://yourhealthsecurity.org/" title="A new direction for health care&#8230;"></a>
                </div>
                <%--#header-image--%>
                <div id="sidebar-header">
                    <div class="widget-area">
                        <div class="textwidget">
                            <div id="social-networking">
                                <ul>
                                    <li class="twitter"><a href="http://twitter.com/Rx_4Change"
                                                           title="Follow us on Twitter"></a></li>
                                    <li class="facebook"><a
                                            href="http://www.facebook.com/pages/Consumers-Union/17274984050"
                                            title="Find us on Facebook"></a></li>
                                    <li class="rss"><a href="http://prescriptionforchange.org/feed/"
                                                       title="Subscribe to this blog's RSS feed"></a></li>
                                </ul>
                            </div>
                        </div>
                    </div>
                    <div class="widget-area">
                        <form method="get" class="searchform" action="http://yourhealthsecurity.org">
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
                            <li id="menu-item-3008"
                                class="menu-item menu-item-type-custom menu-item-object-custom menu-item-3008"><a
                                    title="Take action on healthcare reform!" href="http://cu.convio.net/yhs">Act
                                Now</a></li>
                            <li id="menu-item-3050"
                                class="menu-item menu-item-type-post_type menu-item-object-page menu-item-3050"><a
                                    title="Consumers Union press releases and news articles about health"
                                    href="http://yourhealthsecurity.org/newsroom">News</a></li>
                            <li id="menu-item-3057"
                                class="menu-item menu-item-type-post_type menu-item-object-page menu-item-3057"><a
                                    title="Our blog" href="http://yourhealthsecurity.org/blog">Blog</a></li>
                            <li id="menu-item-3054"
                                class="menu-item menu-item-type-post_type menu-item-object-page menu-item-3054"><a
                                    title="Discover what the new law means for you and your family."
                                    href="http://yourhealthsecurity.org/the_new_law">The New Law</a></li>
                            <li id="menu-item-3065"
                                class="menu-item menu-item-type-post_type menu-item-object-page menu-item-3065"><a
                                    href="http://yourhealthsecurity.org/topics">Topics</a>
                                <ul class="sub-menu">
                                    <li id="menu-item-3070"
                                        class="menu-item menu-item-type-taxonomy menu-item-object-category menu-item-3070">
                                        <a href="http://yourhealthsecurity.org/topics/better_consumer_information">Better
                                            Consumer Information</a></li>
                                    <li id="menu-item-3069"
                                        class="menu-item menu-item-type-taxonomy menu-item-object-category menu-item-3069">
                                        <a href="http://yourhealthsecurity.org/topics/better_insurance_value">Better
                                            Insurance Value</a></li>
                                    <li id="menu-item-3061"
                                        class="menu-item menu-item-type-taxonomy menu-item-object-category menu-item-3061">
                                        <a href="http://yourhealthsecurity.org/topics/rate_review">Rate Review</a></li>
                                    <li id="menu-item-3068"
                                        class="menu-item menu-item-type-custom menu-item-object-custom menu-item-3068">
                                        <a href="http://safepatientproject.org">Safety and Quality</a></li>
                                    <li id="menu-item-3064"
                                        class="menu-item menu-item-type-taxonomy menu-item-object-geography menu-item-3064">
                                        <a href="http://yourhealthsecurity.org/posts/geography/california">California
                                            Project</a></li>
                                </ul>
                            </li>
                            <li id="menu-item-21"
                                class="menu-item menu-item-type-post_type menu-item-object-page current-menu-item page_item page-item-9 current_page_item menu-item-21">
                                <a title="Have a story to tell?" href="http://yourhealthsecurity.org/share-your-story">Share
                                    Your Story</a></li>
                            <li id="menu-item-1435"
                                class="menu-item menu-item-type-post_type menu-item-object-page menu-item-1435"><a
                                    title="Find information specific to your area."
                                    href="http://yourhealthsecurity.org/your_state">Your State</a></li>
                            <li id="menu-item-1588"
                                class="menu-item menu-item-type-custom menu-item-object-custom menu-item-1588"><a
                                    title="Original research, reports and documents on health."
                                    href="http://yourhealthsecurity.org/tags/publications">Publications</a></li>
                            <li id="menu-item-1629"
                                class="menu-item menu-item-type-post_type menu-item-object-page menu-item-1629"><a
                                    href="http://yourhealthsecurity.org/video">Video</a></li>
                        </ul>
                    </div>
                </nav>
            </div>
            <%--#header-content--%>
        </div>
        <%--#header--%>
    </header>
    <div class="clear"></div>
    <div id="body" class="container">
        <div id="slimmed-content">
            <div id="questionnaireWidget"><%--#SYS Widget Content--%></div>
        </div>
        <%--#content--%>
        <div id="sidebar" class="wide-sidebar">
            <div class="sys-video"><h4>Your Video Stories</h4>

                <div class="youtube_channel">
                    <iframe class="youtube-player" type="text/html" width="395" height="321"
                            src="https://www.youtube.com/embed/oLogGZQyXbw?html5=1" frameborder="0">
                    </iframe>
                    <p><a href="https://www.youtube.com/user/ConsumersUnion/" title="More videos »">More videos »</a>
                    </p></div>
            </div>
            <h3>Stories about Healthcare</h3>

            <div id="wide-sidebar-content">
                <p>Here are the most recently published stories from consumers like you.</p>

                <div id="recent-stories"></div>
                <%-- Temporally stories --%>
                <div id="recent-stories2">
                    <div class="post" id="post-2615">
                        <div class="post-content">
                            <h5><a href="http://yourhealthsecurity.org/story/aetnas_cobra_rate_jumps_for_no_reason"
                                   rel="bookmark"
                                   title="Permanent Link to Aetna&#8217;s COBRA rate jumps for no reason">Aetna&#8217;s
                                COBRA rate jumps for no reason</a></h5>

                            <p>After being on COBRA for 6 months, the&nbsp;monthly premium that I paid to Aetna for my
                                health insurance went up $111 for no reason other than the insurance company&#8217;s
                                whim.</p>
                        </div>
                        <div class="story-author">
                            FS of <a href="http://yourhealthsecurity.org/posts/geography/california" rel="tag">California</a>
                        </div>
                    </div>
                    <div class="post" id="post-2608">
                        <div class="post-content">
                            <h5><a href="http://yourhealthsecurity.org/story/insurer_raises_premium_350_on_autistic_son"
                                   rel="bookmark" title="Permanent Link to Insurer raises premium 350% on autistic son">Insurer
                                raises premium 350% on autistic son</a></h5>

                            <p>I have a son with mild Autism and MR. One insurer I recently applied to required a 350%
                                up-rating of his rate. Other that his condition, he does not require any more meds or
                                get sick any more or less than any other child in his school. This is just
                                plain&#8230;</p>
                        </div>
                        <div class="story-author">
                            Greg from Menlo Park, California
                        </div>
                    </div>
                    <div class="post" id="post-2636">
                        <div class="post-content">
                            <h5>
                                <a href="http://yourhealthsecurity.org/story/family_stuck_with_high_premiums_after_unfair_denial"
                                   rel="bookmark"
                                   title="Permanent Link to Family stuck with high premiums after unfair denial">Family
                                    stuck with high premiums after unfair denial</a></h5>

                            <p>Our family is on COBRA, due to my husband&#8217;s job loss. Since it has gone on for a
                                number of months now we decided to check into buying individual plans for the two of us
                                and for our 20-year-old daughter. Our COBRA coverage is through Blue Shield, but the
                                premiums are&#8230;</p>
                        </div>
                        <div class="story-author">
                            Lisa of Lafayette, <a href="http://yourhealthsecurity.org/posts/geography/california"
                                                  rel="tag">California</a></div>
                    </div>
                    <div class="post" id="post-2607">
                        <div class="post-content">
                            <h5><a href="http://yourhealthsecurity.org/story/blue_cross_eating_up_23_of_pension"
                                   rel="bookmark" title="Permanent Link to Blue Cross eating up 2/3 of pension">Blue
                                Cross eating up 2/3 of pension</a></h5>

                            <p>I am on a fixed pension. Blue Cross has increased my premiums an average of over $100 a
                                year. It now consumes over 2/3 of my pension. I am lucky to have some savings to fall
                                back on but when the insurance companies have that too, I guess my only
                                recourse&#8230;</p>
                        </div>
                        <div class="story-author">
                            Richard of Salinas, <a href="http://yourhealthsecurity.org/posts/geography/california"
                                                   rel="tag">California</a></div>
                    </div>
                    <div class="post" id="post-2606">
                        <div class="post-content">
                            <h5>
                                <a href="http://yourhealthsecurity.org/story/fixed_long_term_disability_premium_goes_sky_high"
                                   rel="bookmark"
                                   title="Permanent Link to &#8220;Fixed&#8221; long term disability premium goes sky high">
                                    &#8220;Fixed&#8221; long term disability premium goes sky high</a></h5>

                            <p>I am in good health and not disabled. When I first signed up for &quot;Long Term
                                Disability&quot; insurance through CALPers 12 years ago, I selected a &quot;fixed rate&quot;
                                of $405 paid quarterly ($1620/year). Now it is $814 paid 4 times a year ($3109.25/year).
                                The way the insurance company hiked the rates&#8230;</p>
                        </div>
                        <div class="story-author">
                            Alayne
                        </div>
                    </div>
                </div>
                <%-- Temporally stories --%>
                <a href="http://yourhealthsecurity.org/stories/">Read more stories &raquo;</a>
            </div>
        </div>
        <%--#sidebar--%>
    </div>
    <%--#body, .container--%>
    <div class="clear"></div>
    <div id="poweredBy-content" class="poweredBy-style">
        <a href="https://stori.es/"><img alt="Powered by stori.es"
                                         src="<%= realContext %>/images/powered-by-stories.png"/></a>
    </div>
    <footer>
        <div id="footer" class="container">
            <nav class="footer-menu">
                <ul id="menu-footer-menu" class="menu">
                    <li id="menu-item-35" class="menu-item menu-item-type-custom menu-item-object-custom menu-item-35">
                        <a title="Take action on healthcare reform." href="http://cu.convio.net/yhs">Act Now</a></li>
                    <li id="menu-item-1429"
                        class="menu-item menu-item-type-post_type menu-item-object-page menu-item-1429"><a
                            title="Consumers Union press releases and news articles about health."
                            href="http://yourhealthsecurity.org/newsroom">News</a></li>
                    <li id="menu-item-1477"
                        class="menu-item menu-item-type-post_type menu-item-object-page menu-item-1477"><a
                            title="Our blog." href="http://yourhealthsecurity.org/blog">Blog</a></li>
                    <li id="menu-item-1469"
                        class="menu-item menu-item-type-post_type menu-item-object-page menu-item-1469"><a
                            title="Discover what the new law means for you and your family."
                            href="http://yourhealthsecurity.org/the_new_law">The New Law</a></li>
                    <li id="menu-item-3047"
                        class="menu-item menu-item-type-post_type menu-item-object-page current-menu-item page_item page-item-9 current_page_item menu-item-3047">
                        <a title="Have a story to tell?" href="http://yourhealthsecurity.org/share-your-story">Share
                            Your Story</a></li>
                    <li id="menu-item-1434"
                        class="menu-item menu-item-type-post_type menu-item-object-page menu-item-1434"><a
                            title="Find information specific to your area."
                            href="http://yourhealthsecurity.org/your_state">Your State</a></li>
                    <li id="menu-item-3017"
                        class="menu-item menu-item-type-custom menu-item-object-custom menu-item-3017"><a
                            href="http://yourhealthsecurity.org/tags/publications">Publications</a></li>
                    <li id="menu-item-3055"
                        class="menu-item menu-item-type-post_type menu-item-object-page menu-item-3055"><a
                            title="What we&#8217;re working on&#8230;" href="http://yourhealthsecurity.org/topics">Topics</a>
                    </li>
                    <li id="menu-item-1630"
                        class="menu-item menu-item-type-post_type menu-item-object-page menu-item-1630"><a
                            href="http://yourhealthsecurity.org/video">Video</a></li>
                    <li id="menu-item-32" class="menu-item menu-item-type-post_type menu-item-object-page menu-item-32">
                        <a title="This site is sponsored by Consumer Reports Health."
                           href="http://yourhealthsecurity.org/about">About Us</a></li>
                    <li id="menu-item-3015"
                        class="menu-item menu-item-type-custom menu-item-object-custom menu-item-3015"><a
                            title="Fighting for your rights!"
                            href="http://cu.convio.net/site/PageServer?pagename=yhs_EOY_donate_health">Donate</a></li>
                </ul>
            </nav>
            <div id="sidebar-footer">
                <div class="widget-area">
                    <h4>Recent Posts</h4>
                    <ul>
                        <li>
                            <a href="http://yourhealthsecurity.org/posts/3213-thanks-to-new-health-reform-law-insurers-are-improving-value-preparing-for-rebates-early-data-show"
                               title="Thanks to health reform law insurers are improving value, preparing for rebates">Thanks
                                to health reform law insurers are improving value, preparing for rebates</a></li>
                        <li>
                            <a href="http://yourhealthsecurity.org/posts/3186-healthcare-law-causing-rates-to-drop-in-texas"
                               title="Healthcare law causing rates to drop in Texas">Healthcare law causing rates to
                                drop in Texas</a></li>
                        <li>
                            <a href="http://yourhealthsecurity.org/posts/3173-100000-of-you-joined-the-rally-for-the-affordable-care-act"
                               title="Consumers rally for healthcare rights at the Supreme Court!">Consumers rally for
                                healthcare rights at the Supreme Court!</a></li>
                    </ul>
                </div>
                <div class="widget-area">
                    <h4>Our Work</h4>

                    <div class="menu-sidebar-menu-container">
                        <ul id="menu-sidebar-menu" class="menu">
                            <li id="menu-item-3078"
                                class="menu-item menu-item-type-taxonomy menu-item-object-category menu-item-3078"><a
                                    href="http://yourhealthsecurity.org/topics/better_consumer_information">Better
                                Consumer Information</a></li>
                            <li id="menu-item-3077"
                                class="menu-item menu-item-type-taxonomy menu-item-object-category menu-item-3077"><a
                                    href="http://yourhealthsecurity.org/topics/better_insurance_value">Better Insurance
                                Value</a></li>
                            <li id="menu-item-1635"
                                class="menu-item menu-item-type-taxonomy menu-item-object-category menu-item-1635"><a
                                    href="http://yourhealthsecurity.org/topics/rate_review">Rate Review</a></li>
                            <li id="menu-item-1636"
                                class="menu-item menu-item-type-custom menu-item-object-custom menu-item-1636"><a
                                    href="http://safepatientproject.org">Safety and Quality</a></li>
                            <li id="menu-item-3067"
                                class="menu-item menu-item-type-taxonomy menu-item-object-geography menu-item-3067"><a
                                    href="http://yourhealthsecurity.org/posts/geography/california">California
                                Project</a></li>
                        </ul>
                    </div>
                </div>
                <div class="widget-area">
                    <h4>Contact Us</h4>

                    <div class="textwidget">
                        Consumers Union<br/>
                        Your Health Security<br/>
                        A Project of Consumers Union<br/>
                        506 W. 14th St., Suite A<br/>
                        Austin, TX 78701<br/>
                        <a href="mailto:healthcare@consumersunion.org">healthcare@consumersunion.org</a>
                    </div>
                </div>
            </div>
            <div class="clear"></div>
            <p>    &copy; 2012 <a href="http://www.consumersunion.org/">Consumers Union</a>.
                Our <a href="http://www.consumersunion.org/about/privacy.htm">Privacy Policy</a>.
            </p>
        </div>
        <%--#footer--%>
    </footer>
</div>
<%--#main--%>

<%--[if IE 6]><span id="ftf_link"></span><script src="http://yourhealthsecurity.org/wordpress/wp-content/plugins/ie6-upgrade-option/warning.js" type="text/javascript" charset="ISO-8859-1"></script><script type="text/javascript" charset="ISO-8859-1">

			var ftf = new ftf();
			ftf.instance_name 	= "ftf";
			ftf.icon_size 	 	= "regular";
			ftf.output_to 	 	= "ftf_link";

			/*
			This feature is a Version 2.0 feature. It is the upgrade of the previous localization option.
			This must be a valid JSON file with the appropriate language translations.
			You must upload the language JSON file to your website for use. (ie. ftf.lang_external = "includes/cs.json";)
			*/
			ftf.lang_external	 	 = "http://yourhealthsecurity.org/wordpress/wp-content/plugins/ie6-upgrade-option/lang/en.json";

			/*
			If users browser has a 3(failed) rating.
			Would you like the script to automatically popup onload?
			The default value is true.
			*/
			ftf.onload 		= true;

			ftf.css_external 		= "http://yourhealthsecurity.org/wordpress/wp-content/plugins/ie6-upgrade-option/style.css";

			/*
			The following are the three different approval levels you may set to specific browsers:
			1 = Pass/Recommended
			2 = Pass/Acceptable
			3 = Fail

			Any other number will return an error.

			The following are the default values for each browser but can be easily changed by
			resetting the values using the following method.
			*/
			ftf.rate = {
			"firefox" : 1,
			"chrome" : 1,
			"opera" : 1,
			"safari" : 1,
			"ie6" : 3,
			"ie7" : 2,
			"ie8" : 1,
			"ie9" : 1
			};

			/*
			The following are the default values for each browser icon. Version 3.0 requires you to host your own icons.
			You can download a zip of all the icons above.
			*/
			ftf.icons = {

			"firefox" : "http://yourhealthsecurity.org/wordpress/wp-content/plugins/ie6-upgrade-option/icons/32/firefox.gif",
			"chrome" : 	"http://yourhealthsecurity.org/wordpress/wp-content/plugins/ie6-upgrade-option/icons/32/chrome.gif",
			"opera" : 	"http://yourhealthsecurity.org/wordpress/wp-content/plugins/ie6-upgrade-option/icons/32/opera.gif",
			"safari" : 	"http://yourhealthsecurity.org/wordpress/wp-content/plugins/ie6-upgrade-option/icons/32/safari.gif",
			"ie6" : 	"http://yourhealthsecurity.org/wordpress/wp-content/plugins/ie6-upgrade-option/icons/32/ie9.gif",
			"ie7" : 	"http://yourhealthsecurity.org/wordpress/wp-content/plugins/ie6-upgrade-option/icons/32/ie9.gif",
			"ie8" : 	"http://yourhealthsecurity.org/wordpress/wp-content/plugins/ie6-upgrade-option/icons/32/ie9.gif",
			"ie9" : 	"http://yourhealthsecurity.org/wordpress/wp-content/plugins/ie6-upgrade-option/icons/32/ie9.gif"

			};

			ftf.init(
			);

			</script><![endif]--%>
<script type="text/javascript">
    //<![CDATA[
    var rcGlobal = {
        serverUrl: 'http://yourhealthsecurity.org',
        infoTemp: '%REVIEWER% on %POST%',
        loadingText: 'Loading',
        noCommentsText: 'No comments',
        newestText: '&laquo; Newest',
        newerText: '&laquo; Newer',
        olderText: 'Older &raquo;',
        showContent: '',
        external: '',
        avatarSize: '',
        avatarPosition: '',
        anonymous: 'Anonymous',
        initJson: {items: []}
    };
    //]]>
</script>

<script type='text/html' src='<%=realContext %>/template/yhs/jquery.jsonp-2.1.4.min.js'></script>
<script type='text/html' src='<%=realContext %>/template/yhs/lib.js'></script>
<script type='text/html' src='<%=realContext %>/template/yhs/main.js'></script>
<script type='text/html' src='<%=realContext %>/template/yhs/wp-recentcomments.js'></script>

<script type='text/javascript'>
    /* <![CDATA[ */
    var TB_labels = {
        "no_config": "No configuration settings found",
        "twitter_logo": "Twitter Logo",
        "kino": "Development by Kirill Novitchenko",
        "refresh": "Refresh",
        "no_sources": "Twitter sources to blend are not defined",
        "no_global_config": "Cannot retrieve Tweet Blender configuration options",
        "version_msg": "Powered by Tweet Blender plugin v{0} blending {1}",
        "limit_msg": "You reached Twitter API connection limit",
        "no_tweets_msg": "No tweets found for {0}",
        "loading_msg": "Loading tweets...",
        "time_past": "{0} {1} ago",
        "time_future": "in {0} {1}",
        "second": "second",
        "seconds": "seconds",
        "minute": "minute",
        "minutes": "minutes",
        "hour": "hour",
        "hours": "hours",
        "day": "day",
        "days": "days",
        "week": "week",
        "weeks": "weeks",
        "month": "month",
        "months": "months",
        "year": "year",
        "years": "years",
        "check_fail": "Check failed",
        "limit_num": "Max is {0}\/hour",
        "limit_left": "You have {0} left",
        "from": "from",
        "reply": "reply",
        "follow": "follow",
        "limit_reset": "Next reset",
        "view_more": "view more"
    };
    /* ]]> */
</script>
</body>
</html>
