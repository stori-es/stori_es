package org.consumersunion.stories.server.servlet;

import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

@Component
public final class QuestionnaireServlet extends SpringServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String url = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");

        if (url.contains("/share/free-credit-score-")) {
            response.sendRedirect("https://action.consumerreports.org/creditscore20180124story");
        } else if (url.contains("/share/what-the-fee-travel-")) {
            response.sendRedirect("https://action.consumerreports.org/financefeesair20180124story");
        } else if (url.contains("/share/cfpb-stories-v2")) {
            response.sendRedirect("https://action.consumerreports.org/cfpb20180124story");
        } else if (url.contains("/share/fuel-economy-standards-2016")) {
            response.sendRedirect("https://action.consumerreports.org/fueleconstandards20180124story");
        } else if (url.contains("/share/electric-vehicle-stories-2017-2018")) {
            response.sendRedirect("https://action.consumerreports.org/electricvehicles20180124story");
        } else {
            response.sendRedirect("https://action.consumerreports.org/takeaction");
        }
    }
}
