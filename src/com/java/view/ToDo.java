package com.java.view;

import com.data.dao.UserDao;
import com.data.model.User;
import util.DbUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.util.Vector;

public class ToDo {
    private Vector v;
    private StringBuffer stringBuffer = new StringBuffer();

    public void todo(User user){
        Vector v2 = new Vector();

        Dimension ss = Toolkit.getDefaultToolkit().getScreenSize();
        Font font = new Font("Cooper Black", Font.BOLD, 20);

        //set frame position
        JFrame jFrame = new JFrame("Todo List");
        jFrame.setBounds(ss.width / 3, ss.height / 4,600, 500);

        //set frame Icon
        ImageIcon imageIcon = new ImageIcon("src/com/java/image/icon.jpg");
        jFrame.setIconImage(imageIcon.getImage());
        jFrame.setLayout(null);

        JTextField jt = new JTextField();
        jt.setBounds(100,10,250,30);

        JList jl = new JList();
        jl.setFont(font);
        jl.setBounds(100,50,360,200);
        JList jl2 = new JList();
        jl2.setFont(font);
        jl2.setBounds(100,280,360,150);

        jl.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() == 2){
                    JList myList = (JList) e.getSource();
                    int index = myList.getSelectedIndex();
                    Object obj = myList.getModel().getElementAt(index);
                    System.out.println(obj.toString());
                    v.remove(obj);
                    v2.add(obj.toString());
                    jl.setListData(v);
                    jl2.setListData(v2);
                    StringBuffer sb = new StringBuffer();
                    for(Object s : v){
                        sb.append(s.toString());
                        sb.append("!");
                    }
                    stringBuffer = sb;
                    user.setTask(stringBuffer.toString());
                    UserDao ud = new UserDao();
                    DbUtil du = new DbUtil();
                    try {
                        Connection con = du.getCon();
                        if (ud.task(con, user) > 0) {
                        } else {
                            JOptionPane.showMessageDialog(null, "Error!");
                        }
                        System.out.println("Database connection success");
                    } catch (Exception e1) {
                        e1.printStackTrace();
                        System.out.println("Database connection failure");
                    }

                    System.out.println(v);
                }
            }
        });

        JLabel jLabel = new JLabel("Complete");
        jLabel.setBounds(100,250,90,30);



        JButton jb = new JButton("Submit");
        jb.setBounds(370,10,90,30);
        jb.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e1) {
                String task = jt.getText();
                stringBuffer.append(task);
                stringBuffer.append("!");
                jt.setText("");
                user.setTask(stringBuffer.toString());
                UserDao ud = new UserDao();
                DbUtil du = new DbUtil();
                try {
                    Connection con = du.getCon();
                    if (ud.task(con, user) > 0) {
                        v.add(task);
                        jl.setListData(v);
                    } else {
                        JOptionPane.showMessageDialog(null, "Error!");
                    }
                    System.out.println("Database connection success");
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Database connection failure");
                }
            }
        });

        JButton jb2 = new JButton("Show");
        jb2.setBounds(480,10,80,30);
        jb2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e1) {
                UserDao ud = new UserDao();
                DbUtil du = new DbUtil();
                try {
                    Connection con = du.getCon();
                    stringBuffer = ud.show(con, user);
                    v = showTask(stringBuffer.toString());
                    jl.setListData(v);
                    System.out.println("Database connection success");
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Database connection failure");
                }
            }
        });


        jFrame.add(jt);
        jFrame.add(jb);
        jFrame.add(jb2);
        jFrame.add(jl);
        jFrame.add(jLabel);
        jFrame.add(jl2);

        jFrame.setVisible(true);
        jFrame.setResizable(false);
        jFrame.setDefaultCloseOperation(jFrame.EXIT_ON_CLOSE);
    }

    public Vector showTask(String string){
        StringBuffer s = new StringBuffer(string);
        Vector vector = new Vector();
        if (s == null) {
            return vector;
        }
        while(s.indexOf("!") > 0){
            int index = s.indexOf("!");
            vector.add(s.substring(0,index));
            s.delete(0,index+1);
        }
        return vector;
    }
}
