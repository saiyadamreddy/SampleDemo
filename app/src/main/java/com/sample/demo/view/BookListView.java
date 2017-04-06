package com.sample.demo.view;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.sample.demo.R;
import com.sample.demo.adapter.BookListAdapter;
import com.sample.demo.modal.Books;
import com.sample.demo.modal.ServiceDataModal;
import com.sample.demo.presenter.BookListPresenter;
import com.sample.demo.worker.ServiceWorker;

import java.util.List;


public class BookListView extends AppCompatActivity implements BookListPresenter {
    ProgressDialog progressDialog;
    RecyclerView recyclerView;
    BookListPresenter bookListPresenter = new BookListPresenter() {
        @Override
        public void getBookList(ServiceDataModal serviceDataModal) {
            Log.d("list", "list");
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            if (serviceDataModal != null && serviceDataModal.data != null) {
                updateBookList(serviceDataModal.data);
            }
        }
    };

    private void updateBookList(List<Books> data) {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        BookListAdapter mAdapter = new BookListAdapter(data, this);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booklist);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
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
