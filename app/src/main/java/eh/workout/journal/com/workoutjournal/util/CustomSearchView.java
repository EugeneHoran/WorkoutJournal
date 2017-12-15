package eh.workout.journal.com.workoutjournal.util;

import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
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
        ViewHelper.showSoftKeyboard(editSearch);
        enableSearch = true;
    }

    public void hide() {
        searchHolder.setVisibility(View.GONE);
        editSearch.setText(null);
        toolbar.inflateMenu(R.menu.menu_exercise_selector);
        ViewHelper.hideSoftKeyboard(editSearch);
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
