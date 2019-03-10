package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.math.plot.Plot2DPanel;

import utils.Mensajes;
import controller.Controller;
import model.cromosome.Chromosome;
import model.crossover.ContinuousCrossover;
import model.crossover.Crossover;
import model.crossover.StandardCrossover;
import model.initialization.Initialization;
import model.initialization.IncrementalInitialization;
import model.mutation.Mutation;
import model.mutation.ReplaceMutation;
import model.mutation.ExtraTestMutation;
import model.replacement.Replacement;
import model.replacement.DirectReplacement;
import model.replacement.ElitistReplacement;
import model.selection.StochasticUniversal;
import model.selection.Ranking;
import model.selection.Remains;
import model.selection.Roulette;
import model.selection.Selection;
import model.selection.Tournament;
import model.selection.Truncation;

@SuppressWarnings("serial")
public class Ventana extends JFrame{
	private final Controller c;
	
	private Initialization inicializacion;
	private Selection seleccion;
	private Crossover cruce;
	private Mutation mutacion;
    private Replacement reemplazo;
    
    private Hebra hebra;
    
    private JPanel pIzd;
	private JPanel pDch;
	private JPanel pResultado;
	
	private Plot2DPanel pGraph;
	
    private JLabel lPorcTrunc;
    private JLabel lPartTorn;
    private JLabel lVicTorn;
    private JLabel lPorcElit;

    private JLabel lArbol = new JLabel();
    private JLabel lFitness = new JLabel();
    
	private JTextField tfPob;
	private JTextField tfIter;
	private JTextField tfMaxTests;
    private JTextField tfPorcTrunc;
    private JTextField tfPartTorn;
    private JTextField tfVicTorn;
	private JTextField tfProbCruce;
    private JTextField tfProbMut;
    private JTextField tfPorcElit;

    private JTextArea taFSM;
    private JTextArea taTests;
    private JTextArea taMutants;
    
	private JComboBox<Initialization> cbInic;
	private JComboBox<Selection> cbSelec;
	private JComboBox<Crossover> cbCross;
	private JComboBox<Mutation> cbMut;
	private JComboBox<Replacement> cbReemp;

	private JButton bGenerateData;
	private JButton bCombinatory;
	private JButton bGreedy;
    private JButton bPlay;
    
	public Ventana(final Controller c) {
		this.c = c;
		crearVentana();
	}
	
	private void crearVentana() {
		setTitle(Mensajes.TITULO);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addComponentes(getContentPane());

		setExtendedState(MAXIMIZED_BOTH);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void addComponentes(Container pane) {
		pane.setLayout(new GridLayout(1,2,10,10));

		setupIzdSup();
		setupDchSup();
		setupIzdInf();

		pane.add(pIzd);
		pane.add(pDch);
	}
	
	private void setupIzdSup() {
		pIzd = new JPanel();
		pIzd.setLayout(new GridLayout(19, 2, 10, 10));

//Tamaño
		tfPob = new JTextField();
		tfPob.setText("100");
		pIzd.add(new JLabel(Mensajes.POBLACION, JLabel.RIGHT));
		pIzd.add(tfPob);

//Iteraciones
		tfIter = new JTextField();
		tfIter.setText("100");
		pIzd.add(new JLabel(Mensajes.ITERACIONES, JLabel.RIGHT));
		pIzd.add(tfIter);

//Profundidad inicial
		tfMaxTests = new JTextField();
		tfMaxTests.setText("80");
		pIzd.add(new JLabel(Mensajes.MAX_TESTS, JLabel.RIGHT));
		pIzd.add(tfMaxTests);
		
//Método Inicializacion
		final Initialization[] metInic = {new IncrementalInitialization()};
	
		cbInic = new JComboBox<>(metInic);
		pIzd.add(cbInic);
		pIzd.add(new JLabel());
	
		ActionListener cmbInicListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				inicializacion = (Initialization)cbInic.getSelectedItem();
			}
		};
		
		cbInic.addActionListener(cmbInicListener);
		
//Método Seleccion
		final Selection[] metSel = {new Ranking(), new Remains(), new Roulette(), 
				new Tournament(), new Truncation(0.1), new StochasticUniversal()};
		
