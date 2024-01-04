package com.example.fixapp.adapter;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fixapp.R;
import com.example.fixapp.model.InfoProductOrder;
import com.example.fixapp.model.Order;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private Context context;
    private List<Order> list;
    private Callback callback;
    private List<InfoProductOrder> infoProductOrderList;

    public OrderAdapter(Context context, List<Order> list, Callback callback) {
        this.context = context;
        this.list = list;
        this.infoProductOrderList=new ArrayList<>();
        this.callback = callback;
    }

    public List<InfoProductOrder> getInfoProductOrderList() {
        return infoProductOrderList;
    }


    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = list.get(position);
        if (order == null) {
            return;
        }

        holder.imgMenu.setOnClickListener(view -> {
            callback.logic(order);
        });

        if (list != null && list.size() > position) {
            List<InfoProductOrder> productList = order.getListProduct();
            if (infoProductOrderList == null) {
                infoProductOrderList = new ArrayList<>();
            }
            if (productList != null) {
                infoProductOrderList.clear();
                infoProductOrderList.addAll(productList);
                int index = Math.min(position, infoProductOrderList.size() - 1);
                InfoProductOrder product = infoProductOrderList.get(index);
                holder.tvTotal.setText(String.valueOf(product.getPrice()));
                holder.tvNameProduct.setText(product.getNamePr());
                holder.colorProduct.setBackgroundColor(product.getColorPr());
                holder.tv_size.setText("Size : " + product.getSize());
                holder.tvQuantity.setText(String.valueOf(product.getQuantityPr()));

                if (product.getNote() == null || product.getNote().equals("") || TextUtils.isEmpty(product.getNote()) || product.getNote().isEmpty()) {
                    holder.tvnote.setVisibility(View.GONE);
                } else {
                    holder.tvnote.setVisibility(View.VISIBLE);
                    holder.tvnote.setText("Note: " + product.getNote());
                }


                if (product.getImgPr() != null && !product.getImgPr().isEmpty()) {
                    Uri imageUri = Uri.parse(product.getImgPr());
                    Picasso.get()
                            .load(imageUri)
                            .placeholder(R.drawable.pant)
                            .error(R.drawable.pant)
                            .into(holder.imgProduct);
                } else {
                    holder.imgProduct.setImageResource(R.drawable.pant);
                }

                if (product.getStatus().equals("done") || product.getStatus().equals("deliver")) {
                    holder.tv_paid.setVisibility(View.VISIBLE);
                    if (order.getPaid()) {
                        holder.tv_paid.setText("Đã thanh toán bằng ví");
                    } else {
                        holder.tv_paid.setText("Thanh toán khi nhận hàng");
                    }
                } else {
                    holder.tv_paid.setVisibility(View.GONE);
                }
            } else {
                Log.d("productList null", "onBindViewHolder: " + productList);
            }
        } else {
            Log.d("list null", "onBindViewHolder: " + list);
        }

    }

    @Override
    public int getItemCount() {
        if (list == null) {
            return 0;
        }
        return list.size();
    }



    public class OrderViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgProduct, colorProduct;
        private RecyclerView recyclerView;
        private TextView tvNameProduct;
        private TextView tvQuantity;
        private TextView tvTotal;
        private TextView tvnote;
        private TextView tv_paid;
        private TextView tv_size;
        private ImageView imgMenu;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = (ImageView) itemView.findViewById(R.id.img_product);
            colorProduct = (ImageView) itemView.findViewById(R.id.tv_color);
            tvNameProduct = (TextView) itemView.findViewById(R.id.tv_nameProduct);
            tvQuantity = (TextView) itemView.findViewById(R.id.tv_quantity);
            tvTotal = (TextView) itemView.findViewById(R.id.tv_total);
            imgMenu = (ImageView) itemView.findViewById(R.id.img_menu);
            tvnote = (TextView) itemView.findViewById(R.id.tvNoteOrder);
            tv_paid = (TextView) itemView.findViewById(R.id.tv_paid);
            tv_size = (TextView) itemView.findViewById(R.id.tv_size);
        }
    }

    public interface Callback {
        void logic(Order order);
    }

}
