package eh.workout.journal.com.workoutjournal.ui.entry;


import android.annotation.SuppressLint;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.echo.holographlibrary.Line;
import com.echo.holographlibrary.LinePoint;

import java.util.ArrayList;
import java.util.List;

import eh.workout.journal.com.workoutjournal.JournalApplication;
import eh.workout.journal.com.workoutjournal.db.JournalRepository;
import eh.workout.journal.com.workoutjournal.db.entinty.JournalRepEntity;
import eh.workout.journal.com.workoutjournal.model.OrmHistory;
import eh.workout.journal.com.workoutjournal.util.DateHelper;

public class EntryHistoryViewModel extends AndroidViewModel {
    private JournalRepository repository;
    private MutableLiveData<List<Object>> objectList;
    private String[] strings;

    public EntryHistoryViewModel(@NonNull JournalApplication application, String exerciseId, Long timestamp) {
        super(application);
        repository = application.getRepository();
        Long[] startEndTime = DateHelper.getStartAndEndTimestamp(timestamp);
        strings = new String[2];
        strings[0] = exerciseId;
        strings[1] = String.valueOf(startEndTime[0]);
    }

    void initData() {
        new HistoryTask().execute(strings);
    }

    MutableLiveData<List<Object>> getObjectList() {
        if (objectList == null) {
            objectList = new MutableLiveData<>();
        }
        return objectList;
    }

    @SuppressLint("StaticFieldLeak")
    class HistoryTask extends AsyncTask<String, Void, List<Object>> {
        @Override
        protected List<Object> doInBackground(String... strings) {
            List<Object> objectList = new ArrayList<>();
            List<JournalRepEntity> history = repository.getSetRepsByMaxList(strings[0]);
            if (history.size() >= 1) {
                JournalRepEntity max = repository.getSetRepsByMax(strings[0]);
                JournalRepEntity min = repository.getSetRepsByMin(strings[0]);
                Line l = new Line();
                for (int i = 0; i < history.size(); i++) {
                    JournalRepEntity journalRepEntity = history.get(i);
                    LinePoint p = new LinePoint();
                    p.setColor(Color.BLUE);
                    p.setX(i);
                    p.setY((float) journalRepEntity.getOneRepMax());
                    l.addPoint(p);
                }
                JournalRepEntity dayMax = repository.getSetRepsByMaxDate(strings[0], strings[1]);
                Line l1 = new Line();
                if (dayMax != null) {
                    LinePoint p = new LinePoint();
                    p.setColor(Color.TRANSPARENT);
                    p.setX(0);
                    p.setY((float) dayMax.getOneRepMax());
                    l1.addPoint(p);
                    LinePoint p1 = new LinePoint();
                    p1.setColor(Color.TRANSPARENT);
                    p1.setX(history.size() - 1);
                    p1.setY((float) dayMax.getOneRepMax());
                    l1.addPoint(p1);
                }
                l1.setColor(Color.RED);
                objectList.add(new OrmHistory(l, l1, max, min.getOneRepMax()));
            }
            objectList.addAll(repository.getExerciseSetRepRelationHistory(strings[0], Long.valueOf(strings[1])));
            return objectList;
        }

        @Override
        @SuppressWarnings("ConstantConditions")
        protected void onPostExecute(List<Object> objectList) {
            super.onPostExecute(objectList);
            if (getObjectList().getValue() == null) {
                getObjectList().setValue(objectList);
            } else {
                if (objectList.size() != getObjectList().getValue().size()) {
                    getObjectList().setValue(objectList);
                } else {
                    if (getObjectList().getValue() != null && getObjectList().getValue().size() > 0) {
                        OrmHistory ormOld = (OrmHistory) getObjectList().getValue().get(0);
                        OrmHistory ormNew = (OrmHistory) objectList.get(0);
                        if (!ormOld.toString().equals(ormNew.toString())) {
                            getObjectList().setValue(objectList);
                        }
                    } else {
                        getObjectList().setValue(objectList);
                    }
                }
            }
        }
    }
}
