<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<% request.setCharacterEncoding(
        "UTF-8"); // the charset stuff should set this on the Tomcat Connectors config, but for backup... except it seems ineffective %>
<% final String context = request.getContextPath().replace("^\\s*https://", "http://").trim(); %>
<% final String realContext = request.getContextPath().trim(); %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>stori.es | <%=(String) request.getAttribute("title") %>
    </title>
    <meta name="robots" content="INDEX,FOLLOW">
    <meta name="pagePayState" content="free">
    <link rel="stylesheet" type="text/css" href="<%= realContext %>/template/cr/silver.css">
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
    <script type="text/javascript">
        var CKEDITOR_BASEPATH = '${pageContext.request.contextPath}/questionnaire/ckeditor/';
    </script>
    <link rel="stylesheet" type="text/css" href="<%= realContext %>/template/cr/tabStyle.css">
    <link rel="stylesheet" type="text/css" href="<%= realContext %>/template/cr/premium.css">
    <link rel="stylesheet" type="text/css" href="<%= realContext %>/template/cr/tabs.css">
    <link rel="stylesheet" type="text/css" href="<%= realContext %>/template/cr/right-rail-modules.css">
    <link rel="stylesheet" type="text/css" href="<%= realContext %>/template/cr/price-and-shop.css">
    <link rel="stylesheet" href="<%= realContext %>/template/cr/jquery.css" type="text/css">
    <link rel="stylesheet" href="<%= realContext %>/template/cr/jqzoom-carousel.css" type="text/css">
    <link href="https://netdna.bootstrapcdn.com/font-awesome/3.1.1/css/font-awesome.css" rel="stylesheet">
    <style type="text/css">
        .find-ratings-wrap {
            margin: 0 0 30px 0;
        }

        /* Default universal styles */
        body {
            margin: 0;
            font: normal 11px Arial, Helvetica, sans-serif;
            background: url(<%= realContext %>/template/cr/site_bkgrnd.jpg) repeat-x;
        }

        table {
            margin: 0;
            padding: 0;
            border: 0;
            border-collapse: collapse;
        }

        td {
            margin: 0;
            padding: 0;
            border: 0;
            vertical-align: top;
            font: normal 11px Arial, Helvetica, sans-serif;
        }

        img {
            border: 0;
        }

        p {
            font: normal 11px Arial, Helvetica, sans-serif;
        }

        /* Default toplevel site layout */
        #header {
            clear: both;
            width: 100%;
        }

        #content {
            width: 960px;
            clear: both;
            margin-left: auto;
            margin-right: auto;
            font: 11px Arial, Helvetica, sans-serif;
        }

        #content-header {
            clear: both;
            height: 58px;
            border-left: 1px solid #E6E6EE;
            border-right: 1px solid #E6E6EE;
            width: 918px;
            background: transparent url(<%= realContext %>/template/cr/content-header-backgroud.gif) repeat-x;
        }

        #content-navigation {
            width: 185px;
            float: left;
            margin-left: 10px;
        }

        #content-body {
            margin: 0 0 0 10px;
            width: 705px;
            float: left;
        }

        #content-ad {
            width: 0;
            float: left;
            display: none;
        }

        #content-footer {
            width: 960px;
            clear: both;
        }

        #footer {
            clear: both;
            margin-left: auto;
            margin-right: auto;
            width: 100%;
        }

        @media print {
            #content-navigation {
                display: none;
            }

            #content-ads {
                display: none;
            }

            #footer {
                display: none;
            }
        }

        #previewContainer {
            float: left;
            margin: 25px 0pt 40px 0px;
            padding: 0pt;
            width: 675px;
        }

        #previewContainer .previewHeader {
            background: transparent url(/cro/application-resources/modules/products/images/rr_headbkgnd.png) repeat-x scroll 0%;
            font-family: arial;
            font-size: 22px;
            font-style: normal;
            font-weight: bold;
            padding: 10px 5px 0px 10px;
        }

        #previewContainer .previewHeader img {
            float: right;
        }

        * html #previewContainer .previewHeader img {
            margin-top: -28px;
            padding-right: 5px;
        }

        * + html #previewContainer .previewHeader img {
            margin-top: -28px;
            padding-right: 5px;
        }

        #previewLeftnav {
            background: #EEEEEE none repeat scroll 0%;
            float: left;
            margin: 25px 0px 0px 0px;
            padding-bottom: 20px;
            width: 185px;
        }

        #ps_signin_box {
            background: #EEEEEE none repeat scroll 0%;
            margin: 0px;
            padding: 0pt;
            width: 185px;
        }

        #ps_signin_box dl {
            margin: 15px;
        }

        #cat_leftnav dl {
            font-family: Arial, Helvetica, sans-serif;
            font-size: 11px;
            font-weight: normal;
            line-height: 1.7em;
            list-style-image: none;
            list-style-position: outside;
            list-style-type: none;
            margin: 0pt 0pt 0pt 5px;
            padding: 0pt;
            text-decoration: none;
            width: 150px;
        }

        #ps_signin_box dl dt {
            color: #000;
            font-family: Arial, Helvetica, sans-serif;
            font-size: 14px;
            font-weight: bold;
            line-height: normal;
            margin: 0pt 0pt 2px;
            padding: 0pt;
            list-style-image: none;
            list-style-position: outside;
            list-style-type: none;
            text-decoration: none;
        }

        #ps_signin_box dl dd.login {
            color: #000000;
            font-family: Arial, Helvetica, sans-serif;
            font-size: 11px;
            font-weight: normal;
            line-height: normal;
            margin: 5px 5px 0pt 0pt;
            padding: 0pt;
        }

        #ps_signin_box dl dd.login input {
            width: 108px;
            font-stretch: normal;
        }

        #ps_signin_box dl dd.login a {
            color: #176fcc;
            font-size: 10px;
        }

        #ps_signin_box dl dd.remember {
            color: #000000;
            font-family: Arial, Helvetica, sans-serif;
            font-size: 11px;
            font-weight: normal;
            line-height: normal;
            margin: 2px 0pt 5px -4px;
            vertical-align: text-top;
        }

        #subhead_container, #subtabs_container {
            float: left;
        }

        #content {
            background: none repeat scroll 0 0 #FFFFFF !important;
        }

        #shop-top-mod #mid {
            width: 328px
        }

        #shop-top-mod {
            width: 940px;
        }
    </style>
    <style type="text/css">
        #t-header h1 {
            font-size: 26px;
            font-weight: bold;
            margin: 20px 0 15px;
            color: #000;
        }

        #t-header .pay-indicator {
            float: right;
            left: -6px;
            position: relative;
        }

        #t-header .heading {
            height: 58px;
            line-height: 1;
            margin: 0;
            padding: 0;
        }
    </style>
    <link rel="stylesheet" type="text/css" href="<%= realContext %>/template/cr/cro.css">
    <style type="text/css">
        .noAlert, .ifAlert, .truncDisplayName, .displayName, .ifCRMag, .noCRMag, .ifCROAnnual, .noCROAnnual, .ifSubscriptions, .noSubscriptions, .ifEBSCO {
            display: none;
        }
    </style>
    <link rel="stylesheet" type="text/css" href="<%= realContext %>/template/cr/headerfooter.css">
    <link rel="stylesheet" type="text/css" href="<%= realContext %>/template/cr/grid.css">
    <link rel="stylesheet" type="text/css" href="<%= realContext %>/template/cr/seo-header-dropdown.css">
    <link rel="stylesheet" type="text/css" href="<%= realContext %>/template/cr/style.css">
    <link rel="stylesheet" type="text/css" href="<%= realContext %>/template/cr/content-header.css">
    <link rel="stylesheet" type="text/css" href="<%= realContext %>/template/cr/shared.css">
    <link rel="stylesheet" type="text/css" href="<%= realContext %>/template/cr/loginpopup.css">
    <link rel="stylesheet" media="print" href="<%= realContext %>/template/cr/products-print.css">
    <style type="text/css">
    </style>
    <style type="text/css">
        #header-inner-container .cars-bottom-navigation {
            display: none;
        }

        #printTool .printText {
            margin-left: 6px;
        }

        #printTool .printText a:hover {
            text-decoration: underline;
        }

        @media print {
            body {
                background: none;
            }

            #header #header-inner-container .top-navigation {
                display: none;
            }

            #header #header-inner-container .header-body .search {
                display: none;
            }

            #header #header-inner-container .franchise-navigation {
                display: none;
            }

            #content-header {
                display: none;
            }

            #content #content-body #subhead_container #printTool {
                display: none;
            }

            #content-body {
                float: none;
            }

            #content-ad {
                display: none;
            }

            #content-footer {
                display: none;
            }
        }
    </style>
    <style type="text/css">
        /* breadcrumb css begins */
        #content #content-header .heading {
            width: 918px;
            height: 58px;
            margin: 0;
            padding: 0;
            position: absolute;
            overflow: hidden;
            line-height: 1.2em;
        }

        * html #content #content-header #breadcrumb_and_head {
            position: absolute;
            top: 147px;
        }

        #content #content-header .heading .breadcrumb {
            margin: 4px 0 0 9px;
            font: normal 11px Arial, Helvetica, sans-serif;
            color: #8594a6;
        }

        #content #content-header .heading .bug {
            margin: 5px 0 0 15px;
        }

        #content #content-header .heading .bug h1 {
            font: bold 24px Arial, Helvetica, sans-serif;
            color: #000;
            margin: 14px 0 0 4px;
            display: inline;
        }

        #content #content-header .heading .breadcrumb a:link {
            font: normal 11px Arial, Helvetica, sans-serif;
            color: #8594a6;
            text-decoration: none;
        }

        #content #content-header .heading .breadcrumb a:visited {
            font: normal 11px Arial, Helvetica, sans-serif;
            color: #8594a6;
            text-decoration: none;
        }

        #content #content-header .heading .breadcrumb a:hover {
            font: normal 11px Arial, Helvetica, sans-serif;
            color: #8594a6;
            text-decoration: underline;
        }

        #content #content-header .heading .breadcrumb a:active {
            font: normal 11px Arial, Helvetica, sans-serif;
            color: #8594a6;
            text-decoration: none;
        }

        /* breadcrumb css ends */
    </style>
    <style type="text/css">
        a:link {
            color: #176fcc;
            text-decoration: none;
            font-weight: normal;
        }

        a:visited {
            color: #176fcc;
            text-decoration: none;
            font-weight: normal;
        }

        a:hover {
            color: #176fcc;
            text-decoration: underline;
            font-weight: normal;
        }

        a:active {
            color: #176fcc;
            text-decoration: underline;
            font-weight: normal;
        }

        .go-to-or-print {
            width: 694px;
            margin: 20px 0 16px 0;
            padding: 0;
            float: left;
            clear: both;
            text-align: right;
            font: normal 11px Arial, Helvetica, sans-serif;
        }

        .go-to-or-print img {
            margin: 0 6px 0 12px;
            padding: 0;
            border: 0;
        }

        .go-to-or-print a:link.share {
            border-bottom: 1px dotted #176FCC;
            color: #176FCC;
            text-decoration: none;
        }

        .go-to-or-print a:visited.share {
            border-bottom: 1px dotted #176FCC;
            color: #176FCC;
            text-decoration: none;
        }

        .go-to-or-print a:hover.share {
            border-bottom: 1px dotted #176FCC;
            color: #176FCC;
            text-decoration: none;
        }

        .go-to-or-print a:active.share {
            border-bottom: 1px dotted #176FCC;
            border-style: inset;
            color: #176FCC;
            text-decoration: none;
        }

        .overview-product-model {
            width: 694px;
            margin: 0 0 30px 0;
            padding: 0;
            float: left;
            clear: both;
        }

        .overview-product-model a:link {
            color: #176fcc;
            text-decoration: none;
            font-weight: normal;
        }

        .overview-product-model a:visited {
            color: #176fcc;
            text-decoration: none;
            font-weight: normal;
        }

        .overview-product-model a:hover {
            color: #176fcc;
            text-decoration: underline;
            font-weight: normal;
        }

        .overview-product-model a:active {
            color: #176fcc;
            text-decoration: underline;
            font-weight: normal;
        }

        .overview-product-model .make-and-model {
            width: 694px;
            margin: 0 0 16px 0;
            padding: 0;
            float: left;
            clear: both;
            font: bold 18px Arial, Helvetica, sans-serif;
        }

        .overview-product-model .left-column {
            width: 240px;
            margin: 0 20px 0 0;
            padding: 0;
            float: left;
            clear: both;
        }

        .overview-product-model .left-column .recommended-wrap {
            height: 13px;
        }

        .overview-product-model .left-column .recommended {
            width: 92px;
            margin: 0;
            padding: 0;
            float: left;
            clear: both;
        }

        .overview-product-model .left-column .recommended img {
            margin: 0;
            padding: 0;
            border: none;
        }

        .overview-product-model .left-column .whats-this {
            margin: 0 0 0 12px;
            padding: 0;
            float: left;
            position: relative;
            top: -1px;
            width: 136px;
            font: normal 10px/13px Arial, Helvetica, sans-serif;
        }

        .overview-product-model .left-column .whats-this a:link {
            color: #176fcc;
            text-decoration: none;
            border-bottom: 1px dotted #176fcc;
            font-weight: normal;
        }

        .overview-product-model .left-column .whats-this a:visited {
            color: #176fcc;
            text-decoration: none;
            border-bottom: 1px dotted #176fcc;
            font-weight: normal;
        }

        .overview-product-model .left-column .whats-this a:hover {
            color: #176fcc;
            text-decoration: none;
            border-bottom: 1px dotted #176fcc;
            font-weight: normal;
        }

        .overview-product-model .left-column .whats-this a:active {
            color: #176fcc;
            text-decoration: none;
            border-bottom: 1px dotted #176fcc;
            font-weight: normal;
        }

        .overview-product-model .left-column .whats-this img {
            margin: 0 6px 0 0;
            padding: 0;
            float: left;
            position: relative;
            top: 2px;
        }

        .overview-product-model .left-column .car-image {
            width: 240px;
            margin: 0;
            padding: 0;
            float: left;
            clear: both;
            text-align: center;
        }

        .overview-product-model .left-column .photos-and-videos {
            width: 240px;
            margin: 0;
            padding: 0;
            float: left;
            clear: both;
            text-align: center;
            font: normal 11px Arial, Helvetica, sans-serif;
        }

        .overview-product-model .left-column .photos-and-videos img {
            margin: 0 6px 0 0;
            padding: 0;
            border: none;
        }

        .overview-product-model .left-column .photos-and-videos img.divider {
            margin: 0 12px;
            padding: 0;
            border: none;
        }

        .overview-product-model .middle-column {
            width: 224px;
            margin: 0 20px 0 0;
            padding: 0;
            float: left;
        }

        .overview-product-model .middle-column a:link {
            color: #176fcc;
            text-decoration: none;
        }

        .overview-product-model .middle-column a:visited {
            color: #176fcc;
            text-decoration: none;
        }

        .overview-product-model .middle-column a:hover {
            color: #176fcc;
            text-decoration: underline;
        }

        .overview-product-model .middle-column a:active {
            color: #176fcc;
            text-decoration: underline;
        }

        .overview-product-model .middle-column a.shiftRight {
            padding-left: 6px;
        }

        .overview-product-model .middle-column .price-range-head {
            width: 224px;
            margin: -3px 0 0 0;
            padding: 0;
            float: left;
            clear: both;
            font: bold 11px Arial, Helvetica, sans-serif;
        }

        .overview-product-model .middle-column .price-range-value {
            width: 224px;
            padding: 0;
            float: left;
            clear: both;
            font: bold 14px Arial, Helvetica, sans-serif;
        }

        .overview-product-model .middle-column .price-it-button {
            width: 224px;
            margin: 9px 0 0 0;
            padding: 0;
            float: left;
            clear: both;
        }

        .overview-product-model .middle-column .price-it-button img {
            margin: 0;
            padding: 0;
            border: none;
        }

        .overview-product-model .middle-column .summary {
            width: 224px;
            margin: 9px 0 8px 0;
            padding: 0;
            float: left;
            clear: both;
            font: normal 11px/14px Arial, Helvetica, sans-serif;
        }

        .overview-product-model .middle-column .divider {
            width: 224px;
            margin: 0 0 10px 0;
            padding: 0;
            float: left;
            clear: both;
            height: 3px;
        }

        .overview-product-model .middle-column .user-reviews {
            width: 224px;
            margin: 0;
            padding: 0;
            float: left;
            clear: both;
            font: normal 11px/14px Arial, Helvetica, sans-serif;
        }

        .overview-product-model .middle-column .user-reviews img {
            margin: 0 6px 0 0;
            padding: 0;
            border: none;
        }

        .overview-product-model .middle-column .see-reviews {
            width: 209px;
            margin: 0;
            padding: 0 0 0 15px;
            float: left;
            clear: both;
            font: normal 11px/14px Arial, Helvetica, sans-serif;
        }

        .overview-product-model .middle-column .see-reviews a:link {
            background: #FFEEC1 url(<%= realContext %>/template/cr/cro_cr_slug.gif) no-repeat right center;
            margin: 0;
            padding: 0 20px 0 0;
        }

        .overview-product-model .middle-column .see-reviews a:visited {
            background: #FFEEC1 url(<%= realContext %>/template/cr/cro_cr_slug.gif) no-repeat right center;
            margin: 0;
            padding: 0 20px 0 0;
        }

        .overview-product-model .middle-column .see-reviews a:hover {
            background: #FFEEC1 url(<%= realContext %>/template/cr/cro_cr_slug.gif) no-repeat right center;
            margin: 0;
            padding: 0 20px 0 0;
        }

        .overview-product-model .middle-column .see-reviews a:active {
            background: #FFEEC1 url(<%= realContext %>/template/cr/cro_cr_slug.gif) no-repeat right center;
            margin: 0;
            padding: 0 20px 0 0;
        }

        .overview-product-model .middle-column .write-a-review {
            width: 209px;
            margin: 0;
            padding: 0 0 0 15px;
            float: left;
            clear: both;
            font: normal 11px/14px Arial, Helvetica, sans-serif;
        }

        .overview-product-model .right-column {
            width: 190px;
            margin: 0;
            padding: 0;
            float: left;
        }

        .overview-product-model .right-column .type {
            width: 190px;
            margin: -3px 0 9px 0;
            padding: 0;
            float: left;
            font: bold 11px Arial, Helvetica, sans-serif;
        }

        .overview-product-model .right-column .type .value {
            font-weight: normal;
        }

        .overview-product-model .right-column .overall-score-box {
            width: 190px;
            height: 47px;
            margin: 0;
            padding: 0;
            float: left;
            clear: both;
            background: url(<%= realContext %>/template/cr/overall_score_top.gif) no-repeat;
        }

        .overview-product-model .right-column .overall-score-box-visitor-ad {
            width: 190px;
            margin: 0;
            padding: 0;
            float: left;
            clear: both;
        }

        .overview-product-model .right-column .overall-score-box-visitor-ad a:link {
            margin: 0;
            padding: 0;
            border: none;
        }

        .overview-product-model .right-column .overall-score-box-visitor-ad a:visited {
            margin: 0;
            padding: 0;
            border: none;
        }

        .overview-product-model .right-column .overall-score-box-visitor-ad a:hover {
            margin: 0;
            padding: 0;
            border: none;
        }

        .overview-product-model .right-column .overall-score-box-visitor-ad a:active {
            margin: 0;
            padding: 0;
            border: none;
        }

        .overview-product-model .right-column .overall-score-box-visitor-ad img {
            margin: 0;
            padding: 0;
            border: none;
        }

        .overview-product-model .right-column .overall-score-box .cr-overall-score {
            padding: 6px 0 0 12px;
            margin: 0;
            float: left;
            clear: both;
            width: 120px;
            font: bold 12px Arial, Helvetica, sans-serif;
            color: #091D32;
        }

        .overview-product-model .right-column .overall-score-box .overall-score-value {
            font: bold 32px Arial, Helvetica, sans-serif;
            color: #091D32;
            width: 55px;
            text-align: center;
            float: left;
        }

        .overview-product-model .right-column .overall-score-box .overall-score-value .out-of-100 {
            float: left;
            clear: both;
            margin: -5px 0 0 0;
            padding: 0;
            width: 50px;
            text-align: center;
        }

        .overview-product-model .right-column .overall-score-box .overall-score-value .out-of-100 img {
            border: none;
            margin: 0;
            padding: 0;
        }

        .overview-product-model .right-column .overall-score-box .overall-score-value .testing {
            float: left;
            clear: both;
            margin: 20px 0 0 2px;
            padding: 0;
            width: 50px;
            text-align: center;
        }

        .overview-product-model .right-column .overall-score-box .overall-score-value .testing img {
            border: none;
            margin: 0;
            padding: 0;
        }

        .overview-product-model .right-column .overall-score-box .cr-overall-score .info {
            margin: 3px 0 0 0;
            padding: 0;
            float: left;
            clear: both;
            font: normal 10px Arial, Helvetica, sans-serif;
            color: #006699;
            position: relative;
            top: -2px;
        }

        .overview-product-model .right-column .overall-score-box .cr-overall-score .info img {
            margin: 0 6px 0 0;
            padding: 0;
            border: none;
            float: left;
            position: relative;
            top: 4px;
        }

        .overview-product-model .right-column .overall-score-box .cr-overall-score .info a:link {
            color: #176fcc;
            text-decoration: none;
            border-bottom: 1px dotted #176FCC;
            font: normal 10px/16px Arial, Helvetica, sans-serif;
        }

        .overview-product-model .right-column .overall-score-box .cr-overall-score .info a:visited {
            color: #176fcc;
            text-decoration: none;
            border-bottom: 1px dotted #176fcc;
            font: normal 10px/16px Arial, Helvetica, sans-serif;
        }

        .overview-product-model .right-column .overall-score-box .cr-overall-score .info a:hover {
            color: #176fcc;
            text-decoration: none;
            border-bottom: 1px dotted #176fcc;
            font: normal 10px/16px Arial, Helvetica, sans-serif;
        }

        .overview-product-model .right-column .overall-score-box .cr-overall-score .info a:active {
            color: #176fcc;
            text-decoration: none;
            border-bottom: 1px dotted #176fcc;
            font: normal 10px/16px Arial, Helvetica, sans-serif;
        }

        .overview-product-model .right-column .non-tested-alternatives {
            width: 190px;
            height: 43px;
            margin: 0;
            padding: 0;
            float: left;
            clear: both;
            background: url(<%= realContext %>/template/cr/rprtcard_scoretop.gif) no-repeat;
        }

        .overview-product-model .right-column .non-tested-alternatives .see-alternatives {
            padding: 6px 0 6px 12px;
            margin: 0;
            float: left;
            clear: both;
            width: 175px;
            font: bold 12px Arial, Helvetica, sans-serif;
            color: #091D32;
        }

        .overview-product-model .right-column .non-tested-alternatives .alternatives-list {
            padding: 0;
            margin: 0;
            float: left;
            clear: both;
            width: 190px;
            height: 119px;
            background: #f8f8f8;
        }

        .overview-product-model .right-column .non-tested-alternatives .alternatives-list ul {
            list-style: none;
            margin: 12px 0;
            padding: 0;
        }

        .overview-product-model .right-column .non-tested-alternatives .alternatives-list ul li {
        <%= realContext %> / template / cr background : url(<%= realContext %> /template/cr/squarebullet.gif) no-repeat 0 7 px;
            font: normal 11px/14px Arial, Helvetica, sans-serif;
            margin: 0 0 3px 12px;
            padding: 0 0 0 10px;
        }

        .overview-product-model .right-column .report-card {
            margin: 0;
            padding: 0;
            width: 190px;
        <%= realContext %> / template / cr background : url(<%= realContext %> /template/cr/overall_score_bkg.gif) repeat-y;
            float: left;
            clear: both;
        }

        .overview-product-model .right-column .report-card .top-key {
            margin: 0;
            padding: 0;
            width: 190px;
            height: 19px;
            float: left;
            clear: both;
        }

        .overview-product-model .right-column .report-card .highest-rated {
            width: 187px;
            margin: 9px 0 6px 0;
            padding: 0;
            float: left;
            clear: both;
        }

        .overview-product-model .right-column .report-card .highest-rated .highest-rated-bar {
            margin: 0 3px 0 0;
            padding: 4px 5px;
            background: #828995;
            float: left;
            clear: both;
        }

        .overview-product-model .right-column .report-card .highest-rated .highest-rated-score {
            margin: 0;
            padding: 3px 0 0 0;
            font: normal 10px Arial, Helvetica, sans-serif;
            color: #445566;
            float: left;
        }

        .overview-product-model .right-column .report-card .this-model {
            width: 187px;
            margin: 3px 0 6px 0;
            padding: 0;
            float: left;
            clear: both;
        }

        .overview-product-model .right-column .report-card .this-model .this-model-bar {
            margin: 0 3px 0 0;
            padding: 4px 5px;
            background: #E80000;
            float: left;
            clear: both;
        }

        .overview-product-model .right-column .report-card .this-model .non-and-in-testing {
            margin: 0 3px 0 0;
            padding: 0;
            float: left;
            clear: both;
        }

        .overview-product-model .right-column .report-card .this-model .this-model-score {
            margin: 0;
            padding: 3px 0 0 0;
            font: bold 10px Arial, Helvetica, sans-serif;
            color: #000;
            float: left;
        }

        .overview-product-model .right-column .report-card .lowest-rated {
            width: 187px;
            margin: 3px 0 10px 0;
            padding: 0;
            float: left;
            clear: both;
        }

        .overview-product-model .right-column .report-card .lowest-rated .lowest-rated-bar {
            margin: 0 3px 0 0;
            padding: 4px 5px;
            background: #828995;
            float: left;
            clear: both;
        }

        .overview-product-model .right-column .report-card .lowest-rated .lowest-rated-score {
            margin: 0;
            padding: 3px 0 0 0;
            font: normal 10px Arial, Helvetica, sans-serif;
            color: #445566;
            float: left;
        }

        .overview-product-model .right-column .report-card .bottom-key {
            margin: 0;
            padding: 0;
            width: 190px;
            float: left;
            clear: both;
        }

        .chart-content {
            float: left;
            clear: both;
            width: 694px;
            margin: 0;
            padding: 0;
        }

        .chart-content table.ratings-report-card {
            width: 694px;
            float: left;
            clear: both;
            margin: 0 0 30px 0;
            padding: 0;
            border: 1px solid #C3D2E0;
            border-collapse: separate;
        }

        .chart-content table.ratings-report-card thead th {
            background: #eef1f6;
            font: bold 14px Arial, Helvetica, sans-serif;
            padding: 6px 12px;
            border-bottom: 1px solid #C3D2E0;
            text-align: left;
            width: 598px;
        }

        .chart-content table.ratings-report-card thead th .model-name {
            font: normal 11px Arial, Helvetica, sans-serif;
        }

        .chart-content table.ratings-report-card td {
            background: #fff;
            font: normal 11px/14px Arial, Helvetica, sans-serif;
            color: #000;
            padding: 6px 12px;
            text-align: left;
            border-top: 1px solid #C3D2E0;
            border-right: 1px solid #C3D2E0;
        }

        .chart-content table.ratings-report-card td img {
            margin: 0;
            padding: 0;
            border: none;
        }

        .chart-content table.ratings-report-card td.table-info {
            padding: 9px 12px 10px;
            border-right: none;
            border-top: none;
            border-bottom: 1px solid #C3D2E0;
            font: normal 12px/16px Arial, Helvetica, sans-serif;
        }

        .chart-content table.ratings-report-card td.table-info .description {
            padding: 0;
            margin: 0;
            font: normal 12px/16px Arial, Helvetica, sans-serif;
            width: 330px;
            float: left;
        }

        .chart-content table.ratings-report-card td.table-info a:link {
            color: #176fcc;
            text-decoration: none;
            font-weight: bold;
        }

        .chart-content table.ratings-report-card td.table-info a:visited {
            color: #176fcc;
            text-decoration: none;
            font-weight: bold;
        }

        .chart-content table.ratings-report-card td.table-info a:hover {
            color: #176fcc;
            text-decoration: underline;
            font-weight: bold;
        }

        .chart-content table.ratings-report-card td.table-info a:active {
            color: #176fcc;
            text-decoration: underline;
            font-weight: bold;
        }

        .chart-content table.ratings-report-card td.legend-cell {
            border-right: 0 none;
            border-top: 0 none;
            padding: 7px 12px 12px;
            text-align: right;
            vertical-align: middle;
        }

        .chart-content table.ratings-report-card td.table-info .legend {
            padding: 0;
            margin: 0;
            text-align: right;
            width: 336px;
            float: left;
        }

        .chart-content table.ratings-report-card td.ratings-column-1 {
            text-align: center;
        }

        .chart-content table.ratings-report-card td.ratings-column-2 {
            text-align: center;
        }

        .chart-content table.ratings-report-card td.ratings-column-3 {
            text-align: center;
            border-right: none;
        }

        .chart-content table.ratings-report-card tfoot td {
            background: #eef1f6;
            font: bold 11px Arial, Helvetica, sans-serif;
            color: #000;
            padding: 6px 12px 6px 12px;
            border-top: 1px solid #C3D2E0;
            border-bottom: none;
            border-right: none;
            text-align: left;
            width: 598px;
        }

        .chart-content table.ratings-report-card tfoot td a:link {
            background: #FFEEC1 url(<%= realContext %>/template/cr/cro_cr_slug.gif) no-repeat right center;
            margin: 0;
            padding: 0 20px 0 0;
            font: bold 11px Arial, Helvetica, sans-serif;
            color: #176fcc;
            text-decoration: none;
        }

        .chart-content table.ratings-report-card tfoot td a:visited {
            background: #FFEEC1 url(<%= realContext %>/template/cr/cro_cr_slug.gif) no-repeat right center;
            margin: 0;
            padding: 0 20px 0 0;
            font: bold 11px Arial, Helvetica, sans-serif;
            color: #176fcc;
            text-decoration: none;
        }

        .chart-content table.ratings-report-card tfoot td a:hover {
            background: #FFEEC1 url(<%= realContext %>/template/cr/cro_cr_slug.gif) no-repeat right center;
            margin: 0;
            padding: 0 20px 0 0;
            font: bold 11px Arial, Helvetica, sans-serif;
            color: #176fcc;
            text-decoration: underline;
        }

        .chart-content table.ratings-report-card tfoot td a:active {
            background: #FFEEC1 url(<%= realContext %>/template/cr/cro_cr_slug.gif) no-repeat right center;
            margin: 0;
            padding: 0 20px 0 0;
            font: bold 11px Arial, Helvetica, sans-serif;
            color: #176fcc;
            text-decoration: underline;
        }

        .chart-content table.review-recommended {
            width: 694px;
            float: left;
            clear: both;
            margin: 0 0 30px 0;
            padding: 0;
            border: 1px solid #C3D2E0;
            border-collapse: separate;
        }

        .chart-content table.review-recommended thead th {
            background: #eef1f6;
            font: bold 14px Arial, Helvetica, sans-serif;
            padding: 6px 12px;
            border-bottom: 1px solid #C3D2E0;
            text-align: left;
            width: 598px;
        }

        .chart-content table.review-recommended thead th .model-name {
            font: normal 11px Arial, Helvetica, sans-serif;
        }

        .chart-content table.review-recommended td {
            background: #fff;
            font: normal 12px/16px Arial, Helvetica, sans-serif;
            color: #000;
            padding: 12px;
            text-align: left;
        }

        .chart-content table.review-recommended td p {
            margin: 8px 0 0 0;
            font: normal 12px/16px Arial, Helvetica, sans-serif;
        }

        .chart-content table.review-recommended td p:first-child {
            margin: 0;
        }

        .chart-content table.review-recommended td p.crs-take {
            font-weight: bold;
            margin: 0 0 -8px 0;
        }

        .chart-content table.review-recommended td p .highs-lows {
            background: #dae1e5;
            font-weight: bold;
            margin-right: 3px;
        }

        .chart-content table.review-recommended td img {
            margin: 0 0 0 12px;
            padding: 0;
            border: none;
            float: right;
        }

        .chart-content table.review-recommended tfoot td {
            background: #eef1f6;
            font: bold 11px Arial, Helvetica, sans-serif;
            color: #000;
            padding: 6px 12px 6px 12px;
            border-top: 1px solid #C3D2E0;
            border-bottom: none;
            border-right: none;
            text-align: left;
            width: 598px;
        }

        .chart-content table.review-recommended tfoot td a:link {
            background: #FFEEC1 url(<%= realContext %>/template/cr/cro_cr_slug.gif) no-repeat right center;
            margin: 0;
            padding: 0 20px 0 0;
            font: bold 11px Arial, Helvetica, sans-serif;
            color: #176fcc;
            text-decoration: none;
        }

        .chart-content table.review-recommended tfoot td a:visited {
            background: #FFEEC1 url(<%= realContext %>/template/cr/cro_cr_slug.gif) no-repeat right center;
            margin: 0;
            padding: 0 20px 0 0;
            font: bold 11px Arial, Helvetica, sans-serif;
            color: #176fcc;
            text-decoration: none;
        }

        .chart-content table.review-recommended tfoot td a:hover {
            background: #FFEEC1 url(<%= realContext %>/template/cr/cro_cr_slug.gif) no-repeat right center;
            margin: 0;
            padding: 0 20px 0 0;
            font: bold 11px Arial, Helvetica, sans-serif;
            color: #176fcc;
            text-decoration: underline;
        }

        .chart-content table.review-recommended tfoot td a:active {
            background: #FFEEC1 url(<%= realContext %>/template/cr/cro_cr_slug.gif) no-repeat right center;
            margin: 0;
            padding: 0 20px 0 0;
            font: bold 11px Arial, Helvetica, sans-serif;
            color: #176fcc;
            text-decoration: underline;
        }

        .chart-content table.about-this-model {
            width: 694px;
            float: left;
            clear: both;
            margin: 0 0 30px 0;
            padding: 0;
            border: 1px solid #C3D2E0;
            border-collapse: separate;
        }

        .chart-content table.about-this-model thead th {
            background: #eef1f6;
            font: bold 14px Arial, Helvetica, sans-serif;
            padding: 6px 12px;
            border-bottom: 1px solid #C3D2E0;
            text-align: left;
            width: 598px;
        }

        .chart-content table.about-this-model thead th .model-name {
            font: normal 11px Arial, Helvetica, sans-serif;
        }

        .chart-content table.about-this-model td {
            background: #fff;
            font: normal 12px/16px Arial, Helvetica, sans-serif;
            color: #000;
            padding: 12px;
            text-align: left;
        }

        .chart-content table.about-this-model td p {
            margin: 8px 0 0 0;
            font: normal 12px/16px Arial, Helvetica, sans-serif;
        }

        .chart-content table.about-this-model td p:first-child {
            margin: 0;
        }

        .chart-content table.about-this-model td ul {
            margin: 0 0 8px 0;
            list-style: none;
            padding: 0;
        }

        .chart-content table.about-this-model td ul li {
            background: url(<%= realContext %>/template/cr/squarebullet-a.gif) no-repeat 0 7px;
            font: normal 12px/16px Arial, Helvetica, sans-serif;
            margin: 0;
            padding: 0 0 0 10px;
        }

        .chart-content table.about-this-model td img {
            margin: 0 0 12px 12px;
            padding: 0;
            border: none;
            float: right;
        }

        .chart-content table.about-this-model tfoot td {
            background: #eef1f6;
            font: bold 11px Arial, Helvetica, sans-serif;
            color: #000;
            padding: 6px 12px 6px 12px;
            border-top: 1px solid #C3D2E0;
            border-bottom: none;
            border-right: none;
            text-align: left;
            width: 598px;
        }

        .chart-content table.about-this-model tfoot td a:link {
            background: #FFEEC1 url(<%= realContext %>/template/cr/cro_cr_slug.gif) no-repeat right center;
            margin: 0;
            padding: 0 20px 0 0;
            font: bold 11px Arial, Helvetica, sans-serif;
            color: #176fcc;
            text-decoration: none;
        }

        .chart-content table.about-this-model tfoot td a:visited {
            background: #FFEEC1 url(<%= realContext %>/template/cr/cro_cr_slug.gif) no-repeat right center;
            margin: 0;
            padding: 0 20px 0 0;
            font: bold 11px Arial, Helvetica, sans-serif;
            color: #176fcc;
            text-decoration: none;
        }

        .chart-content table.about-this-model tfoot td a:hover {
            background: #FFEEC1 url(<%= realContext %>/template/cr/cro_cr_slug.gif) no-repeat right center;
            margin: 0;
            padding: 0 20px 0 0;
            font: bold 11px Arial, Helvetica, sans-serif;
            color: #176fcc;
            text-decoration: underline;
        }

        .chart-content table.about-this-model tfoot td a:active {
            background: #FFEEC1 url(<%= realContext %>/template/cr/cro_cr_slug.gif) no-repeat right center;
            margin: 0;
            padding: 0 20px 0 0;
            font: bold 11px Arial, Helvetica, sans-serif;
            color: #176fcc;
            text-decoration: underline;
        }

        .chart-content table.about-this-brand {
            width: 694px;
            float: left;
            clear: both;
            margin: 0 0 30px 0;
            padding: 0;
            border: 1px solid #C3D2E0;
            border-collapse: separate;
        }

        .chart-content table.about-this-brand thead th {
            background: #eef1f6;
            font: bold 14px Arial, Helvetica, sans-serif;
            padding: 6px 12px;
            border-bottom: 1px solid #C3D2E0;
            text-align: left;
            width: 598px;
        }

        .chart-content table.about-this-brand thead th .model-name {
            font: normal 11px Arial, Helvetica, sans-serif;
        }

        .chart-content table.about-this-brand td {
            background: #fff;
            font: normal 12px/16px Arial, Helvetica, sans-serif;
            color: #000;
            padding: 12px;
            text-align: left;
        }

        .chart-content table.about-this-brand td p {
            margin: 8px 0 0 0;
            font: normal 12px/16px Arial, Helvetica, sans-serif;
        }

        .chart-content table.about-this-brand td p:first-child {
            margin: 0;
        }

        .chart-content table.about-this-brand td img {
            margin: 0 0 12px 12px;
            padding: 0;
            border: none;
            float: right;
        }

        .chart-content table.about-this-brand tfoot td {
            background: #eef1f6;
            font: bold 11px Arial, Helvetica, sans-serif;
            color: #000;
            padding: 6px 12px 6px 12px;
            border-top: 1px solid #C3D2E0;
            border-bottom: none;
            border-right: none;
            text-align: left;
            width: 598px;
        }

        .chart-content table.about-this-brand tfoot td a:link {
            background: #FFEEC1 url(<%= realContext %>/template/cr/cro_cr_slug.gif) no-repeat right center;
            margin: 0;
            padding: 0 20px 0 0;
            font: bold 11px Arial, Helvetica, sans-serif;
            color: #176fcc;
            text-decoration: none;
        }

        .chart-content table.about-this-brand tfoot td a:visited {
            background: #FFEEC1 url(<%= realContext %>/template/cr/cro_cr_slug.gif) no-repeat right center;
            margin: 0;
            padding: 0 20px 0 0;
            font: bold 11px Arial, Helvetica, sans-serif;
            color: #176fcc;
            text-decoration: none;
        }

        .chart-content table.about-this-brand tfoot td a:hover {
            background: #FFEEC1 url(<%= realContext %>/template/cr/cro_cr_slug.gif) no-repeat right center;
            margin: 0;
            padding: 0 20px 0 0;
            font: bold 11px Arial, Helvetica, sans-serif;
            color: #176fcc;
            text-decoration: underline;
        }

        .chart-content table.about-this-brand tfoot td a:active {
            background: #FFEEC1 url(<%= realContext %>/template/cr/cro_cr_slug.gif) no-repeat right center;
            margin: 0;
            padding: 0 20px 0 0;
            font: bold 11px Arial, Helvetica, sans-serif;
            color: #176fcc;
            text-decoration: underline;
        }

        .chart-content table.specs-and-features {
            width: 694px;
            float: left;
            clear: both;
            margin: 0 0 30px 0;
            padding: 0;
            border: 1px solid #C3D2E0;
            border-collapse: separate;
            background: #f8f8f8;
        }

        .chart-content table.specs-and-features-full {
            border-bottom: none;
        }

        .chart-content table.specs-and-features thead th {
            background: #eef1f6;
            font: bold 14px Arial, Helvetica, sans-serif;
            padding: 6px 12px;
            border-bottom: 1px solid #C3D2E0;
            text-align: left;
            width: 598px;
        }

        .chart-content table.specs-and-features thead th .model-name {
            font: normal 11px Arial, Helvetica, sans-serif;
        }

        .chart-content table.specs-and-features td {
            background: #fff;
            font: normal 11px/14px Arial, Helvetica, sans-serif;
            color: #000;
            padding: 6px 12px 6px 14px;
            text-align: left;
            border-bottom: 1px solid #C3D2E0;
        }

        .chart-content table.specs-and-features td.spec-feature {
            font: bold 11px/14px Arial, Helvetica, sans-serif;
            width: 244px;
        }

        .chart-content table.specs-and-features td.value {
            font: normal 11px/14px Arial, Helvetica, sans-serif;
            width: 307px;
        }

        .chart-content table.specs-and-features td.shade {
            background: #f8f8f8;
        }

        .chart-content table.specs-and-features td p {
            margin: 8px 0 0 0;
        }

        .chart-content table.specs-and-features td p:first-child {
            margin: 0;
        }

        .chart-content table.specs-and-features td img {
            margin: 0 6px 0 0;
            padding: 0;
            float: left;
            position: relative;
            top: 2px;
        }

        .chart-content table.specs-and-features tfoot td {
            background: #eef1f6;
            font: bold 11px Arial, Helvetica, sans-serif;
            color: #000;
            padding: 6px 12px 6px 12px;
            border-top: none;
            border-bottom: none;
            border-right: none;
            text-align: left;
            width: 598px;
        }

        .chart-content table.specs-and-features tfoot td a:link {
            margin: 0;
            padding: 0;
            font: bold 11px Arial, Helvetica, sans-serif;
            color: #176fcc;
            text-decoration: none;
        }

        .chart-content table.specs-and-features tfoot td a:visited {
            margin: 0;
            padding: 0;
            font: bold 11px Arial, Helvetica, sans-serif;
            color: #176fcc;
            text-decoration: none;
        }

        .chart-content table.specs-and-features tfoot td a:hover {
            margin: 0;
            padding: 0;
            font: bold 11px Arial, Helvetica, sans-serif;
            color: #176fcc;
            text-decoration: underline;
        }

        .chart-content table.specs-and-features tfoot td a:active {
            margin: 0;
            padding: 0;
            font: bold 11px Arial, Helvetica, sans-serif;
            color: #176fcc;
            text-decoration: underline;
        }

        .chart-content .user-reviews {
            width: 692px;
            float: left;
            clear: both;
            margin: 0 0 30px 0;
            padding: 0;
            border: 1px solid #C3D2E0;
        }

        .chart-content .user-reviews .header {
            background: #eef1f6;
            font: bold 14px Arial, Helvetica, sans-serif;
            padding: 6px 12px;
            border-bottom: 1px solid #C3D2E0;
            text-align: left;
            width: 668px;
        }

        .chart-content .user-reviews .average-user-rating {
            padding: 12px;
            width: 668px;
            float: left;
            clear: both;
        }

        .chart-content .user-reviews .average-user-rating .ratings-box {
            background: url(<%= realContext %>/template/cr/user_review_sm_box.gif);
            width: 203px;
            height: 148px;
            margin: 0 0 12px 0;
            padding: 0;
        }

        .chart-content .user-reviews .average-user-rating .ratings-box .average-user-rating-header {
            float: left;
            clear: both;
            width: 203px;
            margin: 0;
            padding: 30px 0 6px 0;
            text-align: center;
            font: bold 14px Arial, Helvetica, sans-serif;
            color: #5A3909;
        }

        .chart-content .user-reviews .average-user-rating .ratings-box .ratings-stars {
            float: left;
            clear: both;
            width: 203px;
            height: 20px;
            margin: 0 0 12px 0;
            padding: 0;
            text-align: center;
            overflow: hidden;
        }

        .chart-content .user-reviews .average-user-rating .ratings-box .number-of-reviews {
            float: left;
            clear: both;
            width: 203px;
            margin: 0 0 12px 0;
            padding: 0;
            text-align: center;
            font: normal 11px Arial, Helvetica, sans-serif;
            color: #5A3909;
        }

        .chart-content .user-reviews .average-user-rating .ratings-box .write-a-review {
            float: left;
            clear: both;
            width: 203px;
            margin: 0;
            padding: 0;
            text-align: center;
            font: bold 11px Arial, Helvetica, sans-serif;
        }

        .chart-content .user-reviews .average-user-rating .ratings-box .write-a-review a:link {
            font-weight: bold;
        }

        .chart-content .user-reviews .average-user-rating .ratings-box .write-a-review a:visited {
            font-weight: bold;
        }

        .chart-content .user-reviews .average-user-rating .ratings-box .write-a-review a:hover {
            font-weight: bold;
        }

        .chart-content .user-reviews .average-user-rating .ratings-box .write-a-review a:active {
            font-weight: bold;
        }

        .chart-content .user-reviews .average-user-rating .display {
            float: left;
            clear: both;
            width: 668px;
            margin: 0 0 12px 0;
            padding: 0;
            font: normal 11px Arial, Helvetica, sans-serif;
        }

        .chart-content .user-reviews .average-user-rating .display a:link {
            background: #FFEEC1 url(<%= realContext %>/template/cr/cro_cr_slug.gif) no-repeat right center;
            margin: 0;
            padding: 0 20px 0 0;
            font: bold 11px Arial, Helvetica, sans-serif;
            color: #176fcc;
            text-decoration: none;
        }

        .chart-content .user-reviews .average-user-rating .display a:visited {
            background: #FFEEC1 url(<%= realContext %>/template/cr/cro_cr_slug.gif) no-repeat right center;
            margin: 0;
            padding: 0 20px 0 0;
            font: bold 11px Arial, Helvetica, sans-serif;
            color: #176fcc;
            text-decoration: none;
        }

        .chart-content .user-reviews .average-user-rating .display a:hover {
            background: #FFEEC1 url(<%= realContext %>/template/cr/cro_cr_slug.gif) no-repeat right center;
            margin: 0;
            padding: 0 20px 0 0;
            font: bold 11px Arial, Helvetica, sans-serif;
            color: #176fcc;
            text-decoration: underline;
        }

        .chart-content .user-reviews .average-user-rating .display a:active {
            background: #FFEEC1 url(<%= realContext %>/template/cr/cro_cr_slug.gif) no-repeat right center;
            margin: 0;
            padding: 0 20px 0 0;
            font: bold 11px Arial, Helvetica, sans-serif;
            color: #176fcc;
            text-decoration: underline;
        }

        .chart-content .user-reviews .divider {
            float: left;
            clear: both;
            height: 1px;
            width: 668px;
            line-height: 1px;
            background: #bfcad3;
            margin: 0 12px 20px 12px;
            padding: 0;
        }

        .chart-content .user-reviews .prReviewWrap {
            float: left;
            clear: both;
            width: 668px;
            margin: 0 12px 20px 12px;
            padding: 0;
            border-bottom: 1px solid #bfcad3;
        }

        .chart-content .user-reviews .prReviewWrap div.prStars {
            background-repeat: no-repeat;
            float: left;
            margin: 0 0.25em 0 0;
            padding: 0;
            position: relative;
        }

        .chart-content .user-reviews .prReviewWrap div.prStars.prStarsSmall {
            width: 85px;
            height: 15px;
            line-height: 15px;
        }

        .chart-content .user-reviews .prReviewWrap .prReviewRatingHeadline {
            color: #213952;
            font-size: 12px;
            display: inline;
            font-weight: bold;
            margin-left: 6px;
            float: left;
            clear: both;
        }

        .chart-content .user-reviews .prReviewWrap div.prStars .helpful-review {
            float: left;
            clear: both;
            width: 668px;
            margin: 0 0 20px 0;
            padding: 0;
            font: normal 11px Arial, Helvetica, sans-serif;
            color: #666;
        }

        .chart-content .user-reviews .prReviewWrap div.prStars .helpful-review .stars {
            float: left;
            width: 83px;
            margin: 0 6px 0 0;
            padding: 0;
            height: 15px;
            overflow: hidden;
            float: left;
            position: relative;
            top: -1px;
        }

        .chart-content .user-reviews .footer {
            background: #eef1f6;
            float: left;
            clear: both;
            font: bold 11px Arial, Helvetica, sans-serif;
            color: #000;
            padding: 6px 12px 6px 12px;
            border-top: 1px solid #C3D2E0;
            border-bottom: none;
            border-right: none;
            text-align: left;
            width: 668px;
        }

        .chart-content .user-reviews .footer a:link {
            background: #FFEEC1 url(<%= realContext %>/template/cr/cro_cr_slug.gif) no-repeat right center;
            margin: 0;
            padding: 0 20px 0 0;
            font: bold 11px Arial, Helvetica, sans-serif;
            color: #176fcc;
            text-decoration: none;
        }

        .chart-content .user-reviews .footer a:visited {
            background: #FFEEC1 url(<%= realContext %>/template/cr/cro_cr_slug.gif) no-repeat right center;
            margin: 0;
            padding: 0 20px 0 0;
            font: bold 11px Arial, Helvetica, sans-serif;
            color: #176fcc;
            text-decoration: none;
        }

        .chart-content .user-reviews .footer a:hover {
            background: #FFEEC1 url(<%= realContext %>/template/cr/cro_cr_slug.gif) no-repeat right center;
            margin: 0;
            padding: 0 20px 0 0;
            font: bold 11px Arial, Helvetica, sans-serif;
            color: #176fcc;
            text-decoration: underline;
        }

        .chart-content .user-reviews .footer a:active {
            background: #FFEEC1 url(<%= realContext %>/template/cr/cro_cr_slug.gif) no-repeat right center;
            margin: 0;
            padding: 0 20px 0 0;
            font: bold 11px Arial, Helvetica, sans-serif;
            color: #176fcc;
            text-decoration: underline;
        }

        .chart-content table.about-price-and-shop {
            width: 694px;
            float: left;
            clear: both;
            margin: 0 0 30px 0;
            padding: 0;
            border: 1px solid #C3D2E0;
            border-collapse: separate;
        }

        .chart-content table.about-price-and-shop thead th {
            background: #eef1f6;
            font: bold 14px Arial, Helvetica, sans-serif;
            padding: 6px 12px;
            border-bottom: 1px solid #C3D2E0;
            text-align: left;
            width: 598px;
        }

        .chart-content table.about-price-and-shop thead th .model-name {
            font: normal 11px Arial, Helvetica, sans-serif;
        }

        .chart-content table.about-price-and-shop td {
            background: #fff;
            font: normal 12px/16px Arial, Helvetica, sans-serif;
            color: #000;
            padding: 12px;
            text-align: left;
        }

        .chart-content table.about-price-and-shop td p {
            margin: 8px 0 0 0;
            font: normal 12px/16px Arial, Helvetica, sans-serif;
        }

        .chart-content table.about-price-and-shop td p:first-child {
            margin: 0;
        }

        /* batch 2 stuff */
        .chart-content table.brand-reliability {
            width: 694px;
            float: left;
            clear: both;
            margin: 0 0 12px 0;
            padding: 0;
            border: 1px solid #C3D2E0;
            background: #f8f8f8;
        }

        .chart-content table.brand-reliability th.brand {
            background: #ECF3FB;
            font: bold 11px/14px Arial, Helvetica, sans-serif;
            color: #000;
            padding: 6px 12px 6px 14px;
            text-align: left;
            border-bottom: 1px solid #C3D2E0;
            border-right: 1px solid #C3D2E0;
        }

        .chart-content table.brand-reliability th.rating {
            background: #ECF3FB;
            font: bold 11px/14px Arial, Helvetica, sans-serif;
            color: #000;
            padding: 6px 12px 6px 14px;
            text-align: left;
            border-bottom: 1px solid #C3D2E0;
        }

        .chart-content table.brand-reliability thead th {
            background: #eef1f6;
            font: bold 14px Arial, Helvetica, sans-serif;
            padding: 6px 12px;
            border-bottom: 1px solid #C3D2E0;
            text-align: left;
        }

        .chart-content table.brand-reliability td {
            background: #fff;
            font: normal 11px/14px Arial, Helvetica, sans-serif;
            color: #000;
            padding: 12px;
            text-align: left;
            border-bottom: 1px solid #C3D2E0;
        }

        .chart-content table.brand-reliability td.brand {
            font: bold 11px/14px Arial, Helvetica, sans-serif;
            width: 315px;
            border-right: 1px solid #C3D2E0;
            background: #ECF3FB;
            padding-right: 220px;
        }

        .chart-content table.brand-reliability td.brandnohover {
            font: bold 11px/14px Arial, Helvetica, sans-serif;
            width: 315px;
            border-right: 1px solid #C3D2E0;
            background: #ECF3FB;
        }

        .chart-content table.brand-reliability td.brand-head {
            background: #fff;
            width: 0;
            /*padding: 0 0 0 310px;*/
        }

        .chart-content table.brand-reliability td.rating-head {
            margin: 0;
            padding: 0;
            width: 378px;
        }

        .chart-content table.brand-reliability td.rating {
            font: bold 11px/14px Arial, Helvetica, sans-serif;
            background: #ECF3FB url(<%= realContext %>/template/cr/reliability_bkg_off.gif) repeat-y;
        }

        .chart-content table.brand-reliability tr:hover td.brand {
            background: #f5f9fd;
        }

        .chart-content table.brand-reliability tr:hover td.rating {
            background: #f5f9fd url(<%= realContext %>/template/cr/reliability_bkg_on.gif) repeat-y;
        }

        .chart-content table.brand-reliability td.rating .ratings-bar {
            background: #526670;
            margin: 0;
            padding: 0;
            float: left;
            clear: both;
        }

        .chart-content table.brand-reliability td.rating .ratings-bar-light {
            background: #94b5c2;
        }

        .chart-content table.brand-reliability td.rating .ratings-bar .value {
            float: right;
            /*width: 25px;*/
            text-align: right;
            color: #fff;
            font: bold 11px Arial, Helvetica, sans-serif;
            margin: 0;
            /*padding: 0 6px 0 0;*/
        }

        .chart-content table.brand-reliability td p {
            margin: 8px 0 0 0;
        }

        .chart-content table.brand-reliability td p:first-child {
            margin: 0;
        }

        .chart-content table.brand-reliability tfoot td {
            font: bold 11px Arial, Helvetica, sans-serif;
            color: #000;
            border-top: none;
            border-bottom: none;
            border-right: none;
            text-align: left;
        }

        .chart-content table.brand-reliability tfoot td img.key1 {
            margin: 0 6px 0 0;
            padding: 0;
            border: none;
        }

        .chart-content table.brand-reliability tfoot td img.key2 {
            margin: 0 6px 0 12px;
            padding: 0;
            border: none;
        }

        .brand-reliability-table-footer {
            float: left;
            clear: both;
            width: 694px;
            margin: 0 0 20px 0;
            padding: 0;
            font: normal 10px Arial, Helvetica, sans-serif;
            color: #999;
        }

        .chart-content table.first-look {
            width: 694px;
            float: left;
            clear: both;
            margin: 0 0 30px 0;
            padding: 0;
            border: 1px solid #C3D2E0;
            border-collapse: separate;
        }

        .chart-content table.first-look thead th {
            background: #eef1f6;
            font: bold 14px Arial, Helvetica, sans-serif;
            padding: 6px 12px;
            border-bottom: 1px solid #C3D2E0;
            text-align: left;
            width: 598px;
        }

        .chart-content table.first-look thead th .model-name {
            font: normal 11px Arial, Helvetica, sans-serif;
        }

        .chart-content table.first-look td {
            background: #fff;
            font: normal 12px/16px Arial, Helvetica, sans-serif;
            color: #000;
            padding: 12px;
            text-align: left;
        }

        .chart-content table.first-look td p {
            margin: 8px 0 0 0;
            font: normal 12px/16px Arial, Helvetica, sans-serif;
        }

        .chart-content table.first-look td p:first-child {
            margin: 0;
        }

        .mboxDefault {
            border: none;
            margin: 0;
            padding: 0;
            overflow: hidden;
        }

        .mboxDefault a:link {
            border: none;
            margin: 0;
            padding: 0;
        }

        .mboxDefault a:visited {
            border: none;
            margin: 0;
            padding: 0;
        }

        .mboxDefault a:hover {
            border: none;
            margin: 0;
            padding: 0;
        }

        .mboxDefault a:active {
            border: none;
            margin: 0;
            padding: 0;
        }

        .ad-slot-1 {
            float: left;
            clear: both;
            margin: 0 0 30px 0;
            padding: 0;
            width: 694px;
        }

        .ad-slot-2 {
            float: left;
            clear: both;
            margin: 0 0 30px 0;
            padding: 0;
            text-align: right;
            width: 694px;
        }

        .ad-slot-3 {
            float: left;
            clear: both;
            width: 180px;
            margin: 0 0 20px 0;
            padding: 0;
        }

        .ad-slot-4 {
            float: left;
            clear: both;
            width: 180px;
            margin: 0 0 20px 0;
            padding: 0;
        }

        #content-footer {
            clear: both;
            float: left;
            margin: 0 0 0 10px;
            padding: 0 0 20px;
            width: 694px;
        }

        * html #content-footer {
            margin-left: 5px;
        }

        .shopping-see-also {
            width: 694px;
            margin: 0 0 30px 0;
            padding: 0 0 20px 0;
            overflow: auto;
            border-top: 1px solid #bfcad3;
            border-bottom: 1px solid #bfcad3;
            float: left;
            clear: both;
        }

        * + html .shopping-see-also {
            margin-top: 30px;
        }

        * html .shopping-see-also {
            margin-top: 30px;
        }

        .shopping-see-also .header {
            width: 670px;
            margin: 0;
            padding: 12px 12px 20px 12px;
            float: left;
            clear: both;
            font: bold 14px Arial, Helvetica, sans-serif;
        }

        .shopping-see-also .header a:link {
            color: #176fcc;
            text-decoration: none;
            font-weight: bold;
        }

        .shopping-see-also .header a:visited {
            color: #176fcc;
            text-decoration: none;
            font-weight: bold;
        }

        .shopping-see-also .header a:hover {
            color: #176fcc;
            text-decoration: underline;
            font-weight: bold;
        }

        .shopping-see-also .header a:active {
            color: #176fcc;
            text-decoration: underline;
            font-weight: bold;
        }

        .shopping-see-also .buying-guide {
            float: left;
            clear: both;
            width: 331px;
            height: 109px;
            background: url(<%= realContext %>/template/cr/shop_buying_bkg.gif) no-repeat;
            margin: 0;
            padding: 0 12px 0 0;
            border-right: 1px solid #bfcad3;
        }

        .shopping-see-also .buying-guide .content {
            float: left;
            width: 307px;
            background: url(<%= realContext %>/template/cr/shop_buying_bkg.gif) no-repeat;
            margin: 0;
            padding: 0 12px;
            font: normal 11px/14px Arial, Helvetica, sans-serif;
        }

        .shopping-see-also .buying-guide .content .header {
            float: left;
            clear: none;
            margin: 0;
            padding: 0;
            font: bold 12px Arial, Helvetica, sans-serif;
            width: 240px;
        }

        .shopping-see-also .buying-guide .content .blurb {
            float: left;
            margin: 0;
            padding: 0;
            font: normal 11px/14px Arial, Helvetica, sans-serif;
            width: 240px;
        }

        .shopping-see-also .buying-guide .content .thumb {
            float: left;
            margin: 0 12px 12px 0;
            padding: 0;
            border: 1px solid #878787;
            width: 53px;
            height: 53px;
        }

        .shopping-see-also .related-articles {
            float: left;
            width: 326px;
            margin: 0;
            padding: 0 12px;
            font: bold 12px Arial, Helvetica, sans-serif;
        }

        .shopping-see-also .related-articles ul {
            list-style: none;
            margin: 6px 0 0 0;
            padding: 0;
        }

        .shopping-see-also .related-articles li {
            background: url(<%= realContext %>/template/cr/cro_newsdash3.gif) no-repeat 0 7px;
            margin: 0;
            padding: 0 12px 3px 12px;
            font: normal 12px/16px Arial, Helvetica, sans-serif;
        }

        .shopping-see-also .related-articles li a:link {
            text-decoration: underline;
            color: #000;
        }

        .shopping-see-also .related-articles li a:visited {
            text-decoration: underline;
            color: #000;
        }

        .shopping-see-also .related-articles li a:hover {
            text-decoration: underline;
            color: #768B9E;
        }

        .shopping-see-also .related-articles li a:active {
            text-decoration: underline;
            color: #768B9E;
        }

        .shopping-see-also .related-articles .more {
            font: bold 11px Arial, Helvetica, sans-serif;
            margin-left: 13px;
        }

        .shopping-see-also .related-articles .more img {
            margin: 0 6px 0 0;
            padding: 0;
            border: none;
        }

        .shopping-see-also .related-articles .more a:link {
            color: #176fcc;
            text-decoration: none;
            font-weight: bold;
        }

        .shopping-see-also .related-articles .more a:visited {
            color: #176fcc;
            text-decoration: none;
            font-weight: bold;
        }

        .shopping-see-also .related-articles .more a:hover {
            color: #176fcc;
            text-decoration: underline;
            font-weight: bold;
        }

        .shopping-see-also .related-articles .more a:active {
            color: #176fcc;
            text-decoration: underline;
            font-weight: bold;
        }

        /*info popup css*/
        .ratings-repos-info-pop {
            width: 258px;
            font: 11px Arial, Helvetica, sans-serif;
        }

        .ratings-repos-info-pop dl {
            width: 258px;
            margin: 0;
        }

        .ratings-repos-info-pop dl dd.top {
            height: 10px;
            margin: 0;
            padding: 0;
            border: none;
        }

        .ratings-repos-info-pop dl dd.middle {
            margin: 0;
            padding: 0 12px 8px 12px;
            border: none;
            background: transparent;
        }

        .ratings-repos-info-pop dl dd.bottom {
            height: 20px;
            margin: 0;
            padding: 0;
            border: none;
            font-size: 1px;
            line-height: 1px;
        }

        .ratings-repos-info-pop dl dd.top {
            background: url(<%= realContext %>/template/cr/pop_box_top.png) no-repeat top left;
        }

        * html .ratings-repos-info-pop dl dd.top {
            background-image: none;
            filter: progid:DXImageTransform.Microsoft.AlphaImageLoader(enabled=true, sizingMethod=scale, src='<%= realContext %>/template/cr/pop_box_top.png');
        }

        .ratings-repos-info-pop dl dd.middle {
            background-image: url(<%= realContext %>/template/cr/products/pop_box_mid.png);
        }

        * html .ratings-repos-info-pop dl dd.middle {
            height: 100%;
            background-image: none;
            filter: progid:DXImageTransform.Microsoft.AlphaImageLoader(enabled='true', sizingMethod='scale', src='<%= realContext %>/template/cr/products/pop_box_mid.png');
        }

        .ratings-repos-info-pop dl dd.bottom {
            background: url(<%= realContext %>/template/cr/products/pop_box_bottom.png) no-repeat bottom left;
        }

        * html .ratings-repos-info-pop dl dd.bottom {
            background-image: none;
            filter: progid:DXImageTransform.Microsoft.AlphaImageLoader(enabled=true, sizingMethod=scale, src='<%= realContext %>/template/cr/products/pop_box_bottom.png');
        }

        .ratings-repos-info-pop .top-right {
            height: 10px;
            margin: 0;
            padding: 0;
            border: none;
            background: url(<%= realContext %>/template/cr/products/pop_box_top_right.png) no-repeat top left;
        }

        * html .ratings-repos-info-pop .top-right {
            background-image: none;
            filter: progid:DXImageTransform.Microsoft.AlphaImageLoader(enabled=true, sizingMethod=scale, src='<%= realContext %>/template/cr/products/pop_box_top_right.png');
        }

        .ratings-repos-info-pop .middle-right {
            margin: 0;
            padding: 0 12px 8px 12px;
            border: none;
            background: transparent;
            background-image: url(<%= realContext %>/template/cr/products/pop_box_mid_right.png);
        }

        * html .ratings-repos-info-pop .middle-right {
            height: 100%;
            background-image: none;
            filter: progid:DXImageTransform.Microsoft.AlphaImageLoader(enabled='true', sizingMethod='scale', src='<%= realContext %>/template/cr/products/pop_box_mid_right.png');
        }

        .ratings-repos-info-pop .bottom-right {
            height: 20px;
            margin: 0;
            padding: 0;
            border: none;
            font-size: 1px;
            line-height: 1px;
            background: url(<%= realContext %>/template/cr/pop_box_bottom_right.png) no-repeat bottom left;
        }

        * html .ratings-repos-info-pop .bottom-right {
            background-image: none;
            filter: progid:DXImageTransform.Microsoft.AlphaImageLoader(enabled=true, sizingMethod=scale, src='<%= realContext %>/template/cr/pop_box_bottom_right.png');
        }

        .info-popup-blob {
            width: 139px;
            font: 11px Arial, Helvetica, sans-serif;
        }

        .info-popup-blob dl {
            width: 139px;
            margin: 0;
        }

        .info-popup-blob dl dd.top {
            height: 10px;
            margin: 0;
            padding: 0;
            border: none;
        }

        .info-popup-blob dl dd.middle {
            margin: 0;
            padding: 0 12px 8px 12px;
            border: none;
            background: transparent;
        }

        .info-popup-blob dl dd.bottom {
            height: 20px;
            margin: 0;
            padding: 0;
            border: none;
            font-size: 1px;
            line-height: 1px;
        }

        .info-popup-blob dl dd.top {
            background: url(<%= realContext %>/template/cr/products/pop_sm_top.png) no-repeat top left;
        }

        * html .info-popup-blob dl dd.top {
            background-image: none;
            filter: progid:DXImageTransform.Microsoft.AlphaImageLoader(enabled=true, sizingMethod=scale, src='<%= realContext %>/template/cr/products/pop_sm_top.png');
        }

        .info-popup-blob dl dd.middle {
            background-image: url(<%= realContext %>/template/cr/pop_sm_mid.png);
        }

        * html .info-popup-blob dl dd.middle {
            height: 100%;
            background-image: none;
            filter: progid:DXImageTransform.Microsoft.AlphaImageLoader(enabled='true', sizingMethod='scale', src='<%= realContext %>/template/cr/pop_sm_mid.png');
        }

        .info-popup-blob dl dd.bottom {
            background: url(<%= realContext %>/template/cr/pop_sm_btm.png) no-repeat bottom left;
        }

        * html .info-popup-blob dl dd.bottom {
            background-image: none;
            filter: progid:DXImageTransform.Microsoft.AlphaImageLoader(enabled=true, sizingMethod=scale, src='<%= realContext %>/template/cr/pop_sm_btm.png');
        }

        .info-popup-blob .top-right {
            height: 10px;
            margin: 0;
            padding: 0;
            border: none;
            background: url(<%= realContext %>/template/cr/products/pop_sm_top.png) no-repeat top left;
        }

        * html .info-popup-blob .top-right {
            background-image: none;
            filter: progid:DXImageTransform.Microsoft.AlphaImageLoader(enabled=true, sizingMethod=scale, src='<%= realContext %>/template/cr/products/pop_sm_top.png');
        }

        .info-popup-blob .middle-right {
            margin: 0;
            padding: 0 12px 8px 12px;
            border: none;
            background: transparent;
            background-image: url(<%= realContext %>/template/cr/pop_sm_mid.png);
        }

        * html .info-popup-blob .middle-right {
            height: 100%;
            background-image: none;
            filter: progid:DXImageTransform.Microsoft.AlphaImageLoader(enabled='true', sizingMethod='scale', src='<%= realContext %>/template/cr/pop_sm_mid.png');
        }

        .info-popup-blob .bottom-right {
            height: 20px;
            margin: 0;
            padding: 0;
            border: none;
            font-size: 1px;
            line-height: 1px;
            background: url(<%= realContext %>/template/cr/products/pop_sm_btm_r.png) no-repeat bottom left;
        }

        * html .info-popup-blob .bottom-right {
            background-image: none;
            filter: progid:DXImageTransform.Microsoft.AlphaImageLoader(enabled=true, sizingMethod=scale, src='<%= realContext %>/template/cr/products/pop_sm_btm_r.png');
        }

        .ratings-repos-info-pop .close {
            float: left;
            clear: both;
            width: 230px;
            margin: 12px 0 0 0;
            padding: 0;
        }

        .ratings-repos-info-pop .close img {
            display: inline;
            margin: 0;
            padding: 0 6px 0 0;
            position: relative;
            top: 2px;
        }

        /*New CSS for Similars*/
        .overview-product-model .left-column .model-colors {
            clear: both;
            color: #000;
            float: left;
            font: bold 11px/14px Arial, Helvetica, sans-serif;
            margin: 5px 0 0;
            width: 644px;
        }

        .overview-product-model .left-column .model-colors select {
            color: #666;
            font: bold 11px/14px Arial, Helvetica, sans-serif;
            margin-left: 5px;
            width: 150px;
        }

        .overview-product-model .left-column .model-colors select option[disabled] {
            color: #666;
        }

        .overview-product-model .left-column .model-colors select option {
            color: #000;
        }

        .overview-product-model .left-column .model-colors select option#default {
            color: #666;
        }

        .overview-product-model .middle-column .price-range-value {
            margin: 0;
        }

        .overview-product-model .middle-column .discontinued {
            color: #666;
            clear: both;
            float: left;
            font: bold 11px/14px Arial, Helvetica, sans-serif;
        }

        .chart-content table.similar-models-chart {
            border: 1px solid #C3D2E0;
            border-collapse: separate;
            clear: both;
            display: block;
            float: none;
            margin: 0 0 30px;
            padding: 0;
            width: 694px;
        }

        .chart-content table.similar-models-chart thead th {
            background: #EEF1F6 none repeat scroll 0 0;
            font: bold 14px Arial, Helvetica, sans-serif;
            padding: 6px 12px;
            text-align: left;
            width: 598px;
        }

        .chart-content table.similar-models-chart td:first-child {
            padding: 17px 12px 5px;
        }

        .chart-content table.similar-models-chart tbody.chart-info td {
            padding: 12px;
        }

        .chart-content table.similar-models-chart td {
            background: #FFFFFF none repeat scroll 0 0;
            border-top: 1px solid #c3d2e0;
            color: #000000;
            font: normal 12px/16px Arial, Helvetica, sans-serif;
            padding: 17px 12px;
            text-align: left;
        }

        .chart-content table.similar-models-chart td.model-image {
            border-top: 0 none;
            margin: 0;
            padding-top: 0;
            width: 95px;
        }

        .chart-content table.similar-models-chart td.recommended {
            border-top: 0 none;
            padding: 0;
        }

        .chart-content table.similar-models-chart td.model-info {
            width: 630px;
            border-top: 0 none;
            font: normal 12px/16px Arial, Helvetica, sans-serif;
            padding: 0 12px 20px 4px;
        }

        .chart-content table.similar-models-chart td.model-image .images {
            position: relative;
        }

        .chart-content table.similar-models-chart td.model-image span.recommended {
            position: absolute;
            right: 6px;
            top: 6px;
        }

        .chart-content table.similar-models-chart td.model-info .price-range-head {
            font-weight: bold;
        }

        .chart-content table.similar-models-chart td.model-info a {
            white-space: nowrap;
        }

        .chart-content table.similar-models-chart td.model-info .price-shop {
            padding: 6px 0;
        }

        .chart-content table.similar-models-chart tr.see-more-row td {
            background: transparent url(/cro/resources/content/products/compare/model_compare_header_bkg.gif) repeat-x scroll center bottom;
            border-left: 0 none;
            border-right: 0 none;
            color: #000000;
            font-size: 12px;
            font-weight: bold;
            padding: 2px 7px 2px 5px;
            vertical-align: bottom;
        }

        .chart-content table.similar-models-chart tr.see-more-row img {
            border: 0 none;
            margin: 0;
            padding: 0;
        }

        .chart-content table.similar-models-chart tr.see-more-row td img.arrow {
            padding: 0 5px 3px 0;
            vertical-align: middle;
        }

        .chart-content table.similar-models-chart tr.see-more-row td img.arrow, x:-moz-any-link {
            padding: 0 5px 0 0;
        }

        .more-ratings .container {
            float: left;
        }

        /* .more-ratings .container .model-information {
	position: relative;
	}
 */
        .more-ratings .container .checkbox {
            float: left;
            margin: 0 3px 0 0;
            padding: 0;
        }

        .more-ratings .container .chosen-models {
            background: #fff;
            /*border: 1px solid #B5BFC7;*/
            clear: both;
            color: #000;
            float: left;
            font: bold 11px/14px Arial, Helvetica, sans-serif;
            margin: 11px 0 0;
            /*padding: 8px 0px 12px 12px;*/
            width: 152px;
        }

        .more-ratings .container .chosen-models {
            border: 1px solid #B5BFC7;
            padding: 8px 0px 12px 12px;
            text-align: left;
        }

        .more-ratings .container .chosen-models div {
            padding: 2px 0;
        }

        .more-ratings .container .chosen-models div:first-child {
            padding-top: 6px;
        }

        .more-ratings .container .chosen-models .chosenBlock {
            float: left;
            width: 140px;
        }

        .more-ratings .container .chosen-models .chosenX {
            float: left;
            padding-right: 12px;
            padding-top: 4px;
        }

        .more-ratings .container .chosen-models .chosenLink {
            overflow: hidden;
            text-align: left;
        }

        .more-ratings .container .chosen-models .brand {
            display: block;
            font-weight: bold;
        }

        .more-ratings .container .chosen-models a:link,
        .more-ratings .container .chosen-models a:visited,
        .more-ratings .container .chosen-models a:active {
            color: #176fcc;
            text-decoration: none
        }

        .more-ratings .container .chosen-models a:hover .brand,
        .more-ratings .container .chosen-models a:hover {
            color: #176fcc;
            text-decoration: underline;
        }

        .more-ratings .container .compare-button {
            float: left;
            padding-top: 10px;
            padding-bottom: 0;
            width: 164px;
            text-align: center;
        }

        #content-right-rail .see-models .header {
            background: #EEF1F6 none repeat scroll 0 0;
            border-bottom: 1px solid #C3D2E0;
            clear: both;
            float: left;
            font: bold 14px Arial, Helvetica, sans-serif;
            line-height: normal;
            margin: 0;
            padding: 3px 12px;
            width: 156px;
        }

        #content-right-rail .see-models {
            border-bottom: 1px solid #C3D2E0;
            clear: both;
            float: left;
            margin: 0 0 14px;
            padding: 0;
            width: 180px;
        }

        #content-right-rail .see-models .links {
            float: left;
            margin: 0;
            padding: 8px 12px;
        }

        #content-right-rail .see-models .links a:link,
        #content-right-rail .see-models .links a:visited,
        #content-right-rail .see-models .links a:active {
            color: #176fcc;
            display: block;
            font: normal 11px/18px Arial, Helvetica, sans-serif;
            text-decoration: none;
        }

        #content-right-rail .see-models .links a:hover {
            text-decoration: underline;
        }

        /*END similars styles*/
        .more-ratings .container {
            float: left;
        }

        .more-ratings .container .model-information {
            position: relative;
            padding: 9px 0 9px 2px;
            width: 162px;
        }

        .more-ratings .container .model-information .product-info {
            padding: 0 0 0 5px;
            width: 70px;
        }

        .more-ratings .container .model-information .product-info .price {
            width: auto;
        }

        .more-ratings .container .model-information .product-info .make {
            float: none;
            width: 70px;
        }

        .more-ratings .container .model-information .product-info .model {
            width: 70px;
            float: none;
            word-wrap: break-word;
        }

        #errorMsg {
            border: 1px solid #BFCAD3;
            padding: 10px;
            background-color: white;
            width: 258px;
            position: absolute;
            visibility: hidden;
            font: 12px Arial, Helvetica, sans-serif;
            z-index: 99;
        }

        #errorMsg dl {
            padding: 0px;
            margin: 0px;
        }

        #errorMsg dd {
            padding: 0px;
            margin: 0px;
        }

        #content-body .chart-content .specs-and-features-full ul {
            padding: 0;
            margin: 0;
            list-style: none;
            line-height: 1.5em;
        }

        #content-body .chart-content .specs-and-features-full li {
            list-style: none;
            padding-left: 8px;
            background: url(<%= realContext %>/template/cr/squarebullet-2.gif) no-repeat 0 7px;
        }

        @media print {
            #content {
                display: block;
                overflow: visible;
            }

            #content-right-rail {
                display: none;
            }

            .go-to-or-print {
                float: none;
                clear: both;
            }

            .chart-content {
                float: none;
                clear: both;
            }

            .chart-content table.ratings-report-card {
                float: none;
                clear: both;
            }

            .chart-content table.ratings-report-card td.table-info .legend {
                float: none;
            }

            .chart-content table.review-recommended {
                float: none;
                clear: both;
            }

            .chart-content table.review-recommended td img {
                float: none;
            }

            .chart-content table.about-this-model {
                float: none;
                clear: both;
            }

            .chart-content table.about-this-model td img {
                float: none;
            }

            .chart-content table.about-this-brand {
                float: none;
                clear: both;
            }

            .chart-content table.about-this-brand td img {
                float: none;
            }

            .chart-content table.specs-and-features {
                float: none;
                clear: both;
            }

            .chart-content table.specs-and-features td img {
                float: none;
            }

            .chart-content .user-reviews {
                float: none;
                clear: both;
            }

            .chart-content .user-reviews .average-user-rating {
                float: none;
                clear: both;
            }

            .chart-content .user-reviews .average-user-rating .ratings-box .average-user-rating-header {
                float: none;
                clear: both;
            }

            .chart-content .user-reviews .average-user-rating .ratings-box .ratings-stars {
                float: none;
                clear: both;
            }

            .chart-content .user-reviews .average-user-rating .ratings-box .number-of-reviews {
                float: none;
                clear: both;
            }

            .chart-content .user-reviews .average-user-rating .ratings-box .write-a-review {
                overflow: visible;
                float: none;
                clear: both;
            }

            .chart-content .user-reviews .average-user-rating .display {
                float: none;
            }

            .chart-content .user-reviews .divider {
                float: none;
                clear: both;
            }

            .chart-content .user-reviews .prReviewWrap {
                float: none;
                clear: both;
            }

            .chart-content .user-reviews .prReviewWrap div.prStars {
                float: none;
            }

            .chart-content .user-reviews .prReviewWrap .prReviewRatingHeadline {
                float: none;
                clear: both;
            }

            .chart-content .user-reviews .prReviewWrap div.prStars .helpful-review {
                float: none;
            }

            .chart-content .user-reviews .prReviewWrap div.prStars .helpful-review .stars {
                overflow: visible;
                float: none;
            }

            .chart-content .user-reviews .footer {
                float: none;
            }

            .chart-content table.about-price-and-shop {
                float: none;
                clear: both;
            }

            .chart-content table.brand-reliability {
                float: none;
                clear: both;
            }

            .chart-content table.brand-reliability td.rating .ratings-bar {
                float: none;
                clear: both;
            }

            .chart-content table.brand-reliability td.rating .ratings-bar .value {
                float: none;
            }

            .brand-reliability-table-footer {
                float: none;
                clear: both;
            }

            .chart-content table.first-look {
                float: none;
                clear: both;
            }

            .ad-slot-1 {
                float: none;
                clear: both;
            }

            .ad-slot-2 {
                float: none;
                clear: both;
            }

            .ad-slot-3 {
                float: none;
                clear: both;
            }

            .ad-slot-4 {
                float: none;
            }

            #content-footer {
                float: none;
            }

            .shopping-see-also {
                overflow: visible;
                float: none;
            }

            .shopping-see-also .header {
                float: none;
                clear: both;
            }

            .shopping-see-also .buying-guide {
                float: none;
            }

            .shopping-see-also .buying-guide .content {
                float: none;
            }

            .shopping-see-also .buying-guide .content .header {
                float: none;
            }

            .shopping-see-also .buying-guide .content .blurb {
                float: none;
            }

            .shopping-see-also .buying-guide .content .thumb {
                float: none;
            }

            .shopping-see-also .related-articles {
                float: none;
            }

            .ratings-repos-info-pop .close {
                float: none;
            }

            .mboxDefault {
                overflow: visible;
            }
        }

        .model-colors select#default {
            color: #666 !important;
        }
    </style>
    <style type="text/css">
        /* shop online disclaimer */
        .shoponline ul {
            color: #000;
            padding: 0;
            margin: 0 0 20px 10px;
            list-style: none;
            line-height: 1.2em;
        }

        .shoponline li {
            padding: 0 0 0 8px;
            margin: 0 0 8px 10px;
            background: url(<%= realContext %>/template/cr/squarebullet-3.gif) no-repeat 0 7px;
        }

        /* shop online disclaimer ends */
        .prReviewResult {
            line-height: normal;
            padding: 6px 12px;
            height: 14px;
        }

        .prSubmitLink a:hover, .prSubmitLink a:link, .prSubmitLink a:visited {
            background: #FFEEC1 url(<%= realContext %>/template/cr/cro_cr_slug.gif) no-repeat right center;
            margin: 0;
            padding: 0 20px 0 0;
            font: bold 11px Arial, Helvetica, sans-serif;
            color: #176fcc;
            text-decoration: none;
        }

        .prSubmitLink a:hover {
            text-decoration: underline;
        }

        .user-reviews-wrapper {
            clear: both;
            float: left;
            margin: 0 0 30px;
            padding: 0;
        }

        .prReviewEngine {
            border: 1px solid #C3D2E0;
            clear: both;
            float: left;
            font-family: inherit;
            font-size: inherit;
            font-size-adjust: inherit;
            font-stretch: inherit;
            font-style: inherit;
            font-variant: inherit;
            font-weight: inherit;
            line-height: inherit;
            width: 692px;
        }

        #prReviewSummary {
            background: #FFFFFF none repeat scroll 0 0;
        }

        .prSummaryHeader {
            background: #F1F4F8 none repeat scroll 0 0;
            border-bottom: 1px solid #C3D2E0;
            border-top: medium none;
            margin: 0;
            padding: 6px 12px;
            width: 668px;
        }

        .prSummaryTitle {
            color: #000000;
            font-family: Arial, Helvetica, sans-serif;
            font-size: 14px;
            font-size-adjust: none;
            font-stretch: normal;
            font-style: normal;
            font-variant: normal;
            font-weight: bold;
            line-height: normal;
        }

        .prSummaryBody {
            margin: 12px;
            overflow: auto;
            padding: 0;
        }

        .prSummaryRating {
            width: 203px;
            margin: 0 12px 0 0;
            padding: 0;
            background: url(<%= realContext %>/template/cr/user_review_sm_box.gif) no-repeat;
            height: 148px;
            float: left;
        }

        .prSummaryRating span {
            float: left;
            clear: both;
            font: normal 11px Arial, Helvetica, sans-serif;
            color: #5A3909;
            margin: 0 0 10px;
            text-align: center;
            width: 100%;
        }

        .prSummaryRating .prSummaryAverageRatingText {
            color: #5A3909;
            float: left;
            font-family: Arial, Helvetica, sans-serif;
            font-size: 14px;
            font-size-adjust: none;
            font-stretch: normal;
            font-style: normal;
            font-variant: normal;
            font-weight: bold;
            line-height: normal;
            margin: 0;
            padding: 20px 0 0;
            text-align: center;
            width: 100%;
        }

        .prSummaryRatingAdSlot {
            float: right;
            margin: 50px 0 0;
            overflow: hidden;
            padding: 0;
            width: 205px;
        }

        div.prStars {
            background-repeat: no-repeat;
            clear: both;
            float: left;
            height: 20px;
            line-height: 20px;
            margin: 12px 12px 6px 42px;
            padding: 0;
            width: 112px;
        }

        .prReviewWrap {
            border-color: #AABBDD;
            color: #000;
            font-size: 12px;
            position: relative;
            border-top: 1px solid #CCCCCC;
            margin: 1em;
            padding-top: 1em;
        }

        .prReviewRating {
            clear: both;
            float: left;
            font-size: 1.15em;
            position: absolute;
        }

        div.prStars.prStarsSmall {
            width: 85px;
            height: 15px;
            line-height: 15px;
            background-repeat: no-repeat;
            float: left;
            margin: 0 0.25em 0 0;
            padding: 0;
            position: relative;
        }

        .prReviewRatingHeadline {
            color: #000;
            font-size: 14px;
            clear: both;
            font-weight: bold;
            padding-top: 8px;
        }

        * html .prReviewRatingHeadline {
            padding-top: 4px;
        }

        * + html .prReviewRatingHeadline {
            padding-top: 4px;
        }

        .prReviewAuthor {
            clear: both;
            padding-top: 40px;
            color: #666666;
            font-size: 11px;
        }

        .prReviewAuthorName span, .prReviewAuthorLocation span, .prReviewAuthorDate span {
            font-weight: bold;
        }

        .prReviewPoints {
            margin: 1em 0;
            overflow: auto;
        }

        .attributeGroup, .prAttributeGroup {
            margin: 0.5em 0;
        }

        .prReviewKey {
            clear: left;
            float: left;
            font-weight: bold;
            padding-right: 2px;
            vertical-align: top;
        }

        .prReviewValue {
            padding: 0 0 0 10px;
            text-align: left;
            vertical-align: top;
        }

        .prAttributeGroupSeparator {
            clear: both;
        }

        .prReviewText {
            clear: both;
            margin: 1.5em 0 1em;
            font-size: 12px;
            line-height: 16px;
        }

        .prReviewTools {
            margin: 0.5em -1em;
            padding: 0.5em 1em 0;
        }

        .prReviewTools span {
            font-style: normal;
        }

        .prReviewHelpfulText {
            float: left;
            font-size: 0.9em;
            margin-bottom: -20px;
            margin-left: 100px;
            margin-top: 1px;
        }

        .prReviewHelpfulTextT {
            font-size: 0.9em;
            margin-bottom: -20px;
            margin-left: 10px;
            margin-top: 0;
        }

        a.prReviewHelpfulTextLink:link, a.prReviewHelpfulTextLink:visited, a.prReviewHelpfulTextLink:hover, a.prReviewHelpfulTextLink:active {
            font-size: 1em;
        }

        .prReviewTools span a {
            font-weight: bold;
        }

        .prReviewReportIssue {
            font-size: 0.9em;
            font-style: italic;
            margin-left: 0.5em;
        }

        .prWriteReview {
            clear: both;
            text-align: center;
        }

        .prWriteReview a, .prWriteReview a:link, .prWriteReview a:hover, .prWriteReview a:visited {
            font-weight: bold;
        }

        .prSubmitBottom {
            background: #EEF1F6 none repeat scroll 0 0;
            border-top: 1px solid #C3D2E0;
            border-right: medium none;
            border-bottom: medium none;
            line-height: normal;
            padding: 6px 12px;
            height: 14px;
        }

        @print media {
            .prReviewHelpfulText {
                float: none;
            }

            .prReviewKey {
                float: none;
            }

            div.prStars.prStarsSmall {
                float: none;
            }

            .prReviewRating {
                float: none;
            }

            div.prStars {
                float: none;
            }

            .prSummaryRatingAdSlot {
                overflow: visible;
                float: none;
            }

            .prSummaryRating .prSummaryAverageRatingText {
                float: none;
            }

            .prSummaryRating span {
                float: none;
            }

            .prSummaryRating {
                float: none;
            }

            .prReviewEngine {
                float: none;
            }

            .user-reviews-wrapper {
                float: none;
            }

            .prSummaryBody {
                overflow: visible;
            }

            .prReviewPoints {
                overflow: visible;
            }
        }
    </style>
    <script type="text/javascript" src="<%= realContext %>/template/cr/jquery_003.js"></script>
    <script type="text/javascript" src="<%= realContext %>/template/cr/jquery_002.js"></script>
    <script type="text/javascript" src="<%= realContext %>/template/cr/jquery.js"></script>
    <script type="text/javascript" src="<%= realContext %>/template/cr/header.js"></script>
    <!-- script type="text/javascript" src="<%= realContext %>/template/cr/typeahead.js"></script -->
    <script type="text/javascript" src="<%= realContext %>/template/cr/sx-render.js"></script>
    <script type="text/javascript" src="<%= realContext %>/template/cr/oas_analytics.js"></script>
    <script type="text/javascript" src="<%= realContext %>/template/cr/mbox.js"></script>
    <script type="text/javascript" src="<%= realContext %>/template/cr/event-handlers.js"></script>
    <script type="text/javascript" src="<%= realContext %>/template/cr/jquery-ui-1.js"></script>
    <!-- script type="text/javascript" src="<%= realContext %>/template/cr/user-info.js"></script -->
    <script type="text/javascript" src="<%= realContext %>/template/cr/loginpopup.js"></script>
    <script type="text/javascript">addEventHandler(window, "load", renderAds);</script>
    <script src="<%= realContext %>/template/cr/69071259.js"></script>
    <script type="text/javascript" src="<%= realContext %>/template/cr/Arrays.js"></script>
    <script type="text/javascript" src="<%= realContext %>/template/cr/DOM.js"></script>
    <!-- script type="text/javascript" src="<%= realContext %>/template/cr/oeLauncher.js"></script -->
    <script type="text/javascript" src="<%= realContext %>/template/cr/event-handlers_002.js"></script>
    <script type="text/javascript" src="<%= realContext %>/template/cr/InfoPopup.js"></script>
    <script type="text/javascript" src="<%= realContext %>/template/cr/Cookies.js"></script>
    <script type="text/javascript" src="<%= realContext %>/template/cr/Properties.js"></script>
    <script type="text/javascript" src="<%= realContext %>/template/cr/sessvars.js"></script>
    <script type="text/javascript" src="<%= realContext %>/template/cr/json2.js"></script>
    <script type="text/javascript" src="<%= realContext %>/template/cr/compare-event.js"></script>
    <script type="text/javascript" src="<%= realContext %>/template/cr/compare-basket.js"></script>
    <script type="text/javascript" src="<%= realContext %>/template/cr/compare-integration.js"></script>
    <script type="text/javascript" src="<%= realContext %>/template/cr/compare.js"></script>
    <script type="text/javascript" src="<%= realContext %>/template/cr/jqzoom-carousel.js"></script>
    <script type="text/javascript" src="<%= realContext %>/template/cr/carousel.js"></script>
    <script type="text/javascript" src="<%= realContext %>/template/cr/photo-gallery-carousel.js"></script>
    <script language="javascript">
        function launchErrorDiv(elem) {
            if (document.getElementById('error_div' + elem).style.display != "block")
                document.getElementById('error_div' + elem).style.display = "block";
            else
                document.getElementById('error_div' + elem).style.display = "none";
        }
    </script>
    <link rel="stylesheet" type="text/css" href="<%= realContext %>/template/cr/sys-cr.css">
