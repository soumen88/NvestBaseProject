package nvest.com.nvestlibrary.nvestWebModel;

import android.support.design.widget.TextInputLayout;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * Created by Soumen on 01/08/17.
 */

public class ProductValidationField {

    private Spinner spinner;
    private TextInputLayout textInputLayout;
    private EditText editText;

    public Spinner getSpinner() {
        return spinner;
    }

    public void setSpinner(Spinner spinner) {
        this.spinner = spinner;
    }

    public TextInputLayout getTextInputLayout() {
        return textInputLayout;
    }

    public void setTextInputLayout(TextInputLayout textInputLayout) {
        this.textInputLayout = textInputLayout;
    }

    public EditText getEditText() {
        return editText;
    }

    public void setEditText(EditText editText) {
        this.editText = editText;
    }
}
