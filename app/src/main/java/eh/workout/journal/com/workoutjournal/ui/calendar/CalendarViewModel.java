package eh.workout.journal.com.workoutjournal.ui.calendar;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.github.sundeepk.compactcalendarview.domain.Event;

import java.util.ArrayList;
import java.util.List;

import eh.workout.journal.com.workoutjournal.JournalApplication;
import eh.workout.journal.com.workoutjournal.db.JournalRepository;
import eh.workout.journal.com.workoutjournal.db.entinty.JournalSetEntity;

public class CalendarViewModel extends AndroidViewModel {
    private JournalRepository repository;
    private MutableLiveData<List<Event>> setDates;

    public CalendarViewModel(@NonNull Application application) {
        super(application);
        repository = ((JournalApplication) application).getRepository();
    }

    void initData() {
        new EventTask().execute();
    }

    MutableLiveData<List<Event>> getSetDates() {
        if (setDates == null) {
            setDates = new MutableLiveData<>();
        }
        return setDates;
    }

    @SuppressLint("StaticFieldLeak")
    class EventTask extends AsyncTask<Void, Void, List<Event>> {
        @Override
        protected List<Event> doInBackground(Void... voids) {
            List<Event> listEvents = new ArrayList<>();
            List<JournalSetEntity> setEntities = repository.getJournalSetEntityDatesList();
            for (int i = 0; i < setEntities.size(); i++) {
                JournalSetEntity journalSetEntity = setEntities.get(i);
                listEvents.add(new Event(Color.parseColor("#F44336"), journalSetEntity.getTimestamp(), "contains data"));
            }
            return listEvents;
        }

        @Override
        protected void onPostExecute(List<Event> events) {
            super.onPostExecute(events);
            getSetDates().setValue(events);
        }
    }
}
