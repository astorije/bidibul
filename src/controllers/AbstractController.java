package controllers;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import models.AbstractModel;

@Deprecated
public abstract class AbstractController implements Observer {
    private final ArrayList _registeredViews;
    private final ArrayList _registeredModels;

    public AbstractController() {
        _registeredViews = new ArrayList();
        _registeredModels = new ArrayList();
    }


    public void addModel(AbstractModel model) {
        _registeredModels.add(model);
        ((Observable)model).addObserver(this);
    }
/*
    public void addView(AbstractViewPanel view) {
        _registeredViews.add(view);
    }
*/
}