import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import jakarta.servlet.annotation.*;

@WebServlet(name = "mainServlet", value = "/main-servlet")
public class MainServlet extends HttpServlet {
    private DatabaseConnector connector;
    @Override
    public void init(ServletConfig config) throws ServletException {
        this.connector = DatabaseConnector.getInstance();
        try {
            this.countWords(this.getFiles());
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
        super.init(config);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String word = req.getParameter("word");
        Map<String, Integer> data = connector.getData(word.toLowerCase());
        req.setAttribute("files", data);
        req.setAttribute("word", word.toLowerCase());
        req.getRequestDispatcher("/index.jsp").forward(req, resp);
    }

    private List<Path> getFiles() throws URISyntaxException, IOException {
        URI filesURI = Objects.requireNonNull(this.getClass().getClassLoader().getResource("/files")).toURI();
        return Files.list(Paths.get(filesURI))
                    .filter(Files::isRegularFile)
                    .collect(Collectors.toList());
    }

    private void countWords(List<Path> paths) {
        paths.forEach(path -> {
            Map<String, Long> result = parseFile(path);
            addWords(path.toString(), result);
        });
    }

    private Map<String, Long> parseFile(Path path) {
        try (Stream<String> lines = Files.lines(path, StandardCharsets.UTF_8)) {
            return lines.parallel().flatMap(line -> Stream.of(line.split(" ")))
                    .map(word -> word.replaceAll("\\p{Punct}", ""))
                    .filter(word -> !word.chars().allMatch(Character::isWhitespace))
                    .collect(Collectors.groupingBy(String::toLowerCase, Collectors.counting()));
        } catch (IOException ignored) {
            return new HashMap<>();
        }
    }

    private void addWords(String path, Map<String, Long> data) {
        data.forEach((word, count) -> connector.insertWord(word, count, path));
    }
}