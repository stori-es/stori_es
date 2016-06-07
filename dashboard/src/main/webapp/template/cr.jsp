<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<% request.setCharacterEncoding(
        "UTF-8"); // the charset stuff should set this on the Tomcat Connectors config, but for backup... except it seems ineffective %>
<% final String context = request.getContextPath().replace("^\\s*https://", "http://").trim(); %>
<% final String realContext = request.getContextPath().trim(); %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

    <title>stori.es | <%=(String) request.getAttribute("title") %>
    </title>

    <meta name="viewport" content="width=device-width, user-scalable=yes, initial-scale=1">

    <link rel="stylesheet" type="text/css" href="/template/cr/globalNavigationStandalone.css">
    <link rel="stylesheet" type="text/css" href="/template/cr/homepage.css">

    <jsp:include page="./../WEB-INF/jsp/Analytics.jsp"/>
    <script type="text/javascript" src="/template/cr/jquery.js"></script>
    <link type="text/css" rel="stylesheet"
          href="//fast.fonts.net/cssapi/c05bbfa2-a62a-4505-9231-09b9957a01e9.css"/>
    <link href="//netdna.bootstrapcdn.com/font-awesome/3.1.1/css/font-awesome.css" rel="stylesheet">

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

    <style text="text/css">
        #questionnaireWidget {
            margin: 40px;
            box-sizing: border-box;
        }

        .cu-collectionSurveyTitle {
            display: none !important;
        }
    </style>
