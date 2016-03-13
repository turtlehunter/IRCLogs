package me.urielsalis.ircLogs;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * me.urielsalis.ircLogs - uriel IRCLogs 13/3/2016
 */
public class Main {

    public static Main main;

    private File allDir;
    private File storeDir;
    private File original;

    private ArrayList<File> filenames = new ArrayList<File>();
    private HashMap<String, File> userAllFolders = new HashMap<String, File>();
    private HashMap<String, File> userNetworkChannel = new HashMap<String, File>();

    private SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
    private SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy");


    public static void main(String[] args) {
        main = new Main();
    }

    public Main() {
        createDirs();
        addFiles();
        run();
    }


    private void createDirs() {
        allDir = new File("all");
        if(!allDir.exists()) allDir.mkdir();
        storeDir = new File("logs");
        if(!storeDir.exists()) storeDir.mkdir();
        original = new File("original");
        if(!original.exists()) {
            System.err.println("No logs to process");
            System.exit(1);
        }
    }

    private void addFiles() {
        for (final File fileEntry : original.listFiles()) {
            if(!fileEntry.isDirectory()) filenames.add(fileEntry);
        }
    }

    private void run() {
        for(final File file: filenames) {
            String name = file.getName();
            String parts[] = name.split("_");
            String user = parts[0];
            String network = parts[1];
            String channel = parts[2];
            String day = parts[3].replace(".log", "");

            if(!userAllFolders.containsKey(user)) {
                File temp = new File(allDir, user);
                if(!temp.exists()) temp.mkdir();
                userAllFolders.put(user, temp);
            }
            try {
                Files.copy(file.toPath(), new File(userAllFolders.get(user), network + " - " + channel + " - " + day + ".log").toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(!userNetworkChannel.containsKey(user+network+channel)) {
                File temp = new File(storeDir, network);
                if(!temp.exists()) temp.mkdir();
                File temp2 = new File(temp, user);
                if(!temp2.exists()) temp2.mkdir();
                File temp3 = new File(temp2, channel);
                if(!temp3.exists()) temp3.mkdir();
                userNetworkChannel.put(user+network+channel, temp3);
            }
            try {
                Files.copy(file.toPath(), new File(userNetworkChannel.get(user+network+channel), parse(day) + ".log").toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private String parse(String day) {
        try {
            Date date = format.parse(day);
            return output.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

}
