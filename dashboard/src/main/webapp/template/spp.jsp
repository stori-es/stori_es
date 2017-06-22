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
    <meta name="keywords" content=" , Safe Patient Project , End secrecy, save lives."/>
    <meta name="description" content=" | End secrecy, save lives."/>
    <meta charset="UTF-8"/>

    <link rel="profile" href="http://gmpg.org/xfn/11"/>
    <link rel="pingback" href="http://safepatientproject.org/xmlrpc.php"/>
    <link rel="alternate" type="application/rss+xml" title="Safe Patient Project (RSS)"
          href="http://safepatientproject.org/feed"/>
    <link rel="alternate" type="application/atom+xml" title="Safe Patient Project (Atom)"
          href="http://safepatientproject.org/feed/atom"/>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>

    <!--[if lt IE 9]>
    <script src="https://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->

    <%-- SYS --%>
    <script type="text/javascript" src="<%= context %>/questionnairemvp/questionnairemvp.nocache.js"></script>
    <script type="text/javascript">
        var CKEDITOR_BASEPATH = '${pageContext.request.contextPath}/questionnaire/ckeditor/';
    </script>
    <link rel="stylesheet" type="text/css" media="all" href="<%= context %>/style/main.css"/>
    <link rel="stylesheet" type="text/css" media="all" href="<%= context %>/template/spp/pfc.css"/>
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

    <style type="text/css">
        .recentcomments a {
            display: inline !important;
            padding: 0 !important;
            margin: 0 !important;
        }

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

    <link rel="stylesheet" type="text/css" media="all" href="<%= realContext %>/template/spp/style.css"/>
</head>

<body class="home blog cat-116-id cat-111-id" data-context-path="${pageContext.request.contextPath}">
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
    <img alt="Share this site!" src="<%= realContext %>/template/spp/safepatientproject.org-sharing-widget175x175.png"
         width="175" height="175"/>
    The Safe Patient Project is a Consumers
    Union campaign focused on eliminating medical harm, improving FDA
    oversight of prescription drugs and promoting disclosure laws that
    give information to consumers about health care safety and quality.
</div>