</head>
<body cz-shortcut-listen="true">
<div class="globalheader parsys">
    <script>
        window.globalNavConfigurationApiUrl = 'https://api.consumerreports.org';
        window.globalNavConfigurationApiKey = 'atxv3a7taxumapc2qh2puy43';
    </script>


    <div class="global-header-mobile-overlay" style="display: none;"></div>

    <header class="global-header-container">
        <div class="container-fluid">
            <a class="global-header-burger visible-xs-block visible-sm-block visible-md-block" href="javascript:{}"></a>
            <a href="http://www.consumerreports.org/cro/index.htm" class="cro-logo"
               data-trackpagelink="[&#39;cro_logo&#39;, &#39;header&#39;]">
                <img alt="Consumer Reports" src="/template/cr/logo.svg">
            </a>
            <div class="global-header-tab-wrapper visible-lg-inline">
                <div class="global-header-top-nav-item active-carrot active">
                    <a id="product-review" href="javascript:{}"
                       data-trackpagelink="[&#39;product_reviews&#39;, &#39;header&#39;]">Product Reviews <img
                            src="/template/cr/arrow_down_black.png"></a>
                </div>
                <div class="global-header-top-nav-item">
                    <a id="issues-matter" href="javascript:{}"
                       data-trackpagelink="[&#39;issues_that_matter&#39;, &#39;header&#39;]">Issues That Matter <img
                            src="/template/cr/arrow_down_black.png"></a>
                </div>
                <div class="global-header-top-nav-item">
                    <a id="about-us" href="http://www.consumerreports.org/cro/about-us/index.htm"
                       data-trackpagelink="[&#39;about_us&#39;, &#39;header&#39;]">About Us</a>
                </div>
            </div>
            <div class="global-header-top-nav-account">
                <div class="global-header-account-btn">
                    <a href="https://donateconsumers.org/ea-action/action?ea.client.id=1926&amp;ea.campaign.id=35640&amp;sourcecode=6021000120&amp;en_txn6=6021000120"
                       data-trackpagelink="[&#39;donate&#39;, &#39;header&#39;]">Donate</a>
                </div>
                <div class="global-header-account-settings mobile_off">
                    <div>
                        <ol class="alert"></ol>
                        <ul class="global-header-renew-subscriptions">
                            <li class="global-header-buyCRO">
                                <a href="https://ec.consumerreports.org/ec/cro/order.htm?INTKEY=I57HLT0"
                                   data-trackpagelink="[&#39;subscribe_step2_digital&#39;, &#39;header&#39;]">Buy
                                    Digital Subscription</a>
                            </li>
                            <li class="global-header-buyCRMag">
                                <a href="https://ec.consumerreports.org/ec/cr/order.htm?pkey=crMagTwelveOnlyPromo&amp;INTKEY=IU41CD12"
                                   data-trackpagelink="[&#39;subscribe_step2_magazine&#39;, &#39;header&#39;]">Buy
                                    Magazine Subscription</a>
                            </li>
                        </ul>
                        <ul>
                            <li><a href="https://ec.consumerreports.org/ec/myaccount/main.htm"
                                   data-trackpagelink="[&#39;manage_my_account&#39;, &#39;header&#39;]">Manage My
                                Account</a></li>
                            <li class="header_signOut" style="display: none;"><a
                                    href="https://ec.consumerreports.org/ec/logout.htm">Sign Out</a></li>
                        </ul>
                        <ul class="mobile-invitation hidden-lg hidden-md hidden-sm" style="display: block;">
                            <li><a class="mobile_signIn" href="https://ec.consumerreports.org/ec/cro/mob/login.htm"
                                   data-trackpagelink="[&#39;mobile_sign_in_start&#39;, &#39;header&#39;]">Sign in</a>
                            </li>
                            <li><a class="mobile_subscribe"
                                   href="https://ec.consumerreports.org/ec/cro/mob/order.htm?INTKEY=I51MLT0"
                                   data-trackpagelink="[&#39;mobile_subscribe_step1&#39;, &#39;header&#39;]">Subscribe</a>
                            </li>
                        </ul>
                    </div>
                </div>
                <div class="global-header-subscribe-dropdown">
                    <div>
                        <ul>
                            <li>
                                <a href="https://ec.consumerreports.org/ec/cro/order.htm?INTKEY=I57HLT0"
                                   data-trackpagelink="[&#39;subscribe_step2_digital&#39;, &#39;header&#39;]"
                                   style="display: inline-block;">Buy Digital
                                    Subscription</a>
                            </li>
                            <li>
                                <a href="https://ec.consumerreports.org/ec/cr/order.htm?INTKEY=IW57CDR4"
                                   data-trackpagelink="[&#39;subscribe_step2_magazine&#39;, &#39;header&#39;]"
                                   style="display: inline-block;">Buy Magazine
                                    Subscription</a>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
            <div class="search-wrapping">
                <a class="global-header-search-icon open" href="javascript:{}" data-track="search_button-true_header"
                   data-track-title="Search" data-search-element="global-header-search-box"
                   style="background: url('/template/cr/search-icon.png') 50% 53% no-repeat">
                    <img src="/template/cr/search-icon-gray.png">
                </a>
                <div class="global-header-search-wrap ui-widget" style="display: block;">
                    <div>
                        <form name="global-header-search-form" id="global-header-search-form"
                              action="http://www.consumerreports.org/cro/search.htm" autocomplete="off">
                            <input type="text" id="global-header-search-box" name="query" autocomplete="off"
                                   data-track="search_enter-true_header" data-track-title="Search"
                                   data-search-element="global-header-search-box"
                                   onkeypress="if(event.keyCode==13){headerElementsTracking(this);return false;}"
                                   placeholder="" class="ui-autocomplete-input">
                            <a class="global-header-search-icon submit" href="javascript:{}"
                               data-track="search_button-true_header" data-track-title="Search"
                               data-search-element="global-header-search-box">
                                <span class="hidden-xs">Search</span><img class="hidden-lg hidden-md hidden-sm"
                                                                          src="/template/cr/search-icon-white.png">
                            </a>
                        </form>
                        <a href="http://www.consumerreports.org/cro/a-to-z-index/products/index.htm"
                           data-trackpagelink="['A_to_Z', 'header']">All Products A-Z</a>
                    </div>
                </div>
            </div>
        </div>
        <!--Start Tablet Verison-->
        <div class="hidden-lg global-header-mobile-container" style="display: none;">
            <div class="global-header-mobile-wrap">
                <div>
                    <a class="global-header-mobile-search mobile-search-open" href="javascript:{}"><img
                            src="/template/cr/search-icon.png"></a>
                    <a class="global-header-mobile-search mobile-search-submit" href="javascript:{}"><img
                            src="/template/cr/search-icon.png"></a>
                    <a class="global-header-mobile-close" href="javascript:{}"><img
                            src="/template/cr/icn-close.svg"></a>
                    <br class="clear">

                    <div class="global-header-mobile-search-wrap ui-widget" style="display: none;">
                        <form name="global-header-mobile-search-form" id="global-header-mobile-search-form"
                              action="http://www.consumerreports.org/cro/search.htm" autocomplete="off">
                            <input type="text" id="global-header-mobile-search-box" name="query" placeholder="Search"
                                   class="ui-autocomplete-input" autocomplete="off">
                        </form>
                    </div>
                </div>

                <!--Start back to categories section-->
                <div class="hidden-md hidden-sm global-header-mobile-back-to-categories">
                    <a href="javascript:{}">
                        <img src="/template/cr/arrow-left-gray.png">
                        All Product Review
                    </a>
                </div>
                <!--End back to categories section-->

                <div class="row">
                    <div class="global-header-mobile-top-list col-xs-4">
                        <div class="global-header-mobile-content-container active">
                            <a href="javascript:{}" class="active global-header-mobile-open-product-reviews">Product
                                Reviews</a>
                            <!--Start Franchises-->
                            <div class="global-header-mobile-sub-franchise">
                                <ul>

                                    <li class="global-header-mobile-franchise-28934" data-mobile-productid="28934">

                                        <a href="javascript:{}" title="Cars">Cars</a>
                                    </li>

                                    <li class="global-header-mobile-franchise-28949" data-mobile-productid="28949">

                                        <a href="javascript:{}" title="Electronics">Electronics</a>
                                    </li>

                                    <li class="global-header-mobile-franchise-28967" data-mobile-productid="28967">

                                        <a href="javascript:{}" title="Appliances">Appliances</a>
                                    </li>

                                    <li class="global-header-mobile-franchise-28937" data-mobile-productid="28937">

                                        <a href="javascript:{}" title="Home &amp; garden">Home &amp; garden</a>
                                    </li>

                                    <li class="global-header-mobile-franchise-28985" data-mobile-productid="28985">

                                        <a href="javascript:{}" title="Babies &amp; kids">Babies &amp; kids</a>
                                    </li>

                                    <li class="global-header-mobile-franchise-34458" data-mobile-productid="34458">

                                        <a href="javascript:{}" title="Money">Money</a>
                                    </li>

                                    <li class="global-header-mobile-franchise-36786" data-mobile-productid="36786">

                                        <a href="javascript:{}" title="Health">Health</a>
                                    </li>

                                    <li class="global-header-mobile-franchise-" data-mobile-productid="">


                                        <a href="http://consumerreports.org/cro/news/index.htm" title="News">News</a>


                                    </li>


                                </ul>
                            </div>
                        </div>
                        <div class="global-header-mobile-content-container">
                            <a href="javascript:{}" class="global-header-mobile-open-issues">Issues that Matter</a>

                            <div class="global-header-mobile-issues-matter">
                                <div class="mobile-issueItem-container">
                                    <a class="mobile-issue-item"
                                       href="https://consumersunion.org/surprise-medical-bills?source=CRO&amp;sub_source=top">
                                        <img class="issueNav-img" alt="End Surprise Medical Bills"
                                             data-original="http://www.consumerreports.org/etc/designs/cr/images/common/surprise_medical_bills.png">

                                        <div class="issue-item-title">End Surprise Medical Bills</div>
                                        <br class="clear">
                                    </a>
                                </div>

                                <div class="mobile-issueItem-container">
                                    <a class="mobile-issue-item"
                                       href="https://consumersunion.org/end-robocalls?source=CRO&amp;sub_source=top">
                                        <img class="issueNav-img" alt="End Robocalls"
                                             data-original="http://www.consumerreports.org/etc/designs/cr/images/common/robo_call.png">

                                        <div class="issue-item-title">End Robocalls</div>
                                        <br class="clear">
                                    </a>
                                </div>

                                <div class="mobile-issueItem-container">
                                    <a class="mobile-issue-item"
                                       href="http://www.consumerreports.org/cars/vw-diesel-emissions-recall">
                                        <img class="issueNav-img" alt="Guide to the Volkswagen Emissions Recall"
                                             data-original="http://www.consumerreports.org/etc/designs/cr/images/common/volkswagen-dieselgate-emissions-recall.png">

                                        <div class="issue-item-title">Guide to the Volkswagen Emissions Recall</div>
                                        <br class="clear">
                                    </a>
                                </div>

                                <div class="mobile-issueItem-container">
                                    <a class="mobile-issue-item"
                                       href="http://www.consumerreports.org/cro/health/GMO/index.htm">
                                        <img class="issueNav-img" alt="What You Need to Know About GMO Labeling"
                                             data-original="http://www.consumerreports.org/etc/designs/cr/images/common/gmo-labeling.png">

                                        <div class="issue-item-title">What You Need to Know About GMO Labeling</div>
                                        <br class="clear">
                                    </a>
                                </div>

                                <div class="mobile-issueItem-container">
                                    <a class="mobile-issue-item"
                                       href="http://www.consumerreports.org/cro/health/the-rise-of-superbugs/index.htm">
                                        <img class="issueNav-img" alt="The Rise of Superbugs"
                                             data-original="http://www.consumerreports.org/etc/designs/cr/images/common/rise-of-superbugs.png">

                                        <div class="issue-item-title">The Rise of Superbugs</div>
                                        <br class="clear">
                                    </a>
                                </div>

                                <div class="all-issues-wrap">
                                    <a class="all-issues-link"
                                       href="http://www.consumerreports.org/cro/about-us/policy-and-action-product-food-safety-financial-health-reform/index.htm">View
                                        All</a>
                                </div>
                            </div>
                        </div>
                        <div><a id="about-us" href="http://www.consumerreports.org/cro/about-us/index.htm"
                                data-trackpagelink="[&#39;about_us&#39;, &#39;header&#39;]">About Us</a></div>
                        <div class="hidden-md hidden-sm">
                            <a href="https://donateconsumers.org/ea-action/action?ea.client.id=1926&amp;ea.campaign.id=35640&amp;sourcecode=6021000120&amp;en_txn6=6021000120"
                               data-trackpagelink="[&#39;about_us&#39;, &#39;header&#39;]">Donate</a>
                        </div>
                    </div>
                    <!--Start Super Categories-->
                    <div class="global-header-mobile-superCat" style="display: none;">
                        <div class="global-header-mobile-superCat-item"></div>
                    </div>
                </div>

                <!--Mobile user info-->
                <div class="global-header-mobile-account hidden-md hidden-sm">
                    <div class="global-header-mobile-subscribe-wrap">
                        <div class="global-header-mobile-account-btn">
                            <a href="https://ec.consumerreports.org/ec/cro/mob/login.htm"
                               data-trackpagelink="[&#39;sign_in_start&#39;, &#39;header&#39;]">Sign In</a>
                        </div>
                        <div class="global-header-mobile-account-btn subscribe-btn-red">
                            <a href="https://ec.consumerreports.org/ec/cro/mob/order.htm?INTKEY=I51MLT0"
                               data-trackpagelink="[&#39;subscribe_step1&#39;, &#39;header&#39;]">Subscribe</a>
                        </div>
                    </div>
                    <div class="global-header-account-wrap">
                        <div class="global-header-account-abrev">
                            <span></span>
                        </div>
                        <div class="global-header-account-info">
                            <!--fsrHiddenBlockStart--><span class="pdata"></span><!--fsrHiddenBlockEnd-->
                        </div>
                    </div>
                </div>
                <!--End mobile user info-->

                <div class="socialContainer hidden-xs">

                    <a href="https://www.facebook.com/consumerreports" title="Facebook" target="_blank"
                       data-trackpagelink="[&#39;facebook fanpage&#39;, &#39;header&#39;, null, &#39;social&#39;]"
                       class="facebook">Facebook</a>
                    <a href="https://twitter.com/consumerreports" title="Twitter" target="_blank"
                       data-trackpagelink="[&#39;twitter cr handle&#39;, &#39;header&#39;, null, &#39;social&#39;]"
                       class="twitter">Twitter</a>
                    <a href="https://www.youtube.com/user/consumerreports" title="youtube" target="_blank"
                       data-trackpagelink="[&#39;youtube cr page&#39;, &#39;header&#39;, null, &#39;social&#39;]"
                       class="youtube">Youtube</a>
                    <a href="https://instagram.com/consumerreports" target="_blank" title="Instagram"
                       data-trackpagelink="[&#39;instagram cr page&#39;, &#39;header&#39;, null, &#39;social&#39;]"
                       class="instagram">Instagram</a>
                    <a class="more-icons" href="javascript:{}" title="More">More</a>
                    <a class="hiddenIcons google" href="https://plus.google.com/u/0/106084461720436231771/posts"
                       target="_blank" title="Google Plus"
                       data-trackpagelink="[&#39;google plus cr page&#39;, &#39;header&#39;, null, &#39;social&#39;]">Google
                        Plus</a>
                    <a class="hiddenIcons pinterest" href="https://www.pinterest.com/consumerreports/" target="_blank"
                       title="Pinterest"
                       data-trackpagelink="[&#39;pinterest cr page&#39;, &#39;header&#39;, null, &#39;social&#39;]">Pinterest</a>
                </div>
            </div>
        </div>
        <!--End Tablet Verison-->

        <div class="global-header-products-content global-header-nav-menu-item hidden-md hidden-sm hidden-xs">
            <nav id="global-nav-breadcrumb" style="display: block;">
                <ul class="global-header-sub-franchise-menu">
                    <li data-cfaid="28934" class="global-header-nav-menu-item-franchise">
                        <a class="global-header-sub-franchise-item" href="javascript:{}">Cars</a>
                    </li>
                    <li data-cfaid="28949" class="global-header-nav-menu-item-franchise">
                        <a class="global-header-sub-franchise-item" href="javascript:{}">Electronics</a>
                    </li>
                    <li data-cfaid="28967" class="global-header-nav-menu-item-franchise">
                        <a class="global-header-sub-franchise-item" href="javascript:{}">Appliances</a>
                    </li>
                    <li data-cfaid="28937" class="global-header-nav-menu-item-franchise">
                        <a class="global-header-sub-franchise-item" href="javascript:{}">Home &amp; Garden</a>
                    </li>
                    <li data-cfaid="28985" class="global-header-nav-menu-item-franchise">
                        <a class="global-header-sub-franchise-item" href="javascript:{}">Babies &amp; Kids</a>
                    </li>
                    <li data-cfaid="34458" class="global-header-nav-menu-item-franchise">
                        <a class="global-header-sub-franchise-item" href="javascript:{}">Money</a>
                    </li>
                    <li data-cfaid="36786" class="global-header-nav-menu-item-franchise">
                        <a class="global-header-sub-franchise-item" href="javascript:{}">Health</a>
                    </li>
                    <li><a class="global-header-sub-franchise-item"
                           href="http://www.consumerreports.org/cro/news/index.htm">News</a></li>
                    <li class="global-header-social-wrap">

                        <div class="socialContainer">

                            <a href="https://www.facebook.com/consumerreports" title="Facebook" target="_blank"
                               data-trackpagelink="[&#39;facebook fanpage&#39;, &#39;header&#39;, null, &#39;social&#39;]"
                               class="facebook">Facebook</a>
                            <a href="https://twitter.com/consumerreports" title="Twitter" target="_blank"
                               data-trackpagelink="[&#39;twitter cr handle&#39;, &#39;header&#39;, null, &#39;social&#39;]"
                               class="twitter">Twitter</a>
                            <a href="https://www.youtube.com/user/consumerreports" title="youtube" target="_blank"
                               data-trackpagelink="[&#39;youtube cr page&#39;, &#39;header&#39;, null, &#39;social&#39;]"
                               class="youtube">Youtube</a>
                            <a href="https://instagram.com/consumerreports" target="_blank" title="Instagram"
                               data-trackpagelink="[&#39;instagram cr page&#39;, &#39;header&#39;, null, &#39;social&#39;]"
                               class="instagram">Instagram</a>
                            <a class="more-icons" href="javascript:{}" title="More">More</a>
                            <a class="hiddenIcons google" href="https://plus.google.com/u/0/106084461720436231771/posts"
                               target="_blank" title="Google Plus"
                               data-trackpagelink="[&#39;google plus cr page&#39;, &#39;header&#39;, null, &#39;social&#39;]">Google
                                Plus</a>
                            <a class="hiddenIcons pinterest" href="https://www.pinterest.com/consumerreports/"
                               target="_blank" title="Pinterest"
                               data-trackpagelink="[&#39;pinterest cr page&#39;, &#39;header&#39;, null, &#39;social&#39;]">Pinterest</a>
                        </div>
                    </li>
                </ul>
            </nav>
            <div class="global-header-nav-cat-menu">
                <ul>
                    <li class="global-header-cfa-item cfaId-28934">
                        <ul>
                            <li>
                                <a class="global-header-cfa-item-close" href="javascript:{}"><img
                                        src="/template/cr/close-btn.png"></a>

                                <div class="global-header-superCat">

                                    <ul class="global-header-alt-items col-lg-2">
                                        <li>
                                            <a href="http://www.consumerreports.org/cro/cars/index.htm"
                                               data-trackpagelink="[&#39;new_cars&#39;, &#39;subheader&#39;, null, &#39;cars&#39;]">New
                                                Cars</a>
                                        </li>
                                        <li>
                                            <a href="http://www.consumerreports.org/cro/cars/used-cars/index.htm"
                                               data-trackpagelink="[&#39;used_cars&#39;, &#39;subheader&#39;, null, &#39;cars&#39;]">Used
                                                Cars</a>
                                        </li>
                                        <li>
                                            <a href="http://www.consumerreports.org/cro/cars/prices/index.htm"
                                               data-trackpagelink="[&#39;buying_&amp;_pricing&#39;, &#39;subheader&#39;, null, &#39;cars&#39;]">Car
                                                Buying &amp; Pricing</a>
                                        </li>
                                        <li>
                                            <a href="http://www.consumerreports.org/cro/cars/maintenance-repair/index.htm"
                                               data-trackpagelink="[&#39;maintenance_&amp;_repair&#39;, &#39;subheader&#39;, null, &#39;cars&#39;]">Maintenance
                                                &amp; Repair</a>
                                        </li>
                                        <li>
                                            <a href="http://www.consumerreports.org/cro/cars/safety-recalls.htm"
                                               data-trackpagelink="[&#39;car_safety&#39;, &#39;subheader&#39;, null, &#39;cars&#39;]">Car
                                                Safety</a>
                                        </li>
                                        <li>
                                            <a href="http://www.consumerreports.org/cro/cars/guide_to_fuel_economy/index.htm"
                                               data-trackpagelink="[&#39;fuel_economy&#39;, &#39;subheader&#39;, null, &#39;cars&#39;]">Fuel
                                                economy</a>
                                        </li>
                                        <li>
                                            <a href="http://www.consumerreports.org/cro/cars/guide_to_car_reliability/index.htm"
                                               data-trackpagelink="[&#39;relability_satisfaction&#39;, &#39;subheader&#39;, null, &#39;cars&#39;]">Reliability
                                                &amp; Satisfaction</a>
                                        </li>
                                        <li class="spotlight-item">
                                            <a href="http://www.consumerreports.org/cro/cars/best-cars-suvs-autos-spotlight/index.htm"
                                               data-trackpagelink="[&#39;autos_spotlight&#39;, &#39;subheader&#39;, null, &#39;cars&#39;]">
                                                <img alt="Consumer Reports"
                                                     src="/template/cr/autos-spotlight.png">
                                                <span>2016 Autos Spotlight</span>
                                            </a>
                                        </li>
                                    </ul>

                                    <ul class="global-header-alt-subnav col-lg-8">
                                        <li class="col-lg-4">
                                            <ul>
                                                <li class="global-header-alt-subnav-titles">See Ratings, Reliability
                                                    &amp; Recommended
                                                </li>
                                                <li><a href="http://www.consumerreports.org/cro/cars/convertibles.htm">Convertibles</a>
                                                </li>
                                                <li><a href="http://www.consumerreports.org/cro/cars/luxury-cars.htm">Luxury
                                                    cars</a></li>
                                                <li><a href="http://www.consumerreports.org/cro/cars/hybrids-evs.htm">Hybrids/EVs</a>
                                                </li>
                                                <li><a href="http://www.consumerreports.org/cro/cars/minivans.htm">Minivans</a>
                                                </li>
                                                <li><a href="http://www.consumerreports.org/cro/cars/pickup-trucks.htm">Pickup
                                                    trucks</a></li>
                                                <li>
                                                    <a href="http://www.consumerreports.org/cro/cars/sedans.htm">Sedans</a>
                                                </li>
                                                <li><a href="http://www.consumerreports.org/cro/cars/small-cars.htm">Small
                                                    cars</a></li>
                                                <li><a href="http://www.consumerreports.org/cro/cars/sports-cars.htm">Sports
                                                    cars</a></li>
                                                <li><a href="http://www.consumerreports.org/cro/cars/suvs.htm">SUVs</a>
                                                </li>
                                                <li>
                                                    <a href="http://www.consumerreports.org/cro/cars/wagons.htm">Wagons</a>
                                                </li>
                                            </ul>
                                        </li>
                                        <li class="col-lg-4">
                                            <ul>
                                                <li class="global-header-alt-subnav-titles">Tires &amp; Car
                                                    Accessories
                                                </li>
                                                <li><a href="http://www.consumerreports.org/cro/car-batteries.htm">Car
                                                    batteries</a></li>
                                                <li><a href="http://www.consumerreports.org/cro/car-wax.htm">Car wax</a>
                                                </li>
                                                <li><a href="http://www.consumerreports.org/cro/gps.htm">GPS</a></li>
                                                <li>
                                                    <a href="http://www.consumerreports.org/cro/tire-pressure-gauges.htm">Tire
                                                        pressure gauges</a></li>
                                                <li><a href="http://www.consumerreports.org/cro/jump-starters.htm">Jump
                                                    starters</a></li>
                                                <li><a href="http://www.consumerreports.org/cro/tires.html">Tires</a>
                                                </li>
                                                <li>
                                                    <a href="http://www.consumerreports.org/cro/wiper-blades/buying-guide.htm">Wiper
                                                        blades</a></li>
                                                <li><a href="http://www.consumerreports.org/cro/car-seats.htm">Car
                                                    seats</a></li>
                                            </ul>
                                        </li>
                                        <li class="col-lg-4">
                                            <ul>
                                                <li class="global-header-alt-subnav-titles">Build &amp; Buy Car Buying
                                                    Service
                                                </li>

                                                <li>
                                                    <a href="http://www.consumerreports.org/cro/car-prices-build-buy-service/index.htm?ep=S5">United
                                                        States</a></li>
                                                <li class="global-header-cars-last"><a
                                                        href="http://consumerreports.unhaggle.com/cbp/?keycode=I5BBAHU">Canada</a>
                                                </li>
                                            </ul>
                                        </li>
                                    </ul>
                                    <br class="clear">


                                    <div class="global-header-view-all">
                                        <a href="http://www.consumerreports.org/cro/cars/index.htm">All Cars</a>
                                        <a href="http://www.consumerreports.org/cro/news/index.htm#url=/bin/feedinfo.tag%3DproductsAndServices%3Ataxonomy|cars.json&amp;title=Cars">Cars
                                            News</a>
                                    </div>
                                </div>
                            </li>
                        </ul>
                    </li>
                    <li class="global-header-cfa-item cfaId-28949">
                        <ul>
                            <li>
                                <a class="global-header-cfa-item-close" href="javascript:{}"><img
                                        src="/template/cr/close-btn.png"></a>

                                <div class="global-header-superCat">

                                    <ul>

                                        <li>
                                            <span>Audio &amp; video</span>
                                            <div>
                                                <ul class="global-header-subNav">


                                                    <li><a href="http://www.consumerreports.org/cro/blu-ray-players.htm"
                                                           data-trackpagelink="[&#39;Blu-ray_players&#39;, &#39;subheader&#39;, null, &#39;electronics&#39;]">Blu-ray
                                                        players</a></li>


                                                    <li><a href="http://www.consumerreports.org/cro/camcorders.htm"
                                                           data-trackpagelink="[&#39;Camcorders&#39;, &#39;subheader&#39;, null, &#39;electronics&#39;]">Camcorders</a>
                                                    </li>


                                                    <li><a href="http://www.consumerreports.org/cro/headphones.htm"
                                                           data-trackpagelink="[&#39;Headphones&#39;, &#39;subheader&#39;, null, &#39;electronics&#39;]">Headphones</a>
                                                    </li>


                                                    <li><a href="http://www.consumerreports.org/cro/sound-bars.htm"
                                                           data-trackpagelink="[&#39;Sound_bars&#39;, &#39;subheader&#39;, null, &#39;electronics&#39;]">Sound
                                                        bars</a></li>


                                                    <li>
                                                        <a href="http://www.consumerreports.org/cro/streaming-media-players-services.htm"
                                                           data-trackpagelink="[&#39;Streaming_media_players_&amp;_services&#39;, &#39;subheader&#39;, null, &#39;electronics&#39;]">Streaming
                                                            media players &amp; services</a></li>


                                                    <li><a href="http://www.consumerreports.org/cro/tvs.htm"
                                                           data-trackpagelink="[&#39;TVs&#39;, &#39;subheader&#39;, null, &#39;electronics&#39;]">TVs</a>
                                                    </li>


                                                    <li>
                                                        <a href="http://www.consumerreports.org/cro/video-game-consoles/buying-guide.htm"
                                                           data-trackpagelink="[&#39;Video-game_consoles&#39;, &#39;subheader&#39;, null, &#39;electronics&#39;]">Video-game
                                                            consoles</a></li>


                                                    <li>
                                                        <a href="http://www.consumerreports.org/cro/wireless-speakers.htm"
                                                           data-trackpagelink="[&#39;Wireless_speakers&#39;, &#39;subheader&#39;, null, &#39;electronics&#39;]">Wireless
                                                            speakers</a></li>

                                                </ul>
                                                <br class="clear">
                                            </div>
                                        </li>

                                    </ul>
                                    <ul>
                                        <li>
                                            <span>Computers &amp; Internet</span>
                                            <div>
                                                <ul class="global-header-subNav">


                                                    <li><a href="http://www.consumerreports.org/cro/computers.htm"
                                                           data-trackpagelink="[&#39;Computers&#39;, &#39;subheader&#39;, null, &#39;electronics&#39;]">Computers</a>
                                                    </li>


                                                    <li>
                                                        <a href="http://www.consumerreports.org/cro/computer-backup-systems/buying-guide.htm"
                                                           data-trackpagelink="[&#39;Computer_backup_systems&#39;, &#39;subheader&#39;, null, &#39;electronics&#39;]">Computer
                                                            backup systems</a></li>


                                                    <li>
                                                        <a href="http://www.consumerreports.org/cro/computer-monitors.htm"
                                                           data-trackpagelink="[&#39;Computer_monitors&#39;, &#39;subheader&#39;, null, &#39;electronics&#39;]">Computer
                                                            monitors</a></li>


                                                    <li><a href="http://www.consumerreports.org/cro/printers.htm"
                                                           data-trackpagelink="[&#39;Printers&#39;, &#39;subheader&#39;, null, &#39;electronics&#39;]">Printers</a>
                                                    </li>


                                                    <li>
                                                        <a href="http://www.consumerreports.org/cro/security-software.htm"
                                                           data-trackpagelink="[&#39;Security_software&#39;, &#39;subheader&#39;, null, &#39;electronics&#39;]">Security
                                                            software</a></li>


                                                    <li><a href="http://www.consumerreports.org/cro/tablets.htm"
                                                           data-trackpagelink="[&#39;Tablets&#39;, &#39;subheader&#39;, null, &#39;electronics&#39;]">Tablets</a>
                                                    </li>


                                                    <li>
                                                        <a href="http://www.consumerreports.org/cro/telecom-services.htm"
                                                           data-trackpagelink="[&#39;Telecom_services&#39;, &#39;subheader&#39;, null, &#39;electronics&#39;]">Telecom
                                                            services</a></li>


                                                    <li>
                                                        <a href="http://www.consumerreports.org/cro/wireless-routers.htm"
                                                           data-trackpagelink="[&#39;Wireless_routers&#39;, &#39;subheader&#39;, null, &#39;electronics&#39;]">Wireless
                                                            routers</a></li>

                                                </ul>
                                                <br class="clear">
                                            </div>
                                        </li>

                                    </ul>
                                    <ul>
                                        <li>
                                            <span>Digital cameras &amp; photography</span>
                                            <div>
                                                <ul class="global-header-subNav">


                                                    <li><a href="http://www.consumerreports.org/cro/digital-cameras.htm"
                                                           data-trackpagelink="[&#39;Digital_cameras&#39;, &#39;subheader&#39;, null, &#39;electronics&#39;]">Digital
                                                        cameras</a></li>

                                                </ul>
                                                <br class="clear">
                                            </div>
                                        </li>

                                        <li>
                                            <span>Phones &amp; mobile devices</span>
                                            <div>
                                                <ul class="global-header-subNav">


                                                    <li>
                                                        <a href="http://www.consumerreports.org/cro/cell-phones-services.htm"
                                                           data-trackpagelink="[&#39;Cell_phones_&amp;_services&#39;, &#39;subheader&#39;, null, &#39;electronics&#39;]">Cell
                                                            phones &amp; services</a></li>


                                                    <li><a href="http://www.consumerreports.org/cro/cordless-phones.htm"
                                                           data-trackpagelink="[&#39;Cordless_phones&#39;, &#39;subheader&#39;, null, &#39;electronics&#39;]">Cordless
                                                        phones</a></li>


                                                    <li><a href="http://www.consumerreports.org/cro/e-book-readers.htm"
                                                           data-trackpagelink="[&#39;E-book_readers&#39;, &#39;subheader&#39;, null, &#39;electronics&#39;]">E-book
                                                        readers</a></li>


                                                    <li>
                                                        <a href="http://www.consumerreports.org/cro/fitness-trackers.htm"
                                                           data-trackpagelink="[&#39;Fitness_trackers&#39;, &#39;subheader&#39;, null, &#39;electronics&#39;]">Fitness
                                                            trackers</a></li>


                                                    <li><a href="http://www.consumerreports.org/cro/gps.htm"
                                                           data-trackpagelink="[&#39;GPS&#39;, &#39;subheader&#39;, null, &#39;electronics&#39;]">GPS</a>
                                                    </li>


                                                    <li>
                                                        <a href="http://www.consumerreports.org/cro/mobile-security-software.htm"
                                                           data-trackpagelink="[&#39;Mobile_security_software&#39;, &#39;subheader&#39;, null, &#39;electronics&#39;]">Mobile
                                                            security software</a></li>


                                                    <li><a href="http://www.consumerreports.org/cro/smartwatch.htm"
                                                           data-trackpagelink="[&#39;Smartwatches&#39;, &#39;subheader&#39;, null, &#39;electronics&#39;]">Smartwatches</a>
                                                    </li>

                                                </ul>
                                                <br class="clear">
                                            </div>
                                        </li>

                                    </ul>
                                    <ul>
                                        <li>
                                            <span>Supplies &amp; accessories</span>
                                            <div>
                                                <ul class="global-header-subNav">


                                                    <li><a href="http://www.consumerreports.org/cro/batteries.htm"
                                                           data-trackpagelink="[&#39;Batteries&#39;, &#39;subheader&#39;, null, &#39;electronics&#39;]">Batteries</a>
                                                    </li>

                                                </ul>
                                                <br class="clear">
                                            </div>
                                        </li>

                                    </ul>
                                    <br class="clear">


                                    <div class="global-header-view-all">
                                        <a href="http://www.consumerreports.org/cro/electronics-computers/index.htm">All
                                            Electronics</a>
                                        <a href="http://www.consumerreports.org/cro/news/index.htm#url=/bin/feedinfo.tag%3DproductsAndServices%3Ataxonomy|electronicsComputers.json&amp;title=Electronics%20%26%20computers">Electronics
                                            News</a>
                                    </div>
                                </div>
                            </li>
                        </ul>
                    </li>
                    <li class="global-header-cfa-item cfaId-28967">
                        <ul>
                            <li>
                                <a class="global-header-cfa-item-close" href="javascript:{}"><img
                                        src="/template/cr/close-btn.png"></a>

                                <div class="global-header-superCat">

                                    <ul>

                                        <li>
                                            <span>Heating, cooling &amp; air</span>
                                            <div>
                                                <ul class="global-header-subNav">


                                                    <li>
                                                        <a href="http://www.consumerreports.org/cro/air-conditioners.htm"
                                                           data-trackpagelink="[&#39;Air_conditioners&#39;, &#39;subheader&#39;, null, &#39;appliances&#39;]">Air
                                                            conditioners</a></li>


                                                    <li><a href="http://www.consumerreports.org/cro/air-purifiers.htm"
                                                           data-trackpagelink="[&#39;Air_purifiers&#39;, &#39;subheader&#39;, null, &#39;appliances&#39;]">Air
                                                        purifiers</a></li>


                                                    <li>
                                                        <a href="http://www.consumerreports.org/cro/central-air-conditioning/buying-guide.htm"
                                                           data-trackpagelink="[&#39;Central_air_conditioning&#39;, &#39;subheader&#39;, null, &#39;appliances&#39;]">Central
                                                            air conditioning</a></li>


                                                    <li><a href="http://www.consumerreports.org/cro/dehumidifiers.htm"
                                                           data-trackpagelink="[&#39;Dehumidifiers&#39;, &#39;subheader&#39;, null, &#39;appliances&#39;]">Dehumidifiers</a>
                                                    </li>


                                                    <li>
                                                        <a href="http://www.consumerreports.org/cro/gas-furnaces/buying-guide.htm"
                                                           data-trackpagelink="[&#39;Gas_furnaces&#39;, &#39;subheader&#39;, null, &#39;appliances&#39;]">Gas
                                                            furnaces</a></li>


                                                    <li><a href="http://www.consumerreports.org/cro/humidifiers.htm"
                                                           data-trackpagelink="[&#39;Humidifiers&#39;, &#39;subheader&#39;, null, &#39;appliances&#39;]">Humidifiers</a>
                                                    </li>


                                                    <li><a href="http://www.consumerreports.org/cro/space-heaters.htm"
                                                           data-trackpagelink="[&#39;Space_heaters&#39;, &#39;subheader&#39;, null, &#39;appliances&#39;]">Space
                                                        heaters</a></li>


                                                    <li><a href="http://www.consumerreports.org/cro/thermostats.htm"
                                                           data-trackpagelink="[&#39;Thermostats&#39;, &#39;subheader&#39;, null, &#39;appliances&#39;]">Thermostats</a>
                                                    </li>


                                                    <li>
                                                        <a href="http://www.consumerreports.org/cro/water-heaters/buying-guide.htm"
                                                           data-trackpagelink="[&#39;Water_heaters&#39;, &#39;subheader&#39;, null, &#39;appliances&#39;]">Water
                                                            heaters</a></li>

                                                </ul>
                                                <br class="clear">
                                            </div>
                                        </li>

                                    </ul>
                                    <ul>
                                        <li>
                                            <span>Kitchen appliances</span>
                                            <div>
                                                <ul class="global-header-subNav">


                                                    <li><a href="http://www.consumerreports.org/cro/blenders.htm"
                                                           data-trackpagelink="[&#39;Blenders&#39;, &#39;subheader&#39;, null, &#39;appliances&#39;]">Blenders</a>
                                                    </li>


                                                    <li><a href="http://www.consumerreports.org/cro/coffee-makers.htm"
                                                           data-trackpagelink="[&#39;Coffee_makers&#39;, &#39;subheader&#39;, null, &#39;appliances&#39;]">Coffee
                                                        makers</a></li>


                                                    <li>
                                                        <a href="http://www.consumerreports.org/cro/cooktops-wall-ovens.htm"
                                                           data-trackpagelink="[&#39;Cooktops_&amp;_wall_ovens&#39;, &#39;subheader&#39;, null, &#39;appliances&#39;]">Cooktops
                                                            &amp; wall ovens</a></li>


                                                    <li><a href="http://www.consumerreports.org/cro/dishwashers.htm"
                                                           data-trackpagelink="[&#39;Dishwashers&#39;, &#39;subheader&#39;, null, &#39;appliances&#39;]">Dishwashers</a>
                                                    </li>


                                                    <li>
                                                        <a href="http://www.consumerreports.org/cro/food-processors-choppers.htm"
                                                           data-trackpagelink="[&#39;Food_processors_&amp;_choppers&#39;, &#39;subheader&#39;, null, &#39;appliances&#39;]">Food
                                                            processors &amp; choppers</a></li>


                                                    <li><a href="http://www.consumerreports.org/cro/freezers.htm"
                                                           data-trackpagelink="[&#39;Freezers&#39;, &#39;subheader&#39;, null, &#39;appliances&#39;]">Freezers</a>
                                                    </li>


                                                    <li>
                                                        <a href="http://www.consumerreports.org/cro/garbage-disposers.htm"
                                                           data-trackpagelink="[&#39;Garbage_disposers&#39;, &#39;subheader&#39;, null, &#39;appliances&#39;]">Garbage
                                                            disposers</a></li>


                                                    <li><a href="http://www.consumerreports.org/cro/hot-plates.htm"
                                                           data-trackpagelink="[&#39;Hot_plates&#39;, &#39;subheader&#39;, null, &#39;appliances&#39;]">Hot
                                                        plates</a></li>


                                                    <li><a href="http://www.consumerreports.org/cro/juicers.htm"
                                                           data-trackpagelink="[&#39;Juicers&#39;, &#39;subheader&#39;, null, &#39;appliances&#39;]">Juicers</a>
                                                    </li>


                                                    <li>
                                                        <a href="http://www.consumerreports.org/cro/meat-thermometers.htm"
                                                           data-trackpagelink="[&#39;Meat_thermometers&#39;, &#39;subheader&#39;, null, &#39;appliances&#39;]">Meat
                                                            thermometers</a></li>

                                                </ul>
                                                <ul class="global-header-subNav">


                                                    <li><a href="http://www.consumerreports.org/cro/microwave-ovens.htm"
                                                           data-trackpagelink="[&#39;Microwave_ovens&#39;, &#39;subheader&#39;, null, &#39;appliances&#39;]">Microwave
                                                        ovens</a></li>


                                                    <li><a href="http://www.consumerreports.org/cro/mixers.htm"
                                                           data-trackpagelink="[&#39;Mixers&#39;, &#39;subheader&#39;, null, &#39;appliances&#39;]">Mixers</a>
                                                    </li>


                                                    <li><a href="http://www.consumerreports.org/cro/ranges.htm"
                                                           data-trackpagelink="[&#39;Ranges&#39;, &#39;subheader&#39;, null, &#39;appliances&#39;]">Ranges</a>
                                                    </li>


                                                    <li>
                                                        <a href="http://www.consumerreports.org/cro/range-hoods/buying-guide.htm"
                                                           data-trackpagelink="[&#39;Range_hoods&#39;, &#39;subheader&#39;, null, &#39;appliances&#39;]">Range
                                                            hoods</a></li>


                                                    <li><a href="http://www.consumerreports.org/cro/refrigerators.htm"
                                                           data-trackpagelink="[&#39;Refrigerators&#39;, &#39;subheader&#39;, null, &#39;appliances&#39;]">Refrigerators</a>
                                                    </li>


                                                    <li>
                                                        <a href="http://www.consumerreports.org/cro/refrigerator-thermometers.htm"
                                                           data-trackpagelink="[&#39;Refrigerator_thermometers&#39;, &#39;subheader&#39;, null, &#39;appliances&#39;]">Refrigerator
                                                            thermometers</a></li>


                                                    <li>
                                                        <a href="http://www.consumerreports.org/cro/slow-cookers/buying-guide.htm"
                                                           data-trackpagelink="[&#39;Slow_cookers&#39;, &#39;subheader&#39;, null, &#39;appliances&#39;]">Slow
                                                            cookers</a></li>


                                                    <li><a href="http://www.consumerreports.org/cro/toasters.htm"
                                                           data-trackpagelink="[&#39;Toasters&#39;, &#39;subheader&#39;, null, &#39;appliances&#39;]">Toasters</a>
                                                    </li>


                                                    <li><a href="http://www.consumerreports.org/cro/wine-chillers.htm"
                                                           data-trackpagelink="[&#39;Wine_chillers&#39;, &#39;subheader&#39;, null, &#39;appliances&#39;]">Wine
                                                        chillers</a></li>

                                                </ul>
                                                <br class="clear">
                                            </div>
                                        </li>

                                    </ul>
                                    <ul>
                                        <li>
                                            <span>Laundry &amp; cleaning</span>
                                            <div>
                                                <ul class="global-header-subNav">


                                                    <li>
                                                        <a href="http://www.consumerreports.org/cro/all-purpose-cleaners.htm"
                                                           data-trackpagelink="[&#39;All-purpose_cleaners&#39;, &#39;subheader&#39;, null, &#39;appliances&#39;]">All-purpose
                                                            cleaners</a></li>


                                                    <li><a href="http://www.consumerreports.org/cro/carpet-cleaners.htm"
                                                           data-trackpagelink="[&#39;Carpet_cleaners&#39;, &#39;subheader&#39;, null, &#39;appliances&#39;]">Carpet
                                                        cleaners</a></li>


                                                    <li>
                                                        <a href="http://www.consumerreports.org/cro/carpet-stain-removers.htm"
                                                           data-trackpagelink="[&#39;Carpet_stain_removers&#39;, &#39;subheader&#39;, null, &#39;appliances&#39;]">Carpet
                                                            stain removers</a></li>


                                                    <li><a href="http://www.consumerreports.org/cro/clothes-dryers.htm"
                                                           data-trackpagelink="[&#39;Clothes_dryers&#39;, &#39;subheader&#39;, null, &#39;appliances&#39;]">Clothes
                                                        dryers</a></li>


                                                    <li>
                                                        <a href="http://www.consumerreports.org/cro/dishwasher-detergents.htm"
                                                           data-trackpagelink="[&#39;Dishwasher_detergents&#39;, &#39;subheader&#39;, null, &#39;appliances&#39;]">Dishwasher
                                                            detergents</a></li>


                                                    <li>
                                                        <a href="http://www.consumerreports.org/cro/garbage-bags/buying-guide.htm"
                                                           data-trackpagelink="[&#39;Garbage_bags&#39;, &#39;subheader&#39;, null, &#39;appliances&#39;]">Garbage
                                                            bags</a></li>


                                                    <li>
                                                        <a href="http://www.consumerreports.org/cro/laundry-detergents.htm"
                                                           data-trackpagelink="[&#39;Laundry_detergents&#39;, &#39;subheader&#39;, null, &#39;appliances&#39;]">Laundry
                                                            detergents</a></li>


                                                    <li><a href="http://www.consumerreports.org/cro/paper-towels.htm"
                                                           data-trackpagelink="[&#39;Paper_towels&#39;, &#39;subheader&#39;, null, &#39;appliances&#39;]">Paper
                                                        towels</a></li>


                                                    <li>
                                                        <a href="http://www.consumerreports.org/cro/sewing-machines/buying-guide.htm"
                                                           data-trackpagelink="[&#39;Sewing_machines&#39;, &#39;subheader&#39;, null, &#39;appliances&#39;]">Sewing
                                                            machines</a></li>


                                                    <li><a href="http://www.consumerreports.org/cro/steam-irons.htm"
                                                           data-trackpagelink="[&#39;Steam_irons&#39;, &#39;subheader&#39;, null, &#39;appliances&#39;]">Steam
                                                        irons</a></li>

                                                </ul>
                                                <ul class="global-header-subNav">


                                                    <li><a href="http://www.consumerreports.org/cro/steam-mops.htm"
                                                           data-trackpagelink="[&#39;Steam_mops&#39;, &#39;subheader&#39;, null, &#39;appliances&#39;]">Steam
                                                        mops</a></li>


                                                    <li>
                                                        <a href="http://www.consumerreports.org/cro/toilet-bowl-cleaners/buying-guide.htm"
                                                           data-trackpagelink="[&#39;Toilet-bowl_cleaners&#39;, &#39;subheader&#39;, null, &#39;appliances&#39;]">Toilet-bowl
                                                            cleaners</a></li>


                                                    <li><a href="http://www.consumerreports.org/cro/vacuum-cleaners.htm"
                                                           data-trackpagelink="[&#39;Vacuum_cleaners&#39;, &#39;subheader&#39;, null, &#39;appliances&#39;]">Vacuum
                                                        cleaners</a></li>


                                                    <li>
                                                        <a href="http://www.consumerreports.org/cro/washing-machines.htm"
                                                           data-trackpagelink="[&#39;Washing_machines&#39;, &#39;subheader&#39;, null, &#39;appliances&#39;]">Washing
                                                            machines</a></li>


                                                    <li><a href="http://www.consumerreports.org/cro/wet-dry-vacuums.htm"
                                                           data-trackpagelink="[&#39;Wet\/dry_vacuums&#39;, &#39;subheader&#39;, null, &#39;appliances&#39;]">Wet/dry
                                                        vacuums</a></li>

                                                </ul>
                                                <br class="clear">
                                            </div>
                                        </li>

                                    </ul>
                                    <ul>
                                    </ul>
                                    <br class="clear">


                                    <div class="global-header-view-all">
                                        <a href="http://www.consumerreports.org/cro/appliances/index.htm">All
                                            Appliances</a>
                                        <a href="http://www.consumerreports.org/cro/news/index.htm#url=/bin/feedinfo.tag%3DproductsAndServices%3Ataxonomy|appliances.json&amp;title=Appliances">Appliances
                                            News</a>
                                    </div>
                                </div>
                            </li>
                        </ul>
                    </li>
                    <li class="global-header-cfa-item cfaId-28937">
                        <ul>
                            <li>
                                <a class="global-header-cfa-item-close" href="javascript:{}"><img
                                        src="/template/cr/close-btn.png"></a>

                                <div class="global-header-superCat">

                                    <ul>

                                        <li>
                                            <span>Bed &amp; bath</span>
                                            <div>
                                                <ul class="global-header-subNav">


                                                    <li><a href="http://www.consumerreports.org/cro/mattresses.htm"
                                                           data-trackpagelink="[&#39;Mattresses&#39;, &#39;subheader&#39;, null, &#39;home_&amp;_garden&#39;]">Mattresses</a>
                                                    </li>


                                                    <li>
                                                        <a href="http://www.consumerreports.org/cro/pillows/buying-guide.htm"
                                                           data-trackpagelink="[&#39;Pillows&#39;, &#39;subheader&#39;, null, &#39;home_&amp;_garden&#39;]">Pillows</a>
                                                    </li>


                                                    <li><a href="http://www.consumerreports.org/cro/sheets.htm"
                                                           data-trackpagelink="[&#39;Sheets&#39;, &#39;subheader&#39;, null, &#39;home_&amp;_garden&#39;]">Sheets</a>
                                                    </li>


                                                    <li><a href="http://www.consumerreports.org/cro/showerheads.htm"
                                                           data-trackpagelink="[&#39;Showerheads&#39;, &#39;subheader&#39;, null, &#39;home_&amp;_garden&#39;]">Showerheads</a>
                                                    </li>


                                                    <li><a href="http://www.consumerreports.org/cro/toilets.htm"
                                                           data-trackpagelink="[&#39;Toilets&#39;, &#39;subheader&#39;, null, &#39;home_&amp;_garden&#39;]">Toilets</a>
                                                    </li>


                                                    <li><a href="http://www.consumerreports.org/cro/toilet-paper.htm"
                                                           data-trackpagelink="[&#39;Toilet_paper&#39;, &#39;subheader&#39;, null, &#39;home_&amp;_garden&#39;]">Toilet
                                                        paper</a></li>

                                                </ul>
                                                <br class="clear">
                                            </div>
                                        </li>

                                    </ul>
                                    <ul>
                                        <li>
                                            <span>Home improvement</span>
                                            <div>
                                                <ul class="global-header-subNav">


                                                    <li>
                                                        <a href="http://www.consumerreports.org/cro/co-and-smoke-alarms.htm"
                                                           data-trackpagelink="[&#39;CO_&amp;_smoke_alarms&#39;, &#39;subheader&#39;, null, &#39;home_&amp;_garden&#39;]">CO
                                                            &amp; smoke alarms</a></li>


                                                    <li><a href="http://www.consumerreports.org/cro/countertops.htm"
                                                           data-trackpagelink="[&#39;Countertops&#39;, &#39;subheader&#39;, null, &#39;home_&amp;_garden&#39;]">Countertops</a>
                                                    </li>


                                                    <li><a href="http://www.consumerreports.org/cro/decking.htm"
                                                           data-trackpagelink="[&#39;Decking&#39;, &#39;subheader&#39;, null, &#39;home_&amp;_garden&#39;]">Decking</a>
                                                    </li>


                                                    <li><a href="http://www.consumerreports.org/cro/door-locks.htm"
                                                           data-trackpagelink="[&#39;Door_locks&#39;, &#39;subheader&#39;, null, &#39;home_&amp;_garden&#39;]">Door
                                                        locks</a></li>


                                                    <li>
                                                        <a href="http://www.consumerreports.org/cro/entry-doors/buying-guide.htm"
                                                           data-trackpagelink="[&#39;Entry_doors&#39;, &#39;subheader&#39;, null, &#39;home_&amp;_garden&#39;]">Entry
                                                            doors</a></li>


                                                    <li>
                                                        <a href="http://www.consumerreports.org/cro/faucets/buying-guide.htm"
                                                           data-trackpagelink="[&#39;Faucets&#39;, &#39;subheader&#39;, null, &#39;home_&amp;_garden&#39;]">Faucets</a>
                                                    </li>


                                                    <li>
                                                        <a href="http://www.consumerreports.org/cro/fire-extinguishers/buying-guide.htm"
                                                           data-trackpagelink="[&#39;Fire_extinguishers&#39;, &#39;subheader&#39;, null, &#39;home_&amp;_garden&#39;]">Fire
                                                            extinguishers</a></li>


                                                    <li><a href="http://www.consumerreports.org/cro/flooring.htm"
                                                           data-trackpagelink="[&#39;Flooring&#39;, &#39;subheader&#39;, null, &#39;home_&amp;_garden&#39;]">Flooring</a>
                                                    </li>


                                                    <li><a href="http://www.consumerreports.org/cro/generators.htm"
                                                           data-trackpagelink="[&#39;Generators&#39;, &#39;subheader&#39;, null, &#39;home_&amp;_garden&#39;]">Generators</a>
                                                    </li>


                                                    <li><a href="http://www.consumerreports.org/cro/glues.htm"
                                                           data-trackpagelink="[&#39;Glues&#39;, &#39;subheader&#39;, null, &#39;home_&amp;_garden&#39;]">Glues</a>
                                                    </li>

                                                </ul>
                                                <ul class="global-header-subNav">


                                                    <li><a href="http://www.consumerreports.org/cro/home-windows.htm"
                                                           data-trackpagelink="[&#39;Home_windows&#39;, &#39;subheader&#39;, null, &#39;home_&amp;_garden&#39;]">Home
                                                        windows</a></li>


                                                    <li>
                                                        <a href="http://www.consumerreports.org/cro/ladders/buying-guide.htm"
                                                           data-trackpagelink="[&#39;Ladders&#39;, &#39;subheader&#39;, null, &#39;home_&amp;_garden&#39;]">Ladders</a>
                                                    </li>


                                                    <li>
                                                        <a href="http://www.consumerreports.org/cro/lead-test-kits/buying-guide.htm"
                                                           data-trackpagelink="[&#39;Lead_test_kits&#39;, &#39;subheader&#39;, null, &#39;home_&amp;_garden&#39;]">Lead
                                                            test kits</a></li>


                                                    <li><a href="http://www.consumerreports.org/cro/lightbulbs.htm"
                                                           data-trackpagelink="[&#39;Lightbulbs&#39;, &#39;subheader&#39;, null, &#39;home_&amp;_garden&#39;]">Lightbulbs</a>
                                                    </li>


                                                    <li><a href="http://www.consumerreports.org/cro/paints.htm"
                                                           data-trackpagelink="[&#39;Paints&#39;, &#39;subheader&#39;, null, &#39;home_&amp;_garden&#39;]">Paints</a>
                                                    </li>


                                                    <li><a href="http://www.consumerreports.org/cro/radon-test-kits.htm"
                                                           data-trackpagelink="[&#39;Radon_test_kits&#39;, &#39;subheader&#39;, null, &#39;home_&amp;_garden&#39;]">Radon
                                                        test kits</a></li>


                                                    <li>
                                                        <a href="http://www.consumerreports.org/cro/roofing/buying-guide.htm"
                                                           data-trackpagelink="[&#39;Roofing&#39;, &#39;subheader&#39;, null, &#39;home_&amp;_garden&#39;]">Roofing</a>
                                                    </li>


                                                    <li><a href="http://www.consumerreports.org/cro/siding.htm"
                                                           data-trackpagelink="[&#39;Siding&#39;, &#39;subheader&#39;, null, &#39;home_&amp;_garden&#39;]">Siding</a>
                                                    </li>


                                                    <li><a href="http://www.consumerreports.org/cro/sinks.htm"
                                                           data-trackpagelink="[&#39;Sinks&#39;, &#39;subheader&#39;, null, &#39;home_&amp;_garden&#39;]">Sinks</a>
                                                    </li>


                                                    <li><a href="http://www.consumerreports.org/cro/wood-stains.htm"
                                                           data-trackpagelink="[&#39;Wood_stains&#39;, &#39;subheader&#39;, null, &#39;home_&amp;_garden&#39;]">Wood
                                                        stains</a></li>

                                                </ul>
                                                <ul class="global-header-subNav">
                                                </ul>
                                                <br class="clear">
                                            </div>
                                        </li>

                                    </ul>
                                    <ul>
                                        <li>
                                            <span>Kitchen</span>
                                            <div>
                                                <ul class="global-header-subNav">


                                                    <li>
                                                        <a href="http://www.consumerreports.org/cro/kitchen-cabinets/buying-guide.htm"
                                                           data-trackpagelink="[&#39;Kitchen_cabinets&#39;, &#39;subheader&#39;, null, &#39;home_&amp;_garden&#39;]">Kitchen
                                                            cabinets</a></li>


                                                    <li>
                                                        <a href="http://www.consumerreports.org/cro/kitchen-cookware.htm"
                                                           data-trackpagelink="[&#39;Kitchen_cookware&#39;, &#39;subheader&#39;, null, &#39;home_&amp;_garden&#39;]">Kitchen
                                                            cookware</a></li>


                                                    <li><a href="http://www.consumerreports.org/cro/kitchen-knives.htm"
                                                           data-trackpagelink="[&#39;Kitchen_knives&#39;, &#39;subheader&#39;, null, &#39;home_&amp;_garden&#39;]">Kitchen
                                                        knives</a></li>


                                                    <li><a href="http://www.consumerreports.org/cro/water-filters.htm"
                                                           data-trackpagelink="[&#39;Water_filters&#39;, &#39;subheader&#39;, null, &#39;home_&amp;_garden&#39;]">Water
                                                        filters</a></li>

                                                </ul>
                                                <br class="clear">
                                            </div>
                                        </li>

                                    </ul>
                                    <ul>
                                        <li>
                                            <span>Lawn &amp; garden</span>
                                            <div>
                                                <ul class="global-header-subNav">


                                                    <li><a href="http://www.consumerreports.org/cro/gas-grills.htm"
                                                           data-trackpagelink="[&#39;Gas_grills&#39;, &#39;subheader&#39;, null, &#39;home_&amp;_garden&#39;]">Gas
                                                        grills</a></li>

                                                </ul>
                                                <br class="clear">
                                            </div>
                                        </li>

                                        <li>
                                            <span>Tools &amp; power equipment</span>
                                            <div>
                                                <ul class="global-header-subNav">


                                                    <li><a href="http://www.consumerreports.org/cro/chain-saws.htm"
                                                           data-trackpagelink="[&#39;Chain_saws&#39;, &#39;subheader&#39;, null, &#39;home_&amp;_garden&#39;]">Chain
                                                        saws</a></li>


                                                    <li>
                                                        <a href="http://www.consumerreports.org/cro/cordless-drills-tool-kits.htm"
                                                           data-trackpagelink="[&#39;Cordless_drills_&amp;_tool_kits&#39;, &#39;subheader&#39;, null, &#39;home_&amp;_garden&#39;]">Cordless
                                                            drills &amp; tool kits</a></li>


                                                    <li><a href="http://www.consumerreports.org/cro/lawn-mowers.htm"
                                                           data-trackpagelink="[&#39;Lawn_mowers_&amp;_tractors&#39;, &#39;subheader&#39;, null, &#39;home_&amp;_garden&#39;]">Lawn
                                                        mowers &amp; tractors</a></li>


                                                    <li><a href="http://www.consumerreports.org/cro/leaf-blowers.htm"
                                                           data-trackpagelink="[&#39;Leaf_blowers&#39;, &#39;subheader&#39;, null, &#39;home_&amp;_garden&#39;]">Leaf
                                                        blowers</a></li>


                                                    <li>
                                                        <a href="http://www.consumerreports.org/cro/pressure-washers.htm"
                                                           data-trackpagelink="[&#39;Pressure_washers&#39;, &#39;subheader&#39;, null, &#39;home_&amp;_garden&#39;]">Pressure
                                                            washers</a></li>


                                                    <li><a href="http://www.consumerreports.org/cro/snow-blowers.htm"
                                                           data-trackpagelink="[&#39;Snow_blowers&#39;, &#39;subheader&#39;, null, &#39;home_&amp;_garden&#39;]">Snow
                                                        blowers</a></li>


                                                    <li><a href="http://www.consumerreports.org/cro/string-trimmers.htm"
                                                           data-trackpagelink="[&#39;String_trimmers&#39;, &#39;subheader&#39;, null, &#39;home_&amp;_garden&#39;]">String
                                                        trimmers</a></li>

                                                </ul>
                                                <br class="clear">
                                            </div>
                                        </li>

                                    </ul>
                                    <ul>
                                    </ul>
                                    <br class="clear">


                                    <div class="global-header-view-all">
                                        <a href="http://www.consumerreports.org/cro/home-garden/index.htm">All Home
                                            &amp; garden</a>
                                        <a href="http://www.consumerreports.org/cro/news/index.htm#url=/bin/feedinfo.tag%3DproductsAndServices%3Ataxonomy|homeGarden.json&amp;title=Home%20%26%20garden">Home
                                            &amp; garden News</a>
                                    </div>
                                </div>
                            </li>
                        </ul>
                    </li>
                    <li class="global-header-cfa-item cfaId-28985">
                        <ul>
                            <li>
                                <a class="global-header-cfa-item-close" href="javascript:{}"><img
                                        src="/template/cr/close-btn.png"></a>

                                <div class="global-header-superCat">

                                    <ul>

                                        <li>
                                            <span>Babies &amp; toddlers</span>
                                            <div>
                                                <ul class="global-header-subNav">


                                                    <li>
                                                        <a href="http://www.consumerreports.org/cro/baby-activity-centers/buying-guide.htm"
                                                           data-trackpagelink="[&#39;Baby_activity_centers&#39;, &#39;subheader&#39;, null, &#39;babies_&amp;_kids&#39;]">Baby
                                                            activity centers</a></li>


                                                    <li>
                                                        <a href="http://www.consumerreports.org/cro/baby-bathtubs/buying-guide.htm"
                                                           data-trackpagelink="[&#39;Baby_bathtubs&#39;, &#39;subheader&#39;, null, &#39;babies_&amp;_kids&#39;]">Baby
                                                            bathtubs</a></li>


                                                    <li>
                                                        <a href="http://www.consumerreports.org/cro/baby-bottles/buying-guide.htm"
                                                           data-trackpagelink="[&#39;Baby_bottles&#39;, &#39;subheader&#39;, null, &#39;babies_&amp;_kids&#39;]">Baby
                                                            bottles</a></li>


                                                    <li>
                                                        <a href="http://www.consumerreports.org/cro/baby-carriers/buying-guide.htm"
                                                           data-trackpagelink="[&#39;Baby_carriers&#39;, &#39;subheader&#39;, null, &#39;babies_&amp;_kids&#39;]">Baby
                                                            carriers</a></li>


                                                    <li>
                                                        <a href="http://www.consumerreports.org/cro/baby-clothes/buying-guide.htm"
                                                           data-trackpagelink="[&#39;Baby_clothes&#39;, &#39;subheader&#39;, null, &#39;babies_&amp;_kids&#39;]">Baby
                                                            clothes</a></li>


                                                    <li>
                                                        <a href="http://www.consumerreports.org/cro/baby-food/buying-guide.htm"
                                                           data-trackpagelink="[&#39;Baby_food&#39;, &#39;subheader&#39;, null, &#39;babies_&amp;_kids&#39;]">Baby
                                                            food</a></li>


                                                    <li>
                                                        <a href="http://www.consumerreports.org/cro/baby-formula/buying-guide.htm"
                                                           data-trackpagelink="[&#39;Baby_formulas&#39;, &#39;subheader&#39;, null, &#39;babies_&amp;_kids&#39;]">Baby
                                                            formulas</a></li>


                                                    <li>
                                                        <a href="http://www.consumerreports.org/cro/baby-jumpers/buying-guide.htm"
                                                           data-trackpagelink="[&#39;Baby_jumpers&#39;, &#39;subheader&#39;, null, &#39;babies_&amp;_kids&#39;]">Baby
                                                            jumpers</a></li>


                                                    <li><a href="http://www.consumerreports.org/cro/baby-monitors.htm"
                                                           data-trackpagelink="[&#39;Baby_monitors&#39;, &#39;subheader&#39;, null, &#39;babies_&amp;_kids&#39;]">Baby
                                                        monitors</a></li>


                                                    <li>
                                                        <a href="http://www.consumerreports.org/cro/baby-swings/buying-guide.htm"
                                                           data-trackpagelink="[&#39;Baby_swings&#39;, &#39;subheader&#39;, null, &#39;babies_&amp;_kids&#39;]">Baby
                                                            swings</a></li>

                                                </ul>
                                                <ul class="global-header-subNav">


                                                    <li>
                                                        <a href="http://www.consumerreports.org/cro/baby-walkers/buying-guide.htm"
                                                           data-trackpagelink="[&#39;Baby_walkers&#39;, &#39;subheader&#39;, null, &#39;babies_&amp;_kids&#39;]">Baby
                                                            walkers</a></li>


                                                    <li>
                                                        <a href="http://www.consumerreports.org/cro/backpack-carriers/buying-guide.htm"
                                                           data-trackpagelink="[&#39;Backpack_carriers&#39;, &#39;subheader&#39;, null, &#39;babies_&amp;_kids&#39;]">Backpack
                                                            carriers</a></li>


                                                    <li>
                                                        <a href="http://www.consumerreports.org/cro/bassinets/buying-guide.htm"
                                                           data-trackpagelink="[&#39;Bassinets&#39;, &#39;subheader&#39;, null, &#39;babies_&amp;_kids&#39;]">Bassinets</a>
                                                    </li>


                                                    <li>
                                                        <a href="http://www.consumerreports.org/cro/bike-trailers/buying-guide.htm"
                                                           data-trackpagelink="[&#39;Bike_trailers&#39;, &#39;subheader&#39;, null, &#39;babies_&amp;_kids&#39;]">Bike
                                                            trailers</a></li>


                                                    <li>
                                                        <a href="http://www.consumerreports.org/cro/bouncer-seats/buying-guide.htm"
                                                           data-trackpagelink="[&#39;Bouncer_seats&#39;, &#39;subheader&#39;, null, &#39;babies_&amp;_kids&#39;]">Bouncer
                                                            seats</a></li>


                                                    <li>
                                                        <a href="http://www.consumerreports.org/cro/breast-pumps/buying-guide.htm"
                                                           data-trackpagelink="[&#39;Breast_pumps&#39;, &#39;subheader&#39;, null, &#39;babies_&amp;_kids&#39;]">Breast
                                                            pumps</a></li>


                                                    <li><a href="http://www.consumerreports.org/cro/car-seats.htm"
                                                           data-trackpagelink="[&#39;Car_seats&#39;, &#39;subheader&#39;, null, &#39;babies_&amp;_kids&#39;]">Car
                                                        seats</a></li>


                                                    <li>
                                                        <a href="http://www.consumerreports.org/cro/changing-tables/buying-guide.htm"
                                                           data-trackpagelink="[&#39;Changing_tables&#39;, &#39;subheader&#39;, null, &#39;babies_&amp;_kids&#39;]">Changing
                                                            tables</a></li>


                                                    <li><a href="http://www.consumerreports.org/cro/cribs.htm"
                                                           data-trackpagelink="[&#39;Cribs&#39;, &#39;subheader&#39;, null, &#39;babies_&amp;_kids&#39;]">Cribs</a>
                                                    </li>


                                                    <li>
                                                        <a href="http://www.consumerreports.org/cro/crib-bedding/buying-guide.htm"
                                                           data-trackpagelink="[&#39;Crib_bedding&#39;, &#39;subheader&#39;, null, &#39;babies_&amp;_kids&#39;]">Crib
                                                            bedding</a></li>

                                                </ul>
                                                <ul class="global-header-subNav">


                                                    <li>
                                                        <a href="http://www.consumerreports.org/cro/crib-mattresses/buying-guide.htm"
                                                           data-trackpagelink="[&#39;Crib_mattresses&#39;, &#39;subheader&#39;, null, &#39;babies_&amp;_kids&#39;]">Crib
                                                            mattresses</a></li>


                                                    <li>
                                                        <a href="http://www.consumerreports.org/cro/diapers/buying-guide.htm"
                                                           data-trackpagelink="[&#39;Diapers&#39;, &#39;subheader&#39;, null, &#39;babies_&amp;_kids&#39;]">Diapers</a>
                                                    </li>


                                                    <li>
                                                        <a href="http://www.consumerreports.org/cro/diaper-bags/buying-guide.htm"
                                                           data-trackpagelink="[&#39;Diaper_bags&#39;, &#39;subheader&#39;, null, &#39;babies_&amp;_kids&#39;]">Diaper
                                                            bags</a></li>


                                                    <li>
                                                        <a href="http://www.consumerreports.org/cro/diaper-pails/buying-guide.htm"
                                                           data-trackpagelink="[&#39;Diaper_pails&#39;, &#39;subheader&#39;, null, &#39;babies_&amp;_kids&#39;]">Diaper
                                                            pails</a></li>


                                                    <li>
                                                        <a href="http://www.consumerreports.org/cro/gliders-rocking-chairs/buying-guide.htm"
                                                           data-trackpagelink="[&#39;Gliders_&amp;_rocking_chairs&#39;, &#39;subheader&#39;, null, &#39;babies_&amp;_kids&#39;]">Gliders
                                                            &amp; rocking chairs</a></li>


                                                    <li><a href="http://www.consumerreports.org/cro/high-chairs.htm"
                                                           data-trackpagelink="[&#39;High_chairs&#39;, &#39;subheader&#39;, null, &#39;babies_&amp;_kids&#39;]">High
                                                        chairs</a></li>


                                                    <li>
                                                        <a href="http://www.consumerreports.org/cro/nursing-bras/buying-guide.htm"
                                                           data-trackpagelink="[&#39;Nursing_bras&#39;, &#39;subheader&#39;, null, &#39;babies_&amp;_kids&#39;]">Nursing
                                                            bras</a></li>


                                                    <li>
                                                        <a href="http://www.consumerreports.org/cro/pacifiers/buying-guide.htm"
                                                           data-trackpagelink="[&#39;Pacifiers&#39;, &#39;subheader&#39;, null, &#39;babies_&amp;_kids&#39;]">Pacifiers</a>
                                                    </li>


                                                    <li>
                                                        <a href="http://www.consumerreports.org/cro/play-yards/buying-guide.htm"
                                                           data-trackpagelink="[&#39;Play_yards&#39;, &#39;subheader&#39;, null, &#39;babies_&amp;_kids&#39;]">Play
                                                            yards</a></li>


                                                    <li>
                                                        <a href="http://www.consumerreports.org/cro/safety-gates/buying-guide.htm"
                                                           data-trackpagelink="[&#39;Safety_gates&#39;, &#39;subheader&#39;, null, &#39;babies_&amp;_kids&#39;]">Safety
                                                            gates</a></li>

                                                </ul>
                                                <ul class="global-header-subNav">


                                                    <li>
                                                        <a href="http://www.consumerreports.org/cro/shopping-cart-covers/buying-guide.htm"
                                                           data-trackpagelink="[&#39;Shopping_cart_covers&#39;, &#39;subheader&#39;, null, &#39;babies_&amp;_kids&#39;]">Shopping
                                                            cart covers</a></li>


                                                    <li><a href="http://www.consumerreports.org/cro/strollers.htm"
                                                           data-trackpagelink="[&#39;Strollers&#39;, &#39;subheader&#39;, null, &#39;babies_&amp;_kids&#39;]">Strollers</a>
                                                    </li>

                                                </ul>
                                                <br class="clear">
                                            </div>
                                        </li>

                                    </ul>
                                    <ul>
                                        <li>
                                            <span>Children's health</span>
                                            <div>
                                                <ul class="global-header-subNav">


                                                    <li><a href="http://www.consumerreports.org/cro/thermometers.htm"
                                                           data-trackpagelink="[&#39;Thermometers&#39;, &#39;subheader&#39;, null, &#39;babies_&amp;_kids&#39;]">Thermometers</a>
                                                    </li>

                                                </ul>
                                                <br class="clear">
                                            </div>
                                        </li>

                                        <li>
                                            <span>School-age kids</span>
                                            <div>
                                                <ul class="global-header-subNav">


                                                    <li>
                                                        <a href="http://www.consumerreports.org/cro/backpacks/buying-guide.htm"
                                                           data-trackpagelink="[&#39;Backpacks&#39;, &#39;subheader&#39;, null, &#39;babies_&amp;_kids&#39;]">Backpacks</a>
                                                    </li>

                                                </ul>
                                                <br class="clear">
                                            </div>
                                        </li>

                                    </ul>
                                    <br class="clear">


                                    <div class="global-header-view-all">
                                        <a href="http://www.consumerreports.org/cro/babies-kids/index.htm">All Babies
                                            &amp; kids</a>
                                        <a href="http://www.consumerreports.org/cro/news/index.htm#url=/bin/feedinfo.tag%3DproductsAndServices%3Ataxonomy|babiesKids.json&amp;title=Babies%20%26%20kids">Babies
                                            &amp; kids News</a>
                                    </div>
                                </div>
                            </li>
                        </ul>
                    </li>
                    <li class="global-header-cfa-item cfaId-34458">
                        <ul>
                            <li>
                                <a class="global-header-cfa-item-close" href="javascript:{}"><img
                                        src="/template/cr/close-btn.png"></a>

                                <div class="global-header-superCat">

                                    <ul>

                                        <li>
                                            <span>Banking &amp; credit</span>
                                            <div>
                                                <ul class="global-header-subNav">


                                                    <li>
                                                        <a href="http://www.consumerreports.org/cro/banks-credit-unions.htm"
                                                           data-trackpagelink="[&#39;Banks_&amp;_credit_unions&#39;, &#39;subheader&#39;, null, &#39;money&#39;]">Banks
                                                            &amp; credit unions</a></li>


                                                    <li>
                                                        <a href="http://www.consumerreports.org/cro/credit-cards/buying-guide.htm"
                                                           data-trackpagelink="[&#39;Credit_cards&#39;, &#39;subheader&#39;, null, &#39;money&#39;]">Credit
                                                            cards</a></li>


                                                    <li>
                                                        <a href="http://www.consumerreports.org/cro/prepaid-cards/buying-guide.htm"
                                                           data-trackpagelink="[&#39;Prepaid_cards&#39;, &#39;subheader&#39;, null, &#39;money&#39;]">Prepaid
                                                            cards</a></li>


                                                    <li>
                                                        <a href="http://www.consumerreports.org/cro/rewards-cards/buying-guide.htm"
                                                           data-trackpagelink="[&#39;Rewards_cards&#39;, &#39;subheader&#39;, null, &#39;money&#39;]">Rewards
                                                            cards</a></li>


                                                    <li>
                                                        <a href="http://www.consumerreports.org/cro/store-credit-cards/buying-guide.htm"
                                                           data-trackpagelink="[&#39;Store_credit_cards&#39;, &#39;subheader&#39;, null, &#39;money&#39;]">Store
                                                            credit cards</a></li>

                                                </ul>
                                                <br class="clear">
                                            </div>
                                        </li>

                                    </ul>
                                    <ul>
                                        <li>
                                            <span>Insurance</span>
                                            <div>
                                                <ul class="global-header-subNav">


                                                    <li><a href="http://www.consumerreports.org/cro/car-insurance.htm"
                                                           data-trackpagelink="[&#39;Car_insurance&#39;, &#39;subheader&#39;, null, &#39;money&#39;]">Car
                                                        insurance</a></li>


                                                    <li>
                                                        <a href="http://www.consumerreports.org/cro/homeowners-insurance.htm"
                                                           data-trackpagelink="[&#39;Homeowners_insurance&#39;, &#39;subheader&#39;, null, &#39;money&#39;]">Homeowners
                                                            insurance</a></li>

                                                </ul>
                                                <br class="clear">
                                            </div>
                                        </li>

                                        <li>
                                            <span>Personal finance</span>
                                            <div>
                                                <ul class="global-header-subNav">


                                                    <li>
                                                        <a href="http://www.consumerreports.org/cro/brokerage-services/buying-guide.htm"
                                                           data-trackpagelink="[&#39;Brokerage_services&#39;, &#39;subheader&#39;, null, &#39;money&#39;]">Brokerage
                                                            services</a></li>

                                                </ul>
                                                <br class="clear">
                                            </div>
                                        </li>

                                    </ul>
                                    <ul>
                                        <li>
                                            <span>Shopping</span>
                                            <div>
                                                <ul class="global-header-subNav">


                                                    <li>
                                                        <a href="http://www.consumerreports.org/cro/appliance-stores.htm"
                                                           data-trackpagelink="[&#39;Appliance_stores&#39;, &#39;subheader&#39;, null, &#39;money&#39;]">Appliance
                                                            stores</a></li>


                                                    <li>
                                                        <a href="http://www.consumerreports.org/cro/customer-service.htm"
                                                           data-trackpagelink="[&#39;Customer_service&#39;, &#39;subheader&#39;, null, &#39;money&#39;]">Customer
                                                            service</a></li>


                                                    <li>
                                                        <a href="http://www.consumerreports.org/cro/electronics-stores.htm"
                                                           data-trackpagelink="[&#39;Electronics_stores&#39;, &#39;subheader&#39;, null, &#39;money&#39;]">Electronics
                                                            stores</a></li>


                                                    <li>
                                                        <a href="http://www.consumerreports.org/cro/extended-warranties/buying-guide.htm"
                                                           data-trackpagelink="[&#39;Extended_warranties&#39;, &#39;subheader&#39;, null, &#39;money&#39;]">Extended
                                                            warranties</a></li>


                                                    <li><a href="http://www.consumerreports.org/cro/eyeglass-stores.htm"
                                                           data-trackpagelink="[&#39;Eyeglass_stores&#39;, &#39;subheader&#39;, null, &#39;money&#39;]">Eyeglass
                                                        stores</a></li>


                                                    <li><a href="http://www.consumerreports.org/cro/outlet-malls.htm"
                                                           data-trackpagelink="[&#39;Outlet_malls&#39;, &#39;subheader&#39;, null, &#39;money&#39;]">Outlet
                                                        malls</a></li>


                                                    <li><a href="http://www.consumerreports.org/cro/pharmacies.htm"
                                                           data-trackpagelink="[&#39;Pharmacies&#39;, &#39;subheader&#39;, null, &#39;money&#39;]">Pharmacies</a>
                                                    </li>


                                                    <li>
                                                        <a href="http://www.consumerreports.org/cro/retail-stores/buying-guide.htm"
                                                           data-trackpagelink="[&#39;Retail_stores&#39;, &#39;subheader&#39;, null, &#39;money&#39;]">Retail
                                                            stores</a></li>


                                                    <li>
                                                        <a href="http://www.consumerreports.org/cro/shopping-websites.htm"
                                                           data-trackpagelink="[&#39;Shopping_websites&#39;, &#39;subheader&#39;, null, &#39;money&#39;]">Shopping
                                                            websites</a></li>


                                                    <li>
                                                        <a href="http://www.consumerreports.org/cro/sporting-goods-stores.htm"
                                                           data-trackpagelink="[&#39;Sporting_goods_stores&#39;, &#39;subheader&#39;, null, &#39;money&#39;]">Sporting
                                                            goods stores</a></li>

                                                </ul>
                                                <ul class="global-header-subNav">


                                                    <li><a href="http://www.consumerreports.org/cro/supermarkets.htm"
                                                           data-trackpagelink="[&#39;Supermarkets&#39;, &#39;subheader&#39;, null, &#39;money&#39;]">Supermarkets</a>
                                                    </li>

                                                </ul>
                                                <br class="clear">
                                            </div>
                                        </li>

                                    </ul>
                                    <ul>
                                        <li>
                                            <span>Travel</span>
                                            <div>
                                                <ul class="global-header-subNav">


                                                    <li><a href="http://www.consumerreports.org/cro/airline-travel.htm"
                                                           data-trackpagelink="[&#39;Airline_travel&#39;, &#39;subheader&#39;, null, &#39;money&#39;]">Airline
                                                        travel</a></li>


                                                    <li>
                                                        <a href="http://www.consumerreports.org/cro/hotel-rooms/buying-guide.htm"
                                                           data-trackpagelink="[&#39;Hotel_rooms&#39;, &#39;subheader&#39;, null, &#39;money&#39;]">Hotel
                                                            rooms</a></li>


                                                    <li><a href="http://www.consumerreports.org/cro/luggage.htm"
                                                           data-trackpagelink="[&#39;Luggage&#39;, &#39;subheader&#39;, null, &#39;money&#39;]">Luggage</a>
                                                    </li>

                                                </ul>
                                                <br class="clear">
                                            </div>
                                        </li>

                                    </ul>
                                    <ul>
                                    </ul>
                                    <br class="clear">


                                    <div class="global-header-view-all">
                                        <a href="http://www.consumerreports.org/cro/money/index.htm">All Money</a>
                                        <a href="http://www.consumerreports.org/cro/news/index.htm#url=/bin/feedinfo.tag%3DproductsAndServices%3Ataxonomy|money.json&amp;title=Money">Money
                                            News</a>
                                    </div>
                                </div>
                            </li>
                        </ul>
                    </li>
                    <li class="global-header-cfa-item cfaId-36786">
                        <ul>
                            <li>
                                <a class="global-header-cfa-item-close" href="javascript:{}"><img
                                        src="/template/cr/close-btn.png"></a>

                                <div class="global-header-superCat">

                                    <ul class="global-header-alt-items col-lg-2">
                                        <li><a href="http://www.consumerreports.org/cro/health/index.htm">Health &amp;
                                            Wellness</a></li>
                                        <li>
                                            <a href="http://www.consumerreports.org/cro/health/vitamins-and-supplements/index.htm">Vitamins
                                                &amp; Supplements</a></li>
                                        <li>
                                            <a href="http://www.consumerreports.org/cro/health/drugs/index.htm">Drugs</a>
                                        </li>
                                        <li>
                                            <a href="http://www.consumerreports.org/cro/health/conditions-and-treatments/index.htm">Conditions
                                                &amp; Treatments</a></li>
                                        <li>
                                            <a href="http://www.consumerreports.org/cro/health/doctors-and-hospitals/index.htm">Doctors
                                                &amp; Hospitals</a></li>
                                        <li>
                                            <a href="http://www.consumerreports.org/cro/health/health-insurance/index.htm">Insurance</a>
                                        </li>
                                        <li>
                                            <a href="http://www.consumerreports.org/health/doctors-hospitals/hospital-ratings.htm">Find
                                                Hospital Ratings</a></li>
                                        <li>
                                            <a href="http://www.consumerreports.org/cro/health/prescription-drugs/best-buy-drugs/index.htm">Best
                                                Buy Drugs</a></li>
                                    </ul>


                                    <ul class="global-header-alt-subnav col-lg-8">
                                        <li class="col-lg-4">
                                            <ul>
                                                <li class="global-header-alt-subnav-titles">Exercise &amp; Fitness</li>
                                                <li><a href="http://www.consumerreports.org/cro/bike-helmets.htm">Bike
                                                    helmets</a></li>
                                                <li><a href="http://www.consumerreports.org/cro/diet-plans.htm">Diet
                                                    plans</a></li>
                                                <li><a href="http://www.consumerreports.org/cro/ellipticals.htm">Elliptical
                                                    exercisers</a></li>
                                                <li><a href="http://www.consumerreports.org/cro/exercise-bikes.htm">Exercise
                                                    bikes</a></li>
                                                <li><a href="http://www.consumerreports.org/cro/pedometers.htm">Pedometers</a>
                                                </li>
                                                <li><a href="http://www.consumerreports.org/cro/rowing-machines.htm">Rowing
                                                    machines</a></li>
                                                <li><a href="http://www.consumerreports.org/cro/treadmills.htm">Treadmills</a>
                                                </li>
                                            </ul>
                                            <ul>
                                                <li class="global-header-alt-subnav-titles">Home Medical Products</li>
                                                <li>
                                                    <a href="http://www.consumerreports.org/cro/blood-glucose-meters.htm">Blood
                                                        glucose meters</a></li>
                                                <li>
                                                    <a href="http://www.consumerreports.org/cro/blood-pressure-monitors.htm">Blood
                                                        pressure monitors</a></li>
                                                <li>
                                                    <a href="http://www.consumerreports.org/cro/heart-rate-monitors.htm">Heart-rate
                                                        monitors</a></li>
                                            </ul>
                                        </li>
                                        <li class="col-lg-4">
                                            <ul>
                                                <li class="global-header-alt-subnav-titles">Food &amp; Nutrition</li>
                                                <li><a href="http://www.consumerreports.org/cro/bacon.htm">Bacon</a>
                                                </li>
                                                <li><a href="http://www.consumerreports.org/cro/beer.htm">Beer</a></li>
                                                <li>
                                                    <a href="http://www.consumerreports.org/cro/breakfast-sandwiches.htm">Breakfast
                                                        sandwiches</a></li>
                                                <li><a href="http://www.consumerreports.org/cro/cereals.htm">Cereals</a>
                                                </li>
                                                <li><a href="http://www.consumerreports.org/cro/chocolates.htm">Chocolates</a>
                                                </li>
                                                <li><a href="http://www.consumerreports.org/cro/coffee.htm">Coffee</a>
                                                </li>
                                                <li>
                                                    <a href="http://www.consumerreports.org/cro/fast-food-restaurants.htm">Fast
                                                        food restaurants</a></li>
                                                <li><a href="http://www.consumerreports.org/cro/frozen-pizza.htm">Frozen
                                                    pizza</a></li>
                                                <li><a href="http://www.consumerreports.org/cro/frozen-waffles.htm">Frozen
                                                    waffles</a></li>
                                                <li>
                                                    <a href="http://www.consumerreports.org/cro/guacamole.htm">Guacamole</a>
                                                </li>
                                                <li><a href="http://www.consumerreports.org/cro/healthy-snacks.htm">Healthy
                                                    snacks</a></li>
                                                <li>
                                                    <a href="http://www.consumerreports.org/cro/ice-creams-frozen-yogurts.htm">Ice
                                                        creams &amp; frozen yogurts</a></li>

                                            </ul>
                                        </li>
                                        <li class="col-lg-4">
                                            <ul>
                                                <li class="global-header-alt-subnav-titles">&nbsp;</li>
                                                <li><a href="http://www.consumerreports.org/cro/popcorn.htm">Popcorn</a>
                                                </li>
                                                <li><a href="http://www.consumerreports.org/cro/salad-dressings.htm">Salad
                                                    dressings</a></li>
                                                <li><a href="http://www.consumerreports.org/cro/soups-broths.htm">Soups
                                                    &amp; broths</a></li>
                                                <li><a href="http://www.consumerreports.org/cro/spiral-hams.htm">Spiral
                                                    hams</a></li>
                                                <li><a href="http://www.consumerreports.org/cro/yogurt.htm">Yogurt</a>
                                                </li>
                                            </ul>
                                            <ul>
                                                <li class="global-header-alt-subnav-titles">Personal Care Products</li>
                                                <li><a href="http://www.consumerreports.org/cro/electric-razors.htm">Electric
                                                    razors</a></li>
                                                <li><a href="http://www.consumerreports.org/cro/facial-tissues.htm">Facial
                                                    tissues</a></li>
                                                <li><a href="http://www.consumerreports.org/cro/insect-repellent.htm">Insect
                                                    repellent</a></li>
                                                <li><a href="http://www.consumerreports.org/cro/scales.htm">Scales</a>
                                                </li>
                                                <li><a href="http://www.consumerreports.org/cro/sunscreens.htm">Sunscreens</a>
                                                </li>
                                            </ul>
                                        </li>
                                    </ul>

                                    <br class="clear">


                                    <div class="global-header-view-all">
                                        <a href="http://www.consumerreports.org/cro/health/index.htm">All Health</a>
                                        <a href="http://www.consumerreports.org/cro/news/index.htm#url=/bin/feedinfo.tag%3DproductsAndServices%3Ataxonomy|health.json&amp;title=Health">Health
                                            News</a>
                                    </div>
                                </div>
                            </li>
                        </ul>
                    </li>
                </ul>
            </div>
        </div>

        <script defer="defer">
            var cfaNewsURLs = {};

            cfaNewsURLs[28934] = {
                news: 'http://www.consumerreports.org/cro/news/index.htm#url=/bin/feedinfo.tag%3DproductsAndServices%3Ataxonomy|cars.json&title=Cars',
                mobileNews: 'http://www.consumerreports.org/cro/mobile/news/cars.htm',
                url: 'http://www.consumerreports.org/cro/cars/index.htm'
            };

            cfaNewsURLs[28949] = {
                news: 'http://www.consumerreports.org/cro/news/index.htm#url=/bin/feedinfo.tag%3DproductsAndServices%3Ataxonomy|electronicsComputers.json&title=Electronics%20%26%20computers',
                mobileNews: 'http://www.consumerreports.org/cro/mobile/news/electronicsComputers.htm',
                url: 'http://www.consumerreports.org/cro/electronics-computers/index.htm'
            };

            cfaNewsURLs[28967] = {
                news: 'http://www.consumerreports.org/cro/news/index.htm#url=/bin/feedinfo.tag%3DproductsAndServices%3Ataxonomy|appliances.json&title=Appliances',
                mobileNews: 'http://www.consumerreports.org/cro/mobile/news/appliances.htm',
                url: 'http://www.consumerreports.org/cro/appliances/index.htm'
            };

            cfaNewsURLs[28937] = {
                news: 'http://www.consumerreports.org/cro/news/index.htm#url=/bin/feedinfo.tag%3DproductsAndServices%3Ataxonomy|homeGarden.json&title=Home%20%26%20garden',
                mobileNews: 'http://www.consumerreports.org/cro/mobile/news/homeGarden.htm',
                url: 'http://www.consumerreports.org/cro/home-garden/index.htm'
            };

            cfaNewsURLs[28985] = {
                news: 'http://www.consumerreports.org/cro/news/index.htm#url=/bin/feedinfo.tag%3DproductsAndServices%3Ataxonomy|babiesKids.json&title=Babies%20%26%20kids',
                mobileNews: 'http://www.consumerreports.org/cro/mobile/news/babiesKids.htm',
                url: 'http://www.consumerreports.org/cro/babies-kids/index.htm'
            };

            cfaNewsURLs[34458] = {
                news: 'http://www.consumerreports.org/cro/news/index.htm#url=/bin/feedinfo.tag%3DproductsAndServices%3Ataxonomy|money.json&title=Money',
                mobileNews: 'http://www.consumerreports.org/cro/mobile/news/money.htm',
                url: 'http://www.consumerreports.org/cro/money/index.htm'
            };

            cfaNewsURLs[36786] = {
                news: 'http://www.consumerreports.org/cro/news/index.htm#url=/bin/feedinfo.tag%3DproductsAndServices%3Ataxonomy|health.json&title=Health',
                mobileNews: 'http://www.consumerreports.org/cro/mobile/news/health.htm',
                url: 'http://www.consumerreports.org/cro/health/index.htm'
            };
        </script>
        <div class="issuesMatter issues-matter-wrap hidden-sm hidden-md hidden-xs"><div class="issues-matter-items">
            <a class="global-header-issues-that-matters-close" href="javascript:;"><img data-original="/template/cr/close-btn.png" src="http://static3.consumerreportscdn.org/etc/designs/cr/images/common/close-btn.png" style="display: inline;"></a>

            <div class="issueItem-container">
                <a class="issue-item" href="https://consumersunion.org/surprise-medical-bills?source=CRO&amp;sub_source=top">
                    <img class="issueNav-img" data-original="http://static3.consumerreportscdn.org/etc/designs/cr/images/common/surprise_medical_bills.png" alt="End Surprise Medical Bills" src="http://static3.consumerreportscdn.org/etc/designs/cr/images/common/surprise_medical_bills.png" style="display: inline;"><br>

                    <div class="issue-item-title">End Surprise Medical Bills</div>
                    <!--<div class="issue-item-descr">If you don’t know how the ground beef you eat was raised, you may be putting yourself at higher…</div>-->
                </a>
            </div>
            <div class="issueItem-container">
                <a class="issue-item" href="https://consumersunion.org/end-robocalls?source=CRO&amp;sub_source=top">
                    <img class="issueNav-img" data-original="http://static4.consumerreportscdn.org/etc/designs/cr/images/common/robo_call.png" alt="End Robocalls" src="http://static4.consumerreportscdn.org/etc/designs/cr/images/common/robo_call.png" style="display: inline;"><br>

                    <div class="issue-item-title">End Robocalls</div>
                    <!--<div class="issue-item-descr">Phone scams cost consumers an estimated $350 million in financial losses annually…</div>-->
                </a>
            </div>
            <div class="issueItem-container">
                <a class="issue-item" href="http://www.consumerreports.org/cars/vw-diesel-emissions-recall">
                    <img class="issueNav-img" data-original="http://static4.consumerreportscdn.org/etc/designs/cr/images/common/volkswagen-dieselgate-emissions-recall.png" alt="Guide to the Volkswagen Emissions Recall" src="http://static4.consumerreportscdn.org/etc/designs/cr/images/common/volkswagen-dieselgate-emissions-recall.png" style="display: inline;"><br>

                    <div class="issue-item-title">Guide to the Volkswagen Emissions Recall</div>
                    <!--<div class="issue-item-descr">Our exclusive data analysis of more than 2 billion car insurance price quotes across every...</div>-->
                </a>
            </div>
            <div class="issueItem-container">
                <a class="issue-item" href="http://www.consumerreports.org/cro/health/GMO/index.htm">
                    <img class="issueNav-img" data-original="http://static4.consumerreportscdn.org/etc/designs/cr/images/common/gmo-labeling.png" alt="What You Need to Know About GMO Labeling" src="http://static4.consumerreportscdn.org/etc/designs/cr/images/common/gmo-labeling.png" style="display: inline;"><br>

                    <div class="issue-item-title">What You Need to Know About GMO Labeling</div>
                    <!--<div class="issue-item-descr">If you don’t know how the ground beef you eat was raised, you may be putting yourself at…</div>-->
                </a>
            </div>
            <div class="issueItem-container">
                <a class="issue-item" href="http://www.consumerreports.org/cro/health/the-rise-of-superbugs/index.htm">
                    <img class="issueNav-img" data-original="http://static2.consumerreportscdn.org/etc/designs/cr/images/common/rise-of-superbugs.png" alt="The Rise of Superbugs" src="http://static2.consumerreportscdn.org/etc/designs/cr/images/common/rise-of-superbugs.png" style="display: inline;"><br>

                    <div class="issue-item-title">The Rise of Superbugs</div>
                    <!--<div class="issue-item-descr">If you don’t know how the ground beef you eat was raised, you may be putting yourself at higher…</div>-->
                </a>
            </div>
        </div>
            <div class="clear"></div>
            <div class="all-issues-wrap">
                <a class="all-issues-link" href="http://www.consumerreports.org/cro/about-us/policy-and-action-product-food-safety-financial-health-reform/index.htm">View
                    All</a>
            </div></div>

        <div class="sign-in sign-in-overlay"></div>
        <div class="sign-in sign-in-dialog">
            <div class="close-sign-in-wrap">
                <div class="sign-in-title">Sign In</div>
                <a class="close-sign-in-btn" href="javascript:;"><img
                        src="/template/cr/close-btn.png"></a>
            </div>
            <div class="sign-in-form-wrap">
                <form id="sign-in-form" action="https://ec.consumerreports.org/ec/cro/login.htm" method="post"
                      name="login">
                    <div class="sign-in-input"><input placeholder="Username" name="userName" type="text"
                                                      oninput="inputSignIn()" value=""></div>
                    <div class="forgot-section-wrap">
                        <div class="sign-in-request-links">Hint: Your username could be your e-mail address</div>
                        <div class="clear"></div>
                    </div>


                    <div class="sign-in-input"><input placeholder="Password" name="password" type="password"
                                                      oninput="inputSignIn()"></div>
                    <div class="forgot-section-wrap">
                        <div class="sign-in-request-links">
                            Forgot <a
                                href="https://ec.consumerreports.org/ec/myaccount/forgot_username.htm">Username</a> or
                            <a href="https://ec.consumerreports.org/ec/myaccount/forgot_password.htm"> Password</a>?
                        </div>
                        <div class="remember-text">
                            <input type="checkbox" id="sign-in-check-box" name="setAutoLogin" checked="checked">
                            <label for="sign-in-check-box"><span></span>Remember Me</label>
                        </div>
                        <div class="clear"></div>
                    </div>
                </form>
            </div>
            <div class="sign-in-btn-wrap"><a
                    data-trackpagelink="[&#39;sign_in_complete_digital&#39;, &#39;header&#39;]">Sign In</a></div>
            <div class="subscribe-btn-wrap">
                Magazine Subscribers <a href="https://ec.consumerreports.org/ec/myaccount/main.htm"
                                        data-trackpagelink="[&#39;sign_in_complete_magazine&#39;, &#39;header&#39;]">Access
                My Account
                Here</a><br><br>
                Don't have an account? <a href="https://ec.consumerreports.org/ec/cro/order.htm">Subscribe now</a>

                <div class="help-wrap">
                    <div class="help-image"><img width="57" height="54"
                                                 src="/template/cr/question-mark.png">
                    </div>
                    <div class="help-title">Need help?</div>
                    <div class="help-text">If you need further assistance, please call Customer Service at
                        1-800-234-1645
                    </div>
                </div>
            </div>
        </div>

    </header>
