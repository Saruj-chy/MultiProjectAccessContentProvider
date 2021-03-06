package com.example.controllerproject.notify;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.controllerproject.R;

import java.util.List;


public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {


    private Context mCtx;
    private List<NotifyModel> itemList;

    public NotificationAdapter(Context mCtx, List<NotifyModel> itemList) {
        this.mCtx = mCtx;
        this.itemList = itemList;
    }

    @Override
    public NotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.item_list, null);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NotificationViewHolder holder, int position) {
        final NotifyModel item = itemList.get(position);



        holder.textViewTitle.setText(item.getTitle());
        holder.textViewBodyText.setText(String.valueOf(item.getBody_text()));
        holder.textViewTopic.setText(item.getTopic());



//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(mCtx, FoodDetailsActivity.class) ;
//                intent.putExtra("foodName", product.getFood_name()) ;
//                intent.putExtra("price", String.valueOf(product.getPrice())) ;
//                intent.putExtra("resturantName", product.getResturant_name()) ;
//                intent.putExtra("rating", product.getRating()) ;
//                intent.putExtra("image", product.getImage()) ;
//                mCtx.startActivity(intent);
//                Toast.makeText(mCtx, "yes", Toast.LENGTH_SHORT).show();
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class NotificationViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle, textViewBodyText, textViewTopic;

        public NotificationViewHolder(View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.text_title);
            textViewBodyText = itemView.findViewById(R.id.text_body_text);
            textViewTopic = itemView.findViewById(R.id.text_topic);


        }
    }
}