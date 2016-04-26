package com.example.osal.rxbindings;

import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.databinding.Observable;
import android.support.annotation.NonNull;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subjects.PublishSubject;

/**
 * Created by osal on 26.4.2016.
 */
public class RxProperty<T> implements rx.Observer<T>, Observable {

    private final PublishSubject<T> subject = PublishSubject.create();
    private final rx.Observable<T> output;
    private final List<OnPropertyChangedCallback> callbacks = new ArrayList<>();
    private T value;

    public RxProperty() {
        output = subject;
        connectCallbacks();
    }

    public RxProperty(final T startValue) {
        output = subject.startWith(startValue);
        connectCallbacks();
    }

    @Bindable
    public T getValue() {
        return value;
    }

    public void setValue(T t) {
        onNext(t);
    }

    public @NonNull rx.Observable<T> getStream() {
        return output;
    }

    @Override
    public void addOnPropertyChangedCallback(OnPropertyChangedCallback onPropertyChangedCallback) {
        synchronized (callbacks) {
            callbacks.add(onPropertyChangedCallback);
        }
    }

    @Override
    public void removeOnPropertyChangedCallback(OnPropertyChangedCallback onPropertyChangedCallback) {
        synchronized (callbacks) {
            callbacks.remove(onPropertyChangedCallback);
        }
    }

    @Override
    public void onCompleted() {
        subject.onCompleted();
    }

    @Override
    public void onError(Throwable e) {
        subject.onError(e);
    }

    @Override
    public void onNext(T t) {
        subject.onNext(t);
    }

    private void connectCallbacks() {
        final Observable _this = this;
        output.observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<T>() {
            @Override
            public void call(T t) {
                value = t;
                synchronized (callbacks) {
                    for (OnPropertyChangedCallback callback : callbacks) {
                        callback.onPropertyChanged(_this, 0);
                    }
                }
            }
        });
    }

    @BindingAdapter("android:text")
    public static <T> void setText(TextView view, RxProperty<T> bo) {
        view.setText(bo != null && bo.getValue() != null ?
                bo.getValue().toString() :
                "");
    }
}