</div>

<input type="hidden" name="globalheader-pageType" value="homepage">

<!-- Overriding global navigation domain for other domains:

<script>
    window.globalNavDomain = '//consumerreports.org';
    window.globalNavConfigurationApiUrl = '//api.consumerreports.org';
    window.globalNavConfigurationApiKey = 'um4aqdv9cm48xvvgsjtvhdta';
</script>

-->
<!-- Google Tag Manager -->
<script>
    $(document).ready(function () {
        var userID = getCookieField("userInfo", "ID");
        var productList = getCookieField("userInfo", "products");
        var segment;


        if ((userID != "") && (productList.indexOf("CRO") != -1))
            segment = "subscriber";
        else
            segment = "visitor";

        if (userID == 'undefined')
            userID = '';

        var pageName = 'CRO:HomePage';
        var contentType = '';
        var cfa = '';
        var variableMap = {};
        variableMap['userId'] = userID;
        variableMap['segment'] = segment;
        variableMap['pageType'] = false ? 'pay' : 'free';
        variableMap['siteLayout'] = false ? 'mobile' : 'desktop';
        if (pageName != 'undefined') {
            pageName = pageName.split(":");
            $.each(pageName, function (index) {
                if (index > 0) //skip first item
                    variableMap['siteSectionL' + (index + 1)] = pageName[index];
            });
        }
        if (contentType) {
            variableMap.ContentType = contentType;
        }
        if (cfa) {
            variableMap.cfa = cfa;
        }
        dataLayer.push(variableMap);
    })