</head>
<body>
<div id="t-header" class="container_16">
    <%-- necessary for GWT history support --%>
    <iframe src="javascript:''" id="__gwt_historyFrame" tabIndex='-1'
            style="position:absolute;width:0;height:0;border:0"></iframe>
    <div class="header">
        <div class="header">


            <div id="body-wrap">
                <div id="sign-in-wrap">
                    <div id="sign-in-menu">
                        <div id="sign-in-menu-openned" class="dialog" style="display:none;">
                            <div class="content">
                                <div class="t"></div>
                                <div style="padding-top:10px;">
                                    <div class="sign-in-btn-left">&nbsp;</div>
                                    <div class="sign-in-btn-body">

                                <span onclick="toggleLogMenu()" id="sign-in-link">Sign In <img
                                        src="<%= realContext %>/template/cr/arrow_signin_up.png" alt="up-arrow"
                                        class="sign-in-arrow"></span>|
                                        <a class="sign-in-link-r"
                                           href="https://ec.consumerreports.org/ec/cro/order.htm?INTKEY=I0AHLT4">Subscribe</a>

                                    </div>
                                    <div class="sign-in-btn-right">&nbsp;</div>


                                    <div style="clear:both; border-top:1px solid #F8F8F8;">
                                        <form id="sign-in-form" action="https://ec.consumerreports.org/ec/cro/login.htm"
                                              method="post" name="login">
                                            <label id="sign-in-username">Username</label>
                                            <input class="sign-in-input" name="userName"
                                                   onkeydown="addInputSubmitEvent(event)"
                                                   type="text"><br>
                                            <label id="sign-in-password">Password</label>
                                            <input class="sign-in-input" name="password"
                                                   onkeydown="addInputSubmitEvent(event)"
                                                   type="password">

                                            <div id="sign-in-bottom">
                                                <input src="<%= realContext %>/template/cr/b_signin_signin.png"
                                                       alt="Sign In"
                                                       id="sign-in-btn" type="image">
                                                <input id="sign-in-check-box" name="setAutoLogin" type="checkbox">
                                                <label class="remember-text">Remember Me</label>

                                                <div id="sign-in-request-links">
                                                    <a href="https://ec.consumerreports.org/ec/myaccount/forgot_username.htm"
                                                       class="sign-in-recover-links">Forgot username?</a><br>
                                                    <a href="https://ec.consumerreports.org/ec/myaccount/forgot_password.htm"
                                                       class="sign-in-recover-links">Forgot password?</a>
                                                </div>
                                            </div>
                                        </form>
                                    </div>

                                </div>
                            </div>
                            <div class="b">
                                <div style="height:15px"></div>
                            </div>
                            <img src="<%= realContext %>/template/cr/b_close.gif" alt="close" class="sign-in-close-btn"
                                 onclick="toggleLogMenu()">
                        </div>
                        <div id="sign-in-menu-closed">
                            <div class="sign-in-btn-left">&nbsp;</div>
                            <div class="sign-in-btn-body">

                        <span onclick="toggleLogMenu()" id="sign-in-link">Sign In <img
                                src="<%= realContext %>/template/cr/arrow_signin_down.png" alt="down-arrow"
                                class="sign-in-arrow"></span>|
                                <a class="sign-in-link-r"
                                   href="https://ec.consumerreports.org/ec/cro/order.htm?INTKEY=I0AHLT4">Subscribe</a>

                            </div>
                            <div class="sign-in-btn-right">&nbsp;</div>
                        </div>
                        <div id="signup-links">

                            <a href="https://ec.consumerreports.org/ec/myaccount/main.htm" id="sign-up-myaccount">My
                                Account </a>&nbsp;|&nbsp;

                            <a href="http://custhelp.consumerreports.org/cgi-bin/consumerreports.cfg/php/enduser/home.php"
                               id="sign-up-cservice">Customer Service</a>&nbsp;&nbsp;|&nbsp;
                            <a href="http://web.consumerreports.org/features/index.html">Site Features</a>
                        </div>
                    </div>
                </div>
                <div id="subscribe-wrap">
                    <a href="https://consumerreports.secure-donor.com/consumerreports?source=3028000101"
                       class="header-tr-nav">Donate</a>
                    <span style="float:right;margin-left:7px;">|</span>


                    <a href="https://ec.consumerreports.org/ec/<%= realContext %>/template/cr/order.htm?INTKEY=IW06CDR4"
                       class="header-tr-nav">Subscribe to the magazine</a>

                    <img src="<%= realContext %>/template/cr/home_header_magazines.png" alt="Magazine"
                         id="magazine-image-btn">

                </div>
                <div id="header-content-wrap">
                    <div id="header-content">
                        <a href="http://www.consumerreports.org/cro/index.htm"><img id="cr-logo"
                                                                                    src="<%= realContext %>/template/cr/cr_logo_home.png"
                                                                                    alt="ConsumerReports.org"></a>

                        <div id="typeahead">
                            <div class="input-box">
                                <div class="input-box-copy">
                                    <form name="search-form" id="search-form"
                                          action="http://www.consumerreports.org/cro/search.htm">
                                        <input class="search" autocomplete="off"
                                               typeahead="/etc/designs/cro/application-resources/modules/header/data/typeahead-data.js"
                                               name="query" id="search" onfocus="focusSearch(this)"
                                               onblur="blurSearch(this)"
                                               type="text">
                                    </form>
                                </div>
                                <div class="input-box-button" onclick="CUTypeAhead.submit();"></div>
                            </div>
                            <div class="results-outer-box" id="matches" style="display: none;"></div>
                        </div>
                        <div id="AZ-link">
                            <a href="http://www.consumerreports.org/cro/a-to-z-index/products/index.htm">A-Z Index</a>
                        </div>
                    </div>
                </div>
            </div>

            <div style="display:none; position: absolute; z-index: 3;" id="log-in-popup-wrap">
                <div class="pointer-top-left non-ie-box" id="log-in-popup-wrap-top" style="display: none;">&nbsp;</div>
                <div class="description-box non-ie-box">Subscribers only<br><a class="sign-in" href="#">Sign in</a> or
                    <a
                            class="subscribe" href="https://ec.consumerreports.org/ec/cro/order.htm">Subscribe now!</a>
                </div>
                <div class="pointer-bottom-left non-ie-box" id="log-in-popup-wrap-bottom" style="display: block;">
                    &nbsp;</div>
            </div>


        </div>
        <div class="clear"></div>
        <div class="mainNav">
            <script type="text/javascript">
                /* seo-header script */
                jQuery(document).ready(function () {
                    jQuery("#seo-header-wrap > dd").bind("mouseenter", function () {
                        jQuery("#seo-header-wrap > dd").removeAttr('style');
                        jQuery(".main-nav-wraps").dequeue().hide();
                        if (jQuery(this).hasClass("shopping")) {
                            jQuery("#main-nav-shopping").css({
                                'position': 'absolute',
                                'top': jQuery(this).position().top + 40,
                                'left': jQuery(this).position().left - 192
                            });
                        } else if (jQuery(this).hasClass("health")) {
                            jQuery("#main-nav-health").css({
                                'position': 'absolute',
                                'top': jQuery(this).position().top + 40,
                                'left': jQuery(this).position().left - 208
                            });
                        } else {
                            jQuery("#main-nav-" + this.className.split(' ')[0]).css({
                                'position': 'absolute',
                                'top': jQuery(this).position().top + 40,
                                'left': jQuery(this).position().left
                            });
                        }
                        jQuery("#main-nav-" + this.className.split(' ')[0]).css({opacity: 0, display: "block"});
                        jQuery(this).css({backgroundPosition: "0 -46px"});
                        jQuery("#main-nav-" + this.className.split(' ')[0]).animate({queue: false, opacity: 0.95});
                    });
                    jQuery("#main_nav").bind("mouseleave", function () {
                        jQuery(".main-nav-wraps").dequeue().hide();
                        jQuery("#main-nav-" + this.className.split(' ')[0]).show().animate({
                            queue: false,
                            opacity: 0
                        }, 200, function () {
                            jQuery("#main-nav-" + this.className.split(' ')[0]).css('display', "none");
                        });
                        jQuery("#seo-header-wrap > dd").removeAttr('style');
                    });
                    jQuery(".nav-close-button").bind("click", function () {
                        jQuery(".main-nav-wraps").dequeue().hide();
                    });
                });
            </script>
            <div class="grid_16">
                <div id="main_nav">
                    <dl id="seo-header-wrap">
                        <dd class="cars">
                            <div class="cars parbase image mainNavCategory">
                                <a id="main-nav-cars-link" href="http://www.consumerreports.org/cro/cars/index.htm"></a>
                            </div>
                        </dd>
                        <dd class="appliances on">
                            <div class="appliances parbase image mainNavCategory">
                                <a id="main-nav-appliances-link"
                                   href="http://www.consumerreports.org/cro/appliances/index.htm"></a>
                            </div>
                        </dd>
                        <dd class="electronics">
                            <div class="electronics parbase image mainNavCategory">
                                <a id="main-nav-electronics-link"
                                   href="http://www.consumerreports.org/cro/electronics-computers/index.htm"></a>
                            </div>
                        </dd>
                        <dd class="home">
                            <div class="home parbase image mainNavCategory">
                                <a id="main-nav-home-link"
                                   href="http://www.consumerreports.org/cro/home-garden/index.htm"></a>
                            </div>
                        </dd>
                        <dd class="babies">
                            <div class="babies parbase image mainNavCategory">
                                <a id="main-nav-babies-link"
                                   href="http://www.consumerreports.org/cro/babies-kids/index.htm"></a>
                            </div>
                        </dd>
                        <dd class="money">
                            <div class="money parbase image mainNavCategory">
                                <a id="main-nav-money-link"
                                   href="http://www.consumerreports.org/cro/money/index.htm"></a>
                            </div>
                        </dd>
                        <dd class="shopping">
                            <div class="shopping parbase image mainNavCategory">
                                <a id="main-nav-shopping-link"
                                   href="http://www.consumerreports.org/cro/shopping/index.htm"></a>
                            </div>
                        </dd>
                        <dd class="health">
                            <div class="health parbase image mainNavCategory">
                                <a id="main-nav-health-link"
                                   href="http://www.consumerreports.org/cro/health/index.htm"></a>
                            </div>
                        </dd>
                    </dl>


                    <!-- START MENU DIVS -->
                    <div id="subNavBlocker"></div>

                    <div style="display: none;" id="main-nav-cars" class="main-nav-wraps">
                        <div id="header-dropdown-box-cars">
                            <div class="nav-close-button">
                                <img src="<%= realContext %>/template/cr/close_bttn.gif" alt="" border="0">
                            </div>

                            <div class="products-header">Car Types</div>

                            <div class="left-column">


                                <a href="http://www.consumerreports.org/cro/cars/convertibles.htm">Convertibles</a>


                                <a href="http://www.consumerreports.org/cro/cars/hybridsevs.htm">Hybrids/EVs</a>


                                <a href="http://www.consumerreports.org/cro/cars/luxury-cars.htm">Luxury cars</a>


                                <a href="http://www.consumerreports.org/cro/cars/minivans.htm">Minivans</a>


                                <a href="http://www.consumerreports.org/cro/cars/pickup-trucks.htm">Pickup trucks</a>


                            </div>
                            <div class="right-column">


                                <a href="http://www.consumerreports.org/cro/cars/sedans.htm">Sedans</a>


                                <a href="http://www.consumerreports.org/cro/cars/small-cars.htm">Small cars</a>


                                <a href="http://www.consumerreports.org/cro/cars/sports-cars.htm">Sports cars</a>


                                <a href="http://www.consumerreports.org/cro/cars/suvs.htm">SUVs</a>


                                <a href="http://www.consumerreports.org/cro/cars/wagons.htm">Wagons</a>


                            </div>

                            <div class="see-all-products">
                                <img src="<%= realContext %>/template/cr/white_small_arrows.gif" alt="" border="0">

                                <a class="product-link"
                                   href="http://www.consumerreports.org/cro/cars/new-cars/index.htm">See new cars</a>

                                &nbsp;&nbsp;|&nbsp;&nbsp;

                                <a class="product-link"
                                   href="http://www.consumerreports.org/cro/cars/used-cars/index.htm">used cars</a>

                                &nbsp;&nbsp;|&nbsp;&nbsp;

                                <a class="product-link" href="http://www.consumerreports.org/cro/cars/index.htm">all
                                    cars</a>

                            </div>

                            <div class="botborder">&nbsp;</div>

                            <div class="products-header">Tires &amp; Car Care</div>

                            <div class="left-column">


                                <a href="http://www.consumerreports.org/cro/car-batteries.htm">Car batteries</a>


                                <a href="http://www.consumerreports.org/cro/gps.htm">GPS</a>


                            </div>
                            <div class="right-column">


                                <a href="http://www.consumerreports.org/cro/tires.htm">Tires</a>


                            </div>

                            <div class="botborder">&nbsp;</div>

                            <div class="products-header">Topics</div>

                            <div class="left-column">


                                <a href="http://news.consumerreports.org/cars/">Cars news</a>


                                <a href="http://www.consumerreports.org/cro/cars/prices/index.htm">Car prices &amp;
                                    deals</a>


                            </div>
                            <div class="right-column">


                                <a href="http://www.consumerreports.org/cro/cars/buying-advice/index.htm">Car buying
                                    advice</a>


                                <a href="http://www.consumerreports.org/cro/cars/safety-recalls/index.htm">Car
                                    safety</a>


                            </div>

                            <div class="botborder">&nbsp;</div>


                        </div>
                    </div>

                    <div style="display: none;" id="main-nav-appliances" class="main-nav-wraps">
                        <div id="header-dropdown-box">
                            <div class="nav-close-button">
                                <img src="<%= realContext %>/template/cr/close_bttn.gif" alt="" border="0">
                            </div>

                            <div class="products-header">Top Products</div>

                            <div class="left-column">


                                <a href="http://www.consumerreports.org/cro/air-conditioners.htm">Air conditioners</a>


                                <a href="http://www.consumerreports.org/cro/air-purifiers.htm">Air purifiers</a>


                                <a href="http://www.consumerreports.org/cro/blenders.htm">Blenders</a>


                                <a href="http://www.consumerreports.org/cro/clothes-dryers.htm">Clothes dryers</a>


                                <a href="http://www.consumerreports.org/cro/coffeemakers.htm">Coffeemakers</a>


                                <a href="http://www.consumerreports.org/cro/cooktops-wall-ovens.htm">Cooktops &amp; wall
                                    ovens</a>


                                <a href="http://www.consumerreports.org/cro/dehumidifiers.htm">Dehumidifiers</a>


                                <a href="http://www.consumerreports.org/cro/dishwashers.htm">Dishwashers</a>


                                <a href="http://www.consumerreports.org/cro/freezers.htm">Freezers</a>


                            </div>
                            <div class="right-column">


                                <a href="http://www.consumerreports.org/cro/microwave-ovens.htm">Microwave ovens</a>


                                <a href="http://www.consumerreports.org/cro/kitchen-ranges.htm">Kitchen ranges</a>


                                <a href="http://www.consumerreports.org/cro/refrigerators.htm">Refrigerators</a>


                                <a href="http://www.consumerreports.org/cro/sewing-machines.htm">Sewing machines</a>


                                <a href="http://www.consumerreports.org/cro/space-heaters.htm">Space heaters</a>


                                <a href="http://www.consumerreports.org/cro/steam-irons.htm">Steam irons</a>


                                <a href="http://www.consumerreports.org/cro/toasters.htm">Toasters</a>


                                <a href="http://www.consumerreports.org/cro/vacuum-cleaners.htm">Vacuum cleaners</a>


                                <a href="http://www.consumerreports.org/cro/washing-machines.htm">Washing machines</a>


                            </div>

                            <div class="see-all-products">
                                <img src="<%= realContext %>/template/cr/white_small_arrows.gif" alt="" border="0">

                                <a class="product-link" href="http://www.consumerreports.org/cro/appliances/index.htm">See
                                    all products</a>

                            </div>

                            <div class="botborder">&nbsp;</div>

                            <div class="products-header">Topics</div>

                            <div class="left-column">


                                <a href="http://news.consumerreports.org/home/">Home news</a>


                            </div>
                            <div class="right-column">

                            </div>


                        </div>
                    </div>

                    <div style="display: none;" id="main-nav-electronics" class="main-nav-wraps">
                        <div id="header-dropdown-box">
                            <div class="nav-close-button">
                                <img src="<%= realContext %>/template/cr/close_bttn.gif" alt="" border="0">
                            </div>

                            <div class="products-header">Top Products</div>

                            <div class="left-column">


                                <a href="http://www.consumerreports.org/cro/camcorders.htm">Camcorders</a>


                                <a href="http://www.consumerreports.org/cro/cell-phones-services.htm">Cell phones &amp;
                                    services</a>


                                <a href="http://www.consumerreports.org/cro/computers.htm">Computers</a>


                                <a href="http://www.consumerreports.org/cro/cordless-phones.htm">Cordless phones</a>


                                <a href="http://www.consumerreports.org/cro/digital-cameras.htm">Digital cameras</a>


                                <a href="http://www.consumerreports.org/cro/digital-picture-frames.htm">Digital picture
                                    frames</a>


                                <a href="http://www.consumerreports.org/cro/blu-ray-and-dvd-players.htm">Blu-ray
                                    players</a>


                            </div>
                            <div class="right-column">


                                <a href="http://www.consumerreports.org/cro/gps.htm">GPS</a>


                                <a href="http://www.consumerreports.org/cro/headphones.htm">Headphones</a>


                                <a href="http://www.consumerreports.org/cro/home-theater-systems.htm">Home theater
                                    systems</a>


                                <a href="http://www.consumerreports.org/cro/mp3-players.htm">MP3 Players</a>


                                <a href="http://www.consumerreports.org/cro/printers.htm">Printers</a>


                                <a href="http://www.consumerreports.org/cro/tvs.htm">TVs</a>


                            </div>

                            <div class="see-all-products">
                                <img src="<%= realContext %>/template/cr/white_small_arrows.gif" alt="" border="0">

                                <a class="product-link"
                                   href="http://www.consumerreports.org/cro/electronics-computers/index.htm">See all
                                    products</a>

                            </div>

                            <div class="botborder">&nbsp;</div>

                            <div class="products-header">Topics</div>

                            <div class="left-column">


                                <a href="http://news.consumerreports.org/electronics/">Electronics news</a>


                            </div>
                            <div class="right-column">

                            </div>


                        </div>
                    </div>

                    <div style="display: none;" id="main-nav-home" class="main-nav-wraps">
                        <div id="header-dropdown-box">
                            <div class="nav-close-button">
                                <img src="<%= realContext %>/template/cr/close_bttn.gif" alt="" border="0">
                            </div>

                            <div class="products-header">Top Products</div>

                            <div class="left-column">


                                <a href="http://www.consumerreports.org/cro/cordless-drills-tool-kits.htm">Cordless
                                    drills</a>


                                <a href="http://www.consumerreports.org/cro/flooring.htm">Flooring</a>


                                <a href="http://www.consumerreports.org/cro/gas-grills.htm">Gas grills</a>


                                <a href="http://www.consumerreports.org/cro/kitchen-cookware.htm">Kitchen cookware</a>


                                <a href="http://www.consumerreports.org/cro/lawn-mowers.htm">Lawn mowers &amp;
                                    tractors</a>


                            </div>
                            <div class="right-column">


                                <a href="http://www.consumerreports.org/cro/leaf-blowers.htm">Leaf blowers</a>


                                <a href="http://www.consumerreports.org/cro/mattresses.htm">Mattresses</a>


                                <a href="http://www.consumerreports.org/cro/snow-blowers.htm">Snow blowers</a>


                                <a href="http://www.consumerreports.org/cro/string-trimmers.htm">String trimmers</a>


                                <a href="http://www.consumerreports.org/cro/water-filters.htm">Water filters</a>


                            </div>

                            <div class="see-all-products">
                                <img src="<%= realContext %>/template/cr/white_small_arrows.gif" alt="" border="0">

                                <a class="product-link" href="http://www.consumerreports.org/cro/home-garden/index.htm">See
                                    all products</a>

                            </div>

                            <div class="botborder">&nbsp;</div>

                            <div class="products-header">Topics</div>

                            <div class="left-column">

                                <div class="last">


                                    <a href="http://news.consumerreports.org/home/">Home news</a>


                                </div>

                            </div>
                            <div class="right-column">

                                <div class="last">


                                    <a href="http://news.consumerreports.org/safety/">Safety news</a>


                                </div>

                            </div>


                        </div>
                    </div>

                    <div style="display: none; position: absolute; top: 171px; left: 609px; opacity: 0.95;"
                         id="main-nav-babies"
                         class="main-nav-wraps">
                        <div id="header-dropdown-box">
                            <div class="nav-close-button">
                                <img src="<%= realContext %>/template/cr/close_bttn.gif" alt="" border="0">
                            </div>

                            <div class="products-header">Top Products</div>

                            <div class="left-column">


                                <a href="http://www.consumerreports.org/cro/baby-monitors.htm">Baby monitors</a>


                                <a href="http://www.consumerreports.org/cro/backpacks.htm">Backpacks</a>


                                <a href="http://www.consumerreports.org/cro/car-seats.htm">Car seats</a>


                                <a href="http://www.consumerreports.org/cro/cribs.htm">Cribs</a>


                            </div>
                            <div class="right-column">


                                <a href="http://www.consumerreports.org/cro/high-chairs.htm">High chairs</a>


                                <a href="http://www.consumerreports.org/cro/play-yards.htm">Play yards</a>


                                <a href="http://www.consumerreports.org/cro/strollers.htm">Strollers</a>


                                <a href="http://www.consumerreports.org/cro/thermometers.htm">Thermometers</a>


                            </div>

                            <div class="see-all-products">
                                <img src="<%= realContext %>/template/cr/white_small_arrows.gif" alt="" border="0">

                                <a class="product-link" href="http://www.consumerreports.org/cro/babies-kids/index.htm">See
                                    all products</a>

                            </div>

                            <div class="botborder">&nbsp;</div>

                            <div class="products-header">Topics</div>

                            <div class="left-column">

                                <div class="last">


                                    <a href="http://news.consumerreports.org/baby/">Babies &amp; Kids news</a>


                                </div>

                            </div>
                            <div class="right-column">

                                <div class="last">


                                    <a href="http://news.consumerreports.org/safety/">Safety news</a>


                                </div>

                            </div>


                        </div>
                    </div>

                    <div style="display: none;" id="main-nav-shopping" class="main-nav-wraps">
                        <div id="header-dropdown-box">
                            <div class="nav-close-button">
                                <img src="<%= realContext %>/template/cr/close_bttn.gif" alt="" border="0">
                            </div>

                            <div class="products-header">Top Products</div>

                            <div class="left-column">


                                <a href="http://www.consumerreports.org/cro/electronics-computers/audio-video/blu-ray-and-dvd-players/blu-ray-and-dvd-player-price-and-shop/blu-ray-player.htm">Buy
                                    Blu-ray players</a>


                                <a href="http://www.consumerreports.org/cro/electronics-computers/audio-video/camcorders/camcorder-price-and-shop/camcorder.htm">Buy
                                    camcorders</a>


                                <a href="http://www.consumerreports.org/cro/babies-kids/baby-toddler/car-seats/car-seat-price-and-shop/infant-car-seat.htm">Buy
                                    car seats</a>


                                <a href="http://www.consumerreports.org/cro/appliances/laundry-and-cleaning/clothes-dryers/clothes-dryer-price-and-shop/electric-dryer.htm">Buy
                                    clothes dryers</a>


                                <a href="http://www.consumerreports.org/cro/appliances/kitchen-appliances/coffeemakers/coffeemaker-price-and-shop/drip-coffeemaker.htm">Buy
                                    coffeemakers</a>


                                <a href="http://www.consumerreports.org/cro/electronics-computers/computers-internet/computers/computer-price-and-shop/laptop.htm">Buy
                                    computers</a>


                                <a href="http://www.consumerreports.org/cro/electronics-computers/phones-mobile-devices/cordless-phones/cordless-phone-price-and-shop/cordless-phone.htm">Buy
                                    cordless phones</a>


                                <a href="http://www.consumerreports.org/cro/electronics-computers/cameras-photography/digital-cameras/digital-camera-price-and-shop/point-shoot-digital-camera.htm">Buy
                                    digital cameras</a>


                                <a href="http://www.consumerreports.org/cro/appliances/kitchen-appliances/dishwashers/dishwasher-price-and-shop/dishwasher.htm">Buy
                                    dishwashers</a>


                                <a href="http://www.consumerreports.org/cro/cars/tires-auto-parts/gps/gps-price-and-shop/gps.htm">Buy
                                    GPS</a>


                            </div>
                            <div class="right-column">


                                <a href="http://www.consumerreports.org/cro/home-garden/lawn-garden/gas-grills/gas-grill-price-and-shop/gas-grill.htm">Buy
                                    gas grills</a>


                                <a href="http://www.consumerreports.org/cro/home-garden/tools-power-equipment/lawn-mowers/lawn-mower-price-and-shop/push-mower.htm">Buy
                                    lawn mowers</a>


                                <a href="http://www.consumerreports.org/cro/appliances/kitchen-appliances/microwave-ovens/microwave-oven-price-and-shop/countertop-microwave-oven.htm">Buy
                                    microwave ovens</a>


                                <a href="http://www.consumerreports.org/cro/electronics-computers/computers-internet/printers/printer-price-and-shop/all-in-one-printer.htm">Buy
                                    printers</a>


                                <a href="http://www.consumerreports.org/cro/appliances/kitchen-appliances/kitchen-ranges/kitchen-range-price-and-shop/electric-range.htm">Buy
                                    kitchen ranges</a>


                                <a href="http://www.consumerreports.org/cro/appliances/kitchen-appliances/refrigerators/refrigerator-price-and-shop/bottom-freezer-refrigerator.htm">Buy
                                    refrigerators</a>


                                <a href="http://www.consumerreports.org/cro/electronics-computers/tvs-services/tvs/tv-price-and-shop/lcd-tv.htm">Buy
                                    TVs</a>


                                <a href="http://www.consumerreports.org/cro/appliances/laundry-and-cleaning/vacuum-cleaners/vacuum-cleaner-price-and-shop/canister-vacuum-cleaner.htm">Buy
                                    vacuum cleaners</a>


                                <a href="http://www.consumerreports.org/cro/appliances/laundry-and-cleaning/washing-machines/washing-machine-price-and-shop/front-loading-washing-machine.htm">Buy
                                    washing machines</a>


                            </div>

                            <div class="see-all-products">
                                <img src="<%= realContext %>/template/cr/white_small_arrows.gif" alt="" border="0">

                                <a class="product-link" href="http://www.consumerreports.org/cro/shopping/index.htm">See
                                    all products</a>

                            </div>

                            <div class="botborder">&nbsp;</div>


                        </div>
                    </div>

                    <div style="display: none;" id="main-nav-health" class="main-nav-wraps">
                        <div id="header-dropdown-box">
                            <div class="nav-close-button">
                                <img src="<%= realContext %>/template/cr/close_bttn.gif" alt="" border="0">
                            </div>

                            <div class="products-header">Top Products</div>

                            <div class="left-column">


                                <a href="http://www.consumerreports.org/cro/blood-glucose-meters.htm">Blood Glucose
                                    Meters</a>


                                <a href="http://www.consumerreports.org/cro/blood-pressure-monitors.htm">Blood Pressure
                                    Monitors</a>


                                <a href="http://www.consumerreports.org/cro/diet-plans.htm">Diet Plans</a>


                                <a href="http://www.consumerreports.org/cro/toothbrushes.htm">Electric Toothbrushes</a>


                                <a href="http://www.consumerreports.org/cro/ellipticals.htm">Ellipticals</a>


                                <a href="http://www.consumerreports.org/cro/exercise-bikes/buying-guide.htm">Exercise
                                    Bikes</a>


                                <a href="http://www.consumerreports.org/cro/eye-creams/buying-guide.htm">Eye Creams</a>


                                <a href="http://www.consumerreports.org/cro/hair-dryers.htm">Hair Dryers</a>


                            </div>
                            <div class="right-column">


                                <a href="http://www.consumerreports.org/cro/hair-dyes/buying-guide.htm">Hair Dyes</a>


                                <a href="http://www.consumerreports.org/cro/2012/12/hear-well-in-a-noisy-world/index.htm">Hearing
                                    Aids</a>


                                <a href="http://www.consumerreports.org/cro/sunscreens.htm">Sunscreens</a>


                                <a href="http://www.consumerreports.org/cro/magazine-archive/august-2009/home-garden/tooth-whiteners/overview/tooth-whiteners-ov.htm">Tooth
                                    Whiteners</a>


                                <a href="http://www.consumerreports.org/cro/treadmills.htm">Treadmills</a>


                                <a href="http://www.consumerreports.org/cro/athletic-shoes.htm">Walking Shoes</a>


                                <a href="http://www.consumerreports.org/cro/wrinkle-creams/buying-guide.htm">Wrinkle
                                    Creams</a>


                                <a href="http://www.consumerreports.org/cro/wrinkle-creams/buying-guide.htm">Wrinkle
                                    Serums</a>


                            </div>

                            <div class="botborder">&nbsp;</div>

                            <div class="products-header">Topics</div>

                            <div class="left-column">


                                <a href="http://www.consumerreports.org/health/best-buy-drugs/index.htm">Best Buy
                                    Drugs</a>


                                <a href="http://www.consumerreports.org/health/insurance/health-insurance-plans.htm">Health
                                    Insurance Plan
                                    Rankings</a>


                                <a href="http://www.consumerreports.org/health/doctors-hospitals/heart-surgeons.htm">Heart
                                    Surgeon
                                    Ratings</a>


                                <a href="http://www.consumerreports.org/health/doctors-hospitals/hospital-ratings.htm">Hospital
                                    Ratings</a>


                                <a href="http://www.consumerreports.org/cro/health/medical-treatments-conditions/adhd-treatment/index.htm">ADHD</a>


                                <a href="http://www.consumerreports.org/cro/2012/08/relief-from-springtime-allergies/index.htm">Allergies</a>


                                <a href="http://www.consumerreports.org/cro/2013/01/relief-for-your-aching-back/index.htm">Back
                                    Pain</a>


                            </div>
                            <div class="right-column">


                                <a href="http://www.consumerreports.org/cro/2013/01/depression-and-anxiety/index.htm">Depression</a>


                                <a href="http://www.consumerreports.org/cro/health/medical-treatments-conditions/type-2-diabetes/index.htm">Diabetes</a>


                                <a href="http://www.consumerreports.org/cro/health/medical-treatments-conditions/heart-guide/index.htm">Heart
                                    Health</a>


                                <a href="http://www.consumerreports.org/cro/magazine/2012/08/how-did-you-sleep-last-night/index.htm">Insomnia</a>


                                <a href="http://www.consumerreports.org/cro/2013/02/lasik-eye-surgery/index.htm">Lasik
                                    Eye Surgery</a>


                                <a href="http://www.consumerreports.org/cro/2013/01/healthy-sex-his-and-hers/index.htm">Sexual
                                    Health</a>


                            </div>

                            <div class="botborder">&nbsp;</div>


                        </div>
                    </div>

                    <!-- END MENU DIVS -->


                </div>
            </div>
        </div>
    </div>


    <div style="float:left;background-color:#fff;">
        <div class="grid_16">
        </div>
    </div>
