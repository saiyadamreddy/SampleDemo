package com.sample.demo.view;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.sample.demo.R;
import com.sample.demo.modal.ServiceDataModal;
import com.sample.demo.presenter.BookListPresenter;
import com.sample.demo.worker.ServiceWorker;

/**
 * Created by Sai on 06-04-2017.
 */

public class BookListView extends AppCompatActivity implements BookListPresenter {
    ProgressDialog progressDialog;
    BookListPresenter bookListPresenter = new BookListPresenter() {
        @Override
        public void getBookList(ServiceDataModal serviceDataModal) {
            Log.d("list", "list");
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booklist);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait while books loading");
        progressDialog.show();
        ServiceWorker serviceWorker = new ServiceWorker();
        serviceWorker.getItemList(bookListPresenter);
    }

    @Override
    public void getBookList(ServiceDataModal serviceDataModal) {

    }
}
