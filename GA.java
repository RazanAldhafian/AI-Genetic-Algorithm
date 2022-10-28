package com.zetcode;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;


import java.io.*;
import java.util.Scanner;
import java.lang.Math;
import java.util.Arrays;
import java.util.Random;


public class GA extends JFrame {

	//------------------------------------------------------------------------

	public GA() {

		initUI();
	}

	private void initUI() {

		XYDataset dataset = createDataset();
		JFreeChart chart = createChart(dataset);

		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		chartPanel.setBackground(Color.white);
		add(chartPanel);

		pack();
		setTitle("GA chart");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private XYDataset createDataset() {

		var series = new XYSeries("");

		for(int i =0; i<Sumcounter ; i++){
			series.add(i, sumFF[i]);
		}


		var dataset = new XYSeriesCollection();
		dataset.addSeries(series);

		return dataset;
	}

	private JFreeChart createChart(XYDataset dataset) {

		JFreeChart chart = ChartFactory.createXYLineChart(
				"GA Preformance",
				"Genaration",
				"Fitness",
				dataset,
				PlotOrientation.VERTICAL,
				true,
				true,
				false
				);

		XYPlot plot = chart.getXYPlot();

		var renderer = new XYLineAndShapeRenderer();
		renderer.setSeriesPaint(0, Color.RED);
		renderer.setSeriesStroke(0, new BasicStroke(2.0f));

		plot.setRenderer(renderer);
		plot.setBackgroundPaint(Color.white);

		plot.setRangeGridlinesVisible(true);
		plot.setRangeGridlinePaint(Color.BLACK);

		plot.setDomainGridlinesVisible(true);
		plot.setDomainGridlinePaint(Color.BLACK);

		chart.getLegend().setFrame(BlockBorder.NONE);

		chart.setTitle(new TextTitle("GA Preformance",
				new Font("Serif", java.awt.Font.BOLD, 18)
				)
				);

		return chart;
	}


	//---------------------------------end---------------------------------------


	static Scanner input = new Scanner(System.in);
	static Random random = new Random();
	static double[] sumFF = new double[50000];
	static int Sumcounter=0;


	public static void main(String[] args) {

		System.out.println(" \n|| WELCOME TO CARGO COMPANY ||\n ");
		System.out.println("Optimizing your container-loading plan will take 2 simple Questions! \n");
		System.out.println("------------------------------------------- \n");

		System.out.print("Please Enter The Number Of Containers: ");
		int c = input.nextInt();

		System.out.print("Please Enter The Number Of Items: ");
		int n = input.nextInt();

		double[] items = new double[n];

		System.out.print(
				"\nTo Set The Weights Of The Items, Please Enter 1 for Option A(weight=i/2) Or 2 For Option B(weight=i^2/2): ");
		int option = input.nextInt();

		// assign weights for items.
		while (true) {
			if (option == 1 || option == 2) {
				int j = 0;
				for (double i = 1; i <= n; i++) {
					if (option == 1)
						items[j++] = i / 2;
					else
						items[j++] = i * i / 2;
				}
				break;
			}

			else {
				System.out.print(
						"\nInvalid input! Please Enter 1 for Option A(weight=i/2) Or 2 For Option B(weight=i^2/2): ");
				option = input.nextInt();
			}
		}

		// number of all possible solutions (number of containers^number of items).
		double APS = Math.pow(c, n);
		System.out.println("\n------------------------------------ ");
		System.out.println("\nNumber of all possible solutions = " + APS);
		System.out.println("");

		/*
		 * creating an array of chormosomes (population of random size), each chromosome
		 * consists of genes,
		 * each gene represents the number of container the item is assigned to.
		 */
		System.out.println("Enter populationSize:");
		int populationSize = input.nextInt();

		int[][] population = new int[populationSize][n];

		for (int i = 0; i < populationSize; i++) {// assign items to containers randomly.
			int k;
			for (int j = 0; j < n; j++) {
				k = random.nextInt(c);
				population[i][j] = k;
			}
		}

		double[] contsWeights = new double[c]; // array of container's weights.
		double[] fitRanked = new double[populationSize]; /*
		 * array of fitness values for each chromosome,
		 * ranked from lowest(best) to worst.
		 */

		for (int ch = 0; ch < populationSize; ch++) {// population loop.

			for (int cont = 0; cont < c; cont++) {/*
			 * containers loop to calculate total weights and assign
			 * each total in weights array.
			 */
				double totalWeight = 0.0;
				for (int it = 0; it < n; it++) {// items loop to calculate total weight of a container
					if (population[ch][it] == cont)
						totalWeight = totalWeight + items[it];
				} // end items loop

				contsWeights[cont] = totalWeight;
			} // end containers loop

			// fitness function
			// first we will sort the total weights in increasing order so we can then take
			// the difference.
			double temp = 0.0;
			for (int i = 0; i < contsWeights.length; i++) {
				for (int j = i + 1; j < contsWeights.length; j++) {
					if (contsWeights[i] > contsWeights[j]) {
						temp = contsWeights[i];
						contsWeights[i] = contsWeights[j];
						contsWeights[j] = temp;
					}
				}
			}
			// take the difference from largest to smallest.
			double fitVal = contsWeights[c - 1] - contsWeights[0];
			fitRanked[ch] = fitVal;

		} // end population loop

		// rank the chromosomes in the population from lowest(best) to highest(worst)
		// fitness value.
		double temp1 = 0;
		int[] temp2;
		for (int i = 0; i < fitRanked.length; i++) {
			for (int j = i + 1; j < fitRanked.length; j++) {
				if (fitRanked[i] > fitRanked[j]) {
					temp1 = fitRanked[i];
					fitRanked[i] = fitRanked[j];
					fitRanked[j] = temp1;

					temp2 = population[i];
					population[i] = population[j];
					population[j] = temp2;
				}
			}
		}

		System.out.println("------------------------------------------- \n\n");
		System.out.println("Number Of Containers: " + c + ", Number Of Items: " + n);
		System.out.println("\nThe Item's Weight Are:  ");
		for (int i = 0; i < n; i++) {
			System.out.println("Item " + (i + 1) + ": " + items[i] + "kg, ");
		}
		System.out.println("\n\n    || Population's Chromosomes Ranked From Best Fitness Value To Worst: ||    \n");
		for (int i = 0; i < populationSize; i++) {
			System.out.println("");
			System.out.print("Chromosome " + (i + 1) + ": ");
			for (int j = 0; j < n; j++)
				System.out.print(population[i][j] + 1);

			System.out.println(", Fitness Value = " + fitRanked[i] + ", Item's Distribution: ");

			for (int cont = 0; cont < c; cont++) {
				System.out.print("(Container " + (cont + 1) + ": ");
				for (int it = 0; it < n; it++) {
					if (population[i][it] == cont)
						System.out.print((items[it]) + "kg, ");
				}
				System.out.print(") ");
			}
			System.out.println("\n----------------------");
		}

		// PHASE 2
		int fitEval = 0;
		int state = 0;

		System.out.println("Enter mutation operator:");
		int k = input.nextInt();


		do {
			// Selection
			int a = random.nextInt(populationSize);
			int b = random.nextInt(populationSize);
			int[] selecteda;
			int[] selectedb;

			if (fitRanked[a] < fitRanked[b])
				selecteda = population[a];
			else
				selecteda = population[b];

			a = random.nextInt(populationSize);
			b = random.nextInt(populationSize);

			if (fitRanked[a] < fitRanked[b])
				selectedb = population[a];
			else
				selectedb = population[b];

			// Single-Point Crossover 

			int length = selecteda.length;
			int[] e = new int[length];
			int[] f = new int[length];

			int min = 1;
			int max = length - 1;

			int crossover = (int) (Math.random() * (max - min) + min);

			// Compute children

			for (int i = 0; i < crossover; i++) {

				e[i] = selecteda[i];
				f[i] = selectedb[i];

				// Statement that the CROSSOVER is running perfectly
				if (state == 0) {
					System.out.println("\n\n\n----- || Single-Point Crossover || -----");
					System.out.println("    Single-Point Crossover PROCESSING .. \n\n");
					state++;
				}

			}

			for (int j = crossover; j < length; j++) {
				e[j] = selectedb[j];
				f[j] = selecteda[j];
			}

			if (fitEval == 200) { //example

				System.out.println("-------------------- AN EXAMPLE OF THE PROCESS ----------------------- \n");
				System.out.println("\n----- || Single-Point Crossover || -----\n");
				System.out.println("The Crossover Point Is: " + crossover);
				System.out.println("Parent 1: " + Arrays.toString(selecteda));
				System.out.println("Parent 2: " + Arrays.toString(selectedb) + "\n");

				System.out.println("First Child: " + Arrays.toString(e));
				System.out.println("Second Child: " + Arrays.toString(f));
				System.out.println("\n       ------------------- \n");
			}

			// Multi Gene Mutation

			// k is num of times replacing the gene (1,5)

			Random ran = new Random();
			int[] mu1 = e;
			int[] mu2 = f;

			int r = 0;
			int j = 0;

			if (fitEval == 200) {//example

				System.out.println("\n----- || Multi-Gene Mutation || -----\n");
				System.out.println("Parent 1: " + Arrays.toString(e));
				System.out.println("Parent 2: " + Arrays.toString(f) + "\n");

			}

			for (int i = 0; i < k; i++) {
				r = ran.nextInt(length);
				j = ran.nextInt(length);
				mu1[r] = ran.nextInt(length);
				mu2[j] = ran.nextInt(length);

				// Statement that the MUTATION is running perfectly
				if (state == 1) {
					System.out.println("----- || Multi-Gene Mutation || -----");
					System.out.println("    Multi-Gene Mutation PROCESSING .. \n\n");
					System.out.println("----- ||  Weakest Replacement || -----");
					System.out.println("    Weakest Replacement PROCESSING .. \n\n");
					System.out.println("First Child: " + Arrays.toString(mu1));
					System.out.println("Second Child: " + Arrays.toString(mu2));
					System.out.println("\n       ------------------- \n");
					state++;
				}

			}






			// replacemnet


			double[] WeightsMu1 = new double[c];
			for (int cont = 0; cont < c; cont++) {
				double WeightMu1 = 0.0;
				for (int it = 0; it < n; it++) {
					if (mu1[it] == cont)
						WeightMu1 = WeightMu1 + items[it];
				}
				WeightsMu1[cont] = WeightMu1;
			}

			double tempMu1 = 0.0;
			for (int i = 0; i < WeightsMu1.length; i++) {
				for (int p = i + 1; p < WeightsMu1.length; p++) {
					if (WeightsMu1[i] > WeightsMu1[p]) {
						tempMu1 = WeightsMu1[i];
						WeightsMu1[i] = WeightsMu1[p];
						WeightsMu1[p] = tempMu1;
					}
				}
			}
			double fitValMu1 = WeightsMu1[c - 1] - WeightsMu1[0]; // fitness value for mu1


			double[] WeightsMu2 = new double[c];
			for (int cont = 0; cont < c; cont++) {
				double WeightMu2 = 0.0;
				for (int it = 0; it < n; it++) {
					if (mu2[it] == cont)
						WeightMu2 = WeightMu2 + items[it];
				}
				WeightsMu2[cont] = WeightMu2;
			}

			double tempMu2 = 0.0;
			for (int i = 0; i < WeightsMu2.length; i++) {
				for (int p = i + 1; p < WeightsMu2.length; p++) {
					if (WeightsMu2[i] > WeightsMu2[p]) {
						tempMu2 = WeightsMu2[i];
						WeightsMu2[i] = WeightsMu2[p];
						WeightsMu2[p] = tempMu2;
					}
				}
			}
			double fitValMu2 = WeightsMu2[c - 1] - WeightsMu2[0]; // fitness value for mu2

			if (fitEval == 200) {System.out.println("----- || Weakest Replacement  || -----\n"); }//example

			// compare first child
			if (fitValMu1 < fitRanked[fitRanked.length - 1]) {
				double tempp1 = 0.0;
				int[] tempp2;

				tempp1 = fitRanked[fitRanked.length - 1];
				fitRanked[fitRanked.length - 1] = fitValMu1;
				tempp2 = population[population.length - 1];
				population[population.length - 1] = mu1;
				//example
				if (fitEval == 200) {System.out.println("child 1 " + Arrays.toString(mu1) + " has replaced with " + Arrays.toString(tempp2));} 
			} else
			{if (fitEval == 200) {//example
				System.out.println("child 1 has not replaced"); }}

			double tempFit1 = 0;
			int[] tempPop1;
			for (int i = 0; i < fitRanked.length; i++) {
				for (int p = i + 1; p < fitRanked.length; p++) {
					if (fitRanked[i] > fitRanked[p]) {
						tempFit1 = fitRanked[i];
						fitRanked[i] = fitRanked[p];
						fitRanked[p] = tempFit1;

						tempPop1 = population[i];
						population[i] = population[p];
						population[p] = tempPop1;
					}
				}
			}


			// compare second child
			if (fitValMu2 < fitRanked[fitRanked.length - 1] || fitValMu2 < fitRanked[fitRanked.length - 2]) {
				Boolean FV1 = false;
				Boolean FV2 = false;
				double tempp1 = 0.0;
				int[] tempp2;

				if (fitValMu2 < fitRanked[fitRanked.length - 1]) {
					FV1 = true;
				} else if (fitValMu2 < fitRanked[fitRanked.length - 2]) {
					FV2 = true;
				}

				if (FV1) {
					tempp1 = fitRanked[fitRanked.length - 1];
					fitRanked[fitRanked.length - 1] = fitValMu2;
					tempp2 = population[population.length - 1];
					population[population.length - 1] = mu2;
					//example
					if (fitEval == 200) {
						System.out.println("child 2 " + Arrays.toString(mu2) + " has replaced with " + Arrays.toString(tempp2));
						System.out.println("\n       ------------------- ");}
				}

				if (FV2) {
					tempp1 = fitRanked[fitRanked.length - 2];
					fitRanked[fitRanked.length - 2] = fitValMu2;
					tempp2 = population[population.length - 2];
					population[population.length - 2] = mu2;
					//example
					if (fitEval == 200) {
						System.out.println("child 2 " + Arrays.toString(mu2) + " has replaced with " + Arrays.toString(tempp2));
						System.out.println("\n       -------------------  "); }
				}

			} else {
				if (fitEval == 200) {//example
					System.out.println("child 2 has not replaced");
					System.out.println("\n       ------------------- ");}
			}



			double tempFit2 = 0;
			int[] tempPo2p;
			for (int i = 0; i < fitRanked.length; i++) {
				for (int p = i + 1; p < fitRanked.length; p++) {
					if (fitRanked[i] > fitRanked[p]) {
						tempFit2 = fitRanked[i];
						fitRanked[i] = fitRanked[p];
						fitRanked[p] = tempFit2;

						tempPo2p = population[i];
						population[i] = population[p];
						population[p] = tempPo2p;
					}
				}
			}




			fitEval++;


			sumFF[Sumcounter]=fitRanked[0];
			Sumcounter++;


		} while (fitEval != 10000); // termination

		System.out.println("\nSingle-Point Crossover Accomplished SUCCESSFULLY =) !");
		System.out.println("Multi-Gene Mutation Accomplished SUCCESSFULLY  =) !");
		System.out.println("Weakest Replacement Accomplished SUCCESSFULLY  =) !");
		System.out.println("\n       ------------------- \n");

		System.out.println("\n\n    || Population's Chromosomes Ranked From Best Fitness Value To Worst after evaluations: ||    \n");
		for (int i = 0; i < populationSize; i++) {
			System.out.println("");
			System.out.print("Chromosome " + (i + 1) + ": ");
			for (int j = 0; j < n; j++)
				System.out.print(population[i][j] + 1);

			System.out.println(", Fitness Value = " + fitRanked[i] + ", Item's Distribution: ");

			for (int cont = 0; cont < c; cont++) {
				System.out.print("(Container " + (cont + 1) + ": ");
				for (int it = 0; it < n; it++) {
					if (population[i][it] == cont)
						System.out.print((items[it]) + "kg, ");
				}
				System.out.print(") ");
			}
			System.out.println("\n----------------------");
		}



		//------------------------------------------------------------------------

		EventQueue.invokeLater(() -> {

			var ex = new GA();
			ex.setVisible(true);
		});
		//---------------------------------end---------------------------------------

	}//Main




}//class