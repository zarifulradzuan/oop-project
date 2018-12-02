package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Time;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;


import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import controller.LabController;
import controller.LoginController;
import controller.ReservationController;
import controller.UserController;
import net.miginfocom.swing.MigLayout;


public class ProjectMain {
	private static String userID;
	private static String position;
	private static JFrame loginFrame;
	public static void main(String [] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					loginWindow();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	private static void loginWindow() throws Exception{
		loginFrame = new JFrame();
		loginFrame.setVisible(true);
		loginFrame.setBounds(100, 100, 450, 200);
		loginFrame.setResizable(false);
		loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		loginFrame.getContentPane().setLayout(new MigLayout("", "[][][][][grow][][][][grow][][][][]", "[][][][][][][][]"));
		
		JLabel lblLabReservationSystem = new JLabel(".:: L A B  R E S E R V A T I O N  S Y S T E M ::.");
		loginFrame.getContentPane().add(lblLabReservationSystem, "cell 6 1 2 1");
		
		JLabel lblUsername = new JLabel("USERNAME :");
		loginFrame.getContentPane().add(lblUsername, "cell 6 3,alignx trailing");
		
		JTextField fieldUsername = new JTextField();
		loginFrame.getContentPane().add(fieldUsername, "cell 7 3,growx");
		fieldUsername.setColumns(10);
		
		JLabel lblPassword = new JLabel("PASSWORD :");
		loginFrame.getContentPane().add(lblPassword, "cell 6 4,alignx trailing");
		
		JPasswordField fieldPassword = new JPasswordField();
		loginFrame.getContentPane().add(fieldPassword, "cell 7 4,growx");
		
		JButton btnLogin = new JButton("LOGIN");
		LoginController loginController = new LoginController();
		loginFrame.getContentPane().add(btnLogin, "cell 9 7");
		
		JLabel lblIncorrect = new JLabel("Incorrect username or password entered");
		lblIncorrect.setVisible(false);
		loginFrame.getContentPane().add(lblIncorrect, "cell 7 9");
		
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					if(loginController.doLogin(fieldUsername.getText(), fieldPassword.getPassword())) {
						loginFrame.dispose();
						lblIncorrect.setVisible(false);
						fieldUsername.setText("");
						fieldPassword.setText("");
						userID = loginController.getUser().getUserID();
						position = loginController.getUser().getPosition();
						if(position.matches("ADMIN"))
							adminView();
						if(position.matches("STAFF"))
							staffView();
						if(position.matches("LECTURER") || position.matches("STUDENT"))
							userView();
					}
					else
						lblIncorrect.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		});
	}
	
	private static void adminView() {
		JFrame frame = new JFrame();
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setBounds(100, 100, 278, 305);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new MigLayout("", "[][][][][][][][][][][]", "[][][][][][][][][][][]"));
		
		JLabel lblLA = new JLabel(".:: L A B  R E S E R V A T I O N  S Y S T E M ::.");
		frame.getContentPane().add(lblLA, "cell 1 0");
		
		JButton btnAddUser = new JButton("Add User");
		btnAddUser.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				addUser();
			}
		});
		frame.getContentPane().add(btnAddUser, "cell 1 2,growx");
		
		JButton btnAddLab = new JButton("Add Lab");
		btnAddLab.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				addLab();
			}
		});
		frame.getContentPane().add(btnAddLab, "cell 1 3,growx");
		
		JButton btnEditLabInfo = new JButton("Edit Lab Info");
		btnEditLabInfo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				editLab();
			}
		});
		frame.getContentPane().add(btnEditLabInfo, "cell 1 4,growx");
		
		JButton btnViewAllReservation = new JButton("View All Reservation Data");
		btnViewAllReservation.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				viewReservation();
			}
		});
		frame.getContentPane().add(btnViewAllReservation, "cell 1 6,growx");
		
		JButton btnViewGraphOf = new JButton("View Graph");
		btnViewGraphOf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chartMenu();
			}
		});
		frame.getContentPane().add(btnViewGraphOf, "cell 1 7,growx");
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JButton btnLogout = new JButton("Logout");
		btnLogout.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				frame.dispose();
				loginFrame.setVisible(true);
			}
		});
		menuBar.add(btnLogout);
	}

	@SuppressWarnings("serial")
	private static void userView() throws Exception {
		JFrame frame = new JFrame();
		frame.setVisible(true);
		frame.setBounds(100, 100, 728, 403);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(new MigLayout("", "[117.00px][][grow][][][][][]", "[][20px,grow][][][grow][][][][]"));
		
		DefaultTableModel model = new DefaultTableModel() ;
		model.addColumn("Lab Name");
		model.addColumn("Start time");
		model.addColumn("End time");
		model.addColumn("Date");
		model.addColumn("Reason");
		model.addColumn("Approval Status");
		model.addColumn("ID");
		JTable tableReservation = new JTable(model) {
			@Override
			public boolean isCellEditable(int row, int column) {
					return false;
			}
		};
		
		try {
			for(Object[] a : ReservationController.getReservedByUser(userID))
				model.addRow(a);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		tableReservation.setFillsViewportHeight(true);
		tableReservation.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableReservation.setRowSelectionAllowed(false);
		
		tableReservation.getColumnModel().getColumn(0).setResizable(false);
		tableReservation.getColumnModel().getColumn(0).setPreferredWidth(156);
		tableReservation.getColumnModel().getColumn(1).setResizable(false);
		tableReservation.getColumnModel().getColumn(2).setResizable(false);
		tableReservation.getColumnModel().getColumn(3).setResizable(false);
		tableReservation.getColumnModel().getColumn(3).setPreferredWidth(83);
		tableReservation.getColumnModel().getColumn(5).setResizable(false);
		tableReservation.getColumnModel().getColumn(5).setPreferredWidth(98);
		tableReservation.getTableHeader().setReorderingAllowed(false);
		
		JLabel lblLblheader = new JLabel(".:: L A B  R E S E R V A T I O N  S Y S T E M ::.");
		frame.getContentPane().add(lblLblheader, "cell 1 0");
		frame.getContentPane().add(new JScrollPane(tableReservation), "cell 0 1 8 2,grow");
		
		JButton btnNewReservation = new JButton("New Reservation");
		btnNewReservation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newReservation(model);
			}
		});
		frame.getContentPane().add(btnNewReservation, "cell 0 5,growx");
		
		JButton btnDeleteSelected = new JButton("Delete selected");
		frame.getContentPane().add(btnDeleteSelected, "cell 0 6");
		
		tableReservation.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				btnDeleteSelected.setEnabled(true);
			}
		});
		
		btnDeleteSelected.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					if(ReservationController.deleteReservation(tableReservation.getValueAt(tableReservation.getSelectedRow(), 6).toString())>0)
						showMessage("Successfully deleted");
					model.setRowCount(0);
					for(Object[] a : ReservationController.getReservedByUser(userID))
						model.addRow(a);
					btnDeleteSelected.setEnabled(false);
				} catch (Exception e1) {
					e1.printStackTrace();
					showMessage("Failed to delete.");
					btnDeleteSelected.setEnabled(false);
				}
				
			}
		});
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JButton btnLogout= new JButton("Logout");
		menuBar.add(btnLogout);
		btnLogout.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				loginFrame.setVisible(true);
				frame.dispose();
			}
		});
	}
	
	private static void staffView(){
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.setVisible(true);
		frame.setBounds(100, 100, 800, 340);
		frame.setResizable(true);
		
		frame.getContentPane().setLayout(new MigLayout("", "[730px,grow]", "[][277px][][][][][][][]"));
		
		Label lblHeader = new Label(".:: L A B  R E S E R V A T I O N  S Y S T E M ::.");
		frame.getContentPane().add(lblHeader, "cell 0 0");
		
		DefaultTableModel model = new DefaultTableModel() ;
		model.addColumn("Lab Name");
		model.addColumn("Start time");
		model.addColumn("End time");
		model.addColumn("Date");
		model.addColumn("Reserved By");
		model.addColumn("Reason");
		model.addColumn("Approval Status");
		model.addColumn("R.ID");
		@SuppressWarnings("serial")
		JTable tableReservation = new JTable(model) {
			@Override
			public boolean isCellEditable(int row, int column) {
				if(column==6)
					return true;
				else
					return false;
			}
		};
		
		
		tableReservation.setColumnSelectionAllowed(false);
		tableReservation.setEnabled(true);
		JComboBox<String> approvalStatusCombo = new JComboBox<String>();
		approvalStatusCombo.addItem("APPROVED");
		approvalStatusCombo.addItem("DENIED");
		approvalStatusCombo.addItem("IN REVIEW");
		try {
			for(Object[] a : ReservationController.getReservedByStaff(userID,"IN REVIEW")) {
				model.addRow(a);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		tableReservation.getColumnModel().getColumn(6).setCellEditor(new DefaultCellEditor(approvalStatusCombo));
		
		tableReservation.getTableHeader().setReorderingAllowed(false);
		tableReservation.setFillsViewportHeight(true);
		tableReservation.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableReservation.getColumnModel().getColumn(0).setResizable(true);
		tableReservation.getColumnModel().getColumn(0).setPreferredWidth(156);
		tableReservation.getColumnModel().getColumn(1).setResizable(false);
		tableReservation.getColumnModel().getColumn(2).setResizable(false);
		tableReservation.getColumnModel().getColumn(3).setResizable(false);
		tableReservation.getColumnModel().getColumn(3).setPreferredWidth(83);
		tableReservation.getColumnModel().getColumn(4).setResizable(false);
		tableReservation.getColumnModel().getColumn(5).setResizable(true);
		tableReservation.getColumnModel().getColumn(6).setResizable(false);
		tableReservation.getColumnModel().getColumn(6).setPreferredWidth(98);
		tableReservation.getColumnModel().getColumn(7).setResizable(false);
		tableReservation.getColumnModel().getColumn(7).setPreferredWidth(25);
		tableReservation.getModel().addTableModelListener(new TableModelListener() {
		      public void tableChanged(TableModelEvent e) {
		         try {
					if(ReservationController.changeStatus(tableReservation.getModel().getValueAt(e.getFirstRow(), 7).toString(), 
							tableReservation.getModel().getValueAt(e.getFirstRow(), e.getColumn()).toString())>0)
					showMessage("Successfully changed.");
					else
						showMessage("Error occurred.");
				} catch (Exception e1) {
					e1.printStackTrace();
				}
		      }
		    });
		
		
		JScrollPane scrollPane = new JScrollPane(tableReservation);
		frame.getContentPane().add(scrollPane, "cell 0 1,grow");
		
		JButton btnDeleteSelected = new JButton("Delete selected");
		
		btnDeleteSelected.setEnabled(false);
		
		tableReservation.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				btnDeleteSelected.setEnabled(true);
			}
		});
		
		btnDeleteSelected.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					if(ReservationController.deleteReservation(tableReservation.getValueAt(tableReservation.getSelectedRow(), 7).toString())>0)
						showMessage("Successfully deleted");
					model.setRowCount(0);
					for(Object[] a : ReservationController.getReservedByStaff(userID,"IN REVIEW"))
						model.addRow(a);
					btnDeleteSelected.setEnabled(false);
				} catch (Exception e1) {
					e1.printStackTrace();
					showMessage("Failed to delete.");
					btnDeleteSelected.setEnabled(false);
				}
				
			}
		});
		JButton btnAddNewReservation = new JButton("Add new reservation");
		btnAddNewReservation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newReservation(model);
			}
		});
		frame.getContentPane().add(btnAddNewReservation, "flowx,cell 0 5");
		
		frame.getContentPane().add(btnDeleteSelected, "cell 0 6");
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JButton btnLogout= new JButton("Logout");
		menuBar.add(btnLogout);
		btnLogout.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				frame.dispose();
				loginFrame.setVisible(true);
			}
		});
		
		JButton btnViewApproved = new JButton("View approved");
		btnViewApproved.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {	
				try {
						model.setRowCount(0);
						for(Object[] a : ReservationController.getReservedByStaff(userID,"APPROVED")) {
							model.addRow(a);
						}
					} catch (Exception e1) {
						e1.printStackTrace();
					}
			}
		});
		menuBar.add(btnViewApproved);
		
		JButton btnViewPending = new JButton("View Pending");
		btnViewPending.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					model.setRowCount(0);
					for(Object[] a : ReservationController.getReservedByStaff(userID,"IN REVIEW")) {
						model.addRow(a);
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		menuBar.add(btnViewPending);
		
		JButton btnViewDenied = new JButton("View Denied");
		btnViewDenied.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					model.setRowCount(0);
					for(Object[] a : ReservationController.getReservedByStaff(userID,"DENIED")) {
						model.addRow(a);
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		menuBar.add(btnViewDenied);
	}

	private static void chartMenu() {
		JFrame frame = new JFrame();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setBounds(100, 100, 527, 202);
		frame.setResizable(true);
		frame.getContentPane().setLayout(new MigLayout("", "[488.00px]", "[22px][20px,grow][][]"));
		
		Label lblHeader = new Label(".:: L A B  R E S E R V A T I O N  S Y S T E M ::.");
		frame.getContentPane().add(lblHeader, "cell 0 0,alignx left,aligny top");
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, "cell 0 1 1 2,grow");
		panel.setLayout(new MigLayout("", "[78.00px][136.00]", "[20px][][][][][][][][]"));
		
		JLabel lblChartType = new JLabel("Chart Type:");
		panel.add(lblChartType, "cell 0 0,alignx trailing");
		
		JComboBox<String> typeOfChart = new JComboBox<String>();//						0					1	bar										2 pie														3pie																			4pie												5line									6line		
		typeOfChart.setModel(new DefaultComboBoxModel<String>(new String[] {"Choose type of chart","Status of reservations from all lab" ,"Comparison of amount of reservations among all lab","Comparison of amount of reservations approved among all lab", "Comparison of amount of reservations denied among all lab", "Amount of denied reservations over time","Amount of approved reservations over time"}));
		panel.add(typeOfChart, "cell 1 0,growx");
		
		JLabel lblLabName = new JLabel("Lab Name:");
		panel.add(lblLabName, "cell 0 1,alignx trailing");
		
		JComboBox<String> labName = new JComboBox<String>();
		
		labName.setEnabled(false);
		panel.add(labName, "cell 1 1,growx,aligny top");
		labName.addItem("Choose lab..");
		
		
		JButton btnShowChart = new JButton("Show chart");
		btnShowChart.setEnabled(false);
		btnShowChart.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				try {
					ChartGenerator.createChart(typeOfChart.getSelectedIndex(), typeOfChart.getSelectedItem().toString(),labName.getSelectedItem().toString());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});		
		panel.add(btnShowChart, "cell 1 3,alignx trailing");
		
		labName.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if(labName.getSelectedIndex()==0)
					btnShowChart.setEnabled(false);
				else
					btnShowChart.setEnabled(true);
			}
		});
		typeOfChart.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if(typeOfChart.getSelectedIndex()==0) {
					labName.removeAllItems();
					labName.addItem("Choose lab..");
					btnShowChart.setEnabled(false);
					labName.setEnabled(false);
				}
				else if(typeOfChart.getSelectedIndex()<5) {
					labName.removeAllItems();
					labName.setEnabled(false);
					labName.addItem("Choose lab..");
					btnShowChart.setEnabled(true);
				}
				else if(typeOfChart.getSelectedIndex()>4 ) {
					labName.removeAllItems();
					labName.setEnabled(true);
					btnShowChart.setEnabled(false);
					labName.addItem("Choose lab..");
					try {
						for(String lName : LabController.allLabName())
							labName.addItem(lName);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		
	}

	private static void newReservation(DefaultTableModel model) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		frame.setVisible(true);
		frame.setBounds(100, 100, 500, 340);
		frame.getContentPane().setLayout(new MigLayout("", "[][][][][grow][][13.00][231.00][][63.00][][][grow][][][][]", "[][][][][][20.00][][][58.00][]"));
		
		JLabel lblLabReservationSystem = new JLabel(".:: L A B  R E S E R V A T I O N  S Y S T E M ::.");
		frame.getContentPane().add(lblLabReservationSystem, "cell 6 1 2 1");
		
		JLabel lblDate = new JLabel("Date:");
		frame.getContentPane().add(lblDate, "cell 6 2,alignx right");
		
		JSpinner dateInput = new JSpinner();
		dateInput.setModel(new SpinnerDateModel(new java.util.Date(), null,null, Calendar.DATE));
		dateInput.setEditor(new JSpinner.DateEditor(dateInput,"yyyy-MM-dd"));
		frame.getContentPane().add(dateInput, "cell 7 2,growx");
		
		JLabel lblTimeStart = new JLabel("Time start:");
		frame.getContentPane().add(lblTimeStart, "cell 6 3,alignx right");
		JSpinner timeInputStart = new JSpinner();
		timeInputStart.setModel(new SpinnerDateModel(new java.util.Date(), null,null, Calendar.HOUR_OF_DAY));
		timeInputStart.setEditor(new JSpinner.DateEditor(timeInputStart,"HH:mm"));
		frame.getContentPane().add(timeInputStart, "cell 7 3,growx");		
		
		JLabel lblTimeEnd= new JLabel("Time end:");
		frame.getContentPane().add(lblTimeEnd, "cell 6 4,alignx right");		
		JSpinner timeInputEnd = new JSpinner();
		
		timeInputEnd.setModel(new SpinnerDateModel(new java.util.Date(), null,null, Calendar.HOUR_OF_DAY));
		timeInputEnd.setEditor(new JSpinner.DateEditor(timeInputEnd,"HH:mm"));
		frame.getContentPane().add(timeInputEnd, "cell 7 4,growx");
		
		JLabel lblLabName = new JLabel("Lab Name:");
		frame.getContentPane().add(lblLabName, "cell 6 6,alignx right");
		
		JComboBox<String> labName = new JComboBox<String>();
		labName.setEnabled(false);
		frame.getContentPane().add(labName, "cell 7 6,growx");
		
		JLabel lblLabType = new JLabel("Lab Type:");
		frame.getContentPane().add(lblLabType, "cell 6 5,alignx right");
		JComboBox<String> labType = new JComboBox<String>();
		
		labType.setModel(new DefaultComboBoxModel<String>(new String[] {"Choose...", "Artificial Intelligence", "Multimedia", "Networking", "Software Engineering", "Database", "Gaming"}));
		frame.getContentPane().add(labType, "cell 7 5,growx");

		
		JLabel lblPurpose = new JLabel("Purpose :");
		frame.getContentPane().add(lblPurpose, "cell 6 7,alignx right");
		JTextArea purposePane = new JTextArea();
		purposePane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		purposePane.setLineWrap(true);
		frame.getContentPane().add(purposePane, "cell 7 7 1 2,grow");
		
		JButton btnSubmit = new JButton("NEXT");
		btnSubmit.setEnabled(false);
		
		frame.getContentPane().add(btnSubmit, "cell 9 9");
		//Action listeners
		btnSubmit.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e) {
				try {
					ReservationController reservationController = new ReservationController(LabController.searchLabByName(labName.getSelectedItem().toString()));
					SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm:ss");
					Time timeStart = Time.valueOf(formatTime.format(timeInputStart.getValue()));
					timeStart.setSeconds(0);
					Time timeEnd = Time.valueOf(formatTime.format(timeInputEnd.getValue()));
					timeEnd.setSeconds(0);
					reservationController.addNewReservation(timeStart, timeEnd, Date.valueOf(new SimpleDateFormat("yyyy-MM-dd").format(dateInput.getValue())), userID, purposePane.getText(),position);
					showMessage("Successfully placed reservation.");
					model.setRowCount(0);
					if(position=="STAFF")
						for(Object[] a : ReservationController.getReservedByStaff(userID,"IN REVIEW"))
							model.addRow(a);
					else
						for(Object[] a : ReservationController.getReservedByUser(userID))
							model.addRow(a);
					frame.dispose();
				} catch(Exception reservationE) {
					reservationE.printStackTrace();
					showMessage("An error occurred, check that the database is connected.");
				}
			}
		});
		dateInput.addChangeListener(new ChangeListener() {
			@SuppressWarnings("deprecation")
			public void stateChanged(ChangeEvent arg0) {
				labName.setEnabled(true);
				labName.removeAllItems();
				try {
					ReservationController reservationController;
					for(model.Lab lab : LabController.searchLabByType(labType.getSelectedItem().toString())) {
						reservationController = new ReservationController(lab);
						SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm:ss");
						Time timeStart = Time.valueOf(formatTime.format(timeInputStart.getValue()));
						timeStart.setSeconds(0);
						Time timeEnd = Time.valueOf(formatTime.format(timeInputEnd.getValue()));
						timeEnd.setSeconds(0);
						if(reservationController.checkIfAvailable(timeStart, timeEnd, Date.valueOf(new SimpleDateFormat("yyyy-MM-dd").format(dateInput.getValue())))) 
							labName.addItem(lab.getName());
					}
					if(labName.getItemCount()==0) {
						labName.addItem("No lab available for selected time");
						labName.setEnabled(false);
						btnSubmit.setEnabled(false);
					}
					else
						btnSubmit.setEnabled(true);
				} catch (Exception e) {
					labName.setEnabled(false);
					labName.addItem("No lab available for selected time");
					btnSubmit.setEnabled(false);
				}
			}
		});
		timeInputStart.addChangeListener(new ChangeListener() {
			@SuppressWarnings("deprecation")
			public void stateChanged(ChangeEvent arg0) {
				labName.setEnabled(true);
				labName.removeAllItems();
				try {
					ReservationController reservationController;
					for(model.Lab lab : LabController.searchLabByType(labType.getSelectedItem().toString())) {
						reservationController = new ReservationController(lab);
						SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm:ss");
						Time timeStart = Time.valueOf(formatTime.format(timeInputStart.getValue()));
						timeStart.setSeconds(0);
						Time timeEnd = Time.valueOf(formatTime.format(timeInputEnd.getValue()));
						timeEnd.setSeconds(0);
						if(reservationController.checkIfAvailable(timeStart, timeEnd, Date.valueOf(new SimpleDateFormat("yyyy-MM-dd").format(dateInput.getValue())))) 
							labName.addItem(lab.getName());
					}
					if(labName.getItemCount()==0) {
						labName.addItem("No lab available for selected time");
						labName.setEnabled(false);btnSubmit.setEnabled(false);
					}
					else
						btnSubmit.setEnabled(true);
				} catch (Exception e) {
					labName.setEnabled(false);
					labName.addItem("No lab available for selected time");btnSubmit.setEnabled(false);
				}
			}
		});
		timeInputEnd.addChangeListener(new ChangeListener() {
			@SuppressWarnings("deprecation")
			public void stateChanged(ChangeEvent arg0) {
				labName.setEnabled(true);
				labName.removeAllItems();
				try {
					ReservationController reservationController;
					for(model.Lab lab : LabController.searchLabByType(labType.getSelectedItem().toString())) {
						reservationController = new ReservationController(lab);
						SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm:ss");
						Time timeStart = Time.valueOf(formatTime.format(timeInputStart.getValue()));
						timeStart.setSeconds(0);
						Time timeEnd = Time.valueOf(formatTime.format(timeInputEnd.getValue()));
						timeEnd.setSeconds(0);
						if(reservationController.checkIfAvailable(timeStart, timeEnd, Date.valueOf(new SimpleDateFormat("yyyy-MM-dd").format(dateInput.getValue())))) 
							labName.addItem(lab.getName());
					}
					if(labName.getItemCount()==0) {
						labName.addItem("No lab available for selected time");
						labName.setEnabled(false);btnSubmit.setEnabled(false);
					}
					else
						btnSubmit.setEnabled(true);
				} catch (Exception e) {
					labName.setEnabled(false);
					labName.addItem("No lab available for selected time");btnSubmit.setEnabled(false);
				}
			}
		});
		labType.addItemListener(new ItemListener() {
			@SuppressWarnings("deprecation")
			public void itemStateChanged(ItemEvent arg0) {
				labName.setEnabled(true);
				labName.removeAllItems();
				try {
					ReservationController reservationController;
					for(model.Lab lab : LabController.searchLabByType(labType.getSelectedItem().toString())) {
						reservationController = new ReservationController(lab);
						SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm:ss");
						Time timeStart = Time.valueOf(formatTime.format(timeInputStart.getValue()));
						timeStart.setSeconds(0);
						Time timeEnd = Time.valueOf(formatTime.format(timeInputEnd.getValue()));
						timeEnd.setSeconds(0);
						if(reservationController.checkIfAvailable(timeStart, timeEnd, Date.valueOf(new SimpleDateFormat("yyyy-MM-dd").format(dateInput.getValue())))) 
							labName.addItem(lab.getName());
					}
					if(labName.getItemCount()==0) {
						labName.addItem("No lab available for selected time");
						labName.setEnabled(false);btnSubmit.setEnabled(false);
					}
					else
						btnSubmit.setEnabled(true);
				} catch (Exception e) {
					labName.setEnabled(false);
					labName.addItem("No lab available for selected time");btnSubmit.setEnabled(false);
				}
			}
		});
	}
	
	
	
	private static void viewReservation() {
		JFrame frame = new JFrame();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setBounds(100, 100, 800, 340);
		frame.setResizable(true);
		
		frame.getContentPane().setLayout(new MigLayout("", "[730px,grow]", "[][277px][][][][][][][]"));
		
		Label lblHeader = new Label(".:: L A B  R E S E R V A T I O N  S Y S T E M ::.");
		frame.getContentPane().add(lblHeader, "cell 0 0");
		
		DefaultTableModel model = new DefaultTableModel() ;
		model.addColumn("Lab Name");
		model.addColumn("Start time");
		model.addColumn("End time");
		model.addColumn("Date");
		model.addColumn("Reserved By");
		model.addColumn("Reason");
		model.addColumn("Approval Status");
		model.addColumn("R.ID");
		@SuppressWarnings("serial")
		JTable tableReservation = new JTable(model) {
			@Override
			public boolean isCellEditable(int row, int column) {
				if(column==6)
					return true;
				else
					return false;
			}
		};
		
		
		tableReservation.setColumnSelectionAllowed(false);
		tableReservation.setEnabled(true);
		JComboBox<String> approvalStatusCombo = new JComboBox<String>();
		approvalStatusCombo.addItem("APPROVED");
		approvalStatusCombo.addItem("DENIED");
		approvalStatusCombo.addItem("IN REVIEW");
	
	
		
		tableReservation.getTableHeader().setReorderingAllowed(false);
		tableReservation.setFillsViewportHeight(true);
		tableReservation.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableReservation.getColumnModel().getColumn(0).setResizable(true);
		tableReservation.getColumnModel().getColumn(0).setPreferredWidth(156);
		tableReservation.getColumnModel().getColumn(1).setResizable(false);
		tableReservation.getColumnModel().getColumn(2).setResizable(false);
		tableReservation.getColumnModel().getColumn(3).setResizable(false);
		tableReservation.getColumnModel().getColumn(3).setPreferredWidth(83);
		tableReservation.getColumnModel().getColumn(4).setResizable(false);
		tableReservation.getColumnModel().getColumn(5).setResizable(true);
		tableReservation.getColumnModel().getColumn(6).setCellEditor(new DefaultCellEditor(approvalStatusCombo));
		tableReservation.getColumnModel().getColumn(6).setResizable(false);
		tableReservation.getColumnModel().getColumn(6).setPreferredWidth(98);
		tableReservation.getColumnModel().getColumn(7).setResizable(false);
		tableReservation.getColumnModel().getColumn(7).setPreferredWidth(25);
		tableReservation.getModel().addTableModelListener(new TableModelListener() {
		      public void tableChanged(TableModelEvent e) {
		         try {
					if(ReservationController.changeStatus(tableReservation.getModel().getValueAt(e.getFirstRow(), 7).toString(), 
							tableReservation.getModel().getValueAt(e.getFirstRow(), e.getColumn()).toString())>0)
					showMessage("Successfully changed.");
					else
						showMessage("Error occurred.");
				} catch (Exception e1) {
					e1.printStackTrace();
				}
		      }
		    });
		
		
		JScrollPane scrollPane = new JScrollPane(tableReservation);
		frame.getContentPane().add(scrollPane, "cell 0 1,grow");
		
		JButton btnDeleteSelected = new JButton("Delete selected");
		
		btnDeleteSelected.setEnabled(false);
		
		tableReservation.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				btnDeleteSelected.setEnabled(true);
			}
		});
		
		btnDeleteSelected.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					if(ReservationController.deleteReservation(tableReservation.getValueAt(tableReservation.getSelectedRow(), 7).toString())>0)
						showMessage("Successfully deleted");
					model.setRowCount(0);
					for(Object[] a : ReservationController.getReservedAll("IN REVIEW"))
						model.addRow(a);
					btnDeleteSelected.setEnabled(false);
				} catch (Exception e1) {
					e1.printStackTrace();
					showMessage("Failed to delete.");
					btnDeleteSelected.setEnabled(false);
				}
				
			}
		});
		JButton btnAddNewReservation = new JButton("Add new reservation");
		btnAddNewReservation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newReservation(model);
			}
		});
		frame.getContentPane().add(btnAddNewReservation, "flowx,cell 0 5");
		
		frame.getContentPane().add(btnDeleteSelected, "cell 0 6");
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JButton btnViewApproved = new JButton("View approved");
		btnViewApproved.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {	
				try {
						model.setRowCount(0);
						for(Object[] a : ReservationController.getReservedAll("APPROVED")) {
							model.addRow(a);
						}
					} catch (Exception e1) {
						e1.printStackTrace();
					}
			}
		});
		menuBar.add(btnViewApproved);
		
		JButton btnViewPending = new JButton("View Pending");
		btnViewPending.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					model.setRowCount(0);
					for(Object[] a : ReservationController.getReservedAll("IN REVIEW")) {
						model.addRow(a);
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		menuBar.add(btnViewPending);
		
		JButton btnViewDenied = new JButton("View Denied");
		btnViewDenied.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					model.setRowCount(0);
					for(Object[] a : ReservationController.getReservedAll("DENIED")) {
						model.addRow(a);
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		menuBar.add(btnViewDenied);
	}
	private static void editLab() {
		JFrame frame = new JFrame();
		frame.setVisible(true);
		frame.setBounds(100, 100, 609, 289);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(new MigLayout("", "[][][grow][][][][][][][][][]", "[][grow][][][][][][][grow][][][]"));
		
		JLabel lblLA = new JLabel(".:: L A B  R E S E R V A T I O N  S Y S T E M ::.");
		frame.getContentPane().add(lblLA, "cell 2 0");
		
		
		
		DefaultTableModel model = new DefaultTableModel() ;
		model.addColumn("Lab ID");
		model.addColumn("Lab Abbrev");
		model.addColumn("Lab Name");
		model.addColumn("Lab Type");
		model.addColumn("Lab Location");
		model.addColumn("Staff Username");
		
		JComboBox<String> labTypeBox = new JComboBox<String>();
		labTypeBox.setModel(new DefaultComboBoxModel<String>(new String[] {"ARTIFICIAL INTELLIGENCE", "MULTIMEDIA", "NETWORKING", "SOFTWARE ENGINEERING", "DATABASE", "GAMING"}));
		JComboBox<String> staffBox = new JComboBox<String>();
		try {
			for(String staffUsername : UserController.listAllUsername(UserController.searchByPosition("STAFF")))
				staffBox.addItem(staffUsername);
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		
		JScrollPane scrollPane = new JScrollPane();
		frame.getContentPane().add(scrollPane, "cell 0 1 11 8,grow");
		@SuppressWarnings("serial")
		JTable tableLab = new JTable(model) {
			@Override
			public boolean isCellEditable(int row, int column) {
				if(column==0)
					return false;
				else
					return true;
			}
		};
		try {
			for(Object[] a : LabController.getAllLab()) {
				model.addRow(a);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		JLabel lblErrorStatus = new JLabel("All changes saved.");
		frame.getContentPane().add(lblErrorStatus, "cell 1 9");
		tableLab.getModel().addTableModelListener(new TableModelListener() {
		      public void tableChanged(TableModelEvent e) {
		         try {
					if(LabController.updateLab(tableLab.getModel().getValueAt(e.getFirstRow(), 0).toString(), 
							tableLab.getModel().getValueAt(e.getFirstRow(), 2).toString(), 
							tableLab.getModel().getValueAt(e.getFirstRow(), 1).toString(), 
							tableLab.getModel().getValueAt(e.getFirstRow(), 4).toString(), 
							UserController.searchByUser(tableLab.getModel().getValueAt(e.getFirstRow(), 5).toString()).getUserID(), 
							tableLab.getModel().getValueAt(e.getFirstRow(), 3).toString())>0)
						lblErrorStatus.setText("All changes saved.");
					else
						lblErrorStatus.setText("Error occurred, check for duplicate value.");
				} catch (Exception e1) {
					e1.printStackTrace();
				}
		      }
		    });
		scrollPane.setViewportView(tableLab);
		
		tableLab.setColumnSelectionAllowed(false);
		tableLab.setEnabled(true);
		
		
		
		tableLab.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(labTypeBox));
		tableLab.getColumnModel().getColumn(5).setCellEditor(new DefaultCellEditor(staffBox));
	}
	
	private static void addUser() {
	JFrame frame = new JFrame();
	frame.setVisible(true);
	frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	frame.setBounds(100, 100, 309, 233);
	frame.setResizable(false);
	frame.getContentPane().setLayout(new MigLayout("", "[][][grow]", "[][][115.00][]"));
	
	JLabel lblHeader = new JLabel(".:: L A B  R E S E R V A T I O N  S Y S T E M ::.");
	frame.getContentPane().add(lblHeader, "cell 0 0");
	
	JPanel panel = new JPanel();
	frame.getContentPane().add(panel, "cell 0 1 3 2,grow");
	panel.setLayout(new MigLayout("", "[73px][86px][19px]", "[20px][20px][20px][20px]"));
	
	JLabel lblUsername = new JLabel("Username:");
	panel.add(lblUsername, "cell 0 0,alignx right,aligny center");
	
	JLabel lblValid = new JLabel("");
	JTextField usernameField = new JTextField();
	
	panel.add(usernameField, "cell 1 0,growx,aligny top");
	usernameField.setColumns(10);
	
	
	panel.add(lblValid, "cell 2 0,growx,aligny center");
	
	JLabel lblPassword = new JLabel("Password:");
	panel.add(lblPassword, "cell 0 1,alignx right,aligny center");
	
	JPasswordField passwordField = new JPasswordField();
	panel.add(passwordField, "cell 1 1,growx,aligny top");
	
	JLabel lblPhoneNumber = new JLabel("Phone number:");
	panel.add(lblPhoneNumber, "cell 0 2,alignx right,aligny center");
	
	JTextField phoneField = new JTextField();
	panel.add(phoneField, "cell 1 2,growx,aligny top");
	phoneField.setColumns(10);
	
	JLabel lblPosition = new JLabel("Position");
	panel.add(lblPosition, "cell 0 3,alignx right,aligny center");
	
	JComboBox<String> positionBox = new JComboBox<String>();
	positionBox.setModel(new DefaultComboBoxModel<String>(new String[] {"LECTURER", "STUDENT", "STAFF"}));
	panel.add(positionBox, "cell 1 3,growx,aligny top");
	
	JButton btnSave = new JButton("Save");
	btnSave.addMouseListener(new MouseAdapter() {
		@SuppressWarnings("deprecation")
		@Override
		public void mouseClicked(MouseEvent arg0) {
			UserController userController = new UserController();
			try {
				userController.addUser(usernameField.getText(), passwordField.getText(), positionBox.getSelectedItem().toString(), phoneField.getText());
				showMessage("User registered successfully");
			} catch (Exception e) {
				showMessage("An error occurred");
				e.printStackTrace();
			}
		}
	});
	frame.getContentPane().add(btnSave, "cell 0 3,alignx center");
	
	usernameField.addKeyListener(new KeyAdapter() {
		@Override
		public void keyReleased(KeyEvent e) {
			try {
				if(UserController.searchByUser(usernameField.getText())!=null) {
						lblValid.setText("X (exist)");
						btnSave.setEnabled(false);
				}
				else {
					lblValid.setText("/");
					btnSave.setEnabled(true);
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	});
}
	
	private static void showMessage(String message) {
		JFrame messageFrame = new JFrame();
		messageFrame.setVisible(true);
		messageFrame.setBounds(100, 100, 265, 163);
		messageFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		messageFrame.getContentPane().setLayout(new BorderLayout(0, 0));
		JLabel lblMessage = new JLabel(message);
		lblMessage.setHorizontalAlignment(SwingConstants.CENTER);
		messageFrame.getContentPane().add(lblMessage, BorderLayout.CENTER);
		
		JButton btnOkay = new JButton("Okay");
		btnOkay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				messageFrame.dispose();
			}
		});
		messageFrame.getContentPane().add(btnOkay, BorderLayout.SOUTH);
	}
	
	private static void addLab(){
		JFrame frame = new JFrame();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setBounds(100, 100, 305, 249);
		frame.setResizable(false);
		frame.getContentPane().setLayout(new MigLayout("", "[][][grow]", "[][][115.00][][][][][][][]"));
		
		JLabel lblHeader = new JLabel(".:: L A B  R E S E R V A T I O N  S Y S T E M ::.");
		frame.getContentPane().add(lblHeader, "cell 0 0");
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, "cell 0 1 3 3,grow");
		panel.setLayout(new MigLayout("", "[73px][86px,grow][19px]", "[20px][20px][20px][20px][]"));
		
		JLabel lblLabName = new JLabel("Lab Name:");
		panel.add(lblLabName, "cell 0 0,alignx right,aligny center");
		
		JLabel lblValid = new JLabel("");
		JTextField labNameField = new JTextField();
		
		panel.add(labNameField, "cell 1 0,growx,aligny top");
		labNameField.setColumns(10);
		
		
		panel.add(lblValid, "cell 2 0,growx,aligny center");
		
		JLabel lblLabAbbreviation = new JLabel("Abbreviation:");
		panel.add(lblLabAbbreviation, "cell 0 1,alignx right,aligny center");
		
		JTextField abbreviationField = new JTextField();
		panel.add(abbreviationField, "cell 1 1,growx,aligny top");
		
		JLabel lblLocation = new JLabel("Location:");
		panel.add(lblLocation, "cell 0 2,alignx right,aligny center");
		
		JTextField locationField = new JTextField();
		panel.add(locationField, "cell 1 2,growx,aligny top");
		locationField.setColumns(10);
		
		JLabel lblLabType = new JLabel("Lab Type");
		panel.add(lblLabType, "cell 0 3,alignx right,aligny center");
		
		JComboBox<String> labTypeBox = new JComboBox<String>();
		labTypeBox.setModel(new DefaultComboBoxModel<String>(new String[] {"ARTIFICIAL INTELLIGENCE", "MULTIMEDIA", "NETWORKING", "SOFTWARE ENGINEERING", "DATABASE", "GAMING"}));
		panel.add(labTypeBox, "cell 1 3,growx,aligny top");
		
		JLabel lblStaff = new JLabel("Staff:");
		panel.add(lblStaff, "cell 0 4,alignx trailing");
		
		JComboBox<String> staffBox = new JComboBox<String>();
		try {
			for(String staffUsername : UserController.listAllUsername(UserController.searchByPosition("STAFF")))
				staffBox.addItem(staffUsername);
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		panel.add(staffBox, "cell 1 4,growx");
		
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					LabController.addLab(labNameField.getText(), abbreviationField.getText(), locationField.getText(), UserController.searchByUser(staffBox.getSelectedItem().toString()).getUserID(), labTypeBox.getSelectedItem().toString());
					showMessage("Successfully added.");
					frame.dispose();
				} catch (Exception e) {
					showMessage("Error occurred, lab already exist, or duplicate values.");
					e.printStackTrace();
				}
			}
		});
		btnSave.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				UserController userController = new UserController();
				try {
					userController.addUser(labNameField.getText(), abbreviationField.getText(), labTypeBox.getSelectedItem().toString(), locationField.getText());
					showMessage("User registered successfully");
				} catch (Exception e) {
					showMessage("An error occurred");
					e.printStackTrace();
				}
			}
		});
		frame.getContentPane().add(btnSave, "cell 0 7,alignx center");
		
		labNameField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
			}
			public void keyReleased(KeyEvent e) {
				try {
					if(UserController.searchByUser(labNameField.getText())!=null) {
							
							lblValid.setText("X (exist)");
							btnSave.setEnabled(false);
					}
					else {
						lblValid.setText("/");
						btnSave.setEnabled(true);
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
	}
}