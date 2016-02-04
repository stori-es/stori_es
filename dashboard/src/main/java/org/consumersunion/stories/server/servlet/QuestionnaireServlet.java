package org.consumersunion.stories.server.servlet;

import java.io.IOException;
import java.net.URLDecoder;

import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.consumersunion.stories.common.shared.model.Collection;
import org.consumersunion.stories.common.shared.model.Theme;
import org.consumersunion.stories.i18n.CommonI18nErrorMessages;
import org.consumersunion.stories.server.business_logic.ThemeService;
import org.consumersunion.stories.server.persistence.CollectionPersister;
import org.springframework.stereotype.Component;

import net.lightoze.gwt.i18n.server.LocaleFactory;

@Component
public final class QuestionnaireServlet extends SpringServlet {
    @Inject
    private ThemeService themeService;
    @Inject
    private CollectionPersister collectionPersister;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String urlPath = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");
        int idStart = urlPath.lastIndexOf('/') + 1;
        String entitySlug = urlPath.substring(idStart);
        urlPath = urlPath.replace("/" + entitySlug, "");

        idStart = urlPath.lastIndexOf('/') + 1;
        String entityKind = urlPath.substring(idStart);

        RequestDispatcher reqDispacher = null;

        String permalink = getFullPermalink(entityKind, entitySlug);

        Collection entity = null;
        try {
            entity = collectionPersister.retrieveByPermalink(permalink);
        } catch (Exception ignored) {
            reqDispacher = redirectError(request,
                    LocaleFactory.get(CommonI18nErrorMessages.class).questionnaireNotFound());
        }

        String previewKey = request.getParameter("preview");
        if (entity != null) {
            if ((entity.isPublished() && previewKey == null) || previewKeyMatches(entity, previewKey)) {
                Theme theme = themeService.getTheme(entity.getTheme());
                request.setAttribute("permalink", permalink);
                request.setAttribute("title", entity.getTitle());
                request.setAttribute("isQuestionnaire", String.valueOf(entity.isQuestionnaire()));

                reqDispacher = getServletContext().getRequestDispatcher("/template/" + theme.getThemePage());
            } else if (entity.isPublished() && previewKey != null) {
                response.sendRedirect(request.getRequestURI());
            } else {
                reqDispacher = redirectToNotPublished(request);
            }
        } else {
            reqDispacher = redirectToNotPublished(request);
        }

        if (reqDispacher != null) {
            reqDispacher.forward(request, response);
        }
    }

    private String getFullPermalink(String entityKind, String entitySlug) {
        if (isQuestionnaire(entityKind)) {
            return "/questionnaires/" + entitySlug;
        } else {
            return "/collections/" + entitySlug;
        }
    }

    private boolean isQuestionnaire(String entityKind) {
        String lowerCaseEntity = entityKind.toLowerCase();
        return "share".equals(lowerCaseEntity) || "questionnaires".equals(lowerCaseEntity);
    }

    private boolean previewKeyMatches(Collection collection, String previewKey) {
        return previewKey != null && previewKey.equals(collection.getPreviewKey());
    }

    private RequestDispatcher redirectToNotPublished(HttpServletRequest request) {
        return redirectError(request,
                LocaleFactory.get(CommonI18nErrorMessages.class).contentNotPublished());
    }

    private RequestDispatcher redirectError(HttpServletRequest request, String errorMessage) {
        request.setAttribute("errorMsg", errorMessage);
        request.setAttribute("title", "Error");

        return getServletContext().getRequestDispatcher("/error.jsp");
    }
}
