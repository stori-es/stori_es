package org.consumersunion.stories.server.servlet;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.consumersunion.stories.server.helper.geo.FullGeoCoder;
import org.consumersunion.stories.server.util.ApplicationContextProvider;
import org.springframework.context.ApplicationContext;

public class GeoCodeServlet extends HttpServlet {
    private final static Logger logger = Logger.getLogger(GeoCodeServlet.class.getName());
    private static final Integer DEFAULT_QUOTA = 1000;

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException,
            IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
        ApplicationContext context = ApplicationContextProvider.getApplicationContext();

        try {
            logger.log(Level.INFO, "GeoCoding Addresses...");
            FullGeoCoder geoCoder = context.getBean(FullGeoCoder.class);
            geoCoder.geoCodeAll(DEFAULT_QUOTA);
            logger.log(Level.INFO, "GeoCoding Finised...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
