package com.sample.demo.view;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.sample.demo.R;
import com.sample.demo.adapter.BookListAdapter;


public class CartView extends AppCompatActivity {
    RecyclerView recyclerView;

    private void updateBookList() {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        BookListAdapter mAdapter = new BookListAdapter(null, this, false);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booklist);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        updateBookList();

    }

}
