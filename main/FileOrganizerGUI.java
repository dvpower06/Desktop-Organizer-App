package main;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Paths;

public class FileOrganizerGUI extends JFrame {

    private JTextArea logArea;
    private JButton organizeButton;
    private PrintStream printStream;

    public FileOrganizerGUI() {
        // Set up the main window
        setTitle("File Organizer");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create layout
        setLayout(new BorderLayout());

        // Initialize text area for logs
        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(logArea);
        add(scrollPane, BorderLayout.CENTER);

        // Initialize button
        organizeButton = new JButton("Organize Files");
        organizeButton.setFont(new Font("Arial", Font.BOLD, 14));
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(organizeButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Redirect console output to text area
        printStream = new PrintStream(new TextAreaOutputStream(logArea));
        System.setOut(printStream);
        System.setErr(printStream);

        // Add button action listener
        organizeButton.addActionListener(_ -> organizeFiles());

        // Display source directory
        logArea.append("Source Directory: " + Paths.get(System.getProperty("user.home"), "Desktop").toString() + "\n");
        logArea.append("Ready to organize files. Click the button to start.\n");
    }

    private void organizeFiles() {
        organizeButton.setEnabled(false);
        logArea.append("\nStarting file organization...\n");
        
        // Run organization in a separate thread to keep UI responsive
        new Thread(() -> {
            try {
                Main.main(null); // Call the original Main class's main method
                SwingUtilities.invokeLater(() -> {
                    logArea.append("File organization completed.\n");
                    organizeButton.setEnabled(true);
                });
            } catch (IOException ex) {
                SwingUtilities.invokeLater(() -> {
                    logArea.append("Error during organization: " + ex.getMessage() + "\n");
                    organizeButton.setEnabled(true);
                });
            }
        }).start();
    }

    // Custom OutputStream to redirect console output to text area
    private static class TextAreaOutputStream extends OutputStream {
        private JTextArea textArea;

        public TextAreaOutputStream(JTextArea textArea) {
            this.textArea = textArea;
        }

        @Override
        public void write(int b) {
            // Append character to text area
            SwingUtilities.invokeLater(() -> {
                textArea.append(String.valueOf((char) b));
                textArea.setCaretPosition(textArea.getDocument().getLength());
            });
        }
    }

    public static void main(String[] args) {
        // Run GUI on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            new FileOrganizerGUI().setVisible(true);
        });
    }
}