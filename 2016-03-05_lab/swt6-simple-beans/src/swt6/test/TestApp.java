package swt6.test;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import swt6.beans.ProgressMeter;
import swt6.beans.TimerEvent;
import swt6.beans.TimerListener;
import swt6.simplebeans.Timer;
import swt6.beans.ProgressMeter.ShapeType;

public class TestApp extends JFrame {
	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private JPanel panel;
	private JButton btnStart;
	private JButton btnStop;
	private JButton btnContinue;
	private JButton btnSpeedUp;
	private JButton btnSpeedDown;
	private ProgressMeter progressMeter;
	/**
	 * @wbp.nonvisual location=391,59
	 */
	private final Timer timer = new Timer();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TestApp frame = new TestApp();
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
	public TestApp() {
		timer.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent event) {
				handleTimerPropertyChange(event);
			}
		});
		timer.addTimerListener(new TimerListener() {
			public void expired(TimerEvent event) {
				handleTimerExpired(event);
			}
		});

		timer.setNumTicks(50);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 351, 363);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		panel = new JPanel();
		contentPane.add(panel, BorderLayout.SOUTH);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleBtnStartActionPerformed(e);
			}
		});
		panel.add(btnStart);

		btnStop = new JButton("Stop");
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleBtnStopActionPerformed(e);
			}
		});
		panel.add(btnStop);

		btnContinue = new JButton("Continue");
		btnContinue.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleBtnContinueActionPerformed(e);
			}
		});
		panel.add(btnContinue);

		btnSpeedUp = new JButton("++");
		btnSpeedUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleBtnSpeedUpActionPerformed(e);
			}
		});
		panel.add(btnSpeedUp);

		btnSpeedDown = new JButton("--");
		btnSpeedDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleBtnSpeedDownActionPerformed(e);
			}
		});
		panel.add(btnSpeedDown);

		progressMeter = new ProgressMeter();
		contentPane.add(progressMeter, BorderLayout.CENTER);
	}

	protected void handleBtnStartActionPerformed(ActionEvent e) {
		if (!timer.isRunning()) {
			progressMeter.reset();
			timer.reset();
			timer.start();
		}
	}

	protected void handleBtnStopActionPerformed(ActionEvent e) {
		if (timer.isRunning()) {
			timer.stop();
		}
	}

	protected void handleBtnContinueActionPerformed(ActionEvent e) {
		if (!timer.isRunning()) {
			timer.start();
		}
	}

	protected void handleBtnSpeedUpActionPerformed(ActionEvent e) {
		if (timer.getInterval() > 100) {
			timer.setInterval(timer.getInterval() - 100);
		}
	}

	protected void handleBtnSpeedDownActionPerformed(ActionEvent e) {
		timer.setInterval(timer.getInterval() + 100);
	}

	private void handleTimerExpired(TimerEvent event) {
		EventQueue.invokeLater(() -> progressMeter.progress());
	}

	// phase 2: bounded property
	protected void handleTimerPropertyChange(PropertyChangeEvent event) {
		System.out.printf("Property %s changed its value from %d to %d.%n", event.getPropertyName(),
				event.getOldValue(), event.getNewValue());
	}
	// phase 2: end
}
