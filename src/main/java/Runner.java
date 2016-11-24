import org.docopt.Docopt;

import java.util.Map;

public class Runner {
    static final String doc =
            "Usage: \n"
                    + "tftp [-h] [-v | --verbose] [user@]host\n"
                    + "tftp [-h] [-v | --verbose] -s <port> [-u <file> | --users <file>]\n"
                    + "\n"
                    + "Options:\n"
                    + "-h --help                  show this\n"
                    + "-s <port>                  server mode\n"
                    + "-u <file>, --users <file>  credentials file\n"
                    + "-v --verbose               print more text\n"
                    + "\n";

    public static void main(String[] args) {
        Map<String, Object> opts = new Docopt(doc).withVersion("TFTP 0.1").parse(args);
        System.out.println(opts);

        if(opts.get("s").equals(true)) {
            runServer(opts);
        } else {
            runClient(opts);
        }
    }

    public static void runServer(Map<String, Object> opts) {
        int port = (Integer) opts.get("port");
        String file =
        Server server = new Server(port);
    }

    public static void runClient(Map<String, Object> opts) {
        opts.get()
    }
}
