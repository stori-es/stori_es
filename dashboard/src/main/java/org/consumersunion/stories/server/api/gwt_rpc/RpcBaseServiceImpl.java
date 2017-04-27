package org.consumersunion.stories.server.api.gwt_rpc;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.consumersunion.stories.common.shared.model.User;
import org.consumersunion.stories.server.business_logic.AuthorizationService;
import org.consumersunion.stories.server.business_logic.UserService;
import org.consumersunion.stories.server.persistence.PersistenceService;
import org.consumersunion.stories.server.index.IndexerFactory;
import org.consumersunion.stories.server.util.ApplicationContextProvider;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public abstract class RpcBaseServiceImpl extends RemoteServiceServlet {
    @Inject
    protected AuthorizationService authService;
    @Inject
    protected UserService userService;
    @Inject
    protected IndexerFactory indexerFactory;
    @Inject
    protected PersistenceService persistenceService;

    @Override
    public void init(ServletConfig cfg) throws ServletException {
        super.init(cfg);

        ApplicationContext ctx = ApplicationContextProvider.getApplicationContext();
        AutowireCapableBeanFactory beanFactory = ctx.getAutowireCapableBeanFactory();
        beanFactory.autowireBean(this);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(req));

        super.service(req, resp);
    }

    /**
     * Root's effective subject is always '0', whereas in all other cases, the user gets the Active profile ID.
     *
     * @return Profile ID or 0 for super user.
     */
    protected Integer getEffectiveSubject() {
        return getEffectiveSubject(userService.getLoggedInUser());
    }

    protected Integer getEffectiveSubject(User user) {
        return userService.getEffectiveSubject(user);
    }
}
