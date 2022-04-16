package views.components;

import javax.swing.*;
import java.awt.*;

public class ButtonWithResizableIcon extends JButton {
	public ButtonWithResizableIcon(String label, Icon icon, Dimension iconSize) {
		super(label);

		setIcon(icon, iconSize);
		setIconTextGap(12);

		setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));

		setFocusPainted(false);
		setRolloverEnabled(false);
	}

	public ButtonWithResizableIcon(Icon icon, Dimension iconSize) {
		this("", icon, iconSize);
	}

//	public void setIconSize(Dimension size) {
//		setIcon(getIcon(), size);
//	}

	public void setIcon(Icon icon, Dimension size) {
		Image originalImage = ((ImageIcon) icon).getImage();
		Image scaledImage = originalImage.getScaledInstance(
				size.width,
				size.height,
				Image.SCALE_SMOOTH
		);

		setIcon(new ImageIcon(scaledImage));
	}
}
