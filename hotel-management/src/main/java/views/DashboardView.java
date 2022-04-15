package views;

import shared.ButtonWithResizableIcon;
import shared.panels.ImagePanel;
import utils.Constants;
import views.panels.EmployeeManagementPanel;
import views.tabbed_panels.ProductManagementTabbed;
import views.tabbed_panels.RoomManagementTabbed;
import views.tabbed_panels.ServiceManagementTabbed;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class DashboardView extends JPanel {
	private final Constants.Role role;

	// Main frame.
	final private JFrame mainFrame;

	// Components at the left panel.
	private JLabel usernameLabel;
	private JLabel roleLabel;
	private ButtonWithResizableIcon roomManagementButton;
	private ButtonWithResizableIcon serviceManagementButton;
	private ButtonWithResizableIcon productManagementButton;
	private ButtonWithResizableIcon facilitiesManagementButton;
	private ButtonWithResizableIcon employeeManagementButton;
	private ButtonWithResizableIcon statisticsButton;
	private ButtonWithResizableIcon logoutButton;

	// Components at the right panel.
	private RoomManagementTabbed roomManagementTabbed;
	private ServiceManagementTabbed serviceManagementTabbed;
	private ProductManagementTabbed productManagementTabbed;
//	private FacilitiesManagementPanel facilitiesManagementPanel;
	private EmployeeManagementPanel employeeManagementPanel;
//	private StatisticsPanel statisticsPanel;

	public DashboardView(JFrame mainFrame, Constants.Role role) {
		super();
		this.role = role;
		this.mainFrame = mainFrame;

		setLayout(null);
		initLeftPanelComponents();
		initRightPanelComponents();
		setPreferredSize(new Dimension(1440, 847));
	}

	private void initLeftPanelComponents() {
		// Left panel
		JPanel leftPanel = new JPanel();
		leftPanel.setBounds(0, 0, 320, 847);
		leftPanel.setLayout(null);
		leftPanel.setBackground(Constants.Colors.TERTIARY_90);
		add(leftPanel);

		Border lineBorderRightEdge = BorderFactory.createMatteBorder(0, 0, 0, 1, Color.BLACK);
		leftPanel.setBorder(lineBorderRightEdge);

		JPanel accountInfoPanel = new JPanel();
		accountInfoPanel.setBounds(20, 40, 280, 70);
		accountInfoPanel.setLayout(null);
		accountInfoPanel.setBackground(Constants.Colors.TRANSPARENT);
		leftPanel.add(accountInfoPanel);

		// Profile image
		ImagePanel profileImage = new ImagePanel(Constants.APP_ICON_ROUNDED_BORDER, 70, 70);
		profileImage.setBounds(0, 0, 70, 70);
		profileImage.setBackground(Constants.Colors.TRANSPARENT);
		accountInfoPanel.add(profileImage);

		// User name label
		usernameLabel = new JLabel("Username", SwingConstants.LEFT);
		usernameLabel.setBounds(78, 18, 194, 16);
		usernameLabel.setFont(Constants.Fonts.HEADLINE);
		usernameLabel.setForeground(Constants.Colors.WHITE);
		accountInfoPanel.add(usernameLabel);

		// Role label
		roleLabel = new JLabel("Role", SwingConstants.LEFT);
		roleLabel.setBounds(78, 38, 194, 16);
		roleLabel.setFont(new Font(Constants.Fonts.FONT_NAME, Font.PLAIN, 11));
		roleLabel.setForeground(Constants.Colors.WHITE);
		accountInfoPanel.add(roleLabel);

		initSidebarItems(leftPanel);

		Icon logoutIcon = new ImageIcon(Constants.IconNames.LOGOUT);
		Dimension iconSize = new Dimension(28, 28);
		logoutButton = new ButtonWithResizableIcon("Logout", logoutIcon, iconSize);
		logoutButton.setBounds(20, 775, 280, 52);
		logoutButton.setBorder(BorderFactory.createLineBorder(Constants.Colors.WHITE, 1));
		logoutButton.setFont(Constants.Fonts.HEADLINE);
		logoutButton.setBackground(Constants.Colors.TERTIARY);
		logoutButton.setForeground(Constants.Colors.WHITE);
		leftPanel.add(logoutButton);
	}

	private void initSidebarItems(JPanel leftPanel) {
		int sidebarHeight = role == Constants.Role.MANAGER ? 392 : 256;
		JPanel sidebarItemsPanel = new JPanel();
		sidebarItemsPanel.setBounds(20, 150, 280, sidebarHeight);
		sidebarItemsPanel.setLayout(null);
		sidebarItemsPanel.setBackground(Constants.Colors.TRANSPARENT);
		leftPanel.add(sidebarItemsPanel);

		Dimension iconSize = new Dimension(28, 28);

		Icon roomIcon = new ImageIcon(Constants.IconNames.HOTEL);
		roomManagementButton = new ButtonWithResizableIcon("Room Management", roomIcon, iconSize);
		roomManagementButton.setBounds(0, 0, 280, 52);
		roomManagementButton.setFont(Constants.Fonts.HEADLINE);
		roomManagementButton.setBackground(Constants.Colors.SECONDARY);
		roomManagementButton.setHorizontalAlignment(SwingConstants.LEFT);
		roomManagementButton.setForeground(Color.WHITE);
		sidebarItemsPanel.add(roomManagementButton);

		Icon serviceIcon = new ImageIcon(Constants.IconNames.ROOM_SERVICE);
		serviceManagementButton = new ButtonWithResizableIcon("Service Management", serviceIcon, iconSize);
		serviceManagementButton.setBounds(0, 68, 280, 52);
		serviceManagementButton.setFont(Constants.Fonts.HEADLINE);
		serviceManagementButton.setBackground(Constants.Colors.SECONDARY);
		serviceManagementButton.setContentAreaFilled(false);
		serviceManagementButton.setHorizontalAlignment(SwingConstants.LEFT);
		serviceManagementButton.setForeground(Constants.Colors.LIGHT_GRAY);
		sidebarItemsPanel.add(serviceManagementButton);

		Icon productIcon = new ImageIcon(Constants.IconNames.SHOPPING_CART);
		productManagementButton = new ButtonWithResizableIcon("Product Management", productIcon, iconSize);
		productManagementButton.setBounds(0, 136, 280, 52);
		productManagementButton.setFont(Constants.Fonts.HEADLINE);
		productManagementButton.setBackground(Constants.Colors.SECONDARY);
		productManagementButton.setContentAreaFilled(false);
		productManagementButton.setHorizontalAlignment(SwingConstants.LEFT);
		productManagementButton.setForeground(Constants.Colors.LIGHT_GRAY);
		sidebarItemsPanel.add(productManagementButton);

		Color t = Constants.Colors.SECONDARY;

		Icon facilityIcon = new ImageIcon(Constants.IconNames.HOME_REPAIR_SERVICE);
		facilitiesManagementButton = new ButtonWithResizableIcon("Facilities Management", facilityIcon, iconSize);
		facilitiesManagementButton.setBounds(0, 204, 280, 52);
		facilitiesManagementButton.setFont(Constants.Fonts.HEADLINE);
		facilitiesManagementButton.setBackground(Constants.Colors.SECONDARY);
		facilitiesManagementButton.setContentAreaFilled(false);
		facilitiesManagementButton.setHorizontalAlignment(SwingConstants.LEFT);
		facilitiesManagementButton.setForeground(Constants.Colors.LIGHT_GRAY);
		sidebarItemsPanel.add(facilitiesManagementButton);

		if (role == Constants.Role.MANAGER) {
			Icon employeeIcon = new ImageIcon(Constants.IconNames.BADGE);
			employeeManagementButton = new ButtonWithResizableIcon("Employee Management", employeeIcon, iconSize);
			employeeManagementButton.setBounds(0, 276, 280, 52);
			employeeManagementButton.setFont(Constants.Fonts.HEADLINE);
			employeeManagementButton.setBackground(Constants.Colors.SECONDARY);
			employeeManagementButton.setContentAreaFilled(false);
			employeeManagementButton.setHorizontalAlignment(SwingConstants.LEFT);
			employeeManagementButton.setForeground(Constants.Colors.LIGHT_GRAY);
			sidebarItemsPanel.add(employeeManagementButton);

			Icon statisticIcon = new ImageIcon(Constants.IconNames.INSERT_CHART);
			statisticsButton = new ButtonWithResizableIcon("Statistics", statisticIcon, iconSize);
			statisticsButton.setBounds(0, 348, 280, 52);
			statisticsButton.setFont(Constants.Fonts.HEADLINE);
			statisticsButton.setBackground(Constants.Colors.SECONDARY);
			statisticsButton.setContentAreaFilled(false);
			statisticsButton.setHorizontalAlignment(SwingConstants.LEFT);
			statisticsButton.setForeground(Constants.Colors.LIGHT_GRAY);
			sidebarItemsPanel.add(statisticsButton);
		}
	}

	private void initRightPanelComponents() {
		// Right panel
		JPanel rightPanel = new JPanel();
		rightPanel.setBounds(320, 0, 1120, 847);
		rightPanel.setLayout(null);
		rightPanel.setBackground(Constants.Colors.TERTIARY_50);
		add(rightPanel);

//		initRoomManagementTabbed(rightPanel);
//		initServiceManagementTabbed(rightPanel);
//		initProductManagementPanel(rightPanel);
//		initFacilitiesManagementPanel();
		initEmployeeManagementPanel(rightPanel);
//		initStatisticsPanel();
	}

//	private void initRoomManagementTabbed(JPanel panel) {
//		// tabbed pane: top (23), left (2), bottom (2), right(2)
//		roomManagementTabbed = new RoomManagementTabbed();
//		roomManagementTabbed.setBounds(20, 20, 1078, 807);
//		panel.add(roomManagementTabbed);
//	}

	private void initServiceManagementTabbed(JPanel panel) {
		// tabbed pane: top (23), left (2), bottom (2), right(2)
		serviceManagementTabbed = new ServiceManagementTabbed();
		serviceManagementTabbed.setBounds(20, 20, 1078, 807);
		panel.add(serviceManagementTabbed);
	}

	private void initProductManagementPanel(JPanel panel) {
		// tabbed pane: top (23), left (2), bottom (2), right(2)
		productManagementTabbed = new ProductManagementTabbed();
		productManagementTabbed.setBounds(20, 20, 1078, 807);
		panel.add(productManagementTabbed);
	}
//
//	private void initFacilitiesManagementPanel() {
//
//	}
//
	private void initEmployeeManagementPanel(JPanel panel) {
		// tabbed pane: top (23), left (2), bottom (2), right(2)
		employeeManagementPanel = new EmployeeManagementPanel();
		employeeManagementPanel.setBounds(20, 20, 1078, 807);
		panel.add(employeeManagementPanel);
	}
//
//	private void initStatisticsPanel() {
//
//	}

	public void display() {
		mainFrame.setResizable(false);
		mainFrame.setContentPane(this);
		mainFrame.pack();
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setVisible(true);
	}

	public JFrame getMainFrame() {
		return mainFrame;
	}

}
