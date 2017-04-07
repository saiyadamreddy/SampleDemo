package com.sample.demo.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.sample.demo.R;
import com.sample.demo.adapter.BookListAdapter;
import com.sample.demo.modal.Books;
import com.sample.demo.modal.GlobaDataHolder;
import com.sample.demo.modal.ServiceDataModal;
import com.sample.demo.presenter.BookListPresenter;
import com.sample.demo.worker.ServiceWorker;

import java.util.List;


public class BookListView extends AppCompatActivity {
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
        BookListAdapter mAdapter = new BookListAdapter(data, this, true);
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
        if (isNetworkAvailable()) {
            ServiceWorker serviceWorker = new ServiceWorker();
            serviceWorker.getItemList(bookListPresenter);
        } else {
            Toast.makeText(BookListView.this, "Please check ur network connection", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.cart_btn) {
            if (GlobaDataHolder.getGlobaDataHolder().getShoppingList() != null && GlobaDataHolder.getGlobaDataHolder().getShoppingList().size() > 0) {
                Intent i = new Intent(BookListView.this, CartView.class);
                startActivity(i);
            } else {
                Toast.makeText(BookListView.this, "Please add items to the cart", Toast.LENGTH_LONG).show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //check network connection need to move to utils
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
