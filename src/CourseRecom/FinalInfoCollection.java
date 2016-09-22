package CourseRecom;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class FinalInfoCollection extends JFrame {
	private static final String PROGRAM_NAME = "final_info_collection";
	
	Connection connection;
	String username;
	
	PreparedStatement stmt = null;
	
	private int pointer = 0;
	private int pointer2 = 0;
	private List<Integer> cr_list = new ArrayList<Integer>();
	
	private List<String> skill_list;
	private List<String> topic_list;
	private Map<Integer, Integer> ce_list;
	private List<Integer> course_id_list = new ArrayList<Integer>();
	private List<Integer> skill_id_list = new ArrayList<Integer>();
	private List<Integer> topic_id_list = new ArrayList<Integer>();
	
	
	private JTable newcourses_1;
	private JTable newcourses_2;
	private JTable newcourses_3;
	
	private JComboBox<String> grade_list;
	private JComboBox<String> sat_list;
	private JComboBox<String> rank_list;
	
	private JComboBox<String> dskill_rank_b;
	private JComboBox<String> dskill_rank_a;
	private JComboBox<Object> dskill_add;
	private JTextField tnew_skill;
	
	private JComboBox<String> dtopic_rank_b;
	private JComboBox<String> dtopic_rank_a;
	private JComboBox<Object> dtopic_add;
	private JTextField tnew_topic;
	
	private JButton evaluate_skill;
	private JButton evaluate_topic;
	
	private JButton s_reset;
	private JButton t_reset;
	
	private JTable course_ex;
	private JTable tskill;
	private JTable ttopic;
	
	private String[] course_grade = {"", "A+", "A", "A-", "B+", "B", "B-", "C+", "C", "C-", "D+", "D", "D-", "F"};
	private String[] rank = {"", "Bad", "So so", "Good", "Very Good", "Excellent"};
	private String[] interest = {"", "Never Want", "Boring", "Not Really", "Interested", "Very Interested"};
	
	Object[] course_ranking_list = {"Grade", "Instructor Rank", "Course Satisfaction"};
	Object[] st_course_list = {"Department", "Course Number"};
	Object[] skill_list_column = {"Skill", "Skill Level Before", "Skill Level After"};
	Object[] topic_list_column = {"Topic", "Topic Interest Before", "Topic Interest After"};
	
	public FinalInfoCollection(Connection conn, String username, Map<Integer, Integer> ce_list, List<Integer> course_id_list)throws SQLException{
		super("Course Recommendation System");
		
		this.connection = conn;
		this.username = username;
		this.ce_list = ce_list;
		this.course_id_list = course_id_list;
		
		for(int i = 0;i < course_id_list.size();i++){
			cr_list.add(0);
		}
		
		addWindowListener(new WindowAdapter() {
	        public void windowClosing(WindowEvent e) {
	          
	          DBConnection.closeConnection(conn);
	          System.exit(0);
	        }
	    }); 
		
		
		
		/*****************************Initialization**********************************/
		JTabbedPane tabbedPane = new JTabbedPane();
		GridBagConstraints c = new GridBagConstraints();
		
		//course info
		JPanel tab1 = new JPanel(new GridBagLayout());
		String Tab1 = "Course Experience Information";
		
		//skill info
		JPanel tab2 = new JPanel(new GridBagLayout());
		String Tab2 = "Course Skill Ranking";
		
		//interest info
		JPanel tab3 = new JPanel(new GridBagLayout());
		String Tab3 = "Course Topic Interest";
		
		Dimension d = new Dimension(200, 30);
		Dimension dropdown = new Dimension(100, 30);
		
		
		JLabel title1 = new JLabel("Your New Courses");
		
		newcourses_1 = new JTable(); //need to be refreshed to get new course info
		
		//grade section
		JLabel grades = new JLabel("Course Grade: ");
		grade_list = new JComboBox<String>(course_grade);
		grade_list.setPreferredSize(dropdown);
		grade_list.setEnabled(false);
		grade_list.setSelectedIndex(0);
		
		//course satisfaction
		JLabel course_sat = new JLabel("Course Satisfaction Level: ");
		sat_list = new JComboBox<String>(rank);
		sat_list.setPreferredSize(dropdown);
		sat_list.setEnabled(false);
		sat_list.setSelectedIndex(0);
		
		//instructor ranking
		JLabel instructor_rank = new JLabel("Instructor Level: ");
		rank_list = new JComboBox<String>(rank);
		rank_list.setPreferredSize(dropdown);
		rank_list.setEnabled(false);
		rank_list.setSelectedIndex(0);
		
		//course edit button
		JButton c_finish = new JButton("Finish");
		c_finish.setPreferredSize(d);
		c_finish.setEnabled(false);
		JButton c_reset = new JButton("Reset");
		c_reset.setPreferredSize(d);
		
		//--------------------section for skill--------------------//
		Skills s = new Skills();
		skill_list = s.getSkillList(connection);
		skill_list.add(0, "");
		skill_list.add("Other Skill");
		
		JLabel title2 = new JLabel("Your New Courses");
		
		newcourses_2 = new JTable();
		refreshTable2();
		
		JLabel course_skill = new JLabel("Course Skills");
		tskill = new JTable();
		
		JLabel skill_rank_b = new JLabel("Skill Before: ");
		dskill_rank_b = new JComboBox<String>(rank);
		dskill_rank_b.setEnabled(false);
		dskill_rank_b.setSelectedIndex(0);
		
		JLabel skill_rank_a = new JLabel("Skill After: ");
		dskill_rank_a = new JComboBox<String>(rank);
		dskill_rank_a.setEnabled(false);
		dskill_rank_a.setSelectedIndex(0);
		
		evaluate_skill = new JButton("Evaluate");
		evaluate_skill.setEnabled(false);
		
		JLabel skill_add = new JLabel("Add Skill: ");
		dskill_add = new JComboBox<Object>(skill_list.toArray());
		dskill_add.setSelectedIndex(0);
		dskill_add.setPreferredSize(dropdown);
		dskill_add.setEnabled(false);
		
		//if skill does not exist, add new one
		JLabel new_skill = new JLabel("New Skill: ");
		tnew_skill = new JTextField(20);
		tnew_skill.setPreferredSize(dropdown);
		tnew_skill.setEnabled(false);
		JButton add_new_skill = new JButton("Add New Skill");
		add_new_skill.setPreferredSize(d);
		add_new_skill.setEnabled(false);
		
		//JButton s_finish = new JButton("Finish");
		s_reset = new JButton("Reset");
		s_reset.setEnabled(false);
		//--------------------section for skill--------------------//
		
		
		//--------------------section for topic--------------------//
		Topics t = new Topics();
		topic_list = t.getTopicList(conn);
		topic_list.add(0, "");
		topic_list.add("Other Topic");
		
		JLabel title3 = new JLabel("Your New Courses");
		
		newcourses_3 = new JTable();
		refreshTable3();
		
		JLabel course_topic = new JLabel("Course Topics");
		ttopic = new JTable();
		
		JLabel topic_rank_b = new JLabel("Interest Before: ");
		dtopic_rank_b = new JComboBox<String>(interest);
		dtopic_rank_b.setSelectedIndex(0);
		dtopic_rank_b.setEnabled(false);
		
		JLabel topic_rank_a = new JLabel("Interest After: ");
		dtopic_rank_a = new JComboBox<String>(interest);
		dtopic_rank_a.setSelectedIndex(0);
		dtopic_rank_a.setEnabled(false);
		
		JLabel topic_add = new JLabel("Add Topic: ");
		dtopic_add = new JComboBox<Object>(topic_list.toArray());
		dtopic_add.setSelectedIndex(0);
		dtopic_add.setEnabled(false);
		
		//if skill does not exist, add new one
		JLabel new_topic = new JLabel("New Topic: ");
		tnew_topic = new JTextField(20);
		tnew_topic.setPreferredSize(d);
		tnew_topic.setEnabled(false);
		JButton add_new_topic = new JButton("Add New Topic");
		add_new_topic.setPreferredSize(d);
		add_new_topic.setEnabled(false);
		
		evaluate_topic = new JButton("Evaluate");
		evaluate_topic.setEnabled(false);
		
		//JButton t_finish = new JButton("Finish");
		t_reset = new JButton("Reset");
		t_reset.setEnabled(false);
		//--------------------section for topic--------------------//
		
		/*****************************Initialization**********************************/
		
		Container contentPane = getContentPane();
		c.insets = new Insets(5, 0, 5, 0);
		
		/***************************Tab 1**********************************/
		c.fill = GridBagConstraints.CENTER;
		c.anchor = GridBagConstraints.CENTER;
		c.weightx = 0.75;
		c.weighty = 0;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		tab1.add(title1, c);
		
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.CENTER;
		c.weightx = 1;
		c.weighty = 1;
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 2;
		tab1.add(new JScrollPane(newcourses_1), c);
		refreshTable1();
		
		newcourses_1.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				if(e.getClickCount() == 1){
					grade_list.setEnabled(true);
					sat_list.setEnabled(true);
					rank_list.setEnabled(true);
					c_finish.setEnabled(true);
					JTable target = (JTable)e.getSource();
					int row = target.getSelectedRow();
					pointer = row;
					try{
						refreshEX(ce_list.get(course_id_list.get(pointer)));
					}catch(SQLException ex){
						SQLError.show(ex);
					}
				}
			}
		});
		
		
		JLabel title_c = new JLabel("Course Experience");
		c.fill = GridBagConstraints.CENTER;
		c.anchor = GridBagConstraints.CENTER;
		c.weightx = 0.75;
		c.weighty = 0;
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 2;
		tab1.add(title_c, c);
		
		course_ex = new JTable();
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.CENTER;
		c.weightx = 1;
		c.weighty = 1;
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 2;
		tab1.add(new JScrollPane(course_ex), c);
		
		
		c.fill = GridBagConstraints.CENTER;
		c.anchor = GridBagConstraints.LINE_END;
		c.weightx = 0.75;
		c.weighty = 0;
		c.gridx = 0;
		c.gridy = 9;
		c.gridwidth = 1;
		tab1.add(grades, c);
		
		c.fill = GridBagConstraints.CENTER;
		c.anchor = GridBagConstraints.LINE_START;
		c.weightx = 0.75;
		c.weighty = 0;
		c.gridx = 1;
		c.gridy = 9;
		c.gridwidth = 1;
		tab1.add(grade_list, c);
		
		c.fill = GridBagConstraints.CENTER;
		c.anchor = GridBagConstraints.LINE_END;
		c.weightx = 0.75;
		c.weighty = 0;
		c.gridx = 0;
		c.gridy = 10;
		c.gridwidth = 1;
		tab1.add(course_sat, c);
		
		c.fill = GridBagConstraints.CENTER;
		c.anchor = GridBagConstraints.LINE_START;
		c.weightx = 0.75;
		c.weighty = 0;
		c.gridx = 1;
		c.gridy = 10;
		c.gridwidth = 1;
		tab1.add(sat_list, c);
		
		c.fill = GridBagConstraints.CENTER;
		c.anchor = GridBagConstraints.LINE_END;
		c.weightx = 0.75;
		c.weighty = 0;
		c.gridx = 0;
		c.gridy = 11;
		c.gridwidth = 1;
		tab1.add(instructor_rank, c);
		
		c.fill = GridBagConstraints.CENTER;
		c.anchor = GridBagConstraints.LINE_START;
		c.weightx = 0.75;
		c.weighty = 0;
		c.gridx = 1;
		c.gridy = 11;
		c.gridwidth = 1;
		tab1.add(rank_list, c);
		
		c.fill = GridBagConstraints.CENTER;
		c.anchor = GridBagConstraints.LINE_START;
		c.weightx = 0.75;
		c.weighty = 0;
		c.gridx = 1;
		c.gridy = 12;
		c.gridwidth = 1;
		tab1.add(c_finish, c);
		
		c_finish.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(grade_list.getSelectedIndex() == 0 || rank_list.getSelectedIndex() == 0
						|| sat_list.getSelectedIndex() == 0){
					JOptionPane.showMessageDialog(null, "Please select all the data needed.");
				}else{
					Enrollments el = new Enrollments();
					el.setEditionId(ce_list.get(course_id_list.get(pointer)));
					el.setUserName(username);
					el.setLetterGrade(grade_list.getSelectedItem().toString());
					el.setInstrRanking(rank_list.getSelectedIndex());
					el.setCourseRanking(sat_list.getSelectedIndex());
					try{
						el.updateNewEdition(connection);
						refreshEX(ce_list.get(course_id_list.get(pointer)));
						cr_list.set(pointer, 1);
						clearInput1();
					}catch(SQLException ex){
						SQLError.show(ex);
					}
				}
			}
		});
		
		c.fill = GridBagConstraints.CENTER;
		c.anchor = GridBagConstraints.LINE_END;
		c.weightx = 0.75;
		c.weighty = 0;
		c.gridx = 1;
		c.gridy = 12;
		c.gridwidth = 1;
		tab1.add(c_reset, c);
		
		c_reset.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				Enrollments el = new Enrollments();
				el.setEditionId(ce_list.get(course_id_list.get(pointer)));
				el.setUserName(username);
				try{
					el.deleteNewEdition(conn);
					refreshEX(ce_list.get(course_id_list.get(pointer)));
					cr_list.set(pointer, 0);
					clearInput1();
				}catch(SQLException ex){
					SQLError.show(ex);
				}
			}
		});
		
		tabbedPane.add(Tab1, tab1);
		/***************************Tab 1**********************************/
		
		/***************************Tab 2**********************************/
		c.fill = GridBagConstraints.CENTER;
		c.anchor = GridBagConstraints.CENTER;
		c.weightx = 0.75;
		c.weighty = 0;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		tab2.add(title2, c);
		
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.CENTER;
		c.weightx = 4;
		c.weighty = 4;
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 2;
		tab2.add(new JScrollPane(newcourses_2), c);
		
		newcourses_2.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				if(e.getClickCount() == 1){
					dskill_rank_b.setEnabled(false);
					dskill_rank_a.setEnabled(false);
					evaluate_skill.setEnabled(false);
					dskill_add.setEnabled(true);
					s_reset.setEnabled(false);
					JTable target = (JTable)e.getSource();
					int row = target.getSelectedRow();
					pointer = row;
					try{
						refreshSkill(course_id_list.get(pointer));
					}catch(SQLException ex){
						SQLError.show(ex);
					}
				}
			}
		});
		
		
		c.fill = GridBagConstraints.CENTER;
		c.anchor = GridBagConstraints.CENTER;
		c.weightx = 0.75;
		c.weighty = 0;
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 2;
		tab2.add(course_skill, c);
		
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.CENTER;
		c.weightx = 3;
		c.weighty = 3;
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 2;
		tab2.add(new JScrollPane(tskill), c);
		
		tskill.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				if(e.getClickCount() == 1){
					dskill_rank_b.setEnabled(true);
					dskill_rank_a.setEnabled(true);
					evaluate_skill.setEnabled(true);
					//dskill_add.setEnabled(true);
					s_reset.setEnabled(true);
					JTable target = (JTable)e.getSource();
					int row = target.getSelectedRow();
					pointer2 = row;
					try{
						refreshSkill(course_id_list.get(pointer));
					}catch(SQLException ex){
						SQLError.show(ex);
					}
				}
			}
		});
		
		c.fill = GridBagConstraints.CENTER;
		c.anchor = GridBagConstraints.LINE_END;
		c.weightx = 0.75;
		c.weighty = 0;
		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 1;
		tab2.add(skill_rank_b, c);
		
		c.fill = GridBagConstraints.CENTER;
		c.anchor = GridBagConstraints.CENTER;
		c.weightx = 0.75;
		c.weighty = 0;
		c.gridx = 1;
		c.gridy = 4;
		c.gridwidth = 1;
		tab2.add(dskill_rank_b, c);
		
		c.fill = GridBagConstraints.CENTER;
		c.anchor = GridBagConstraints.LINE_END;
		c.weightx = 0.75;
		c.weighty = 0;
		c.gridx = 0;
		c.gridy = 5;
		c.gridwidth = 1;
		tab2.add(skill_rank_a, c);
		
		c.fill = GridBagConstraints.CENTER;
		c.anchor = GridBagConstraints.CENTER;
		c.weightx = 0.75;
		c.weighty = 0;
		c.gridx = 1;
		c.gridy = 5;
		c.gridwidth = 1;
		tab2.add(dskill_rank_a, c);
		
		c.fill = GridBagConstraints.CENTER;
		c.anchor = GridBagConstraints.CENTER;
		c.weightx = 0.75;
		c.weighty = 0;
		c.gridx = 1;
		c.gridy = 6;
		c.gridwidth = 1;
		tab2.add(evaluate_skill, c);
		
		evaluate_skill.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(dskill_rank_b.getSelectedIndex() == 0 || dskill_rank_a.getSelectedIndex() == 0)
					JOptionPane.showMessageDialog(null, "Please enter skill level before and after.");
				else{
					Skill_Rankings sr = new Skill_Rankings();
					sr.setCourseId(course_id_list.get(pointer));
					sr.setEditionId(ce_list.get(course_id_list.get(pointer)));
					//System.out.print(pointer + "\n");
					//System.out.print("Pointer 2: " + pointer2 + "\n");
					sr.setSkillId(skill_id_list.get(pointer2));
					sr.setUserName(username);
					sr.setRankBefore(dskill_rank_b.getSelectedIndex());
					//System.out.print("When refresh Table: \n");
					//System.out.print(dskill_rank_b.getSelectedIndex() + "\n");
					sr.setRankAfter(dskill_rank_a.getSelectedIndex());
					//System.out.print(dskill_rank_a.getSelectedIndex() + "\n");
					try{
						if(sr.checkExist(connection)) sr.updateSkillRank(connection, 1);
						else sr.updateSkillRank(connection, 0);
						refreshSkill(course_id_list.get(pointer));
						clearSkillInput();
					}catch(SQLException ex){
						SQLError.show(ex);
					}
				}
			}
		});
		
		c.fill = GridBagConstraints.CENTER;
		c.anchor = GridBagConstraints.LINE_END;
		c.weightx = 0.75;
		c.weighty = 0;
		c.gridx = 1;
		c.gridy = 6;
		c.gridwidth = 1;
		tab2.add(s_reset, c);
		
		s_reset.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				Skill_Rankings sr = new Skill_Rankings();
				sr.setCourseId(course_id_list.get(pointer));
				sr.setEditionId(ce_list.get(course_id_list.get(pointer)));
				sr.setSkillId(skill_id_list.get(pointer2));
				sr.setUserName(username);
				try{
					sr.deleteSkillRank(conn);
					refreshSkill(course_id_list.get(pointer));
					clearSkillInput();
				}catch(SQLException ex){
					SQLError.show(ex);
				}
			}
		});
		
		c.fill = GridBagConstraints.CENTER;
		c.anchor = GridBagConstraints.LINE_END;
		c.weightx = 0.75;
		c.weighty = 0;
		c.gridx = 0;
		c.gridy = 7;
		c.gridwidth = 1;
		tab2.add(skill_add, c);
		
		c.fill = GridBagConstraints.CENTER;
		c.anchor = GridBagConstraints.CENTER;
		c.weightx = 0.75;
		c.weighty = 0;
		c.gridx = 1;
		c.gridy = 7;
		c.gridwidth = 1;
		tab2.add(dskill_add, c);
		
		dskill_add.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				JComboBox cb = (JComboBox)e.getSource();
				String select = (String)cb.getSelectedItem();
				if(!select.equals("")) add_new_skill.setEnabled(true);
				else add_new_skill.setEnabled(false);
				if(select.equals("Other Skill")) tnew_skill.setEnabled(true);
			}
		});
		
		c.fill = GridBagConstraints.CENTER;
		c.anchor = GridBagConstraints.LINE_END;
		c.weightx = 0.75;
		c.weighty = 0;
		c.gridx = 0;
		c.gridy = 8;
		c.gridwidth = 1;
		tab2.add(new_skill, c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.CENTER;
		c.weightx = 0.75;
		c.weighty = 0;
		c.gridx = 1;
		c.gridy = 8;
		c.gridwidth = 1;
		tab2.add(tnew_skill, c);
		
		c.fill = GridBagConstraints.CENTER;
		c.anchor = GridBagConstraints.CENTER;
		c.weightx = 0.75;
		c.weighty = 0;
		c.gridx = 1;
		c.gridy = 9;
		c.gridwidth = 1;
		tab2.add(add_new_skill, c);
		
		add_new_skill.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try{
					if(dskill_add.getSelectedItem().equals("Other Skill")){
						Skills s = new Skills();
						Course_Skills cs = new Course_Skills();
						s.setSkill(tnew_skill.getText());
						cs.setSkill(tnew_skill.getText());
						cs.setCourseId(course_id_list.get(pointer));
						s.insertNewSkill(connection);
						cs.insertNewCSkill(connection);
					}else{
						Course_Skills cs = new Course_Skills();
						cs.setSkill(dskill_add.getSelectedItem().toString());
						//System.out.print(dskill_add.getSelectedItem().toString());
						cs.setCourseId(course_id_list.get(pointer));
						cs.insertNewCSkill(connection);
					}
					tnew_skill.setText("");
					dskill_add.setSelectedIndex(0);
					add_new_skill.setEnabled(false);
					refreshSkill(course_id_list.get(pointer));
					
				}catch(SQLException ex){
					SQLError.show(ex);
				}
			}
		});
		
		/*
		c.fill = GridBagConstraints.CENTER;
		c.anchor = GridBagConstraints.CENTER;
		c.weightx = 0.75;
		c.weighty = 0;
		c.gridx = 0;
		c.gridy = 10;
		c.gridwidth = 1;
		tab2.add(s_finish, c);
		*/
		
		
		tabbedPane.add(Tab2, tab2);
		/***************************Tab 2**********************************/
		
		/***************************Tab 3**********************************/
		c.fill = GridBagConstraints.CENTER;
		c.anchor = GridBagConstraints.CENTER;
		c.weightx = 0.75;
		c.weighty = 0;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		tab3.add(title3, c);
		
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.CENTER;
		c.weightx = 4;
		c.weighty = 4;
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 2;
		tab3.add(new JScrollPane(newcourses_3), c);
		
		newcourses_3.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				if(e.getClickCount() == 1){
					dtopic_rank_b.setEnabled(false);
					dtopic_rank_a.setEnabled(false);
					evaluate_topic.setEnabled(false);
					dtopic_add.setEnabled(true);
					t_reset.setEnabled(false);
					JTable target = (JTable)e.getSource();
					int row = target.getSelectedRow();
					pointer = row;
					try{
						refreshTopic(course_id_list.get(pointer));
					}catch(SQLException ex){
						SQLError.show(ex);
					}
				}
			}
		});
		
		
		c.fill = GridBagConstraints.CENTER;
		c.anchor = GridBagConstraints.CENTER;
		c.weightx = 0.75;
		c.weighty = 0;
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 2;
		tab3.add(course_topic, c);
		
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.CENTER;
		c.weightx = 3;
		c.weighty = 3;
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 2;
		tab3.add(new JScrollPane(ttopic), c);
		
		ttopic.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				if(e.getClickCount() == 1){
					dtopic_rank_b.setEnabled(true);
					dtopic_rank_a.setEnabled(true);
					evaluate_topic.setEnabled(true);
					t_reset.setEnabled(true);
					JTable target = (JTable)e.getSource();
					int row = target.getSelectedRow();
					pointer2 = row;
					try{
						refreshTopic(course_id_list.get(pointer));
					}catch(SQLException ex){
						SQLError.show(ex);
					}
				}
			}
		});
		
		c.fill = GridBagConstraints.CENTER;
		c.anchor = GridBagConstraints.LINE_END;
		c.weightx = 0.75;
		c.weighty = 0;
		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 1;
		tab3.add(topic_rank_b, c);
		
		c.fill = GridBagConstraints.CENTER;
		c.anchor = GridBagConstraints.CENTER;
		c.weightx = 0.75;
		c.weighty = 0;
		c.gridx = 1;
		c.gridy = 4;
		c.gridwidth = 1;
		tab3.add(dtopic_rank_b, c);
		
		c.fill = GridBagConstraints.CENTER;
		c.anchor = GridBagConstraints.LINE_END;
		c.weightx = 0.75;
		c.weighty = 0;
		c.gridx = 0;
		c.gridy = 5;
		c.gridwidth = 1;
		tab3.add(topic_rank_a, c);
		
		c.fill = GridBagConstraints.CENTER;
		c.anchor = GridBagConstraints.CENTER;
		c.weightx = 0.75;
		c.weighty = 0;
		c.gridx = 1;
		c.gridy = 5;
		c.gridwidth = 1;
		tab3.add(dtopic_rank_a, c);
		
		c.fill = GridBagConstraints.CENTER;
		c.anchor = GridBagConstraints.CENTER;
		c.weightx = 0.75;
		c.weighty = 0;
		c.gridx = 1;
		c.gridy = 6;
		c.gridwidth = 1;
		tab3.add(evaluate_topic, c);
		
		evaluate_topic.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(dtopic_rank_b.getSelectedIndex() == 0 || dtopic_rank_a.getSelectedIndex() == 0)
					JOptionPane.showMessageDialog(null, "Please enter interest level before and after.");
				else{
					Topic_Interests sr = new Topic_Interests();
					sr.setCourseId(course_id_list.get(pointer));
					sr.setEditionId(ce_list.get(course_id_list.get(pointer)));
					sr.setTopicId(topic_id_list.get(pointer2));
					sr.setUserName(username);
					sr.setInterestBefore(dtopic_rank_b.getSelectedIndex());
					//System.out.print("Pointer 2: " + pointer2 + "\n");
					//System.out.print("When refresh Table: \n");
					//System.out.print(dskill_rank_b.getSelectedIndex() + "\n");
					sr.setInterestAfter(dtopic_rank_a.getSelectedIndex());
					//System.out.print(dskill_rank_a.getSelectedIndex() + "\n");
					try{
						if(sr.checkExist(connection)) sr.updateTopicRank(connection, 1);
						else sr.updateTopicRank(connection, 0);
						refreshTopic(course_id_list.get(pointer));
						clearTopicInput();
					}catch(SQLException ex){
						SQLError.show(ex);
					}
				}
			}
		});
		
		c.fill = GridBagConstraints.CENTER;
		c.anchor = GridBagConstraints.LINE_END;
		c.weightx = 0.75;
		c.weighty = 0;
		c.gridx = 1;
		c.gridy = 6;
		c.gridwidth = 1;
		tab3.add(t_reset, c);
		
		t_reset.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				Topic_Interests sr = new Topic_Interests();
				sr.setCourseId(course_id_list.get(pointer));
				sr.setEditionId(ce_list.get(course_id_list.get(pointer)));
				sr.setTopicId(topic_id_list.get(pointer2));
				sr.setUserName(username);
				try{
					sr.deleteTopicRank(conn);
					clearTopicInput();
					refreshTopic(course_id_list.get(pointer));
				}catch(SQLException ex){
					SQLError.show(ex);
				}
			}
		});
		
		c.fill = GridBagConstraints.CENTER;
		c.anchor = GridBagConstraints.LINE_END;
		c.weightx = 0.75;
		c.weighty = 0;
		c.gridx = 0;
		c.gridy = 7;
		c.gridwidth = 1;
		tab3.add(topic_add, c);
		
		c.fill = GridBagConstraints.CENTER;
		c.anchor = GridBagConstraints.CENTER;
		c.weightx = 0.75;
		c.weighty = 0;
		c.gridx = 1;
		c.gridy = 7;
		c.gridwidth = 1;
		tab3.add(dtopic_add, c);
		
		dtopic_add.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				JComboBox cb = (JComboBox)e.getSource();
				String select = (String)cb.getSelectedItem();
				if(!select.equals("")) add_new_topic.setEnabled(true);
				else add_new_topic.setEnabled(false);
				if(select.equals("Other Topic")) tnew_topic.setEnabled(true);
			}
		});
		
		c.fill = GridBagConstraints.CENTER;
		c.anchor = GridBagConstraints.LINE_END;
		c.weightx = 0.75;
		c.weighty = 0;
		c.gridx = 0;
		c.gridy = 8;
		c.gridwidth = 1;
		tab3.add(new_topic, c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.CENTER;
		c.weightx = 0.75;
		c.weighty = 0;
		c.gridx = 1;
		c.gridy = 8;
		c.gridwidth = 1;
		tab3.add(tnew_topic, c);
		
		c.fill = GridBagConstraints.CENTER;
		c.anchor = GridBagConstraints.CENTER;
		c.weightx = 0.75;
		c.weighty = 0;
		c.gridx = 1;
		c.gridy = 9;
		c.gridwidth = 1;
		tab3.add(add_new_topic, c);
		
		add_new_topic.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try{
					if(dtopic_add.getSelectedItem().equals("Other Topic")){
						Topics s = new Topics();
						Course_Topics cs = new Course_Topics();
						s.setTopic(tnew_topic.getText());
						cs.setTopic(tnew_topic.getText());
						cs.setCourseId(course_id_list.get(pointer));
						s.insertNewTopic(connection);
						cs.insertNewCTopic(connection);
					}else{
						Course_Topics cs = new Course_Topics();
						cs.setTopic(dtopic_add.getSelectedItem().toString());
						//System.out.print(dtopic_add.getSelectedItem().toString());
						cs.setCourseId(course_id_list.get(pointer));
						cs.insertNewCTopic(connection);
					}
					refreshTopic(course_id_list.get(pointer));
					
				}catch(SQLException ex){
					SQLError.show(ex);
				}
			}
		});
		
		
		tabbedPane.add(Tab3, tab3);
		
		/***************************Tab 3**********************************/
		
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		
		JPanel button_panel = new JPanel();
		JButton last = new JButton("Finish");
		button_panel.add(last);
		contentPane.add(button_panel, BorderLayout.PAGE_END);
		
		last.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				boolean go_exit = true;
				for(int i = 0;i < cr_list.size();i++){
					if(cr_list.get(i).equals(0)){
						go_exit = false;
						break;
					}
				}
				if(!go_exit) JOptionPane.showMessageDialog(null, "Please at least evaluate all the new course experiences, thank you!");
				else{
					JOptionPane.showMessageDialog(null, "Thank you for using our recommendation system.");
					System.exit(0);
				}
			}
		});
		
	}
	
	
	public void refreshTable1()throws SQLException{
			DefaultTableModel model = new DefaultTableModel(new Object[0][0], st_course_list);
			java.util.List<NewCourseAdapter> nc = NewCourseAdapter.getAllNewCourses(connection);
			for(NewCourseAdapter n : nc){
				Object[] o = new Object[2];
				o[0] = n.getDeptCode();
				o[1] = n.getCourseNumber();
				model.addRow(o);
			}
			newcourses_1.setModel(model);
			newcourses_1.repaint();
	}
	
	
	
	public void refreshTable2()throws SQLException{
		DefaultTableModel model = new DefaultTableModel(new Object[0][0], st_course_list);
		java.util.List<NewCourseAdapter> nc = NewCourseAdapter.getAllNewCourses(connection);
		for(NewCourseAdapter n : nc){
			Object[] o = new Object[2];
			o[0] = n.getDeptCode();
			o[1] = n.getCourseNumber();
			model.addRow(o);
		}
		newcourses_2.setModel(model);
		newcourses_2.repaint();
	}
	
	public void refreshTable3()throws SQLException{
		DefaultTableModel model = new DefaultTableModel(new Object[0][0], st_course_list);
		java.util.List<NewCourseAdapter> nc = NewCourseAdapter.getAllNewCourses(connection);
		for(NewCourseAdapter n : nc){
			Object[] o = new Object[2];
			o[0] = n.getDeptCode();
			o[1] = n.getCourseNumber();
			model.addRow(o);
		}
		newcourses_3.setModel(model);
		newcourses_3.repaint();
	}
	
	
	public void refreshEX(int edition_id)throws SQLException{
		DefaultTableModel model = new DefaultTableModel(new Object[0][0], course_ranking_list);
		List<Enrollments> el = Enrollments.getCourseExperience(connection, edition_id, username);
		for(Enrollments e : el){
			Object[] o = new Object[3];
			o[0] = e.getLetterGrade();
			o[1] = rank[e.getInstrRanking()];
			o[2] = rank[e.getCourseRanking()];
			model.addRow(o);
		}
		course_ex.setModel(model);
		course_ex.repaint();
	}
	
	public void refreshSkill(int course_id)throws SQLException{
		DefaultTableModel model = new DefaultTableModel(new Object[0][0], skill_list_column);
		Skill_Rankings sr = new Skill_Rankings();
		sr.setEditionId(ce_list.get(course_id));
		sr.setCourseId(course_id);
		sr.setUserName(username);
		List<Skill_Rankings> skill_rank = sr.getSkillInfo(connection);
		skill_id_list.clear();
		for(Skill_Rankings s : skill_rank){
			Object[] o = new Object[3];
			o[0] = s.getSkillName();
			List<Skill_Rankings> n = s.getSkillRank(connection);
			if(!n.isEmpty()){
				o[1] = rank[n.get(0).getRankBefore()];
				//System.out.print(n.get(0).getRankBefore() + "\n");
				o[2] = rank[n.get(0).getRankAfter()];
				//System.out.print(n.get(0).getRankAfter() + "\n");
			}
			skill_id_list.add(s.getSkillId());
			//System.out.print(skill_id_list.size() + "\n");
			model.addRow(o);
		}
		tskill.setModel(model);
		tskill.repaint();
	}
	
	public void refreshTopic(int course_id)throws SQLException{
		DefaultTableModel model = new DefaultTableModel(new Object[0][0], topic_list_column);
		Topic_Interests sr = new Topic_Interests();
		sr.setEditionId(ce_list.get(course_id));
		sr.setCourseId(course_id);
		sr.setUserName(username);
		List<Topic_Interests> topic_interest = sr.getTopicInfo(connection);
		topic_id_list.clear();
		for(Topic_Interests t : topic_interest){
			Object[] o = new Object[3];
			o[0] = t.getTopicName();
			List<Topic_Interests> n = t.getTopicInterest(connection);
			if(!n.isEmpty()){
				o[1] = interest[n.get(0).getInterestBefore()];
				//System.out.print(n.get(0).getRankBefore() + "\n");
				o[2] = interest[n.get(0).getInterestAfter()];
				//System.out.print(n.get(0).getRankAfter() + "\n");
			}

			topic_id_list.add(t.getTopicId());
			model.addRow(o);
		}
		ttopic.setModel(model);
		ttopic.repaint();
	}
	
	public void clearInput1(){
		sat_list.setSelectedIndex(0);
		rank_list.setSelectedIndex(0);
		grade_list.setSelectedIndex(0);
	}
	
	public void clearSkillInput(){
		dskill_rank_b.setSelectedIndex(0);
		dskill_rank_b.setEnabled(false);
		dskill_rank_a.setSelectedIndex(0);
		dskill_rank_a.setEnabled(false);
		evaluate_skill.setEnabled(false);
		s_reset.setEnabled(false);
	}
	
	public void clearTopicInput(){
		dtopic_rank_b.setSelectedIndex(0);
		dtopic_rank_b.setEnabled(false);
		dtopic_rank_a.setSelectedIndex(0);
		dtopic_rank_a.setEnabled(false);
		evaluate_topic.setEnabled(false);
		t_reset.setEnabled(false);
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
    	
    	FinalInfoCollection app = new FinalInfoCollection(conn, "CC");
    	app.setSize(480, 600);
    	app.setLocationRelativeTo(null);
        app.setVisible(true);
        
	}
	*/
	
}
