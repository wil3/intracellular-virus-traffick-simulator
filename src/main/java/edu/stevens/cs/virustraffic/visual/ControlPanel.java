package edu.stevens.cs.virustraffic.visual;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.log4j.Logger;

import edu.stevens.cs.virustraffic.VirusTrafficSimulator;

public class ControlPanel extends JPanel{
	private final Logger logger = Logger.getLogger(ControlPanel.class);
	List<RealTimeSimulation> simulators;
	public ControlPanel(List<RealTimeSimulation> simulators){
		this.simulators = simulators;
		create();
	}
	
	private void create(){
		JButton btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent ae) {
				logger.info("Start!");
				for (RealTimeSimulation sim : simulators){
					sim.start();
				}
			
			}
		});
		add(btnStart);
		
		JButton btnPause = new JButton("Pause");
		btnPause.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent ae) {
				logger.info("Pause!");

				for (RealTimeSimulation sim : simulators){
					sim.pause();
				}
			}
		});
		add(btnPause);
		
		JButton btnResume = new JButton("Stop");
		btnResume.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ae) {
				logger.info("Stop!");

				for (RealTimeSimulation sim : simulators){
					sim.stop();
				}
				
			}
		});
		add(btnResume);
		
		
		JSlider virusAlpha = new JSlider(JSlider.HORIZONTAL,
                0, 255, 1);
		virusAlpha.setValue(VisualConstants.COLOR_VIRUS_ALPHA);
		virusAlpha.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider)e.getSource();
			  //  if (!source.getValueIsAdjusting()) {
			        int alpha = (int)source.getValue();
			        logger.info("Slider=" + alpha);
			        VisualConstants.COLOR_VIRUS_ALPHA = alpha;
			  //  }
			}
		});
		add(virusAlpha);
	}
}

