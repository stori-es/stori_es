package org.consumersunion.stories.common.client.util;

import com.google.gwt.user.client.Window;

public abstract class Callback<T> {
    public void onFailure() {
    }

    public void onFailure(String error) {
        Window.alert("ERROR : " + error);
    }

    public abstract void onSuccess(T value);

    public abstract void onCancel();
}
