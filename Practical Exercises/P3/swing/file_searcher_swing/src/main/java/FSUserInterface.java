package edu.curtin.comp3003.filesearcher;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * A Swing-based user interface for the file searcher.
 */
public class FSUserInterface
{
    private JFrame window = null;
    private JLabel tally = null;

    // A list-like container used to keep track of search results.
    private DefaultListModel<String> searchResults = null;
    
    public FSUserInterface() {}
    
    public void show()
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override 
            public void run()
            {            
                buildInterface();
            }
        });
    }
    
    private void buildInterface()
    {
        window = new JFrame("File Searcher");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                
        JPanel searchPanel = new JPanel(new FlowLayout());        
        final JTextField searchPathBox = new JTextField("/usr", 20);
        final JTextField searchTermBox = new JTextField(20);
        JButton searchButton = new JButton("Search");
        
        searchPanel.add(new JLabel("Path:"));
        searchPanel.add(searchPathBox);
        searchPanel.add(new JLabel("Search text:"));
        searchPanel.add(searchTermBox);
        searchPanel.add(searchButton);
        
        // When the "Search" button is pressed...
        searchButton.addActionListener(new ActionListener()
        {
            @Override 
            public void actionPerformed(ActionEvent e)
            {
                FSFileFinder finder = new FSFileFinder(
                    searchPathBox.getText(), 
                    searchTermBox.getText(), 
                    FSUserInterface.this);
                    
                finder.search();
            }
        });
        
        searchResults = new DefaultListModel<>();        
        JScrollPane resultsList = new JScrollPane(new JList<String>(searchResults));
        
        JPanel auxPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        tally = new JLabel();
        JButton clearButton = new JButton("Clear results");
        auxPanel.add(tally);
        auxPanel.add(clearButton);
        
        // When the "Clear results" button is pressed...
        clearButton.addActionListener(new ActionListener()
        {   
            @Override 
            public void actionPerformed(ActionEvent e)
            {
                searchResults.clear();
                tally.setText("");
            }
        });
        
        Container contentPane = window.getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(searchPanel, BorderLayout.NORTH);
        contentPane.add(resultsList, BorderLayout.CENTER);   
        contentPane.add(auxPanel, BorderLayout.SOUTH);
        window.pack();
        window.setVisible(true);
    }
    
    public void addResult(String result)
    {
        searchResults.addElement(result);
        tally.setText(Integer.toString(searchResults.getSize()) + " result(s) found");
    }
    
    public void showError(String message)
    {
        JOptionPane.showMessageDialog(window, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
