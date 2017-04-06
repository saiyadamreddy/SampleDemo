package com.sample.demo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sample.demo.R;
import com.sample.demo.modal.Books;
import com.sample.demo.modal.GlobaDataHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class BookListAdapter extends
        RecyclerView.Adapter<BookListAdapter.VersionViewHolder> implements
        ItemTouchHelperAdapter {

    private List<Books> productList = new ArrayList<Books>();
    private Context context;

    public BookListAdapter(List<Books> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }

    @Override
    public VersionViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.item_product_list, viewGroup, false);
        VersionViewHolder viewHolder = new VersionViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final VersionViewHolder holder,
                                 final int position) {

        holder.itemName.setText(productList.get(position).name);

        holder.startDate.setText(productList.get(position).startDate);

        holder.endDate.setText(productList.get(position).endDate);

        Glide.with(context).load(productList.get(position).icon).placeholder(R.drawable.common_ic_googleplayservices)
                .error(R.drawable.common_ic_googleplayservices).centerCrop().into(holder.imagView);

        holder.addItem.findViewById(R.id.add_item).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        //current object
                        Books tempObj = productList.get(position);
                        //if current object is lready in shopping list
                        if (GlobaDataHolder.getGlobaDataHolder()
                                .getShoppingList().contains(tempObj)) {
                            //get position of current item in shopping list
                            int indexOfTempInShopingList = GlobaDataHolder
                                    .getGlobaDataHolder().getShoppingList()
                                    .indexOf(tempObj);
                            // update quanity in shopping list
                            GlobaDataHolder
                                    .getGlobaDataHolder()
                                    .getShoppingList()
                                    .get(indexOfTempInShopingList)
                                    .setQuantity(
                                            String.valueOf(Integer
                                                    .valueOf(tempObj
                                                            .getQuantity()) + 1));
                            // update current item quanitity
                            holder.quanitity.setText(tempObj.getQuantity());
                        } else {
                            tempObj.setQuantity(String.valueOf(1));
                            holder.quanitity.setText(tempObj.getQuantity());
                            GlobaDataHolder.getGlobaDataHolder()
                                    .getShoppingList().add(tempObj);
                        }
                    }
                });
        holder.removeItem.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Books tempObj = (productList).get(position);
                if (GlobaDataHolder.getGlobaDataHolder().getShoppingList()
                        .contains(tempObj)) {
                    int indexOfTempInShopingList = GlobaDataHolder
                            .getGlobaDataHolder().getShoppingList()
                            .indexOf(tempObj);
                    if (Integer.valueOf(tempObj.getQuantity()) != 0) {
                        GlobaDataHolder
                                .getGlobaDataHolder()
                                .getShoppingList()
                                .get(indexOfTempInShopingList)
                                .setQuantity(
                                        String.valueOf(Integer.valueOf(tempObj
                                                .getQuantity()) - 1));
                        holder.quanitity.setText(GlobaDataHolder
                                .getGlobaDataHolder().getShoppingList()
                                .get(indexOfTempInShopingList).getQuantity());
                        if (Integer.valueOf(GlobaDataHolder
                                .getGlobaDataHolder().getShoppingList()
                                .get(indexOfTempInShopingList).getQuantity()) == 0) {
                            GlobaDataHolder.getGlobaDataHolder()
                                    .getShoppingList()
                                    .remove(indexOfTempInShopingList);
                            notifyDataSetChanged();
                        }
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList == null ? 0 : productList.size();
    }

    class VersionViewHolder extends RecyclerView.ViewHolder implements
            OnClickListener {
        TextView itemName, startDate, endDate, availability, quanitity,
                addItem, removeItem;
        ImageView imagView;

        public VersionViewHolder(View itemView) {
            super(itemView);

            itemName = (TextView) itemView.findViewById(R.id.item_name);

            startDate = (TextView) itemView.findViewById(R.id.item_short_desc);

            endDate = (TextView) itemView.findViewById(R.id.item_price);

            quanitity = (TextView) itemView.findViewById(R.id.iteam_amount);

            itemName.setSelected(true);

            imagView = ((ImageView) itemView.findViewById(R.id.product_thumb));

            addItem = ((TextView) itemView.findViewById(R.id.add_item));

            removeItem = ((TextView) itemView.findViewById(R.id.remove_item));

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    @Override
    public void onItemDismiss(int position) {
        productList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(productList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(productList, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

}
