package com.example.dynodash.ui.restaurant.code;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dynodash.R;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CodeListAdapter extends RecyclerView.Adapter<CodeListAdapter.CodeViewHolder> {

    private List<CodeListItem> mCodeListItems = new ArrayList<>();
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void getCode(CodeListItem item) throws IOException;
    }

    public void setCodeItems(List<CodeListItem> codeItems) {
        mCodeListItems = codeItems;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public CodeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_code_item, parent, false);
        return new CodeViewHolder(itemView, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CodeViewHolder holder, int position) {
        CodeListItem codeItem = mCodeListItems.get(position);
        holder.bind(codeItem);
    }

    public int getItemCount() {return mCodeListItems.size();}

    class CodeViewHolder extends RecyclerView.ViewHolder {
        public TextView mTableNumber;
        public Button mGetTableQR;

        public CodeViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            mTableNumber = itemView.findViewById(R.id.table_number);
            mGetTableQR = itemView.findViewById(R.id.generate_qr_button);

            mGetTableQR.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        CodeListItem codeItem = mCodeListItems.get(position);
                        if (mListener != null) {
                            try {
                                mListener.getCode(codeItem);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });
        }

        public void bind(CodeListItem codeItem) {
            mTableNumber.setText("Table " + String.valueOf(codeItem.tableNumber));
        }
    }

}
