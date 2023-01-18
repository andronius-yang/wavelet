import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

class Handler implements URLHandler {
    // The one bit of state on the server: a number that will be manipulated by
    // various requests.
    ArrayList<String> entries;

    public String handleRequest(URI url) {
        if(entries == null){
            entries = new ArrayList<String>();
        }
        if (url.getPath().equals("/")) {
            return "This is a search engine, please use /add path to add an entry and /search path to search for a substring.";
        } else if (url.getPath().equals("/add")) {
            String[] parameters = url.getQuery().split("=");
            if(parameters[0].equals("s")){
                if(!entries.contains(parameters[1])){
                    entries.add(parameters[1]);
                    return "Entry " + parameters[1] + " successfully added";
                }else{
                    return "Error, entry " + parameters[1] + " already exists!";
                }
            }
            return "Error, please include '?s=desiredObject' after path!";
        } else if(url.getPath().equals("/search")){
            String[] parameters = url.getQuery().split("=");
            if(parameters[0].equals("s")){
                String query = parameters[1];
                ArrayList<String> results = new ArrayList<>();
                for(String s:entries){
                    if(s.contains(query)){
                        results.add(s);
                    }
                }
                if(results.size() > 0){
                    return "The following matches the query: " + results;
                }else{
                    return "Failed, no such match for query " + query;
                }
            }
            return "Error, please include '?s=desiredObject' after path!";
        } 
        else {
            System.out.println("Path: " + url.getPath());
            return "404 Not Found!";
        }
    }
}

class SearchEngine {
    public static void main(String[] args) throws IOException {
        if(args.length == 0){
            System.out.println("Missing port number! Try any number between 1024 to 49151");
            return;
        }

        int port = Integer.parseInt(args[0]);
        Server.start(port, new Handler());
    }
}
