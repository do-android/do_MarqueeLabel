package doext.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.TextView;

public class DoMarqueeTextView extends TextView implements Runnable {
	
	private final static int MOVEMENT_SPEED = 2;
	private String mDirection;
	private int currentScrollX;
	private boolean isStop = false;
	private int textLength;
	private boolean isMeasure = false;
	private Handler handler;

	public DoMarqueeTextView(Context context) {
		super(context);
		handler = new Handler();
	}
	
	public void setDirection(String direction) {
		this.mDirection = direction;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (!isMeasure) {
			setTextLength(getText());
			isMeasure = true;
		}
	}

	protected void setTextLength(CharSequence charSequence) {
		Paint paint = this.getPaint();
		String str = this.getText().toString();
		this.textLength =  (int) paint.measureText(str);
		startScroll();
	}

	@Override
	public void scrollTo(int x, int y) {
		if(x == 0) {
			int speed = MOVEMENT_SPEED - 1;
			if (isRightScroll()) {
				x = getScrollX() - speed;
			} else {
				x = getScrollX() + speed;
			}
			currentScrollX = x;
		}
		super.scrollTo(x, y);
		postInvalidate();
	}

	@Override
	public void run() {
		if (isStop) {
			return;
		}
		if (isRightScroll()) {
			currentScrollX -= MOVEMENT_SPEED;
			scrollTo(currentScrollX, 0);
			if (getScrollX() <= -(this.getWidth())) {
				currentScrollX = textLength;
			}
		} else { //left
			currentScrollX += MOVEMENT_SPEED;
			scrollTo(currentScrollX, 0);
			if (getScrollX() >= textLength) {
				currentScrollX = -getWidth();
			}
		}
		handler.postDelayed(this, 10);
		postInvalidate();
	}
	
	private boolean isRightScroll(){
		if (!TextUtils.isEmpty(mDirection) && "right".equalsIgnoreCase(mDirection)) {
			return true;
		}
		return false;
	}
	
	protected void startScroll() {
		stopScroll();
		if(textLength > 0) {
			isStop = false;
			handler.post(this);
		}
	}

	protected void stopScroll() {
		isStop = true;
		removeMarqueeCallbacks();
	}
	
	protected void removeMarqueeCallbacks() {
		handler.removeCallbacks(this);
	}
}