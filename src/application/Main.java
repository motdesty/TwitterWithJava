package application;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	static void PrintList(ArrayList<HashMap<String, Object>> list) { // ArrayList <HashMap<String,Object>> 형식을 프린트
		for (int i = 0; i < list.size(); i++) {
			System.out.println("list 순서 " + i + "번쨰");
			for (Map.Entry<String, Object> elem : list.get(i).entrySet()) {
				System.out.println(String.format("%s: %s", elem.getKey(), elem.getValue()));
			}
		}
	}

	public static void main(String[] args) throws SQLException {
		System.out.println("Hello world!");
		

		DBConnection db_conn = new DBConnection();
		db_conn.sqlRun();
		launch(args);//GUI실행
		
		
		db_conn.CreateUser("abcd", "abcd1234!"); // abcd 계정 생성
		if (!db_conn.Login("abcd", "abcd1234!")) { // abcd 계정 로그인 성공하면 true/ 실패하면 false 리턴
			System.out.println("로그인 에러");
			return;
		}

		for (int i = 0; i < 10; i++) {
			db_conn.CreateUser("user" + Integer.toString(i), "abcd1234!"); // abcd 계정 생성
			db_conn.DoFollow("user" + Integer.toString(i));
		}

		db_conn.WritePost("user2", "Write Post1"); // user2의 board에 abcd로 글쓰기

		// username이 abcd인 유저의 모든 포스트(트윗, 리트윗)을 arraylist로 반환
		ArrayList<HashMap<String, Object>> post_list = db_conn.GetAllPost("abcd");
		PrintList(post_list); // arraylist 출력

		db_conn.WriteComment(1, "comment1"); // post id: 1에 코멘트 작성
		ArrayList<HashMap<String, Object>> comment_list = db_conn.GetComment(1); // post id :1의 모든 코멘트 반환
		PrintList(comment_list); // 출력

		ArrayList<HashMap<String, Object>> followee_list = db_conn.GetAllFollowee("abcd");
		PrintList(followee_list); // 출력

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		Parent root = FXMLLoader.load(getClass().getResource("window.fxml"));
		primaryStage.setTitle("test");
		// primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setScene(new Scene(root));
		primaryStage.show();
	}

}
