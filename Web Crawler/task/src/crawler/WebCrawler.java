package crawler;
import java.lang.Object;
import javax.swing.*;
import java.net.URI;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;


public class WebCrawler extends JFrame {
    DefaultTableModel model = new DefaultTableModel(new Object[][]{}, new String[]{"URL", "Title"});
    JTable linkTable = new JTable(model);
    JScrollPane scrollPane = new JScrollPane(linkTable);
    JTextField textField = new JTextField("https://wikipedia.org");
    JToggleButton runButton = new JToggleButton("Run");
    JPanel panel = new JPanel();
    JLabel titleLabel = new JLabel("Title:");
    JLabel htmlLabel = new JLabel("URL:");
    //JLabel htmlTitel = new JLabel("");
    JLabel workerLabel = new JLabel("Workers:");
    JTextField workerField = new JTextField("");
    JLabel depthLabel = new JLabel("Depth:");
    JTextField depthField = new JTextField("0");
    JCheckBox depthCheckBox = new JCheckBox("Enabled");
    JLabel timelimitLabel = new JLabel("Time limit:");
    JTextField timelimitField = new JTextField("0");
    JCheckBox timelimitCheckBox = new JCheckBox("Enabled");
    JLabel elapsedTimeLabel = new JLabel("Elapsed time:");
    JLabel elapsedTime = new JLabel("");
    JLabel parsedPagesLabel = new JLabel("Parsed pages:");
    int parsedPagesCount = 0;
    JLabel parsedPages = new JLabel("");
    JLabel exportLabel = new JLabel("Export:");
    JTextField exportField = new JTextField("d:\\test\\last.txt");
    JButton exportButton = new JButton("Save");
    List<String> links = new ArrayList<>();

    public WebCrawler() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 1000);
        setTitle("Web Crawler");
        setLayout(new BorderLayout());
        panel.setLayout(new GridBagLayout());
        linkTable.setName("TitlesTable");
        textField.setName("UrlTextField");
        runButton.setName("RunButton");
