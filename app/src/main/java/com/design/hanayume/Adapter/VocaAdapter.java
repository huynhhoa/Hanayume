package com.design.hanayume.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.design.hanayume.R;
import com.design.hanayume.VocaListActivity;

import java.util.List;

public class VocaAdapter extends BaseAdapter {
    private VocaListActivity context;
    private int layout, isCheck;
    private List<VocaListGetSet> vocaListListGetSet;

    public VocaAdapter(VocaListActivity context, int layout, List<VocaListGetSet> vocaListListGetSet) {
        this.context = context;
        this.layout = layout;
        this.vocaListListGetSet = vocaListListGetSet;
    }
    private static class ViewHolder{
        //tranh lap lai khi load listview
        CheckBox cbRemember;
        TextView txtTuHan, txtKanji;
    }
    @Override
    public int getCount() {
        //tra ve so dong hien thi tren listview
        return vocaListListGetSet.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //tra ve moi dong tren item
        ViewHolder holder;
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout,null); //Lay toan bo layout
            //khoi tao viewholder
            holder = new ViewHolder();
            //anh xa view
            holder.txtTuHan = convertView.findViewById(R.id.txtTuHan);
            holder.txtKanji = convertView.findViewById(R.id.txtKanji);
            holder.cbRemember = convertView.findViewById(R.id.cbRemember);

            convertView.setTag(holder); //truyen vao trang thai anh xa cua view
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        VocaListGetSet vocaListGetSet = vocaListListGetSet.get(position);
        holder.txtKanji.setText(vocaListGetSet.getKanji());
        holder.txtTuHan.setText(vocaListGetSet.getTuHan());
        holder.cbRemember.setChecked(vocaListGetSet.isRemember());
        holder.cbRemember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.cbRemember.isChecked()){
                    isCheck = 1;
                }else{
                    isCheck = 0;}
                context.updateData(vocaListGetSet.getId(),isCheck);
            }
        });

        return convertView;
    }

}
