package views;

import utils.Constants;
import utils.RoleManager;
import utils.UtilFunctions;
import views.components.ButtonWithResizableIcon;
import views.components.panels.ImagePanel;
import views.panels.employees.EmployeeManagementPanel;
import views.panels.statistics.StatisticsPanel;
import views.tabbed_panels.ProductManagementTabbed;
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
	private ImagePanel changePasswordImage;
	private ButtonWithResizableIcon roomManagementButton;
	private ButtonWithResizableIcon serviceManagementButton;
	private ButtonWithResizableIcon productManagementButton;
	private ButtonWithResizableIcon employeeManagementButton;
	private ButtonWithResizableIcon statisticsButton;
	private ButtonWithResizableIcon logoutButton;

	// Components at the right panel.
	private RoomManagementTabbed roomManagementTabbed;
	private ServiceManagementTabbed serviceManagementTabbed;
	private ProductManagementTabbed productManagementTabbed;
	private EmployeeManagementPanel employeeManagementPanel;
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
		accountInfoPanel.setBounds(20, 40, 240, 70);
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
		usernameLabel.setBounds(78, 18, 154, 16);
		usernameLabel.setFont(Constants.Fonts.HEADLINE);
		usernameLabel.setForeground(Constants.Colors.WHITE);
		accountInfoPanel.add(usernameLabel);

		// Role Label.
		String roleAsString = RoleManager.getInstance().isEmployee() ? "Employee" : "Manager";
		JLabel roleLabel = new JLabel(roleAsString, SwingConstants.LEFT);
		roleLabel.setBounds(78, 38, 154, 16);
		roleLabel.setFont(new Font(Constants.Fonts.FONT_NAME, Font.PLAIN, 11));
		roleLabel.setForeground(Constants.Colors.WHITE);
		accountInfoPanel.add(roleLabel);

		// Change Password Image.
		changePasswordImage = new ImagePanel(Constants.IconNames.LOCK_RESET_WHITE, 32, 32);
		changePasswordImage.setBounds(268, 59, 32, 32);
		changePasswordImage.setBackground(Constants.Colors.TRANSPARENT);
		leftPanel.add(changePasswordImage);

		// Sidebar Items.
		initSidebarItems(leftPanel);

		Dimension iconSize = new Dimension(28, 28);

		// Logout Button.
		Icon logoutIcon = new ImageIcon(Constants.IconNames.LOGOUT_WHITE);
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
		initProductManagementPanel(rightPanel);

		if (RoleManager.getInstance().isManager()) {
			initEmployeeManagementPanel(rightPanel);
			initStatisticsPanel(rightPanel);
		}
	}

	private void initRoomManagementTabbed(JPanel panel) {
		// tabbed pane: top (23), left (2), bottom (2), right(2)
		roomManagementTabbed = new RoomManagementTabbed();
		roomManagementTabbed.setBounds(20, 20, 1078, 807);
		panel.add(roomManagementTabbed);
	}

	private void initServiceManagementTabbed(JPanel panel) {
		// tabbed pane: top (23), left (2), bottom (2), right(2)
		serviceManagementTabbed = new ServiceManagementTabbed();
		serviceManagementTabbed.setBounds(20, 20, 1078, 807);
		serviceManagementTabbed.setVisible(false);
		panel.add(serviceManagementTabbed);
	}

	private void initProductManagementPanel(JPanel panel) {
		// tabbed pane: top (23), left (2), bottom (2), right(2)
		productManagementTabbed = new ProductManagementTabbed();
		productManagementTabbed.setBounds(20, 20, 1078, 807);
		productManagementTabbed.setVisible(false);
		panel.add(productManagementTabbed);
	}

	private void initEmployeeManagementPanel(JPanel panel) {
		// tabbed pane: top (23), left (2), bottom (2), right(2)
		employeeManagementPanel = new EmployeeManagementPanel();
		employeeManagementPanel.setBounds(20, 20, 1080, 807);
		employeeManagementPanel.setBorder(BorderFactory.createLineBorder(Constants.Colors.TABLE_BORDER_COLOR, 1));
		employeeManagementPanel.setVisible(false);
		panel.add(employeeManagementPanel);
	}

	private void initStatisticsPanel(JPanel panel) {
		statisticsPanel = new StatisticsPanel();
		statisticsPanel.setBounds(20, 20, 1080, 807);
		statisticsPanel.setBorder(BorderFactory.createLineBorder(Constants.Colors.GRAY, 1));
		statisticsPanel.setVisible(false);
		panel.add(statisticsPanel);
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

	public JLabel getUsernameLabel() {
		return usernameLabel;
	}

	public ImagePanel getChangePasswordImage() {
		return changePasswordImage;
	}

	public ButtonWithResizableIcon getRoomManagementButton() {
		return roomManagementButton;
	}

	public ButtonWithResizableIcon getServiceManagementButton() {
		return serviceManagementButton;
	}

	public ButtonWithResizableIcon getProductManagementButton() {
		return productManagementButton;
	}

	public ButtonWithResizableIcon getEmployeeManagementButton() {
		return employeeManagementButton;
	}

	public ButtonWithResizableIcon getStatisticsButton() {
		return statisticsButton;
	}

	public ButtonWithResizableIcon getLogoutButton() {
		return logoutButton;
	}

	public RoomManagementTabbed getRoomManagementTabbed() {
		return roomManagementTabbed;
	}

	public ServiceManagementTabbed getServiceManagementTabbed() {
		return serviceManagementTabbed;
	}

	public ProductManagementTabbed getProductManagementTabbed() {
		return productManagementTabbed;
	}

	public EmployeeManagementPanel getEmployeeManagementPanel() {
		return employeeManagementPanel;
	}

	public StatisticsPanel getStatisticsPanel() {
		return statisticsPanel;
	}
}