//        htmlTitel.setName("TitleLabel");
        exportField.setName("ExportUrlTextField");
        exportButton.setName("ExportButton");
        depthField.setName("DepthTextField");
        depthCheckBox.setName("DepthCheckBox");
        parsedPages.setName("ParsedLabel");
        JPanel exportPanel = new JPanel();

        //linkTable.
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.2;
        gbc.anchor = GridBagConstraints.LINE_START;
        panel.add(htmlLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.6;
        panel.add(textField, gbc);
        textField.setColumns(35);

        gbc.gridx = 2;
        gbc.weightx = 0.2;
        panel.add(runButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.2;
        panel.add(workerLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.8;
        gbc.anchor = GridBagConstraints.LINE_START;
        panel.add(workerField, gbc);
        workerField.setColumns(4);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.2;
        panel.add(depthLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.6;
        panel.add(depthField, gbc);
        depthField.setColumns(4);

        gbc.gridx = 2;
        gbc.weightx = 0.2;
        gbc.anchor = GridBagConstraints.LINE_START;
        panel.add(depthCheckBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.2;
        panel.add(timelimitLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.6;
        gbc.anchor = GridBagConstraints.LINE_START;
        panel.add(timelimitField, gbc);
        timelimitField.setColumns(4);

        gbc.gridx = 2;
        gbc.weightx = 0.2;
        gbc.anchor = GridBagConstraints.LINE_START;
        panel.add(timelimitCheckBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.2;
        panel.add(elapsedTimeLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.8;
        panel.add(elapsedTime, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0.2;
        panel.add(parsedPagesLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.8;
        panel.add(parsedPages, gbc);

        linkTable.setEnabled(false);
        add(panel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        exportPanel.setLayout(new GridBagLayout());
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.2;
        exportPanel.add(exportLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.6;
        exportPanel.add(exportField, gbc);
        exportField.setColumns(40);

        gbc.gridx = 2;
        gbc.weightx = 0.2;
        exportPanel.add(exportButton, gbc);
        add(exportPanel, BorderLayout.SOUTH);

        setVisible(true);

        runButton.addActionListener(e -> {
            model.setRowCount(0);
            CrawlWorker worker = new CrawlWorker(textField.getText(), this);
            worker.execute();
/*
            String txt = getTextFromUrl(textField.getText());
            //htmlTitel.setText(extractTitle(txt));
            model.addRow(new Object[]{textField.getText(), extractTitle(txt)});
            String[] links = txt.split("<a href=\"|<a href='");

            for (int i = 1; i < links.length; i++) {
                String link = links[i].split("\"|'")[0];

                if (isRelativeLink(link)) {
                    link = toAbsoluteLink(textField.getText(), link);
                }

                String title = extractTitle(getTextFromUrl(link));
                if(!title.isEmpty()) {
                    model.addRow(new Object[]{link, title});
                }
            }

 */
            linkTable.setEnabled(true);
        });
        exportButton.addActionListener(e -> {
            saveLinksToFile();
            saveLinksToFile2();
/*            try (FileWriter writer = new FileWriter(exportField.getText())) {
                for (int i = 0; i < model.getRowCount(); i++) {
                    String url = (String) model.getValueAt(i, 0);
                    String title = (String) model.getValueAt(i, 1);
                    writer.write(url + System.lineSeparator());
                    writer.write(title + System.lineSeparator());
                }
                //JOptionPane.showMessageDialog(this, "Export Successful");
            } catch (IOException ioException) {
                JOptionPane.showMessageDialog(this, "Error during export: " + ioException.getMessage());
            }

 */
        });
    }

    void saveLinksToFile() {
        try (FileWriter writer = new FileWriter(exportField.getText())) {
            for (int i = 0; i < model.getRowCount(); i++) {
                String url = (String) model.getValueAt(i, 0);
                String title = (String) model.getValueAt(i, 1);
                writer.write(url + System.lineSeparator());
                writer.write(title + System.lineSeparator());
            }
            //JOptionPane.showMessageDialog(this, "Export Successful");
        } catch (IOException ioException) {
            JOptionPane.showMessageDialog(this, "Error during export: " + ioException.getMessage());
        }catch(Exception e){}
    }

    void saveLinksToFile2() {
        try (FileWriter writer = new FileWriter(exportField.getText() + ".txt")) {
            for (int i = 0; i < model.getRowCount(); i++) {
                String url = (String) model.getValueAt(i, 0);
                String title = (String) model.getValueAt(i, 1);
                writer.write(url + " " + title + System.lineSeparator());
                writer.write(title + System.lineSeparator());
            }
            //JOptionPane.showMessageDialog(this, "Export Successful");
        } catch (IOException ioException) {
            JOptionPane.showMessageDialog(this, "Error during export: " + ioException.getMessage());
        }catch(Exception e){}
    }

    synchronized void  addRow(Object[] row) {
        model.addRow(row);
    }

    synchronized void setElapsedTime(String time) {
        elapsedTime.setText(time);
    }

    synchronized void increasePages() {
        parsedPagesCount++;
        parsedPages.setText(String.format("%d", parsedPagesCount));
    }

    synchronized void resetPages() {
        parsedPagesCount = -1;
        parsedPages.setText("0");
        links.clear();
    }

    synchronized boolean checkDoubleLink(String url) {
        boolean result = false;

        if(!links.contains(url.toLowerCase())) {
            links.add(url.toLowerCase());
            System.out.println(url.toLowerCase());
            saveLinksListToFile();
            result = true;
        }

        return result;
    }

    synchronized boolean checkLevel(int level) {
        boolean result = false;
        if(!depthCheckBox.isSelected()) {return true;}

        try {
            if (level < Integer.parseInt(depthField.getText())) {
                result = true;
            }
        }catch(Exception e){
            if(level == 0){
                result = true;
            }
        }
        return result;
    }


    void saveLinksListToFile() {
        try (FileWriter fileWriter = new FileWriter("d:\\test\\links.txt")) {
            for (String link : links) {
                fileWriter.write(link + System.lineSeparator());
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error during saving links: " + e.getMessage());
        }
    }
}

