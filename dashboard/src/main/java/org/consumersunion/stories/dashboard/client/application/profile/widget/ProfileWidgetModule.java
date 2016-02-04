package org.consumersunion.stories.dashboard.client.application.profile.widget;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class ProfileWidgetModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bindPresenterWidget(ContactsPresenter.class, ContactsPresenter.MyView.class,
                ContactsView.class);

        bindPresenterWidget(NotesManagerPresenter.class, NotesManagerPresenter.MyView.class,
                NotesManagerView.class);
    }
}
