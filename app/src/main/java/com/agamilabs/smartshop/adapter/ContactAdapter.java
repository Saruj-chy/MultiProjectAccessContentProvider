package com.agamilabs.smartshop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.agamilabs.smartshop.R;
import com.agamilabs.smartshop.controller.AppController;
import com.agamilabs.smartshop.model.Contacts;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder>  {
    Context mContext;
    ArrayList<Contacts> contacts;
    private ArrayList<String> phoneNumList = new ArrayList<>();
    private ArrayList<String> allContacts;
    private boolean isSelectedAll = false;
    
    public ContactAdapter(Context mContext, ArrayList<Contacts> contacts,ArrayList<String> allContacts) {
        this.mContext = mContext;
        this.contacts = contacts;
        this.allContacts=allContacts;
    }

    public ArrayList<String> getSelectedPhoneNum(){
        AppController.getAppController().getInAppNotifier().log("returnAllNumbers",phoneNumList+"");
        AppController.getAppController().getInAppNotifier().log("returnAllNumbersSize",phoneNumList.size()+"");
        return phoneNumList;
    }

    public void selectAll(){
        isSelectedAll=true;
        phoneNumList.addAll(allContacts);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ContactAdapter.ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.contact_layout, parent, false);
        return new ContactAdapter.ContactViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactAdapter.ContactViewHolder holder, int position) {
        Contacts contact = contacts.get(position);
        holder.text_name.setText(contact.getName());
        holder.text_phoneNumber.setText(contact.getPhoneNumber());

        if(isSelectedAll){
            holder.mCheckBox.setChecked(true);
        }

        if(contact.getImage() == null){
            holder.circleImageViewProfile.setVisibility(View.GONE);
            holder.mIdentifierCard.setVisibility(View.VISIBLE);
            String SingleText = contact.getName().substring(0,1);
            holder.text_identifier.setText(SingleText);
        } else {
            Glide.with(mContext)
                    .load(contact.getImage())
                    .into(holder.circleImageViewProfile);
            holder.mIdentifierCard.setVisibility(View.GONE);
            holder.circleImageViewProfile.setVisibility(View.VISIBLE);
        }

        if(!isSelectedAll){
            holder.mCheckBox.setChecked(contact.isSelected());
            holder.mCheckBox.setTag(contacts.get(position));
        }

        holder.mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Contacts contacts1 = (Contacts) holder.mCheckBox.getTag();
                if(isSelectedAll){
                    holder.mCheckBox.setChecked(holder.mCheckBox.isChecked());
                }
                else {
                    contacts1.setSelected(holder.mCheckBox.isChecked());
                    contacts.get(position).setSelected(holder.mCheckBox.isChecked());
                    notifyDataSetChanged();
                }

                if(holder.mCheckBox.isChecked()){
                    phoneNumList.add(contact.getPhoneNumber().trim());
                }
                else {
                    phoneNumList.remove(contact.getPhoneNumber().trim());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        CheckBox mCheckBox;
        TextView text_name,text_phoneNumber,text_identifier;
        public LinearLayout linearLayoutContact;
        public CircleImageView circleImageViewProfile;
        CardView mIdentifierCard ;
        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            mCheckBox = itemView.findViewById(R.id.checkbox);
            text_identifier = itemView.findViewById(R.id.text_identifier);
            text_name = itemView.findViewById(R.id.display_contactName);
            text_phoneNumber = itemView.findViewById(R.id.display_contactPhoneNumber);
            linearLayoutContact = itemView.findViewById(R.id.linear_layout_contact);
            circleImageViewProfile = itemView.findViewById(R.id.circle_image_profile);
            mIdentifierCard = itemView.findViewById(R.id.card_identifier);
        }
    }
}