</div>
<div id="content" class="container_16">
    <div id="content-body" class="grid_13">
        <div id="questionnaireWidget"><%--#SYS Widget Content--%></div>
    </div>
    <div id="content-right-rail">


        <div class="ad-slot-3">
            <div class="mboxDefault"><a href="https://ec.consumerreports.org/ec/cro/order.htm?INTKEY=I94PMT0"><img
                    src="<%= realContext %>/template/cr/subscribe_banner_180X360_V1.jpg"></a></div>
            <script type="text/javascript"
                    language="JavaScript1.2">mboxCreate('cro_model_ovrw_tested_subscribead');</script>
        </div>


        <div class="see-models">
            <div class="header">See all Models</div>
            <div class="links">
                <a href="http://www.consumerreports.org/cro/appliances/kitchen-appliances/dishwashers/dishwasher-recommendations/dishwasher.htm">Recommended</a>
                <a href="http://www.consumerreports.org/cro/appliances/kitchen-appliances/dishwashers/dishwasher-ratings/ratings-overview.htm">Ratings</a>
            </div>
        </div>
        <div class="shopping-behind-ratings">
            <dl>
                <dt>
                <div class="mainhead">Nobody Tests Like <br>We Do</div>
                </dt>
                <dd class="video">
                    <a href="http://www.consumerreports.org/cro/video-hub/miscellaneous/about-us/no-one-tests-like-we-do/35157223001/32326377001/"><img
                            src="<%= realContext %>/template/cr/shop_inside_test.jpg" border="0" height="87"
                            width="149"></a>
                </dd>
                <dd class="subhead">Our testers put 100s of products through their
                    paces at our National Testing and Research Center. Learn more about how
                    we test for:
                </dd>
                <dd class="divider">
                    <div class="line"></div>
                </dd>
                <dd class="bullet-list">Performance</dd>
                <dd class="bullet-list">Safety</dd>
                <dd class="bullet-list">Reliability</dd>
                <dd class="bullet-list-arrow"><a href="http://www.consumerreports.org/cro/aboutus/test/index.htm">Learn
                    more</a></dd>
            </dl>
        </div>


        <div class="ad-slot-4">
            <div class="mboxDefault"><a href="https://ec.consumerreports.org/ec/cro/order.htm?INTKEY=I94PMT0"><img
                    src="<%= realContext %>/template/cr/see_other_models_banner_180X282_V1.jpg"></a></div>
            <script type="text/javascript"
                    language="JavaScript1.2">mboxCreate('cro_model_ovrw_tested_othermodels');</script>
        </div>

    </div>
