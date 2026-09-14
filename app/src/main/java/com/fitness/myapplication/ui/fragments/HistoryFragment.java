package com.fitness.myapplication.ui.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fitness.myapplication.R;
import com.fitness.myapplication.data.java.TruckerLog;
import com.fitness.myapplication.ui.viewmodel.java.TruckerLogViewModel;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {
    private TruckerLogViewModel viewModel;
    private LogAdapter adapter;
    private View emptyState;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(TruckerLogViewModel.class);
        
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        emptyState = view.findViewById(R.id.emptyState);
        EditText searchEditText = view.findViewById(R.id.searchEditText);
        MaterialToolbar toolbar = view.findViewById(R.id.toolbar);

        toolbar.setNavigationOnClickListener(v -> Navigation.findNavController(v).navigateUp());

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new LogAdapter(log -> {
            Bundle args = new Bundle();
            args.putInt("logId", log.getId());
            Navigation.findNavController(view).navigate(R.id.action_historyFragment_to_editFragment, args);
        });
        recyclerView.setAdapter(adapter);

        viewModel.getAllLogs().observe(getViewLifecycleOwner(), logs -> {
            adapter.setLogs(logs);
            emptyState.setVisibility(logs.isEmpty() ? View.VISIBLE : View.GONE);
        });

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.setSearchQuery(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        return view;
    }

    private static class LogAdapter extends RecyclerView.Adapter<LogAdapter.ViewHolder> {
        private List<TruckerLog> logs = new ArrayList<>();
        private final OnLogClickListener listener;

        public interface OnLogClickListener {
            void onLogClick(TruckerLog log);
        }

        public LogAdapter(OnLogClickListener listener) {
            this.listener = listener;
        }

        public void setLogs(List<TruckerLog> logs) {
            this.logs = logs;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_log, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            TruckerLog log = logs.get(position);
            holder.textDate.setText(log.getDate());
            holder.textStartTime.setText(holder.itemView.getContext().getString(R.string.label_start_prefix) + log.getStartTime());
            holder.textEndTime.setText(holder.itemView.getContext().getString(R.string.label_end_prefix) + log.getEndTime());
            String details = holder.itemView.getContext().getString(R.string.label_driving_prefix) + log.getDrivingTime() + "h | " + 
                    holder.itemView.getContext().getString(R.string.label_rest_prefix) + log.getRestDuration();
            if (log.getKilometers() != null && !log.getKilometers().isEmpty()) {
                details += " | " + log.getKilometers() + " km";
            }
            holder.textDetails.setText(details);
            holder.itemView.setOnClickListener(v -> listener.onLogClick(log));
        }

        @Override
        public int getItemCount() { return logs.size(); }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView textDate, textStartTime, textEndTime, textDetails;
            ViewHolder(View view) {
                super(view);
                textDate = view.findViewById(R.id.textDate);
                textStartTime = view.findViewById(R.id.textStartTime);
                textEndTime = view.findViewById(R.id.textEndTime);
                textDetails = view.findViewById(R.id.textDetails);
            }
        }
    }
}
