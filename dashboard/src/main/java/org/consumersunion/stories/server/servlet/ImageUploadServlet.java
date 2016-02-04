package org.consumersunion.stories.server.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.consumersunion.stories.server.util.ApplicationContextProvider;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;

import gwtupload.server.UploadAction;
import gwtupload.server.exceptions.UploadActionException;

public class ImageUploadServlet extends UploadAction {
    @Inject
    private ImageUploadService imageUploadService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        ApplicationContext ctx = ApplicationContextProvider.getApplicationContext();
        AutowireCapableBeanFactory beanFactory = ctx.getAutowireCapableBeanFactory();
        beanFactory.autowireBean(this);
    }

    @Override
    public String executeAction(HttpServletRequest request, List<FileItem> sessionFiles) throws UploadActionException {
        String url = "";

        FileItem fileItem = sessionFiles.get(0);
        if (!fileItem.isFormField()) {
            InputStream inputStream;
            try {
                inputStream = fileItem.getInputStream();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            url = imageUploadService.upload(fileItem.getName(), inputStream, fileItem.getSize());
        }

        removeSessionFileItems(request);

        return url;
    }
}
