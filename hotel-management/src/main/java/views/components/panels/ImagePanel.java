package views.components.panels;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImagePanel extends JPanel {
	private Image image;

	private Dimension imageSize;

	public ImagePanel(String filename) {
		super();

		try {
			this.image = ImageIO.read(new File(filename));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ImagePanel(String filename, int width, int height) {
		super();

		try {
			BufferedImage bufferedImage = ImageIO.read(new File(filename));
			this.image = bufferedImage.getScaledInstance(width, height, Image.SCALE_DEFAULT);
			this.imageSize = new Dimension(width, height);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, 0, 0, this);
	}

	public Dimension getImageSize() {
		return imageSize;
	}
}
