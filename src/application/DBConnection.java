package application;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

class DBConnection {

    Connection conn;
    Statement db_conn;
    int user_id;
    String username;

    DBConnection() {
        String url="jdbc:mysql://localhost:3306/Twitter?useUnicode=true&serverTimezone=Asia/Seoul";
        //jdbc:mysql://localhost/db?useUnicode=true&useJDBCCompliantTim
        //rk_tu_lager?useLegacyDatetimeCode=false&amp;serverTimezone=Europe/Amsterdam&amp;useSSL=false"ezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC
        String userid="root";
        String pwd="1234";

        try {
            conn= DriverManager.getConnection(url, userid, pwd);
            System.out.println("MySQL connection ^O^ \n");
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }



    int CreateUser(String username, String password) {
        if (!PasswordValidCheck(password)) //8자 영문+특문+숫자
            return -1; // 유저 생성 실패(비밀번호 취약)

        String query = "INSERT INTO Users (username, password) VALUES(?,?)";
        System.out.println("create user");
        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);
            int result = stmt.executeUpdate();
            System.out.println(result);
            return 1; // 유저 생성 성공
        }
        catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("username already exist");
            return -2; // 유저 생성 실패(같은 이름의 유저가 있음)
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return -3; // 유저 생성 실패
    }

    private Boolean PasswordValidCheck(String password) {
        String password_pattern = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,20}$"; //8자 영문+특문+숫자
        Boolean password_check = Pattern.matches(password_pattern, password);
        return password_check;
    }

    Boolean Login(String username, String password) throws SQLException {
        String query = "SELECT * from Users where username = ? and password = ?";


        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, username);
        stmt.setString(2, password);
        ResultSet result =stmt.executeQuery();
        System.out.println("login");
        while(result.next()) {
            this.user_id = result.getInt("id"); // 인스턴스 변수 user_id에 Users.id를 저장. 이 값으로 본인 식별.
            this.username = result.getString("username"); // 인스턴스 변수 username에 Users.name를 저장.
        }
        System.out.println("userid" + this.user_id);
            if (user_id > 0)
                return true;  // 로그인 성공
            else return false; // 로그인 실패

    }

    // 트윗 작성
    Boolean WritePost(String board_owner_id, String content) throws SQLException{
        String query = "INSERT INTO Post (author_id, board_owner_id, post) VALUES(?,?,?)";
        int _board_owner_id = GetUserID(board_owner_id);
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, this.user_id);
        stmt.setInt(2, _board_owner_id);
        stmt.setString(3, content);
        int result = stmt.executeUpdate();
        
        if (result == 1) {
            System.out.println("포스트 작성 성공");
            return true;
        }else {
            System.out.println("포스트 작성 실패");
            return false;
        }
    }

    // post id 가 <post_id>인 게시물에 리트윗 작성
    Boolean WriteRetwitte(int post_id, String content) throws SQLException {
        String query = "INSERT INTO Retweet (author_id, post_id, post) VALUES(?,?,?)";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, this.user_id);
        stmt.setInt(2, post_id);
        stmt.setString(3, content);
        int result = stmt.executeUpdate();
        if (result == 1) {
            System.out.println("리트윗 작성 성공");
            return true;
        }else {
            System.out.println("리트윗 작성 실패");
            return false;
        }
    }

    Boolean WriteComment(int post_id, String content) throws SQLException {
        String query = "INSERT INTO Comments (author_id, post_id, comment) VALUES(?,?,?)";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, this.user_id);
        stmt.setInt(2, post_id);
        stmt.setString(3, content);
        int result = stmt.executeUpdate();

        if (result == 1) {
            System.out.println("코멘트 작성 성공");
            return true;
        }else {
            System.out.println("코멘트 작성 실패");
            return false;
        }
    }

    // <username이 작성한 모든 포스트 <트윗, 리트윗>을 반환
    public ArrayList<HashMap<String,Object>> GetAllPost(String username) throws SQLException {
        int user_id = GetUserID(username);
        String query = "SELECT author_id, post, stamp, null as posd_id from Post where Post.author_id =? union all select author_id, post, stamp, post_id from retweet where retweet.author_id=? ORDER BY stamp DESC;";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, user_id);
        stmt.setInt(2, user_id);
        ResultSet result =stmt.executeQuery();

        return ConvertResultSetToArrayList(result);

    }
    
    // post id가 <post_id>인 게시물의 모든 코멘트 반환
    public ArrayList<HashMap<String,Object>> GetComment(int post_id) throws SQLException {
        int user_id = GetUserID(username);
        String query = "SELECT * FROM Comments WHERE post_id = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, post_id);
        ResultSet result =stmt.executeQuery();

        return ConvertResultSetToArrayList(result);
    }

    public Boolean DoFollow(String username_to_follow)  {

        String query = "INSERT INTO Follow (follower, followee) VALUES (?,?)";

        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, this.username);
            stmt.setString(2, username_to_follow);
            int result = stmt.executeUpdate();

            if (result == 1) {
                System.out.println("팔로우 성공");
                return true;
            }else {
                System.out.println("팔로우 실패");
                return false;
            }
        }
        catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("already following");
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }



    public Boolean UnFollow(String userane_to_unfollow) throws SQLException {
        String query = "DELETE FROM Follow WHERE follower = ? and followee = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, this.username);
        stmt.setString(2, userane_to_unfollow);
        int result = stmt.executeUpdate();

        if (result == 1) {
            System.out.println("언팔로우 성공");
            return true;
        }else {
            System.out.println("언팔로우 실패");
            return false;
        }
    }

    public ArrayList<HashMap<String,Object>> GetAllFollowee(String username) throws SQLException {
        String query = "SELECT  followee FROM Follow WHERE follower = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, username);
        ResultSet result =stmt.executeQuery();

        return ConvertResultSetToArrayList(result);

    }


    //ResultSet 을  ArrayList<HashMap<String,Object>> 형식으로 변환
    private ArrayList<HashMap<String,Object>> ConvertResultSetToArrayList(ResultSet rs) throws SQLException {
        ResultSetMetaData md = rs.getMetaData();
        int columns = md.getColumnCount();
        System.out.println(columns);
        ArrayList<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
        while(rs.next()) {
            HashMap<String,Object> row = new HashMap<String, Object>(columns);
            for(int i=1; i<=columns; ++i) {
                row.put(md.getColumnName(i), rs.getObject(i));
//                System.out.println(md.getColumnName(i));
////                System.out.println(rs.getObject(i));
            }
            list.add(row);
        }

        return list;
    }
    // username으로 users.id 반환
    private int GetUserID(String username) throws SQLException{
        String query = "SELECT * from Users where username = ?";
        int USERID = 0;

        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, username);
        ResultSet result =stmt.executeQuery();
        result.next();
        USERID = result.getInt("id"); // 인스턴스 변수 user_id에 Users.id를 저장. 이 값으로 본인 식별.
        System.out.println(USERID+"  "+ username);
        return USERID;
    }




    void sqlRun() {
        String query="SELECT * FROM Users";
        try {
            db_conn =conn.createStatement();
            ResultSet rs=db_conn.executeQuery(query);
            //System.out.println(" BOOK NO \tBOOK NAME \t\tPUBLISHER \tPRICE ");
            while(rs.next()) {
                System.out.print("\t"+rs.getString(1));
                System.out.print("\t\t"+rs.getString(2));
            }
            rs.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
}