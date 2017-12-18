package eh.workout.journal.com.workoutjournal.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class QueryTextWatcher {


    private MultiTextWatcherInterfacePicker pickerCallback;

    public interface MultiTextWatcherInterfacePicker {
        void onPickerChanged(EditText editText, Integer value);
    }

    public QueryTextWatcher setCallback(MultiTextWatcherInterfacePicker pickerCallback) {
        this.pickerCallback = pickerCallback;
        return this;
    }


    public boolean registered = false;

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }

    //
    private MultiTextWatcherInterface callback;

    public interface MultiTextWatcherInterface {
        void onQuery(String query);
    }

    public QueryTextWatcher setCallback(MultiTextWatcherInterface callback) {
        this.callback = callback;
        return this;
    }

    public QueryTextWatcher registerEditText(final EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (callback != null && registered) {
                    final StringBuilder sb = new StringBuilder(charSequence.length());
                    sb.append(charSequence);
                    callback.onQuery(sb.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (pickerCallback != null) {
                    if (s.toString().length() != 0) {
                        Integer value = Integer.parseInt(s.toString());
                        if (value >= 1) {
                            pickerCallback.onPickerChanged(editText, value);
                        } else {
                            pickerCallback.onPickerChanged(editText, 1);
                        }
                    }
                }
            }
        });

        return this;
    }

}
