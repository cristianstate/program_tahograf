package com.fitness.myapplication.ui.viewmodel.java;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.fitness.myapplication.data.java.TruckerLog;
import com.fitness.myapplication.data.java.TruckerLogDao;
import com.fitness.myapplication.data.java.TruckerLogDatabase;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TruckerLogViewModel extends AndroidViewModel {
    private final TruckerLogDao truckerLogDao;
    private final ExecutorService executorService;
    private final MutableLiveData<String> searchQuery = new MutableLiveData<>("");
    private final LiveData<List<TruckerLog>> allLogs;

    public TruckerLogViewModel(@NonNull Application application) {
        super(application);
        TruckerLogDatabase db = TruckerLogDatabase.getDatabase(application);
        truckerLogDao = db.truckerLogDao();
        executorService = Executors.newFixedThreadPool(2);
        
        allLogs = Transformations.switchMap(searchQuery, query -> {
            if (query == null || query.isEmpty()) {
                return truckerLogDao.getAllLogs();
            } else {
                return truckerLogDao.searchLogsByDate(query);
            }
        });
    }

    public LiveData<List<TruckerLog>> getAllLogs() {
        return allLogs;
    }

    public void setSearchQuery(String query) {
        searchQuery.setValue(query);
    }

    public void insert(TruckerLog log) {
        executorService.execute(() -> truckerLogDao.insert(log));
    }

    public void update(TruckerLog log) {
        executorService.execute(() -> truckerLogDao.update(log));
    }

    public void delete(TruckerLog log) {
        executorService.execute(() -> truckerLogDao.delete(log));
    }

    public TruckerLog getLogById(int id) {
        // Warning: This is a synchronous call. In a real app, use a callback or LiveData.
        // For simplicity in this migration task, we'll keep it as provided in requirements.
        return truckerLogDao.getLogById(id);
    }
}
