package nvest.com.nvestlibrary.customView;

import android.content.Context;
import android.graphics.Canvas;
import androidx.annotation.NonNull;
import android.util.AttributeSet;

import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class PrefixEditText extends MaterialEditText {
    private float mOriginalLeftPadding = -1;
    private String prefix = "\u20B9 ";
    private static final String TAG = PrefixEditText.class.getSimpleName();

    public PrefixEditText(Context context) {
        super(context);
    }

    public PrefixEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PrefixEditText(Context context, AttributeSet attrs, int style) {
        super(context, attrs, style);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        calculatePrefix();

        // add text watcher
        // addTextChangedListener(this);
    }

    private void calculatePrefix() {
        if (mOriginalLeftPadding == -1) {

            float[] widths = new float[prefix.length()];
            getPaint().getTextWidths(prefix, widths);
            float textWidth = 0;
            for (float w : widths) {
                textWidth += w;
            }
            mOriginalLeftPadding = getCompoundPaddingStart();
            setPaddings((int) (textWidth + mOriginalLeftPadding),
                    getPaddingTop(), getPaddingEnd(),
                    getPaddingBottom());
        }
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawText(prefix, mOriginalLeftPadding,
                getLineBounds(0, null), getPaint());


    }

    /*@Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        // remove text watcher
        removeTextChangedListener(this);

        String text = s.toString();
        if (!text.isEmpty()) {
            String textWithoutComma = text.replaceAll(",", "");
            double amount = Double.parseDouble(textWithoutComma);
            String textWithComma = getCommaSeparatedAmount(amount, 0);
            if (!textWithComma.equals(text)) {
                setText(textWithComma);
                int length = textWithComma.length();
                setSelection(length);
                CommonMethod.log(TAG, "\nText changed => " + textWithComma + "\nText Length => " + length);
            }
        }

        // add text watcher
        addTextChangedListener(this);
    }*/

    private String getCommaSeparatedAmount(double amount, int decimalPrecision) {
        // default pattern
        StringBuilder patternBuilder = new StringBuilder("#,##,##,##,###");

        // add decimals in pattern
        if (decimalPrecision > 0) {
            patternBuilder.append(".");
            for (int i = 0; i < decimalPrecision; i++) {
                patternBuilder.append("0");
            }
        }

        // generate pattern
        String pattern = patternBuilder.toString();

        // generate decimal formatter
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter.applyPattern(pattern);

        return formatter.format(amount);

    }
}
