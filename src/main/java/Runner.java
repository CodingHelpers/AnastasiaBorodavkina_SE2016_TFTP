import org.docopt.Docopt;
import org.omg.CORBA.INTERNAL;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.file.NoSuchFileException;
import java.util.Map;
import java.util.logging.Logger;

public class Runner {
    private final static Logger LOGGER = Logger.getLogger(Runner.class.getName());
    private final static String USERS_DB_FILENAME = "users.txt";

    static final String doc =
            "Usage: "
                    + "tftp [-h] [-v | --verbose] (<host> <port> [--login <login> --password <passwd>] | -s <port> [-u <file> | --users <file>])\n"
                    + "\n"
                    + "Options:\n"
                    + "-h --help                  show this\n"
                    + "-s <port>                  server mode\n"
                    + "-u <file>, --users <file>  credentials file\n"
                    + "-v --verbose               print more text\n"
                    + "\n";

    public static void main(String[] args) {
        System.setProperty("java.net.preferIPv4Stack", "true");
        Map<String, Object> opts = new Docopt(doc).withVersion("TFTP 0.1").parse(args);
        System.out.println(opts.toString());

        if(opts.get("-s").equals(true)) {
            runServer(opts);
        } else {
            runClient(opts);
        }
    }

    public static void runServer(Map<String, Object> opts) {
        int port = Integer.parseInt((String) opts.get("<port>"));
        String file = (String) opts.get("<file>");
        ClientDB clients = null;

        try {
            clients = new ClientDB(file);
        } catch (FileNotFoundException | NullPointerException e) {
            LOGGER.warning(file + " not found. Falling back to default");
        }

        if(clients == null) {
            try {
                clients = new ClientDB(USERS_DB_FILENAME);
            } catch (FileNotFoundException e) {
                LOGGER.severe("Default file " + USERS_DB_FILENAME + " not found. \nFalling back to no-authorization mode.");
            }
        }

        Server server = new Server(port, clients);
        try {
            server.listen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void runClient(Map<String, Object> opts) {
        String host = (String) opts.get("<host>");
        Integer port = Integer.parseInt((String) opts.get("<port>"));
        String login = (String) opts.get("<login>");
        String passwd = (String) opts.get("<passwd>");

        InetAddress addr = null;
        try {
            addr = InetAddress.getByName(host);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.exit(1);
        }

        try {
            Client client = new Client(addr, port, login, passwd);
          //  client.readFile("./testdata/vim.txt",
          //                  "./testdata/recv_vim.txt");
            client.writeFile("./testdata/vim.txt",
                             "./testdata/sent_vim.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
