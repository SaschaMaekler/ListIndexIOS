package de.itemis.itemislistindexios;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.SectionIndexer;

/**
 * Created by Maekler on 02.03.16.
 */
public class ListIndex extends View {

    public final static String SECTIONS = "#abcdefghijklmnopqrstuvwxyz";
    private final boolean enableLetterHighlight = false;
    private Context ctx;
    private static int indWidth = 20;
    private String[] sections;
    private String section;
    private float scaledWidth;
    private float sx;
    private int indexSize;
    private boolean showLetter = true;
    private Handler listHandler;
    private ListView listView;
    private int minElements;


    public ListIndex(Context context, AttributeSet attrs) {
        super(context, attrs);
        ctx = context;
        sections = new String[SECTIONS.length()];
        for (int i = 0; i < SECTIONS.length(); ++i) {
            this.sections[i] = "" + SECTIONS.charAt(i);
        }

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.ListIndex,
                0, 0);

        try {
            minElements = a.getInt(R.styleable.ListIndex_minElements, 0);
        } finally {
            a.recycle();
        }
    }

    public void setListView(ListView listView) {
        this.listView = listView;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(listView != null && listView.getAdapter() != null && listView.getAdapter().getCount() < minElements){
            return;
        }


        scaledWidth = indWidth * getSizeInPixel(ctx);
        sx = this.getWidth() - this.getPaddingRight() - scaledWidth;

        Paint p = new Paint();
        p.setColor(Color.WHITE);
        p.setAlpha(100);

        canvas.drawRect(sx, this.getPaddingTop(), sx + scaledWidth,
                this.getHeight() - this.getPaddingBottom(), p);

        indexSize = (this.getHeight() - this.getPaddingTop() - getPaddingBottom())
                / sections.length;

        Paint textPaint = new Paint();
        textPaint.setColor(ContextCompat.getColor(ctx, R.color.listIndexBlue));
        textPaint.setTextSize(scaledWidth / 2);
        textPaint.setFakeBoldText(true);
        textPaint.setTextAlign(Paint.Align.CENTER);

        for (int i = 0; i < sections.length; i++)
            canvas.drawText(sections[i],
                    sx + textPaint.getTextSize() / 2, getPaddingTop()
                            + indexSize * (i + 1), textPaint);

        // Letter Highlight feature - not finished yet
        if (enableLetterHighlight && showLetter & section != null && !section.equals("")) {
            Paint textPaint2 = new Paint();
            textPaint2.setColor(ContextCompat.getColor(ctx, R.color.listIndexBlue));
            textPaint2.setTextSize(2 * indWidth);
            textPaint2.setFakeBoldText(true);
            canvas.drawText(section.toUpperCase(), getWidth() / 2, getHeight() / 2, textPaint2);
        }
    }

    private static float getSizeInPixel(Context ctx) {
        return ctx.getResources().getDisplayMetrics().density;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (listView == null)
            return false;

        float x = event.getX();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                if (x < sx)
                    return super.onTouchEvent(event);
                else {
                    // We touched the index bar
                    float y = event.getY() - this.getPaddingTop() - getPaddingBottom();
                    int currentPosition = (int) Math.floor(y / indexSize);
                    if (currentPosition < 0 || currentPosition >= SECTIONS.length()) {
                        return false;
                    }
                    section = sections[currentPosition];
                    ListIndex.this.invalidate();
                    showLetter = true;
                    int position = ((SectionIndexer) listView.getAdapter())
                            .getPositionForSection(currentPosition);
                    if (position >= 0) {
                        this.listView.setSelection(position);
                    }
                }
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                if (x < sx)
                    return super.onTouchEvent(event);
                else {
                    float y = event.getY();
                    int currentPosition = (int) Math.floor(y / indexSize);
                    if (currentPosition < 0 || currentPosition >= SECTIONS.length()) {
                        return false;
                    }
                    section = sections[currentPosition];
                    ListIndex.this.invalidate();
                    showLetter = true;
                    int position = ((SectionIndexer) listView.getAdapter())
                            .getPositionForSection(currentPosition);
                    if (position >= 0) {
                        this.listView.setSelection(position);
                    }
                }
                break;

            }
            case MotionEvent.ACTION_UP: {
                listHandler = new ListHandler();
                listHandler.sendEmptyMessageDelayed(0, 300);

                break;
            }
        }
        return true;
    }


    private class ListHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            showLetter = false;
            ListIndex.this.invalidate();
        }
    }
}

