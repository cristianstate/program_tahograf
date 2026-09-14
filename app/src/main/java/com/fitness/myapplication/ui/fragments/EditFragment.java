package com.fitness.myapplication.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.fitness.myapplication.R;
import com.fitness.myapplication.data.java.TruckerLog;
import com.fitness.myapplication.ui.viewmodel.java.TruckerLogViewModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EditFragment extends Fragment {
    private TruckerLogViewModel viewModel;
    private EditText dateEditText, startEditText, endEditText, drivingEditText, kilometersEditText;
    private RadioGroup restRadioGroup;
    private int logId;
    private TruckerLog currentLog;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd MM yyyy", Locale.getDefault());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(TruckerLogViewModel.class);
        
        if (getArguments() != null) {
            logId = getArguments().getInt("logId");
        }

        dateEditText = view.findViewById(R.id.dateEditText);
        startEditText = view.findViewById(R.id.startEditText);
        endEditText = view.findViewById(R.id.endEditText);
        drivingEditText = view.findViewById(R.id.drivingEditText);
        kilometersEditText = view.findViewById(R.id.kilometersEditText);
        restRadioGroup = view.findViewById(R.id.restRadioGroup);
        MaterialToolbar toolbar = view.findViewById(R.id.toolbar);

        toolbar.setNavigationOnClickListener(v -> Navigation.findNavController(v).navigateUp());
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_delete) {
                deleteLog();
                return true;
            }
            return false;
        });

        loadLogData();

        dateEditText.setOnClickListener(v -> showDatePicker());
        startEditText.setOnClickListener(v -> showTimePicker(startEditText));
        endEditText.setOnClickListener(v -> showTimePicker(endEditText));

        view.findViewById(R.id.btnUpdate).setOnClickListener(v -> updateLog());

        return view;
    }

    private void loadLogData() {
        // Warning: Non-reactive single fetch on background thread recommended.
        // For simplicity, we use the synchronous method we added to ViewModel.
        new Thread(() -> {
            currentLog = viewModel.getLogById(logId);
            if (currentLog != null && getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    dateEditText.setText(currentLog.getDate());
                    startEditText.setText(currentLog.getStartTime());
                    endEditText.setText(currentLog.getEndTime());
                    drivingEditText.setText(currentLog.getDrivingTime());
                    kilometersEditText.setText(currentLog.getKilometers());
                    if ("9h".equals(currentLog.getRestDuration())) {
                        restRadioGroup.check(R.id.radio9h);
                    } else {
                        restRadioGroup.check(R.id.radio11h);
                    }
                });
            }
        }).start();
    }

    private void showDatePicker() {
        long selection = MaterialDatePicker.todayInUtcMilliseconds();
        try {
            Date d = dateFormat.parse(dateEditText.getText().toString());
            if (d != null) selection = d.getTime();
        } catch (Exception ignored) {}

        MaterialDatePicker<Long> picker = MaterialDatePicker.Builder.datePicker()
                .setSelection(selection)
                .build();
        picker.addOnPositiveButtonClickListener(selectionDate -> {
            dateEditText.setText(dateFormat.format(new Date(selectionDate)));
        });
        picker.show(getParentFragmentManager(), "DATE_PICKER");
    }

    private void showTimePicker(EditText target) {
        MaterialTimePicker picker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(8)
                .setMinute(0)
                .build();
        picker.addOnPositiveButtonClickListener(v -> {
            target.setText(String.format(Locale.getDefault(), "%02d:%02d", picker.getHour(), picker.getMinute()));
        });
        picker.show(getParentFragmentManager(), "TIME_PICKER");
    }

    private void updateLog() {
        String driving = drivingEditText.getText().toString();
        if (driving.isEmpty()) {
            Toast.makeText(getContext(), R.string.error_empty_fields, Toast.LENGTH_SHORT).show();
            return;
        }

        String rest = restRadioGroup.getCheckedRadioButtonId() == R.id.radio9h ? "9h" : "11h";
        
        currentLog.setDate(dateEditText.getText().toString());
        currentLog.setStartTime(startEditText.getText().toString());
        currentLog.setEndTime(endEditText.getText().toString());
        currentLog.setDrivingTime(driving);
        currentLog.setKilometers(kilometersEditText.getText().toString());
        currentLog.setRestDuration(rest);
        
        viewModel.update(currentLog);
        Navigation.findNavController(requireView()).navigateUp();
        Toast.makeText(getContext(), R.string.log_updated_success, Toast.LENGTH_SHORT).show();
    }

    private void deleteLog() {
        if (currentLog != null) {
            viewModel.delete(currentLog);
            Navigation.findNavController(requireView()).navigateUp();
            Toast.makeText(getContext(), R.string.log_deleted_success, Toast.LENGTH_SHORT).show();
        }
    }
}
