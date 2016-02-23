package watch.oms.omswatch.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.wearable.watchface.CanvasWatchFaceService;
import android.support.wearable.watchface.WatchFaceService;
import android.support.wearable.watchface.WatchFaceStyle;
import android.text.format.Time;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.WindowInsets;



import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import watch.oms.omswatch.MainActivity;
import watch.oms.omswatch.R;

/**
 * Created by paulruiz on 3/25/15.
 */
public class CustomWatchFaceService extends CanvasWatchFaceService {

    @Override
    public Engine onCreateEngine() {
        return new WatchFaceEngine();
    }

    private class WatchFaceEngine extends Engine {

        //Member variables
        private Typeface WATCH_TEXT_TYPEFACE = Typeface.create( Typeface.SERIF, Typeface.NORMAL );

        private static final int MSG_UPDATE_TIME_ID = 42;
        private static final long DEFAULT_UPDATE_RATE_MS = 1000;
        private long mUpdateRateMs = 1000;

        private Time mDisplayTime;

        private Paint mBackgroundColorPaint;
        private Paint mTextColorPaint,mTextTitleColorPaint;

        private boolean mHasTimeZoneReceiverBeenRegistered = false;
        private boolean mIsInMuteMode;
        private boolean mIsLowBitAmbient;

        private float mXOffset;
        private float mYOffset;

        private int mBackgroundColor = Color.parseColor( "#eeeeee" );
        private int mTextColor = Color.parseColor( "red" );

