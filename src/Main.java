import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Main {


    public static void main(String[] args) throws SQLException {
        System.out.println("Hello world!");

        DBConnection db_conn = new DBConnection();
        db_conn.sqlRun();
//        db_conn.CreateUser("abcd", "abcd1234!");
        db_conn.Login("abcd", "abcd1234!");

        db_conn.WritePost("Write Post1");
        db_conn.WritePost("Write Post2");
        db_conn.WritePost("Write Post3");

        ArrayList <HashMap<String,Object>> post_list =db_conn.GetAllPost("abcd");

        for(int i = 0; i < post_list.size(); i++){
            System.out.println("post_list 순서 " + i + "번쨰");
            for( Map.Entry<String, Object> elem : post_list.get(i).entrySet() ){
                System.out.println( String.format("%s: %s", elem.getKey(), elem.getValue()) );
            }
        }
    }
}


class DBConnection {

    Connection conn;
    Statement db_conn;
    int user_id;

    DBConnection() {
        String url="jdbc:mysql://localhost:3306/Twitter";
        String userid="root";
        String pwd="1234";

        try {
            conn= DriverManager.getConnection(url, userid, pwd);
            System.out.println("MySQL connection ^O^ \n");
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }



    Boolean CreateUser(String username, String password) throws SQLException {
        if (!PasswordValidCheck(password)) //8자 영문+특문+숫자
            return false;

        String query = "INSERT INTO Users (username, password) VALUES(?,?)";
        System.out.println("create user");

        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, username);
        stmt.setString(2, password);
        int result = stmt.executeUpdate();

        System.out.println(result);
        return true;

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
        }
        System.out.println("userid" + this.user_id);
        return true;
    }

    void WritePost(String content) throws SQLException{
        String query = "INSERT INTO Post (tweet, user_id) VALUES(?,?)";

        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, content);
        stmt.setInt(2, this.user_id);
        int result = stmt.executeUpdate();

        return;
    }

    public ArrayList<HashMap<String,Object>> GetAllPost(String username) throws SQLException {
        int user_id = GetUserID(username);
        String query = "SELECT * from Post where user_id = ? ORDER BY id DESC ";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, user_id);
        ResultSet result =stmt.executeQuery();

        return ConvertResultSetToArrayList(result);

    }

    private ArrayList<HashMap<String,Object>> ConvertResultSetToArrayList(ResultSet rs) throws SQLException {
        ResultSetMetaData md = rs.getMetaData();
        int columns = md.getColumnCount();
        System.out.println(columns);
        ArrayList<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
        System.out.println(rs.next());
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

//
//    String getData(String in) {
//
//        String query="SELECT phone FROM test where name =" + "\"" + in + "\"";
//        //System.out.print(query);
//        String a=null;
//        try {
//            db_conn=con.createStatement();
//            ResultSet rs=db_conn.executeQuery(query);
//
//            if (rs.next() == true)
//                a=rs.getString(1);
//            else
//                a = "사용자가 없습니다.";
//            rs.close();
//        } catch(SQLException e) {
//            e.printStackTrace();
//        }
//
//        return a;
//    }
//
//    void updateData(String in, String in1) {
//        String query="UPDATE test set phone = " + "\"" + in1 + "\"" + " where name =" + "\"" + in + "\"";
//        //System.out.print(query);
//        try {
//            db_conn.executeUpdate(query);
//        } catch(SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    void insertData(String in, String in1) {
//        String query="insert into test values (" + "\"" + in + "\"" + " , " + "\"" + in1 + "\" )";
//        //System.out.print(query);
//        try {
//            db_conn.executeUpdate(query);
//        } catch(SQLException e) {
//            e.printStackTrace();
//        }
//    }

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