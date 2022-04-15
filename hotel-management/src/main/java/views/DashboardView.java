package views;

import shared.ButtonWithResizableIcon;
import shared.panels.ImagePanel;
import utils.Constants;
import utils.RoleManager;
import utils.UtilFunctions;
import views.panels.statistics.StatisticsPanel;
import views.tabbed_panels.RoomManagementTabbed;
import views.tabbed_panels.ServiceManagementTabbed;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class DashboardView extends JPanel {

	// Main frame.
	final private JFrame mainFrame;

	// Components at the left panel.
	private JLabel usernameLabel;
	private ButtonWithResizableIcon roomManagementButton;
	private ButtonWithResizableIcon serviceManagementButton;
	private ButtonWithResizableIcon productManagementButton;
	private ButtonWithResizableIcon employeeManagementButton;
	private ButtonWithResizableIcon statisticsButton;
	private ButtonWithResizableIcon logoutButton;

	// Components at the right panel.
	private RoomManagementTabbed roomManagementTabbed;
	private ServiceManagementTabbed serviceManagementTabbed;
//	private ProductManagementTabbed productManagementTabbed;
//	private EmployeeManagementPanel employeeManagementPanel;
	private StatisticsPanel statisticsPanel;

	public DashboardView(JFrame mainFrame) {
		super();
		this.mainFrame = mainFrame;

		setLayout(null);
		initLeftPanelComponents();
		initRightPanelComponents();
		setPreferredSize(new Dimension(1440, 847));
	}

	private void initLeftPanelComponents() {
		// Left Panel.
		JPanel leftPanel = new JPanel();
		leftPanel.setBounds(0, 0, 320, 847);
		leftPanel.setLayout(null);
		leftPanel.setBackground(Constants.Colors.TERTIARY_90);
		add(leftPanel);

		Border lineBorderRightEdge = BorderFactory.createMatteBorder(0, 0, 0, 1, Color.BLACK);
		leftPanel.setBorder(lineBorderRightEdge);

		// Account Info Panel.
		JPanel accountInfoPanel = new JPanel();
		accountInfoPanel.setBounds(20, 40, 280, 70);
		accountInfoPanel.setLayout(null);
		accountInfoPanel.setBackground(Constants.Colors.TRANSPARENT);
		leftPanel.add(accountInfoPanel);

		// Profile Image.
		ImagePanel profileImage = new ImagePanel(Constants.IconNames.ACCOUNT_CIRCLE_WHITE, 70, 70);
		profileImage.setBounds(0, 0, 70, 70);
		profileImage.setBackground(Constants.Colors.TRANSPARENT);
		accountInfoPanel.add(profileImage);

		// User's Name Label.
		usernameLabel = new JLabel("Username", SwingConstants.LEFT);
		usernameLabel.setBounds(78, 18, 194, 16);
		usernameLabel.setFont(Constants.Fonts.HEADLINE);
		usernameLabel.setForeground(Constants.Colors.WHITE);
		accountInfoPanel.add(usernameLabel);

		// Role Label.
		String roleAsString = RoleManager.getInstance().isEmployee() ? "Employee" : "Manager";
		JLabel roleLabel = new JLabel(roleAsString, SwingConstants.LEFT);
		roleLabel.setBounds(78, 38, 194, 16);
		roleLabel.setFont(new Font(Constants.Fonts.FONT_NAME, Font.PLAIN, 11));
		roleLabel.setForeground(Constants.Colors.WHITE);
		accountInfoPanel.add(roleLabel);

		// Sidebar Items.
		initSidebarItems(leftPanel);

		// Logout Button.
		Icon logoutIcon = new ImageIcon(Constants.IconNames.LOGOUT_WHITE);
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
		int sidebarHeight = RoleManager.getInstance().isEmployee() ? 188 : 324;

		JPanel sidebarItemsPanel = new JPanel();
		sidebarItemsPanel.setBounds(20, 150, 280, sidebarHeight);
		sidebarItemsPanel.setLayout(null);
		sidebarItemsPanel.setBackground(Constants.Colors.TRANSPARENT);
		leftPanel.add(sidebarItemsPanel);

		Dimension iconSize = new Dimension(28, 28);

		Icon roomIcon = new ImageIcon(Constants.IconNames.HOTEL_WHITE);
		roomManagementButton = new ButtonWithResizableIcon("Room Management", roomIcon, iconSize);
		roomManagementButton.setBounds(0, 0, 280, 52);
		UtilFunctions.configureSidebarButtonOnMainThread(roomManagementButton, true);
		sidebarItemsPanel.add(roomManagementButton);

		Icon serviceIcon = new ImageIcon(Constants.IconNames.ROOM_SERVICE_WHITE);
		serviceManagementButton = new ButtonWithResizableIcon("Service Management", serviceIcon, iconSize);
		serviceManagementButton.setBounds(0, 68, 280, 52);
		UtilFunctions.configureSidebarButtonOnMainThread(serviceManagementButton, false);
		sidebarItemsPanel.add(serviceManagementButton);

		Icon productIcon = new ImageIcon(Constants.IconNames.SHOPPING_CART_WHITE);
		productManagementButton = new ButtonWithResizableIcon("Product Management", productIcon, iconSize);
		productManagementButton.setBounds(0, 136, 280, 52);
		UtilFunctions.configureSidebarButtonOnMainThread(productManagementButton, false);
		sidebarItemsPanel.add(productManagementButton);

		if (RoleManager.getInstance().isManager()) {
			Icon employeeIcon = new ImageIcon(Constants.IconNames.BADGE_WHITE);
			employeeManagementButton = new ButtonWithResizableIcon("Employee Management", employeeIcon, iconSize);
			employeeManagementButton.setBounds(0, 204, 280, 52);
			UtilFunctions.configureSidebarButtonOnMainThread(employeeManagementButton, false);
			sidebarItemsPanel.add(employeeManagementButton);

			Icon statisticIcon = new ImageIcon(Constants.IconNames.INSERT_CHART_WHITE);
			statisticsButton = new ButtonWithResizableIcon("Statistics", statisticIcon, iconSize);
			statisticsButton.setBounds(0, 272, 280, 52);
			UtilFunctions.configureSidebarButtonOnMainThread(statisticsButton, false);
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

		initRoomManagementTabbed(rightPanel);
		initServiceManagementTabbed(rightPanel);
//		initProductManagementPanel();

		if (RoleManager.getInstance().isManager()) {
//			initEmployeeManagementPanel();
			initStatisticsPanel(rightPanel);
		}
	}

	private void initRoomManagementTabbed(JPanel panel) {
		// tabbed pane: top (23), left (2), bottom (2), right(2)
		roomManagementTabbed = new RoomManagementTabbed();
		roomManagementTabbed.setBounds(20, 20, 1078, 807);
		panel.add(roomManagementTabbed);

		roomManagementButton.addActionListener(event -> {
			roomManagementTabbed.setVisible(true);
			serviceManagementTabbed.setVisible(false);

			UtilFunctions.switchSidebarButtonActiveStateOnMainThread(roomManagementButton, true);
			UtilFunctions.switchSidebarButtonActiveStateOnMainThread(serviceManagementButton, false);

			if (RoleManager.getInstance().isManager()) {
				statisticsPanel.setVisible(false);
				UtilFunctions.switchSidebarButtonActiveStateOnMainThread(statisticsButton, false);
			}
		});
	}

	private void initServiceManagementTabbed(JPanel panel) {
		// tabbed pane: top (23), left (2), bottom (2), right(2)
		serviceManagementTabbed = new ServiceManagementTabbed();
		serviceManagementTabbed.setBounds(20, 20, 1078, 807);
		serviceManagementTabbed.setVisible(false);
		panel.add(serviceManagementTabbed);

		serviceManagementButton.addActionListener(event -> {
			roomManagementTabbed.setVisible(false);
			serviceManagementTabbed.setVisible(true);

			UtilFunctions.switchSidebarButtonActiveStateOnMainThread(roomManagementButton, false);
			UtilFunctions.switchSidebarButtonActiveStateOnMainThread(serviceManagementButton, true);

			if (RoleManager.getInstance().isManager()) {
				statisticsPanel.setVisible(false);
				UtilFunctions.switchSidebarButtonActiveStateOnMainThread(statisticsButton, false);
			}
		});
	}

//	private void initProductManagementPanel() {
//
//	}
//
//	private void initFacilitiesManagementPanel() {
//
//	}
//
//	private void initEmployeeManagementPanel() {
//
//	}

	private void initStatisticsPanel(JPanel panel) {
		statisticsPanel = new StatisticsPanel();
		statisticsPanel.setBounds(20, 20, 1080, 807);
		statisticsPanel.setBorder(BorderFactory.createLineBorder(Constants.Colors.GRAY, 1));
		statisticsPanel.setVisible(false);
		panel.add(statisticsPanel);

		statisticsButton.addActionListener(event -> {
			roomManagementTabbed.setVisible(false);
			serviceManagementTabbed.setVisible(false);
			statisticsPanel.setVisible(true);

			UtilFunctions.switchSidebarButtonActiveStateOnMainThread(roomManagementButton, false);
			UtilFunctions.switchSidebarButtonActiveStateOnMainThread(serviceManagementButton, false);
			UtilFunctions.switchSidebarButtonActiveStateOnMainThread(statisticsButton, true);
		});
	}

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