<div id="main">
    <div class="cu-logo">
        <a href="http://www.consumersunion.org"></a>
    </div>
    <header>
        <div id="header" class="container">
            <div id="header-content">
                <div id="header-image">
                    <h1>Safe Patient Project</h1>

                    <p>End secrecy, save lives.</p>
                    <a href="http://safepatientproject.org/" title="End secrecy, save lives."></a>
                </div>
                <%--#header-image--%>
                <div id="sidebar-header">
                    <div class="widget-area">
                        <div class="textwidget">
                            <div id="social-networking">
                                <ul>
                                    <li class="twitter"><a href="http://twitter.com/CUSafePatient "
                                                           title="Follow us on Twitter"></a></li>
                                    <li class="facebook"><a
                                            href="http://www.facebook.com/pages/Consumers-Union/17274984050"
                                            title="Find us on Facebook"></a></li>
                                    <li class="rss"><a href="http://safepatientproject.org/feed/feed/"
                                                       title="Subscribe to this blog's RSS feed"></a></li>
                                </ul>
                            </div>
                        </div>
                    </div>
                    <div class="widget-area">
                        <form method="get" class="searchform" action="http://safepatientproject.org">
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
                            <li id="menu-item-11"
                                class="menu-item menu-item-type-custom menu-item-object-custom menu-item-11"><a
                                    href="http://cu.convio.net/spp">Act Now</a></li>
                            <li id="menu-item-2432"
                                class="menu-item menu-item-type-post_type menu-item-object-page menu-item-2432"><a
                                    href="http://safepatientproject.org/blog">Blog</a></li>
                            <li id="menu-item-2436"
                                class="menu-item menu-item-type-post_type menu-item-object-page menu-item-2436"><a
                                    href="http://safepatientproject.org/topics">Topics</a>
                                <ul class="sub-menu">
                                    <li id="menu-item-2440"
                                        class="menu-item menu-item-type-taxonomy menu-item-object-category menu-item-2440">
                                        <a href="http://safepatientproject.org/topics/doctor_accountability">Doctor
                                            Accountability</a></li>
                                    <li id="menu-item-2699"
                                        class="menu-item menu-item-type-taxonomy menu-item-object-category menu-item-2699">
                                        <a href="http://safepatientproject.org/topics/drugs_and_medical_devices">Drugs
                                            &#038; Medical Devices</a>
                                        <ul class="sub-menu">
                                            <li id="menu-item-2443"
                                                class="menu-item menu-item-type-taxonomy menu-item-object-category menu-item-2443">
                                                <a href="http://safepatientproject.org/topics/drugs_and_medical_devices/medical_device_safety">Medical
                                                    Device Safety</a></li>
                                        </ul>
                                    </li>
                                    <li id="menu-item-2438"
                                        class="menu-item menu-item-type-taxonomy menu-item-object-category menu-item-2438">
                                        <a href="http://safepatientproject.org/topics/hospital_acquired_infections">Hospital
                                            Acquired Infections</a>
                                        <ul class="sub-menu">
                                            <li id="menu-item-2439"
                                                class="menu-item menu-item-type-taxonomy menu-item-object-category menu-item-2439">
                                                <a href="http://safepatientproject.org/topics/hospital_acquired_infections/mrsa">MRSA</a>
                                            </li>
                                            <li id="menu-item-2445"
                                                class="menu-item menu-item-type-taxonomy menu-item-object-category menu-item-2445">
                                                <a href="http://safepatientproject.org/topics/hospital_acquired_infections/c_diff">C.diff</a>
                                            </li>
                                        </ul>
                                    </li>
                                    <li id="menu-item-2441"
                                        class="menu-item menu-item-type-taxonomy menu-item-object-category menu-item-2441">
                                        <a href="http://safepatientproject.org/topics/medical_errors">Medical Errors</a>
                                    </li>
                                </ul>
                            </li>
                            <li id="menu-item-2433"
                                class="menu-item menu-item-type-post_type menu-item-object-page current-menu-item menu-item-2433 ">
                                <a href="http://safepatientproject.org/share-your-story">Share Your Story</a></li>
                            <li id="menu-item-3169"
                                class="menu-item menu-item-type-post_type menu-item-object-page menu-item-3169"><a
                                    href="http://safepatientproject.org/twitter">Twitter</a></li>
                            <li id="menu-item-2435"
                                class="menu-item menu-item-type-post_type menu-item-object-page menu-item-2435"><a
                                    href="http://safepatientproject.org/video">Video</a></li>
                            <li id="menu-item-87"
                                class="menu-item menu-item-type-custom menu-item-object-custom menu-item-87"><a
                                    href="http://safepatientproject.org/tags/activist">Activists</a></li>
                            <li id="menu-item-2420"
                                class="menu-item menu-item-type-custom menu-item-object-custom menu-item-2420"><a
                                    href="http://cu.convio.net/site/PageServer?pagename=spp__EOY_donate_health">Donate</a>
                            </li>
                            <li id="menu-item-2431"
                                class="menu-item menu-item-type-post_type menu-item-object-page menu-item-2431"><a
                                    href="http://safepatientproject.org/about">About Us</a></li>
                        </ul>
                    </div>
                </nav>
                <%--.primary--%>
            </div>
            <%--#header-content--%>
        </div>
        <%--#header--%>
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
        <%--#content--%>
    </div>
    <%--#body, .container--%>
    <div class="clear"></div>
    <footer>
        <div id="footer" class="container">
            <nav class="footer-menu">
                <ul id="menu-footer-menu" class="menu">
                    <li id="menu-item-20" class="menu-item menu-item-type-custom menu-item-object-custom menu-item-20">
                        <a href="http://cu.convio.net/spp">Act Now</a></li>
                    <li id="menu-item-2425"
                        class="menu-item menu-item-type-post_type menu-item-object-page menu-item-2425"><a
                            href="http://safepatientproject.org/blog">Blog</a></li>
                    <li id="menu-item-2427"
                        class="menu-item menu-item-type-post_type menu-item-object-page menu-item-2427"><a
                            href="http://safepatientproject.org/topics">Topics</a></li>
                    <li id="menu-item-2426"
                        class="menu-item menu-item-type-post_type menu-item-object-page menu-item-2426"><a
                            href="http://safepatientproject.org/share-your-story">Share Your Story</a></li>
                    <li id="menu-item-2428"
                        class="menu-item menu-item-type-post_type menu-item-object-page menu-item-2428"><a
                            href="http://safepatientproject.org/twitter">Twitter</a></li>
                    <li id="menu-item-2430"
                        class="menu-item menu-item-type-post_type menu-item-object-page menu-item-2430"><a
                            href="http://safepatientproject.org/video">Video</a></li>
                    <li id="menu-item-88" class="menu-item menu-item-type-custom menu-item-object-custom menu-item-88">
                        <a href="http://safepatientproject.org/tags/activist">Activists</a></li>
                    <li id="menu-item-26" class="menu-item menu-item-type-custom menu-item-object-custom menu-item-26">
                        <a href="http://cu.convio.net/site/PageServer?pagename=spp__EOY_donate_health">Donate</a></li>
                    <li id="menu-item-2424"
                        class="menu-item menu-item-type-post_type menu-item-object-page menu-item-2424"><a
                            href="http://safepatientproject.org/about">About Us</a></li>
                </ul>
            </nav>
            <div id="sidebar-footer">
                <div class="widget-area">
                    <h4>Topics</h4>
                    <ul id="almost_all_categories_widget">
                        <li class="cat-item cat-item-5"><a
                                href="http://safepatientproject.org/topics/doctor_accountability"
                                title="When physicians provide poor quality care, their patients are typically the last to know. Some physician backgrounds may be available in your state, but can you tell which ones have the most complaints, malpractice claims or disciplinary actions? Knowing the background information on your doctor could save your life. ">Doctor
                            Accountability</a></li>
                        <li class="cat-item cat-item-116"><a
                                href="http://safepatientproject.org/topics/drugs_and_medical_devices"
                                title="Americans have suffered and lost their lives because they are not given ALL information about risks by either manufacturers or the FDA. Pharmaceutical companies should be accountable for safety problems, and not keep drug risks hidden from the public. Medical device companies should respond to malfunctioning implants.">Drugs
                            &amp; Medical Devices</a></li>
                        <li class="cat-item cat-item-7"><a
                                href="http://safepatientproject.org/topics/hospital_acquired_infections"
                                title="Hospital acquired infections are a leading cause of death in the U.S. Consumers Union supports public disclosure of infection rates so that you can choose the safest hospital and hospitals will have an incentive to improve. ">Hospital
                            Acquired Infections</a></li>
                        <li class="cat-item cat-item-9"><a href="http://safepatientproject.org/topics/medical_errors"
                                                           title="Wrong surgery, wrong medication, serious bedsores... Unsafe practices and poor quality care kill 98,000 patients each year and waste billions of dollars every year. What information do you have about the safety of your hospital? What protections do you have if the hospital makes a mistake with you? ">Medical
                            Errors</a></li>
                    </ul>
                </div>
                <div class="widget-area">
                    <h4>Recent Posts</h4>
                    <ul>
                        <li><a href="http://safepatientproject.org/posts/3293-real-people-real-medical-device-stories-2"
                               title="Real People, Real Medical Device Stories">Real People, Real Medical Device
                            Stories</a></li>
                        <li><a href="http://safepatientproject.org/posts/3277-3277"
                               title="Problem Medical Devices: Read Avery&#8217;s story">Problem Medical Devices: Read
                            Avery&#8217;s story</a></li>
                        <li>
                            <a href="http://safepatientproject.org/posts/3220-senate-passes-fda-user-fees-law-speeds-up-drugdevice-approvals-slows-down-safety"
                               title="Congress passes FDA user fees law, speeds up drug/device approvals; slows down safety">Congress
                                passes FDA user fees law, speeds up drug/device approvals; slows down safety</a></li>
                    </ul>
                </div>
                <div class="widget-area">
                    <h4>Contact Us</h4>

                    <div class="textwidget"> Lisa McGiffert<br/> Project Director<br/> Consumers Union<br/> 506 West
                        14th St., Suite A<br/> Austin, Texas 78701<br/> <a href="mailto:safepatient@consumersunion.org">safepatient@consumersunion.org</a>
                    </div>
                </div>
            </div>
            <div class="clear"></div>
            <p class="center"> &copy; 2012 <a href="http://www.consumersunion.org/">Consumers Union</a>. Our <a
                    href="http://www.consumersunion.org/about/privacy.htm">Privacy Policy</a>.
            </p>
        </div>
        <%--#footer--%>
    </footer>
</div>
<%--#main--%>
<script>
    var rcGlobal = {
        serverUrl: 'http://safepatientproject.org',
        infoTemp: '%REVIEWER% on %POST%',
        loadingText: 'Loading',
        noCommentsText: 'No comments',
        newestText: '&laquo; Newest',
        newerText: '&laquo; Newer',
        olderText: 'Older &raquo;',
        showContent: '1',
        external: '1',
        avatarSize: '32',
        avatarPosition: 'left',
        anonymous: 'Anonymous'
    };
</script>

</body>
</html>
