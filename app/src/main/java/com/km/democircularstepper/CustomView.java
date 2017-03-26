package com.km.democircularstepper;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import java.util.Locale;

/**
 * Created by Kajal on 2/23/2017.
 */
public class CustomView extends View {
    private static final float ARC_OUTSIDE_START_ANGLE = 135f;
    private static final float ARC_OUTSIDE_SWEEP_ANGLE = 270f;

    private static final float ARC_INSIDE_START_ANGLE = 0f;
    private static final float ARC_INSIDE_SWEEP_ANGLE = 360f;

    private static final String PRIMARY_TEXT = "%d/%d";
    private static final String SECONDARY_TEXT = "Visit";

    private static final int STATES_COUNT = 14;
    private static final float ARC_PROGRESS_START_ANGLE = 135f;
    private static final float ARC_PROGRESS_PLACEMENT_ANGLE = 270f / STATES_COUNT;
    private static final double SCISSOR_ANGLE = 90f;
    private float mSweepProgressAngle = 0f;
    private String mPrimaryText = " 0/14";


    private RectF mRectArcOutside;
    private RectF mRectArcInside;
    private RectF mRectProgress;
    private Rect mRectTextPrimary;

    private Paint mPaintArcOutside;
    private Paint mPaintArcInside;
    private Paint mPaintTextPrimary;
    private Paint mPaintTextSecondary;
    private Paint mPaintProgress;

    private Bitmap mBitmapScissor;
    private Bitmap mBitmapUnChecked;
    private Bitmap mBitmapChecked;


    private int mTextPrimaryX;
    private int mTextPrimaryY;
    private int mStepperCount;
    private int mScissorX;
    private int mScissorY;
    private int mArcDiameterMid;
    private int mIconsLeft;
    private int mIconsTop;


