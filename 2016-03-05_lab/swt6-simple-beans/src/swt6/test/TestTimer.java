package swt6.test;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import swt6.simplebeans.Timer;
import java.awt.Font;
import javax.swing.SwingConstants;
import swt6.beans.TimerListener;
import swt6.beans.TimerEvent;

public class TestTimer extends JFrame {

	private JPanel contentPane;
	/**
	 * @wbp.nonvisual location=491,29
	 */
	private final Timer timer = new Timer();
	private JLabel lblCounter;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TestTimer frame = new TestTimer();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public TestTimer() {
		timer.addTimerListener(new TimerListener() {
			public void expired(TimerEvent evt) {
				handleTimerExpired(evt);
			}
		});
		timer.setNumTicks(50);
		timer.setInterval(200);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		lblCounter = new JLabel("Counter");
		lblCounter.setHorizontalAlignment(SwingConstants.CENTER);
		lblCounter.setFont(new Font("Tahoma", Font.PLAIN, 40));
		contentPane.add(lblCounter, BorderLayout.CENTER);

		JButton btnStart = new JButton("Start Timer");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleActionPerformed(e);
			}
		});
		contentPane.add(btnStart, BorderLayout.SOUTH);
	}

	protected void handleActionPerformed(ActionEvent e) {
		timer.start();
	}

	// Invoked in a background Thread, so no access to Main-Thread
	protected void handleTimerExpired(TimerEvent evt) {
		// Invokes lambda on Main-Thread
		EventQueue.invokeLater(() -> {
			lblCounter.setText(String.format("%d-%d", evt.getTickCount(), evt.getNumTicks()));
		});
	}

}
