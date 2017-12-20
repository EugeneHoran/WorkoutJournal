package eh.workout.journal.com.workoutjournal.util.views;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class SimpleTextWatcher {
    private SimpleTextWatcherInterface listener;
    private boolean register;

    public SimpleTextWatcher(SimpleTextWatcherInterface listener, boolean register) {
        this.listener = listener;
        this.register = register;
    }

    public void setRegister(boolean register) {
        this.register = register;
    }

    public interface SimpleTextWatcherInterface {
        void onTextChanged(EditText editText, String string, CharSequence charSequence, int count);

    }

    public SimpleTextWatcher registerEditText(final EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (listener != null && register) {
                    listener.onTextChanged(editText, String.valueOf(charSequence), charSequence, charSequence.length());
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return this;
    }
}