    public CustomView(Context context) {
        super(context);
        initPaint();
    }

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();

    }

    public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initPaint();

    }

    private void initPaint() {
        mRectArcOutside = new RectF();
        mRectArcInside = new RectF();
        mRectTextPrimary = new Rect();
        mRectProgress = new RectF();

        mPaintArcOutside = new Paint();
        mPaintArcOutside.setStrokeWidth(8f);
        mPaintArcOutside.setAntiAlias(true);
        mPaintArcOutside.setColor(Color.RED);
        mPaintArcOutside.setStyle(Paint.Style.STROKE);

        mPaintArcInside = new Paint();
        mPaintArcInside.setStyle(Paint.Style.FILL);
        mPaintArcInside.setAntiAlias(true);
        mPaintArcInside.setColor(Color.RED);

        mPaintTextPrimary = new Paint();
        mPaintTextPrimary.setColor(Color.BLACK);
        mPaintTextPrimary.setAntiAlias(true);
        mPaintTextPrimary.setTextSize(50f);
        mPaintTextPrimary.setTextAlign(Paint.Align.CENTER);
        mPaintTextPrimary.getTextBounds(PRIMARY_TEXT, 0, PRIMARY_TEXT.length() - 1, mRectTextPrimary);

        mPaintTextSecondary = new Paint();
        mPaintTextSecondary.setColor(Color.BLACK);
        mPaintTextSecondary.setAntiAlias(true);
        mPaintTextSecondary.setTextSize(50f);
        mPaintTextSecondary.setTextAlign(Paint.Align.CENTER);
        mPaintTextSecondary.getTextBounds(SECONDARY_TEXT, 0, SECONDARY_TEXT.length() - 1, mRectTextPrimary);

        mPaintProgress = new Paint();
        mPaintProgress.setColor(Color.BLUE);
        mPaintProgress.setStrokeWidth(10.0f);
        mPaintProgress.setStyle(Paint.Style.STROKE);
        mPaintProgress.setAntiAlias(true);


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        int min = Math.min(width, height);

        int arcDiameter = min - getPaddingLeft() - getPaddingRight();
        int leftArcOutSide = width / 2 - arcDiameter / 2;
        int topArcOutside = height / 2 - arcDiameter / 2;
        int rightArcOutside = leftArcOutSide + arcDiameter;
        int bottomArcOutside = topArcOutside + arcDiameter;

        int leftArcInside = leftArcOutSide + arcDiameter / 4;
        int topArcInside = topArcOutside + arcDiameter / 4;
        int rightArcInside = rightArcOutside - arcDiameter / 4;
        int bottomArcInside = bottomArcOutside - arcDiameter / 4;

        mTextPrimaryX = width / 2;
        mTextPrimaryY = height / 2;

        Drawable drawableScissor = ContextCompat.getDrawable(getContext(), R.mipmap.ic_launcher);
        mBitmapScissor = ((BitmapDrawable) drawableScissor).getBitmap();
        mScissorX = (int) ((arcDiameter / 2) * Math.cos(SCISSOR_ANGLE * Math.PI / 180) + leftArcOutSide + arcDiameter / 2 - mBitmapScissor.getWidth() / 2);
        mScissorY = (int) ((arcDiameter / 2) * Math.sin(SCISSOR_ANGLE * Math.PI / 180) + topArcOutside + arcDiameter / 2 - mBitmapScissor.getHeight() / 2);

        Drawable drawableChecked = ContextCompat.getDrawable(getContext(), R.drawable.ic_checked);
        mBitmapChecked = ((BitmapDrawable) drawableChecked).getBitmap();
        Drawable drawableUnChecked = ContextCompat.getDrawable(getContext(), R.drawable.ic_unchecked);
        mBitmapUnChecked = ((BitmapDrawable) drawableUnChecked).getBitmap();

        mArcDiameterMid = arcDiameter * 3 / 8;
        float iconLeft = leftArcOutSide + arcDiameter / 8;
        float iconTop = topArcOutside + arcDiameter / 8;
        mIconsLeft = (int) (iconLeft + mArcDiameterMid);
        mIconsTop = (int) (iconTop + mArcDiameterMid);

        mRectArcOutside.set(leftArcOutSide, topArcOutside, rightArcOutside, bottomArcOutside);
        mRectArcInside.set(leftArcInside, topArcInside, rightArcInside, bottomArcInside);
        mRectProgress.set(leftArcInside, topArcInside, rightArcInside, bottomArcInside);


    }

    public void incrementStep() {
        if (mSweepProgressAngle <= 270) {
            mStepperCount++;
            mSweepProgressAngle += ARC_PROGRESS_PLACEMENT_ANGLE;
            mPrimaryText = String.format(Locale.US, PRIMARY_TEXT, mStepperCount, STATES_COUNT);
            invalidate();
        }

    }

    public void reset() {
        mStepperCount = 0;
        mSweepProgressAngle = 0f;
        mPrimaryText = String.format(Locale.US, PRIMARY_TEXT, mStepperCount, STATES_COUNT);
        invalidate();


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawArc(mRectArcOutside,
                ARC_OUTSIDE_START_ANGLE,
                ARC_OUTSIDE_SWEEP_ANGLE,
                false,
                mPaintArcOutside);

        canvas.drawArc(mRectArcInside,
                ARC_INSIDE_START_ANGLE,
                ARC_INSIDE_SWEEP_ANGLE,
                false,
                mPaintArcInside);

        canvas.drawArc(mRectProgress,
                ARC_PROGRESS_START_ANGLE,
                mSweepProgressAngle,
                false,
                mPaintProgress);

        canvas.drawText(mPrimaryText,
                0,
                mPrimaryText.length(),
                mTextPrimaryX,
                mTextPrimaryY,
                mPaintTextPrimary);


        canvas.drawText(SECONDARY_TEXT,
                0,
                SECONDARY_TEXT.length(),
                mTextPrimaryX,
                mTextPrimaryY + mRectTextPrimary.height() + 10,
                mPaintTextPrimary);

        canvas.drawBitmap(mBitmapScissor,
                mScissorX,
                mScissorY,
                null);

        for (int i = 0; i < STATES_COUNT; i++) {
            Bitmap bitmap = mBitmapUnChecked;
            float angle = ARC_PROGRESS_START_ANGLE + ARC_PROGRESS_PLACEMENT_ANGLE * i;
            int iconLeft = (int) ((mArcDiameterMid) * Math.cos(angle * Math.PI / 180));
            int iconTop = (int) ((mArcDiameterMid) * Math.sin(angle * Math.PI / 180));

            if (i <= mStepperCount) {
                bitmap = mBitmapChecked;
            }
            canvas.drawBitmap(bitmap,
                    mIconsLeft + iconLeft,
                    mIconsTop + iconTop,
                    null);
        }


    }


}
