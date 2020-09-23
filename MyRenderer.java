package edu.fortLee.finalProject.swing;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class MyRenderer extends DefaultTableCellRenderer  
{ 
    public Component getTableCellRendererComponent(JTable table, Object value, boolean   isSelected, boolean hasFocus, int row, int column) 
	{ 
	    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column); 
	
	    setBorder(BorderFactory.createEmptyBorder(5,5,5,5)); //padding
	    //if (! table.isRowSelected(row))
	    //{
	    	if(column == 0 || column == 2) {
	    	    c.setBackground(Color.LIGHT_GRAY); 
	    	} else {
	    	    c.setBackground(Color.WHITE);
	    	}
	    //}
	  
	    return c; 
	} 

} 
