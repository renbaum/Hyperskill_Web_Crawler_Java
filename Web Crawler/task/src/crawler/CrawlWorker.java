package crawler;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Time;
import java.time.LocalTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class CrawlWorker extends SwingWorker<Void, String[]> {

    private String startUrl;
    private WebCrawler gui = null;
    private ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
    int openThreads = 0;

    public CrawlWorker(String startUrl, WebCrawler gui) {
        this.startUrl = startUrl;
        this.gui = gui;
    }

    synchronized void increaseOpenThreads(){
        openThreads++;
    }
    synchronized void decreaseOpenThreads(){
        openThreads--;
    }

    @Override
    protected Void doInBackground() throws Exception {
        gui.runButton.setText("Stop");
        gui.resetPages();
        gui.elapsedTime.setText("0");
        openThreads = 0;

        addTask(new DoLinks(0, startUrl, gui, this));

        while(openThreads > 0) {
            Thread.sleep(100);
            gui.setElapsedTime(String.format("%d", openThreads));
        }

        //executor.shutdown();
//        if(!executor.awaitTermination(Long.MAX_VALUE, java.util.concurrent.TimeUnit.NANOSECONDS)) {
//            executor.shutdownNow();
//        }
        gui.runButton.setText("Run");
        gui.runButton.setSelected(false);

        //gui.saveLinksToFile();
/*        while(executor.getActiveCount() > 0){
            Thread.sleep(100);
            int activeCount = executor.getActiveCount();
            if(executor.getActiveCount() == 0 && gui.runButton.isSelected()){
                executor.shutdown();
            }
        }

 */
        return null;
    }

    public void addTask(Runnable task){
        executor.execute(task);
    }
}

class DoLinks implements Runnable{
    int level;
    String url;
    WebCrawler gui;
    CrawlWorker crawler;

    public DoLinks(int level, String url, WebCrawler gui, CrawlWorker crawler){
        this.level = level;
        this.url = url;
        this.gui = gui;
        this.crawler = crawler;
        this.crawler.increaseOpenThreads();
    }

    @Override
    public void run() {
        try {
            if(gui.checkDoubleLink(url)) {
                if(gui.checkLevel(level)) {
                    WebPage page = new WebPage(url);
                    String title = page.getTitle();
                    gui.increasePages();
                    if (!title.isEmpty()) {
                        gui.addRow(new Object[]{url, title});
                        for (String link : page.getLinks()) {
//                    gui.addRow(new Object[]{link, ""});
                            crawler.addTask(new DoLinks(level + 1, link, gui, crawler));
                        }
                    }
                }
            }
        }catch (Exception e){

        }
        crawler.decreaseOpenThreads();
    }
}

