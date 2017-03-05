package com.cum.chat.shop1;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by 2-1Ping on 2017/2/16.
 */

public class ClothAdapter extends RecyclerView.Adapter<ClothAdapter.ClothView>{
    private List<Cloth> cloths;


    public ClothAdapter(List<Cloth> list) {
        cloths=list;
    }
    public interface OnItemClickLitener
    {
        void onItemClick(View view, int position);
        void onItemLongClick(View view , int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }
    @Override
    public ClothView onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cloth_item, viewGroup, false);
        return new ClothView(view);
    }

    @Override
    public void onBindViewHolder(final ClothView masonryView, int position) {
        masonryView.imageView.setImageBitmap(cloths.get(position).getImg());
        masonryView.textView.setText(cloths.get(position).getTitle());
        // 如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null)
        {
            masonryView.imageView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    int pos = masonryView.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(masonryView.imageView, pos);
                }
            });


        }



    }

    @Override
    public int getItemCount() {
        return cloths.size();
    }

    public static class ClothView extends  RecyclerView.ViewHolder{

        ImageView imageView;
        TextView textView;

        public ClothView(View itemView){
            super(itemView);
            imageView= (ImageView) itemView.findViewById(R.id.masonry_item_img );
            textView= (TextView) itemView.findViewById(R.id.id_num);
        }

    }

}
