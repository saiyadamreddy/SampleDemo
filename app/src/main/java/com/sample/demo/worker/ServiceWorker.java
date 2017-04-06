package com.sample.demo.worker;

import android.support.annotation.NonNull;
import android.util.Log;

import com.sample.demo.modal.ServiceDataModal;
import com.sample.demo.presenter.BookListPresenter;

import java.io.IOException;
import java.util.concurrent.Callable;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;




public class ServiceWorker {

    public static final String BASE_URL = "http://guidebook.com/";

    public void getItemList(final BookListPresenter presenter)  {
        final Observable<ServiceDataModal> cardsList = Observable.fromCallable(new Callable<ServiceDataModal>() {
            @Override
            public ServiceDataModal call() {

                return getBookFromService();
            }
        });

        Observable<ServiceDataModal> observable;
        observable = cardsList.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        observable.subscribe(new Observer<ServiceDataModal>() {
            @Override
            public void onCompleted() {
                Log.d("service", "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.d("service", "onError");
            }

            @Override
            public void onNext(@NonNull ServiceDataModal serviceDataModal) {
                Log.d("service", "onNext");
                presenter.getBookList(serviceDataModal);
            }
        });
    }

    public static ServiceDataModal getBookFromService() {
        ServiceDataModal serviceDataModal = null;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ServiceApi service = retrofit.create(ServiceApi.class);
        Call<ServiceDataModal> bookshelf = service.getBook();
        Response<ServiceDataModal> execute = null;
        try {
            execute = bookshelf.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (execute.isSuccessful()) {
            serviceDataModal = execute.body();
        }
        return serviceDataModal;
    }

    public interface ServiceApi {
        @GET("service/v2/upcomingGuides/")
        Call<ServiceDataModal> getBook();
    }
}
