package shared;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class RoundedButton extends JButton {

	private final int cornerRadius;
	private Shape shape;

	public RoundedButton(String label, int cornerRadius) {
		super(label);
		this.cornerRadius = cornerRadius;

		setContentAreaFilled(false);
	}

	protected void paintComponent(Graphics graphics) {
		if (getModel().isArmed()) {
			graphics.setColor(Color.lightGray);
		} else {
			graphics.setColor(getBackground());
		}
		graphics.fillRoundRect(
				0, 0,
				getWidth() - 1, getHeight() - 1,
				cornerRadius, cornerRadius
		);

		super.paintComponent(graphics);
	}

	protected void paintBorder(Graphics graphics) {
		graphics.setColor(getForeground());
		graphics.drawRoundRect(
				0, 0,
				getWidth() - 1, getHeight() - 1,
				cornerRadius, cornerRadius
		);
	}

	public boolean contains(int x, int y) {
		if (shape == null || !shape.getBounds().equals(getBounds())) {
			shape = new RoundRectangle2D.Float(
					0, 0,
					getWidth() - 1, getHeight() - 1,
					cornerRadius, cornerRadius
			);
		}

		return shape.contains(x, y);
	}

}
