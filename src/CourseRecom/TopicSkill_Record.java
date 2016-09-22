package CourseRecom;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import java.sql.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

import CourseRecom.DBConnection;

public class TopicSkill_Record extends JFrame {
	public static final String PROGRAM_NAME = "TopicSkill_Record";
	
	private String username;
	
	Connection conn;
	boolean new_user;
	
	final static int extraWindowWidth = 100;
	
	//record the skill evaluate value
	Map<String,Integer> skill_val;
	Map<String,Integer> topic_val;
	
	Object[] skill_evaluate = {"Skill Name","Evaluation"};
	Object[] topic_evaluate = {"Topic Name","Evaluation"};
	
	public TopicSkill_Record(Connection conn,String username, boolean new_user)throws SQLException{
		super("Record Skill and Topic");
		
		this.username = username;
		this.conn = conn;
		skill_val = new HashMap<>();
		topic_val = new HashMap<>();
		
		/*************************Initialize the hash map**********************************/
		Skills init_skill = new Skills();
		init_skill.getAllSkills(conn, skill_val);
		
		Topics init_topic = new Topics();
	    init_topic.getAllTopic(conn, topic_val);
	    
		
	    addWindowListener(new WindowAdapter() {
	        public void windowClosing(WindowEvent e) {
	          DBConnection.closeConnection(conn);
	          System.exit(0);
	        }
	    }); 
	    
	    //Initialization
	     
		JTabbedPane tabbedPane = new JTabbedPane();
		
		//create the cards
		Department dept = new Department();
		java.util.List<Department> all_dept = new ArrayList<Department>();
		all_dept = dept.getAllDept(conn); 
		Container contentPane = getContentPane();
		
		for(int i = 0; i < all_dept.size(); i++){
			Department d = new Department();
			d = all_dept.get(i);
			
			////////////////get all related skills and topics of that department/////////////
			Course_Skills cs = new Course_Skills();
			java.util.List<Integer> skill_list = new ArrayList<>();
			skill_list = cs.getCourse_skill(conn, d.getCode());
			
			Course_Topics ct = new Course_Topics();
			java.util.List<Integer> topic_list = new ArrayList<>();
			topic_list = ct.getCourse_topic(conn, d.getCode());
			
			JPanel card = new JPanel(new GridBagLayout()){
				//make the panel wider than it really needs,so the window's wide enough
				//for the tabs to stay
				//in one row
				public Dimension getPreferredSize(){
					Dimension size = super.getPreferredSize();
					size.width += extraWindowWidth;
					return size;
				}
			};
			
			JButton edit_button = new JButton("Edit");
			edit_button.setPreferredSize(new Dimension(300, 40));
			JButton edit_button2 = new JButton("Edit");
			edit_button2.setPreferredSize(new Dimension(80, 40));
						
			GridBagConstraints c = new GridBagConstraints();
			
			//add the table
			JTable table = new JTable();
			c.fill = GridBagConstraints.BOTH;
			c.anchor = GridBagConstraints.CENTER;
			c.weightx = 7.0;
			c.weighty = 7.0;
			c.gridx = 0;
			c.gridy = 0;
			c.gridwidth = 5;
			card.add(new JScrollPane(table), c);
			refreshTable(table,skill_list,skill_val);
			
			//add the label and textbox
			JLabel label = new JLabel("Current Level: ");
			c.fill = GridBagConstraints.CENTER;
	        c.anchor = GridBagConstraints.LINE_START;
	        c.weightx = 0.5;
	        c.weighty = 0;
	        c.gridx = 0;
	        c.gridy = 1;
	        c.gridwidth = 1;
	        card.add(label,c);	
	        
	        JTextField text = new JTextField(20);
	        c.fill = GridBagConstraints.HORIZONTAL;
	        c.anchor = GridBagConstraints.LINE_START;
	        c.weightx = 0.6;
	        c.weighty = 0;
	        c.gridx = 1;
	        c.gridy = 1;
	        c.gridwidth = 1;
	        card.add(text,c);
			
			//add the button
			edit_button.setEnabled(false);
			c.fill = GridBagConstraints.CENTER;
	        c.anchor = GridBagConstraints.LINE_END;
	        c.weightx = 0.8;
	        c.weighty = 0;
	        c.gridx = 1;
	        c.gridy = 2;
	        c.gridwidth = 1;
	        card.add(edit_button,c);
	        
	        edit_button.addActionListener(new ActionListener(){
	        	public void actionPerformed(ActionEvent e){
	        		String skill_name = label.getText();
	        		int val = Integer.parseInt(text.getText());
	        		if(val > 5 || val < 1) JOptionPane.showMessageDialog(null, "Please input a rank between 1 and 5.");
	        		else{
		        		skill_val.put(skill_name, val);
						//System.out.print(skill_name);
	
		        		//refreshTable(table,skill_list,skill_val);
		        		changeVal(table,skill_val);
		        		edit_button.setEnabled(false);
	        		}
	        		text.setText("");
	        	}
	        });
			
			JTable table2 = new JTable();
			c.fill = GridBagConstraints.BOTH;
			c.anchor = GridBagConstraints.CENTER;
			c.weightx = 7.0;
			c.weighty = 7.0;
			c.gridx = 0;
			c.gridy = 3;
			c.gridwidth = 5;
			card.add(new JScrollPane(table2), c);
			refreshTable2(table2,topic_list,topic_val);

			
			//add the label and textbox
			JLabel label2 = new JLabel("Current Level: ");
			c.fill = GridBagConstraints.HORIZONTAL;
	        c.anchor = GridBagConstraints.LINE_END;
	        c.weightx = 0.25;
	        c.weighty = 0;
	        c.gridx = 0;
	        c.gridy = 4;
	        c.gridwidth = 1;
	        card.add(label2,c);	
	        
	        JTextField text2 = new JTextField(20);
	        c.fill = GridBagConstraints.HORIZONTAL;
	        c.anchor = GridBagConstraints.CENTER;
	        c.weightx = 0.75;
	        c.weighty = 0;
	        c.gridx = 1;
	        c.gridy = 4;
	        c.gridwidth = 1;
	        card.add(text2,c);
			
			//add the button
			edit_button2.setEnabled(false);
			c.fill = GridBagConstraints.CENTER;
	        c.anchor = GridBagConstraints.LINE_END;
	        c.weightx = 0.5;
	        c.weighty = 0;
	        c.gridx = 1;
	        c.gridy = 5;
	        c.gridwidth = 1;
	        card.add(edit_button2,c);
	        
	        edit_button2.addActionListener(new ActionListener(){
	        	public void actionPerformed(ActionEvent e){
	        		String topic_name = label2.getText();
	        		int val = Integer.parseInt(text2.getText());
	        		if(val > 5 || val < 1){
	        			JOptionPane.showMessageDialog(null, "Please input a rank between 1 to 5.");
	        		}else{
	        			topic_val.put(topic_name, val);
	        			changeVal2(table2,topic_val);
		        		edit_button2.setEnabled(false);
		        		
	        		}
					//System.out.print(topic_name);

	        		//refreshTable(table,skill_list,skill_val);
	        		text2.setText("");
	        	}
	        });
	        
	        JButton finish_button = new JButton("Finish");
	        finish_button.setPreferredSize(new Dimension(120, 35));
	        finish_button.setEnabled(false);
	        c.fill = GridBagConstraints.CENTER;
	        c.anchor = GridBagConstraints.CENTER;
	        c.weightx = 0;
	        c.weighty = 0;
	        c.gridx = 0;
	        c.gridy = 6;
	        c.gridwidth = 2;
	        card.add(finish_button,c);
	        
	        finish_button.addActionListener(new ActionListener(){
	        	public void actionPerformed(ActionEvent e){
	        		/********************go to another interface***************************/
	        		//calculate the closest 15 person
	        		try {
	        			/****************************5 skills and 5 topics************************************/
	        			int skill_count = 0;
	        			for(String key:skill_val.keySet()){
	        				if(skill_val.get(key)!=0){
	        					skill_count++;
	        				}
	        			}
	        			int topic_count = 0;
	        			for(String key:topic_val.keySet()){
	        				if(topic_val.get(key)!=0){
	        					topic_count++;
	        				}
	        			}
	        			
	        			if(skill_count<5){
	        				JOptionPane.showMessageDialog(contentPane, "Please rank at least 5 skills for better course recommendation result!","Skill Problem",JOptionPane.ERROR_MESSAGE);
	        			}else if(topic_count<5){
	        				JOptionPane.showMessageDialog(contentPane, "Please rank at least 5 topics for better course recommendation result!","Topic Problem",JOptionPane.ERROR_MESSAGE);

	        			}else{
							FindClosest fc = new FindClosest(conn, skill_val,topic_val, username);
							
							FindRecomCourses frc = new FindRecomCourses(conn, username);
							setVisible(false);
							//System.out.print(username);
							Recom_Interface ri = new Recom_Interface(conn, username, new_user);
							ri.setSize(480, 600);
							ri.setLocationRelativeTo(null);
							ri.setVisible(true);
							dispose();
	        			}
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

	        	}
	        });
			
			//set the mouse listener
			table.addMouseListener(new MouseAdapter(){
				public void mouseClicked(MouseEvent e){
					if(e.getClickCount()==1){
						/********************add the activity************************/
						edit_button.setEnabled(true);
						finish_button.setEnabled(true);
						
						JTable target = (JTable)e.getSource();
						int row = target.getSelectedRow();
						String skill_name = (String)target.getValueAt(row,0);
						
						label.setText(skill_name);
						text.setText(Integer.toString((int)target.getValueAt(row, 1)));
					}
				}
			});
			
			table2.addMouseListener(new MouseAdapter(){
				public void mouseClicked(MouseEvent e){
					if(e.getClickCount()==1){
						/********************add the activity************************/
						edit_button2.setEnabled(true);
						finish_button.setEnabled(true);
						
						JTable target = (JTable)e.getSource();
						int row = target.getSelectedRow();
						String topic_name = (String)target.getValueAt(row,0);
						
						label2.setText(topic_name);
						text2.setText(Integer.toString((int)target.getValueAt(row, 1)));
					}
				}
			});
	        
	        tabbedPane.addTab(d.getName(), card);
		}
		contentPane.add(tabbedPane, BorderLayout.CENTER);
	}

	private void refreshTable(JTable table,java.util.List<Integer> skill_list,Map<String,Integer> evaluate_val){
		try{
			DefaultTableModel model = new DefaultTableModel(new Object[0][0],skill_evaluate);
			for(int i = 0; i< skill_list.size();i++){
				Skills s = new Skills();
				String skill = s.getSkill(conn, skill_list.get(i));
				Object[] o = new Object[2];
				o[0] = skill;
				o[1] = evaluate_val.get(skill);
				model.addRow(o);
			}
			table.setModel(model);
			table.repaint();
			
		}catch(SQLException e){
			SQLError.show(e);
		}
	}
	
	private void changeVal(JTable table,Map<String,Integer> evaluate_val){
		int row = table.getRowCount();
		for(int i = 0; i < row; i++){
			if(table.getValueAt(i, 1)!= evaluate_val.get(table.getValueAt(i, 0))){
				//System.out.print(table.getValueAt(i, 1)+Integer.toString(evaluate_val.get(table.getValueAt(i, 0))));
				table.setValueAt(evaluate_val.get(table.getValueAt(i, 0)), i, 1);
			}
		}
	}
	
	private void refreshTable2(JTable table,java.util.List<Integer> topic_list,Map<String,Integer> evaluate_val){
		try{
			DefaultTableModel model = new DefaultTableModel(new Object[0][0],topic_evaluate);
			for(int i = 0; i < topic_list.size();i++){
				Topics t = new Topics();
				String topic = t.getTopic(conn, topic_list.get(i));
				Object[] o = new Object[2];
				o[0] = topic;
				o[1] = evaluate_val.get(topic);
				model.addRow(o);
			}
			table.setModel(model);
			table.repaint();
		}catch(SQLException e){
			SQLError.show(e);
		}
	}
	
	private void changeVal2(JTable table,Map<String,Integer> map){
		int row = table.getRowCount();
		for(int i = 0; i < row; i++){
			if(table.getValueAt(i, 1) != map.get(table.getValueAt(i, 0))){
				table.setValueAt(map.get(table.getValueAt(i, 0)), i, 1);
			}
		}
	}
	/*
	public static void main(String [] args) throws IOException, SQLException {
		if (args.length == 0){
    		System.out.println("Usage: "+PROGRAM_NAME+" <name of properties file>");
    		System.exit(1);
    	}
		
		Properties props = new Properties();
    	FileInputStream in = new FileInputStream(args[0]);
    	props.load(in);
    	in.close();
    	java.sql.Connection conn = DBConnection.getConnection (props);	
		
		conn.createStatement().execute("PRAGMA foreign_keys = ON");
    	
    	if (conn == null)
    		System.exit(1);
    	
    	TopicSkill_Record app = new TopicSkill_Record(conn, "CC", false);
    	app.setSize(480, 600);
    	app.setLocationRelativeTo(null);
        app.setVisible(true);
        
	}
	*/
	
}
