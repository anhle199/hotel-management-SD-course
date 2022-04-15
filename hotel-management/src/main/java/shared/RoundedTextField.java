package shared;

import utils.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class RoundedTextField extends JTextField {

	private final int cornerRadius;
	private Shape shape;

	public RoundedTextField(String placeholder, int cornerRadius) {
		super();
		this.cornerRadius = cornerRadius;

		setOpaque(false);
	}

	protected void paintComponent(Graphics graphics) {
		graphics.setColor(getBackground());
		graphics.fillRoundRect(
				0, 0,
				getWidth() - 1, getHeight() - 1,
				cornerRadius, cornerRadius
		);

		super.paintComponent(graphics);
	}

	protected void paintBorder(Graphics graphics) {
		graphics.setColor(getBackground());
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