</div>
</div>


<div class="footer parsys">
    <div id="homepage-footer-container">
        <div id="homepage-footer">
            <div id="testing">
                <div class="buttons">
                    <a href="http://www.consumerreports.org/cro/2013/02/report-a-problem-product/index.htm">
                        <img alt="Report a Safety Problem"
                             src="<%= realContext %>/template/cr/b_footer_report_problem.png">
                    </a>
                </div>
                <div class="buttons">
                    <a href="https://consumerreports.secure-donor.com/consumerreports?source=3027222227">
                        <img alt="Donate" src="<%= realContext %>/template/cr/b_footer_donate.png">
                    </a>
                </div>
                <div id="testing-links" class="links">
                    <ul class="column-1">
                        <div class="footerC1">
                            <li>
                                <a href="http://www.consumerreports.org/cro/about-us/whats-behind-the-ratings/testing/index.htm">
                                    How we test </a></li>
                            <li><a href="http://www.consumerreports.org/cro/about-us/our-mission/index.htm"> Our
                                mission </a></li>
                            <li><a href="http://www.consumerreports.org/cro/about-us/history/index.htm"> Our
                                history </a></li>
                            <li><a href="http://www.consumerreports.org/cro/aboutus/labtour/index.htm"> Lab tour </a>
                            </li>
                        </div>
                    </ul>
                    <ul class="column-2">
                        <div class="footerC2">
                            <li><a href="http://www.consumerreports.org/cro/about-us/index.htm"> About Us </a></li>
                            <li><a href="http://pressroom.consumerreports.org/"> Press Room </a></li>
                            <li>
                                <a href="http://www.consumerreports.org/cro/about-us/no-commerical-use-policy/index.htm">
                                    No commercial use policy </a></li>
                            <li><a href="https://jobs-consumers.icims.com/jobs/intro"> Career Opportunities </a></li>
                        </div>
                    </ul>
                </div>
            </div>
            <div id="cr-links">
                <div id="cr-links-column-1" class="links">
                    <div class="footerL1">
                        <h3> ConsumerReports.org </h3>
                        <ul>
                            <li>
                                <a href="http://custhelp.consumerreports.org/cgi-bin/consumerreports.cfg/php/enduser/std_adp.php?p_faqid=264">
                                    Contact Us </a></li>
                            <li><a href="https://ec.consumerreports.org/ec/myaccount/main.htm"> My Account </a></li>
                            <li><a href="http://www.consumerreports.org/cro/a-to-z-index/products/index.htm"> A-Z
                                Index </a></li>
                            <li><a href="http://www.consumerreports.org/cro/a/cars/index.htm"> Car Index </a></li>
                            <li><a href="http://www.consumerreports.org/cro/a/products/index.htm"> Product Index </a>
                            </li>
                            <li><a href="http://www.consumerreports.org/cro/customer-service/privacy/index.htm"> Your
                                Privacy Rights </a></li>
                            <li>
                                <script src="<%= realContext %>/template/cr/js" type="text/javascript"></script>
                                <a style="cursor:pointer" onclick="TRUSTeWidget.Tab.link()">Ad Choices</a></li>
                            <li><a href="http://www.consumerreports.org/cro/customer-service/user-agreement/index.htm">
                                User Agreement </a></li>
                            <li><a href="http://web.consumerreports.org/mobile/index.htm"> Mobile Products </a></li>
                            <li><a href="http://m.consumerreports.org/"> View Mobile Web Site </a></li>
                        </ul>
                    </div>
                </div>
                <img class="divider" alt="divider" src="<%= realContext %>/template/cr/home_footer_vertical_divider.png"
                     height="334" width="2">

                <div id="cr-links-column-2" class="links">
                    <div class="footerL2">
                        <h3> Consumer Reports Network </h3>
                        <ul>
                            <li><a href="http://www.consumerreports.org/cro/index.htm"> ConsumerReports.org </a></li>
                            <li><a href="http://consumerist.com/"> The Consumerist </a></li>
                            <li><a href="http://www.consumerreports.org/cro/cars/car-prices/index.htm"> Cars Best Deals
                                Plus </a></li>
                            <li><a href="https://ec.consumerreports.org/ec/aps/order.htm?INTKEY=I0AH0L9"> Car Pricing
                                Service </a></li>
                            <li><a href="http://www.consumersunion.org/"> ConsumersUnion.org </a></li>
                            <li><a href="http://www.consumerreports.org/cro/canada-extra/index.htm"> Canada Extra </a>
                            </li>
                            <li><a href="http://espanol.consumerreports.org/"> en Español </a></li>
                            <li><a href="http://clickcheckandprotect.org/"> School Safety Alert </a></li>
                            <li><a href="http://www.consumerreports.org/cro/book-store/products-and-services/index.htm">
                                Consumer Reports Bookstore </a></li>
                            <li><a href="http://shopsmartmag.org/"> ShopSmartMag.org </a></li>
                        </ul>
                    </div>
                </div>
            </div>
            <div class="disclaimer">
                <div id="copyright">
                    <p><span class="Apple-style-span"
                             style="font-family: Arial, Helvetica, sans-serif; font-size: 10px; color: rgb(56, 69, 82);">Copyright © 2006-2013&nbsp;<a
                            style="text-decoration: underline; color: rgb(56, 69, 82); font: normal normal normal 10px/normal Arial, Helvetica, sans-serif; line-height: 12px;"></a><a
                            href="http://www.consumerreports.org/">Consumer Reports</a>. No reproduction, in whole or in part, without written&nbsp;<a
                            style="text-decoration: underline; color: rgb(56, 69, 82); font: normal normal normal 10px/normal Arial, Helvetica, sans-serif; line-height: 12px;"></a><a
                            href="http://www.consumerreports.org/cro/book-store/additional-information/permission-requests/index.htm">permission</a>.</span>
                    </p>
                </div>
            </div>
        </div>
    </div>
