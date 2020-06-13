package com.dev.rexhuang.rlib.log;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dev.rexhuang.rlib.R;
import com.dev.rexhuang.rlib.log.common.ILogPrinter;
import com.dev.rexhuang.rlib.log.common.RLogConfig;
import com.dev.rexhuang.rlib.log.common.RLogType;

import java.util.ArrayList;
import java.util.List;

/**
 * *  created by RexHuang
 * *  on 2020/6/1
 */
public class RLogViewPrinter implements ILogPrinter {

    private final LogAdapter mAdapter;
    private final RLogViewPrinterProvider mViewProvider;
    private RecyclerView mRecyclerView;

    public RLogViewPrinter(Activity activity) {
        FrameLayout rootView = activity.findViewById(android.R.id.content);
        mRecyclerView = new RecyclerView(activity);
        mAdapter = new LogAdapter(LayoutInflater.from(mRecyclerView.getContext()));
        LinearLayoutManager layoutManager = new LinearLayoutManager(mRecyclerView.getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mViewProvider = new RLogViewPrinterProvider(rootView, mRecyclerView);
    }

    public RLogViewPrinterProvider getViewProvider() {
        return mViewProvider;
    }

    @Override
    public void print(@NonNull RLogConfig config, int level, String tag, String printString) {
        mAdapter.addItem(new RLogMo(System.currentTimeMillis(), level, tag, printString));
        mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount() - 1);
    }

    public static class LogAdapter extends RecyclerView.Adapter<LogViewHolder> {
        private LayoutInflater inflater;

        private List<RLogMo> logs = new ArrayList<>();

        LogAdapter(LayoutInflater inflater) {
            this.inflater = inflater;
        }

        void addItem(RLogMo logItem) {
            logs.add(logItem);
            notifyItemInserted(logs.size() - 1);
        }

        @NonNull
        @Override
        public LogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = inflater.inflate(R.layout.ilog_item, parent, false);
            return new LogViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(LogViewHolder holder, int position) {
            RLogMo logItem = logs.get(position);

            int color = getHighlightColor(logItem.level);
            holder.tagView.setTextColor(color);
            holder.messageView.setTextColor(color);

            holder.tagView.setText(logItem.getFlattened());
            holder.messageView.setText(logItem.log);
        }

        /**
         * 跟进log级别获取不同的高了颜色
         *
         * @param logLevel log 级别
         * @return 高亮的颜色
         */
        private int getHighlightColor(int logLevel) {
            int highlight;
            switch (logLevel) {
                case RLogType.V:
                    highlight = 0xffbbbbbb;
                    break;
                case RLogType.D:
                    highlight = 0xffffffff;
                    break;
                case RLogType.I:
                    highlight = 0xff6a8759;
                    break;
                case RLogType.W:
                    highlight = 0xffbbb529;
                    break;
                case RLogType.E:
                    highlight = 0xffff6b68;
                    break;
                default:
                    highlight = 0xffffff00;
                    break;
            }
            return highlight;
        }

        @Override
        public int getItemCount() {
            return logs.size();
        }
    }

    public static class LogViewHolder extends RecyclerView.ViewHolder {

        TextView tagView;
        TextView messageView;

        public LogViewHolder(@NonNull View itemView) {
            super(itemView);
            tagView = itemView.findViewById(R.id.tag);
            messageView = itemView.findViewById(R.id.message);
        }
    }
}
