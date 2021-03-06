/*
    StockD fetches EOD stock market data from Offical Stock exchange sites
    Copyright (C) 2020  Viresh Gupta

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along
    with this program; if not, write to the Free Software Foundation, Inc.,
    51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package main;

import java.io.File;
import java.io.IOException;

import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.sql2o.Sql2oException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.ConfigurationWrapper;
import parsers.ParseEQ;
import parsers.ParseFO;
import parsers.ParseIndices;

public class FxApp extends Application {
	public static Logger logger;

	@Override
	public void start(Stage primaryStage) {
		Parent root = null;
		try {
			root = FXMLLoader.load(getClass().getResource("/MainWindow.fxml"));
		} catch (IOException e) {
			logger.log(Level.FINER, e.getMessage(), e);
			System.out.println(e);
			e.printStackTrace();
		}
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("StockD - Stock Data Downloader");
        primaryStage.show();
	}
	
    public static void main(String[] args) {
    	FileHandler fh = null;
    	try {
	    	//// Setup logging facilities
    		logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	    	fh = new FileHandler(System.getProperty("user.dir") + "/app.log", true);
	    	SimpleFormatter formatter = new SimpleFormatter();
	    	fh.setFormatter(formatter);
	    	logger.addHandler(fh);
	    	
	    	//// Setup temporary directory
	    	File dir = new File(System.getProperty("user.dir")+"/Temp");
	    	if(!dir.exists()) {
	    		dir.mkdir();
	    	}
	    	else {
				File[] tempFiles = dir.listFiles();
				for (int i = 0; i < tempFiles.length; i++) {
					tempFiles[i].delete();
				}
	    	}
	    	
	    	//// Setup Configuration
	    	ConfigurationWrapper configs = ConfigurationWrapper.getInstance();
	    	
	    	if(args.length == 0) {
	    		launch();    		
	    	}
	    	else {
//	    		ParseEQ p = new ParseEQ(System.getProperty("user.dir") + "/Equity/");
//	    		ParseFO p = new ParseFO(System.getProperty("user.dir") + "/Futures/");
//	    		ParseIndices p = new ParseIndices("./Indices/");
//	    		p.parse("/home/viresh/Desktop/test_nse/ind_close_all_18062020.csv");
	    		for(int i=0; i<args.length; i++) {
	    			if(args[i].equals("-h") || args[i].equals("--help")) {
	    				System.out.println("Usage: ./StockD <From Date: DD/MM/YYYY> <To Date: DD/MM/YYYY>");
	    				System.out.println("All configurations must be set from GUI.");
	    			}
	    		}
	    		System.out.println("Console Mode to be implemented!");
	    		System.exit(0);
	    	}
    	}
    	catch(Sql2oException ex) {
    		if(ex.getMessage().contains("Could not acquire a connection from DataSource")) {
    			logger.log(Level.SEVERE, "Failed to connect to settings store. Probably another instance is already running");
    			logger.log(Level.SEVERE, ex.getMessage(), ex);
    		}
    		ex.printStackTrace();

    		System.exit(0);
    	}
    	catch(Exception ex) {
    		System.out.println("Fatal Error:");
    		System.out.println(ex.getMessage());
    		ex.printStackTrace();
    		System.exit(0);
    	}
    	finally {
    		if(fh != null) {
    			fh.close();    			
    		}
		}
    }
}