		cbSelec = new JComboBox<>(metSel);
		pIzd.add(cbSelec);
		pIzd.add(new JLabel());

		ActionListener cmbSelecListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				seleccion = (Selection)cbSelec.getSelectedItem();
				switch(seleccion.getSelection()) {
				case 1:
				case 2:
				case 3:
				case 6:
                    lPorcTrunc.setVisible(false);
                    tfPorcTrunc.setVisible(false);
                    lPartTorn.setVisible(false);
                    tfPartTorn.setVisible(false);
                    lVicTorn.setVisible(false);
                    tfVicTorn.setVisible(false);
					break;
				case 4:
                    lPorcTrunc.setVisible(false);
                    tfPorcTrunc.setVisible(false);
                    lPartTorn.setVisible(true);
                    tfPartTorn.setVisible(true);
                    lVicTorn.setVisible(true);
                    tfVicTorn.setVisible(true);
					break;
				case 5:
                    lPorcTrunc.setVisible(true);
                    tfPorcTrunc.setVisible(true);
                    lPartTorn.setVisible(false);
                    tfPartTorn.setVisible(false);
                    lVicTorn.setVisible(false);
                    tfVicTorn.setVisible(false);
					break;
				default:
					System.err.println("Error cambiando de selección");
				}
			}
		};
		
		cbSelec.addActionListener(cmbSelecListener);
		
//elitismo de truncamiento
		lPorcTrunc = new JLabel(Mensajes.PORCENTAJE_TRUNCAMIENTO, JLabel.RIGHT);
		tfPorcTrunc = new JTextField();
		tfPorcTrunc.setText("0.125");
		pIzd.add(lPorcTrunc);
		pIzd.add(tfPorcTrunc);

//participantes y victoria de torneo
		lPartTorn = new JLabel(Mensajes.PARTICIPANTES_TORNEO, JLabel.RIGHT);
		tfPartTorn = new JTextField();
		tfPartTorn.setText("3");
		pIzd.add(lPartTorn);
		pIzd.add(tfPartTorn);
		lVicTorn = new JLabel(Mensajes.PORCENTAJE_VICTORIA_TORNEO, JLabel.RIGHT);
		tfVicTorn = new JTextField();
		tfVicTorn.setText("0.8");
		pIzd.add(lVicTorn);
		pIzd.add(tfVicTorn);

//Método Cruce
		final Crossover[] metCross = {new ContinuousCrossover(), new StandardCrossover()};
		
		cbCross = new JComboBox<>(metCross);
		pIzd.add(cbCross);
		pIzd.add(new JLabel());

		ActionListener cmbCrossListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cruce = (Crossover)cbCross.getSelectedItem();
			}
		};
		
		cbCross.addActionListener(cmbCrossListener);
		
//Prob Cruce
		tfProbCruce = new JTextField();
		tfProbCruce.setText("0.6");
		pIzd.add(new JLabel(Mensajes.PROBABILIDAD_CRUCE, JLabel.RIGHT));
		pIzd.add(tfProbCruce);

//Método Mutación
		final Mutation[] metMut = {new ExtraTestMutation(), new ReplaceMutation()};
		
		cbMut = new JComboBox<>(metMut);
		pIzd.add(cbMut);
		pIzd.add(new JLabel());

		ActionListener cmbMutacListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mutacion = (Mutation)cbMut.getSelectedItem();
			}
		};
		
		cbMut.addActionListener(cmbMutacListener);
			
//Prob Mutacion
		tfProbMut = new JTextField();
		tfProbMut.setText("0.08");
		pIzd.add(new JLabel(Mensajes.PROBABILIDAD_MUTACION, JLabel.RIGHT));
		pIzd.add(tfProbMut);
		
//Metodo Reemplazo
		final Replacement[] metReemp = {new DirectReplacement(), new ElitistReplacement()};
		
		cbReemp = new JComboBox<>(metReemp);
		pIzd.add(cbReemp);
		pIzd.add(new JLabel());

		ActionListener cmbReempListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				reemplazo = (Replacement)cbReemp.getSelectedItem();
				switch(reemplazo.getReplacement()) {
				case 1:
	                lPorcElit.setVisible(false);
	                tfPorcElit.setVisible(false);
	                break;
				case 2:
	                lPorcElit.setVisible(true);
	                tfPorcElit.setVisible(true);
	                break;
				default:
					System.err.println("Error cambiando de reemplazo");
				}
			}
		};
		
		cbReemp.setSelectedIndex(1);
		cbReemp.addActionListener(cmbReempListener);
		