</script>


<noscript>&lt;iframe src="www.googletagmanager.com/ns.html?id=GTM-N8GSGR" height="0" width="0"
    style="display:none;visibility:hidden"&gt;&lt;/iframe&gt;</noscript>
<script>
    (function (w, d, s, l, i) {
        w[l] = w[l] || [];
        w[l].push({'gtm.start': new Date().getTime(), event: 'gtm.js'});
        var f = d.getElementsByTagName(s)[0], j = d.createElement(s), dl = l != 'dataLayer' ? '&l=' + l : '';
        j.async = true;
        j.src = '//www.googletagmanager.com/gtm.js?id=' + i + dl;
        f.parentNode.insertBefore(j, f);
    })(window, document, 'script', 'dataLayer', 'GTM-N8GSGR');
</script>

<!-- End Google Tag Manager -->

<div id="header">

</div>

<div id="content">
    <div>
        <div class="center-pars">
            <div id="questionnaireWidget"><%--#SYS Widget Content--%></div>
        </div>
    </div>

</div>

<div class="parbase globalfooter">
    <div id="global-footer" class="globalfooter-wrapper">

        <div class="footer parsys">

            <div class="global-footer-wrapper">

                <div class="global-footer-cols row footer-navigation">

                    <div class="col-xs-4 col-sm-4 col-md-3 col-lg-3">
                        <div class="global-footer-col col-lg-12 footer-divider">
                            <div class="global-footer-col-name">Consumer Support</div>
                            <ul>
                                <li><a href="https://ec.consumerreports.org/ec/myaccount/main.htm" title="My Account"
                                       data-trackpagelink="[&#39;my_account&#39;, &#39;footer&#39;]">My Account</a></li>
                                <li>
                                    <a href="http://www.consumerreports.org/content/cro/en/about-us/customer-service-main"
                                       title="Customer Care"
                                       data-trackpagelink="[&#39;customer_care&#39;, &#39;footer&#39;]">Customer
                                        Care</a></li>
                                <li>
                                    <a href="http://www.consumerreports.org/cro/2013/02/report-a-problem-product/index.htm"
                                       title="Report a Safety Problem"
                                       data-trackpagelink="[&#39;report_a_safety_problem&#39;, &#39;footer&#39;]">Report
                                        a Safety Problem</a></li>
                                <li><a href="http://www.consumerreports.org/cro/careers/landing-page/index.htm"
                                       title="Career Opportunities"
                                       data-trackpagelink="[&#39;career_opportunities&#39;, &#39;footer&#39;]">Career
                                    Opportunities</a></li>
                            </ul>
                            <div class="global-footer-col">
                                <div class="about-us-container">
                                    <a href="http://www.consumerreports.org/cro/about-us/index.htm"
                                       data-trackpagelink="[&#39;about_us&#39;, &#39;footer&#39;]">About Us</a> <br>
                                    <a href="https://donateconsumers.org/ea-action/action?ea.client.id=1926&amp;ea.campaign.id=35640&amp;sourcecode=6021000121&amp;en_txn6=6021000121"
                                       data-trackpagelink="[&#39;donate&#39;, &#39;footer&#39;]">Donate</a>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="col-xs-8 col-sm-8 col-md-9 col-lg-9 global-footer-divider-left">
                        <div class="row">
                            <div class="global-footer-col col-xs-6 col-sm-6 col-md-3 col-lg-3">
                                <div class="global-footer-col-item">
                                    <div class="global-footer-col-name">Our Site</div>
                                    <div class="ourSiteList">
                                        <ul>
                                            <li>
                                                <a href="http://www.consumerreports.org/cro/a-to-z-index/products/index.htm"
                                                   title="A-Z Index"
                                                   data-trackpagelink="[&#39;a-z_index&#39;, &#39;footer&#39;]">A-Z
                                                    Index</a></li>
                                            <li><a href="http://www.consumerreports.org/cro/a/products/index.htm"
                                                   title="Product Index"
                                                   data-trackpagelink="[&#39;product_index&#39;, &#39;footer&#39;]">Product
                                                Index</a></li>
                                            <li><a href="http://www.consumerreports.org/cro/a/cars/index.htm"
                                                   title="Car Index"
                                                   data-trackpagelink="[&#39;car_index&#39;, &#39;footer&#39;]">Car
                                                Index</a></li>
                                            <li><a href="http://www.consumerreports.org/cro/video-hub/video.htm"
                                                   title="Video Index"
                                                   data-trackpagelink="[&#39;video_index&#39;, &#39;footer&#39;]">Video
                                                Index</a></li>
                                            <li><a href="http://web.consumerreports.org/features/index.html"
                                                   title="Site Features"
                                                   data-trackpagelink="[&#39;site_features&#39;, &#39;footer&#39;]">Site
                                                Features</a></li>
                                            <li><a href="http://consumerreports.org/cro/canada-extra/index.htm"
                                                   title="Canada Extra"
                                                   data-trackpagelink="[&#39;canada_extra&#39;, &#39;footer&#39;]">Canada
                                                Extra</a></li>
                                            <li><a href="http://espanol.consumerreports.org/" title="en Español"
                                                   data-trackpagelink="[&#39;en_español&#39;, &#39;footer&#39;]">en
                                                Español</a></li>
                                            <li><a href="http://pressroom.consumerreports.org/" title="Press Room"
                                                   data-trackpagelink="[&#39;press_room&#39;, &#39;footer&#39;]">Press
                                                Room</a></li>

                                        </ul>
                                    </div>
                                </div>
                            </div>

                            <div class="global-footer-col col-xs-6 col-sm-6 col-md-3 col-lg-3">
                                <div class="global-footer-col-name">Products &amp; Services</div>
                                <ul>
                                    <div class="global-footer-copy-wrap">Build &amp; Buy Car Buying Service</div>
                                    <li class="level-2"><a
                                            href="http://www.consumerreports.org/cro/car-prices-build-buy-service/index.htm?ep=A0">United
                                        States</a></li>
                                    <li class="level-2"><a
                                            href="http://consumerreports.unhaggle.com/cbp/?keycode=I5CBAAU">Canada</a>
                                    </li>
                                </ul>
                                <ul>
                                    <li>
                                        <a href="http://www.consumerreports.org/cro/2012/02/consumer-reports-bookstore/index.htm"
                                           title="Books &amp; Magazines"
                                           data-trackpagelink="[&#39;books_&amp;_magazines&#39;, &#39;footer&#39;]">Books
                                            &amp; Magazines</a></li>
                                    <li><a href="http://web.consumerreports.org/mobile/index.htm" title="Mobile Apps"
                                           data-trackpagelink="[&#39;mobile_apps&#39;, &#39;footer&#39;]">Mobile
                                        Apps</a></li>
                                    <li><a href="http://www.consumerreports.org/cro/car-prices/index.htm"
                                           title="National Car Prices"
                                           data-trackpagelink="[&#39;national_car_prices&#39;, &#39;footer&#39;]">National
                                        Car Prices</a></li>
                                </ul>
                            </div>

                            <div class="global-footer-col col-xs-5 col-sm-5 col-md-3 col-lg-3">
                                <div class="global-footer-col-item">
                                    <div class="global-footer-col-name">Our Network</div>
                                    <ul>
                                        <li><a href="http://www.consumersunion.org/" title="Consumers Union"
                                               data-trackpagelink="[&#39;consumers_union&#39;, &#39;footer&#39;]">Consumers
                                            Union</a></li>
                                        <li><a href="http://consumerist.com/" title="Consumerist"
                                               data-trackpagelink="[&#39;consumerist&#39;, &#39;footer&#39;]">Consumerist</a>
                                        </li>
                                        <li><a href="http://www.consumerhealthchoices.org/"
                                               title="Consumer Health Choices"
                                               data-trackpagelink="[&#39;consumer_health_choices&#39;, &#39;footer&#39;]">Consumer
                                            Health Choices</a></li>

                                    </ul>
                                </div>
                            </div>

                            <div class="global-footer-col col-xs-7 col-sm-7 col-md-3 col-lg-3">
                                <div class="global-footer-mag-wrapper">
                                    <a href="http://www.consumerreports.org/cro/magazine/index.htm">
                                        <img src="/template/cr/magazine-icon.svg"><br>
                                        <span>View Recent &amp; Past Issues</span>
                                    </a>
                                </div>
                                <br class="clear">
                            </div>
                        </div>
                        <div class="row hidden-xs hidden-sm">
                            <div class="col-xs-12 global-footer-social-margin-top">
                                <div class="col-xs-6">
                                    <div class="global-footer-social-wrapper">

                                        <a href="https://www.facebook.com/consumerreports" title="Facebook"
                                           target="_blank"
                                           data-trackpagelink="[&#39;facebook fanpage&#39;, &#39;footer&#39;, null, &#39;social&#39;]"
                                           class="facebook">Facebook</a>
                                        <a href="https://twitter.com/consumerreports" title="Twitter" target="_blank"
                                           data-trackpagelink="[&#39;twitter cr handle&#39;, &#39;footer&#39;, null, &#39;social&#39;]"
                                           class="twitter">Twitter</a>
                                        <a href="https://www.youtube.com/user/consumerreports" title="youtube"
                                           target="_blank"
                                           data-trackpagelink="[&#39;youtube cr page&#39;, &#39;footer&#39;, null, &#39;social&#39;]"
                                           class="youtube">Youtube</a>
                                        <a href="https://instagram.com/consumerreports" target="_blank"
                                           title="Instagram"
                                           data-trackpagelink="[&#39;instagram cr page&#39;, &#39;footer&#39;, null, &#39;social&#39;]"
                                           class="instagram">Instagram</a>
                                        <a class="more-icons" href="javascript:{}" title="More">More</a>
                                        <a class="hiddenIcons google"
                                           href="https://plus.google.com/u/0/106084461720436231771/posts"
                                           target="_blank" title="Google Plus"
                                           data-trackpagelink="[&#39;google plus cr page&#39;, &#39;footer&#39;, null, &#39;social&#39;]">Google
                                            Plus</a>
                                        <a class="hiddenIcons pinterest"
                                           href="https://www.pinterest.com/consumerreports/" target="_blank"
                                           title="Pinterest"
                                           data-trackpagelink="[&#39;pinterest cr page&#39;, &#39;footer&#39;, null, &#39;social&#39;]">Pinterest</a>
                                    </div>
                                </div>

                                <div class="col-xs-6 text-right global-footer-copy-wrap">
                                    <span class="copyright"> © 2006 - 2016 Consumer Reports </span><br>
                                    <a href="http://www.consumerreports.org/cro/customerservice/privacy-policy/highlights/index.htm"
                                       data-trackpagelink="[&#39;privacy_policy&#39;, &#39;footer&#39;]"
                                       target="_blank">Privacy Policy</a> <span class="delimiter">/</span>
                                    <a href="http://www.consumerreports.org/cro/2015/01/user-agreement/index.htm"
                                       data-trackpagelink="[&#39;user_agreement&#39;, &#39;footer&#39;]"
                                       target="_blank">User Agreement</a> <span class="delimiter">/</span>

                                    <a href="javascript:{}"
                                       data-trackpagelink="[&#39;ad_choices&#39;, &#39;footer&#39;]"
                                       class="TRUSTeWidget_Tab_link">Ad
                                        Choices</a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!--Start Tablet and mobile only social section-->
                <div class="container devices">
                    <div class="row hidden-md hidden-lg">
                        <div class="footer-mobile-account-btn">
                            <div class="global-footer-account-wrap">
                                <div class="global-footer-account-btn logOut">
                                    <a href="https://ec.consumerreports.org/ec/logout.htm" class="logout-button"
                                       data-trackpagelink="[&#39;logout&#39;, &#39;footer&#39;]">Logout</a>
                                </div>
                                <div class="global-footer-account-btn">
                                    <a href="https://ec.consumerreports.org/ec/cro/mob/login.htm" class="mobile-signIn"
                                       data-trackpagelink="[&#39;sign_in&#39;, &#39;footer&#39;]">Sign in</a>
                                </div>
                                <div class="global-footer-subscribe-btn">
                                    <a href="https://ec.consumerreports.org/ec/cro/mob/order.htm?INTKEY=I51MLT0"
                                       data-trackpagelink="[&#39;subscribe&#39;, &#39;footer&#39;]">Subscribe</a>
                                </div>
                            </div>
                        </div>

                        <div class="about-us-mobile">
                            <a class="about-link" href="http://www.consumerreports.org/cro/about-us/index.htm"
                               data-trackpagelink="[&#39;about_us&#39;, &#39;footer&#39;]">About Us</a>
                            <a class="donate-link"
                               href="https://donateconsumers.org/ea-action/action?ea.client.id=1926&amp;ea.campaign.id=35640&amp;sourcecode=6021000121&amp;en_txn6=6021000121"
                               data-trackpagelink="[&#39;donate&#39;, &#39;footer&#39;]">Donate</a>
                            <a class="help-link"
                               href="http://www.consumerreports.org/cro/2013/09/consumer-reports-help/index.htm"
                               data-trackpagelink="[&#39;help&#39;, &#39;footer&#39;]">Help</a>
                        </div>
                        <div class="col-xs-12 global-footer-social-margin-top">
                            <div class="col-xs-12 text-center">
                                <div class="global-footer-social-wrapper">

                                    <a href="https://www.facebook.com/consumerreports" title="Facebook" target="_blank"
                                       data-trackpagelink="[&#39;facebook fanpage&#39;, &#39;footer&#39;, null, &#39;social&#39;]"
                                       class="facebook">Facebook</a>
                                    <a href="https://twitter.com/consumerreports" title="Twitter" target="_blank"
                                       data-trackpagelink="[&#39;twitter cr handle&#39;, &#39;footer&#39;, null, &#39;social&#39;]"
                                       class="twitter">Twitter</a>
                                    <a href="https://www.youtube.com/user/consumerreports" title="youtube"
                                       target="_blank"
                                       data-trackpagelink="[&#39;youtube cr page&#39;, &#39;footer&#39;, null, &#39;social&#39;]"
                                       class="youtube">Youtube</a>
                                    <a href="https://instagram.com/consumerreports" target="_blank" title="Instagram"
                                       data-trackpagelink="[&#39;instagram cr page&#39;, &#39;footer&#39;, null, &#39;social&#39;]"
                                       class="instagram">Instagram</a>
                                    <a class="more-icons" href="javascript:{}" title="More">More</a>
                                    <a class="hiddenIcons google"
                                       href="https://plus.google.com/u/0/106084461720436231771/posts" target="_blank"
                                       title="Google Plus"
                                       data-trackpagelink="[&#39;google plus cr page&#39;, &#39;footer&#39;, null, &#39;social&#39;]">Google
                                        Plus</a>
                                    <a class="hiddenIcons pinterest" href="https://www.pinterest.com/consumerreports/"
                                       target="_blank" title="Pinterest"
                                       data-trackpagelink="[&#39;pinterest cr page&#39;, &#39;footer&#39;, null, &#39;social&#39;]">Pinterest</a>
                                </div>
                            </div>

                            <div class="col-xs-12 text-center global-footer-copy-wrap">
                                <span class="copyright"> © 2006 - 2016 Consumer Reports</span><br>
                                <a href="http://www.consumerreports.org/cro/customerservice/privacy-policy/highlights/index.htm"
                                   data-trackpagelink="[&#39;privacy_policy&#39;, &#39;footer&#39;]" target="_blank">Privacy
                                    Policy</a> <span class="delimiter">/</span>
                                <a href="http://www.consumerreports.org/cro/2015/01/user-agreement/index.htm"
                                   data-trackpagelink="[&#39;user_agreement&#39;, &#39;footer&#39;]" target="_blank">User
                                    Agreement</a> <span class="delimiter">/</span>

                                <a href="javascript:{}" data-trackpagelink="[&#39;ad_choices&#39;, &#39;footer&#39;]"
                                   class="TRUSTeWidget_Tab_link">Ad
                                    Choices</a>
                            </div>
                        </div>
                    </div>
                </div>
                <!--End Tablet and mobile only social section-->

            </div>
        </div>

    </div>
