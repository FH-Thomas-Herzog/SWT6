package swt6.beans;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;

import javax.swing.JComponent;

public class ProgressMeter extends JComponent {

	private static final long serialVersionUID = 1L;

	public enum ShapeType {
		DISC, BAR
	};

	private static final int strokeWidth = 4;
	private Stroke stroke = new BasicStroke(strokeWidth);

	private int min = 0;
	private int max = 100;
	private int value = min;
	private Color backColor = Color.white;
	private Color foreColor = Color.red;
	private Color borderColor = Color.blue;
	private ShapeType shape = ShapeType.DISC;

	// property minimum
	public void setMinimum(int min) {
		this.min = min;
		repaint();
	}

	public int getMinimum() {
		return min;
	}

	// property maximum
	public void setMaximum(int max) {
		this.max = max;
		repaint();
	}

	public int getMaximum() {
		return max;
	}

	// property value
	public void setValue(int val) {
		if (min <= val && val <= max) {
			this.value = val;
			repaint();
		}
	}

	public int getValue() {
		return value;
	}

	// property backgroundColor
	public void setBackgroundColor(Color col) {
		this.backColor = col;
		repaint();
	}

	public Color getBackgroundColor() {
		return this.backColor;
	}

	// property foregroundColor
	public void setForegroundColor(Color col) {
		this.foreColor = col;
		repaint();
	}

	public Color getForegroundColor() {
		return this.foreColor;
	}

	// property borderColor
	public void setBorderColor(Color col) {
		this.borderColor = col;
		repaint();
	}

	public Color getBorderColor() {
		return this.borderColor;
	}

	// property shape
	public void setShape(ShapeType shape) {
		this.shape = shape;
		repaint();
	}

	public ShapeType getShape() {
		return this.shape;
	}

	// On every call to this method the progress meter's value is incremented by
	// 1.
	public void progress() {
		setValue(getValue() + 1);
	}

	// Progress meter is set to it's initial position.
	public void reset() {
		value = min;
		repaint();
	}

	// Draws the progress meter.
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		if (shape == ShapeType.DISC) {
			paintDisc(g2, (double) (value - min) / (max - min), borderColor, foreColor, backColor);
		} else {
			paintBar(g2, (double) (value - min) / (max - min), borderColor, foreColor, backColor);
		}
	}

	// Draw the progress meter in the form of a disc segment.
	private void paintDisc(Graphics2D g, double percFilled, Color borderColor, Color foreColor, Color backColor) {
		if (percFilled >= 0 && percFilled <= 1) {
			int w = this.getWidth() - strokeWidth;
			int h = this.getHeight() - strokeWidth;
			int theta = (int) (360 * percFilled + 0.5);

			g.setStroke(stroke);
			g.setColor(backColor);
			g.fillOval(strokeWidth / 2, strokeWidth / 2, w, h);
			// Draw the ellipse segment with a start angle of 90 deg and a
			// segment angle of theta.
			// A negative value for theta defines a counter clockwise disc
			// segment.
			g.setColor(foreColor);
			g.fillArc(strokeWidth / 2, strokeWidth / 2, w, h, 90, -theta);
			g.setColor(borderColor);
			g.drawOval(strokeWidth / 2, strokeWidth / 2, w, h);
		}
	}

	// Draw the progress meter in form of a bar.
	private void paintBar(Graphics2D g, double percFilled, Color borderColor, Color foreColor, Color backColor) {
		if (percFilled >= 0 && percFilled <= 1) {
			int w = this.getWidth() - strokeWidth;
			int h = this.getHeight() - strokeWidth;
			int val = (int) (w * percFilled);

			g.setStroke(stroke);
			g.setColor(backColor);
			g.fillRect(strokeWidth / 2, strokeWidth / 2, w, h);
			g.setColor(foreColor);
			g.fillRect(strokeWidth / 2, strokeWidth / 2, val, h);
			g.setColor(borderColor);
			g.drawRect(strokeWidth / 2, strokeWidth / 2, w, h);
		}
	}
}