        final BroadcastReceiver mTimeZoneBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mDisplayTime.clear( intent.getStringExtra( "time-zone" ) );
                mDisplayTime.setToNow();
            }
        };

        private final Handler mTimeHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch( msg.what ) {
                    case MSG_UPDATE_TIME_ID: {
                        invalidate();
                        if( isVisible() && !isInAmbientMode() ) {
                            long currentTimeMillis = System.currentTimeMillis();
                            long delay = mUpdateRateMs - ( currentTimeMillis % mUpdateRateMs );
                            mTimeHandler.sendEmptyMessageDelayed( MSG_UPDATE_TIME_ID, delay );
                        }
                        break;
                    }
                }
            }
        };

        //Overridden methods
        @Override
        public void onCreate(SurfaceHolder holder) {
            super.onCreate(holder);
//http://stackoverflow.com/questions/24717289/changing-the-location-of-the-battery-icon-on-a-custom-android-wear-watch-face
            setWatchFaceStyle(new WatchFaceStyle.Builder(CustomWatchFaceService.this)
                            .setBackgroundVisibility(WatchFaceStyle.BACKGROUND_VISIBILITY_INTERRUPTIVE)
                            .setCardPeekMode(WatchFaceStyle.PEEK_MODE_VARIABLE)
                            .setHotwordIndicatorGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL)
                            .setStatusBarGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL)
                            .setShowSystemUiTime(false)
                            .setAcceptsTapEvents(true)
                            .build()
            );


            initBackground();
            initDisplayText();
            initDisplayTitleText();

            mDisplayTime = new Time();
        }


        @Override
        public void onTapCommand(@TapType int tapType, int x, int y, long eventTime) {
            switch(tapType) {
                case WatchFaceService.TAP_TYPE_TAP:
                 /*   Toast.makeText(getApplicationContext(),"TAP_TYPE_TAP",Toast.LENGTH_SHORT).show();*/
                    Intent i = new Intent(CustomWatchFaceService.this,MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    break;
                case WatchFaceService.TAP_TYPE_TOUCH:
                    // Toast.makeText(getApplicationContext(),"TAP_TYPE_TOUCH",Toast.LENGTH_SHORT).show();
                    break;
                case WatchFaceService.TAP_TYPE_TOUCH_CANCEL:
                    // Toast.makeText(getApplicationContext(),"TAP_TYPE_TOUCH_CANCEL",Toast.LENGTH_SHORT).show();
                    break;
                default:
                    super.onTapCommand(tapType, x, y, eventTime);
                    break;
            }
        }

        @Override
        public void onDestroy() {
            mTimeHandler.removeMessages(MSG_UPDATE_TIME_ID);
            super.onDestroy();
        }

        @Override
        public void onVisibilityChanged( boolean visible ) {
            super.onVisibilityChanged(visible);

            if( visible ) {
                if( !mHasTimeZoneReceiverBeenRegistered ) {

                    IntentFilter filter = new IntentFilter( Intent.ACTION_TIMEZONE_CHANGED );
                    CustomWatchFaceService.this.registerReceiver( mTimeZoneBroadcastReceiver, filter );

                    mHasTimeZoneReceiverBeenRegistered = true;
                }

                mDisplayTime.clear( TimeZone.getDefault().getID() );
                mDisplayTime.setToNow();
            } else {
                if( mHasTimeZoneReceiverBeenRegistered ) {
                    CustomWatchFaceService.this.unregisterReceiver( mTimeZoneBroadcastReceiver );
                    mHasTimeZoneReceiverBeenRegistered = false;
                }
            }

            updateTimer();
        }

        @Override
        public void onApplyWindowInsets(WindowInsets insets) {
            super.onApplyWindowInsets(insets);

            mYOffset = getResources().getDimension( R.dimen.y_offset );

            if( insets.isRound() ) {
                mXOffset = getResources().getDimension( R.dimen.x_offset_round );
            } else {
                mXOffset = getResources().getDimension( R.dimen.x_offset_square );
            }
        }

        @Override
        public void onPropertiesChanged(Bundle properties) {
            super.onPropertiesChanged(properties);

            if( properties.getBoolean(PROPERTY_BURN_IN_PROTECTION, false) ) {
                mIsLowBitAmbient = properties.getBoolean(PROPERTY_LOW_BIT_AMBIENT, false);
            }
        }

        @Override
        public void onTimeTick() {
            super.onTimeTick();

            invalidate();
        }

        @Override
        public void onAmbientModeChanged(boolean inAmbientMode) {
            super.onAmbientModeChanged(inAmbientMode);

            if( inAmbientMode ) {
                mTextColorPaint.setColor( Color.parseColor( "white" ) );
                mTextTitleColorPaint.setColor( Color.parseColor( "white" ) );
                mBackgroundColorPaint.setColor(Color.parseColor("black"));
            } else {
                mTextColorPaint.setColor( Color.parseColor( "red" ) );
                mTextTitleColorPaint.setColor( Color.parseColor( "red" ) );
                mBackgroundColorPaint.setColor(Color.parseColor("#eeeeee"));
            }

            if( mIsLowBitAmbient ) {
                mTextColorPaint.setAntiAlias( !inAmbientMode );
                mTextTitleColorPaint.setAntiAlias( !inAmbientMode );
            }

            invalidate();
            updateTimer();
        }

        @Override
        public void onInterruptionFilterChanged(int interruptionFilter) {
            super.onInterruptionFilterChanged(interruptionFilter);

            boolean isDeviceMuted = ( interruptionFilter == android.support.wearable.watchface.WatchFaceService.INTERRUPTION_FILTER_NONE );
            if( isDeviceMuted ) {
                mUpdateRateMs = TimeUnit.MINUTES.toMillis( 1 );

            } else {
                mUpdateRateMs = DEFAULT_UPDATE_RATE_MS;
            }

            if( mIsInMuteMode != isDeviceMuted ) {
                mIsInMuteMode = isDeviceMuted;
                int alpha = ( isDeviceMuted ) ? 100 : 255;
                mTextColorPaint.setAlpha( alpha );
                mTextTitleColorPaint.setAlpha( alpha );
                invalidate();

            }

            updateTimer();
        }

        @Override
        public void onDraw(Canvas canvas, Rect bounds) {
            super.onDraw(canvas, bounds);

            mDisplayTime.setToNow();

            drawBackground( canvas, bounds );
            drawFaceTitle(canvas);
            drawTimeText(canvas);
        }

        //Utility methods
        private void initBackground() {
            mBackgroundColorPaint = new Paint();
            mBackgroundColorPaint.setColor(mBackgroundColor);
        }

        private void initDisplayText() {
            mTextColorPaint = new Paint();
            mTextColorPaint.setColor( mTextColor );
            mTextColorPaint.setTypeface( WATCH_TEXT_TYPEFACE );
            mTextColorPaint.setAntiAlias( true );
            mTextColorPaint.setTextSize(getResources().getDimension(R.dimen.text_size));
        }
private void initDisplayTitleText(){
    mTextTitleColorPaint = new Paint();
    mTextTitleColorPaint.setColor( mTextColor );
    mTextTitleColorPaint.setTypeface( WATCH_TEXT_TYPEFACE );
    mTextTitleColorPaint.setAntiAlias( true );
    mTextTitleColorPaint.setTextSize(25);
}
        private void updateTimer() {
            mTimeHandler.removeMessages(MSG_UPDATE_TIME_ID);
            if( isVisible() && !isInAmbientMode() ) {
                mTimeHandler.sendEmptyMessage( MSG_UPDATE_TIME_ID );
            }
        }

        private void drawBackground( Canvas canvas, Rect bounds ) {
            canvas.drawRect(0, 0, bounds.width(), bounds.height(), mBackgroundColorPaint);
        }

        private void drawTimeText( Canvas canvas ) {
            String timeText = getHourString() + ":" + String.format( "%02d", mDisplayTime.minute );
            if( isInAmbientMode() || mIsInMuteMode ) {
                timeText += ( mDisplayTime.hour < 12 ) ? "AM" : "PM";
            } else {
                timeText += String.format( ":%02d", mDisplayTime.second);
            }
            canvas.drawText(timeText, mXOffset+10, mYOffset+40, mTextColorPaint);
        }

        private void drawFaceTitle( Canvas canvas ) {
          /*  String timeText = getHourString() + ":" + String.format( "%02d", mDisplayTime.minute );
            if( isInAmbientMode() || mIsInMuteMode ) {
                timeText += ( mDisplayTime.hour < 12 ) ? "AM" : "PM";
            } else {
                timeText += String.format( ":%02d", mDisplayTime.second);
            }*/
            canvas.drawText( "OMS", mXOffset+30, mYOffset-10, mTextTitleColorPaint );
        }

        private String getHourString() {
            if( mDisplayTime.hour % 12 == 0 )
                return "12";
            else if( mDisplayTime.hour <= 12 )
                return String.valueOf( mDisplayTime.hour );
            else
                return String.valueOf( mDisplayTime.hour - 12 );
        }
    }

}
