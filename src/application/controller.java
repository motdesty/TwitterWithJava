package application;

import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class controller {

	   @FXML
	    private Button btn1;

	    @FXML
	    private Button btn2;

	    @FXML
	    private Button btn3;

	    @FXML
	    private Button btn4;
	    
	    @FXML
	    private Pane panel;
	    @FXML
	    private Label label1;
	    @FXML
	    private Label label2;
	    @FXML
	    private GridPane pn1;

	    @FXML
	    private GridPane pn2;

	    @FXML
	    private GridPane pn3;

	    @FXML
	    private GridPane pn4;
	    @FXML
	    
	    DBConnection db_conn = new DBConnection();
	    
	    public void handleClick(ActionEvent e) throws SQLException {
		if(e.getSource() == btn1) {
			label1.setText("event1");
			label2.setText("btn1");
			panel.setBackground(new Background(new BackgroundFill(Color.rgb(180, 180, 0), CornerRadii.EMPTY, Insets.EMPTY)));
			pn1.toBack();
			
			//db가 gui에 잘 출력 되는지 확인
			label1.setText(String.valueOf(db_conn.CreateUser("abcd", "abcd1234!"))); // abcd 계정 생성
			if (!db_conn.Login("abcd", "abcd1234!")) { // abcd 계정 로그인 성공하면 true/ 실패하면 false 리턴
				System.out.println("로그인 에러");
				return;
			}
			
		}
		else 
		if(e.getSource() == btn2) {
			label1.setText("event2");
			label2.setText("btn2");
			panel.setBackground(new Background(new BackgroundFill(Color.rgb(120, 120, 120), CornerRadii.EMPTY, Insets.EMPTY)));
			pn2.toBack();
		}
		else 
		if(e.getSource() == btn3) {
			label1.setText("event3");
			label2.setText("btn3");
			panel.setBackground(new Background(new BackgroundFill(Color.rgb(60, 60, 60), CornerRadii.EMPTY, Insets.EMPTY)));
			pn3.toBack();
		}
		else 
		if(e.getSource() == btn4) {
			label1.setText("event4");
			label2.setText("btn4");
			panel.setBackground(new Background(new BackgroundFill(Color.rgb(180, 180, 180), CornerRadii.EMPTY, Insets.EMPTY)));
			pn4.toBack();
		}
	}
	    
	    public void close(MouseEvent e) {
	    	System.exit(0);
	    }
}
