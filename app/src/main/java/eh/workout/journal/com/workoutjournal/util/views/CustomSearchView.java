package eh.workout.journal.com.workoutjournal.util.views;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import eh.workout.journal.com.workoutjournal.R;

public class CustomSearchView {
    private SearchInterface listener;

    private boolean enableSearch = false;

    private Toolbar toolbar;
    private LinearLayout searchHolder;
    private EditText editSearch;
    private ImageView imageClear;

    public CustomSearchView(Toolbar toolbar) {
        this.toolbar = toolbar;
        toolbar.inflateMenu(R.menu.menu_exercise_selector);
        searchHolder = (LinearLayout) toolbar.getChildAt(0);
        editSearch = (EditText) searchHolder.getChildAt(0);
        imageClear = (ImageView) searchHolder.getChildAt(1);
        editSearch.addTextChangedListener(searchTextWatcher);
        imageClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editSearch.setText(null);
            }
        });
    }

    public void show() {
        toolbar.getMenu().clear();
        searchHolder.setVisibility(View.VISIBLE);
        InputMethodManager inputMethodManager = (InputMethodManager) editSearch.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        editSearch.requestFocus();
        if (inputMethodManager != null)
            inputMethodManager.showSoftInput(editSearch, 0);
        enableSearch = true;
    }

    public void hide() {
        searchHolder.setVisibility(View.GONE);
        editSearch.setText(null);
        toolbar.inflateMenu(R.menu.menu_exercise_selector);
        InputMethodManager inputMethodManager = (InputMethodManager) editSearch.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        editSearch.clearFocus();
        if (inputMethodManager != null)
            inputMethodManager.hideSoftInputFromWindow(editSearch.getWindowToken(), 0);
        enableSearch = false;
    }


    private TextWatcher searchTextWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (listener != null) {
                imageClear.setVisibility(i2 == 0 ? View.GONE : View.VISIBLE);
                if (enableSearch) {
                    listener.onSearchQuery(charSequence.toString());
                }
            }
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };


    public interface SearchInterface {
        void onSearchQuery(String query);
    }

    public void setListener(SearchInterface listener) {
        this.listener = listener;
    }
}
