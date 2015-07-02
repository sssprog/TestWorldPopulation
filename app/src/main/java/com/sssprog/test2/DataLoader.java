package com.sssprog.test2;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import junit.framework.Assert;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.exceptions.OnErrorThrowable;
import rx.schedulers.Schedulers;

public class DataLoader {

    private static final long CONNECTION_TIMEOUT = 30;
    private static final long READ_TIMEOUT = 30;

    private static DataLoader instance;

    private Page1Fragment view;
    private Observable<List<Country>> dataObservable;
    private Runnable action;

    public static DataLoader getInstance() {
        if (instance == null) {
            instance = new DataLoader();
        }
        return instance;
    }

    public DataLoader() {
        dataObservable = getCountries().cache();
    }

    public void loadData() {
        dataObservable.subscribe(new Subscriber<List<Country>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(final List<Country> countries) {
                if (countries.isEmpty()) {
                    Log.i("-tag-", "No countries");
                }
                Runnable viewAction = new Runnable() {
                    @Override
                    public void run() {
                        view.onDataLoaded(countries.get(new Random().nextInt(countries.size())));
                    }
                };
                if (view != null) {
                    viewAction.run();
                } else {
                    action = viewAction;
                }
            }
        });
    }

    public void attach(Page1Fragment view) {
        Assert.assertNotNull(view);
        this.view = view;
        if (action != null) {
            action.run();
            action = null;
        }
    }

    public void detach() {
        view = null;
    }

    private Observable<List<Country>> getCountries() {
        return Observable
                .create(new Observable.OnSubscribe<List<Country>>() {
                    @Override
                    public void call(Subscriber<? super List<Country>> subscriber) {
                        subscriber.onNext(getCountriesSync());
                        subscriber.onCompleted();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private List<Country> getCountriesSync() {
        Request.Builder builder = new Request.Builder()
                .url("http://www.androidbegin.com/tutorial/jsonparsetutorial.txt");
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS);
        client.setReadTimeout(READ_TIMEOUT, TimeUnit.SECONDS);
        try {
            Response response = client.newCall(builder.build()).execute();
            if (response.code() != 200) {
                throw new RuntimeException("unknown error");
            }
            Log.i("-tag-", "request success");
            DataModel result = new Gson().fromJson(response.body().string(), new TypeToken<DataModel>() {
            }.getType());
            return result.countries;
        } catch (IOException | JsonSyntaxException e) {
            throw OnErrorThrowable.from(e);
        }
    }

}
