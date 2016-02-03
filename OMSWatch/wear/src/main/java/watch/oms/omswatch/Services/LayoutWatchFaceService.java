/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package watch.oms.omswatch.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.support.wearable.watchface.CanvasWatchFaceService;
import android.support.wearable.watchface.WatchFaceService;
import android.support.wearable.watchface.WatchFaceStyle;
import android.text.format.Time;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.TextView;




import java.lang.ref.WeakReference;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import android.support.wearable.watchface.CanvasWatchFaceService;
import android.support.wearable.watchface.WatchFaceService;
import android.support.wearable.watchface.WatchFaceStyle;

import watch.oms.omswatch.MainActivity;
import watch.oms.omswatch.R;

/**
 * Digital watch face with seconds. In ambient mode, the seconds aren't displayed. On devices with
 * low-bit ambient mode, the text is drawn without anti-aliasing in ambient mode.
 */
public class LayoutWatchFaceService extends CanvasWatchFaceService {
    private static final Typeface NORMAL_TYPEFACE =
            Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL);

    /**
     * Update rate in milliseconds for interactive mode. We update once a second since seconds are
     * displayed in interactive mode.
     */
    private static final long INTERACTIVE_UPDATE_RATE_MS = TimeUnit.SECONDS.toMillis(1);

    /**
     * Handler message id for updating the time periodically in interactive mode.
     */
    private static final int MSG_UPDATE_TIME = 0;

    @Override
    public Engine onCreateEngine() {
        return new Engine();
    }

    private class Engine extends CanvasWatchFaceService.Engine {
        private int specW, specH;
        private View myLayout;
        private TextView day, date, month, hour,reminderVal,worklistVal;
        private final Point displaySize = new Point();
        private  String reminderCount = "0";
        private  String worklistCount = "0";
        final Handler mUpdateTimeHandler = new EngineHandler(this);

        final BroadcastReceiver mTimeZoneReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mTime.clear(intent.getStringExtra("time-zone"));
                mTime.setToNow();
            }
        };

        boolean mRegisteredTimeZoneReceiver = false;

        //   Paint mBackgroundPaint;
        //    Paint mTextPaint;

        boolean mAmbient;

        Time mTime;

        float mXOffset;
        float mYOffset;

        /**
         * Whether the display supports fewer bits for each color in ambient mode. When true, we
         * disable anti-aliasing in ambient mode.
         */
        boolean mLowBitAmbient;

        @Override
        public void onTapCommand(@TapType int tapType, int x, int y, long eventTime) {
            switch(tapType) {
                case WatchFaceService.TAP_TYPE_TAP:
                 /*   Toast.makeText(getApplicationContext(),"TAP_TYPE_TAP",Toast.LENGTH_SHORT).show();*/
                    Intent i = new Intent(getApplicationContext(),MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
        public void onCreate(SurfaceHolder holder) {
            super.onCreate(holder);

            setWatchFaceStyle(new WatchFaceStyle.Builder(LayoutWatchFaceService.this)
                    .setCardPeekMode(WatchFaceStyle.PEEK_MODE_VARIABLE)
                    .setBackgroundVisibility(WatchFaceStyle.BACKGROUND_VISIBILITY_INTERRUPTIVE)
                    .setShowSystemUiTime(false)
                    .setAcceptsTapEvents(true)
                    .build());
            Resources resources = LayoutWatchFaceService.this.getResources();
            mYOffset = resources.getDimension(R.dimen.digital_y_offset);

         /*   mBackgroundPaint = new Paint();
            mBackgroundPaint.setColor(resources.getColor(R.color.digital_background));

            mTextPaint = new Paint();*/
            //    mTextPaint = createTextPaint(resources.getColor(R.color.digital_text));

            mTime = new Time();

            // Inflate the layout that we're using for the watch face
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            myLayout = inflater.inflate(R.layout.watchface, null);

            // Load the display spec - we'll need this later for measuring myLayout
            Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE))
                    .getDefaultDisplay();
            display.getSize(displaySize);

            specW = View.MeasureSpec.makeMeasureSpec(displaySize.x,
                    View.MeasureSpec.EXACTLY);
            specH = View.MeasureSpec.makeMeasureSpec(displaySize.y,
                    View.MeasureSpec.EXACTLY);

            // Find some views for later use
         /*   day = (TextView) myLayout.findViewById(R.id.day);
            date = (TextView) myLayout.findViewById(R.id.date);
            month = (TextView) myLayout.findViewById(R.id.month);*/
            hour = (TextView) myLayout.findViewById(R.id.hour);
        /*    minute = (TextView) myLayout.findViewById(R.id.minute);
            second = (TextView) myLayout.findViewById(R.id.second);*/
      //      reminderVal = (TextView)myLayout.findViewById(R.id.reminder);
      //      worklistVal  = (TextView)myLayout.findViewById(R.id.worklist);
          /* TextView reminderLable =  (TextView)myLayout.findViewById(R.id.reminder_label);
            reminderLable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent faceActivity = new Intent(getApplicationContext(), FaceActivity.class);
                    startActivity(faceActivity);
                }
            });*/
            IntentFilter messageFilter = new IntentFilter(Intent.ACTION_SEND);
            MessageReceiver messageReceiver = new MessageReceiver();
            LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(messageReceiver, messageFilter);

            //MessageService.getInstance().startMessageService(getApplicationContext(),"reminderCount");
            //     DataLayerService.getInstance().startDataLayerService(getApplicationContext(), "");
        }

        @Override
        public void onDestroy() {
            mUpdateTimeHandler.removeMessages(MSG_UPDATE_TIME);
            super.onDestroy();
        }

      /*  private Paint createTextPaint(int textColor) {
            Paint paint = new Paint();
            paint.setColor(textColor);
            paint.setTypeface(NORMAL_TYPEFACE);
            paint.setAntiAlias(true);
            return paint;
        }*/

        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);

            if (visible) {
                registerReceiver();

                // Update time zone in case it changed while we weren't visible.
                mTime.clear(TimeZone.getDefault().getID());
                mTime.setToNow();
            } else {
                unregisterReceiver();
            }

            // Whether the timer should be running depends on whether we're visible (as well as
            // whether we're in ambient mode), so we may need to start or stop the timer.
            updateTimer();
        }

        private void registerReceiver() {
            if (mRegisteredTimeZoneReceiver) {
                return;
            }
            mRegisteredTimeZoneReceiver = true;
            IntentFilter filter = new IntentFilter(Intent.ACTION_TIMEZONE_CHANGED);
            LayoutWatchFaceService.this.registerReceiver(mTimeZoneReceiver, filter);
        }

        private void unregisterReceiver() {
            if (!mRegisteredTimeZoneReceiver) {
                return;
            }
            mRegisteredTimeZoneReceiver = false;
            LayoutWatchFaceService.this.unregisterReceiver(mTimeZoneReceiver);
        }

        @Override
        public void onApplyWindowInsets(WindowInsets insets) {
            super.onApplyWindowInsets(insets);
            if (insets.isRound()) {
                // Shrink the face to fit on a round screen
                mYOffset = mXOffset = displaySize.x * 0.1f;
                displaySize.y -= 2 * mXOffset;
                displaySize.x -= 2 * mXOffset;
            } else {
                mXOffset = mYOffset = 0;
            }

            // Recompute the MeasureSpec fields - these determine the actual size of the layout
            specW = View.MeasureSpec.makeMeasureSpec(displaySize.x, View.MeasureSpec.EXACTLY);
            specH = View.MeasureSpec.makeMeasureSpec(displaySize.y, View.MeasureSpec.EXACTLY);
           /* // Load resources that have alternate values for round watches.
            Resources resources = LayoutWatchFaceService.this.getResources();
            boolean isRound = insets.isRound();
            mXOffset = resources.getDimension(isRound
                    ? R.dimen.digital_x_offset_round : R.dimen.digital_x_offset);
            float textSize = resources.getDimension(isRound
                    ? R.dimen.digital_text_size_round : R.dimen.digital_text_size);*/

            //mTextPaint.setTextSize(textSize);
        }

        @Override
        public void onPropertiesChanged(Bundle properties) {
            super.onPropertiesChanged(properties);
            mLowBitAmbient = properties.getBoolean(PROPERTY_LOW_BIT_AMBIENT, false);
        }

        @Override
        public void onTimeTick() {
            super.onTimeTick();
            invalidate();
        }

        @Override
        public void onAmbientModeChanged(boolean inAmbientMode) {
            super.onAmbientModeChanged(inAmbientMode);
            if (mAmbient != inAmbientMode) {
                mAmbient = inAmbientMode;

                // Show/hide the seconds fields
                if (inAmbientMode) {
                    //second.setVisibility(View.GONE);
                   // myLayout.findViewById(R.id.second_label).setVisibility(View.GONE);
                } else {
                    //second.setVisibility(View.VISIBLE);
                 //   myLayout.findViewById(R.id.second_label).setVisibility(View.VISIBLE);
                }

                // Switch between bold & normal font
                Typeface font = Typeface.create("sans-serif-condensed",
                        inAmbientMode ? Typeface.NORMAL : Typeface.BOLD);
                ViewGroup group = (ViewGroup) myLayout;
                for (int i = group.getChildCount() - 1; i >= 0; i--) {
                    // We only get away with this because every child is a TextView
                    if(group.getChildAt(i) instanceof TextView) {
                        ((TextView) group.getChildAt(i)).setTypeface(font);
                    }
                }

                invalidate();
                updateTimer();
            }
        }

        @Override
        public void onDraw(Canvas canvas, Rect bounds) {
            mTime.setToNow();
            //    day.setText(String.format("%ta", mTime.toMillis(false)));
            //   date.setText(String.format("%02d", mTime.monthDay));
            // month.setText(String.format("%02d", mTime.month));

            hour.setText(String.format("%02d", mTime.hour)+":"+String.format("%02d", mTime.minute)+":"+String.format("%02d", mTime.second));
            //minute.setText(String.format("%02d", mTime.minute));
//            reminderVal.setText(reminderCount);
  //          worklistVal.setText(worklistCount);
          /*  if (!mAmbient) {
                second.setText(String.format("%02d", mTime.second));
            }
*/
            // Update the layout
            myLayout.measure(specW, specH);
            myLayout.layout(0, 0, myLayout.getMeasuredWidth(), myLayout.getMeasuredHeight());

            // Draw it to the Canvas
            canvas.drawColor(Color.BLACK);
            canvas.translate(mXOffset, mYOffset);
            myLayout.draw(canvas);

            // Draw the background.
         /*   canvas.drawRect(0, 0, bounds.width(), bounds.height(), mBackgroundPaint);

            // Draw H:MM in ambient mode or H:MM:SS in interactive mode.
            mTime.setToNow();
            String text = mAmbient
                    ? String.format("%d:%02d", mTime.hour, mTime.minute)
                    : String.format("%d:%02d:%02d", mTime.hour, mTime.minute, mTime.second);
            canvas.drawText(text, mXOffset, mYOffset, mTextPaint);*/
        }


        /**
         * Starts the {@link #mUpdateTimeHandler} timer if it should be running and isn't currently
         * or stops it if it shouldn't be running but currently is.
         */
        private void updateTimer() {
            mUpdateTimeHandler.removeMessages(MSG_UPDATE_TIME);
            if (shouldTimerBeRunning()) {
                mUpdateTimeHandler.sendEmptyMessage(MSG_UPDATE_TIME);
            }
        }

        /**
         * Returns whether the {@link #mUpdateTimeHandler} timer should be running. The timer should
         * only run when we're visible and in interactive mode.
         */
        private boolean shouldTimerBeRunning() {
            return isVisible() && !isInAmbientMode();
        }

        /**
         * Handle updating the time periodically in interactive mode.
         */
        private void handleUpdateTimeMessage() {
            invalidate();
            if (shouldTimerBeRunning()) {
                long timeMs = System.currentTimeMillis();
                long delayMs = INTERACTIVE_UPDATE_RATE_MS
                        - (timeMs % INTERACTIVE_UPDATE_RATE_MS);
                mUpdateTimeHandler.sendEmptyMessageDelayed(MSG_UPDATE_TIME, delayMs);
            }
        }

        public class MessageReceiver extends BroadcastReceiver {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getStringExtra("reminder")!=null) {
                    String ReminderCount = intent.getStringExtra("reminder");
                    //int workListCount = intent.getIntExtra("workListCount",0);


                    // Display message in UI
                    String display = "ReminderCount: " + ReminderCount;
                    reminderCount = ReminderCount;
                    myLayout.invalidate();
                    Log.d("TAG", "Couts::::" + display);
                }else if(intent.getStringExtra("worklist")!=null){
                    String workListCount = intent.getStringExtra("worklist");
                    //int workListCount = intent.getIntExtra("workListCount",0);


                    // Display message in UI
                    String display = "workListCount: " + workListCount;
                    worklistCount = workListCount;
                    myLayout.invalidate();
                    Log.d("TAG", "WorkListCount::::" + display);
                }
        /*    processCounts(ReminderCount, workListCount);
            invalidateIfNecessary();*/
                //   mTextView.setText(display);
            }
        }
    }


    private static class EngineHandler extends Handler {
        private final WeakReference<LayoutWatchFaceService.Engine> mWeakReference;

        public EngineHandler(LayoutWatchFaceService.Engine reference) {
            mWeakReference = new WeakReference<>(reference);
        }

        @Override
        public void handleMessage(Message msg) {
            LayoutWatchFaceService.Engine engine = mWeakReference.get();
            if (engine != null) {
                switch (msg.what) {
                    case MSG_UPDATE_TIME:
                        engine.handleUpdateTimeMessage();
                        break;
                }
            }
        }
    }
}
