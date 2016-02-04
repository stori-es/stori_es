package org.consumersunion.stories.service.client;

/*import static org.junit.Assert.assertEquals;

import javax.servlet.http.HttpServletResponse;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.webapp.WebAppContext;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;*/

public class LoginTest {

    //private static Server server;

    // @BeforeClass
    // public static void setUp() {
    // if(service == null) {
    // try {
    //
    // service = new Server(9090);
    // service.setStopAtShutdown(false);
    //
    // WebAppContext context = new WebAppContext();
    // context.setDescriptor(context + "/WEB-INF/web.xml");
    // context.setResourceBase("site");
    // context.setContextPath("/");
    // context.setAttribute("webContext", context);
    //
    // service.addHandler(context);
    //
    // service.start();
    // service.join();
    //
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // }
    // }
    //
    // @Test
    // public void testLoadLoginPage(){
    // WebClient client = new WebClient();
    // client.setThrowExceptionOnFailingStatusCode(false);
    // Page loginPage;
    // try {
    // loginPage = client.getPage("http://127.0.0.1:9090/signin.jsp");
    // assertEquals("Bad loginPage answer response", HttpServletResponse.SC_OK,
    // loginPage.getWebResponse().getStatusCode());
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    //
    // }
    //
    //
    // @AfterClass
    // public static void tearDown() {
    // try {
    // service.stop();
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // }
}
