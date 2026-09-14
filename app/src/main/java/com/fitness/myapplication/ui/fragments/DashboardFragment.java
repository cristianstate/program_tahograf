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
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DashboardFragment extends Fragment {
    private TruckerLogViewModel viewModel;
    private EditText dateEditText, startEditText, endEditText, drivingEditText, kilometersEditText;
    private RadioGroup restRadioGroup;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd MM yyyy", Locale.getDefault());
    private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        
        viewModel = new ViewModelProvider(requireActivity()).get(TruckerLogViewModel.class);
        
        dateEditText = view.findViewById(R.id.dateEditText);
        startEditText = view.findViewById(R.id.startEditText);
        endEditText = view.findViewById(R.id.endEditText);
        drivingEditText = view.findViewById(R.id.drivingEditText);
        kilometersEditText = view.findViewById(R.id.kilometersEditText);
        restRadioGroup = view.findViewById(R.id.restRadioGroup);
        MaterialToolbar toolbar = view.findViewById(R.id.toolbar);
        
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_history) {
                Navigation.findNavController(requireView()).navigate(R.id.action_dashboardFragment_to_historyFragment);
                return true;
            }
            return false;
        });

        // Default values
        Calendar now = Calendar.getInstance();
        dateEditText.setText(dateFormat.format(now.getTime()));
        startEditText.setText("08:00");
        endEditText.setText("17:00");

        dateEditText.setOnClickListener(v -> showDatePicker());
        startEditText.setOnClickListener(v -> showTimePicker(startEditText));
        endEditText.setOnClickListener(v -> showTimePicker(endEditText));

        view.findViewById(R.id.btnSave).setOnClickListener(v -> saveLog());

        return view;
    }

    private void showDatePicker() {
        MaterialDatePicker<Long> picker = MaterialDatePicker.Builder.datePicker()
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build();
        picker.addOnPositiveButtonClickListener(selection -> {
            dateEditText.setText(dateFormat.format(new Date(selection)));
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

    private void saveLog() {
        String driving = drivingEditText.getText().toString();
        if (driving.isEmpty()) {
            Toast.makeText(getContext(), R.string.error_empty_fields, Toast.LENGTH_SHORT).show();
            return;
        }

        String rest = restRadioGroup.getCheckedRadioButtonId() == R.id.radio9h ? "9h" : "11h";
        
        TruckerLog log = new TruckerLog(
                dateEditText.getText().toString(),
                startEditText.getText().toString(),
                endEditText.getText().toString(),
                driving,
                rest,
                kilometersEditText.getText().toString()
        );
        
        viewModel.insert(log);
        drivingEditText.setText("");
        kilometersEditText.setText("");
        Toast.makeText(getContext(), R.string.log_saved_success, Toast.LENGTH_SHORT).show();
    }
}
