/*
 * Copyright (C) 2016 Felipe Joglar Santos
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fjoglar.etsitnews.view.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.fjoglar.etsitnews.R;
import com.fjoglar.etsitnews.model.entities.Category;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.FilterViewHolder> {

    private List<Category> mFilterList;
    private FilterItemClickListener mFilterItemClickListener;

    public interface FilterItemClickListener {
        void filterItemClicked(Category category);
    }

    public FilterAdapter(@NonNull FilterItemClickListener itemClickListener) {
        this.mFilterList = Collections.emptyList();
        this.mFilterItemClickListener = itemClickListener;
    }

    public void setFilterAdapter(List<Category> categoryList) {
        this.mFilterList = categoryList;
    }

    final static class FilterViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.filter_text_view) TextView filterTextView;
        @BindView(R.id.filter_checkbox) CheckBox filterCheckBox;

        public FilterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setOnClickListener(View.OnClickListener listener) {
            itemView.setOnClickListener(listener);
        }
    }

    @Override
    public FilterAdapter.FilterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.drawer_filter_list_item, parent, false);
        return new FilterViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FilterAdapter.FilterViewHolder holder, final int position) {
        final Category category = mFilterList.get(position);

        holder.filterTextView.setText(category.getTitle());
        holder.filterCheckBox.setChecked(category.isEnabled());

        holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFilterItemClickListener.filterItemClicked(category);
            }
        });
        // Same behavior when touching directly in the CheckBox
        holder.filterCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFilterItemClickListener.filterItemClicked(category);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFilterList.size();
    }

}