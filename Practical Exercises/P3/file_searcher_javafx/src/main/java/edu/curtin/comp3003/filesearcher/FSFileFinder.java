package edu.curtin.comp3003.filesearcher;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class FSFileFinder
{
    private String searchPath;
    private String searchTerm;
    private FSUserInterface ui;

    public FSFileFinder(String searchPath, String searchTerm, FSUserInterface ui)
    {
        this.searchPath = searchPath;
        this.searchTerm = searchTerm;
        this.ui = ui;
    }
    
    public void search()
    {
        try
        {
            // Recurse through the directory tree
            Files.walkFileTree(Paths.get(searchPath), new SimpleFileVisitor<Path>()
            {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                {
                    // Check whether each file contains the search term, and if
                    // so, add it to the list.
                    String fileStr = file.toString();
                    if(fileStr.contains(searchTerm))
                    {
                        ui.addResult(fileStr);
                    }
                    
                    return FileVisitResult.CONTINUE;
                }
            });
        }
        catch(IOException e)
        {
            // This error handling is a bit quick-and-dirty, but it will suffice here.
            ui.showError(e.getClass().getName() + ": " + e.getMessage());
        }
    }
}
