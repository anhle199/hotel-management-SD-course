package shared;

import utils.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class RoundedButton extends JButton {

	private final int cornerRadius;
	private final int borderWidth;
	private final Color borderColor;
	private Shape shape;

	public RoundedButton(String label, int cornerRadius) {
		this(label, cornerRadius, 0, Constants.Colors.SECONDARY);
	}

	public RoundedButton(String label, int cornerRadius, int borderWidth, Color borderColor) {
		super(label);
		this.cornerRadius = cornerRadius;
		this.borderWidth = Math.max(borderWidth, 0);
		this.borderColor = borderColor;

		setContentAreaFilled(false);
		setFocusPainted(false);
		setRolloverEnabled(false);
	}

	public void setIcon(Icon icon, Dimension size) {
		Image originalImage = ((ImageIcon) icon).getImage();
		Image scaledImage = originalImage.getScaledInstance(size.width, size.height, Image.SCALE_SMOOTH);

		setIconTextGap(12);
		setIcon(new ImageIcon(scaledImage));
	}

	protected void paintComponent(Graphics graphics) {
		if (getModel().isArmed()) {
			graphics.setColor(Color.lightGray);
		} else {
			graphics.setColor(getBackground());
		}
		graphics.fillRoundRect(
				borderWidth / 2, borderWidth / 2,
				getWidth() - borderWidth, getHeight() - borderWidth,
				cornerRadius, cornerRadius
		);

		super.paintComponent(graphics);
	}

	protected void paintBorder(Graphics graphics) {
		if (borderWidth > 0) {
			graphics.setColor(borderColor);
			graphics.drawRoundRect(
					borderWidth / 2, borderWidth / 2,
					getWidth() - borderWidth, getHeight() - borderWidth,
					cornerRadius, cornerRadius
			);
		}
	}

	public boolean contains(int x, int y) {
		if (shape == null || !shape.getBounds().equals(getBounds())) {
			shape = new RoundRectangle2D.Float(
					(float) (borderWidth / 2),
					(float) (borderWidth / 2),
					getWidth() - borderWidth,
					getHeight() - borderWidth,
					cornerRadius,
					cornerRadius
			);
		}

		return shape.contains(x, y);
	}

}