</div>


<script>
    var widthScreen = $(window).width();
    var experience = "Mobile";
    if (widthScreen > 1200) {
        experience = 'Desktop';
    } else if (widthScreen > 768) {
        experience = 'Tablet';
    }

    var s_account = "cuglobal"
    var initSScode = $.Deferred();

    initSScode.then(function () {

        s.channel = "cro";
        s.pageName = "CRO:HomePage";

        s.prop1 = "HomePage";

        s.prop9 = "free"

        s.prop15 = getCookieField("userInfo", "ID");
        var productList = getCookieField("userInfo", "products");


        if ((s.prop15 != "") && (productList.indexOf("CRO") != -1)) {
            s.prop16 = "subscriber";
        } else {
            s.prop16 = "visitor";
        }


        s.prop58 = s.eVar58 = experience;

        s.prop72 = 'homepage version2';
        if (s.prop72)
            s.eVar72 = s.prop72;

        if ('true' == jQuery.cookie('siteCatalystLoginSuccess')) {
            s.eVar8 = s.prop15
            s.events = 'event13';
            if (location.href.indexOf('consumerreports.org') != -1) {
                jQuery.cookie('siteCatalystLoginSuccess', null, {
                    expires: -1,
                    domain: '.consumerreports.org',
                    path: '/'
                });
            } else if (location.href.indexOf('consumer.org') != -1) {
                jQuery.cookie('siteCatalystLoginSuccess', null, {expires: -1, domain: '.consumer.org', path: '/'});
            }
        }


        /************* DO NOT ALTER ANYTHING BELOW THIS LINE ! **************/
        var s_code = s.t();
        if (s_code)document.write(s_code);


    });

    function getCookieField(c_name, f_name) {
        var c_value = getCookie(c_name);
        if (c_value == "")
            return c_value;
        c_value = unescape(c_value);
        var fields = c_value.split("&");

        for (i = 0; i < fields.length; i++) {
            var field = fields[i].split("=");
            if (field.length == 2) {
                if (field[0] == f_name) {
                    return field[1];
                }
            }
        }
        return "";
    }


    function getCookie(c_name) {
        var i, x, y, z, a;
        var cookies = document.cookie.split(";");

        for (i = 0; i < cookies.length; i++) {
            x = cookies[i].substr(0, cookies[i].indexOf("="));
            y = cookies[i].substr(cookies[i].indexOf("=") + 1);
            x = x.replace(/^\s+|\s+$/g, '');
            z = z + "--" + x + "--";

            if (x == c_name) {
                return decodeURI(y);
            }
        }
        return "";
    }


    function updateCompareEventToSC(products) {
        if (products != "") {
            var scvars = {
                'linkTrackVars': 'events,eVar6',
                'linkTrackEvents': 'event9',
                'eVar6': 'compare',
                'events': 'event9',
                'products': products
            };
            updateGenericEventToSC(scvars);
        }
    }

    function updateGenericEventToSC(scvars) {
        var s = s_gi("cuglobal");
        for (var key in scvars) {
            if (key != undefined && key != 'undefined') {
                s[key] = scvars[key];
            }
        }
        s.t();
    }


    function updateBuyingGuideEventsToSC(name) {
        var scvars = {
            "pageName": "CRO:HomePage" + ":" + name,
            "prop1": "HomePage",
            "prop2": name,
            'channel': 'cro',
            'prop9': 'free',
            'prop15': getCookieField("userInfo", "ID")
        };
        s.prop16 = s.prop16 || 'visitor';
        updateGenericEventToSC(scvars);
    }

</script>

<script type="text/javascript"
        src="/template/cr/globalNavigationStandalone.js"></script>
<script type="text/javascript" src="/template/cr/homepage.js"></script>
<script type="text/javascript" src="/template/cr/mobile-common.js"></script>

<div style="display: none; visibility: hidden;">
    <noscript></noscript>
</div>
<ul class="ui-autocomplete ui-front ui-menu ui-widget ui-widget-content" id="ui-id-1" tabindex="0"
    style="display: none;"></ul>
<span role="status" aria-live="assertive" aria-relevant="additions" class="ui-helper-hidden-accessible"></span>
<ul class="ui-autocomplete ui-front ui-menu ui-widget ui-widget-content" id="ui-id-2" tabindex="0"
    style="display: none;"></ul>
<span role="status" aria-live="assertive" aria-relevant="additions" class="ui-helper-hidden-accessible"></span>
</body>
</html>
