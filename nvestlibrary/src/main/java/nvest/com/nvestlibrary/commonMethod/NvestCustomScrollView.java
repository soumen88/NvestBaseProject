package nvest.com.nvestlibrary.commonMethod;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class NvestCustomScrollView extends ScrollView {
    public interface OnScrollChangedListener {
        // Developer must implement these methods.
        void onScrollStart();
        void onScrollEnd();
    }

    private long lastScrollUpdate = -1;
    private int scrollTaskInterval = 100;
    private Runnable mScrollingRunnable;
    public OnScrollChangedListener mOnScrollListener;

    public NvestCustomScrollView(Context context) {
        this(context, null, 0);
        init(context);
    }

    public NvestCustomScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init(context);
    }

    public NvestCustomScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        // Check for scrolling every scrollTaskInterval milliseconds
        mScrollingRunnable = new Runnable() {
            public void run() {
                if ((System.currentTimeMillis() - lastScrollUpdate) > scrollTaskInterval) {
                    // Scrolling has stopped.
                    lastScrollUpdate = -1;
                    //CustomHorizontalScrollView.this.onScrollEnd();
                    mOnScrollListener.onScrollEnd();
                } else {
                    // Still scrolling - Check again in scrollTaskInterval milliseconds...
                    postDelayed(this, scrollTaskInterval);
                }
            }
        };
    }

    public void setOnScrollChangedListener(OnScrollChangedListener onScrollChangedListener) {
        this.mOnScrollListener = onScrollChangedListener;
    }

    public void setScrollTaskInterval(int scrollTaskInterval) {
        this.scrollTaskInterval = scrollTaskInterval;
    }

    //void onScrollStart() {
    //    System.out.println("Scroll started...");
    //}

    //void onScrollEnd() {
    //    System.out.println("Scroll ended...");
    //}

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mOnScrollListener != null) {
            if (lastScrollUpdate == -1) {
                //CustomHorizontalScrollView.this.onScrollStart();
                mOnScrollListener.onScrollStart();
                postDelayed(mScrollingRunnable, scrollTaskInterval);
            }

            lastScrollUpdate = System.currentTimeMillis();
        }
    }


}