import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.*;

import java.sql.*;

public class Follow extends JFrame implements ActionListener
{
    public JFrame Follow = new JFrame();
    public JPanel contentPane;
    JToggleButton follow = new JToggleButton("팔로우");
    JLabel following = new JLabel("팔로잉");
    JLabel follower = new JLabel("팔로워");
    JLabel followingnum = new JLabel("");
    JLabel followernum = new JLabel("");
    String user = "def"; // 팔로우 or 언팔로우 받을 유저 (user2 임의로 설정)

    public void init()
    {
        Follow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Follow.setBounds(100, 100, 212, 186);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        Follow.setContentPane(contentPane);
        contentPane.setLayout(null);

        follow.setBounds(29, 93, 135, 23);
        contentPane.add(follow);

        following.setBounds(29, 51, 57, 15);
        contentPane.add(following);

        follower.setBounds(107, 51, 57, 15);
        contentPane.add(follower);

        followingnum.setBounds(69, 51, 57, 15);
        contentPane.add(followingnum);

        followernum.setBounds(145, 51, 57, 15);
        contentPane.add(followernum);

        Follow.setVisible(true);
    }
    public void start()
    {
        follow.addActionListener(this);
    }
    Follow()
    {
        start();
        init();
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        JToggleButton follow = (JToggleButton)e.getSource();
        if (follow.isSelected()) {
            follow.setText("언팔로우");
            // 버튼을 한 번 눌렀을 때 언팔로우로 변경
            DBConnection followbutton = new DBConnection();
            followbutton.follow(user);
            // 버튼을 눌렀을 때 DB의 follower, followee 값 변경
            followernum.setText(followbutton.getfollowerData(user));
            // 팔로워 값 받아오기
            followingnum.setText(followbutton.getfollowingData(user));
            // 팔로잉 값 받아오기

//            System.out.println("button selected");
        }
        else
        {
            follow.setText("팔로우");
            DBConnection followbutton = new DBConnection();
            followbutton.unfollow(user);
            followernum.setText(followbutton.getfollowerData(user));
            followingnum.setText(followbutton.getfollowingData(user));

//            System.out.println("button not selected");
        }
    }
}

class DBConnection {
    Connection conn;

    DBConnection() {
        String url = "jdbc:mysql://localhost:3306/twitter";
        String userid = "twitter";
        String pwd = "1234";

        try {
            conn = DriverManager.getConnection(url, userid, pwd);
//            System.out.println("MySQL connection ^O^ \n");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //user 1, 2 아이디
    String user1 = "abc";

    void follow(String user) {
        String query = "insert into Follow (follower, followee) values ('" + user1 + "', '" + user + "')";
        // 로그인 한 유저 = user1이라고 가정
        // 로그인 한 유저가 user를 팔로우, MySQL 쿼리 작성
        try {
            Statement stmt = conn.createStatement();
            stmt.execute(query);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    void unfollow(String user) {
        String query = "delete from Follow where follower = " + "\"" + user1 + "\"" + "and followee = " + "\"" + user + "\"";
        // 로그인 한 유저 = user1이라고 가정
        // 로그인 한 유저가 user를 언팔로우, MySQL 쿼리 작성
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(query);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    String getfollowerData(String user)
    {
        String output = "";
        String followerquery = "select count(follower) from Follow where followee = '" + user + "'";
        // 팔로워수 출력
        try
        {
            Statement stmt = conn.createStatement();
            ResultSet followerrs = stmt.executeQuery(followerquery);

            if (followerrs.next() == true)
            {
                output = followerrs.getString(1);
            }
            else
            {
                output = "팔로잉 목록이 없습니다.";
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return output;

    }

    String getfollowingData(String user)
    {
        String output = "";
        String followingquery = "select count(follower) from Follow where follower = '" + user + "'";
        // 팔로잉수 출력
        try
        {
            Statement stmt = conn.createStatement();
            ResultSet followingrs = stmt.executeQuery(followingquery);

            if (followingrs.next() == true)
            {
                output = followingrs.getString(1);
            }
            else
            {
                output = "팔로잉 목록이 없습니다.";
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return output;

    }
}

class main {
    public static void main(String[] args) {

        new Follow();

    }
}