//Porcentaje Elitismo
	    lPorcElit = new JLabel(Mensajes.PORCENTAJE_ELITISMO, JLabel.RIGHT);
	    tfPorcElit = new JTextField();
	    tfPorcElit.setText("0.02");
		pIzd.add(lPorcElit);
		pIzd.add(tfPorcElit);
 

		cbInic.setSelectedIndex(0);
		cbSelec.setSelectedIndex(3);
		cbCross.setSelectedIndex(0);
		cbMut.setSelectedIndex(0);
		cbReemp.setSelectedIndex(0);
		cmbInicListener.actionPerformed(null);
		cmbSelecListener.actionPerformed(null);
		cmbCrossListener.actionPerformed(null);
		cmbMutacListener.actionPerformed(null);
		cmbReempListener.actionPerformed(null);
		
		
		
//BOTONES
		bGenerateData = new JButton(Mensajes.GENERATE_DATA);
		pIzd.add(bGenerateData);

		ActionListener bGenerateDataListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				c.generateData(taFSM.getText(),20,100);
			}
		};
		bGenerateData.addActionListener(bGenerateDataListener);

		
		bCombinatory = new JButton(Mensajes.COMBINATORY);
		pIzd.add(bCombinatory);

		ActionListener bCombinatoryListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				c.combinatory(taTests.getText(),taMutants.getText(), tfMaxTests.getText().equals("") ? 10 : Integer.parseInt(tfMaxTests.getText()), Ventana.this);
			}
		};
		bCombinatory.addActionListener(bCombinatoryListener);
		

		bGreedy = new JButton(Mensajes.GREEDY);
		pIzd.add(bGreedy);

		ActionListener bGreedyListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				c.greedy(taTests.getText(),taMutants.getText(), tfMaxTests.getText().equals("") ? 10 : Integer.parseInt(tfMaxTests.getText()), Ventana.this);
			}
		};
		bGreedy.addActionListener(bGreedyListener);
		
		
		bPlay = new JButton(Mensajes.GENETIC_ALGORITHM);
		pIzd.add(bPlay);

		ActionListener bPlayListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int size = tfPob.getText().equals("") ?  100 : Integer.parseInt(tfPob.getText());
				int iters = tfIter.getText().equals("") ? 100 : Integer.parseInt(tfIter.getText());
				int max_tests = tfMaxTests.getText().equals("") ? 80 : Integer.parseInt(tfMaxTests.getText());
				int participantes = tfPartTorn.getText().equals("") ? 3 : Integer.parseInt(tfPartTorn.getText());
				double vict = tfVicTorn.getText().equals("") ? 0.8 : Double.parseDouble(tfVicTorn.getText());
				double trunc = tfPorcTrunc.getText().equals("") ? 0.125 : Double.parseDouble(tfPorcTrunc.getText());
				double prob_cruce = tfProbCruce.getText().equals("") ? 0.6 : Double.parseDouble(tfProbCruce.getText());
				double prob_mut = tfProbMut.getText().equals("") ? 0.08 : Double.parseDouble(tfProbMut.getText());
				double elit = tfPorcElit.getText().equals("") ? 0.02 : Double.parseDouble(tfPorcElit.getText());
				
	            if((hebra != null && !hebra.equals(null)) && hebra.isAlive())
	    			hebra.interrupt();
	            hebra = new Hebra(size, iters, max_tests,
						participantes, vict, trunc,
						prob_cruce,
						prob_mut,
						elit
						);
	            hebra.start();
			}
		};
		
		bPlay.addActionListener(bPlayListener);
		
	}

	private void setupDchSup() {

		pDch = new JPanel();
		pDch.setLayout(new BorderLayout());

		pGraph = new Plot2DPanel();
		pGraph.addLegend("EAST");
		pGraph.setAxisLabel(0, Mensajes.GENERACION);
		pGraph.setAxisLabel(1, Mensajes.FITNESS);
		
		pDch.add(pGraph, BorderLayout.CENTER);

		lFitness = new JLabel();
		lArbol = new JLabel();
		pResultado = new JPanel();
		pDch.add(pResultado, BorderLayout.SOUTH);
		
    }

	private void setupIzdInf() {
		pIzd.add(new JLabel("Specification file:", JLabel.RIGHT));
		taFSM = new JTextArea("files/FSM.txt");
		JScrollPane scrlFSM = new JScrollPane(taFSM);
		pIzd.add(scrlFSM);
		
		pIzd.add(new JLabel("Tests file:", JLabel.RIGHT));
		taTests = new JTextArea("files/Tests.txt");
		JScrollPane scrlTests = new JScrollPane(taTests);
		pIzd.add(scrlTests);
		
		pIzd.add(new JLabel("Mutants file:", JLabel.RIGHT));
		taMutants = new JTextArea("files/Mutants.txt");
		JScrollPane scrlMutants = new JScrollPane(taMutants);
		pIzd.add(scrlMutants);
	}

	
	public void actualizaGrafica(double[] generacion, double[] mejores, double[] peores, double[] medias, double[] absolutos) {

		pGraph.removeAllPlots();
		pGraph.addLinePlot("Absolute best", Color.GREEN, generacion, absolutos);
        pGraph.addLinePlot("Generational best", Color.BLUE, generacion, mejores);
        pGraph.addLinePlot("Average fitness", Color.LIGHT_GRAY, generacion, medias);
        pGraph.addLinePlot("Generational worse", Color.RED, generacion, peores);
        pGraph.revalidate();
        pDch.revalidate();
	}
	
	public void actualizaResultado(Chromosome c) {

		lFitness.setText("\t\t\t\t");
		lFitness.revalidate();
		lFitness.removeAll();
		lArbol.setText("\t\t\t\t");
		lArbol.revalidate();
		lArbol.removeAll();
		pResultado.removeAll();
		pResultado.revalidate();
		pDch.revalidate();
		
		pResultado = new JPanel();
		pResultado.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();

		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.fill = GridBagConstraints.HORIZONTAL;

		pResultado.add(new JLabel(Mensajes.MEJOR_GLOBAL, JLabel.TRAILING), constraints);
		lFitness = new JLabel( ((Double)c.getFitness()) .toString() );

		constraints = new GridBagConstraints();
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.gridwidth = 2;
		constraints.gridheight = 1;
		constraints.fill = GridBagConstraints.HORIZONTAL;

		pResultado.add(lFitness, constraints);
		
		constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.fill = GridBagConstraints.HORIZONTAL;

		pResultado.add(new JLabel(Mensajes.CODIFICACION), constraints);
		
		constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 2;
		constraints.gridheight = 1;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		
		lArbol = new JLabel(c.toString());
		pResultado.add(lArbol, constraints);
		lArbol.revalidate();
		lFitness.revalidate();
		pResultado.revalidate();
		pDch.add(pResultado, BorderLayout.SOUTH);
		pDch.revalidate();
	}
	
	private final class Hebra extends Thread {
		private int size;
		private int iters;
		private int max_tests;
		private int participantes;
		private double vict;
		private double trunc;
		private double prob_cruce;
		private double prob_mut;
		private double elit;

		public Hebra(int size, int iters, int max_tests, int participantes, double vict, double trunc,
				double prob_cruce, double prob_mut, double elit) {
			super();
			this.size = size;
			this.iters = iters;
			this.max_tests = max_tests;
			this.participantes = participantes;
			this.vict = vict;
			this.trunc = trunc;
			this.prob_cruce = prob_cruce;
			this.prob_mut = prob_mut;
			this.elit = elit;
		}
		
		public void run() {
			try {
				Thread.sleep(1);
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						c.run(size, iters, inicializacion, max_tests,
								seleccion, participantes, vict, trunc,
								cruce, prob_cruce,
								mutacion, prob_mut,
								taTests.getText(), taMutants.getText(),
								reemplazo, elit,
								Ventana.this);
					}
				});
			} catch (InterruptedException e) {
				System.err.println("matando hebra en ventana");
			}
		}
	}
}