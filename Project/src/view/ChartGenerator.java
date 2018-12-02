package view;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;



import database.DatabaseConnection;

public class ChartGenerator {
	static JFrame chartFrame;
	static String title;
	static String labName;
	static int chartTypeNo;
	public ChartGenerator() {}
	public static void createChart(int chartTypeNo, String title, String labName) throws Exception {
		chartFrame = new JFrame();
		ChartGenerator.title = title;
		ChartGenerator.labName = labName;
		ChartGenerator.chartTypeNo = chartTypeNo;
		if(chartTypeNo==1)
			chartFrame.setContentPane(createBarChartPanel( ));
		else if(chartTypeNo<5)
			chartFrame.setContentPane(createPieChartPanel());
		else
			chartFrame.setContentPane(createLineChartPanel());
		chartFrame.setSize( 800 , 400 );    
	    chartFrame.setLocationRelativeTo(null);    
		chartFrame.setVisible(true);
		chartFrame.setAlwaysOnTop(true);
	}
	

	private static PieDataset createPieDataset( ) throws Exception {
		String sql = null;
		if(chartTypeNo==2)
			sql= "SELECT Count(reservationID) 'Number', lab.labName 'Lab Name'\r\n" + 
				"FROM RESERVATION \r\n" + 
				"JOIN lab\r\n" + 
				"ON lab.labID = reservation.labID\r\n" + 
				"GROUP BY reservation.labID\r\n" + 
				"ORDER BY lab.labName";
		if(chartTypeNo==3)
			sql= "SELECT Count(reservationID) 'Number', lab.labName 'Lab Name'\r\n" + 
				"FROM RESERVATION \r\n" + 
				"JOIN lab\r\n" + 
				"ON lab.labID = reservation.labID\r\n"+
				"WHERE reservation.approvalStatus='APPROVED'\r\n" + 
				"GROUP BY reservation.labID\r\n" + 
				"ORDER BY lab.labName";
		if(chartTypeNo==4)
			sql= "SELECT Count(reservationID) 'Number', lab.labName 'Lab Name'\r\n" + 
				"FROM RESERVATION \r\n" + 
				"JOIN lab\r\n" + 
				"ON lab.labID = reservation.labID\r\n" +
				"WHERE reservation.approvalStatus='DENIED'\r\n" +
				"GROUP BY reservation.labID\r\n" + 
				"ORDER BY lab.labName";
		DatabaseConnection db = new DatabaseConnection();
		Connection conn = db.getConnection();
		PreparedStatement ps = conn.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
	      DefaultPieDataset dataset = new DefaultPieDataset( );
	      while(rs.next())    
	    	  dataset.setValue( rs.getString(2) , rs.getInt(1) );  
	      return dataset;         
	 }
	
	private static CategoryDataset createCategoryDataset() throws Exception {
		final DefaultCategoryDataset dataset = new DefaultCategoryDataset( );
		String sql = "";
		if(chartTypeNo==1)
			sql = "SELECT COUNT(reservationID) 'Number',approvalStatus 'Approval Status',  lab.labAbbrev 'Lab Name'\r\n" + 
					"FROM RESERVATION \r\n" + 
					"JOIN lab\r\n" + 
					"ON lab.labID = reservation.labID\r\n" + 
					"GROUP BY reservation.labID, approvalStatus\r\n" + 
					"ORDER BY lab.labName";
		if(chartTypeNo==5)
			sql="SELECT reservation.dateReserved,Count(reservationID) 'Number'\r\n" + 
					"FROM RESERVATION \r\n" + 
					"JOIN lab\r\n" + 
					"ON lab.labID = reservation.labID\r\n" + 
					"WHERE reservation.approvalStatus = 'DENIED' && lab.labName = ? \r\n" + 
					"GROUP BY reservation.labID, reservation.dateReserved\r\n" + 
					"ORDER BY reservation.dateReserved";
		if(chartTypeNo==6)
			sql="SELECT reservation.dateReserved,Count(reservationID) 'Number'\r\n" + 
					"FROM RESERVATION \r\n" + 
					"JOIN lab\r\n" + 
					"ON lab.labID = reservation.labID\r\n" + 
					"WHERE reservation.approvalStatus = 'APPROVED' && lab.labName = ? \r\n" + 
					"GROUP BY reservation.labID, reservation.dateReserved\r\n" + 
					"ORDER BY reservation.dateReserved";
		DatabaseConnection db = new DatabaseConnection();
		Connection conn = db.getConnection();
		PreparedStatement ps = conn.prepareStatement(sql);
		ResultSet rs;
		System.out.println(chartTypeNo);
		if(chartTypeNo==6 || chartTypeNo==5) {
			ps.setString(1, labName);
			rs = ps.executeQuery();
			dataset.addValue( 0 , "Date","0");
			while(rs.next())
				dataset.addValue( rs.getInt(2) , "Date",rs.getString(1));
		}
		else {
			rs = ps.executeQuery();
			while(rs.next())
				dataset.addValue( rs.getInt(1) , rs.getString(2), rs.getString(3));
		}
		conn.close();
		return dataset;         
		}
	   
	   private static JFreeChart createPieChart( PieDataset dataset ) {
	      JFreeChart chart = ChartFactory.createPieChart(      
	         title,   // chart title 
	         dataset,          // data    
	         true,             // include legend   
	         true, 
	         false);

	      return chart;
	   }
	   
	   private static JFreeChart createBarChart( CategoryDataset dataset ) throws Exception {
		      JFreeChart chart = ChartFactory.createBarChart(      
    		  title,           
    	      "Lab Name",            
	          "Number of Reservations",            
	          createCategoryDataset(),          
	          PlotOrientation.VERTICAL,           
	          true, true, false);

		      return chart;
		   }
	   
	   public static JPanel createPieChartPanel( ) throws Exception {
	      JFreeChart pieChart = createPieChart(createPieDataset( ) );  
	      return new ChartPanel( pieChart ); 
	   }
	   
	   public static JPanel createBarChartPanel( ) throws Exception {
		   JFreeChart barChart = createBarChart(createCategoryDataset( ));  
		   return new ChartPanel( barChart ); 
	   }
	   
		private static JPanel createLineChartPanel() throws Exception {
			JFreeChart lineChart = createLineChart(createCategoryDataset());
			return new ChartPanel(lineChart);
		}


		private static JFreeChart createLineChart(Object createLineDataSet) throws Exception {
			JFreeChart chart = ChartFactory.createLineChart(title, "Date", "Number of reservations", createCategoryDataset());
			return chart;
		}
}