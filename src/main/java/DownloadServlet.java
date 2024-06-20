import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DownloadServlet extends HttpServlet {
    private int BUFFER_SIZE = 1024;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();

        response.setHeader("content-disposition", "attachment;");

        InputStream inputStream = new FileInputStream(path);
        OutputStream outputStream = response.getOutputStream();

        byte[] buffer = new byte[BUFFER_SIZE];

        while(true) {
            int len = inputStream.read(buffer);

            if(len == -1) {
                inputStream.close();
                break;
            }

            outputStream.write(buffer,0, len);
        }

        outputStream.flush();
    }
}