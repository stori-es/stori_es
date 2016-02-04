<%-- Must be included in the header of the tracked page --%>
<%-- https://developers.google.com/analytics/devguides/collection/gajs/ --%>

<%@page import="org.consumersunion.stories.server.helper.AnalyticsProperties,
                org.springframework.context.ApplicationContext,
                org.springframework.web.context.support.WebApplicationContextUtils" %>
<%

    ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(session.getServletContext());

    final AnalyticsProperties conf = (AnalyticsProperties) ctx.getBean("analyticsProperties");
    if (!conf.isDisabled()) {
%>

<script type="text/javascript">
    var _gaq = _gaq || [];
    _gaq.push(['_setAccount', '<%= conf.getApiKey()%>']);
    _gaq.push(['_trackPageview']);
    _gaq.push(['_trackPageLoadTime']);

    <% if(conf.isDisabled()) { %>
    window['ga-disable-<%= conf.getApiKey()%>'] = true;
    <% } %>

    (function () {
        var ga = document.createElement('script');
        ga.type = 'text/javascript';
        ga.async = true;
        ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
        var s = document.getElementsByTagName('script')[0];
        s.parentNode.insertBefore(ga, s);
    })();
</script>
<% } // close the analytics disabled check %>
