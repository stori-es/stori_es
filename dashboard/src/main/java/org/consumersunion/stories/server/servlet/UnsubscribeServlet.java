package org.consumersunion.stories.server.servlet;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.consumersunion.stories.common.shared.model.Collection;
import org.consumersunion.stories.common.shared.model.NotificationTrigger;
import org.consumersunion.stories.common.shared.model.Subscription;
import org.consumersunion.stories.server.persistence.CollectionPersister;
import org.consumersunion.stories.server.persistence.SubscriptionPersister;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;

@Component
public class UnsubscribeServlet extends SpringServlet {
    private static final Logger LOGGER = Logger.getLogger(VerifyContactServlet.class.getName());

    @Inject
    private SubscriptionPersister subscriptionPersister;
    @Inject
    private CollectionPersister collectionPersister;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String collectionIdS = request.getParameter("collection");
        String profileIdS = request.getParameter("profile");

        RequestDispatcher reqDispacher;

        if (Strings.isNullOrEmpty(collectionIdS) || Strings.isNullOrEmpty(profileIdS)) {
            reqDispacher = get400Dispatcher();
        } else {
            String confirmation = request.getParameter("confirmed");

            try {
                int collectionId = Integer.parseInt(collectionIdS);
                int profileId = Integer.parseInt(profileIdS);

                Collection collection = collectionPersister.get(collectionId);

                if (collection != null) {
                    request.setAttribute("collectionTitle", collection.getTitle());

                    if (confirmation != null && "1".equals(confirmation)) {
                        subscriptionPersister.setInactive(Subscription.builder()
                                .withType(NotificationTrigger.STORY_ADDED)
                                .withTarget(collectionId)
                                .withProfile(profileId)
                                .build());

                        reqDispacher = getServletContext().getRequestDispatcher("/unsubscribeConfirm.jsp");
                    } else {
                        reqDispacher = getServletContext().getRequestDispatcher("/unsubscribeContact.jsp");
                    }
                } else {
                    reqDispacher = get404Dispatcher();
                }
            } catch (Exception e) {
                LOGGER.log(Level.INFO, e.getMessage(), e);
                reqDispacher = get400Dispatcher();
            }
        }

        reqDispacher.forward(request, response);
    }

    private RequestDispatcher get400Dispatcher() {
        return getServletContext().getRequestDispatcher("/400page.jsp");
    }

    private RequestDispatcher get404Dispatcher() {
        return getServletContext().getRequestDispatcher("/404page.jsp");
    }
}
