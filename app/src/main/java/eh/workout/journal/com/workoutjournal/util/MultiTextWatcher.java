package eh.workout.journal.com.workoutjournal.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;


public class MultiTextWatcher {

    private boolean register = true;


    private MultiTextWatcherInterfacePicker pickerCallback;

    public interface MultiTextWatcherInterfacePicker {
        void onPickerChanged(EditText editText, Integer value);
    }

    public MultiTextWatcher setCallback(MultiTextWatcherInterfacePicker pickerCallback) {
        this.pickerCallback = pickerCallback;
        return this;
    }

    //
    private MultiTextWatcherInterface callback;

    public interface MultiTextWatcherInterface {
        void onWeightChanged(int count);
    }

    public MultiTextWatcher setCallback(MultiTextWatcherInterface callback) {
        this.callback = callback;
        return this;
    }

    public void registerCallback(boolean register) {
        this.register = register;
    }

    public MultiTextWatcher registerEditText(final EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (register) {
                    if (callback != null) {
                        callback.onWeightChanged(s.length());
                    }
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
