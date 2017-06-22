package window.cardTournament;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import data.exception.DataNotFoundException;
import data.robot.RoboList;
import data.team.TeamList;
import data.tournament.Game;
import data.tournament.Tournament;

public class TournamentViewer extends JPanel implements UpdateTourViewListener {
	private static final long serialVersionUID = 1L;

	private RoboList robo_list = null;
	private TeamList team_list = null;
	private Tournament tour = null;

	private String[] team_name_list;

	
	public TournamentViewer() {
		this.setOpaque(false);
	}
	
	public void set_robo_list(RoboList list){
		robo_list = list;
	}
	public void set_team_list(TeamList list){
		team_list = list;
	}
	public void set_tournament(Tournament t){
		tour = t;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;

		if(robo_list == null) return;
		if(team_list == null) return;
		if(tour == null) return;
		
		//	大きさ取得
		Dimension wh = this.getSize();
		int width = wh.width;
		int height = wh.height;
		
		int num_of_team = tour.get_num_of_team();
		
		// ラウンド数を計算
		int round;
		for(round = 0; Math.pow(2, round) < num_of_team; round++);
		
		// トーナメント表の最終段の形状を取得
		int[] seed_info = tour.get_seed_info();
		if(seed_info == null) return;
		
		// チームリストのチェック
		if( num_of_team != team_name_list.length) return;
		
		// 試合結果取得
		int[] result = tour.get_result();
		if( result.length != num_of_team - 1 ) return;
		
		// -----------------------------------------------
		// トーナメント表の線とチーム名を書く
		// int name_width = (int)( width*0.38 );
		int name_width = (int)( 300 );
		int tour_width = (int)( (width-name_width-width*0.02) / (round+1) );
		g2.setStroke(new BasicStroke(4.0f));
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);		
		
		// 最初の1本
		if(result[result.length-1] == -1){
			g2.setColor(Color.BLACK);
		}else{
			g2.setColor(Color.RED);
		}
		g2.drawLine(width - tour_width, height/2, width, height/2);
		
		// 残りの線とチーム名
		int game_number2_horizontal1 = 0; // 1回戦の線色を決定するためのカウンター
		int game_number2_horizontal2_long = 0; // 1回戦のない2回戦の線色を決定するためのカウンター
		int game_number2_horizontal2 = 0; // 2回戦以降の線色を決定するためのカウンター
		int game_number_vertical = 0; // 2回戦以降の線色を決定するためのカウンター
		for(int x_cnt = round; x_cnt > 0; x_cnt--){
			double y_size =  height / Math.pow(2, x_cnt+1) ;
			int team_number = 0;
			for(int y_cnt = 0; y_cnt < Math.pow(2, x_cnt); y_cnt++){
				// horizontal line
				int y = (int)( (2*y_cnt+1) * y_size );
				if( x_cnt == round-1 && seed_info[y_cnt] == Game.TYPE_TEAM){
					// 横線 (長い2回戦)
					int game_number2 = game_number2_horizontal1 + game_number2_horizontal2_long;
					if( result[game_number2/2] == 0 && game_number2%2 == 0 ){
						g2.setColor(Color.RED);
					}else if( result[game_number2/2] == 1 && game_number2%2 == 1 ){
						g2.setColor(Color.RED);
					}else{
						g2.setColor(Color.BLACK);
					}
					g2.drawLine(width - tour_width*(x_cnt+2), y, width - tour_width*x_cnt, y);
					game_number2_horizontal2_long++;
					
					// チーム名 (2回戦から出場)
					g2.setColor(Color.BLACK);
					g2.setFont(new Font("", Font.PLAIN, 15));
					int y_width = g2.getFontMetrics().stringWidth(team_name_list[team_number]);
					g2.drawString(team_name_list[team_number], name_width - y_width, y+6);
					team_number++;
				}else if(x_cnt == round && seed_info[y_cnt/2] == Game.TYPE_TEAM){
					// 2回戦から試合なので、線を描画しない
					if(y_cnt%2 == 0) team_number++;
				}else{
					// 横線 (その他すべて)
					if( x_cnt == round ){
						if( result[game_number2_horizontal1/2] == 0 && game_number2_horizontal1%2 == 0 ){
							g2.setColor(Color.RED);
						}else if( result[game_number2_horizontal1/2] == 1 && game_number2_horizontal1%2 == 1 ){
							g2.setColor(Color.RED);
						}else{
							g2.setColor(Color.BLACK);
						}
					}else{
						if( result[game_number2_horizontal2] == -1 ){
							g2.setColor(Color.BLACK);
						}else{
							g2.setColor(Color.RED);
						}
					}
					g2.drawLine(width - tour_width*(x_cnt+1), y, width - tour_width*x_cnt, y);
					
					// チーム名 (1回戦から出場)
					if(x_cnt == round){
						g2.setColor(Color.BLACK);
						g2.setFont(new Font("", Font.PLAIN, 15));
						int y_width = g2.getFontMetrics().stringWidth(team_name_list[team_number]);
						g2.drawString(team_name_list[team_number], name_width - y_width, y+6);
						team_number += 1;
						game_number2_horizontal1++;
					}else{
						game_number2_horizontal2++;
						game_number2_horizontal2_long++;
					}
					if(x_cnt == round-1) team_number += 2;
				}
				
				// vertical line
				if( y_cnt%2 == 0 ){
					if( !(x_cnt == round && seed_info[y_cnt/2] == Game.TYPE_TEAM) ){
						int y_upper  = (int)( (2*y_cnt+1) * y_size );
						int y_lower  = (int)( (2*y_cnt+3) * y_size );
						int y_center = (int)( (y_upper+y_lower)/2 );
						if( result[game_number_vertical] == 0 ){
							g2.setColor(Color.RED);
							g2.drawLine(width - tour_width*x_cnt, y_upper, width - tour_width*x_cnt, y_center);
							g2.setColor(Color.BLACK);
							g2.drawLine(width - tour_width*x_cnt, y_center, width - tour_width*x_cnt, y_lower);
						}else if( result[game_number_vertical] == 1 ){
							g2.setColor(Color.BLACK);
							g2.drawLine(width - tour_width*x_cnt, y_upper, width - tour_width*x_cnt, y_center);
							g2.setColor(Color.RED);
							g2.drawLine(width - tour_width*x_cnt, y_center, width - tour_width*x_cnt, y_lower);
						}else{
							g2.setColor(Color.BLACK);
							g2.drawLine(width - tour_width*x_cnt, y_upper, width - tour_width*x_cnt, y_center);
							g2.drawLine(width - tour_width*x_cnt, y_center, width - tour_width*x_cnt, y_lower);
						}
						game_number_vertical++;
					}
				}
			}
			
		}
		
	}

	@Override
	public void update_tour_view() {
		int[] team = tour.get_team_list();
		team_name_list = new String[team.length];
		for(int i = 0; i < team.length; i++){
			try {
				team_name_list[i] = team_list.get_team_name(team[i]);
			} catch (DataNotFoundException e) {
				// e.printStackTrace();
				team_name_list[i] = "No Data"+i;
			}
		}
		this.repaint();
	}		
}