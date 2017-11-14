package com.quantpower.silverquotes.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ShuLin on 2017/5/18.
 * Email linlin.1016@qq.com
 * Company Shanghai Quantpower Information Technology Co.,Ltd.
 */

public abstract class RecyclerAdapter<T> extends BaseRecyclerAdapter<RecyclerViewHolder> {

    protected Context context;

    protected LayoutInflater inflater;

    protected List<T> listData;

    protected final int itemLayoutId;

    private OnItemClickListener itemClickListener;

    private OnItemLongClickListener itemLongClickListener;

    private Map<Integer, Boolean> map = new HashMap<>();
    private int lastAnimatedPosition = -1;
    private boolean animationsLocked = false;
    private boolean delayEnterAnimation = true;

    public RecyclerAdapter(Context context, List<T> listData, int itemLayoutId) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.listData = listData;
        this.itemLayoutId = itemLayoutId;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View view = this.inflater.inflate(itemLayoutId, parent, false);
        RecyclerViewHolder holder = new RecyclerViewHolder(view);
        return holder;
    }


//    private void runEnterAnimation(View view, int position) {
//
//        if (animationsLocked) return;//animationsLocked是布尔类型变量，一开始为false，确保仅屏幕一开始能够显示的item项才开启动画
//
//        if (position > lastAnimatedPosition) {//lastAnimatedPosition是int类型变量，一开始为-1，这两行代码确保了recycleview滚动式回收利用视图时不会出现不连续的效果
//            lastAnimatedPosition = position;
//            view.setTranslationY(500);//相对于原始位置下方400
//            view.setAlpha(0.f);//完全透明
//            //每个item项两个动画，从透明到不透明，从下方移动到原来的位置
//            //并且根据item的位置设置延迟的时间，达到一个接着一个的效果
//            view.animate()
//                    .translationY(0).alpha(1.f)
//                    .setStartDelay(delayEnterAnimation ? 20 * (position) : 0)//根据item的位置设置延迟时间，达到依次动画一个接一个进行的效果
//                    .setInterpolator(new DecelerateInterpolator(0.5f))//设置动画效果为在动画开始的地方快然后慢
//                    .setDuration(700)
//                    .setListener(new AnimatorListenerAdapter() {
//                        @Override
//                        public void onAnimationEnd(Animator animation) {
//                            animationsLocked = true;//确保仅屏幕一开始能够显示的item项才开启动画，也就是说屏幕下方还没有显示的item项滑动时是没有动画效果
//                        }
//                    })
//                    .start();
//        }
//    }


    /**
     * @param position
     * @return
     */
    public T getItem(int position) {
        return listData.get(position);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position, boolean isItem) {
        convert(holder, getItem(position), position);
//        runEnterAnimation(holder.itemView, position);  //item动画展示
        holder.itemView.setOnClickListener((v) -> {

            if (itemClickListener != null) {
                itemClickListener.onClick(v, position);
            }
        });
        holder.itemView.setOnLongClickListener((v) -> {

            if (itemLongClickListener != null) {
                itemLongClickListener.onLongClick(v, position);
            }
            return false;
        });
    }

    public void insert(T object, int position) {
        insert(listData, object, position);
    }

    /**
     * @param helper
     * @param item
     * @param position
     */
    public abstract void convert(RecyclerViewHolder helper, T item, int position);

    @Override
    public int getAdapterItemViewType(int position) {
        return 0;
    }

    @Override
    public int getAdapterItemCount() {
        return listData.size();
    }

    @Override
    public RecyclerViewHolder getViewHolder(View view) {
        return new RecyclerViewHolder(view);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public void setItemLongClickListener(OnItemLongClickListener listener) {
        this.itemLongClickListener = listener;
    }

    public interface OnItemClickListener {
        void onClick(View parent, int position);
    }

    public interface OnItemLongClickListener {
        boolean onLongClick(View parent, int position);
    }
}
