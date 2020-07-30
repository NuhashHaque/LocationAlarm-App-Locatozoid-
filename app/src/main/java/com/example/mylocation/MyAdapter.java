package com.example.mylocation;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<Data> data;
    private Context context;
    DatabaseHelper myDb;
    //public Data listItems;
    //myDb=new DatabaseHelper(getApplicationContext());

    public MyAdapter(List<Data> data, Context context) {//constructor
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);//inflateint the item list
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

         final Data listItems=data.get(position);
        //holder.setBackgroundColor(Color.YELLOW);
        holder.textViewHead.setText(listItems.getAddress());
        holder.textViewDesc.setText( "Latitude:"+listItems.getLat());
        holder.textViewlongitude.setText("Longitude:"+listItems.getLang());
        holder.textViewRange.setText("Range(in meter):"+listItems.getRange());

        myDb=new DatabaseHelper(context);
//        SharedPreferences sharedPrefs = context.getSharedPreferences("com.example.xyz", MODE_PRIVATE);
//        toggleButton.setChecked(sharedPrefs.getBoolean("NameOfThingToSave"+getAdapterPosition(), true));
        SharedPreferences sharedPreferences = context.getSharedPreferences("com.example.xyz"+position,Context.MODE_PRIVATE);
        boolean tgpref = sharedPreferences.getBoolean("NameOfThingToSave"+position,false);


        if (tgpref){
            holder.toggleButton.setChecked(true);
        }else {
            holder.toggleButton.setChecked(false);
        }




        holder.deleteEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(context,"HELLO DELETE",Toast.LENGTH_SHORT).show();

                //String itemLabel = data.get(position);
                data.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,data.size());
                myDb.deleteEntry(listItems.getAddress());
                
                SharedPreferences.Editor editor = context.getSharedPreferences("com.example.xyz"+position, MODE_PRIVATE).edit();
                editor.putBoolean("NameOfThingToSave"+position, true);
                editor.commit();
                //myDb.close();

            }
        });
        //to show data
        holder.toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    SharedPreferences.Editor editor = context.getSharedPreferences("com.example.xyz"+position, MODE_PRIVATE).edit();
                    editor.putBoolean("NameOfThingToSave"+position, true);
                    editor.commit();
                    //Toast.makeText(context,"DELETE ON",Toast.LENGTH_SHORT).show();
                    myDb.updateConditionHIGH(listItems.getAddress());
                    //myDb.close();
                } else {
                    // The toggle is disabled
                    SharedPreferences.Editor editor = context.getSharedPreferences("com.example.xyz"+position, MODE_PRIVATE).edit();
                    editor.putBoolean("NameOfThingToSave"+position, false);
                    editor.commit();
                    //Toast.makeText(context,"DELETE OFF",Toast.LENGTH_SHORT).show();
                    myDb.updateConditionLOW(listItems.getAddress());
                   // myDb.close();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
      public TextView textViewHead;//initializing views
      public  TextView textViewDesc;
        public  TextView textViewlongitude;
        public  TextView textViewRange;
        public ImageButton deleteEntry;
        public ToggleButton toggleButton;




        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewHead=(TextView) itemView.findViewById(R.id.textViewHead);
            textViewDesc=(TextView) itemView.findViewById(R.id.textViewDesc);
            textViewlongitude=(TextView) itemView.findViewById(R.id.longitude);
            textViewRange=(TextView) itemView.findViewById(R.id.range);
             deleteEntry=(ImageButton)itemView.findViewById(R.id.deleteEntry);
             toggleButton=(ToggleButton)itemView.findViewById(R.id.toggleButton);
            //getWindow().getDecorView().setBackgroundColor(Color.YELLOW);

           // SharedPreferences sharedPrefs = context.getSharedPreferences("com.example.xyz", MODE_PRIVATE);
            //toggleButton.setChecked(sharedPrefs.getBoolean("NameOfThingToSave"+getAdapterPosition(), true));


        }
    }
}