</div>


<div class="mboxDefault"></div>
<script type="text/javascript">mboxCreate('global', 'pageType=model', 'payType=free');</script>


<script language="javascript" type="text/javascript">
    function loadScript(url, callback) {

        var script = document.createElement("script")
        script.type = "text/javascript";

        if (script.readyState) {  //IE
            script.onreadystatechange = function () {
                if (script.readyState == "loaded" ||
                        script.readyState == "complete") {
                    script.onreadystatechange = null;
                    callback();
                }
            };
        } else {  //Others
            script.onload = function () {
                callback();
            };
        }

        script.src = url;
        document.getElementsByTagName("head")[0].appendChild(script);
    }


</script>

<!-- SiteCatalyst code version: H.21.1
Copyright 1996-2010 Adobe, Inc. All Rights Reserved
More info available at http://www.omniture.com -->
<script language="JavaScript" type="text/javascript">
    var s_account = "cuconsumer,cuglobal"
</script>
<script language="JavaScript" type="text/javascript" src="<%= realContext %>/template/cr/s_code.js"></script>
<script language="JavaScript" type="text/javascript">
    (function () {
        var old = s.ot;
        s.ot = function (el) {
            return el.tagUrn ? '' : old(el);
        };
    })();

    s.channel = "cro"

    s.pageName = "CRO:Appliances:Dishwashers:Jenn-airJDB3200AW[W]:Overview"


    s.prop1 = "Appliances";

    s.prop2 = "Dishwashers";

    s.prop3 = "Jenn-airJDB3200AW[W]";

    s.prop4 = "Overview";


    s.prop16 = "visitor"


    s.prop17 = "model";


    s.prop30 = "Appliances";


    s.eVar31 = "Appliances";


    s.prop31 = "Tested";


    s.prop32 = "99030215";


    s.prop33 = "Dishwashers";


    s.prop34 = "ConventionalDishwashers";


    s.prop9 = "free";


    if (typeof jQuery == 'undefined') {
        loadScript("/cro/shared-resources/scripts/jquery/jquery-1.3.2.js", loadQueryCode);
    } else {
        loadQueryCode();
    }

    function loadQueryCode() {
        if (typeof jQuery != 'undefined') {
            jQuery.expr[':'].textEquals = function (a, i, m) {
                return jQuery(a).text().match("^" + m[3] + "$");
            };
            jQuery('img[src*="print_nodots"]').bind('click', function () {
                var parent = jQuery(this).parent();
                updateEventToSC('print', parent);
            });

            jQuery('img[src*="send_nodots"]').bind('click', function () {
                var parent = jQuery(this).parent();
                updateEventToSC('email', parent);
            });

            jQuery('a:textEquals("Print"),img[src*="icon_print"],a:.icon-print').bind('click', function () {
                if (jQuery(this).attr("tagName").toLowerCase() == "img")
                    updateEventToSC('print', jQuery(this).parent());
                else
                    updateEventToSC('print', this);
            });
            jQuery('a:textEquals("Email"),img[src*="icon_email"],a:.icon-email').bind('click', function () {
                if (jQuery(this).attr("tagName").toLowerCase() == "img")
                    updateEventToSC('email', jQuery(this).parent());
                else
                    updateEventToSC('email', this);
            });

            if ((typeof jQuery.cookie == 'function') && ('true' == jQuery.cookie('siteCatalystLoginSuccess'))) {

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
        }
    }
    function updateEventToSC(event, element) {
        var s = s_gi("cuconsumer,cuglobal");
        s.linkTrackVars = 'events,eVar5';
        s.linkTrackEvents = 'event3';
        s.eVar5 = event;
        s.events = 'event3';
        s.tl(element, 'o', event);
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
        var s = s_gi("cuconsumer,cuglobal");
        for (var key in scvars) {
            if (key != undefined && key != 'undefined') {
                s[key] = scvars[key];
            }
        }
        s.t();
    }


    function updateToSC() {
        var siteCatalystProductList = "";
        if (typeof theComparator != "undefined" && theComparator != null) {
            if (location.href.indexOf("/insurance/NCQA-rankings-compare-plans.htm") != -1) {
                if (theComparator.getProducts() != null && theComparator.getProducts().getKeys() != null) {
                    var theCompareArray = theComparator.getProducts().getKeys();
                    if (theCompareArray.length > 0) {
                        for (i = 0; (i < theCompareArray.length); i++) {
                            siteCatalystProductList += ";" + itsStateName + "/" + itsPlanCategory + "/" + theCompareArray[i] + ((i < theCompareArray.length - 1) ? "," : "");
                        }
                    }
                }
            } else if (location.href.indexOf("/ratings/compare-hospitals.htm") != -1) {
                if (theComparator.getProducts() != null && theComparator.getProducts().getKeys() != null) {
                    var theCompareArray = theComparator.getProducts().getKeys();
                    if (theCompareArray.length > 0) {
                        for (i = 0; (i < theCompareArray.length); i++) {
                            siteCatalystProductList += ";" + theCompareArray[i] + ((i < theCompareArray.length - 1) ? "," : "");
                        }
                    }
                }
            }
        }
        if (siteCatalystProductList != "") {
            var s = s_gi("cuconsumer,cuglobal");
            s.linkTrackVars = 'events,eVar6';
            s.linkTrackEvents = 'event9';
            s.eVar6 = 'compare';
            s.events = 'event9';
            s.products = siteCatalystProductList;
            s.t();
        }

    }


    /************* DO NOT ALTER ANYTHING BELOW THIS LINE ! **************/
    var s_code = s.t();
    if (s_code)document.write(s_code)
</script>


<div aria-labelledby="ui-dialog-title-sign-in-menu-link" role="dialog" tabindex="-1"
     class="ui-dialog no-title ui-draggable ui-resizable"
     style="display: none; z-index: 1000; outline: 0px none; position: relative;">
    <div class="ui-dialog-content" id="sign-in-menu-link" style="">
        <div class="dialog" id="sign-in-menu-openned-dialog">
            <div class="content">
                <div class="t"></div>
                <div style="padding-top: 10px;">
                    <div class="sign-in-btn-left">&nbsp;</div>
                    <div class="sign-in-btn-body-lock">
                        <span>Sign In</span>
                    </div>
                    <div class="sign-in-btn-right">&nbsp;</div>
                    <div class="clear"></div>
                    <form action="https://ec.consumerreports.org/ec/cro/login.htm" id="sign-in-form" method="post"
                          name="login">
                        <div>
                            <label id="sign-in-username">Username</label>
                            <input name="userName" onkeydown="addInputSubmitEvent(event)" class="sign-in-input"
                                   type="text"><br>
                            <label id="sign-in-password">Password</label>
                            <input name="password" onkeydown="addInputSubmitEvent(event)" class="sign-in-input"
                                   type="password">

                            <div id="sign-in-bottom">
                                <input id="sign-in-btn" alt="Sign In"
                                       src="<%= realContext %>/template/cr/b_signin_signin.png" type="image">
                                <input class="ifAutoLogin" id="sign-in-check-box" name="setAutoLogin" type="checkbox">
                                <label class="remember-text">Remember Me</label>

                                <div id="sign-in-request-links">
                                    <a class="sign-in-recover-links"
                                       href="https://ec.consumerreports.org/ec/myaccount/forgot_username.htm">Forgot
                                        username?</a><br>
                                    <a class="sign-in-recover-links"
                                       href="https://ec.consumerreports.org/ec/myaccount/forgot_password.htm">Forgot
                                        password?</a>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
            <div class="b">
                <div style="height: 15px;"></div>
            </div>
            <img onclick="jQuery('#sign-in-menu-link').dialog('close');" class="sign-in-close-btn" alt="close"
                 src="<%= realContext %>/template/cr/b_close.gif">
        </div>
    </div>
    <div class="ui-resizable-handle ui-resizable-n"></div>
    <div class="ui-resizable-handle ui-resizable-e"></div>
    <div class="ui-resizable-handle ui-resizable-s"></div>
    <div class="ui-resizable-handle ui-resizable-w"></div>
    <div style="z-index: 1001;"
         class="ui-resizable-handle ui-resizable-se ui-icon ui-icon-gripsmall-diagonal-se ui-icon-grip-diagonal-se"></div>
    <div style="z-index: 1002;" class="ui-resizable-handle ui-resizable-sw"></div>
    <div style="z-index: 1003;" class="ui-resizable-handle ui-resizable-ne"></div>
    <div style="z-index: 1004;" class="ui-resizable-handle ui-resizable-nw"></div>
</div>
</body>
</html>
