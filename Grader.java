package edu.fortLee.finalProject.swing;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.DefaultTableModel;

public class Grader {
	private double grade; 

	
	private List<String> readFile(String filePath)
	{
		List<String> lines = null;
		try {
			lines = Files.readAllLines(Paths.get(filePath));
			// for(String line: lines){ 
				// System.out.println(line);
			// }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lines;
	}

	public void calculateGrade(ArrayList<String> javaFiles, DefaultTableModel tableModel) 
	{
		
		for (String fileName: javaFiles)// loop through files
		{
			List<String> lines = readFile(fileName); 
			for(String line: lines)//loop through lines
			{
				for (int i = 0; i < tableModel.getRowCount(); i++) // loop through keywords 
				{
					if (Integer.parseInt(tableModel.getValueAt(i, 1).toString()) > 0) //keyword required
					{
						String keyword = tableModel.getValueAt(i, 0).toString(); 
						if(line.indexOf(keyword + " ") > 0 || line.indexOf(keyword + "(") > 0) //found keyword
						{
							int newOccurence = Integer.parseInt(tableModel.getValueAt(i, 2).toString()) + 1;
							tableModel.setValueAt(newOccurence, i, 2);
						}
					}
					else
						tableModel.setValueAt("", i, 2);
				}	
			}	
		}
	
		//calculate final grade
		double totalNeeded = 0; 
		int totalFound = 0;
		for (int i = 0; i < tableModel.getRowCount(); i++) // loop through keywords 
		{
			if (Integer.parseInt(tableModel.getValueAt(i, 1).toString()) > 0) //keyword required
			{
				int needed = Integer.parseInt(tableModel.getValueAt(i, 1).toString());
				totalNeeded += needed;
				
				int found = Integer.parseInt(tableModel.getValueAt(i, 2).toString());
				if (found > needed)
					totalFound += needed;
				else
					totalFound += found;
			}
		}	
		grade =(double)totalFound/(double)totalNeeded * 100; 
		if(grade > 100)
			grade = 100.0; 
		
	}

	public double getGrade() 
	{
		return grade;
	}	 
    
}
