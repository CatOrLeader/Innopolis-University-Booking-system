package mockJavaServer;

import APIWrapper.json.BookRoomError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

class Server {
    private final static int BUFFER_SIZE = 256;
    private AsynchronousServerSocketChannel server;
    private final HttpHandler handler;

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final Database database = new Database();

    public Server(HttpHandler handler) throws IOException {
        this.handler = handler;

        updateJson(gson.toJson(new DatabaseJSON(database.rooms, database.freeRooms, database.bookings)));
    }

    public void bootstrap() {
        try {
            server = AsynchronousServerSocketChannel.open();
            server.bind(new InetSocketAddress("127.0.0.1", 3000));

            while (true) {
                Future<AsynchronousSocketChannel> future = server.accept();
                handleClient(future);
            }
        } catch (IOException | ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleClient(Future<AsynchronousSocketChannel> future)
            throws ExecutionException, InterruptedException, IOException {
        System.out.println("new client connection");

        AsynchronousSocketChannel clientChannel = future.get();

        while (clientChannel != null && clientChannel.isOpen()) {
            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
            StringBuilder builder = new StringBuilder();
            boolean keepReading = true;

            while (keepReading) {
                int readResult = clientChannel.read(buffer).get();

                keepReading = readResult == BUFFER_SIZE;
                buffer.flip();
                CharBuffer charBuffer = StandardCharsets.UTF_8.decode(buffer);
                builder.append(charBuffer);

                buffer.clear();
            }

            HttpRequest request = new HttpRequest(builder.toString());
            HttpResponse response = new HttpResponse();

            if (handler != null ) {
                try {
                    String body = this.handler.handle(request, response, database);

                    if (body != null && !body.isBlank()) {
                        if (response.getHeaders().get("Content-Type") == null) {
                            response.addHeader("Content-Type", "application/json; charset=utf-8");
                        }
                        response.setBody(body);
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                    response.setStatusCode(500);
                    response.setStatus("Internal server error");
                    response.getHeaders().put("Content-Type", "application/json; charset=utf-8");

                    BookRoomError error = new BookRoomError("Wrong booking ID");
                    response.setBody(gson.toJson(error));
                }
            } else {
                response.setStatusCode(404);
                response.setStatus("Not Found");
                response.addHeader("Content-Type", "text/html; charset=utf-8");
                response.setBody("<html><body><h1>Resources not found</h1></body></html>");
            }

            ByteBuffer serverResponse = ByteBuffer.wrap(response.getBytes());
            clientChannel.write(serverResponse);

            clientChannel.close();
        }
    }

    private void updateJson(String body) throws IOException {
        if (body == null) return;

        BufferedWriter writer = new BufferedWriter(
                new FileWriter("src/main/java/mockTestingForDevs/database.json"));
        writer.write(body);
        writer.close();
    }
}
