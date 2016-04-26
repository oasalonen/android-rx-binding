package com.example.osal.rxbindings;

import android.databinding.ObservableField;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

/**
 * Created by osal on 26.4.2016.
 */
public class RxViewModel {
    public final BindableObservable<String> runningTime = new BindableObservable<>();

    public RxViewModel() {
        Observable.interval(1, TimeUnit.SECONDS)
                .scan(0, new Func2<Integer, Long, Integer>() {
                    @Override
                    public Integer call(Integer sum, Long aLong) {
                        return ++sum;
                    }
                })
                .map(new Func1<Integer, String>() {
                    @Override
                    public String call(Integer seconds) {
                        return "Running without crashing since " + seconds + " seconds!";
                    }
                })
                .subscribeOn(Schedulers.computation())
                .subscribe(runningTime);
    }
}
