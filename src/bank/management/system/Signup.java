package bank.management.system;

import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class Signup extends JFrame implements ActionListener {
    JRadioButton r1,r2,m1,m2,m3;
    JButton next;
    JTextField firstName, lastName, password, email, contact, aadharNumber, PANNumber, address, city, state, country , zip;
    Random ran = new Random();
    long first4 =(ran.nextLong() % 9000L) +1000L;
    String first = " " + Math.abs(first4);
    Signup(){
        super ("SIGN UP");

        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icon/bank.png"));
        Image i2 = i1.getImage().getScaledInstance(100,100,Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel image = new JLabel(i3);
        image.setBounds(25,10,100,100);
        add(image);

        JLabel label1 = new JLabel("APPLICATION FORM NO."+ first);
        label1.setBounds(160,20,600,40);
        label1.setFont(new Font("Raleway",Font.BOLD,38));
        add(label1);

        JLabel label3 = new JLabel("Personal Details");
        label3.setFont(new Font("Raleway", Font.BOLD,22));
        label3.setBounds(290,90,600,30);
        add(label3);

        JLabel labelName = new JLabel("First Name :");
        labelName.setFont(new Font("Raleway", Font.BOLD, 20));
        labelName.setBounds(100,190,200,30);
        add(labelName);

        firstName = new JTextField();
        firstName.setFont(new Font("Raleway",Font.BOLD, 14));
        firstName.setBounds(300,190,400,30);
        add(firstName);

        JLabel labelLastName = new JLabel("Last Name :");
        labelLastName.setFont(new Font("Raleway", Font.BOLD, 20));
        labelLastName.setBounds(100,240,200,30);
        add(labelLastName);

        lastName = new JTextField();
        lastName.setFont(new Font("Raleway",Font.BOLD, 14));
        lastName.setBounds(300,240,400,30);
        add(lastName);

        JLabel labelG = new JLabel("Gender");
        labelG.setFont(new Font("Raleway", Font.BOLD, 20));
        labelG.setBounds(100,290,200,30);
        add(labelG);

        r1 = new JRadioButton("Male");
        r1.setFont(new Font("Raleway", Font.BOLD,14));
        r1.setBackground(new Color(222,255,228));
        r1.setBounds(300,290,60,30);
        add(r1);

        r2 = new JRadioButton("Female");
        r2.setBackground(new Color(222,255,228));
        r2.setFont(new Font("Raleway", Font.BOLD,14));
        r2.setBounds(450,290,90,30);
        add(r2);

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(r1);
        buttonGroup.add(r2);

        JLabel labelEmail = new JLabel("Email address :");
        labelEmail.setFont(new Font("Raleway", Font.BOLD, 20));
        labelEmail.setBounds(100,340,200,30);
        add(labelEmail);

        email = new JTextField();
        email.setFont(new Font("Raleway",Font.BOLD, 14));
        email.setBounds(300,340,400,30);
        add(email);

        JLabel labelPassword = new JLabel("Password :");
        labelPassword.setFont(new Font("Raleway", Font.BOLD, 20));
        labelPassword.setBounds(100,390,200,30);
        add(labelPassword);

        password = new JTextField();
        password.setFont(new Font("Raleway",Font.BOLD, 14));
        password.setBounds(300,390,400,30);
        add(password);


        JLabel labelMs = new JLabel("Marital Status :");
        labelMs.setFont(new Font("Raleway", Font.BOLD, 20));
        labelMs.setBounds(100,440,200,30);
        add(labelMs);

        m1 = new JRadioButton("Married");
        m1.setBounds(300,440,100,30);
        m1.setBackground(new Color(222,255,228));
        m1.setFont(new Font("Raleway", Font.BOLD,14));
        add(m1);

        m2 = new JRadioButton("Unmarried");
        m2.setBackground(new Color(222,255,228));
        m2.setBounds(450,440,100,30);
        m2.setFont(new Font("Raleway", Font.BOLD,14));
        add(m2);

        m3 = new JRadioButton("Other");
        m3.setBackground(new Color(222,255,228));
        m3.setBounds(635,440,100,30);
        m3.setFont(new Font("Raleway", Font.BOLD,14));
        add(m3);

        ButtonGroup buttonGroup1 = new ButtonGroup();
        buttonGroup1.add(m1);
        buttonGroup1.add(m2);
        buttonGroup1.add(m3);

        JLabel labelAadhar = new JLabel("Aadhar Number :");
        labelAadhar.setFont(new Font("Raleway", Font.BOLD, 20));
        labelAadhar.setBounds(100,490,200,30);
        add(labelAadhar);

        aadharNumber = new JTextField();
        aadharNumber.setFont(new Font("Raleway",Font.BOLD, 14));
        aadharNumber.setBounds(300,490,400,30);
        add(aadharNumber);

        JLabel labelPAN = new JLabel("PAN :");
        labelPAN.setFont(new Font("Raleway", Font.BOLD, 20));
        labelPAN.setBounds(100,540,200,30);
        add(labelPAN);

        PANNumber = new JTextField();
        PANNumber.setFont(new Font("Raleway",Font.BOLD, 14));
        PANNumber.setBounds(300,540,400,30);
        add(PANNumber);

        JLabel labelAddress = new JLabel("Address :");
        labelAddress.setFont(new Font("Raleway", Font.BOLD, 20));
        labelAddress.setBounds(100,590,200,30);
        add( labelAddress);

        address = new JTextField();
        address.setFont(new Font("Raleway",Font.BOLD, 14));
        address.setBounds(300,590,400,30);
        add(address);

        JLabel labelCity = new JLabel("City :");
        labelCity.setFont(new Font("Raleway", Font.BOLD, 20));
        labelCity.setBounds(100,640,200,30);
        add(labelCity);

        city = new JTextField();
        city.setFont(new Font("Raleway",Font.BOLD, 14));
        city.setBounds(300,640,400,30);
        add(city);

        JLabel labelState = new JLabel("State:");
        labelState.setFont(new Font("Raleway", Font.BOLD, 20));
        labelState.setBounds(100,690,200,30);
        add(labelState);

        state = new JTextField();
        state.setFont(new Font("Raleway",Font.BOLD, 14));
        state.setBounds(300,690,400,30);
        add(state);

        JLabel labelCountry = new JLabel("Country:");
        labelCountry.setFont(new Font("Raleway", Font.BOLD, 20));
        labelCountry.setBounds(100,740,200,30);
        add(labelCountry);

        country = new JTextField();
        country.setFont(new Font("Raleway",Font.BOLD, 14));
        country.setBounds(300,740,400,30);
        add(country);

        JLabel labelNumber = new JLabel("Phone Number :");
        labelNumber.setFont(new Font("Raleway", Font.BOLD, 20));
        labelNumber.setBounds(100,790,200,30);
        add(labelNumber);

        contact = new JTextField();
        contact.setFont(new Font("Raleway",Font.BOLD, 14));
        contact.setBounds(300,790,400,30);
        add(contact);

        next = new JButton("Submit");
        next.setFont(new Font("Raleway",Font.BOLD, 14));
        next.setBackground(Color.BLACK);
        next.setForeground(Color.WHITE);
        next.setBounds(620,840,80,30);
        next.addActionListener(this);
        add(next);

        getContentPane().setBackground(new Color(222,255,228));
        setLayout(null);
        setSize(850,1000);
        setLocation(360,40);
        setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String formno = first;
        String firstNameText = firstName.getText();
        String lastNameText = lastName.getText();
        String textPassword = password.getText();
        String textEmail = email.getText();
        String gender = null;
        if(r1.isSelected()){
            gender = "Male";
        }else if (r2.isSelected()){
            gender = "Female";
        }
        String marital =null;
        if (m1.isSelected()){
            marital = "Married";
        } else if (m2.isSelected()) {
            marital = "Unmarried";
        } else if (m3.isSelected()) {
            marital = "Other";
        }
        String PAN = PANNumber.getText();
        String AADHAR = aadharNumber.getText();
        String textAddress = address.getText();
        String textCity = city.getText();
        String textZip = "560078";
        String textState = state.getText();
        String textCountry = country.getText();
        String textContact = contact.getText();

        try{
            if (firstName.getText().equals("")){
                JOptionPane.showMessageDialog(null, "Fill all the fields");
            }else {
                Connn c = new Connn();
                String q = "insert into Customer values('"+formno+"', '"+firstNameText+"','"+lastNameText+"','"+textPassword+"','"+textEmail+"','"+textContact+"','"+AADHAR+"','"+PAN+"', '"+gender+"', '"+marital+"','"+textAddress+"','"+textCity+"','"+textState+"','"+textCountry+"','"+textZip+"' )";
                c.statement.executeUpdate(q);
                new Home(formno.toString());
                setVisible(false);
            }

        }catch (Exception E){
            E.printStackTrace();
        }

    }

    public static void main(String[] args) {
        new Signup();
    }
}
